package lib;

import lib.comparators.Distanceator;

import lib.tilers.Tiler;

import lib.transformations.ImageTransform;
import lib.transformations.ScaleTransform;

import lib.utils.Utils;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * fractal compressor instance. combines the tiler and
 * comparator classes to create a fractal image model
 * 
 * NOTE: the current compressor cannot handle scale transforms
 */
public class Compressor {

    private ScaleTransform              scaleTransform;
    private Tiler<BufferedImage>        tiler;
    private Distanceator<BufferedImage> comparator;
    private Set<ImageTransform>         transforms;
    private Set<BufferedImageOp>        filters;

    /**
     * 
     * NOTE: the current compressor cannot handle scale transforms
     * 
     * @param scaleTransform the scale difference between the ranges and the domains
     * @param tiler the tiler used to tile the image
     * @param comparator the comparator used to compare the tiles of the image
     * @param transforms a list of transform to apply to the tiles of the image
     * @throws NullPointerException if any field is null
     */
    public Compressor(final ScaleTransform scaleTransform,
                      final Tiler<BufferedImage> tiler,
                      final Distanceator<BufferedImage> comparator,
                      final Set<ImageTransform> transforms) throws NullPointerException {
        this(scaleTransform, tiler, comparator, transforms, new HashSet<BufferedImageOp>(0));
    }

    /**
     * 
     * NOTE: the current compressor cannot handle scale transforms
     * 
     * @param scaleTransform the scale difference between the ranges and the domains
     * @param tiler the tiler used to tile the image
     * @param comparator the comparator used to compare the tiles of the image
     * @param transforms a list of transform to apply to the tiles of the image
     * @param filter a filter to apply to the image for normalization
     * @throws NullPointerException if any field is null
     */
    public Compressor(final ScaleTransform scaleTransform,
                      final Tiler<BufferedImage> tiler,
                      final Distanceator<BufferedImage> comparator,
                      final Set<ImageTransform> transforms,
                      final BufferedImageOp filter) throws NullPointerException {
        this(scaleTransform, tiler, comparator, transforms, new HashSet<BufferedImageOp>(1) {{ add(filter); }});
    }

    /**
     * 
     * NOTE: the current compressor cannot handle scale transforms
     * 
     * @param scaleTransform the scale difference between the ranges and the domains
     * @param tiler the tiler used to tile the image
     * @param comparator the comparator used to compare the tiles of the image
     * @param transforms a set of transform to apply to the tiles of the image
     * @param filters a set of filters to apply to the image for normalization
     * @throws NullPointerException if any field is null
     */
    public Compressor(final ScaleTransform scaleTransform,
                      final Tiler<BufferedImage> tiler,
                      final Distanceator<BufferedImage> comparator,
                      final Set<ImageTransform> transforms,
                      final Set<BufferedImageOp> filters) throws NullPointerException {

        if ((scaleTransform == null) || (tiler == null) || (comparator == null) || 
                (transforms == null) || (filters == null)) {
            throw new NullPointerException("Null elements not allowed");
        }

        this.comparator = comparator;
        this.tiler      = tiler;
        this.transforms = transforms;
        this.filters    = filters;

        /*
         * The 'None' transform -- makes no change to the given image.
         * This is useful to traverse the transforms without special casing
         * the comparison with the normal(non-transformed) image.
         */
        this.transforms.add(new ImageTransform() {

            @Override
            public BufferedImage transform(BufferedImage inputimage) {
                return inputimage;
            }
        });
    }

    /**
     * Compress a given image. Compressions takes place as a mapping of
     * small images and transforms to points. Applying the transforms
     * to the images and placing the resulted transformed images
     * to the mapped points, the original image is reassembled.
     * 
     * NOTE: the current compressor cannot handle scale transforms
     * 
     * @param image the image to compress
     * @return a mapping of points to images and transforms.
     */
    public Map<Point, Map.Entry<BufferedImage, ImageTransform>> compress(BufferedImage image) {

        /*
         * Normalization. Before tiling the image, pass it throw a set of filters.
         * This might improve results, if used wisely.
         */
        for (BufferedImageOp filter : filters) {
            image = filter.filter(image, null);
        }

        /*
         * range blocks are the tiles of the original(but filtered) image.
         */
        ArrayList<BufferedImage> rangeblocks = tiler.tile(image);

        /*
         * the domain image to tile, is a scaled factor of the size 
         * of the original image (scalex * width; scaley * height).
         * domain and range blocks have the same size, but, domain
         * blocks are less in number than range blocks.
         *
         *     #domains = (scalex * w) * (scaley * h)
         *     #domains = scalex * scaley * #ranges
         *
         * We pre-calculate all transforms to hold all possible
         * transformed and original domain blocks.
         *
         *     #alldomains = #transforms * scalex * scaley * #ranges
         *
         * We map each result of a transform with the original
         * domain and transform operation which resulted it.
         */
        Map<BufferedImage, Map.Entry<BufferedImage, ImageTransform>> domainblocks =
            new HashMap<BufferedImage, Map.Entry<BufferedImage, ImageTransform>>(
                (int) (transforms.size() * rangeblocks.size() * scaleTransform.getScalex() * scaleTransform.getScaley()));

        for (BufferedImage domainImg : tiler.tile(scaleTransform.transform(image))) {
            for (ImageTransform transform : transforms) {
                domainblocks.put(transform.transform(domainImg), 
                                 new SimpleEntry<BufferedImage, ImageTransform>(domainImg, transform));
            }
        }

        /* A mapping between a range and most suitable domain block */
        Map<BufferedImage, BufferedImage> rangeDomainMatches = 
            new HashMap<BufferedImage, BufferedImage>(rangeblocks.size());

        /*
         * After the end of each domain loop, or in other words, before
         * we iterate to the next range image, we have found the best
         * match for the current (and all previous) range images.
         * Thus, we do not need to hold a mapping between the range image
         * and the domain image along with its distance. We hold the best
         * (minimum) distance found so far, in the a variable, which is
         * reset in every range image iteration.
         */
        for (BufferedImage rangeblock : rangeblocks) {
            double mindiff = Double.MAX_VALUE;

            for (BufferedImage domainblock : domainblocks.keySet()) {
                double diff = comparator.distance(rangeblock, domainblock);

                /*
                 * If we haven't seen the image before (which means
                 * this is a new range image iteration), we store
                 * the distance and map the range and domain images.
                 * If we've seen the image before, but the comparison
                 * yeilds better results (the new difference is smaller
                 * than the best (minimum) so far), we update the best
                 * difference and map the range to the new domain image.
                 */
                if (!rangeDomainMatches.containsKey(rangeblock) || (mindiff > diff)) {
                    rangeDomainMatches.put(rangeblock, domainblock);
                    mindiff = diff;
                }
            }
        }

        /*
         * All matches are found. We now need the original domain block(1),
         * the transform(2) needed to be applied to get the range image and
         * a point(3) which represents the position of the range image in
         * the original image.
         * We map each point to a corresponding domain image and transform.
         */
        Map<Point, Map.Entry<BufferedImage, ImageTransform>> results = 
            new HashMap<Point, Map.Entry<BufferedImage, ImageTransform>>(rangeblocks.size());
        int width = image.getWidth();

        for (BufferedImage range : rangeDomainMatches.keySet()) {
            results.put(Utils.indexToPoint(rangeblocks.indexOf(range), width),
                        domainblocks.get(rangeDomainMatches.get(range)));
        }

        return results;
    }
}

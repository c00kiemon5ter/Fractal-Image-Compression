package lib;

import lib.comparators.Distanceator;

import lib.tilers.Tiler;

import lib.transformations.ImageTransform;
import lib.transformations.ScaleTransform;

import lib.utils.Utils;

import java.awt.Point;
import java.awt.image.BufferedImage;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * fractal compressor instance. combines the tiler and
 * comparator classes to create a fractal image model
 */
public class Compressor {

    private Distanceator<BufferedImage> comparator;
    private Tiler<BufferedImage>        tiler;
    private Set<ImageTransform>         transforms;

    public Compressor(Tiler<BufferedImage> tiler,
                      Distanceator<BufferedImage> comparator,
                      Set<ImageTransform> transforms) {
        this.comparator = comparator;
        this.tiler      = tiler;
        this.transforms = transforms;

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
     * TODO: normalize (eg. Blur) the image ?
     * TODO: handle scale transform -- subimage search ?
     * 
     * Compress a given image. Compressions takes place as a mapping of 
     * small images and transforms to points. Applying the transforms
     * to the images and placing the resulted transformed images
     * to the mapped points, the original image is reassembled.
     * 
     * @param image the image to compress
     * @return a mapping of points to images and transforms. 
     */
    public Map<Point, Map.Entry<BufferedImage, ImageTransform>> compress(BufferedImage image) {
        ArrayList<BufferedImage> rangeblocks = tiler.tile(image);

        /*
         * the domain image to tile, is half the size (half the width
         * and half the height) of the original image.
         * domain and range blocks have the same size, but, domain
         * blocks are less in number than range blocks.
         *
         *     #domains = (1/2 * w) * (1/2 * h) = 1/4 * #ranges
         *
         * We pre-calculate all transforms to hold all possible
         * transformed and original domain blocks.
         *
         *     #domains = #transforms * 1/4 * #ranges
         *
         * We map each result of a transform with the original
         * domain and transform operation which resulted it.
         */
        Map<BufferedImage, Map.Entry<BufferedImage, ImageTransform>> domainblocks = new HashMap<BufferedImage, Map.Entry<BufferedImage, ImageTransform>>(transforms.size() * rangeblocks.size() / 4);

        for (BufferedImage domainImg : tiler.tile(new ScaleTransform(.5, .5).transform(image))) {
            for (ImageTransform transform : transforms) {
                domainblocks.put(transform.transform(domainImg), new SimpleEntry<BufferedImage, ImageTransform>(domainImg, transform));
            }
        }

        /* A mapping between a range and most suitable domain block */
        Map<BufferedImage, BufferedImage> rangeDomainMatches = new HashMap<BufferedImage, BufferedImage>(rangeblocks.size());

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
         * a point(3) which represents the position of the range image.
         * We map each point to a corresponding domain image and transform.
         */
        Map<Point, Map.Entry<BufferedImage, ImageTransform>> results = new HashMap<Point, Map.Entry<BufferedImage, ImageTransform>>(rangeblocks.size());
        int width = image.getWidth();

        for (BufferedImage range : rangeDomainMatches.keySet()) {
            results.put(Utils.indexToPoint(rangeblocks.indexOf(range), width), domainblocks.get(rangeDomainMatches.get(range)));
        }
        
        return results;
    }
}

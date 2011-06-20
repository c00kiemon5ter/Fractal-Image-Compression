package lib;

import lib.comparators.Distanceator;

import lib.tilers.Tiler;

import lib.transformations.ImageTransform;
import lib.transformations.ScaleTransform;

import java.awt.image.BufferedImage;

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
     * TODO: how do we get the transform ? map it ?
     * TODO: handle scale transform -- subimage search ?
     *
     * @param image
     */
    public void compress(BufferedImage image) {
        ArrayList<BufferedImage> ranges = tiler.tile(image);

        /*
         * the domain image to tile, is half the size (half the width
         * and half the height) of the original image.
         * domain and range blocks have the same size, but, domain
         * blocks are less in number than range blocks.
         *
         *     #domains = (1/2 * w) * (1/2 * h) = 1/4 * #ranges
         *
         * Here we pre-calculate all transforms. domains list holds
         * all possible transformed and original domain blocks.
         *
         *     #domains = #transforms * 1/4 * #ranges
         */
        ArrayList<BufferedImage> domains = new ArrayList<BufferedImage>(transforms.size() * ranges.size() / 4);

        for (BufferedImage domainImg : tiler.tile(new ScaleTransform(.5, .5).transform(image))) {
            for (ImageTransform transform : transforms) {
                domains.add(transform.transform(domainImg));
            }
        }

        /* A mapping between a range and most suitable domain block */
        Map<BufferedImage, BufferedImage> mapping = new HashMap<BufferedImage, BufferedImage>(ranges.size());

        /*
         * After the end of each domain loop, or in other words, before
         * we iterate to the next range image, we have found the best
         * match for the current (and all previous) range images.
         * Thus, we do not need to hold a mapping between the range image
         * and the domain image along with its distance. We hold the best
         * (minimum) distance found so far, in the a variable, whichi is
         * reset in every range image iteration.
         */
        for (BufferedImage rangeimg : ranges) {
            double mindiff = Double.MAX_VALUE;

            for (BufferedImage domainimg : domains) {
                double diff = comparator.distance(rangeimg, domainimg);

                /*
                 * If we haven't seen the image before (which means
                 * this is a new range image iteration), we store
                 * the distance and map the range and domain images.
                 * If we've seen the image before, but the comparison
                 * yeilds better results (the new difference is smaller
                 * than the best (minimum) so far), we update the best
                 * difference and map the range to the new domain image.
                 */
                if (!mapping.containsKey(rangeimg) || (mindiff > diff)) {
                    mindiff = diff;
                    mapping.put(rangeimg, domainimg);
                }
            }
        }

        /*
         * TODO: Reverse map the domain blocks to a list of ranges' points
         */
    }
}

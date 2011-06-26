package lib.comparators;

import java.awt.image.BufferedImage;

/**
 * an image comparator returns the distance between two images
 */
public class ImageComparator implements Distanceator<BufferedImage> {

    private final double fuzz;
    private final Metric metric;

    public ImageComparator(Metric distanceMetric) {
        this(distanceMetric, 0);
    }

    public ImageComparator(final Metric distanceMetric, final double fuzz) {
        assert (distanceMetric != null) && (fuzz >= 0);

        this.metric = distanceMetric;
        this.fuzz   = fuzz;
    }

    @Override
    public double distance(final BufferedImage img1, final BufferedImage img2) {
        assert (img1 != null) && (img2 != null);

        int width  = img1.getWidth();
        int height = img1.getHeight();
        int area   = width * height;

        int[] img1pixels = new int[area];
        int[] img2pixels = new int[area];
        img1.getRGB(0, 0, width, height, img1pixels, 0, 0);
        img2.getRGB(0, 0, width, height, img2pixels, 0, 0);

        double distance = 0;

        for (int pixelrow = 0; pixelrow < height; pixelrow++) {
            for (int pixelcol = 0; pixelcol < width; pixelcol++) {
                distance += metric.distance(img1pixels[pixelrow * width + pixelcol], 
                                            img2pixels[pixelrow * width + pixelcol], 
                                            fuzz);
            }
        }

        return distance;
    }
}

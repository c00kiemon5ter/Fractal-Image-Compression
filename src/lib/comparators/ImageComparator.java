package lib.comparators;

import java.awt.image.BufferedImage;

/**
 * an image comparator returns the distance between two images
 */
public class ImageComparator implements Distanceator<BufferedImage> {

	private final Metric metric;
	private final double fuzz;

	public ImageComparator(Metric distanceMetric) {
		this(distanceMetric, 0);
	}
	
	public ImageComparator(Metric distanceMetric, double fuzz) {
		this.metric = distanceMetric;
		this.fuzz   = fuzz;
	}

	@Override
	public double distance(BufferedImage img1, BufferedImage img2) {
		double distance = 0;
		int width  = img1.getWidth();
		int height = img1.getHeight();
		int area   = width * height;
		
		int[] img1pixels = new int[area];
		int[] img2pixels = new int[area];
		img1.getRGB(0, 0, width, height, img1pixels, 0, 0);
		img2.getRGB(0, 0, width, height, img2pixels, 0, 0);
		
		for (int pixelrow = 0; pixelrow < height; pixelrow++) {
			for (int pixelcol = 0; pixelcol < width; pixelcol++) {
				int pixel1 = img1pixels[pixelrow * width + pixelcol];
				int pixel2 = img1pixels[pixelrow * width + pixelcol];
				// TODO: use fuzz
				distance += metric.distance(pixel1, pixel2);
			}
		}
		
		return distance;
	}
}

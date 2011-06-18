package lib.comparators;

import java.awt.image.BufferedImage;

/**
 * an image comparator returns the distance between two images
 */
public class ImageComparator implements Distanceable<BufferedImage> {

	final static int MASK = 0xff;
	
	final static int ALPHA_SHIFT = 24; // int alpha = (pixel >> ALPHA_SHIFT) & MASK;
	final static int RED_SHIFT   = 16; // int red   = (pixel >> RED_SHIFT  ) & MASK;
	final static int GREEN_SHIFT =  8; // int green = (pixel >> GREEN_SHIFT) & MASK;
									   // int blue  = pixel /*no blue shift*/ & MASK;
	
	final static int ALPHA_MASK = 0xff000000; // int alpha = (pixel & ALPHA_MASK) >> ALPHA_SHIFT;
	final static int RED_MASK   = 0x00ff0000; // int red   = (pixel & RED_MASK  ) >> RED_SHIFT;
	final static int GREEN_MASK = 0x0000ff00; // int green = (pixel & GREEN_MASK) >> GREEN_SHIFT;
	final static int BLUE_MASK  = 0x000000ff; // int blue  =  pixel & BLUE_MASK;
	
	private final Metric metric;
	private final double fuzz;

	public ImageComparator(Metric distanceMetric) {
		this(distanceMetric, 0);
	}
	
	public ImageComparator(Metric distanceMetric, double fuzz) {
		this.metric = distanceMetric;
		this.fuzz = fuzz;
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
				distance += metric.distance(pixel1, pixel2);
			}
		}
		
		return distance;
	}

	public static int getAlpha(int pixel) {
		return (pixel >> ALPHA_SHIFT) & MASK;
	}

	public static int getRed(int pixel) {
		return (pixel >> RED_SHIFT) & MASK;
	}

	public static int getGreen(int pixel) {
		return (pixel >> GREEN_SHIFT) & MASK;
	}

	public static int getBlue(int pixel) {
		return pixel & MASK;
	}
}

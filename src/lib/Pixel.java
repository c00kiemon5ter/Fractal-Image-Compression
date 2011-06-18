package lib;

import lib.comparators.ImageComparator;

/**
 * a pixel with color channels
 */
public class Pixel {

	private int pixel;
	private int red;
	private int green;
	private int blue;

	public Pixel(int pixel) {
		this.pixel = pixel;
		this.red   = ImageComparator.getRed(pixel);
		this.green = ImageComparator.getGreen(pixel);
		this.blue  = ImageComparator.getBlue(pixel);
	}

	public int pixel() {
		return this.pixel;
	}

	public int red() {
		return this.red;
	}

	public int green() {
		return this.green;
	}

	public int blue() {
		return this.blue;
	}
}

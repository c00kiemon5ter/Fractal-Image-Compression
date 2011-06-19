package lib.utils;

/**
 * useful functions to manipulate pixels
 * 
 * @see BufferedImage#getRGB(int, int) 
 * @see BufferedImage#getRGB(int, int, int, int, int[], int, int) 
 */
public class PixelUtils {

	final static int MASK = 0xff;
	
	final static int ALPHA_SHIFT = 24; // int alpha = (pixel >> ALPHA_SHIFT) & MASK;
	final static int RED_SHIFT   = 16; // int red   = (pixel >> RED_SHIFT  ) & MASK;
	final static int GREEN_SHIFT =  8; // int green = (pixel >> GREEN_SHIFT) & MASK;
									   // int blue  = pixel /*no blue shift*/ & MASK;
	
	final static int ALPHA_MASK = 0xff000000; // int alpha = (pixel & ALPHA_MASK) >> ALPHA_SHIFT;
	final static int RED_MASK   = 0x00ff0000; // int red   = (pixel & RED_MASK  ) >> RED_SHIFT;
	final static int GREEN_MASK = 0x0000ff00; // int green = (pixel & GREEN_MASK) >> GREEN_SHIFT;
	final static int BLUE_MASK  = 0x000000ff; // int blue  =  pixel & BLUE_MASK;

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

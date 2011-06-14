package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * functor class to scale an image
 */
public class ScaleTransform extends ImageTransform {

	private double scalex, scaley;

	/**
	 * 
	 * @param scalex the factor by which coordinates are scaled along the X axis direction
	 * @param scaley the factor by which coordinates are scaled along the Y axis direction
	 */
	public ScaleTransform(double scalex, double scaley) {
		this.scalex = scalex;
		this.scaley = scaley;
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		return ImageTransform.scale(inputimage, scalex, scaley);
	}
}

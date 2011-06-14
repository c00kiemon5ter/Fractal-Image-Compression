package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * functor class to shear an image
 */
public class ShearTransform extends ImageTransform {

	private double shearx, sheary;

	/**
	 * 
	 * @param shearx the multiplier by which coordinates are shifted in the 
	 * direction of the positive X axis as a factor of their Y coordinate
	 * @param sheary the multiplier by which coordinates are shifted in the 
	 * direction of the positive Y axis as a factor of their X coordinate
	 */
	public ShearTransform(double shearx, double sheary) {
		this.shearx = shearx;
		this.sheary = sheary;
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		return ImageTransform.shear(inputimage, shearx, sheary);
	}
}

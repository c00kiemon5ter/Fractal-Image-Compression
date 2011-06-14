package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * functor class to rotate an image by the given degrees
 */
public class RotateTransform extends ImageTransform {

	private double degrees;

	/**
	 * 
	 * @param degrees the angle of rotation measured in degrees
	 */
	public RotateTransform(double degrees) {
		this.degrees = degrees;
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		return ImageTransform.rotateByDegrees(inputimage, degrees);
	}
}

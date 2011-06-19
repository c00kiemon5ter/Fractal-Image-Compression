package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * functor class to rotate an image by the given degrees
 */
public class AffineRotateTransform extends ImageTransform {

	private double degrees;
	private int interpolationType;

	public AffineRotateTransform(double degrees, int interpolationType) {
		this.degrees = degrees;
		this.interpolationType = interpolationType;
	}

	public AffineRotateTransform(double degrees) {
		this(degrees, AffineTransformOp.TYPE_BILINEAR);
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		return affineTransform(inputimage, AffineTransform.getRotateInstance(
				Math.toRadians(degrees), inputimage.getWidth()  / 2,
				                         inputimage.getHeight() / 2), interpolationType);
	}
}

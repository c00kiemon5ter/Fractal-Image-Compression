package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * functor class to affineShear an image
 */
public class AffineShearTransform extends ImageTransform {

	private double shearx, sheary;
	private int interpolationType;

	/**
	 * 
	 * @param shearx the multiplier by which coordinates are shifted in the 
	 * direction of the positive X axis as a factor of their Y coordinate
	 * @param sheary the multiplier by which coordinates are shifted in the 
	 * direction of the positive Y axis as a factor of their X coordinate
	 * @param interpolationType  
	 */
	public AffineShearTransform(final double shearx, final double sheary, final int interpolationType) {
		this.shearx = shearx;
		this.sheary = sheary;
		this.interpolationType = interpolationType;
	}

	public AffineShearTransform(final double shearx, final double sheary) {
		this(shearx, sheary, AffineTransformOp.TYPE_BILINEAR);
	}

	@Override
	public BufferedImage transform(final BufferedImage inputimage) {
		return affineTransform(inputimage, AffineTransform.getShearInstance(shearx, sheary), interpolationType);
	}
}

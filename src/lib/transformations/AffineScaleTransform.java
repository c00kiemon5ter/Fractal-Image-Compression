package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * 
 */
public class AffineScaleTransform extends ImageTransform {

	private double scalex, scaley;
	private int interpolationType;

	public AffineScaleTransform(final double scalex, final double scaley, final int interpolationType) {
		this.scalex = scalex;
		this.scaley = scaley;
		this.interpolationType = interpolationType;
	}

	public AffineScaleTransform(final double scalex, final double scaley) {
		this(scalex, scaley, AffineTransformOp.TYPE_BILINEAR);
	}

	@Override
	public BufferedImage transform(final BufferedImage inputimage) {
		return affineTransform(inputimage, AffineTransform.getScaleInstance(scalex, scaley), interpolationType);
	}
}

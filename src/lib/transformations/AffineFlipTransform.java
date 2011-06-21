package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * 
 */
public class AffineFlipTransform extends ImageTransform {

	private int interpolationType;

	public AffineFlipTransform(final int interpolationType) {
		this.interpolationType = interpolationType;
	}

	public AffineFlipTransform() {
		this(AffineTransformOp.TYPE_BILINEAR);
	}

	@Override
	public BufferedImage transform(final BufferedImage inputimage) {
		AffineTransform transform = new AffineTransform();
		transform.translate(inputimage.getWidth() / 2, inputimage.getHeight() / 2);
		transform.scale(1, -1);
		transform.translate(-inputimage.getWidth() / 2, -inputimage.getHeight() / 2);
		return affineTransform(inputimage, transform, interpolationType);
	}
}

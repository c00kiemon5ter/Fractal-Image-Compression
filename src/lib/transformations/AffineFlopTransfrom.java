package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * 
 */
public class AffineFlopTransfrom extends ImageTransform {

	private int interpolationType;

	public AffineFlopTransfrom(int interpolationType) {
		this.interpolationType = interpolationType;
	}

	public AffineFlopTransfrom() {
		this(AffineTransformOp.TYPE_BILINEAR);
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		AffineTransform transform = new AffineTransform();
		transform.translate(inputimage.getWidth() / 2, inputimage.getHeight() / 2);
		transform.scale(-1, 1);
		transform.translate(-inputimage.getWidth() / 2, -inputimage.getHeight() / 2);
		return affineTransform(inputimage, transform, interpolationType);
	}
}

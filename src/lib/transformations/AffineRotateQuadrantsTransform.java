package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * functor class to rotate an image by the given quadrant
 */
public class AffineRotateQuadrantsTransform extends ImageTransform {

	private int quadrants;
	private int interpolationType;

	/**
	 * @param quadrants the number of 90 degree arcs to rotate by
	 */
	public AffineRotateQuadrantsTransform(int quadrants, int interpolationType) {
		this.quadrants = quadrants;
		this.interpolationType = interpolationType;
	}

	public AffineRotateQuadrantsTransform(int quadrants) {
		this(quadrants, AffineTransformOp.TYPE_BILINEAR);
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		return affineTransform(inputimage, AffineTransform.getQuadrantRotateInstance(
				quadrants, inputimage.getWidth() / 2,
				inputimage.getHeight() / 2), interpolationType);
	}
}

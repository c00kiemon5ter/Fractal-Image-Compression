package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * functor class to rotate an image by the given quadrant
 */
public class RotateQuadrantsTransform extends ImageTransform {

	private int quadrants;

	/**
	 * 
	 * @param quadrants the number of 90 degree arcs to rotate by
	 */
	public RotateQuadrantsTransform(int quadrants) {
		this.quadrants = quadrants;
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		return ImageTransform.rotateByQuadrants(inputimage, quadrants);
	}
}

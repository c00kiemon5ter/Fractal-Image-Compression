package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * functor class to flip an image
 */
public class FlipTransform extends ImageTransform {

	@Override
	public BufferedImage transform(final BufferedImage inputimage) {
		return ImageTransform.flip(inputimage);
	}
}

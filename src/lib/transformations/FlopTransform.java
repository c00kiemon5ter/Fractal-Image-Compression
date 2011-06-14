package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * functor class to flop an image
 */
public class FlopTransform extends ImageTransform {

	@Override
	public BufferedImage transform(final BufferedImage inputimage) {
		return ImageTransform.flop(inputimage);
	}
}

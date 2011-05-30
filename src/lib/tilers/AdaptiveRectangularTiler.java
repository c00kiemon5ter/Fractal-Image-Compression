package lib.tilers;

import java.awt.image.BufferedImage;

/**
 * Adaptive tiler finds its way on how to tile and split
 * the given image. It will determine the best number of
 * rows and columns in which to split the image.
 */
public class AdaptiveRectangularTiler implements Tiler {

	@Override
	public BufferedImage[] tile(BufferedImage image) {
		// TODO: algorithm to determine num of rows and columns
		throw new UnsupportedOperationException("Not supported yet.");
	}
}

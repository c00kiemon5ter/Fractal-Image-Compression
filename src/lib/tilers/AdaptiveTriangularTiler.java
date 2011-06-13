package lib.tilers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Adaptive tiler finds its way on how to tile and split
 * the given image. It will determine the best (best effort)
 * way to split the image in triangles.
 */
public class AdaptiveTriangularTiler implements Tiler {

	@Override
	public ArrayList<BufferedImage> tile(BufferedImage image) {
		// TODO: FUTURE: algorithm to determine triangle splitting 
		throw new UnsupportedOperationException("Not supported yet.");
	}
}

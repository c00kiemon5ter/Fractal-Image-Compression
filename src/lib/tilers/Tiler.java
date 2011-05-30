package lib.tilers;

import java.awt.image.BufferedImage;

/**
 * Tiler interface any kind of tiler should implement
 */
public interface Tiler {

	public BufferedImage[] tile(BufferedImage image);
}

package lib.tilers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Tiler interface any kind of tiler should implement
 */
public interface Tiler {

	/**
	 * Tile the given image according to the chosen tiler
	 * 
	 * @param image the image to tile or split
	 * @return an array holding the split images
	 */
	public ArrayList<BufferedImage> tile(BufferedImage image);
}

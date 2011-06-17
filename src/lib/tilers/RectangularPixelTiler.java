package lib.tilers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Rectangle tiler. Given the pixels in the x and y axis that a 
 * block must be, it tiles the image into as many blocks can fit.
 */
public class RectangularPixelTiler implements Tiler<BufferedImage> {

	private int blockwidth, blockheight;

	/**
	 * @param blockwidth the pixels each block will be in the x axle
	 * @param blockheight the pixels each block will be in the y axle
	 */
	public RectangularPixelTiler(int blockwidth, int blockheight) {
		this.blockwidth = blockwidth;
		this.blockheight = blockheight;
	}

	@Override
	public ArrayList<BufferedImage> tile(BufferedImage image) {
		// normalize image -- adjust size to blocks' width and height
		int cols = image.getWidth() / blockwidth;
		int rows = image.getHeight() / blockheight;
		image = image.getSubimage(0, 0, cols * blockwidth, rows * blockheight);
		// split image
		ArrayList<BufferedImage> blockslist = new ArrayList<BufferedImage>(rows * cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				blockslist.add(image.getSubimage(col * blockwidth, row * blockheight,
												 blockwidth, blockheight));
			}
		}
		return blockslist;
	}
}

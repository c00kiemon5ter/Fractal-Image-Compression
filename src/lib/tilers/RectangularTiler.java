package lib.tilers;

import java.awt.image.BufferedImage;

/**
 * Rectangle tiler. Given the rows and columns, it
 * splits the image into rows x columns rectangles. 
 */
public class RectangularTiler implements Tiler {

	private int rows, cols;

	/**
	 * 
	 * @param rows the rows to tile the image
	 * @param cols the columns to tile the image
	 */
	public RectangularTiler(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}

	@Override
	public BufferedImage[] tile(BufferedImage image) {
		image = adjustImageSizeDown(image, rows, cols);
		int blockheight = image.getHeight() / rows;
		int blockwidth = image.getWidth() / cols;
		BufferedImage[] blocks = new BufferedImage[rows * cols];
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				blocks[y * cols + x] = image.getSubimage(x * blockwidth,
														 y * blockheight,
														 blockwidth,
														 blockheight);
			}
		}
		return blocks;
	}

	/**
	 * Adjust image size such that it can be split by
	 * the given rows and columns.
	 * Adjust takes place by reducing the image size. 
	 * 
	 * @param image the image to split and fit in rows and columns
	 * @param rows the rows to split the image
	 * @param cols the columns to split the image
	 * @return the adjusted image, the image with the correct size
	 */
	private BufferedImage adjustImageSizeDown(BufferedImage image, int rows, int cols) {
		int width = image.getWidth();
		while (width % cols != 0) {
			width--;
		}
		int height = image.getHeight();
		while (height % rows != 0) {
			height--;
		}
		return image.getSubimage(0, 0, width, height);
	}
}

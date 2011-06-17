package lib.tilers;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Adaptive tiler finds its way on how to tile and split
 * the given image. It will determine the best number of
 * rows and columns in which to split the image.
 */
public class AdaptiveRectangularTiler implements Tiler<BufferedImage> {

	private int rows, cols;

	/**
	 * Given the rows and columns tile the image. Rows and columns
	 * will always be equal or more than the initial numbers given.
	 * @param initialRows the initial number of rows
	 * @param initialCols the initial number of columns
	 */
	public AdaptiveRectangularTiler(int initialRows, int initialCols) {
		this.rows = initialRows;
		this.cols = initialCols;
	}

	@Override
	public ArrayList<BufferedImage> tile(BufferedImage image) {
		Point coord = adjustColsRows(image, rows, cols);
		this.rows = coord.x;
		this.cols = coord.y;
		int blockheight = image.getHeight() / rows;
		int blockwidth = image.getWidth() / cols;
		ArrayList<BufferedImage> blockslist = new ArrayList<BufferedImage>(rows * cols);
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				blockslist.add(image.getSubimage(blockwidth * x, blockheight * y,
												 blockwidth, blockheight));
			}
		}
		return blockslist;
	}

	/**
	 * Adjust the number of columns and rows to split the image
	 * so that it accurately fits in the rectangles. 
	 */
	private Point adjustColsRows(BufferedImage image, int rows, int cols) {
		while (image.getHeight() % rows != 0) {
			rows++;
		}
		while (image.getWidth() % cols != 0) {
			cols++;
		}
		return new Point(rows, cols);
	}
}

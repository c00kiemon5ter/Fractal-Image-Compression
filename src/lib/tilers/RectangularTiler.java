package lib.tilers;

import java.awt.image.BufferedImage;

/**
 *
 * @author Periklis Ntanasis
 */
public class RectangularTiler implements Tiler {

	private int blockwidth = 0, blockheight = 0;
	private int blocksmultitude = 0, blockscol = 0, blocksraw = 0;

	public RectangularTiler(int x, int y) {
		blockwidth = x;
		blockheight = y;
	}

	@Override
	public BufferedImage[] tile(BufferedImage image) {

		if (blockscol == 0 && blocksraw == 0
			&& blockwidth % image.getWidth() == 0
			&& blockheight % image.getHeight() == 0) {
			blockscol = image.getWidth() / blockwidth;
			blocksraw = image.getHeight() / blockheight;
			blocksmultitude = image.getWidth() / blockwidth * image.getHeight() / blockheight;
		} else if (blocksraw != 0 && blockscol != 0) {
			blockwidth = image.getWidth() / blockscol;
			blockheight = image.getHeight() / blocksraw;
			blocksmultitude = blocksraw * blockscol;
			System.out.println(image.getWidth() + " " + image.getHeight());
		}

		System.out.println(blockwidth + " " + blockheight);

		BufferedImage[] blocks = new BufferedImage[blocksmultitude];
		int count = 0;
		for (int x = 0; x < blocksraw && (x + 1) * blockheight < image.getHeight(); x++) {
			for (int y = 0; y < blockscol && ((y + 1) * blockwidth < image.getWidth()); y++) {
				blocks[count++] = image.getSubimage(x * blockheight, y * blockwidth, blockwidth, blockheight);
			}
		}

		return blocks;
	}
}

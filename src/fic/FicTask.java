package fic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import lib.Compress;
import lib.Decompress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import javax.imageio.ImageIO;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;

/**
 *
 */
class FicTask {

	private ExecutorService executor;
	private BufferedImage image;
	private File output;

	public FicTask() {
		this.executor = Executors.newSingleThreadExecutor();
	}

	public void compress(String imagefilename, String compressedfilename) {
		readFiles(compressedfilename, imagefilename);

		test(compressedfilename, imagefilename);

		RunnableFuture<Void> task = new FutureTask<Void>(new Compress(), null);
		executor.execute(task);
	}

	public void decompress(String compressedfilename, String imagefilename) {
		readFiles(compressedfilename, imagefilename);
		RunnableFuture<Void> task = new FutureTask<Void>(new Decompress(), null);
		executor.execute(task);
	}

	private void readFiles(String datafile, String imagefile) {
		this.output = new File(datafile);
		try {
			this.image = ImageIO.read(new File(imagefile));
		} catch (IOException ioe) {
		}
	}

	/**
	 * Do some tests, play with some images
	 *
	 * @param datafile the file to store an image compressed or w/e
	 * @param imagefile the image file
	 */
	private void test(String imagefilename, String compressedfilename) {
		// crop part of image
		int sx = 0, sy = 0, sw = 31, sh = 31;
		BufferedImage res = image.getSubimage(sx, sy, sw, sh);
		try {
			ImageIO.write(res, "PNG", output);	// supported PNG JPEG GIF
		} catch (IOException ex) {
		}

		// split image to blocks
		int rows = 4;
		int cols = 4;
		int blockwidth = image.getWidth() / cols;
		int blockheight = image.getHeight() / rows;
		BufferedImage[][] blocks = new BufferedImage[rows][cols];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				blocks[x][y] = image.getSubimage(x * blockheight, y * blockwidth, blockwidth, blockheight);
				try {
					ImageIO.write(blocks[x][y], "PNG", new File(String.format("data/block_%d%d_%d%d.png", rows, cols, x, y)));
				} catch (IOException ex) {
				}
			}
		}

		/** *********************
		 * ImageMagick Bindings *
		 * **********************/
		// create command
		ImageCommand cmd = new ConvertCmd();
		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage(imagefilename);					// input
		op.addImage(imagefilename + "_small.jpg");	// output
		op.resize(800, 600);							// command
		// execute the operation
		try {
			cmd.run(op);
		} catch (IOException ex) {
		} catch (InterruptedException ex) {
		} catch (IM4JavaException ex) {
		}

		/* search for similarity -- one of billions of ways..
		 *
		 * cli equivalent:
		 * $ convert block_44_00.png block_44_01.png -compose difference \
		 *	-composite -separate -background black -compose plus \
		 *	-flatten diff.png
		 *
		 */
		IMOperation diff = new IMOperation();
		diff.addImage("data/block_44_00.png");
		diff.addImage("data/block_44_01.png");
		diff.compose("difference");
		diff.composite();
		diff.separate();
		diff.background("black");
		diff.compose("plus");
		diff.flatten();
		diff.addImage("data/diff_00_01.png");
		try {
			cmd.run(diff);	// same cmd wrapper -- reuse!
		} catch (IOException ex) {
		} catch (InterruptedException ex) {
		} catch (IM4JavaException ex) {
		}
	}
}

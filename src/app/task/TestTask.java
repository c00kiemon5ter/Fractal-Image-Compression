package app.task;

import app.Err;
import app.Opts;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import lib.tilers.RectangularTiler;
import lib.tilers.Tiler;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.process.ArrayListOutputConsumer;

/**
 * Test cases
 */
public class TestTask extends Task {

	private BufferedImage image;
	private File input, output;

	public TestTask(Properties properties) {
		super(properties);
		input = new File(properties.getProperty(Opts.INPUT.toString()));
		output = new File(properties.getProperty(Opts.OUTPUT.toString()));
		try {
			image = ImageIO.read(input);
		} catch (IOException ex) {
			System.err.println(Err.IMAGE_NOT_FOUND.description());
			System.exit(Err.IMAGE_NOT_FOUND.errcode());
		}
	}

	@Override
	public void run() {
		//testBuffImage();
		testIm4java();
		testRectTiler();
	}

	/**
	 * Play with some buffered images
	 */
	public void testBuffImage() {

		// crop part of image
		int sx = 0, sy = 0, sw = 128, sh = 128;
		BufferedImage res = image.getSubimage(sx, sy, sw, sh);
		try {
			ImageIO.write(res, "PNG", output);
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
					ImageIO.write(blocks[x][y], "PNG", new File(String.format("%s/block_%d%d_%d%d.png", output.getParent(), rows, cols, x, y)));
				} catch (IOException ex) {
				}
			}
		}
	}

	/** *********************
	 * ImageMagick Bindings *
	 *                      *
	 * evaluate im4java lib *
	 * TODO: Pipe + BuffImg *
	 * **********************/
	private void testIm4java() {
		ImageCommand cmd = new ConvertCmd();
		IMOperation op = new IMOperation();
		op.resize(image.getHeight() / 2, image.getWidth() / 2);
		op.addImage(input.getPath());
		op.addImage(output.getParent() + "/half" + "_small.jpg");
		try {
			cmd.run(op);
		} catch (IOException ex) {
		} catch (InterruptedException ex) {
		} catch (IM4JavaException ex) {
		}

		/* search for similarity -- one of billions of ways..
		 *
		 * cli equivalent:
		 * $ convert img1.png img2.png -compose difference \
		 *	-composite -separate -background black -compose plus \
		 *	-flatten diff.png
		 *
		 * TODO: 
		 * $ compare -metric NCC -fuzz 5% img1.jpg img2.jpg diff.jpg
		 * 
		 * TODO: capture stdout and stderr and pipe buffered images
		 * diff.metric("NCC");		// FIXME: make this work
		 */
		IMOperation diff = new IMOperation();
		diff.addImage(output.getParent() + "/block_44_11.png");
		diff.addImage(output.getParent() + "/output.png");
		diff.compose("difference");
		diff.composite();
		diff.separate();
		diff.background("black");
		diff.compose("plus");
		diff.flatten();
		diff.addImage(output.getParent() + "/diff_00_01.png");
		try {
			ArrayListOutputConsumer oc = new ArrayListOutputConsumer();
			cmd.setOutputConsumer(oc);
			cmd.run(diff);
			System.out.println("OC :: " + oc.getOutput());
			System.out.println("EC :: " + cmd.getErrorText().toString());
		} catch (IOException ex) {
		} catch (InterruptedException ex) {
		} catch (IM4JavaException ex) {
		}
	}

	/**
	 * Test the rectangular tiler
	 */
	private void testRectTiler() {
		Tiler tiler = new RectangularTiler(15, 15);
		BufferedImage[] blocks = tiler.tile(image);
	}
}

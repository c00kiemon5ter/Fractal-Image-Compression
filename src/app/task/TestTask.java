package app.task;

import app.Err;
import app.Opts;
import java.awt.color.ColorSpace;

import lib.tilers.Tiler;
import lib.tilers.RectangularTiler;
import lib.tilers.AdaptiveRectangularTiler;

import lib.comparison.ImageComparator;
import lib.comparison.Metric;
import lib.transformations.ImageTransformer;

import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.im4java.core.IM4JavaException;

/**
 * Test cases
 */
public class TestTask extends Task {

	private BufferedImage inputimg;
	private File inputname;
	private String outputname;
	private static String sep = "-----------------------------------------";

	public TestTask(Properties properties) {
		super(properties);
		inputname = new File(properties.getProperty(Opts.INPUT.toString()));
		outputname = properties.getProperty(Opts.OUTPUT.toString());
		try {
			inputimg = ImageIO.read(inputname);
		} catch (IOException ex) {
			System.err.println(Err.IMAGE_NOT_FOUND.description());
			System.exit(Err.IMAGE_NOT_FOUND.errcode());
		}
	}

	@Override
	public void run() {
		//testRectTiler();
		//testAdaptRectTiler();
		//testComparison();
		testTransformation();
	}

	/**
	 * Test the rectangular tiler.
	 * Write the tiled blocks.
	 */
	private void testRectTiler() {
		System.out.printf("%s\n start of testRectTiler \n%s\n", sep, sep);
		Tiler tiler = new RectangularTiler(5, 5);
		BufferedImage[] blocks = tiler.tile(inputimg);
		System.out.println(blocks.length);
		for (int i = 0; i < blocks.length; i++) {
			try {
				ImageIO.write(blocks[i], "PNG", new File(String.format("tile_block_%d.png", i)));
			} catch (IOException ex) {
				System.err.printf("Couldn't write image: %d\n", i);
			}
		}
		System.out.printf("%s\n end of testRectTiler \n%s\n", sep, sep);
	}

	/**
	 * Test the adaptive rectangular tiler.
	 * Write the tiled blocks.
	 */
	private void testAdaptRectTiler() {
		System.out.printf("%s\n start of testAdaptRectTiler \n%s\n", sep, sep);
		Tiler tiler = new AdaptiveRectangularTiler(5, 6);
		BufferedImage[] blocks = tiler.tile(inputimg);
		System.out.println(blocks.length);
		for (int i = 0; i < blocks.length; i++) {
			try {
				ImageIO.write(blocks[i], "PNG", new File(String.format("tile_block_%d.png", i)));
			} catch (IOException ex) {
				System.err.printf("Couldn't write image: %d\n", i);
			}
		}
		System.out.printf("%s\n end of testAdaptRectTiler \n%s\n", sep, sep);
	}

	/**
	 * Test the ImageComparator of images.
	 */
	private void testComparison() {
		System.out.printf("%s\n start of testComparison \n%s\n", sep, sep);
		ImageComparator comparison = new ImageComparator(Metric.PSNR, 5.2D);
		try {
			if (comparison.compare(inputimg, inputimg)) {
				System.out.printf("Images match: %s\n", comparison.getDifference());
			} else {
				System.out.printf("Images differ: %s\n", comparison.getDifference());
			}
		} catch (IOException ex) {
			System.err.printf("Couldn't run op: compare ioe\n");
		} catch (InterruptedException ex) {
			System.err.printf("Couldn't run op: compare ie\n");
		} catch (IM4JavaException ex) {
			System.err.printf("Couldn't run op: compare im4jve\n");
		}
		if (properties.getProperty(Opts.VERBOSE.toString()) != null) {
			System.out.println("== out stream ==");
			for (String line : comparison.getStdout()) {
				System.out.println(line);
			}
			System.err.println("\n== error stream ==");
			for (String line : comparison.getStderr()) {
				System.err.println(line);
			}
		}
		System.out.printf("%s\n end of testComparison \n%s\n", sep, sep);
	}

	/**
	 * Test the affine transformation of images
	 */
	private void testTransformation() {
		System.out.printf("%s\n start of testAffineTransformation \n%s\n", sep, sep);
		try {
			System.out.println("FLIP");
			ImageIO.write(ImageTransformer.flip(inputimg), "PNG", new File(outputname + "_flip.png"));
			System.out.println("FLOP");
			ImageIO.write(ImageTransformer.flop(inputimg), "PNG", new File(outputname + "_flop.png"));
			System.out.println("SHRINK");
			ImageIO.write(ImageTransformer.shrinkToHalf(inputimg), "PNG", new File(outputname + "_shrink.png"));
			System.out.println("ROTATE");
			ImageIO.write(ImageTransformer.rotate(inputimg, 90.0), "PNG", new File(outputname + "_rotate.png"));
			System.out.println("GRAYSCALE");
			ImageIO.write(ImageTransformer.grayScaleImage(inputimg), "PNG", new File(outputname + "_gray.png"));
			System.out.println("FILTER");
			ImageIO.write(ImageTransformer.colorSpaceFilteredImage(inputimg, ColorSpace.CS_LINEAR_RGB), "PNG", new File(outputname + "_cs.png"));
		} catch (IOException ex) {
			System.err.printf("Couldn't run op: affine ioe\n");
		} catch (InterruptedException ex) {
			System.err.printf("Couldn't run op: affine ie\n");
		} catch (IM4JavaException ex) {
			System.err.printf("Couldn't run op: affine im4jve\n");
		}
		System.out.printf("%s\n end of testAffineTransformation\n%s\n", sep, sep);
	}
}

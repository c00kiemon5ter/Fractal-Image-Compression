package app.task;

import app.Err;
import app.Opts;
import java.awt.Color;
import java.awt.Graphics;
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
import lib.transformations.DefaultTransforms;

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
		testAdaptRectTilerImg();
		//testComparison();
		//testTransformation();
		for (DefaultTransforms dt : DefaultTransforms.values()) {
			dt.transform(inputimg);
		}
	}

	/**
	 * Test the rectangular tiler
	 * 
	 * @see RectangularTiler
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
	 * Test the adaptive rectangular tiler
	 * 
	 * @see AdaptiveRectangularTiler
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
	 * Draw the tiled image with its tiles.
	 * 
	 * @see AdaptiveRectangularTiler
	 */
	private void testAdaptRectTilerImg() {
		System.out.printf("%s\n start of testAdaptRectTilerImg \n%s\n", sep, sep);
		Tiler tiler = new AdaptiveRectangularTiler(5, 6);
		BufferedImage[] blocks = tiler.tile(inputimg);
		BufferedImage rectimg = new BufferedImage(inputimg.getWidth(), inputimg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = rectimg.getGraphics();
		g.drawImage(inputimg, 0, 0, null);
		g.setColor(Color.BLACK);
		int bw = blocks[0].getWidth();
		int bh = blocks[0].getHeight();
		int r = inputimg.getHeight() / bh;
		int c = inputimg.getWidth() / bw;
		for (int y = 0; y < r; y++) {
			for (int x = 0; x < c; x++) {
				g.drawRect(x * bw, y * bh, bw, bh);
			}
		}
		g.dispose();
		try {
			ImageIO.write(rectimg, "PNG", new File(outputname + "_rect.png"));
		} catch (IOException ex) {
			System.err.printf("Couldn't write image: rects\n");
		}
		System.out.printf("%s\n end of testAdaptRectTilerImg \n%s\n", sep, sep);
	}

	/**
	 * Test the ImageComparator of images
	 * 
	 * @see ImageComparator
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
	 * Test the transformation of images
	 * 
	 * @see ImageTransformer
	 */
	private void testTransformation() {
		System.out.printf("%s\n start of testAffineTransformation \n%s\n", sep, sep);
		Long start = System.currentTimeMillis();
		BufferedImage flip = ImageTransformer.flip(inputimg);
		BufferedImage flop = ImageTransformer.flop(inputimg);
		BufferedImage shri = ImageTransformer.shrinkToHalf(inputimg);
		BufferedImage qrot = ImageTransformer.rotateByQuadrant(inputimg, 3);
		BufferedImage drot = ImageTransformer.rotateByDegrees(inputimg, 60);
		BufferedImage gray = ImageTransformer.grayScaleImage(inputimg);
		BufferedImage filt = ImageTransformer.colorSpaceFilteredImage(inputimg, ColorSpace.CS_LINEAR_RGB);
		System.out.printf(":: Affine operations took %dms\n", System.currentTimeMillis() - start);
		try {
			System.out.println("FLIP");
			ImageIO.write(flip, "PNG", new File(outputname + "_flip.png"));
			System.out.println("FLOP");
			ImageIO.write(flop, "PNG", new File(outputname + "_flop.png"));
			System.out.println("SHRINK");
			ImageIO.write(shri, "PNG", new File(outputname + "_shrink.png"));
			System.out.println("Q-ROTATE");
			ImageIO.write(qrot, "PNG", new File(outputname + "_qrot.png"));
			System.out.println("D-ROTATE");
			ImageIO.write(drot, "PNG", new File(outputname + "_drot.png"));
			System.out.println("GRAYSCALE");
			ImageIO.write(gray, "PNG", new File(outputname + "_gray.png"));
			System.out.println("FILTER");
			ImageIO.write(filt, "PNG", new File(outputname + "_cs.png"));
		} catch (IOException ex) {
			System.err.printf("Couldn't write image: affine\n");
		}
		System.out.printf("%s\n end of testAffineTransformation\n%s\n", sep, sep);
	}
}

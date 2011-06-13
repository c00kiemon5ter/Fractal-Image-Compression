package app;

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
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

import org.im4java.core.IM4JavaException;

/**
 * Test cases
 * 
 * TODO:
 *	fix (de)compressor interface/constructor
 *	fix tilers to pixel args
 *	native compare images
 */
public class TestTask implements Runnable {

	private Properties properties;
	private String outputname;
	private BufferedImage inputimg;
	private static String sep = "-----------------------------------------";

	public TestTask(Properties properties) {
		this.properties = properties;
		this.outputname = properties.getProperty(Option.OUTPUT.toString());
		File inputfile = new File(properties.getProperty(Option.INPUT.toString()));
		try {
			this.inputimg = ImageIO.read(inputfile);
		} catch (IOException ex) {
			System.err.println(Error.IMAGE_READ.description());
			System.exit(Error.IMAGE_READ.errcode());
		}
	}

	@Override
	public void run() {
		//testRectTiler();
		//testAdaptRectTiler();
		//testAdaptRectTilerImg();
		//testComparison();
		//testTransformation();
		//compress(inputimg, outputname);
	}

	/**
	 * Test the rectangular tiler
	 * 
	 * @see RectangularTiler
	 */
	private void testRectTiler() {
		System.out.printf("%s\n start of testRectTiler \n%s\n", sep, sep);
		Tiler tiler = new RectangularTiler(5, 5);
		ArrayList<BufferedImage> blocks = tiler.tile(inputimg);
		System.out.println(blocks.size());
		for (int i = 0; i < blocks.size(); i++) {
			try {
				ImageIO.write(blocks.get(i), "PNG", new File(String.format("tile_block_%d.png", i)));
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
		ArrayList<BufferedImage> blocks = tiler.tile(inputimg);
		System.out.println(blocks.size());
		for (int i = 0; i < blocks.size(); i++) {
			try {
				ImageIO.write(blocks.get(i), "PNG", new File(String.format("tile_block_%d.png", i)));
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
		ArrayList<BufferedImage> blocks = tiler.tile(inputimg);
		BufferedImage rectimg = new BufferedImage(inputimg.getWidth(), inputimg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = rectimg.getGraphics();
		g.drawImage(inputimg, 0, 0, null);
		g.setColor(Color.BLACK);
		int bw = blocks.get(0).getWidth();
		int bh = blocks.get(0).getHeight();
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
		if (properties.getProperty(Option.VERBOSE.toString()) != null) {
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

	/**
	 * experimental compression loops
	 * 
	 * @param image
	 * @param outputfile
	 */
	public void compress(BufferedImage image, String outputfile) {
		System.out.printf("%s\n start of testCompression \n%s\n", sep, sep);

		// Create range blocks
		Tiler tiler = new AdaptiveRectangularTiler(5, 6);
		ArrayList<BufferedImage> rangeBlocks = tiler.tile(image);

		// Create domain blocks
		tiler = new AdaptiveRectangularTiler(10, 12);
		ArrayList<BufferedImage> domainBlocks = tiler.tile(image);

		ArrayList<BufferedImage> transformedRangeBlocks = new ArrayList<BufferedImage>();
		for (int i = 0; i < rangeBlocks.size(); i++) {
			transformedRangeBlocks.add(rangeBlocks.get(i));
			transformedRangeBlocks.add(ImageTransformer.flip(rangeBlocks.get(i)));
			transformedRangeBlocks.add(ImageTransformer.flop(rangeBlocks.get(i)));
			transformedRangeBlocks.add(ImageTransformer.shrinkToHalf(rangeBlocks.get(i)));
			transformedRangeBlocks.add(ImageTransformer.rotateByQuadrant(rangeBlocks.get(i), 3));
			transformedRangeBlocks.add(ImageTransformer.grayScaleImage(rangeBlocks.get(i)));
			transformedRangeBlocks.add(ImageTransformer.colorSpaceFilteredImage(rangeBlocks.get(i), ColorSpace.CS_LINEAR_RGB));
		}

		ImageComparator comparison = new ImageComparator(Metric.PSNR, 5.2D);

		//contains all similarities between domain blocks and transformed range blocks
		List similarities = new ArrayList<Entry<BufferedImage, BufferedImage>>();

		for (int i = 0; i < domainBlocks.size(); i++) {

			//contains all similarities between domainBlocks[i] and transformed range blocks
			List<Entry<BufferedImage, Float>> rangeSimilarities = new ArrayList<Entry<BufferedImage, Float>>();

			for (int j = 0; j < transformedRangeBlocks.size(); j++) {
				//Compare each domain block with each transformed range block (problem cause of different sizes of the compared blocks) 
				//and add the most similar one's to a hashmap along with their similarity
//                try { 
//				if (comparison.compare(domainBlocks[i], transformedRangeBlocks.get(j))) { 
//				System.out.printf("Images match: %s\n", comparison.getDifference());
//				//add to transformations that match domainBlocks[i] 
//				rangeSimilarities.add(new SimpleEntry<BufferedImage, Float>(transformedRangeBlocks.get(j), 0.5F)); 
//				//^ comparison's difference should be a float from 0 to 1. Temporarily added 0.5F
//				} else {
//				System.out.printf("Images differ: %s\n", comparison.getDifference());
//				//ignore it
//				} 
//				} catch (IOException ex) {
//				System.err.printf("Couldn't run op: compare ioe\n");
//				} catch (InterruptedException ex) {
//				System.err.printf("Couldn't run op: compare ie\n");
//				} catch (IM4JavaException ex) {
//				System.err.printf("Couldn't run op: compare im4jve\n");
//				}
			}

			Collections.sort(rangeSimilarities, new Comparator<Entry<BufferedImage, Float>>() {

				@Override
				public int compare(Entry<BufferedImage, Float> a, Entry<BufferedImage, Float> b) {
					return a.getValue().compareTo(b.getValue());
				}
			});

			//add top two similar images
//			similarities.add(new SimpleEntry<BufferedImage, BufferedImage>(domainBlocks[i], (BufferedImage) ((SimpleEntry) rangeSimilarities.get(0)).getKey()));
//			similarities.add(new SimpleEntry<BufferedImage, BufferedImage>(domainBlocks[i], (BufferedImage) ((SimpleEntry) rangeSimilarities.get(1)).getKey()));

			System.out.printf("%s\n end of testCompression\n%s\n", sep, sep);
		}
	}
}

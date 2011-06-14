package lib;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import lib.comparison.ImageComparator;
import lib.tilers.Tiler;
import java.util.Set;
import lib.transformations.ImageTransform;

/**
 * fractal compressor instance. combines the tiler and 
 * comparator classes to create a fractal image model
 */
public class Compressor {

	private Tiler rangeTiler, domainTiler;
	private ImageComparator comparator;
	private Set<ImageTransform> transforms;

	public Compressor(Tiler rangeTiler, Tiler domainTiler, ImageComparator comparator, Set<ImageTransform> transforms) {
		this.rangeTiler = rangeTiler;
		this.domainTiler = domainTiler;
		this.comparator = comparator;
		this.transforms = transforms;
		/* 
		 * the 'None' transform -- makes no change to the given image. 
		 * this is useful to traverse the transforms without 
		 * special casing the comparison with the normal image.
		 */
		this.transforms.add(new ImageTransform() {

			@Override
			public BufferedImage transform(BufferedImage inputimage) {
				return inputimage;
			}
		});
	}

	public void compress(BufferedImage image) {
		ArrayList<BufferedImage> rangeblocks = rangeTiler.tile(image);
		ArrayList<BufferedImage> domainblocks = domainTiler.tile(image);
		for (BufferedImage domainImg : domainblocks) {
			for (BufferedImage rangeImg : rangeblocks) {
				for (ImageTransform transform : transforms) {
					int diff = comparator.compare(domainImg, transform.transform(rangeImg));
					switch (diff) {
						case 0:
							System.out.println("Match!");
						default:
							System.out.printf("Diff: %s\n", diff);
					}
				}
			}
		}
	}
}

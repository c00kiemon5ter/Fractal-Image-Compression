package lib;

import lib.tilers.Tiler;
import lib.transformations.ImageTransform;
import lib.comparators.Distanceable;
import lib.transformations.ScaleTransform;

import java.util.ArrayList;
import java.util.Set;
import java.awt.image.BufferedImage;

/**
 * fractal compressor instance. combines the tiler and 
 * comparator classes to create a fractal image model
 */
public class Compressor {

	private Tiler<BufferedImage> tiler;
	private Distanceable<BufferedImage> comparator;
	private Set<ImageTransform> transforms;

	public Compressor(Tiler<BufferedImage> tiler,
					  Distanceable<BufferedImage> comparator,
					  Set<ImageTransform> transforms) {
		this.tiler = tiler;
		this.comparator = comparator;
		this.transforms = transforms;
		/* The 'None' transform -- makes no change to the given image. 
		 * This is useful to traverse the transforms without special casing 
		 * the comparison with the normal(non-transformed) image.
		 */
		this.transforms.add(new ImageTransform() {

			@Override
			public BufferedImage transform(BufferedImage inputimage) {
				return inputimage;
			}
		});
	}

	public void compress(BufferedImage image) {
		// TODO: map each range with coord, transform and appropriate domain
		// TODO: normalization -- blur ? boolean ?
		// TODO: handle scale transform -- subimage search ?
		ArrayList<BufferedImage> rangeblocks = tiler.tile(image);
		ArrayList<BufferedImage> domainblocks = tiler.tile(new ScaleTransform(.5, .5).transform(image));

		for (BufferedImage domainImg : domainblocks) {
			for (BufferedImage rangeImg : rangeblocks) {
				for (ImageTransform transform : transforms) {
					double diff = comparator.distance(rangeImg, transform.transform(domainImg));
				}
			}
		}
	}
}

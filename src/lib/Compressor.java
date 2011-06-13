package lib;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import lib.comparison.ComparatorW;
import lib.tilers.Tiler;
import lib.transformations.DefaultTransform;
import org.im4java.core.IM4JavaException;

/**
 * fractal compressor instance. combines the tiler and 
 * comparator classes to create a fractal image model
 */
public class Compressor {

	private Tiler rangeTiler, domainTiler;
	private ComparatorW comparator;

	public Compressor(Tiler rangeTiler, Tiler domainTiler, ComparatorW comparator) {
		this.rangeTiler = rangeTiler;
		this.domainTiler = domainTiler;
		this.comparator = comparator;
	}

	public void compress(BufferedImage image) throws IOException, InterruptedException, IM4JavaException {
		ArrayList<BufferedImage> rangeblocks = rangeTiler.tile(image);
		ArrayList<BufferedImage> domainblocks = domainTiler.tile(image);
		for (BufferedImage domainImg : domainblocks) {
			for (BufferedImage rangeImg : rangeblocks) {
				for (DefaultTransform transformation : DefaultTransform.values()) {
					if (comparator.compare(domainImg, transformation.transform(image))) {
						System.out.println("~~~ MATCH ! ~~~");
					}
					System.out.println(comparator.getDifference());
				}
			}
		}
		throw new UnsupportedOperationException("Not yet implemented");
	}
}

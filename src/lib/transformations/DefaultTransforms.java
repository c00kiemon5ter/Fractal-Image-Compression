package lib.transformations;

import java.awt.image.BufferedImage;

/**
 * Default transformations
 */
public enum DefaultTransforms implements Transformer<BufferedImage> {

	FLIP {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.flip(inputimage);
		}
	},
	FLOP {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.flop(inputimage);
		}
	},
	SHRINK_HALF() {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.shrinkToHalf(inputimage);
		}
	},
	ROTATE_45() {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.rotateByDegrees(inputimage, 45D);
		}
	},
	ROTATE_90() {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.rotateByQuadrant(inputimage, 1);
		}
	},
	ROTATE_180() {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.rotateByQuadrant(inputimage, 2);
		}
	},
	ROTATE_270() {

		@Override
		public BufferedImage transform(final BufferedImage inputimage) {
			return ImageTransformer.rotateByQuadrant(inputimage, 3);
		}
	},;
}

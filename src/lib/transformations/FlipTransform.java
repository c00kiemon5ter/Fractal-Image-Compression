package lib.transformations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * functor class to affineFlip an image
 */
public class FlipTransform extends ImageTransform {

	private boolean preserveAlpha;

	public FlipTransform(boolean preserveAlpha) {
		this.preserveAlpha = preserveAlpha;
	}

	public FlipTransform() {
		this(false);
	}

	@Override
	public BufferedImage transform(final BufferedImage inputimage) {
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage flippedImg = new BufferedImage(inputimage.getWidth(),
													inputimage.getHeight(), imageType);
		Graphics2D graphics = flippedImg.createGraphics();
		if (preserveAlpha) {
			graphics.setComposite(AlphaComposite.Src);
		}
		graphics.drawImage(inputimage,
						   inputimage.getWidth(), 0, 0, inputimage.getHeight(),
						   0, 0, inputimage.getWidth(), inputimage.getHeight(),
						   null);
		graphics.dispose();
		return flippedImg;
	}
}

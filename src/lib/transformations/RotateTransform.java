package lib.transformations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * functor class to rotate an image by the given degrees
 */
public class RotateTransform extends ImageTransform {

	private double degrees, pointx, pointy;
	private boolean preserveAlpha;

	/**
	 * @param degrees the angle of rotation measured in degrees
	 * @param pointx the x coordinate of the origin of the rotation
	 * @param pointy the y coordinate of the origin of the rotation
	 * @param preserveAlpha  whether to preserve the alpha channel or not
	 */
	public RotateTransform(double degrees, double pointx, double pointy, boolean preserveAlpha) {
		this.degrees = degrees;
		this.pointx = pointx;
		this.pointy = pointy;
		this.preserveAlpha = preserveAlpha;
	}

	public RotateTransform(double degrees, double pointx, double pointy) {
		this(degrees, pointx, pointy, false);
	}

	public RotateTransform(double degrees, boolean preserveAlpha) {
		this(degrees, 0, 0, preserveAlpha);
	}

	public RotateTransform(double degrees) {
		this(degrees, 0, 0, false);
	}

	@Override
	public BufferedImage transform(BufferedImage inputimage) {
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage rotatedImg = new BufferedImage(inputimage.getWidth(),
													 inputimage.getHeight(), imageType);
		Graphics2D graphics = rotatedImg.createGraphics();
		if (preserveAlpha) {
			graphics.setComposite(AlphaComposite.Src);
		}
		graphics.rotate(Math.toRadians(degrees), pointx, pointy);
		graphics.drawImage(inputimage, null, null);
		graphics.dispose();
		return rotatedImg;
	}
}

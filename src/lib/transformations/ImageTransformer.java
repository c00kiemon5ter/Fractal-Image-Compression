package lib.transformations;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 * Produces transformed copies of a given input image.
 */
public class ImageTransformer {

	public static BufferedImage affineTransform(BufferedImage inputimage, AffineTransform transform) {
		return new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR).filter(inputimage, null);
	}

	public static BufferedImage flip(BufferedImage inputimage) {
		AffineTransform transform = new AffineTransform();
		transform.translate(inputimage.getWidth() / 2, inputimage.getHeight() / 2);
		transform.scale(1, -1);
		transform.translate(-inputimage.getWidth() / 2, -inputimage.getHeight() / 2);
		return affineTransform(inputimage, transform);
	}

	public static BufferedImage flop(BufferedImage inputimage) {
		AffineTransform transform = new AffineTransform();
		transform.translate(inputimage.getWidth() / 2, inputimage.getHeight() / 2);
		transform.scale(-1, 1);
		transform.translate(-inputimage.getWidth() / 2, -inputimage.getHeight() / 2);
		return affineTransform(inputimage, transform);
	}

	public static BufferedImage shrinkToHalf(BufferedImage inputimage) {
		return scale(inputimage, .5, .5);
	}

	public static BufferedImage scale(BufferedImage inputimage, double scalex, double scaley) {
		return affineTransform(inputimage, AffineTransform.getScaleInstance(scalex, scaley));
	}

	public static BufferedImage shear(BufferedImage inputimage, double shearx, double sheary) {
		return affineTransform(inputimage, AffineTransform.getShearInstance(shearx, sheary));
	}

	public static BufferedImage rotateByDegrees(BufferedImage inputimage, double degrees) {
		return affineTransform(inputimage, AffineTransform.getRotateInstance(degrees,
																			 inputimage.getWidth() / 2,
																			 inputimage.getHeight() / 2));
	}

	public static BufferedImage rotateByQuadrant(BufferedImage inputimage, int quadrantants) {
		return affineTransform(inputimage, AffineTransform.getQuadrantRotateInstance(quadrantants,
																					 inputimage.getWidth() / 2,
																					 inputimage.getHeight() / 2));
	}

	public static BufferedImage grayScaleImage(BufferedImage inputimage) {
		BufferedImage grayImg = new BufferedImage(inputimage.getWidth(),
												  inputimage.getHeight(),
												  BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grayImg.getGraphics();
		g.drawImage(inputimage, 0, 0, null);
		g.dispose();
		return grayImg;
	}

	public static BufferedImage colorSpaceFilteredImage(BufferedImage inputimage, int colorspace) {
		return new ColorConvertOp(ColorSpace.getInstance(colorspace), null).filter(inputimage, null);
	}
}

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
public abstract class ImageTransform implements Transformer<BufferedImage> {

	/**
	 * The Affine transform theory:
	 * 
	 * [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
	 * [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
	 * [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
	 * 
	 * The corresponding matrix values:
	 * 
	 * [  m00  m01  m02  ]     [ scx  shx  trx ]
	 * [  m10  m11  m12  ] <=> [ shy  scy  try ]
	 * [   0    0    1   ]     [  0   0     1  ]
	 * 
	 * 
	 * @param inputimage the original image to apply the transformation to
	 * @param transform the affine transformation operation-matrix
	 * @param interpolationType the interpolation type to use - one of 
	 * AffineTransformOp.{TYPE_NEAREST_NEIGHBOR ,TYPE_BILINEAR, TYPE_BICUBIC}
	 * @return  the transformed image
	 */
	public static BufferedImage affineTransform(final BufferedImage inputimage, final AffineTransform transform, final int interpolationType) {
		return new AffineTransformOp(transform, interpolationType).filter(inputimage, null);
	}

	public static BufferedImage affineTransform(final BufferedImage inputimage, final AffineTransform transform) {
		return affineTransform(inputimage, transform, AffineTransformOp.TYPE_BILINEAR);
	}

	public static BufferedImage flip(final BufferedImage inputimage) {
		AffineTransform transform = new AffineTransform();
		transform.translate(inputimage.getWidth() / 2, inputimage.getHeight() / 2);
		transform.scale(1, -1);
		transform.translate(-inputimage.getWidth() / 2, -inputimage.getHeight() / 2);
		return affineTransform(inputimage, transform);
	}

	public static BufferedImage flop(final BufferedImage inputimage) {
		AffineTransform transform = new AffineTransform();
		transform.translate(inputimage.getWidth() / 2, inputimage.getHeight() / 2);
		transform.scale(-1, 1);
		transform.translate(-inputimage.getWidth() / 2, -inputimage.getHeight() / 2);
		return affineTransform(inputimage, transform);
	}

	public static BufferedImage shrinkToHalf(final BufferedImage inputimage) {
		return scale(inputimage, .5, .5);
	}

	public static BufferedImage scale(final BufferedImage inputimage, final double scalex, final double scaley) {
		return affineTransform(inputimage, AffineTransform.getScaleInstance(scalex, scaley));
	}

	public static BufferedImage shear(final BufferedImage inputimage, final double shearx, final double sheary) {
		return affineTransform(inputimage, AffineTransform.getShearInstance(shearx, sheary));
	}

	public static BufferedImage rotateByDegrees(final BufferedImage inputimage, final double degrees) {
		return affineTransform(inputimage, AffineTransform.getRotateInstance(Math.toRadians(degrees),
																			 inputimage.getWidth() / 2,
																			 inputimage.getHeight() / 2));
	}

	public static BufferedImage rotateByQuadrants(final BufferedImage inputimage, final int quadrantants) {
		return affineTransform(inputimage, AffineTransform.getQuadrantRotateInstance(quadrantants,
																					 inputimage.getWidth() / 2,
																					 inputimage.getHeight() / 2));
	}

	public static BufferedImage grayScaleImage(final BufferedImage inputimage) {
		BufferedImage grayImg = new BufferedImage(inputimage.getWidth(),
												  inputimage.getHeight(),
												  BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grayImg.getGraphics();
		g.drawImage(inputimage, 0, 0, null);
		g.dispose();
		return grayImg;
	}

	public static BufferedImage colorSpaceFilteredImage(final BufferedImage inputimage, final int colorspace) {
		return new ColorConvertOp(ColorSpace.getInstance(colorspace), null).filter(inputimage, null);
	}
}

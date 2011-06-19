package lib.transformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

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
    public static BufferedImage affineTransform(final BufferedImage inputimage,
                                                final AffineTransform transform,
                                                final int interpolationType) {
        return new AffineTransformOp(transform, interpolationType).filter(inputimage, null);
    }
}

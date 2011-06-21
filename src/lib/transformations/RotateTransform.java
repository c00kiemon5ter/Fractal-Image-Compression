package lib.transformations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * functor class to rotate an image by the given degrees
 */
public class RotateTransform extends ImageTransform {

    private double  degrees, pointx, pointy;
    private boolean preserveAlpha;

    public RotateTransform(final double degrees) {
        this(degrees, 0, 0, false);
    }

    public RotateTransform(final double degrees, final boolean preserveAlpha) {
        this(degrees, 0, 0, preserveAlpha);
    }

    public RotateTransform(final double degrees, final double pointx, final double pointy) {
        this(degrees, pointx, pointy, false);
    }

    /**
     * @param degrees the angle of rotation measured in degrees
     * @param pointx the x coordinate of the origin of the rotation
     * @param pointy the y coordinate of the origin of the rotation
     * @param preserveAlpha  whether to preserve the alpha channel or not
     */
    public RotateTransform(final double degrees, final double pointx,
                           final double pointy, final boolean preserveAlpha) {
        this.degrees = degrees;
        this.pointx  = pointx;
        this.pointy  = pointy;
        this.preserveAlpha = preserveAlpha;
    }

    @Override
    public BufferedImage transform(final BufferedImage inputimage) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage rotatedImg = new BufferedImage(inputimage.getWidth(), inputimage.getHeight(), imageType);
        
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

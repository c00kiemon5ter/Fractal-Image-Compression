package lib.transformations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * functor class to affineScale an image
 */
public class ScaleTransform extends ImageTransform {

    private boolean preserveAlpha;
    private double  scalex, scaley;

    public ScaleTransform(final double scalex, final double scaley) {
        this(scalex, scaley, false);
    }

    /**
     *
     * @param scalex the factor by which coordinates are scaled along the X axis direction
     * @param scaley the factor by which coordinates are scaled along the Y axis direction
     * @param preserveAlpha whether to preserve the alpha channel or not
     */
    public ScaleTransform(final double scalex, final double scaley, final boolean preserveAlpha) {
        this.scalex = scalex;
        this.scaley = scaley;
        this.preserveAlpha = preserveAlpha;
    }

    @Override
    public BufferedImage transform(final BufferedImage inputimage) {
        int imageType    = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        int scaledWidth  = (int) (inputimage.getWidth() * scalex);
        int scaledHeight = (int) (inputimage.getHeight() * scaley);
        BufferedImage scaledImg = new BufferedImage(scaledWidth, scaledHeight, imageType);
        
        Graphics2D graphics = scaledImg.createGraphics();
        if (preserveAlpha) {
            graphics.setComposite(AlphaComposite.Src);
        }
        graphics.drawImage(inputimage, 0, 0, scaledWidth, scaledHeight, null);
        graphics.dispose();

        return scaledImg;
    }

    public double getScalex() {
        return scalex;
    }

    public double getScaley() {
        return scaley;
    }

    @Override
    public String toString() {
        return String.format("%.2f:%.2f", scalex, scaley);
    }
}

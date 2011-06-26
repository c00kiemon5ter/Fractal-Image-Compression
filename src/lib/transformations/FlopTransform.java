package lib.transformations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * functor class to affineFlop an image
 */
public class FlopTransform extends ImageTransform {

    private boolean preserveAlpha;

    public FlopTransform(final boolean preserveAlpha) {
        this.preserveAlpha = preserveAlpha;
    }

    public FlopTransform() {
        this(false);
    }

    @Override
    public BufferedImage transform(final BufferedImage inputimage) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage floppedImg = new BufferedImage(inputimage.getWidth(),
                                                     inputimage.getHeight(), imageType);

        Graphics2D graphics = floppedImg.createGraphics();
        if (preserveAlpha) {
            graphics.setComposite(AlphaComposite.Src);
        }
        graphics.drawImage(inputimage,
                           0, inputimage.getWidth(), inputimage.getHeight(), 0,
                           0, 0, inputimage.getWidth(), inputimage.getHeight(),
                           null);
        graphics.dispose();

        return floppedImg;
    }
}

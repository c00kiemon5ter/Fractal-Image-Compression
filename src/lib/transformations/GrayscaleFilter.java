package lib.transformations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * a filter to return an image in the grayscale colorspace
 */
public class GrayscaleFilter extends ImageTransform {

    @Override
    public BufferedImage transform(final BufferedImage inputimage) {
        BufferedImage grayImg = new BufferedImage(inputimage.getWidth(),
                                                  inputimage.getHeight(),
                                                  BufferedImage.TYPE_BYTE_GRAY);
        
        Graphics2D graphics = grayImg.createGraphics();
        graphics.drawImage(inputimage, 0, 0, null);
        graphics.dispose();

        return grayImg;
    }
}

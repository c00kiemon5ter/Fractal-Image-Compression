package lib.transformations;

import java.awt.image.BufferedImage;

/*
 * The 'None' transform -- makes no change to the given image.
 * This is useful to traverse the transforms without special casing
 * the comparison with the normal(non-transformed) image.
 */
public class NoneTransform extends ImageTransform {

    @Override
    public BufferedImage transform(BufferedImage inputimage) {
        return inputimage;
    }
}

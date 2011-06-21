package lib.tilers;

import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Wrapper class around {@code RectangularTiler}
 *
 * @see RectangularTiler
 */
public class SquareTiler implements Tiler<BufferedImage> {

    private RectangularTiler rectTiler;

    public SquareTiler(final int blocksInOneSide) {
        this.rectTiler = new RectangularTiler(blocksInOneSide, blocksInOneSide);
    }

    @Override
    public ArrayList<BufferedImage> tile(final BufferedImage image) {
        return this.rectTiler.tile(image);
    }
}

package lib.tilers;

import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Square tiler. Given the pixels of a side of a block,
 * it tiles the image into as many blocks can fit.
 * This is a wrapper class around {@code RectangularPixelTiler}
 *
 * @see RectangularPixelTiler
 */
public class SquarePixelTiler implements Tiler<BufferedImage> {

    private RectangularPixelTiler rectTiler;

    /**
     * @param blockside the pixels of each side of the block
     */
    public SquarePixelTiler(int blockside) {
        this.rectTiler = new RectangularPixelTiler(blockside, blockside);
    }

    @Override
    public ArrayList<BufferedImage> tile(BufferedImage image) {
        return rectTiler.tile(image);
    }
}

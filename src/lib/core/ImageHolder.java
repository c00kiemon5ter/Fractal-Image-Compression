package lib.core;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * wrapper around images, to add Serialization
 * 
 * @see BufferedImage
 * @see Serializable
 */
public class ImageHolder implements Serializable {

    private BufferedImage image;

    public ImageHolder(BufferedImage image) {
        this.image = image;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        image = ImageIO.read(stream);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        ImageIO.write(image, "PNG", stream);
    }

    private void readObjectNoData() throws ObjectStreamException {
        // noop -- leave image as null
    }

    public BufferedImage getImage() {
        return image;
    }
}

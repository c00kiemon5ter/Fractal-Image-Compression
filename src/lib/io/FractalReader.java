package lib.io;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

import lib.core.FractalModel;
import lib.transformations.ImageTransform;

/**
 * Read a representation of the fractal model from the input stream
 *
 * @see DataInputStream
 */
public class FractalReader {

    private ObjectInputStream in;

    /**
     * @param compressedFile the stream to read the fractal model from
     * 
     * @throws FileNotFoundException if file doesn't exist
     * @throws IOException 
     * 
     * @see DataInputStream
     */
    public FractalReader(File compressedFile) throws FileNotFoundException, IOException {
        this.in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(compressedFile)));
    }

    /**
     * Read a representation of the fractal model from the input stream
     * 
     * @return the fractal model as read from the compressed file
     * 
     * @throws ClassNotFoundException if the compressed file is corrupt
     * 
     * @see FractalModel
     */
    @SuppressWarnings (value="unchecked")
    public FractalModel read() throws ClassNotFoundException {
        FractalModel fmodel = new FractalModel();

        while (true) {
            try {
                BufferedImage domain = ImageIO.read(this.in);
                Map<ImageTransform, Set<Point>> transform_points = (Map<ImageTransform, Set<Point>>) this.in.readObject();

                fmodel.add(new SimpleEntry<BufferedImage, Map<ImageTransform, Set<Point>>>(domain, transform_points));
            } catch (IOException ioe) {
                break;
            }
        }

        return fmodel;
    }

    /**
     * end the stream
     * 
     * @throws IOException 
     */
    public void close() throws IOException {
        this.in.close();
    }
}

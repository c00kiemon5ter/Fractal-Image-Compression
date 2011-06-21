package lib.io;

import lib.transformations.ImageTransform;

import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Map;

import javax.imageio.ImageIO;
import lib.Compressor;

/**
 * Write a representation of the fractal model to a file efficiently.<br />
 * Wrapper class around {@code DataOutputStream}
 *
 * @see DataOutput
 * @see DataOutputStream
 * @see Compressor
 */
public class FractalWriter {

    private DataOutputStream out;

    public FractalWriter(OutputStream outstream) throws FileNotFoundException, IOException {
        this.out = new DataOutputStream(new BufferedOutputStream(outstream));
    }

    /**
     * Write a representation of the fractal model to the output stream. 
     * The representation for each domain image is:
     * 
     *     [domain-image][transform[point..point]..transform[point..point]]
     * 
     * @param fractalmodel
     * @throws IOException 
     */
    public void write(Map<Point, Map.Entry<BufferedImage, ImageTransform>> fractalmodel) throws IOException {

        for (Point point : fractalmodel.keySet()) {
            Map.Entry<BufferedImage, ImageTransform> entry = fractalmodel.get(point);

            ImageIO.write(entry.getKey(), "PNG", this.out);

            this.out.writeInt(point.x);
            this.out.writeInt(point.y);
        }
    }

    public void close() throws IOException {
        this.out.flush();
        this.out.close();
    }
}

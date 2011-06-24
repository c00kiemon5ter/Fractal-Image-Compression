package lib.io;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import lib.core.FractalModel;

/**
 * Write a representation of the fractal model to an output stream
 *
 * @see DataOutputStream
 */
public class FractalWriter {

    private ObjectOutputStream out;

    /**
     * @param outstream the stream to write the fractal model to
     * 
     * @throws IOException 
     * 
     * @see ObjectOutputStream
     */
    public FractalWriter(final OutputStream outstream) throws IOException {
        this.out = new ObjectOutputStream(outstream);
    }

    /**
     * Write a representation of the fractal model to an output stream
     *
     * @param fmodel the fractal model to write to the stream
     * 
     * @throws IOException  
     */
    public void write(FractalModel fmodel) throws IOException {
        for (BufferedImage domain : fmodel.getModel().keySet()) {
            ImageIO.write(domain, "PNG", this.out);
            this.out.writeObject(fmodel.getModel().get(domain));
        }
    }

    /**
     * end the stream
     * 
     * @throws IOException 
     */
    public void close() throws IOException {
        this.out.flush();
        this.out.close();
    }
}

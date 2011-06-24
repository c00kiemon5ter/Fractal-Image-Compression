package lib.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lib.core.FractalModel;

/**
 * Write a representation of the fractal model to an output stream
 *
 * @see DataOutputStream
 */
public class FractalWriter {

    private DataOutputStream out;

    /**
     * @param outstream the stream to write the fractal model to
     * 
     * @see DataOutputStream
     */
    public FractalWriter(final OutputStream outstream) {
        this.out = new DataOutputStream(outstream);
    }

    /**
     * Write a representation of the fractal model to an output stream
     *
     * @param fmodel
     */
    public void write(FractalModel fmodel) {
        throw new UnsupportedOperationException("not supported yet");
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

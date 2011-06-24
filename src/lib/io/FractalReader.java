package lib.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import lib.core.FractalModel;

/**
 * Read a representation of the fractal model from the input stream
 *
 * @see DataInputStream
 */
public class FractalReader {

    private DataInputStream in;

    /**
     * @param compressedFile the stream to read the fractal model from
     * 
     * @throws FileNotFoundException if file doesn't exist
     * 
     * @see DataInputStream
     */
    public FractalReader(File compressedFile) throws FileNotFoundException {
        this.in = new DataInputStream(new BufferedInputStream(new FileInputStream(compressedFile)));
    }

    /**
     * Read a representation of the fractal model from the input stream
     * 
     * @return the fractal model as read from the compressed file
     * 
     * @see FractalModel
     */
    public FractalModel read() {
        throw new UnsupportedOperationException("not supported yet");
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

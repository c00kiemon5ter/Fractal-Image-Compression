package lib.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import lib.core.FractalModel;

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
     * @throws IOException 
     * 
     * @see FractalModel
     */
    @SuppressWarnings (value="unchecked")
    public FractalModel read() throws ClassNotFoundException, IOException {
        return (FractalModel) this.in.readObject();
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

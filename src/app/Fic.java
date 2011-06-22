package app;

import app.configuration.Configuration;

import lib.Compressor;
import lib.Decompressor;

import lib.comparators.ImageComparator;

import lib.core.FractalModel;

import lib.io.ProgressBar;

import lib.tilers.RectangularPixelTiler;

import lib.transformations.AffineRotateQuadrantsTransform;
import lib.transformations.FlipTransform;
import lib.transformations.FlopTransform;
import lib.transformations.GrayscaleFilter;
import lib.transformations.ImageTransform;
import lib.transformations.ScaleTransform;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * The fic object is an instance of the application.
 *
 */
public class Fic implements Observer, Runnable {

    private static final Logger LOGGER = Logger.getLogger("debugger");
    private Configuration       configuration;
    private ProgressBar         progressbar;

    /**
     * @param configuration the configuration
     */
    public Fic(Configuration configuration) {
        this.configuration = configuration;
        this.progressbar   = new ProgressBar();
    }

    @Override
    public void run() {
        FractalModel fmodel;

        // TODO: form Fractal Writer and Reader - Externalizable interface
        // TODO: fix input and output streams
        switch (configuration.command()) {
            case COMPRESS :
                fmodel = compress();
                writeModel(fmodel, null);
            case DECOMPRESS :
                fmodel = readModel(null);
                decompress(fmodel);
            default :
                configuration.usage();
                System.err.println(Error.UNKNOWN_ARG.description(configuration.command().toString()));
                System.exit(Error.UNKNOWN_ARG.errcode());
        }
    }

    /**
     * Update the state of the progress bar. The argument is an array of int
     * with two elements. The first represents the current work done, and the
     * second the whole work that is needed to be done in order to consider
     * the task complete.
     *
     * @param observableSource the observable object that changed state
     * @param arr_done_all array with current completed work
     */
    @Override
    public void update(final Observable observableSource, final Object arr_done_all) {
        int[] work = (int[]) arr_done_all;
        progressbar.update(work[0], work[1]);
    }

    public FractalModel compress() {
        if (configuration.verbose()) {
            LOGGER.log(Level.INFO, ":: Initializing compress process..");
        }

        BufferedImage image = null;

        try {
            image = new GrayscaleFilter().transform(ImageIO.read(configuration.inputfile()));
        } catch (IOException ex) {
            configuration.usage();
            System.err.println(Error.IMAGE_READ.description(configuration.inputfile().toString()));
            System.err.println(ex);
            System.exit(Error.IMAGE_READ.errcode());
        }

        Compressor compressor = new Compressor(
                new ScaleTransform(configuration.scalex(), configuration.scaley()), 
                new RectangularPixelTiler(configuration.xpixels(), configuration.ypixels()),
                new ImageComparator(configuration.metric(), configuration.fuzz()),
                new HashSet<ImageTransform>(5) {{
                    add(new FlipTransform());
                    add(new FlopTransform());
                    add(new AffineRotateQuadrantsTransform(1));
                    add(new AffineRotateQuadrantsTransform(2));
                    add(new AffineRotateQuadrantsTransform(3));
                }}, this);

        if (configuration.verbose()) {
            LOGGER.log(Level.INFO, ":: Starting compression .. ");
        }

        return compressor.compress(image);
    }

    public void decompress(FractalModel fmodel) {
        if (configuration.verbose()) {
            LOGGER.log(Level.INFO, ":: Initializing decompress process..");
        }

        Decompressor decompressor = new Decompressor();
        decompressor.decompress(fmodel);
    }

    public void writeModel(FractalModel model, OutputStream outstream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public FractalModel readModel(InputStream instream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

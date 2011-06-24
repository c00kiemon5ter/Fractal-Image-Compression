package app;

import app.configuration.Configuration;

import com.jhlabs.image.GrayscaleFilter;

import lib.Compressor;
import lib.Decompressor;

import lib.comparators.ImageComparator;

import lib.core.FractalModel;

import lib.io.ProgressBar;
import lib.io.FractalReader;
import lib.io.FractalWriter;

import lib.transformations.AffineRotateQuadrantsTransform;
import lib.transformations.FlipTransform;
import lib.transformations.FlopTransform;
import lib.transformations.ImageTransform;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * The fic object is the main run system of the application.
 * Fic handles the interpretation of the configuration, 
 * runs the appropriate commands with the appropriate values.
 */
public class Fic implements Observer, Runnable {

    private static final Logger LOGGER = Logger.getLogger("debugger");
    private Configuration configuration;
    private ProgressBar progressbar;

    /**
     * @param configuration the configuration options
     */
    public Fic(Configuration configuration) {
        this.configuration = configuration;
        this.progressbar = new ProgressBar();
    }

    @Override
    public void run() {
        FractalModel fmodel;
        BufferedImage image;

        if (configuration.debug()) {
            LOGGER.log(Level.INFO, String.format(":: Initializing %s process ..",
                                                 configuration.command().id()));
        }

        switch (configuration.command()) {
            case COMPRESS:
                if (configuration.debug()) {
                    LOGGER.log(Level.INFO, String.format(":: Reading image: %s",
                                                         configuration.input().getName()));
                }

                image = readImage();

                if (configuration.debug()) {
                    LOGGER.log(Level.INFO, ":: Starting compression ..");
                }

                fmodel = compress(image);

                if (configuration.debug()) {
                    LOGGER.log(Level.INFO, ":: Writing to stream ..");
                }

                writeModel(fmodel);

                break;
            case DECOMPRESS:
                if (configuration.debug()) {
                    LOGGER.log(Level.INFO, ":: Reading compressed file: %s",
                               configuration.input().getName());
                }

                fmodel = readModel();

                if (configuration.debug()) {
                    LOGGER.log(Level.INFO, ":: Starting decompression ..");
                }

                image = decompress(fmodel);

                if (configuration.debug()) {
                    LOGGER.log(Level.INFO, ":: Writing to stream ..");
                }

                writeImage(image);

                break;
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

    private BufferedImage readImage() {
        BufferedImage image = null;

        try {
            image = ImageIO.read(configuration.input());
        } catch (IOException ioe) {
            System.err.println(Error.FILE_READ.description(configuration.input().getName()));
            System.exit(Error.FILE_READ.errcode());
        }

        return image;
    }

    public FractalModel compress(BufferedImage image) {
        Compressor compressor = new Compressor(
                configuration.domainScale(),
                configuration.tiler(),
                new ImageComparator(configuration.metric(), configuration.fuzz()),
                new HashSet<ImageTransform>(5) {{
                    add(new FlipTransform());
                    add(new FlopTransform());
                    add(new AffineRotateQuadrantsTransform(1));
                    add(new AffineRotateQuadrantsTransform(2));
                    add(new AffineRotateQuadrantsTransform(3));
                }}, 
                new HashSet<BufferedImageOp>(1) {{
                    add(new GrayscaleFilter());
                }},
                this);

        return compressor.compress(image);
    }

    public void writeModel(FractalModel fmodel) {
        OutputStream out = System.out;

        if (configuration.output() != null) {
            try {
                out = new FileOutputStream(configuration.output());
            } catch (FileNotFoundException fnfe) {
                System.err.println(Error.FILE_READ.description(configuration.output().getName()));
                System.exit(Error.FILE_READ.errcode());
            }
        }

        try {
            FractalWriter fwriter = new FractalWriter(new BufferedOutputStream(out));
            fwriter.write(fmodel);
            fwriter.close();
        } catch (IOException ioe) {
            System.err.println(Error.STREAM_WRITE.description());
            System.exit(Error.STREAM_WRITE.errcode());
        }
    }

    public FractalModel readModel() {
        FractalModel fmodel = null;

        try {
            FractalReader freader = new FractalReader(configuration.input());
            fmodel = freader.read();
            freader.close();
        } catch (ClassNotFoundException cnfe) {
            System.err.println(Error.FILE_READ.description(configuration.input().getName()));
            System.exit(Error.FILE_READ.errcode());
        } catch (IOException ioe) {
            System.err.println(Error.FILE_READ.description(configuration.input().getName()));
            System.exit(Error.FILE_READ.errcode());
        }

        return fmodel;
    }

    public BufferedImage decompress(FractalModel fmodel) {
        Decompressor decompressor = new Decompressor();

        return decompressor.decompress(fmodel);
    }

    private void writeImage(BufferedImage image) {
        OutputStream out = System.out;

        if (configuration.output() != null) {
            try {
                out = new FileOutputStream(configuration.output());
            } catch (FileNotFoundException fnfe) {
                System.err.println(Error.FILE_READ.description(configuration.output().getName()));
                System.exit(Error.FILE_READ.errcode());
            }
        }

        try {
            ImageIO.write(image, "PNG", new BufferedOutputStream(out));
        } catch (IOException ioe) {
            System.err.println(Error.STREAM_WRITE.description());
            System.exit(Error.STREAM_WRITE.errcode());
        }
    }
}

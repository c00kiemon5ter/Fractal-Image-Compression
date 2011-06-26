package app;

import app.configuration.Configuration;

/**
 * Command line utility to compress an image using
 * fractal image compression methods.
 * 
 * Configuration:
 * TODO: add configuration of filters and transforms
 * 
 * Comparators:
 * FIXME: the current compressor cannot handle scale transforms due to
 * ImageComparator's limitation to same-size image comparison only
 * 
 * Metric:
 * NOTE: currently implemented: AE MSE RMSE
 * NOTE: currently only AE is affected by fuzz
 * 
 * Compression:
 * TODO: FUTURE: multi-threading
 * 
 * Tilers:
 * TODO: FUTURE: intelligent tilers - HV/Quadtree partitioning
 */
public class Application {

    /**
     * read the args, start the app
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configuration configuration = new Configuration(args);
        Runnable fic = new Fic(configuration);

        fic.run();
    }
}

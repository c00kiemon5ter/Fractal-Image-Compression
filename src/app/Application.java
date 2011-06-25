package app;

import app.configuration.Configuration;

/**
 * Command line utility to compress an image using
 * fractal image compression methods.
 * 
 * TODO: add configuration of filters and transforms
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

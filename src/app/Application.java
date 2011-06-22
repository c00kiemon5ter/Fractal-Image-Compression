package app;

import app.configuration.Configuration;

import java.io.IOException;

/**
 * Command line utility to compress an image using
 * fractal image compression methods.
 */
public class Application {

    /**
     * read the args, start the app
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        try {
            configuration.parse("data/fractal.conf", args);
        } catch (IOException ioe) {
            configuration.usage();
            System.err.println(Error.FILE_READ.description("data/fractal.conf"));
            System.exit(Error.FILE_READ.errcode());
        }

        Runnable fic = new Fic(configuration);
        
        fic.run();
    }
}

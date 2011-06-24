package app.configuration;

import app.Fic;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import lib.comparators.Metric;

import lib.tilers.Tiler;

import lib.transformations.ScaleTransform;

import java.awt.image.BufferedImage;

import java.io.File;

/**
 * Holds the configuration of the system,
 * based on the arguments given
 */
public class Configuration {

    private Commands   command;
    private Options    options;
    private JCommander parser;

    /**
     * given arguments, configuration parses them
     * and initializes the option values
     * 
     * @param arguments the arguments to parse
     */
    public Configuration(String[] arguments) {
        parser = new JCommander(this.options = new Options());

        parser.setProgramName(Fic.class.getCanonicalName());
        parser.addCommand(Commands.COMPRESS.id(), Commands.COMPRESS);
        parser.addCommand(Commands.DECOMPRESS.id(), Commands.DECOMPRESS);

        try {
            parser.parse(arguments);
        } catch (ParameterException pe) {

            /*
             * if help was requested
             * print usage and
             * exit successfully
             */
            if (options.help) {
                selectUsage();
                System.exit(0);
            }

            /*
             * else print usage
             * print the error message
             * and exit with fail code
             */
            parser.usage();
            System.err.println(pe.getMessage());
            System.exit(1);
        }

        /*
         * even if there was not a parsing error
         * help could still have been requested
         */
        if (options.help) {
            selectUsage();
            System.exit(0);
        }

        command = Commands.valueOf(parser.getParsedCommand().toUpperCase());
    }

    /**
     * if a command was parsed, show
     * the usage for that command
     */
    private void selectUsage() {
        if (parser.getParsedCommand() != null) {
            parser.usage(parser.getParsedCommand());
        } else {
            parser.usage();
        }
    }

    public boolean help() {
        return options.help;
    }

    public boolean debug() {
        return options.debug;
    }

    public Metric metric() {
        return options.metric;
    }

    public double fuzz() {
        return options.fuzz;
    }

    public ScaleTransform domainScale() {
        return options.domainScale;
    }

    public Tiler<BufferedImage> tiler() {
        return options.tiler;
    }

    public Commands command() {
        return command;
    }

    public File input() {
        return command.input;
    }

    public File output() {
        return command.output;
    }

    public void printUsage() {
        parser.usage();
    }

    public void printUsage(Commands command) {
        parser.usage(command.id());
    }
}

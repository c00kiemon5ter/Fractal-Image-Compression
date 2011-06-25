package app.configuration;

import app.Fic;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import lib.comparators.Metric;
import lib.transformations.ScaleTransform;
import lib.tilers.RectangularPixelTiler;

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
            selectUsage(parser.getParsedCommand());
            System.err.println(pe.getMessage());
            System.exit(options.help ? 0 : 1);
        }

        String cmd = parser.getParsedCommand();

        /*
         * even if there was not a parsing error
         * help could still have been requested
         */
        if (options.help) {
            selectUsage(cmd);
            System.exit(0);
        }

        if (cmd != null) {
            command = Commands.valueOf(cmd.toUpperCase());
        } else {
            parser.usage();
            System.err.println(app.Error.REQUIRED_ARG_NOT_FOUND.description("<command>"));
            System.exit(1);
        }
    }

    /**
     * if a command was parsed, show
     * the usage for that command
     */
    private void selectUsage(String cmd) {
        if (cmd != null) {
            parser.usage(cmd);
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

    public RectangularPixelTiler tiler() {
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

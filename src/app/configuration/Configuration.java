package app.configuration;

import app.Error;
import app.Fic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Properties;
import lib.comparators.Metric;

/**
 * load the configuration
 *
 * TODO: set all default properties
 * TODO: validate and store properties in correct type
 * TODO: add scale factor and pixel x/y options - remove quality
 * TODO: allow configuration of Transforms and Filters and Streams
 *
 * @see Properties
 * @see #parse()
 * @see #parse(java.io.File)
 * @see #parse(java.lang.String[])
 * @see #parse(java.io.File, java.lang.String[])
 */
public class Configuration {

    private final String line_sep = System.getProperty("line.separator");
    private final String comments = new StringBuffer("Configuration file for fic")
                                        .append(line_sep).append("Options are set int the form:")
                                        .append(line_sep).append("\toption = value").toString();
    private Properties   configuration;

    /**
     * initialize and set default configuration
     */
    public Configuration() {
        this.configuration = new Properties();

        this.configuration.setProperty(Option.METRIC. toString(), "AE");
        this.configuration.setProperty(Option.FUZZ.   toString(), "5");
        this.configuration.setProperty(Option.QUALITY.toString(), "0.9");
        this.configuration.setProperty(Option.VERBOSE.toString(), Boolean.FALSE.toString());
        this.configuration.setProperty(Option.DEBUG.  toString(), Boolean.FALSE.toString());
    }

    public Command command() {
        return Command.valueOf(configuration.getProperty(Command.ID));
    }

    public File inputfile() {
        return new File(configuration.getProperty(Option.INPUT.toString()));
    }

    public double scalex() {
        return .5;
    }

    public double scaley() {
        return .5;
    }

    public int xpixels() {
        return 8;
    }

    public int ypixels() {
        return 8;
    }

    public Metric metric() {
        return Metric.valueOf(configuration.getProperty(Option.METRIC.toString()));
    }

    public double fuzz() {
        return Double.parseDouble(configuration.getProperty(Option.FUZZ.toString()));
    }

    public boolean verbose() {
        return Boolean.valueOf(configuration.getProperty(Option.VERBOSE.toString()));
    }

    /**
     * The parser loads the default values. It then proceeds to <br />
     * process the configuration file. If the configuration file <br />
     * is missing it writes the default, else, it reads the file <br />
     * overwriting the default values. <br />
     * Finally the parser read the provided arguments.<br />
     * <br />
     * Priority from higher to lower is:<br />
     *     {@code arguments > configuration file > default values}
     *
     * @param configfilename the configuration file to read
     * @param args the command line arguments to parse
     * @return the properties table containing the configuration
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Properties parse(String configfilename, String[] args) throws FileNotFoundException, IOException {
        configuration = parse(configfilename);
        configuration = parse(args);

        return configuration;
    }

    /**
     * The parser loads the default values. It then proceeds to <br />
     * process the configuration file. If the configuration file <br />
     * is missing it writes the default, else, it reads the file <br />
     * overwriting the default values. <br />
     * Finally the parser read the provided arguments.<br />
     * <br />
     * Priority from higher to lower is:<br />
     *     {@code arguments > configuration file > default values}
     *
     * @param args the command line arguments to parse
     * @return the properties table containing the configuration
     *
     * @throws IOException
     */
    public Properties parse(String[] args) throws IOException {
        configuration = parseArguments(configuration, args);

        return configuration;
    }

    /**
     * The parser loads the default values. It then proceeds to <br />
     * process the configuration file. If the configuration file <br />
     * is missing it writes the default, else, it reads the file <br />
     * overwriting the default values. <br />
     * Finally the parser read the provided arguments.<br />
     * <br />
     * Priority from higher to lower is:<br />
     *     {@code arguments > configuration file > default values}
     *
     * @param configfilename the configuration filename to read
     * @return the properties table containing the configuration
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Properties parse(String configfilename) throws FileNotFoundException, IOException {
        configuration = parseFile(configuration, configfilename);

        return configuration;
    }

    /**
     * The parser loads the default values. It then proceeds to <br />
     * process the configuration file. If the configuration file <br />
     * is missing it writes the default, else, it reads the file <br />
     * overwriting the default values. <br />
     * Finally the parser read the provided arguments.<br />
     * <br />
     * Priority from higher to lower is:<br />
     *     {@code arguments > configuration file > default values}
     *
     * @return the properties table containing the configuration
     *
     * @throws IOException
     */
    public Properties parse() throws IOException {
        return configuration;
    }

    /**
     * Parse the configuration file.
     * If it doesn't exist create a default.
     */
    private Properties parseFile(Properties defaultProperties, String configfilename) throws FileNotFoundException, IOException {
        File       configfile = new File(configfilename);
        Properties properties = new Properties(defaultProperties);

        if (!configfile.exists()) {
            if (!configfile.getParentFile().exists()) {
                configfile.getParentFile().mkdirs();
            }

            properties.store(new PrintWriter(new BufferedWriter(
                             new FileWriter(configfile)), true),
                             comments.toString());
        }

        properties.load(new FileReader(configfile));

        return properties;
    }

    /**
     * Parse the command line arguments
     */
    private Properties parseArguments(Properties defaultProperties, String[] args) {
        if (args.length == 0) {
            return defaultProperties;
        }

        Properties           properties = new Properties(defaultProperties);
        ListIterator<String> iterator   = Arrays.asList(args).listIterator();

        while (iterator.hasNext()) {
            String clielement = iterator.next();

            if (Option.HELP.option().equals(clielement)) {
                usage();
                System.exit(0);
            } else if (Command.COMPRESS.option().equals(clielement)) {
                properties.setProperty(Command.ID, Command.COMPRESS.toString());
            } else if (Command.DECOMPRESS.option().equals(clielement)) {
                properties.setProperty(Command.ID, Command.DECOMPRESS.toString());
            } else if (Option.INPUT.option().equals(clielement)) {
                if (iterator.hasNext() && (clielement = iterator.next()).charAt(0) != '-') {
                    properties.setProperty(Option.INPUT.toString(), clielement);
                } else {
                    usage();
                    System.err.println(Error.MISSING_ARG.description(Option.INPUT.option()));
                    System.exit(Error.MISSING_ARG.errcode());
                }
            } else if (Option.OUTPUT.option().equals(clielement)) {
                if (iterator.hasNext() && (clielement = iterator.next()).charAt(0) != '-') {
                    properties.setProperty(Option.OUTPUT.toString(), clielement);
                } else {
                    usage();
                    System.err.println(Error.MISSING_ARG.description(Option.OUTPUT.option()));
                    System.exit(Error.MISSING_ARG.errcode());
                }
            } else if (Option.METRIC.option().equals(clielement)) {
                if (iterator.hasNext() && (clielement = iterator.next()).charAt(0) != '-') {
                    properties.setProperty(Option.METRIC.toString(), clielement);
                } else {
                    usage();
                    System.err.println(Error.MISSING_ARG.description(Option.METRIC.option()));
                    System.exit(Error.MISSING_ARG.errcode());
                }
            } else if (Option.FUZZ.option().equals(clielement)) {
                if (iterator.hasNext() && (clielement = iterator.next()).charAt(0) != '-') {
                    properties.setProperty(Option.FUZZ.toString(), clielement);
                } else {
                    usage();
                    System.err.println(Error.MISSING_ARG.description(Option.FUZZ.option()));
                    System.exit(Error.MISSING_ARG.errcode());
                }
            } else if (Option.QUALITY.option().equals(clielement)) {
                if (iterator.hasNext() && (clielement = iterator.next()).charAt(0) != '-') {
                    properties.setProperty(Option.QUALITY.toString(), clielement);
                } else {
                    usage();
                    System.err.println(Error.MISSING_ARG.description(Option.QUALITY.option()));
                    System.exit(Error.MISSING_ARG.errcode());
                }
            } else if (Option.VERBOSE.option().equals(clielement)) {
                properties.setProperty(Option.VERBOSE.toString(), Boolean.TRUE.toString());
            } else if (Option.DEBUG.option().equals(clielement)) {
                properties.setProperty(Option.DEBUG.toString(), Boolean.TRUE.toString());
            } else if (Option.LOG.option().equals(clielement)) {
                if (iterator.hasNext() && (clielement = iterator.next()).charAt(0) != '-') {
                    properties.setProperty(Option.LOG.toString(), clielement);
                } else {
                    usage();
                    System.err.println(Error.MISSING_ARG.description(Option.LOG.option()));
                    System.exit(Error.MISSING_ARG.errcode());
                }
            } else {
                usage();
                System.err.println(Error.UNKNOWN_ARG.description(clielement));
                System.exit(Error.UNKNOWN_ARG.errcode());
            }
        }

        return properties;
    }

    /**
     * Build and display a usage message
     */
    public void usage() {
        String        headerformat = "usage: java -jar %s.jar <%s> [%s] %s <input-file>\n";
        String        cmdformat    = "\t\t%s\t%s\n";
        String        optformat    = "\t\t%s\t\t%s\n";
        StringBuilder helpmsg      = new StringBuilder();

        helpmsg.append(String.format(headerformat, Fic.class.getSimpleName(), Command.class.getSimpleName(),
                                     Option.class.getSimpleName(), Option.INPUT.option()));
        helpmsg.append("\n\tCommands:\n");

        for (Command cmd : Command.values()) {
            helpmsg.append(String.format(cmdformat, cmd.option(), cmd.description()));
        }

        helpmsg.append("\n\tOptions:\n");

        for (Option opt : Option.values()) {
            helpmsg.append(String.format(optformat, opt.option(), opt.description()));
        }

        System.out.println(helpmsg.toString());
    }
}

//  /**
//   * Validate properties attributes and init variables
//   */
//  private void validateAndInitProperties() {
//      String validatingfmt = ":: Validating: %s ..";
//
//      // set debug and verbose variables and redirections
//      debug = Boolean.parseBoolean(properties.getProperty(Option.DEBUG.toString()));
//
//      if (debug) {
//          verbose = true;
//
//          if (properties.containsKey(Option.LOG)) {
//              String logfile = properties.getProperty(Option.LOG.toString());
//
//              try {
//                  LOGGER.addHandler(new FileHandler(logfile));
//              } catch (IOException ex) {
//                  LOGGER.log(Level.WARNING, String.format("==> ERROR[io]: Cannot write logfile: %s", logfile));
//              } catch (SecurityException ex) {
//                  LOGGER.log(Level.WARNING, String.format("==> ERROR[sec]: Cannot write logfile: %s", logfile));
//              }
//          }
//      } else {
//          verbose = Boolean.parseBoolean(properties.getProperty(Option.VERBOSE.toString()));
//      }
//
//      // check if a command was provided
//      if (debug) {
//          LOGGER.log(Level.INFO, String.format(validatingfmt, Command.ID));
//      }
//
//      if (!properties.containsKey(Command.ID)) {
//          usage();
//          System.err.println(Error.REQUIRED_ARG_NOT_FOUND.description(Command.ID));
//          System.exit(Error.REQUIRED_ARG_NOT_FOUND.errcode());
//      }
//
//      // check input file
//      if (debug) {
//          LOGGER.log(Level.INFO, String.format(validatingfmt, Option.INPUT));
//      }
//
//      if (!properties.containsKey(Option.INPUT.toString())) {
//          usage();
//          System.err.println(Error.REQUIRED_ARG_NOT_FOUND.description(Option.INPUT.option()));
//          System.exit(Error.REQUIRED_ARG_NOT_FOUND.errcode());
//      }
//
//      inputfile = new File(properties.getProperty(Option.INPUT.toString()));
//
//      if (!(inputfile.exists() && inputfile.canRead() && inputfile.isFile())) {
//          usage();
//          System.err.println(Error.FILE_READ.description(inputfile.toString()));
//          System.exit(Error.FILE_READ.errcode());
//      }
//
//      // check metric consistency
//      if (debug) {
//          LOGGER.log(Level.INFO, String.format(validatingfmt, Option.METRIC));
//      }
//
//      String metricstr = properties.getProperty(Option.METRIC.toString());
//
//      try {
//          metric = Metric.valueOf(metricstr);
//      } catch (IllegalArgumentException iae) {
//          usage();
//          System.err.println(Error.INVALID_VALUE.description(Option.METRIC.option(), metricstr));
//          System.exit(Error.INVALID_VALUE.errcode());
//      }
//
//      // check fuzz consistency
//      if (debug) {
//          LOGGER.log(Level.INFO, String.format(validatingfmt, Option.FUZZ));
//      }
//
//      String fuzzstr = properties.getProperty(Option.FUZZ.toString());
//
//      try {
//          fuzz = Double.parseDouble(fuzzstr);
//      } catch (NumberFormatException nfe) {
//          usage();
//          System.err.println(Error.INVALID_VALUE.description(Option.FUZZ.option(), fuzzstr));
//          System.exit(Error.INVALID_VALUE.errcode());
//      }
//
//      // check quality consistency
//      if (debug) {
//          LOGGER.log(Level.INFO, String.format(validatingfmt, Option.QUALITY));
//      }
//
//      String qualitystr = properties.getProperty(Option.QUALITY.toString());
//
//      try {
//          quality = Double.parseDouble(qualitystr);
//
//          if ((quality <= 0) || (quality > Option.MaxQuality)) {
//              throw new NumberFormatException();
//          }
//
//          // set quality in space (0, 1]
//          quality /= Option.MaxQuality;
//      } catch (NumberFormatException nfe) {
//          usage();
//          System.err.println(Error.INVALID_VALUE.description(Option.QUALITY.option(), qualitystr));
//          System.exit(Error.INVALID_VALUE.errcode());
//      }
//  }
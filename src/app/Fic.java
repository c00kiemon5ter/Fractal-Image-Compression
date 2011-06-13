package app;

import java.io.IOException;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.Compressor;
import lib.Decompressor;
import lib.comparison.Comparator;
import lib.comparison.ImageComparator;
import lib.comparison.Metric;
import lib.tilers.AdaptiveRectangularTiler;
import lib.tilers.Tiler;

/**
 * Command line utility to compress an image using
 * fractal image compression methods. 
 * 
 * @author c00kiemon5ter
 */
public class Fic {

	/**
	 * the system logger
	 */
	private static final Logger LOGGER = Logger.getLogger("debugger");
	/**
	 * Output control - Verbose and Debug messages
	 */
	private static boolean DEBUG, VERBOSE;

	/**
	 * Do some work, start the app, read the args, validate state and run.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Fic fic = new Fic();
		fic.parseCli(args);
		fic.validateAndInitProperties();
		fic.createAndRunTask();
	}
	/**
	 * the properties table holds the configuration settings
	 */
	private Properties properties;
	/** 
	 * Options from the command line
	 */
	private double fuzz, quality;
	private Metric metric;

	/**
	 * The fic object is an instance of the application. 
	 * The constructor initializes the properties table
	 * with the default values.
	 */
	public Fic() {
		this.properties = new Properties() {{
			setProperty(Option.OUTPUT.toString(), "output.fic");
			setProperty(Option.METRIC.toString(), "AE");
			setProperty(Option.FUZZ.toString(), "5");
			setProperty(Option.QUALITY.toString(), "0.9");
			setProperty(Option.VERBOSE.toString(), Boolean.FALSE.toString());
			setProperty(Option.DEBUG.toString(), Boolean.FALSE.toString());
		}};
	}

	/**
	 * Run the command with the appropriate options
	 */
	private void createAndRunTask() {
		Runnable task = null;
		switch (Command.valueOf(properties.getProperty(Command.ID))) {
			case COMPRESS:
				if (VERBOSE) {
					LOGGER.log(Level.INFO, ":: Initializing compress process..");
				}
				task = new Compressor();
				break;
			case DECOMPRESS:
				if (VERBOSE) {
					LOGGER.log(Level.INFO, ":: Initializing decompress process..");
				}
				task = new Decompressor();
				break;
		}

		assert task != null : "==> ERROR[null]: No task initialized";
		if (DEBUG) {
			LOGGER.log(Level.INFO, ":: Initialized task. Starting thread execution..");
		}
		// FIXME: for now just run the tests
		//ExecutorService executor = Executors.newSingleThreadExecutor();
		//executor.execute(task);
		new TestTask(properties).run();
	}

	/**
	 * Validate properties attributes
	 */
	private void validateAndInitProperties() {
		String validatingfmt = ":: Validating: %s ..";
		
		// set debug and verbose variables and redirections
		DEBUG = Boolean.parseBoolean(properties.getProperty(Option.DEBUG.toString()));
		if (DEBUG) {
			VERBOSE = true;
			if (properties.containsKey(Option.LOG)) {
				String logfile = properties.getProperty(Option.LOG.toString());
				try {
					LOGGER.addHandler(new FileHandler(logfile));
				} catch (IOException ex) {
					LOGGER.log(Level.WARNING, String.format("==> ERROR[io]: Cannot write logfile: %s", logfile));
				} catch (SecurityException ex) {
					LOGGER.log(Level.WARNING, String.format("==> ERROR[sec]: Cannot write logfile: %s", logfile));
				}
			}
		} else {
			VERBOSE = Boolean.parseBoolean(properties.getProperty(Option.VERBOSE.toString()));
		}
		
		if (DEBUG) {
			LOGGER.log(Level.INFO, String.format(validatingfmt, Command.ID));
		}
		if (!properties.containsKey(Command.ID)) {
			usage();
			System.err.println(Error.REQUIRED_ARG_NOT_FOUND.description(Command.ID));
			System.exit(Error.REQUIRED_ARG_NOT_FOUND.errcode());
		}

		if (DEBUG) {
			LOGGER.log(Level.INFO, String.format(validatingfmt, Option.INPUT));
		}
		if (!properties.containsKey(Option.INPUT.toString())) {
			usage();
			System.err.println(Error.REQUIRED_ARG_NOT_FOUND.description(Option.INPUT.option()));
			System.exit(Error.REQUIRED_ARG_NOT_FOUND.errcode());
		}

		if (DEBUG) {
			LOGGER.log(Level.INFO, String.format(validatingfmt, Option.METRIC));
		}
		String metricstr = properties.getProperty(Option.METRIC.toString());
		try {
			metric = Metric.valueOf(metricstr);
		} catch (IllegalArgumentException iae) {
			usage();
			System.err.println(Error.INVALID_VALUE.description(Option.METRIC.option(), metricstr));
			System.exit(Error.INVALID_VALUE.errcode());
		}
		
		if (DEBUG) {
			LOGGER.log(Level.INFO, String.format(validatingfmt, Option.FUZZ));
		}
		String fuzzstr = properties.getProperty(Option.FUZZ.toString());
		try {
			fuzz = Double.parseDouble(fuzzstr);
		} catch (NumberFormatException nfe) {
			usage();
			System.err.println(Error.INVALID_VALUE.description(Option.FUZZ.option(), fuzzstr));
			System.exit(Error.INVALID_VALUE.errcode());
		}

		if (DEBUG) {
			LOGGER.log(Level.INFO, String.format(validatingfmt, Option.QUALITY));
		}
		String qualitystr = properties.getProperty(Option.QUALITY.toString());
		try {
			quality = Double.parseDouble(qualitystr);
		} catch (NumberFormatException nfe) {
			usage();
			System.err.println(Error.INVALID_VALUE.description(Option.QUALITY.option(), qualitystr));
			System.exit(Error.INVALID_VALUE.errcode());
		}
	}

	/**
	 * Parse command line options 
	 */
	private void parseCli(final String[] args) {
		if (args.length == 0) {
			usage();
			System.err.println(Error.ARG_COUNT.description(args.length));
			System.exit(Error.ARG_COUNT.errcode());
		}

		ListIterator<String> iterator = Arrays.asList(args).listIterator();
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
	}

	/**
	 * Build and display a usage message
	 */
	private void usage() {
		String headerformat = "usage: java -jar %s.jar <%s> [%s] %s <input-file>\n";
		String cmdformat = "\t\t%s\t%s\n";
		String optformat = "\t\t%s\t\t%s\n";
		StringBuilder helpmsg = new StringBuilder();
		helpmsg.append(String.format(headerformat, Fic.class.getSimpleName(),
									 Command.class.getSimpleName(),
									 Option.class.getSimpleName(),
									 Option.INPUT.option()));
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

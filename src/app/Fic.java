package app;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Properties;

/**
 * Command line utility to compress an image using
 * fractal image compression methods. 
 * 
 * @author c00kiemon5ter
 */
public class Fic {

	/**
	 * the properties table holds the configuration settings
	 */
	private Properties properties;

	/**
	 * Do some work, start the app, read the args, validate state and run.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Fic fic = new Fic();
		fic.parseCli(args);
		fic.validateProperties();
		fic.createAndRunTask();
	}

	/**
	 * The fic object is an instance of the application. 
	 * The constructor initializes the properties table
	 * with the default values.
	 */
	public Fic() {
		this.properties = new Properties() {{
			setProperty(Opts.OUTPUT.toString(), "output.fic");
			setProperty(Opts.QUALITY.toString(), "80");
			setProperty(Opts.VERBOSE.toString(), Boolean.FALSE.toString());
			setProperty(Opts.DEBUG.toString(), Boolean.FALSE.toString());
		}};
	}

	/**
	 * Run the command with the appropriate options
	 */
	private void createAndRunTask() {
		Runnable task = null;
		switch (Cmds.valueOf(properties.getProperty(Cmds.ID))) {
			// TODO: initialize the commands, call the lib
			case COMPRESS:
				break;
			case DECOMPRESS:
				break;
		}
		assert task != null : "==> ERROR: No task initialized";
		task.run();
	}

	/**
	 * Validate properties attributes
	 */
	private void validateProperties() {
		if (properties.getProperty(Cmds.ID) == null) {
			usage();
			System.err.println(Err.REQUIRED_ARG_NOT_FOUND.description(Cmds.ID));
			System.exit(Err.REQUIRED_ARG_NOT_FOUND.errcode());
		}

		if (properties.getProperty(Opts.INPUT.toString()) == null) {
			usage();
			System.err.println(Err.REQUIRED_ARG_NOT_FOUND.description(Opts.INPUT.option()));
			System.exit(Err.REQUIRED_ARG_NOT_FOUND.errcode());
		}

		String qualitystr = properties.getProperty(Opts.QUALITY.toString());
		try {
			Double.parseDouble(qualitystr);
		} catch (NumberFormatException nfe) {
			usage();
			System.err.println(Err.QUALITY_FORMAT.description(qualitystr));
			System.exit(Err.QUALITY_FORMAT.errcode());
		}
	}

	/**
	 * Parse command line options 
	 */
	private void parseCli(final String[] args) {
		if (args.length == 0) {
			usage();
			System.err.println(Err.ARG_COUNT.description(args.length));
			System.exit(Err.ARG_COUNT.errcode());
		}

		ListIterator<String> iterator = Arrays.asList(args).listIterator();
		while (iterator.hasNext()) {
			String clielement = iterator.next();

			if (Opts.HELP.option().equals(clielement)) {
				usage();
				System.exit(0);
			} else if (Cmds.COMPRESS.option().equals(clielement)) {
				properties.setProperty(Cmds.ID, Cmds.COMPRESS.toString());
			} else if (Cmds.DECOMPRESS.option().equals(clielement)) {
				properties.setProperty(Cmds.ID, Cmds.DECOMPRESS.toString());
			} else if (Opts.INPUT.option().equals(clielement)) {
				if (iterator.hasNext()) {
					properties.setProperty(Opts.INPUT.toString(), iterator.next());
				} else {
					usage();
					System.err.println(Err.MISSING_ARG.description(Opts.INPUT.option()));
					System.exit(Err.MISSING_ARG.errcode());
				}
			} else if (Opts.OUTPUT.option().equals(clielement)) {
				if (iterator.hasNext()) {
					properties.setProperty(Opts.OUTPUT.toString(), iterator.next());
				} else {
					usage();
					System.err.println(Err.MISSING_ARG.description(Opts.OUTPUT.option()));
					System.exit(Err.MISSING_ARG.errcode());
				}
			} else if (Opts.QUALITY.option().equals(clielement)) {
				if (iterator.hasNext()) {
					properties.setProperty(Opts.QUALITY.toString(), iterator.next());
				} else {
					usage();
					System.err.println(Err.MISSING_ARG.description(Opts.QUALITY.option()));
					System.exit(Err.MISSING_ARG.errcode());
				}
			} else if (Opts.VERBOSE.option().equals(clielement)) {
				properties.setProperty(Opts.VERBOSE.toString(), Boolean.TRUE.toString());
			} else if (Opts.DEBUG.option().equals(clielement)) {
				properties.setProperty(Opts.DEBUG.toString(), Boolean.TRUE.toString());
			} else {
				usage();
				System.err.println(Err.UNKNOWN_ARG.description(clielement));
				System.exit(Err.UNKNOWN_ARG.errcode());
			}
		}
	}

	/**
	 * Build and display a usage message
	 */
	private void usage() {
		String headerformat = "usage: java -jar %s.jar <command> [options] %s <input-file>\n";
		String cmdformat = "\t\t%s\t%s\n";
		String optformat = "\t\t%s\t\t%s\n";
		StringBuilder helpmsg = new StringBuilder();
		helpmsg.append(String.format(headerformat, Fic.class.getSimpleName(), Opts.INPUT.option()));
		helpmsg.append("\n\tCommands:\n");
		for (Cmds cmd : Cmds.values()) {
			helpmsg.append(String.format(cmdformat, cmd.option(), cmd.description()));
		}
		helpmsg.append("\n\tOptions:\n");
		for (Opts opt : Opts.values()) {
			helpmsg.append(String.format(optformat, opt.option(), opt.description()));
		}
		System.out.println(helpmsg.toString());
	}
}

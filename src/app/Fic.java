package app;

import app.task.Task;
import app.task.TestTask;
import app.task.CompressTask;
import app.task.DecompressTask;

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

	public static void main(String[] args) {
		Properties properties = parseCli(args);
		validateProperties(properties);
		createAndRunTask(properties);
	}

	/**
	 * Run the command with the appropriate options
	 * 
	 * @param properties 
	 */
	private static void createAndRunTask(Properties properties) {
		Task task = null;
		switch (Cmds.valueOf(properties.getProperty(Cmds.ID))) {
			case COMPRESS:
				task = new CompressTask(properties);
				break;
			case DECOMPRESS:
				task = new DecompressTask(properties);
				break;
		}
		// NOTE: for now, just run the tests
		task = new TestTask(properties);
		task.run();
	}

	/**
	 * Validate properties table
	 * 
	 * @param properties the table of properties to validate
	 */
	private static void validateProperties(Properties properties) {
		if (properties.isEmpty()) {
			usage();
			System.err.println(Err.ARG_COUNT.description(properties.size()));
			System.exit(Err.ARG_COUNT.errcode());
		}
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
		if (qualitystr != null) {
			try {
				Integer.parseInt(qualitystr);
			} catch (NumberFormatException nfe) {
				usage();
				System.err.println(Err.QUALITY_FORMAT.description(qualitystr));
				System.exit(Err.QUALITY_FORMAT.errcode());
			}
		}
	}

	/**
	 * Parse command line options 
	 * 
	 * @param args the command line arguments to parse
	 * @return the arguments stored in a property table
	 */
	private static Properties parseCli(String[] args) {
		ListIterator<String> iterator = Arrays.asList(args).listIterator();
		Properties properties = new Properties();

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
				properties.setProperty(Opts.VERBOSE.toString(), clielement);
			} else if (Opts.DEBUG.option().equals(clielement)) {
				properties.setProperty(Opts.DEBUG.toString(), clielement);
			} else {
				usage();
				System.err.println(Err.UNKNOWN_ARG.description(clielement));
				System.exit(Err.UNKNOWN_ARG.errcode());
			}
		}

		return properties;
	}

	/**
	 * Build and display a usage message
	 */
	private static void usage() {
		String headerformat = "usage: java -jar %s.jar <command> [options] %s <input-file>\n";
		String propformat = "\t\t%s\t%s\n";
		StringBuilder helpmsg = new StringBuilder();
		helpmsg.append(String.format(headerformat, Fic.class.getSimpleName(), Opts.INPUT.option()));
		helpmsg.append("\n\tCommands:\n");
		for (Cmds cmd : Cmds.values()) {
			helpmsg.append(String.format(propformat, cmd.option(), cmd.description()));
		}
		helpmsg.append("\n\tOptions:\n");
		for (Opts opt : Opts.values()) {
			helpmsg.append(String.format(propformat, opt.option(), opt.description()));
		}
		System.out.println(helpmsg.toString());
	}
}

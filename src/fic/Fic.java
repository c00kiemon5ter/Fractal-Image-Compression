package fic;

/**
 *
 * @author c00kiemon5ter
 */
public class Fic {

	public static void main(String[] args) {
		if (args.length != 0 && args[0].equals(Opts.HELP.toString())) {
			usage();
			System.exit(0);
		} else if (args.length != 3) {
			usage();
			System.err.println(Err.ARG_COUNT.description(args.length));
			System.exit(Err.ARG_COUNT.errcode());
		} else if (args[0].equals(Opts.COMPRESS.toString())) {
			new FicTask().compress(args[1], args[2]);
		} else if (args[0].equals(Opts.DECOMPRESS.toString())) {
			new FicTask().decompress(args[1], args[2]);
		} else {
			usage();
			System.err.println(Err.UNKNOWN_OPT.description(args[0]));
			System.exit(Err.UNKNOWN_OPT.errcode());
		}
		System.exit(0);
	}

	private static void usage() {
		String headerformat = "usage: java -jar %s [option] [file] [file]\n\n\tOptions:\n";
		String optformat = "\t\t%s\t%s\n";
		StringBuilder helpmsg = new StringBuilder();
		helpmsg.append(String.format(headerformat, Fic.class.getSimpleName()));
		for (Opts opt : Opts.values()) {
			helpmsg.append(String.format(optformat, opt, opt.description()));
		}
		System.out.println(helpmsg.toString());
	}
}

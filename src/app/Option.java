package app;

/**
 * Representation of available options to the app
 * 
 * @author c00kiemon5ter
 */
public enum Option {

	HELP("display this help message"),
	INPUT("the input file"),
	OUTPUT("the ouput file"),
	QUALITY("the quality {1..100}"),
	VERBOSE("enable verbose output"),
	DEBUG("show debug messages"),
	LOG("also write debug messages to logfile"),;
	private String description;

	Option(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return description of the option
	 */
	public String description() {
		return this.description;
	}

	/**
	 * 
	 * @return the option represented
	 */
	public String option() {
		return '-' + this.name().toLowerCase();
	}
}

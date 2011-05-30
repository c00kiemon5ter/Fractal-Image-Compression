package app;

/**
 * Representation of available options to the app
 * 
 * @author c00kiemon5ter
 */
public enum Opts {

	HELP("display this help message"),
	INPUT("input file"),
	OUTPUT("ouput file"),
	QUALITY("quality of operation"),
	DEBUG("show debug messages"),
	VERBOSE("verbose output"),;
	private String description;

	Opts(String description) {
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
		return this.name().toLowerCase();
	}
}

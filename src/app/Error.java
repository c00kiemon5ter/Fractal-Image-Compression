package app;

/**
 * Representation of possible errors coupled with error codes
 * 
 * @author c00kiemon5ter
 */
public enum Error {

	ARG_COUNT("Unexpected argument count: %d"),
	UNKNOWN_ARG("Unknown argument: %s"),
	REQUIRED_ARG_NOT_FOUND("Required argument missing: %s"),
	MISSING_ARG("Missing argument for option: %s"),
	INVALID_VALUE("Invalid value for option: %s %s"),
	IMAGE_NOT_FOUND("Couldn't read image: %s"),;
	private String description;

	Error(String description) {
		this.description = String.format("==> ERROR: %s", description);
	}

	/**
	 * 
	 * @return a description of the error
	 */
	public String description() {
		return this.description;
	}

	/**
	 * 
	 * @param msg extra message to attach
	 * @return a description of the error
	 */
	public String description(String msg) {
		return String.format(this.description, msg);
	}
	
	/**
	 * 
	 * @param opt specify the option
	 * @param val value of the option
	 * @return a description of the error
	 */
	public String description(String opt, String val) {
		return String.format(this.description, opt, val);
	}

	/**
	 * 
	 * @param num a number to attach
	 * @return a description of the error
	 */
	public String description(int num) {
		return String.format(this.description, num);
	}

	/**
	 * The error code representing the error. 
	 * This will change along with the ordering of the errors.
	 * @return the error's code
	 */
	public int errcode() {
		return this.ordinal() + 1;
		//return this.name().hashCode();
	}
}

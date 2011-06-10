package app;

/**
 * Representation of the commands available to the app.
 * 
 * @author c00kiemon5ter
 */
public enum Command {

	COMPRESS("compress image to file"),
	DECOMPRESS("decompress file to image"),;
	public static final String ID = "COMMAND";
	private String description;

	Command(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return a description of the command
	 */
	public String description() {
		return this.description;
	}

	/**
	 * 
	 * @return the option representing the command
	 */
	public String option() {
		return '-' + this.name().toLowerCase();
	}
}

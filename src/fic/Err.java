package fic;

/**
 *
 * @author c00kiemon5ter
 */
public enum Err {

	ARG_COUNT("Unexpected argument count: %d"),
	UNKNOWN_OPT("Unknown option: %s"),
	IMAGE_NOT_FOUND("Couldn't read image: %s"),;
	private String description;

	Err(String description) {
		this.description = String.format("ERROR: %s\n", description);
	}

	public String description() {
		return this.description;
	}

	public String description(String msg) {
		return String.format(this.description, msg);
	}

	public String description(int num) {
		return String.format(this.description, num);
	}

	int errcode() {
		return this.ordinal() + 1;
		//return this.hashCode();
	}
}

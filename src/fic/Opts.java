package fic;

/**
 *
 * @author c00kiemon5ter
 */
public enum Opts {

	HELP("display this help message"),
	COMPRESS("compress image to file"),
	DECOMPRESS("decompress file to image"),;
	private String description;

	Opts(String description) {
		this.description = description;
	}

	public String description() {
		return this.description;
	}

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}

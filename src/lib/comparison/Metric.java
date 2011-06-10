package lib.comparison;

/**
 * different metrics to count the difference of two images
 */
public enum Metric {

	AE,
	PAE,
	PSNR("inf"),
	MAE,
	MSE,
	RMSE,
	MEPP,
	FUZZ,
	NCC("1"),;
	private String equalval;

	Metric() {
		this.equalval = "0";
	}

	Metric(String equalval) {
		this.equalval = equalval;
	}

	/**
	 * 
	 * @return the value that the metric provides to denote 
	 * that there is no difference between the given images
	 */
	public String equalval() {
		return this.equalval;
	}
}

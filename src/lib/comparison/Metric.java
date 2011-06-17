package lib.comparison;

/**
 * different metrics to count the difference of two images
 */
public enum Metric {

	AE,   //   absolute error count, number of different pixels (-fuzz effected)
	FUZZ, //   mean color distance
	MAE,  //   mean absolute error (normalized), average channel error distance
	MEPP, //   mean error per pixel (normalized mean error, normalized peak error)
	MSE,  //   mean error squared, average of the channel error squared
	NCC,  //   normalized cross correlation
	PAE,  //   peak absolute (normalize peak absolute)
	PSNR, //   peak signal to noise ratio
	RMSE, //   root mean squared (normalized root mean squared)
	;
}

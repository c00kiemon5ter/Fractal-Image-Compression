package lib.comparison;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.core.ImageMagickCmd;
import org.im4java.process.ArrayListOutputConsumer;

/**
 * Compares two pictures with a given metric.
 * 
 * @see http://www.imagemagick.org/Usage/compare/
 */
public class Comparison {

	public enum Metric {

		AE, PAE, PSNR, MAE, MSE, RMSE, MEPP, FUZZ, NCC
	}
	private IMOperation operation;
	private ArrayListOutputConsumer stdout;
	private static ImageCommand command = new ImageMagickCmd("compare");

	/**
	 * Compare two images given a metric
	 * 
	 * @param metric the metric to use to compare images
	 */
	public Comparison(Metric metric) {
		this(metric, 0);
	}

	/**
	 * Compare two images given a metric
	 * 
	 * @param metric the metric to use to compare images
	 * @param fuzz the error acceptance factor
	 */
	public Comparison(Metric metric, double fuzz) {
		operation = new IMOperation();
		operation.metric(metric.name());
		operation.fuzz(fuzz);
	}

	public boolean compare(BufferedImage image1, BufferedImage image2)
			throws IOException, InterruptedException, IM4JavaException {
		stdout = new ArrayListOutputConsumer();
		command.setOutputConsumer(stdout);
		operation.addImage();
		operation.addImage();
		operation.addImage("null:-");
		command.run(operation, image1, image2);

		// FIXME: return if images match or not
		return false;
	}

	public void setVerbose() {
		operation.verbose();
	}

	public ArrayList<String> getStdout() {
		return stdout.getOutput();
	}

	public ArrayList<String> getStderr() {
		return command.getErrorText();
	}
}

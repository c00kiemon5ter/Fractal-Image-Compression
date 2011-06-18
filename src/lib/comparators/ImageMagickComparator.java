package lib.comparators;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageMagickCmd;
import org.im4java.process.ArrayListOutputConsumer;

/**
 * Compares two pictures with a given metric.
 * 
 * @see http://www.imagemagick.org/Usage/compare/
 */
public class ImageMagickComparator {

	private IMOperation operation;
	private ArrayListOutputConsumer stdout;
	private static ImageMagickCmd command = new ImageMagickCmd("compare");
	private final Metric metric;
	private double difference;

	/**
	 * Compare two images given a metric
	 * 
	 * @param metric the metric to use to compare images
	 */
	public ImageMagickComparator(Metric metric) {
		this(metric, 0.0D);
	}

	/**
	 * Compare two images given a metric and an acceptance error factor
	 * 
	 * @param metric the metric to use to compare images
	 * @param fuzz the error acceptance factor
	 */
	public ImageMagickComparator(final Metric metric, final double fuzz) {
		this.metric = metric;
		this.stdout = new ArrayListOutputConsumer();
		command.setOutputConsumer(this.stdout);
		this.operation = new IMOperation() {{
			metric(metric.name());
			fuzz(fuzz);
			addImage();
			addImage();
			addImage("null:-");
		}};
	}

	public boolean isEqual(final BufferedImage imageA, final BufferedImage imageB)
			throws IOException, InterruptedException, IM4JavaException {
		command.run(operation, imageA, imageB);
		String result = command.getErrorText().get(command.getErrorText().size() - 1).split("\\s+")[0];

		if (this.metric.equals(Metric.PSNR)) {
			if (result.equals("inf")) {
				this.difference = 0;
			} else {
				this.difference = Double.POSITIVE_INFINITY - Double.parseDouble(result);
			}
		} else if (this.metric.equals(Metric.NCC)) {
			this.difference = 1 - Double.parseDouble(result);
		} else {
			this.difference = Double.parseDouble(result);
		}
		return this.difference == 0;
	}

	public double getDifference() {
		return this.difference;
	}

	/**
	 * 
	 * @return the output printed on standard out stream
	 */
	public ArrayList<String> getStdout() {
		return stdout.getOutput();
	}

	/**
	 * 
	 * @return the output printed on standard error stream
	 */
	public ArrayList<String> getStderr() {
		return command.getErrorText();
	}
}

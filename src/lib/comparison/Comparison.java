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

	private IMOperation operation;
	private ArrayListOutputConsumer stdout;
	private static ImageCommand command = new ImageMagickCmd("compare");
	private final Metric metric;
	private String difference;

	/**
	 * Compare two images given a metric
	 * 
	 * @param metric the metric to use to compare images
	 */
	public Comparison(Metric metric) {
		this(metric, 0.0D);
	}

	/**
	 * Compare two images given a metric and an acceptance error factor
	 * 
	 * @param metric the metric to use to compare images
	 * @param fuzz the error acceptance factor
	 */
	public Comparison(Metric metric, double fuzz) {
		this.metric = metric;
		this.stdout = new ArrayListOutputConsumer();
		command.setOutputConsumer(this.stdout);
		this.operation = new IMOperation();
		this.operation.metric(metric.name());
		this.operation.fuzz(fuzz);
		this.operation.addImage();
		this.operation.addImage();
		this.operation.addImage("null:-");
	}

	/**
	 * compare the given images 
	 * 
	 * @param imageA image to compare
	 * @param imageB image to compare with imageA
	 * @return whether the images are different or not
	 */
	public boolean compare(BufferedImage imageA, BufferedImage imageB)
			throws IOException, InterruptedException, IM4JavaException {
		command.run(operation, imageA, imageB);
		this.difference = command.getErrorText().get(command.getErrorText().size() - 1).split("\\s+")[0];
		return this.metric.equalval().equals(this.difference);
	}

	/**
	 * 
	 * @return the difference between the two images
	 */
	public String getDifference() {
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

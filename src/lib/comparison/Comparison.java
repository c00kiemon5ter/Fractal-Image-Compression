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
        private Metric metric;
        private String result;
        private boolean retval;

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
                this.metric = metric;
	}

	public boolean compare(BufferedImage image1, BufferedImage image2)
			throws IOException, InterruptedException, IM4JavaException {
		stdout = new ArrayListOutputConsumer();
		command.setOutputConsumer(stdout);
		operation.addImage();
		operation.addImage();
		operation.addImage("null:-");
		command.run(operation, image1, image2);
                
                result = this.getStderr().get(this.getStderr().size()-1).
                                replaceAll("(all:\\s*)|\\(|\\)", "").trim();
                                
                retval = false;
                
                switch(metric) {
                    case AE:    retval = Integer.parseInt(result)==0; break;
                    case PAE:case MAE:case MSE:case RMSE:case MEPP:case FUZZ:
                                retval = Integer.parseInt(String.valueOf(result.
                                charAt(0)))==0; break;
                    case PSNR:  retval = result.equalsIgnoreCase("inf"); break;
                    case NCC:   retval = Integer.parseInt(result)==1; break;
                }
                
		return retval;
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

package lib.comparison;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.process.ArrayListOutputConsumer;

/**
 * Compares two pictures with a given metric.
 * @see http://www.imagemagick.org/Usage/compare/
 * @author Periklis Ntanasis
 */
public class Comparison {

	public enum Metric {

		AE, PAE, PSNR, MAE, MSE, RMSE, MEPP, FUZZ, NCC
	}
	private Metric metric;
	private int fuzz;
	private IMOperation operation;
	private ArrayList<String> arguments = new ArrayList<String>();
	private ArrayListOutputConsumer output;
	private ImageCommand compare = new ImageCommand();

	/**
	 * 
	 * @param metric Can be one of: AE, PAE, PSNR, MAE, MSE, RMSE, MEPP, FUZZ, NCC
	 */
	public Comparison(Metric metric) {
		this.metric = metric;
		arguments.add("-metric");
		arguments.add(metric.toString());
	}

	/**
	 * 
	 * @param metric Can be one of: AE, PAE, PSNR, MAE, MSE, RMSE, MEPP, FUZZ, NCC
	 * @param fuzz distance - colors within this distance are considered equal
	 */
	public Comparison(Metric metric, int fuzz) {
		this.metric = metric;
		this.fuzz = fuzz;
		arguments.add("-metric");
		arguments.add(metric.toString());
		arguments.add("-fuzz");
		arguments.add(fuzz + "%");
	}

	public boolean compare(BufferedImage image1, BufferedImage image2) {
		try {
			createOperation();
			compare.run(operation, image1, image2);
		} catch (IOException ex) {
			Logger.getLogger(Comparison.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(Comparison.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IM4JavaException ex) {
			Logger.getLogger(Comparison.class.getName()).log(Level.SEVERE, null, ex);
		}

		ArrayList<String> cmdOutput = output.getOutput();
		for (String line : cmdOutput) {
			System.out.println(line);
		}

		return false;
	}

	public void setVerboseOn() {
		arguments.add("-verbose");
	}

	private void createOperation() {
		operation = new IMOperation();
		operation.addRawArgs(arguments);
		operation.addImage();
		operation.addImage();
		operation.addImage("null:-");

		output = new ArrayListOutputConsumer();

		compare = new ImageCommand();
		compare.setOutputConsumer(output);
		compare.setCommand("compare");
	}
}

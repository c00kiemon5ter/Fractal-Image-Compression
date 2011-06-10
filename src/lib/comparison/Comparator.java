/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.comparison;

import java.io.IOException;
import java.awt.image.BufferedImage;
import org.im4java.core.IM4JavaException;

/**
 * Comparator compares the given images using different metrics 
 * 
 * @see Comparison.Metric
 * @see Comparison
 */
interface Comparator {

	/**
	 * compare the given images 
	 * 
	 * @param imageA image to compare
	 * @param imageB image to compare with imageA
	 * @return whether the images are different or not
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException  
	 */
	public boolean compare(BufferedImage imageA, BufferedImage imageB)
			throws IOException, InterruptedException, IM4JavaException;
}

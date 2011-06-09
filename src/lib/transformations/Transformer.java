package lib.transformations;

import java.awt.Image;

/**
 *
 * @param <T> 
 * @author c00kiemon5ter
 */
public interface Transformer<T extends Image> {

	public T transform(final T inputimage);
}

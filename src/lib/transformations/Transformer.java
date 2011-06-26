package lib.transformations;

import java.awt.Image;

public interface Transformer<T extends Image> {

    public T transform(final T inputimage);
}

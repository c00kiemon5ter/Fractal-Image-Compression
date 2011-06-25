package app.configuration.converters;

import com.beust.jcommander.IStringConverter;
import lib.transformations.ScaleTransform;

/**
 * a converter that turns a series of string arguments into a scale transform
 * 
 * @see ScaleTransform
 */
public class ScaleTransformConverter implements IStringConverter<ScaleTransform> {

    @Override
    public ScaleTransform convert(String arg) {
        String[] sf = arg.split(":");
        return new ScaleTransform(Double.parseDouble(sf[0]), Double.parseDouble(sf[1]));
    }
}

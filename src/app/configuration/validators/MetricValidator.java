package app.configuration.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import lib.comparators.Metric;

/**
 * validate the given value is a Metric 
 * 
 * @see Metric
 */
public class MetricValidator implements IParameterValidator {

    @Override
    public void validate(String arg, String val) throws ParameterException {
        try {
            Metric.valueOf(val.toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new ParameterException(String.format("Parameter %s has an invalid value: %s", arg, val));
        }
    }
}

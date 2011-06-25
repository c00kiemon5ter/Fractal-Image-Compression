package app.configuration.converters;

import com.beust.jcommander.IStringConverter;
import lib.comparators.Metric;

/**
 * a converter that turns a string argument into a Metric
 * 
 * @see Metric
 */
public class MetricConverter implements IStringConverter<Metric> {

    @Override
    public Metric convert(String metric) {
        return Metric.valueOf(metric.toUpperCase());
    }
}

package app.configuration.validators;

import app.configuration.Options;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 
 */
public class TilerValidator implements IParameterValidator {

    @Override
    public void validate(String arg, String val) throws ParameterException {
        String[] values = val.split(Options.tilerdelimit);
        for (String num : values) {
            try {
                if (values.length != 2 || num.isEmpty() || Double.parseDouble(num) <= 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                throw new ParameterException(String.format("Parameter %s has an invalid value: %s", arg, num));
            }
        }
    }
}

package app.configuration.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
 * validate the given value is a file that exists on the filesystem
 */
public class FileExistsValidator implements IParameterValidator {

    @Override
    public void validate(String arg, String val) throws ParameterException {
        File file = new File(val);
        if (!file.exists()) {
            throw new ParameterException(String.format("File for %s doesn't exist: %s", arg, val));
        }
    }
}

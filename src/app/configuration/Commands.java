package app.configuration;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.io.File;

/**
 * The available commands to the system
 */
@Parameters(commandDescription = "compress and decompress commands")
public enum Commands {

    @Parameter(names       = "compress", 
               description = "compress the given image",
               hidden      = true)
    COMPRESS,
    @Parameter(names       = "decompress",
               description = "decompress the given file",
               hidden      = true)
    DECOMPRESS;

    @Parameter(names       = {"-i", "--input"}, 
               description = "the input file", 
               required    = true)
    protected File input;

    @Parameter(names       = {"-o", "--output"}, 
               description = "the output file")
    protected File output;

    /**
     * the command identifier is a unique string for the command
     * 
     * @return the command identifier
     */
    public String id() {
        return this.name().toLowerCase();
    }
}


package lib.transformations;

import java.io.*; 
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand; 

/**
 * Produces transformed copies of a given input image. The copies are saved as "id+output.getName()" under the given output directory.
 * 
 * @author uberspot
 */
public class AffineTransformer {
    
    private File input, output;
    private IMOperation operation; 
    private static ImageCommand command = new ConvertCmd();
    
    private boolean verbose;
    
    public AffineTransformer(String input, String output){
                this(new File(input), new File(output));
    }

    public AffineTransformer(File input, File output) {
                this.input = input; 
                this.output = output; 
                verbose = false;
                operation = new IMOperation();
    }
    
    /**
     * Transforms the input image.
     * @param id the sequence number that will be added to the output image name
     * @param scalex
     * @param scaley
     * @param shearx
     * @param sheary 
     */
    public void transform(int id, double scalex, double scaley, double shearx, double sheary) {  
                operation = new IMOperation(); //clear old operations
                
                operation.addImage(input.getPath());
                if(verbose) 
                    operation.verbose();
                operation.mattecolor(); 
                operation.virtualPixel();  
                operation.affine(scalex, shearx, sheary, scaley, 0.0, 0.0);
                operation.transform(); 
                operation.addImage(output.getParent() + File.separator + id + "_"+ output.getName()); 
                try {
                        command.run(operation);
                } catch (IOException ex) {
                        System.err.printf("Couldn't run op: affine ioe\n" + ex + "\n");
                } catch (InterruptedException ex) {
                        System.err.printf("Couldn't run op: affine ie\n" + ex + "\n");
                } catch (IM4JavaException ex) {
                        System.err.printf("Couldn't run op: affine im4jve\n" + ex + "\n");
                }
    }
    
    public void setVerbose() {
                this.verbose = true;
    }
    
    public void flip(int id){
        this.transform(id, 1.0, -1.0, 0.0, 0.0);
    }
    
    public void flop(int id){
        this.transform(id, -1.0, 1.0, 0.0, 0.0);
    }
    
    public void rotate(int id, int degrees){
                operation = new IMOperation(); //clear old operations
                
                operation.addImage(input.getPath());
                if(verbose) 
                    operation.verbose();
                operation.background("black");
                operation.virtualPixel("background");  
                operation.distort("ScaleRotateTranslate", ""+degrees);
                operation.addImage(output.getParent() + File.separator + id + "_"+ output.getName()); 
                try {
                        command.run(operation);
                } catch (IOException ex) {
                        System.err.printf("Couldn't run op: affine ioe\n" + ex + "\n");
                } catch (InterruptedException ex) {
                        System.err.printf("Couldn't run op: affine ie\n" + ex + "\n");
                } catch (IM4JavaException ex) {
                        System.err.printf("Couldn't run op: affine im4jve\n" + ex + "\n");
                }
    }
    
    public void shrinkToHalf(int id){
        this.transform(id, 0.5, 0.5, 0.0, 0.0);
    }
    
    public void shear(){
        //ToDo...
    }

    /**
     * Apply every given transformation
     * 
     * @param initialID 
     */
    public void applyAllTransforms(int initialID) {
        this.flip(initialID++);
        this.flop(initialID++);
        this.shrinkToHalf(initialID++);
        for(int i=30; i<330; i+=30){
           this.rotate(initialID++, i); 
        }
        
    }
}

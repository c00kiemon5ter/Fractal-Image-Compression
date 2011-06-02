package lib.transformations;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;

/**
 * Produces transformed copies of a given input image.
 */
public class ImageTransformer {

	private enum DefaultAffinePropertiesMaps {

		FLIP(1.0, -1.0, 0.0, 0.0),
		FLOP(-1.0, 1.0, 0.0, 0.0),
		SHRINK_HALF(0.5, 0.5, 0.0, 0.0),
		SHEAR(0.0, 0.0, 0.0, 0.0),; // FIXME: shear affineTransform
		private AffinePropertiesMap properties;

		private DefaultAffinePropertiesMaps(double scalex, double scaley,
											double shearx, double sheary) {
			this.properties = new AffinePropertiesMap();
			this.properties.setProperty(AffineProperty.SCALE_X, scalex);
			this.properties.setProperty(AffineProperty.SCALE_Y, scaley);
			this.properties.setProperty(AffineProperty.SHEAR_X, shearx);
			this.properties.setProperty(AffineProperty.SHEAR_Y, sheary);
		}

		public AffinePropertiesMap properties() {
			return this.properties;
		}
	}
	private static ConvertCmd command = new ConvertCmd();

	public static BufferedImage affineTransform(BufferedImage inputimage,
												AffinePropertiesMap properties)
			throws IOException, InterruptedException, IM4JavaException {
		return affineTransform(inputimage,
							   properties.getPropery(AffineProperty.SCALE_X),
							   properties.getPropery(AffineProperty.SCALE_Y),
							   properties.getPropery(AffineProperty.SHEAR_X),
							   properties.getPropery(AffineProperty.SHEAR_Y));
	}

	public static BufferedImage affineTransform(BufferedImage inputimage,
												double scalex, double scaley,
												double shearx, double sheary)
			throws IOException, InterruptedException, IM4JavaException {
		IMOperation operation = new IMOperation();
		operation.addImage("data/lena.png");
		operation.mattecolor();
		operation.virtualPixel();
		operation.affine(scalex, shearx, sheary, scaley, 0.0, 0.0);
		operation.transform();
		operation.addImage(":-");

		Stream2BufferedImage streamToBuffImg = new Stream2BufferedImage();
		command.setOutputConsumer(streamToBuffImg);
		command.run(operation);

		return streamToBuffImg.getImage();
	}

	public static BufferedImage flip(BufferedImage inputimage)
			throws IOException, InterruptedException, IM4JavaException {
		return affineTransform(inputimage, DefaultAffinePropertiesMaps.FLIP.properties);
	}

	public static BufferedImage flop(BufferedImage inputimage)
			throws IOException, InterruptedException, IM4JavaException {
		return affineTransform(inputimage, DefaultAffinePropertiesMaps.FLOP.properties);
	}

	public static BufferedImage shrinkToHalf(BufferedImage inputimage)
			throws IOException, InterruptedException, IM4JavaException {
		return affineTransform(inputimage, DefaultAffinePropertiesMaps.SHRINK_HALF.properties);
	}

	public static BufferedImage shear(BufferedImage inputimage)
			throws IOException, InterruptedException, IM4JavaException {
		return affineTransform(inputimage, DefaultAffinePropertiesMaps.SHEAR.properties);
	}

	public static BufferedImage rotate(BufferedImage inputimage, double degrees)
			throws IOException, InterruptedException, IM4JavaException {
		IMOperation operation = new IMOperation();
		operation.addImage();
		operation.rotate(degrees);
		operation.addImage(":-");

		Stream2BufferedImage streamToBuffImg = new Stream2BufferedImage();
		command.setOutputConsumer(streamToBuffImg);
		command.run(operation, inputimage);

		return streamToBuffImg.getImage();
	}
}

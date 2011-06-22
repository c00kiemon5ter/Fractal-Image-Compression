package lib.core;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lib.transformations.ImageTransform;

/**
 * a fractal model represents the compressed form of the image.
 */
public class FractalModel {
    Map<BufferedImage, List<Map<ImageTransform, List<Point>>>> model;

    public FractalModel(Map<Point, Map.Entry<BufferedImage, ImageTransform>> simplemodel) {
        model = new HashMap<BufferedImage, List<Map<ImageTransform, List<Point>>>>();
        // TODO: ananlyze model
    }
}

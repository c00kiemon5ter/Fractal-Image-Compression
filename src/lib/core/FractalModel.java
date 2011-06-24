package lib.core;

import java.util.Map.Entry;
import lib.transformations.ImageTransform;

import java.awt.Point;
import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * a fractal model represents the compressed form of the image.<br />
 * <br />
 * {@code from <Range, <Domain, Transform>>      }<br />
 * {@code ..to <Domain, <Transform, {Range}>>  }<br />
 * <br />
 * or from:
 * <br />
 * {@code Point1 - Domain1 - Transform1}<br />
 * {@code Point2 - Domain2 - Transform1}<br />
 * {@code Point3 - Domain1 - Transform2}<br />
 * {@code Point4 - Domain1 - Transform2}<br />
 * {@code Point5 - Domain2 - Transform1}<br />
 * <br />
 * to:
 * <br />
 * {@code Domain1 -[ Transform1 -[ Point1 ]] }<br />
 * {@code ....... .[ Transform2 -[ Point3 ]  }<br />
 * {@code ....... ............. .[ Point4 ]] }<br />
 * {@code Domain2 -[ Transform1 -[ Point2 ]  }<br />
 * {@code ....... ............. .[ Point5 ]] }<br />
 * <br />
 * in other words, instead of storing for each range the transform <br />
 * and the domain, we store the domain once, along with a list of <br />
 * transforms, with each transform, we store a list of points that <br />
 * represent the position of the ranges in the original image.
 * 
 * @see Compressor
 */
public class FractalModel {

    Map<BufferedImage, Map<ImageTransform, Set<Point>>> model;

    public FractalModel() {
        this.model = new HashMap<BufferedImage, Map<ImageTransform, Set<Point>>>();
    }

    public FractalModel(Map<Point, Map.Entry<BufferedImage, ImageTransform>> simplemodel) {
        model = new HashMap<BufferedImage, Map<ImageTransform, Set<Point>>>();

        analyze(simplemodel);
    }

    private void analyze(Map<Point, Entry<BufferedImage, ImageTransform>> simplemodel) {

        /*
         * for each point, extract the appropriate domain and transform
         */
        for (final Point point : simplemodel.keySet()) {
            BufferedImage  domain    = simplemodel.get(point).getKey();
            ImageTransform transform = simplemodel.get(point).getValue();

            /*
             * if the domain is new to the model, add it and create
             * a map for the transform and the range points.
             */
            if (!model.containsKey(domain)) {
                model.put(domain, new HashMap<ImageTransform, Set<Point>>());
                model.get(domain).put(transform, new HashSet<Point>());
            } else if (!model.get(domain).containsKey(transform)) {
                /*
                 * if the domain is not new, but the transform is new,
                 * add the trasform and create a map for the range points
                 */
                model.get(domain).put(transform, new HashSet<Point>());
            }
            /*
             * finally add the point
             */
            model.get(domain).get(transform).add(point);
        }
    }

    public boolean add(Map.Entry<BufferedImage, Map<ImageTransform, Set<Point>>> entry) {
        model.put(entry.getKey(), entry.getValue());

        return false;
    }

    public Map<BufferedImage, Map<ImageTransform, Set<Point>>> getModel() {
        return model;
    }
}

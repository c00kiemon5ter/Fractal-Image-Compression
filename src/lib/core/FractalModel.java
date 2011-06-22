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

    public FractalModel(Map<Point, Map.Entry<BufferedImage, ImageTransform>> simplemodel) {
        model = new HashMap<BufferedImage, Map<ImageTransform, Set<Point>>>();

        analyze(simplemodel);
    }

    private void analyze(Map<Point, Entry<BufferedImage, ImageTransform>> simplemodel) {

        /*
         * for each point, extract the appropriate domain and transform
         */
        for (final Point point : simplemodel.keySet()) {
            final Map.Entry<BufferedImage, ImageTransform> entry = simplemodel.get(point);
            final BufferedImage  domain    = entry.getKey();
            final ImageTransform transform = entry.getValue();

            /*
             * if we've encountered that domain before, 
             */
            if (model.containsKey(domain)) {

                /*
                 * search the list of transforms for that domain 
                 * in the model, to find the current transform
                 */
                if (model.get(domain).containsKey(transform)) {

                    /*
                     * if the transform is found, get the list 
                     * of points and add the current point.
                     */
                    model.get(domain).get(transform).add(point);
                } else {
                    /*
                     * if the transform isn't found, add it, create a new 
                     * list of points for that transform and insert the point.
                     */
                    model.get(domain).put(transform, new HashSet<Point>() {{
                        add(point);
                    }});
                }
            } else {
                /*
                 * if it's the first time we see this domain then insert the domain,
                 * create a new list of trasforms for that domain, insert the transform,
                 * create a new list of points for that transform and insert the point.
                 */
                model.put(domain, new HashMap<ImageTransform, Set<Point>>() {{
                    put(transform, new HashSet<Point>() {{
                        add(point);
                    }});
                }});
            }
        }
    }

    public Map<BufferedImage, Map<ImageTransform, Set<Point>>> getModel() {
        return model;
    }
}

package lib;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import lib.core.FractalModel;
import lib.transformations.ImageTransform;

/**
 * restore the original image from the given fractal model
 */
public class Decompressor extends Observable {

    /**
     * @param observer the observer receiving progress results from the decompression - allowed to be null
     */
    public Decompressor(Observer observer) {
        this.addObserver(observer);
    }

    public BufferedImage decompress(FractalModel fmodel) {
        Map<Point, BufferedImage> simplemodel = new HashMap<Point, BufferedImage>();
        int maxwidth = 0, maxheight = 0, ranges = 0;

        for (BufferedImage domain : fmodel.getModel().keySet()) {
            for (ImageTransform transform : fmodel.getModel().get(domain).keySet()) {
                for (Point rangepoint : fmodel.getModel().get(domain).get(transform)) {
                    simplemodel.put(rangepoint, transform.transform(domain));
                    maxwidth  = maxwidth  < rangepoint.x ? rangepoint.x : maxwidth;
                    maxheight = maxheight < rangepoint.y ? rangepoint.y : maxheight;
                }
            }
        }

        BufferedImage image = new BufferedImage(maxwidth, maxheight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics   = image.getGraphics();

        for (Point point : simplemodel.keySet()) {
            BufferedImage range = simplemodel.get(point);
            graphics.drawImage(range, point.x * range.getWidth(), point.y * range.getHeight(), null);

            this.setChanged();
            this.notifyObservers(new int[]{++ranges, simplemodel.size()});
        }
        graphics.dispose();

        return image;
    }
}

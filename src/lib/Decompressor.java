package lib;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import lib.core.FractalModel;
import lib.core.ImageHolder;

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

    /**
     * reconstruct the original range image from the given fractal model
     * 
     * @param fmodel the fractal model describing the image
     * @return the original image
     * 
     * @see Compressor#compress(java.awt.image.BufferedImage) 
     * @see FractalModel
     */
    public BufferedImage decompress(FractalModel fmodel) {
        Map<Point, BufferedImage> simplemodel = new HashMap<Point, BufferedImage>();

        int blockwidth  = fmodel.getModel().keySet().iterator().next().getImage().getWidth();
        int blockheight = fmodel.getModel().keySet().iterator().next().getImage().getHeight();
        int maxwidth    = 0;
        int maxheight   = 0;

        for (ImageHolder domain : fmodel.getModel().keySet()) {
            for (ImageTransform transform : fmodel.getModel().get(domain).keySet()) {
                for (Point point : fmodel.getModel().get(domain).get(transform)) {
                    simplemodel.put(point, transform.transform(domain.getImage()));

                    if (maxwidth < point.x) {
                        maxwidth = point.x;
                    }

                    if (maxheight < point.y) {
                        maxheight = point.y;
                    }
                }
            }
        }

        int imgwidth  = ++maxwidth  * blockwidth;
        int imgheight = ++maxheight * blockheight;

        BufferedImage image = new BufferedImage(imgwidth, imgheight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics   = image.getGraphics();

        int ranges = 0;
        for (Point point : simplemodel.keySet()) {
            BufferedImage range = simplemodel.get(point);
            graphics.drawImage(range, point.y * blockheight, point.x * blockwidth, null);

            this.setChanged();
            this.notifyObservers(new int[]{ranges++, simplemodel.size()});
        }
        graphics.dispose();

        return image;
    }
}

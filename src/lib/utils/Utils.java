package lib.utils;

import java.awt.Point;

/**
 * useful generic utility tools
 */
public class Utils {

    public static Point indexToPoint(int index, int width) {
        return new Point(index / width, index % width);
    }

    public static int pointToIndex(Point point, int width) {
        return point.x * width + point.y;
    }
}

package lib.utils;

import java.awt.Point;

/**
 * useful generic utility tools
 */
public class Utils {

    public static Point indexToPoint(final int index, final int width) {
        assert (index >= 0) && (width >= 0);
        
        return new Point(index / width, index % width);
    }

    public static int pointToIndex(final Point point, final int width) {
        assert (point != null) && (point.x >= 0) && (point.y >= 0) && (width >= 0);
        
        return point.x * width + point.y;
    }
}

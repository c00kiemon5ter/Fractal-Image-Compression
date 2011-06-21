package lib.utils;

import java.awt.Point;

/**
 * useful generic utility tools
 */
public class Utils {

    public static Point indexToPoint(int index, int width) {
        assert (index >= 0) && (width >= 0);
        
        return new Point(index / width, index % width);
    }

    public static int pointToIndex(Point point, int width) {
        assert (point != null) && (point.x >= 0) && (point.y >= 0) && (width >= 0);
        
        return point.x * width + point.y;
    }
}

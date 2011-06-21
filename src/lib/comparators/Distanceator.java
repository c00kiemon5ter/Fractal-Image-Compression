package lib.comparators;

/**
 * This interface should be implemented by classes that implement
 * a comparison between two object and return their distance-difference.
 * Several distance metrics may be supported.
 * @param <T>
 */
public interface Distanceator<T> {

    /**
     * Distance between two given objects
     *
     * @param obj1 the first object to compare to
     * @param obj2 the second object to compare with
     * @return the distance as defined by a metric between the objects
     *
     * @see Metric
     */
    double distance(final T obj1, final T obj2);
}

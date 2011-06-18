package lib.comparators;

/**
 * This interface should be implemented by classes for which
 * it makes sense to measure the distance between instances.
 * Several distance metrics may be supported.
 * @param <T> 
 */
public interface Distanceable<T> {

	/**
	 * Distance between two given objects
	 * 
	 * @param obj1 the first object to compare to
	 * @param obj2 the second object to compare with
	 * @return the distance as defined by a metric between the objects
	 * 
	 * @see Metric
	 */
	double distance(T obj1, T obj2);
}

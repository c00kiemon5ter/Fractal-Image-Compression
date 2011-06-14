package lib.tilers;

import java.util.ArrayList;

public interface Tiler<T> {

	/**
	 * Tile the given object according
	 * 
	 * @param t the object to tile
	 * @return an array list holding the tiles of the object
	 */
	public ArrayList<T> tile(T t);
}

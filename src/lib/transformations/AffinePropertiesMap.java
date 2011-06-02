package lib.transformations;

import java.util.EnumMap;
import java.util.Map;

/**
 * mapping of affine properties to values
 * 
 * @see AffineProperty
 */
public class AffinePropertiesMap {

	private Map<AffineProperty, Double> properties;

	public AffinePropertiesMap() {
		this.properties = new EnumMap<AffineProperty, Double>(AffineProperty.class);
	}

	public AffinePropertiesMap setProperty(AffineProperty property, double value) {
		this.properties.put(property, value);
		return this;
	}

	public double getPropery(AffineProperty affineProperty) {
		return this.properties.get(affineProperty);
	}
}

package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents expressions of type double. 
 * @author ltomic
 *
 */
public class ElementConstantDouble extends Element {

	/**
	 * Value of the number this object represents.
	 */
	private double value;
	/**
	 * Difference between two number up to which they are considered equal.
	 */
	private static final double precision = 1E-6; 
	
	/**
	 * Constructor a new <code>ElementConstantDouble</code> that represent number
	 * <code>value</code>
	 * @param value number this object represents
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Getter for property value
	 * @return value
	 */
	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	/**
	 * Compares this object with <code>other</code> Object.
	 * @return <code>true</code> if <code>other</code> is type of ElementConstantDouble
	 * 							 and its value differs up to <code>precision</code>
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ElementConstantDouble == false) return false;
		ElementConstantDouble other1 = (ElementConstantDouble)other;
		
		return Math.abs(value - other1.getValue()) <= precision;
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitElementConstantDouble(this);
	}
}

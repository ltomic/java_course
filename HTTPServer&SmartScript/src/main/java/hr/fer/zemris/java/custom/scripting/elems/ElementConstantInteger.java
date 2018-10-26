package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents expressions of type int. 
 * @author ltomic
 *
 */
public class ElementConstantInteger extends Element {

	/**
	 * Value of the number this object represents.
	 */
	private int value;
	
	/**
	 * Constructs a new <code>ElementConstantInteger</code> that represents number
	 * <code>value</code>
	 * @param value number this object represents
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Getter for property value
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitElementConstantInteger(this);
	}
	
	/**
	 * Compares this object with <code>other</code> Object.
	 * @return <code>true</code> if <code>other</code> is type of ElementConstantInteger
	 * 							 and its value is equal to the value this object represents.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ElementConstantInteger == false) return false;
		ElementConstantInteger other1 = (ElementConstantInteger)other;
		
		return value == other1.getValue();
	}
	
}

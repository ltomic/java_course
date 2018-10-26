package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * This class represents expression of type string.
 * @author ltomic
 *
 */
public class ElementString extends Element {

	/**
	 * String this class represents.
	 */
	private String value;
	
	/**
	 * Constructor a new <code>ElementString</code> that represents string
	 * <code>value</code>
	 * @param value string this object represents
	 */
	public ElementString(String value) {
		this.value = Objects.requireNonNull(value, "value cannot be null.");
	}

	/**
	 * Getter for property value
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}	
	
	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitElementString(this);
	}

	/**
	 * Compares this object with <code>other</code> Object.
	 * @return <code>true</code> if <code>other</code> is type of ElementString and
	 * 							 its value is equal to the value this object represents.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ElementString)) return false;
		ElementString other1 = (ElementString)other;

		return value.equals(other1.getValue());
	}
}

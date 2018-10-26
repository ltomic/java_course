package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * This class represents expression of a variable. 
 * @author ltomic
 *
 */
public class ElementVariable extends Element {
	
	/**
	 * The name of the variable.
	 */
	private String name;
	
	/**
	 * Constructs a new <code>ElementVariable</code> that represents variable
	 * <code>name</code>
	 * @param value name of the variable
	 */
	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name, "name cannot be null.");
	}

	/**
	 * Getter for property name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitElementVariable(this);
	}

	/**
	 * Compares this object with <code>other</code> Object.
	 * @return <code>true</code> if <code>other</code> is type of ElementVariable and
	 * 							 its name is equal to the name this object represents.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ElementVariable)) return false;
		ElementVariable other1 = (ElementVariable)other;
		
		return name.equals(other1.getName());
	}

}

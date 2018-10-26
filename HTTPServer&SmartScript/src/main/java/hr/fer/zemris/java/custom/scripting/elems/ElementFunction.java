package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * This class represents expressions of function call.
 * @author ltomic
 *
 */
public class ElementFunction extends Element {
	
	/**
	 * Name of the function.
	 */
	private String name;
	
	/**
	 * Constructs a new <code>ElementFunction</code> that represents call of 
	 * function <code>name</code>
	 * @param name name of the function that should be called in the function
	 * 			   call expression this object represents
	 */
	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * Getter for the property name
	 * @return
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "@"+name;
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitElementFunction(this);
	}
	
	/**
	 * Compares this object with <code>other</code> Object.
	 * @return <code>true</code> if <code>other</code> is type of ElementFunction
	 * 							 and its name is equal to the name this object represents.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ElementFunction == false) return false;
		ElementFunction other1 = (ElementFunction)other;
		
		return name.equals(other1.getName());
	}

}

package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * EchoNode represent a command called tag which generates some textual output
 * dynamically. EchoNode must have a name, which is stored as a first element. 
 * Name can be a symbol '=', or a variable name. 
 * @author ltomic
 *
 */

public class EchoNode extends Node {
	
	/**
	 * Used to store expressions that are in this tag.
	 */
	private Element[] elements;
	
	/**
	 * Constructs an EchoNode with expressions in <code>elements</code>
	 * @param elements expressions in tag
	 */
	public EchoNode(Element[] elements) {
		this.elements = Objects.requireNonNull(elements);
	}

	/**
	 * Getter for property elements.
	 * @return elements
	 */
	public Element[] getElements() {
		return elements;
	}
	
	/**
	 * Returns a text representation of EchoNode with text representations
	 * of expressions in tag included.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append('[');
		for (Element i: elements) {
			str.append(i.toString() + ", ");
		}
		str.append(']');
		
		return str.toString();
	}
	

	/**
	 * @return <code>true</code> if their expressions are equal
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EchoNode)) return false;
		Element[] otherElements = ((EchoNode)other).elements;
		
		int size = elements.length;
		if (size != otherElements.length) return false;
		
		for (int i = 0; i < size; ++i) {
			if (this.elements[i].equals(otherElements[i]) == false) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
	
	
}

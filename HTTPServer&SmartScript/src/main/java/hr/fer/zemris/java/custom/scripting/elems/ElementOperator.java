package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * This class represents expression of type operator.
 * @author ltomic
 *
 */
public class ElementOperator extends Element {

	/**
	 * Operator this object represent
	 */
	private String symbol;

	/**
	 * Constructor a new <code>ElementOperator</code> that represents operator
	 * <code>symbol</code>
	 * @param value operator this object represents
	 * @throws NullPointerException if operator is <code>null</code>
	 */
	public ElementOperator(String symbol) {
		this.symbol = Objects.requireNonNull(symbol);
	}

	/**
	 * Getter for property <code>symbol</code>
	 * @return symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitElementOperator(this);		
	}

	/**
	 * Compares this object with <code>other</code> Object.
	 * @return <code>true</code> if <code>other</code> is type of ElementSymbol and
	 * 							 its value is equal to the value this object represents.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ElementOperator == false) return false;
		ElementOperator other1 = (ElementOperator)other;
		
		return symbol.equals(other1.getSymbol());
	}
	
}

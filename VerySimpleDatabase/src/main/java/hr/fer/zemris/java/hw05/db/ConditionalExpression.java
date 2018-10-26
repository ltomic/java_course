package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Implements a conditional expression which is parsed(and consists of) :
 * 	attribute	operator	literal
 * @author ltomic
 *
 */

public class ConditionalExpression {

	/**
	 * Getter for the attribute of this conditional expression
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * Literal of this conditional expression
	 */
	private String stringLiteral;
	/**
	 * Operator of this conditional expression
	 */
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructs a conditional expression with provided arguments.
	 * @param fieldGetter
	 * @param stringLiteral
	 * @param comparisonOperator
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		super();
		this.fieldGetter = Objects.requireNonNull(fieldGetter, "fieldGetter cannot be null");
		this.stringLiteral = Objects.requireNonNull(stringLiteral, "stringLiteral cannot be null");
		this.comparisonOperator = Objects.requireNonNull(comparisonOperator, "comparisonOperator cannot be null");
	}

	/**
	 * Returns getter
	 * @return getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Returns literal
	 * @return literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Returns operator
	 * @return operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	@SuppressWarnings("javadoc")
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparisonOperator == null) ? 0 : comparisonOperator.hashCode());
		result = prime * result + ((fieldGetter == null) ? 0 : fieldGetter.hashCode());
		result = prime * result + ((stringLiteral == null) ? 0 : stringLiteral.hashCode());
		return result;
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConditionalExpression other = (ConditionalExpression) obj;
		if (comparisonOperator == null) {
			if (other.comparisonOperator != null)
				return false;
		} else if (!comparisonOperator.equals(other.comparisonOperator))
			return false;
		if (fieldGetter == null) {
			if (other.fieldGetter != null)
				return false;
		} else if (!fieldGetter.equals(other.fieldGetter))
			return false;
		if (stringLiteral == null) {
			if (other.stringLiteral != null)
				return false;
		} else if (!stringLiteral.equals(other.stringLiteral))
			return false;
		return true;
	}

	
}

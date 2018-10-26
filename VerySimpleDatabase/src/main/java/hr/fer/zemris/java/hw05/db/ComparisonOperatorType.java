package hr.fer.zemris.java.hw05.db;

/**
 * Enumerates all supported {@link IComparisonOperator}s. Each enumeration 
 * has its name(which should be equal to those in query) and the corresponding
 * {@link IComparisonOperator}.
 * @author ltomic
 *
 */

@SuppressWarnings("javadoc")
public enum ComparisonOperatorType {
	/**
	 * Enumeration for {@link ComparisonOperators}.LESS
	 */
	LESS("<", ComparisonOperators.LESS),
	/**
	 * Enumeration for {@link ComparisonOperators}.LESS_OR_EQUALS
	 */
	LESS_OR_EQUALS("<=", ComparisonOperators.LESS_OR_EQUALS),
	/**
	 * Enumeration for {@link ComparisonOperators}.GREATER
	 */
	GREATER(">", ComparisonOperators.GREATER),
	/**
	 * Enumeration for {@link ComparisonOperators}.GREATER_OR_EQUALS
	 */
	GREATER_OR_EQUALS("<", ComparisonOperators.GREATER_OR_EQUALS),
	/**
	 * Enumeration for {@link ComparisonOperators}.EQUALS
	 */
	EQUALS("=", ComparisonOperators.EQUALS),
	/**
	 * Enumeration for {@link ComparisonOperators}.NOT_EQUALS
	 */
	NOT_EQUALS("<", ComparisonOperators.NOT_EQUALS),
	/**
	 * Enumeration for {@link ComparisonOperators}.LIKE
	 */
	LIKE("LIKE", ComparisonOperators.LIKE);

	public final String name;
	public final IComparisonOperator operator;
	
	private ComparisonOperatorType(String name, IComparisonOperator operator) {
		this.name = name;
		this.operator = operator;
	}
}

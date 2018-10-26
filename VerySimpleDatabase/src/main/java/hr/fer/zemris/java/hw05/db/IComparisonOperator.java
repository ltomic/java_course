package hr.fer.zemris.java.hw05.db;

/**
 * This interface provides a single method that compares two strings.
 * @author ltomic
 *
 */

@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Compares two strings.
	 * @param value1 first string
	 * @param value2 second string
	 * @return true or false, depending on the comparing operator
	 */
	public boolean satisfied(String value1, String value2);
}

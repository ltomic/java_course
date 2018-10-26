package hr.fer.zemris.java.hw05.db;

/**
 * Implements the supported {@link IComparisonOperator}s.
 * @author ltomic
 *
 */

public class ComparisonOperators {

	/**
	 * Compares is first string argument is less than second string argument, lexicographically.
	 */
	public static final IComparisonOperator LESS = (String value1, String value2)-> {
		return value1.compareTo(value2) < 0;
	};
	
	/**
	 * Compares is first string argument is less or equal than second string argument, lexicographically.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (String value1, String value2)-> {
		return value1.compareTo(value2) <= 0;
	};
	
	/**
	 * Compares is first string argument is greater than second string argument, lexicographically.
	 */
	public static final IComparisonOperator GREATER = (String value1, String value2)-> {
		return value1.compareTo(value2) > 0;
	};
	
	/**
	 * Compares is first string argument is greater or equal than second string argument, lexicographically.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (String value1, String value2)-> {
		return value1.compareTo(value2) >= 0;
	};
	
	/**
	 * Compares is first string argument is equal to second string argument, lexicographically.
	 */
	public static final IComparisonOperator EQUALS = (String value1, String value2)-> {
		return value1.compareTo(value2) == 0;
	};
	
	/**
	 * Compares is first string argument is not equal to second string argument, lexicographically.
	 */
	public static final IComparisonOperator NOT_EQUALS = (String value1, String value2)-> {
		return value1.compareTo(value2) != 0;
	};
	
	/**
	 * Tests if string toCheck fits the pattern(second argument). The pattern consist of
	 * letters which string must match and optionally at most one wildcard which matched
	 * any number of letters(possibly zero).
	 */
	public static final IComparisonOperator LIKE = (String toCheck, String pattern)-> {
		if (pattern == "") return toCheck.equals(pattern);
		
		String[] splittedPattern = pattern.split("\\*");
		int size = splittedPattern.length;
		
		if (size > 2) {
			throw new IllegalArgumentException(
					"Invalid pattern given, too many wildcars : " + pattern);
		}
		if (size == 0) return true;
		
		if (pattern.charAt(pattern.length()-1) != '*') {
			if (!toCheck.endsWith(splittedPattern[size-1])) return false;
			toCheck = toCheck.substring(0, toCheck.length()-splittedPattern[size-1].length());
		}
		if (pattern.charAt(0) != '*') {
			if (!toCheck.startsWith(splittedPattern[0])) return false;
		}

		return true;		
	};
}

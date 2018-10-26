package hr.fer.zemris.java.hw05.db;

/**
 * This interface provides a single method that determines if a student satisfies 
 * the filter conditions.
 * @author ltomic
 *
 */

@FunctionalInterface
public interface IFilter {

	/**
	 * Test if a given student satisfies the filter conditions.
	 * @param record student to be checked
	 * @return true if a given student satisfies the filter conditions.
	 */
	public boolean accepts(StudentRecord record);
}

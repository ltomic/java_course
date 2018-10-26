package hr.fer.zemris.java.hw05.db;

/**
 * This interface provides a single method that retrieves a field value from
 * the given {@link StudentRecord}.
 * @author ltomic
 *
 */

@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Returns a field value from the given {@link StudentRecord}
	 * @param record student from which field value should be retrieved from
	 * @return a field value from the given {@link StudentRecord}
	 */
	public String get(StudentRecord record);
}

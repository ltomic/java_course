package hr.fer.zemris.java.hw05.db;

/**
 * Implements supported {@link IFieldValueGetter}s.
 * @author ltomic
 *
 */

public class FieldValueGetters {

	/**
	 * {@link IFieldValueGetter} for first name
	 */
	public static final IFieldValueGetter FIRST_NAME = (StudentRecord record)->{
		return record.getFirstName();
	};
	
	/**
	 * {@link IFieldValueGetter} for last name
	 */
	public static final IFieldValueGetter LAST_NAME = (StudentRecord record)->{
		return record.getLastName();
	};
	
	/**
	 * {@link IFieldValueGetter} for JMBAG
	 */
	public static final IFieldValueGetter JMBAG = (StudentRecord record)->{
		return record.getJmbag();
	};
	
	/**
	 * {@link IFieldValueGetter} for final grade
	 */
	public static final IFieldValueGetter FINAL_GRADE = (StudentRecord record)->{
		return record.getFinalGrade();
	};
}

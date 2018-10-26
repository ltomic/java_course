package hr.fer.zemris.java.hw05.db;

/**
 * Enumeration for attribute types. Each enumeration has its name(which should be
 * equal to those in query) and the corresponding {@link IFieldValueGetter}.
 * @author ltomic
 *
 */

public enum AttributeType {
	/**
	 * First name attribute
	 */
	FIRST_NAME("firstName", FieldValueGetters.FIRST_NAME),
	/**
	 * Last name attribute
	 */
	LAST_NAME("lastName", FieldValueGetters.LAST_NAME),
	/**
	 * JMBAG attribute
	 */
	JMBAG("jmbag", FieldValueGetters.JMBAG);

	/**
	 * Name of the attribute
	 */
	public final String name;
	/**
	 * {@link IFieldValueGetter} of the attribute
	 */
	public final IFieldValueGetter getter;
	
	/**
	 * Constructs a {@link AttributeType} with name and getter.
	 * @param name the name of attribute
	 * @param getter getter of attribute
	 */
	private AttributeType(String name, IFieldValueGetter getter) {
		this.name = name;
		this.getter = getter;
	}
	
}

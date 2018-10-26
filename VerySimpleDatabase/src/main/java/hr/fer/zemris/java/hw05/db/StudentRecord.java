package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * This class repersents one student which is described as 4 variables : jmbag, lastName,
 * firstName, finalGrade.
 * JMBAG is 10-digit string
 * lastName is a string consisting of spaces, letters and '-'
 * firtsName is a string consisting of letters
 * finalGrade is a single-digit string
 * 
 * In database file students each student is in his own line and is described in format:
 * jmbag lastName firstName finalGrade
 * @author ltomic
 *
 */

public class StudentRecord {

	/**
	 * Students JMBAG
	 */
	private String jmbag;
	/**
	 * Students last name
	 */
	private String lastName;
	/**
	 * Students first name
	 */
	private String firstName;
	/**
	 * Students final grade
	 */
	private String finalGrade;
	
	/**
	 * Constructs a {@link StudentRecord} with given arguments
	 * @param jbmag
	 * @param lastName
	 * @param firstName
	 * @param finalGrade
	 */
	public StudentRecord(String jbmag, String lastName, String firstName, String finalGrade) {
		super();
		this.jmbag = Objects.requireNonNull(jbmag, "jmbag argument cannot be null");
		this.lastName = Objects.requireNonNull(lastName, "lastName argument cannot be null");
		this.firstName = Objects.requireNonNull(firstName, "firstName argument cannot be null");
		this.finalGrade = Objects.requireNonNull(finalGrade, "finalGrade argument cannot be null");
	}
	
	/**
	 * Return students jmbag
	 * @return students jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Returns students last name
	 * @return students last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns students first name
	 * @return students first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns students final grade
	 * @return students final grade
	 */
	public String getFinalGrade() {
		return finalGrade;
	}
	

	@SuppressWarnings("javadoc")
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
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
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String toString() {
		return jmbag + " " + lastName + " " + firstName + " " + finalGrade;
	}
}

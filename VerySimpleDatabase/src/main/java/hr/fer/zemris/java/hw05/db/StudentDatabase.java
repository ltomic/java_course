package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw05.collections.SimpleHashTable;

/**
 * Implements a database that stores {@link StudentRecord}s.
 * It supports getting a single student by jmbag and filtering using
 * conditional expressions. Key index of database is JMBAG. Database
 * assumes that for every student a jmbag is unique.
 * @author ltomic
 *
 */

public class StudentDatabase {
	
	/**
	 * Hashtable in which students are stored under jmbag as key.
	 * Used for fast retrieval of student by jmbag.
	 */
	private SimpleHashTable<String, StudentRecord> table;
	/**
	 * Stores all students in order they were loaded from database file.
	 */
	private List<StudentRecord> records;
	

	/**
	 * Constructs a {@link StudentDatabase} and parses through the given text
	 * which consist of students to be stored in database.
	 * @param text list of students to be stored in database
	 */
	public StudentDatabase(List<String> text) {
		super();
		Objects.requireNonNull(text, "text cannot be null");
		
		table = new SimpleHashTable<String, StudentRecord>();
		records = new ArrayList<StudentRecord>();
		
		parseText(text);
	}
	
	/**
	 * Fast retrieval of student by jmbag.
	 * @param jmbag jmbag to of student to be retrieved
	 * @return student with given jbmag
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return table.get(jmbag);
	}
		
	/**
	 * Returns a list of students filtered with given filter.
	 * @param filter the filter to filter with
	 * @return  list of student filtered with given filter
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> filteredStudents = new ArrayList<StudentRecord>();
		for (StudentRecord student : records) {
			if (filter.accepts(student)) filteredStudents.add(student);
		}
		
		return filteredStudents;
	}
	
	/**
	 * Parses a list of students from database file and stores them in records and table.
	 * @param text database file
	 */
	public void parseText(List<String> text) {
		int index = 0;
		for (String line : text) {
			StudentRecord record;
			try {
				record = parseLine(line);
			} catch(IllegalArgumentException ex) {
				throw new IllegalArgumentException(ex.getMessage() + " at line number :"
					+ index + "\n\t " + line);
			}

			records.add(record);
			table.put(record.getJmbag(), record);
			index++;
		}
	}
	
	/**
	 * Generates {@link StudentRecord} from a single line from database file.
	 * @param line single line from database file
	 * @return {@link StudentRecord} from a single line from database file
	 */
	public StudentRecord parseLine(String line) {
		String[] splitted = line.trim().split("\\s+");
		int sz = splitted.length;
		
		if (sz < 4) {
			throw new IllegalArgumentException("Invalid database line");
		}
		
		String jmbag = splitted[0];
		if (!checkJMBAG(jmbag)) {
			throw new IllegalArgumentException("Invalid database line, invalid JMBAG");
		}
		
		String lastName = buildLastName(splitted);
		
		String firstName = splitted[sz-2];
		if (!checkName(firstName)) {
			throw new IllegalArgumentException("Invalid database line, invalid first name");
		}
		
		if (splitted[sz-1].length() != 1 && Character.isDigit(splitted[sz-1].charAt(0))) {
			throw new IllegalArgumentException("Invalid database line, invalid grade");
		}
		String finalGrade = splitted[sz-1];
		
		return new StudentRecord(jmbag, lastName.toString(), firstName, finalGrade);
	}
	
	/**
	 * Generates students last name from a given array of strings
	 * @param splitted array of strings 
	 * @return students last name
	 */
	public String buildLastName(String[] splitted) {
		int sz = splitted.length;
		
		StringBuilder lastName = new StringBuilder();
		for (int i = 1; i < sz-2; ++i) {
			if (!checkName(splitted[i])) {
				throw new IllegalArgumentException("Invalid database line, invalid last name");
			}
			lastName.append(splitted[i]).append(" ");
		}
		lastName.deleteCharAt(lastName.length()-1);
		
		return lastName.toString();
	}
	
	/**
	 * Tests if string can be a students first name
	 * @param name string to test
	 * @return true if string can be a students first name
	 */
	public boolean checkName(String name) {
		char[] charName = name.toCharArray();
		
		for (char c : charName) {
			if (!(Character.isLetter(c) || c == '-')) return false;
		}
		
		return true;
	}
	
	/**
	 * Tests if a given string is jmbag format
	 * @param jmbag string to check
	 * @return true if given string is jmbag format
	 */
	public boolean checkJMBAG(String jmbag) {
		char[] charJMBAG = jmbag.toCharArray();
		if (charJMBAG.length != 10) return false;
		
		for (char c : charJMBAG) {
			if (!Character.isDigit(c)) return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("javadoc")
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (StudentRecord student : records) {
			string.append(student.toString() + "\n");
		}
		
		return string.toString();
	}
	

	@SuppressWarnings("javadoc")
	public StudentRecord[] toArray() {
		return records.toArray(new StudentRecord[0]);
	}
	
}

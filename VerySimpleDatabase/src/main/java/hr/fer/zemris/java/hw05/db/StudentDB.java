package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Implements a simple database emulator. Initializes a {@link StudentDatabase}
 * from a database file located at DATABASE_FILE_PATH. To send a query to database
 * enter query and then a query line as described in {@link QueryParser}.
 * After receiving a query program prints out a formated table of all students
 * satisfying the query and number of students that satisfied the query.
 * To exit the program enter "exit"
 * @author ltomic
 *
 */

public class StudentDB {
	
	/**
	 * Gets a project path
	 */
	private static String PROJECT_PATH = System.getProperty("user.dir");
	/**
	 * Defines the database file
	 */
	private static String DATABASE_FILE_PATH = "/src/main/resources/database.txt";
	/**
	 * Defines the start of query line keyword
	 */
	private static String START_QUERY_LINE = "query ";
	/**
	 * Defines the exit keyword
	 */
	private static String EXIT_LINE = "exit";
	
	/**
	 * Loads the given file located somewhere in project folders and returns it
	 * as a List of strings.
	 * @param filepath file to load
	 * @return file to load as list of strings
	 * @throws IOException
	 */
	public static List<String> loader(String filepath) throws IOException {
		return Files.readAllLines(
				 Paths.get(PROJECT_PATH + filepath),
				 StandardCharsets.UTF_8
				);
	}

	/**
	 * Main method called at the beginning of the program.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		StudentDatabase database = new StudentDatabase(loader(DATABASE_FILE_PATH));
		
		try (Scanner input = new Scanner(System.in)) {
			while (true) {
				System.out.print("> ");
				
				String queryString = input.nextLine();
				
				if (queryString.equals(EXIT_LINE)) break;
				if (!queryString.startsWith(START_QUERY_LINE)) {
					System.out.println("Invalid query, query must start with keyword \"query \"");
				}

				QueryParser parser = new QueryParser(queryString.substring(6, queryString.length()));
				List<StudentRecord> result = new ArrayList<StudentRecord>();
				
				if (parser.isDirectQuery()) {
					System.out.println("Using index for record retrieval.");
					result.add(database.forJMBAG(parser.getQueriedJMBAG()));
				} else {
					for (StudentRecord record : database.filter(new QueryFilter(parser.getQuery()))) {
						result.add(record);
					}
				}

				if (result.size() > 0) {
					String[] formatedTable = generateFormattedResults(result);
					for (String line: formatedTable) {
						System.out.println(line);
					}
				}
				System.out.println("Results selected: " + result.size());
			}
		}
		
		System.out.println("Goodbye!");
	}
	
	/**
	 * Number of attributes of {@link StudentRecord}
	 */
	private static int NUMBER_OF_ATTRIBUTES = 4;
	
	/**
	 * Defines column index of students JMBAG in formatted table
	 */
	private static int JMBAG_COLUMN_INDEX = 0;
	/**
	 * Defines column index of students last name in formatted table
	 */
	private static int LAST_NAME_COLUMN_INDEX = 1;
	/**
	 * Defines column index of students first name in formatted table
	 */
	private static int FIRST_NAME_COLUMN_INDEX = 2;
	/**
	 * Defines column index of students final grade in formatted table
	 */
	private static int FINAL_GRADE_COLUMN_INDEX = 3;
	
	/**
	 * Defines the JMBAG length
	 */
	private static int JMBAG_LENGTH = 10;
	/**
	 * Defines the length of final grade column in formatted table
	 */
	private static int FINAL_GRADE_COLUMN_LENGTH = 1;
	
	/**
	 * Defines the padding of columns in formatted table
	 */
	private static int COLUMN_LEFT_RIGHT_PADDING = 1;
	
	/**
	 * Generates a formatted table from given list of students.
	 * @param result list of students
	 * @return formatted table from given list of students
	 */
	public static String[] generateFormattedResults(List<StudentRecord> result) {
		String[] table = new String[result.size()+2];
		
		int[] columnLengths = new int[NUMBER_OF_ATTRIBUTES];
		columnLengths[FINAL_GRADE_COLUMN_INDEX] = FINAL_GRADE_COLUMN_LENGTH;
		columnLengths[JMBAG_COLUMN_INDEX] = JMBAG_LENGTH;
		
		columnLengths[LAST_NAME_COLUMN_INDEX] = calcMaximumLength(result, FieldValueGetters.LAST_NAME);
		columnLengths[FIRST_NAME_COLUMN_INDEX] = calcMaximumLength(result, FieldValueGetters.FIRST_NAME);
		
		for (int i = 0; i < NUMBER_OF_ATTRIBUTES; ++i) {
			columnLengths[i] += 2*COLUMN_LEFT_RIGHT_PADDING;
		}
		
		table[0] = generateStartEndTableLine(columnLengths);
		for (int i = 0, sz = result.size(); i < sz; ++i) {
			table[i+1] = generateTableRecordLine(columnLengths, result.get(i));
		}
		table[result.size()+1] = generateStartEndTableLine(columnLengths);
		
		return table;
	}
	
	/**
	 * Calculates the maximum length of objects provided by given {@link IFieldValueGetter}
	 * from given list of students
	 * @param result list of students
	 * @param getter 
	 * @return the maximum length of objects provided by given {@link IFieldValueGetter}
	 * 			from given list of students
	 */
	public static int calcMaximumLength(List<StudentRecord> result, IFieldValueGetter getter) {
		int maxLength = 0;
		for (StudentRecord record : result) {
			maxLength = Math.max(maxLength, getter.get(record).length());
		}
		
		return maxLength;
	}
	
	/**
	 * Defines the character representing the corner of formatted table
	 */
	private static char COLUMN_CORNER = '+';
	/**
	 * Defines the character representing the horizontal side of formatted table
	 */
	private static char COLUMN_HORIZONTAL_SIDE = '=';
	/**
	 * Defines the character representing the vertical side of formatted table
	 */
	private static char COLUMN_VERTICAL_SIDE = '|';
	
	/**
	 * Generates the starting and ending line of formatted table
	 * @param columnLengths length of columns in formatted table
	 * @return the starting and ending line of formatted table
	 */
	public static String generateStartEndTableLine(int[] columnLengths) {
		StringBuilder line = new StringBuilder();
		
		for (int i = 0; i < NUMBER_OF_ATTRIBUTES; ++i) {
			line.append(COLUMN_CORNER);
			for (int j = 0, sz = columnLengths[i]; j < sz; ++j) {
				line.append(COLUMN_HORIZONTAL_SIDE);
			}
		}
		line.append(COLUMN_CORNER);
		
		return line.toString();
	}
	
	/**
	 * Generates a single line of formatted table which represents a student
	 * from a given {@link StudentRecord}
	 * @param columnLengths length of columns in formatted table
	 * @param record student to put in this line
	 * @return a single line of formatted table which represents a student
	 * 			from a given {@link StudentRecord}
	 */
	public static String generateTableRecordLine(int columnLengths[], StudentRecord record) {
		StringBuilder line = new StringBuilder();
		IFieldValueGetter getters[] = new IFieldValueGetter[] {
				FieldValueGetters.JMBAG,
				FieldValueGetters.LAST_NAME,
				FieldValueGetters.FIRST_NAME,
				FieldValueGetters.FINAL_GRADE
		};
		
		line.append(COLUMN_VERTICAL_SIDE);
		for (int i = 0; i < NUMBER_OF_ATTRIBUTES; ++i) {
			line.append(' ');
			String attribute = getters[i].get(record);
			line.append(attribute);
			
			for (int j = 0, len = columnLengths[i]-attribute.length()-COLUMN_LEFT_RIGHT_PADDING; j < len; ++j) {
				line.append(' ');
			}
			line.append(COLUMN_VERTICAL_SIDE);
		}
		
		return line.toString();
	}
}

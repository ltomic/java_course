package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program enables user to compare similarity of document or search for a document using keywords.
 * Program should be started with single argument : folder in which are the documents that this program
 * analyzes. Supported commands are: <br>
 * query <code>text</code> - search for the fill similar to <code>text</code> and displays the first 10
 * 								results whose similarity is greater or equal to 0.001 <br>
 * type <code>number</code> - print the document index <code>number</code> in the results <br>
 * results - print the last generated results<br>
 * exit - exits the programm
 * @author ltomic
 *
 */
public class Konzola {

	/** Invalid number of arguments message */
	public static final String INVALID_NUMBER_OF_ARGUMENTS_MESSAGE = "Expected a single argument - path to directory. Got %s";
	/** Given path not a directoy message */
	public static final String GIVEN_PATH_NOT_DIRECTORY_MESSAGE = "Given path is not a path to directory. Path given : %s";
	/** Fire read error message */
	public static final String FILE_READ_ERROR = "IO error while reading from file %s.";
	
	/** Invalid command name message */
	public static final String INVALID_COMMAND_NAME_MESSAGE = "Invalid command name. Expected query, type, results or exit. Given %s.";
	/** No results message */
	public static final String NO_RESULTS_MESSAGE = "No results available";
	/** Resulta are empty message */
	public static final String RESULTS_EMPTY_MESSAGE = "Results are empty";
	
	/** Invalid integer message */
	public static final String INVALID_INTEGER = "Expected integer. Given %s.";
	/** Integer out of bound message */
	public static final String INTEGER_OUT_OF_BOUNDS = "Given integer out of bounds. Expected in range [0, %d>. Given %d.";
	/** Location of stop words file */
	public static final String STOPWORDS_FILE = "src/main/resources/hrvatski_stoprijeci.txt";
	
	/** Results minimal similarity bound */
	public static final double EPSILON = 0.0005;
	/** Results print list size */
	public static final int RESULT_LIST_PRINT_SIZE = 10;
	/** Results line format */
	public static final String RESULT_FORMAT = "[%2d] (%.4f) %s";
	
	/** Query command name */
	public static final String QUERY_COMMAND = "query";
	/** Type command name */
	public static final String TYPE_COMMAND = "type";
	/** Results command name */
	public static final String RESULTS_COMMAND = "results";
	/** Exit command name */
	public static final String EXIT_COMMAND = "exit";
	/** Vocabulary size message */
	public static final String VOCABULARY_SIZE_MESSAGE = "Velicina rjecnika je %d rijeci.";
	
	/**
	 * Method called at the beginning of the program.
	 * @param args - single argument is expected : Directory containing documents that should be analyzed
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println(String.format(INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, args.length));
			return;
		}

		File dir = new File(args[0]);
		if (!dir.isDirectory()) {
			System.out.println(String.format(GIVEN_PATH_NOT_DIRECTORY_MESSAGE, dir.toString()));
			return;
		}
		
		BagOfWords bag = new BagOfWords(dir, new File(STOPWORDS_FILE));
		System.out.println(String.format(VOCABULARY_SIZE_MESSAGE, bag.vocabularySize()));
		
		List<SimpleEntry<Double, Path>> results = null;
		try (Scanner sc = new Scanner(System.in)) {
loop:		while (true) {
				System.out.print("Enter command > ");
				String command = sc.nextLine();
				String splitted[] = command.split("\\s+", 2);
				String commandName = splitted[0].trim();
				String commandArgs = splitted.length < 2 ? null : splitted[1].trim();
				switch (commandName) {
				case EXIT_COMMAND:
					break loop;
				case QUERY_COMMAND:
					results = queryCommand(bag, commandArgs);
					System.out.println("Najboljih 10 rezultata");
					printResults(results);
					break;
				case RESULTS_COMMAND:
					printResults(results);
					break;
				case TYPE_COMMAND:
					printFile(results, commandArgs);
					break;
				default:
					System.out.println(String.format(INVALID_COMMAND_NAME_MESSAGE, commandName));
					break;
				}				
			}
		}
		
	}
	
	/**
	 * Method returning results list for provided {@link BagOfWords} and document
	 * @param bag - bag of words to query
	 * @param doc - query doc
	 * @return results list
	 */
	private static List<SimpleEntry<Double, Path>> queryCommand(BagOfWords bag, String doc) {
		List<SimpleEntry<Double, Path>> result = new ArrayList<>();
		bag.querySimilar(doc).forEach((path, score) -> {
			if (score < EPSILON) return;
			result.add(new SimpleEntry<Double, Path>(score, path));
		});
		
		result.sort((a, b) -> Double.compare(b.getKey(), a.getKey()));
		
		return result;
	}
	
	/**
	 * Method that print the provided results list
	 * @param results - results to be printed
	 */
	private static void printResults(List<SimpleEntry<Double, Path>> results) {
		if (!checkResults(results)) return;
		
 		for (int i = 0, sz = Math.min(RESULT_LIST_PRINT_SIZE, results.size()); i < sz; ++i) {
			SimpleEntry<Double, Path> row = results.get(i);
			System.out.println(String.format(RESULT_FORMAT, i, row.getKey(), row.getValue()));
		}
	}
	
	/**
	 * Method that prints the file on the provided index in the results list
	 * @param results - results list
	 * @param args - {@link String} containing the index
	 */
	private static void printFile(List<SimpleEntry<Double, Path>> results, String args) {
		if (!checkResults(results)) return;
		args = args.trim();
		
		Integer ind;
		try {
			ind = Integer.parseInt(args);
		} catch (NumberFormatException ex) {
			System.out.println(String.format(INVALID_INTEGER, args));
			return;
		}
		
		int sz = results.size();
		if (ind < 0 || ind >= sz) {
			System.out.println(String.format(INTEGER_OUT_OF_BOUNDS, sz, ind));
			return;
		}
		
		String line;
		File f = results.get(ind).getValue().toFile();
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			System.out.println(String.format(FILE_READ_ERROR, f));
		}
	}
	
	/**
	 * Prints appropriate message if results are empty or have not been yet created
	 * @param results - results list
	 * @return false if results are empty or not created, else true
	 */
	private static boolean checkResults(List<SimpleEntry<Double, Path>> results) {
		if (results == null) {
			System.out.println(NO_RESULTS_MESSAGE);
			return false;
		}
		if (results.size() == 0) {
			System.out.println(RESULTS_EMPTY_MESSAGE);
			return false;
		}
		
		return true;
	}
}

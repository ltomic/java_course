package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;

public class Utils {

	/**
	 * Size of buffer used when reading files.
	 */
	public static int DEFAULT_CLUSTER_SIZE = 4096;
	/**
	 * Buffer used when reading files.
	 */
	public static byte[] buffer = new byte[DEFAULT_CLUSTER_SIZE];
	
	/**
	 * Key under which environments stack of directories is saved
	 */
	public static final String stackKey = "cdstack";

	/**
	 * Parses arguments of command converting them into a list of arguments.
	 * A single argument is a word or a sequence of non-whitespace characters
	 * surrounded by quotes. To escape a quote a \ escape symbol is used. Example :
	 * "test1 test2" " \" \\ " test1 -> ["test1 test2", " " \ ", "test1"]
	 * @param arguments string of arguments to be parsed
	 * @return list of parsed arguments
	 */
	public static String[] parse_arguments(String arguments) {
		ArrayList<String> args = new ArrayList<>();
		
		for (int i = 0, sz = arguments.length(); i < sz; ++i) {
			Character c = arguments.charAt(i);
			if (Character.isWhitespace(c)) continue;
			if (c.equals('"')) {
				i = parse_quotes(i, arguments, args);
			} else {
				i = parse_default(i, arguments, args);
			}
		}
		
		return args.toArray(new String[0]);
	}
	
	/**
	 * Parses a argument that is a sequence of characters surrounded by quotes, 
	 * adds it to the provided list of arguments and returns the index where the
	 * argument ended in string.
	 * @param ind index in string where the sequence of characters begins.
	 * @param text string to be parsed from
	 * @param list list to which the parsed argument should be added to.
	 * @return index where the argument ended in string
	 */
	public static int parse_quotes(int ind, String text, List<String> list) {
		StringBuilder parsed = new StringBuilder();
		
		ind++;
		for (int sz = text.length(); ind < sz; ++ind) {
			char c = text.charAt(ind);
			if (c == '\\' && ind + 1 < sz && (text.charAt(ind+1) == '"' || text.charAt(ind+1) == '\\')) {
				parsed.append(text.charAt(ind+1));
				++ind;
				continue;
			}
			parsed.append(c);
			if (c == '"') break;
		}
		
		if (parsed.length() == 0 || parsed.charAt(parsed.length()-1) != '"') {
			throw new IllegalArgumentException("Quotes not closed");
		}
		parsed.deleteCharAt(parsed.length()-1);
		list.add(parsed.toString());
		
		return ind;
	}
	
	/**
	 * Parses a sequence of non-whitespace characters into a argument, adds the
	 * argument to the provided list and returns the index where the argument ended.
	 * @param ind index in string where the argument begins
	 * @param text string to be parsed from
	 * @param list list to which the parsed arguments should be added
	 * @return index where the parsed argument ended in string
	 */
	public static int parse_default(int ind, String text, List<String> list) {
		StringBuilder parsed = new StringBuilder();
		
		for (int sz = text.length(); ind < sz; ++ind) {
			char c = text.charAt(ind);
			if (c == '"' || c == ' ') break;
			parsed.append(c);
		}
		
		list.add(parsed.toString());
		
		return ind;
	}

	/**
	 * Checks if given array of command arguments(Strings) has the given expected number
	 * of arguments. If false, prints the appropriate message to the environment
	 * and returns false, else return true.
	 * @param args array of {@link String}s whose size should be checked
	 * @param expected expected number of arguments
	 * @param env environment where the message should be printed if number of arguments
	 * 				does not match the expected number of arguments
	 * @return true if number of arguments is the expected one, else false
	 */
	public static boolean checkNumberOfArguments(String[] args, int expected, Environment env) {
		if (args.length != expected) {
			env.writeln("Invalid number of arguments. "
					+ "Expected " + expected + " given " + args.length);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if size of array of command arguments(String) is in the expected interval.
	 * If false, prints the appropriate message to the environment and returns false, else
	 * returns true.
	 * @param args array of {@link String} whose size should be checked
	 * @param left left bound of the expected interval
	 * @param right right bound of the expected interval
	 * @param env environment where the message should be printed if size is not in the interval
	 */
	public static boolean checkNumberOfArguments(String[] args, int left, int right, Environment env) {
		if (!(left <= args.length && args.length <= right)) {
			env.writeln("Invalid number of arguments. " + "Expected between " +
					left + " and " + right + ". Given " + args.length);
			return false;
		}
		return true;
	}

	/**
	 * Resolves the given path against the current directory of the given {@link Environment}
	 * @param path path to resolved
	 * @param env {@link Environment} whose current directory should be resolved against
	 * @return resolved path against the current directory of the given {@link Environment}
	 */
	public static Path resolveDirectory(String path, Environment env) {
		return env.getCurrentDirectory().resolve(Paths.get(path)).normalize();
	}

}

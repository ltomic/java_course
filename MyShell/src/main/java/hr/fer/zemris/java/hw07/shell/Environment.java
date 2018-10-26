package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Interface which is used by shell and commands to interact with user.
 * @author ltomic
 *
 */
public interface Environment {

	/**
	 * Reads a single line of input
	 * @return single line of input
	 * @throws ShellIOException
	 */
	String readLine() throws ShellIOException;
	/**
	 * Writes a given string to output
	 * @param text string to be outputted
	 * @throws ShellIOException
	 */
	void write(String text) throws ShellIOException;
	/**
	 * Writes a given string to output with newline
	 * @param text string to be outputted with newline
	 * @throws ShellIOException
	 */
	void writeln(String text) throws ShellIOException;
	/**
	 * Returns available commands stored as name -> ShellCommand mapping
	 * @return SortedMap with name of commands as key and ShellCommand as value
	 */
	SortedMap<String, ShellCommand> commands();
	/**
	 * Returns multiline symbol
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();
	/**
	 * Sets multiline symbol to the given symbol
	 * @param symbol new multiline symbol
	 */
	void setMultilineSymbol(Character symbol);
	/**
	 * Returns prompt symbol
	 * @return prompt symbol
	 */
	Character getPromptSymbol();
	/**
	 * Sets prompt symbol to the given symbol
	 * @param symbol new prompt symbol
	 */
	void setPromptSymbol(Character symbol);
	/**
	 * Returns morelines symbol
	 * @return morelines symbol
	 */
	Character getMorelinesSymbol();
	/**
	 * Sets morelines symbol to the given symbol
	 * @param symbol new morelines symbol
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Returns absolute normalized path to current directory this environment is in
	 * @return absolute normalized path to current directory this environment is in
	 */
	Path getCurrentDirectory();
	
	/**
	 * Sets path as a new current directory of this environment. If path is a relative path
	 * i.e. does not start with symbol '/', it is resolved against the current directory path
	 * @param path path to be set, if relative it is resolved against the current directory path
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Returns data (an Object) that this environment stores under the given key string.
	 * If no data is stored under the given key, null is returned.
	 * @param key string under which the data is stored
	 * @return data stored under the given string key
	 */
	Object getSharedData(String key);
	
	/**
	 * Sets given data object that this environment stored under the given key string.
	 * @param key key under which given value should be stored
	 * @param value value to be stored under the given key
	 */
	void setSharedData(String key, Object value);
}

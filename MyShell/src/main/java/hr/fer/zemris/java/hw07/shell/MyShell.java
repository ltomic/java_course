package hr.fer.zemris.java.hw07.shell;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.ShellCommands;

/**
 * This class implements the shell.
 * When shell is ready to recieve a command it prints out a prompt symbol.
 * By default prompt symbol is '>'. If command is in multiple lines at the
 * end of each line, expect the last one, there should be a moreline symbol.
 * By default moreline symbol is '\'. At the beginning of each new line shell
 * will print a multiline symbol. Propmt, moreline and multilines symbol can
 * be changed via command symbol. When giving command arguments each argument
 * is considered as a sequence of non-whitespace characters, except for group
 * of characters surrounded by ". Group of characters surrounded by " are considered
 * as one argument. Escaping character is '\' and cannot be changed.
 * Shell runs until exit command is given.
 * @author ltomic
 *
 */

public class MyShell {
	
	private static final String WELCOME_MESSAGE = "Welcome to MyShell v 1.0";
	
	private static final Character DEFAULT_PROMPT_SYMBOL = '>';
	private static final Character DEFAULT_MULTILINE_SYMBOL = '|';
	private static final Character DEFAULT_MORELINES_SYMBOL = '\\';
	
	private static final OutputStream DEFAULT_OUTPUT_STREAM = System.out; 
	private static final InputStream DEFAULT_INPUT_STREAM = System.in; 

	/**
	 * Path to directory where the program has benn started
	 */
	private static final Path currentDirectory = Paths.get(System.getProperty("user.dir"));
	
	/**
	 * Default environment for MyShell
	 */
	private static final MyEnvironment env = new MyEnvironment(
			DEFAULT_PROMPT_SYMBOL, DEFAULT_MULTILINE_SYMBOL, DEFAULT_MORELINES_SYMBOL, 
			DEFAULT_INPUT_STREAM, DEFAULT_OUTPUT_STREAM, currentDirectory,
			ShellCommands.values());
	
	/**
	 * Method called at the beginning of the program.
	 * @param args ignored
	 */
	public static void main(String[] args) {
		env.writeln(WELCOME_MESSAGE);
			
		while (true) {
			try {
			env.write(env.getPromptSymbol().toString() + " ");
			String input = readLinesFromConsole(env);
			if (input.length() == 0) continue;
			String[] splittedInput = input.split("\\s+");
			ShellCommand command = env.commands().get(splittedInput[0]);
			
			if (command == null) {
				env.writeln("No command '" + splittedInput[0] + "'");
				continue;
			}
			
			String arguments = input.substring(splittedInput[0].length()).trim();
			ShellStatus status = command.executeCommand(env, arguments);
			if (status == ShellStatus.TERMINATE) break;
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			} catch (RuntimeException ex) {
				env.writeln(ex.getMessage());
			}
		}
		
		try {
			env.close();
		} catch(IOException ignorable) {
		}
	}
	
	/**
	 * Helper method which reads the command given to shell and returns it as a single string.
	 * @return string of concatenated lines from shell input
	 */
	public static String readLinesFromConsole(Environment env) {
		StringBuilder lines = new StringBuilder();
		
		while (true) {
			String line = env.readLine().trim();
			lines.append(line);
			
			if (!line.endsWith(" " + env.getMorelinesSymbol())) break;
			lines.deleteCharAt(lines.length()-1);
			
			env.write(env.getMultilineSymbol().toString() + " ");
		}
		
		return lines.toString();
	}
	
}

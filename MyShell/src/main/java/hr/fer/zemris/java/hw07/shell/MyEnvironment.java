package hr.fer.zemris.java.hw07.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.ShellCommands;

/**
 * Implementation of the {@link Environment} interface for {@link MyShell}
 * @author ltomic
 *
 */
public class MyEnvironment implements Environment {
	
	/**
	 * Available commands stored in SortedMap with name of commands as keys
	 * and command objects as values
	 */
	private SortedMap<String, ShellCommand> commands;
	
	/**
	 * Stores shared data of the environment 
	 */
	private HashMap<String, Object> sharedData;
	
	private Character promptSymbol;
	private Character multilineSymbol;
	private Character morelinesSymbol;
	
	/**
	 * Used for reading from input
	 */
	private BufferedReader reader;
	/**
	 * Used for writing to output
	 */
	private BufferedWriter writer;
	
	/**
	 * Environments current directory
	 */
	private Path currentDirectory;
	
	/**
	 * Constructs a new MyEnvironment with given arguments
	 * @param prompt prompt symbol to be used
	 * @param multiline multiline symbol to be used
	 * @param moreline moreline symbol to be used
	 * @param in stream to read from
	 * @param out stream to write to
	 * @param commands array of available commands
	 */
	public MyEnvironment(Character prompt, Character multiline, Character moreline, 
			InputStream in, OutputStream out, Path dir, ShellCommands[] commands) {
		this.promptSymbol = prompt;
		this.multilineSymbol = multiline;
		this.morelinesSymbol = moreline;
		reader = new BufferedReader(new InputStreamReader(in));
		writer = new BufferedWriter(new OutputStreamWriter(out));
		this.currentDirectory = dir.toAbsolutePath().normalize();
		sharedData = new HashMap<>();

		this.commands = new TreeMap<>();
		for (ShellCommands i : commands) {
			this.commands.put(i.getName(), i.getCommand());
		}
	}
	
	@Override
	public void writeln(String text) throws ShellIOException {
		write(text + "\n");
	}
	
	@Override
	public void write(String text) throws ShellIOException {
		try {
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {
			throw new ShellIOException("Could not write to System.out");
		}
	}
	
	@Override
	public String readLine() throws ShellIOException {
		try {
			return reader.readLine();
		} catch (IOException ex) {
			throw new ShellIOException("Could not read from System.in");
		}
	}
	
	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = Objects.requireNonNull(symbol, "symbol cannot be null");			
	}
	
	@Override
	public void setMultilineSymbol(Character symbol) {
		multilineSymbol = Objects.requireNonNull(symbol, "symbol cannot be null");
	}
	
	@Override
	public void setMorelinesSymbol(Character symbol) {
		morelinesSymbol = Objects.requireNonNull(symbol, "symbol cannot be null");
	}
	
	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}
	
	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}
	
	@Override
	public Character getMorelinesSymbol() {
		return morelinesSymbol;
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return commands;
	}
		
	/**
	 * Closes input and output streams, should be called when closing the progam
	 * @throws IOException
	 */
	public void close() throws IOException {
		reader.close();
		writer.close();
	}

	@Override
	public Path getCurrentDirectory() {
		return currentDirectory;
	}

	@Override
	public void setCurrentDirectory(Path path) {
		if (!Files.exists(path)) 
			throw new IllegalArgumentException("Given directory does not exist");
		if (!Files.isDirectory(path))
			throw new IllegalArgumentException("Given path is not a directory");
		
		currentDirectory = path;
	}

	@Override
	public Object getSharedData(String key) {
		return sharedData.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		Objects.requireNonNull(key, "key cannot be null");
		
		sharedData.put(key, value);
	}
}

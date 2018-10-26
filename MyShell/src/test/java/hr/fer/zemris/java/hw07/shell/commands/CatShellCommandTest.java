package hr.fer.zemris.java.hw07.shell.commands;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyEnvironment;

public class CatShellCommandTest {
	
	private static ByteArrayOutputStream out;
	
	private static MyEnvironment env;
	
	private static final Path currentDirectory = Paths.get(System.getProperty("user.dir"));

	@Before
	public void init() throws IOException{
		out = new ByteArrayOutputStream();
		env = new MyEnvironment(null, null, null, System.in, out, currentDirectory, ShellCommands.values());
	}
	
	public static String readFileToString(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)) ,Charset.defaultCharset());
		} catch (IOException ex) {
			throw new IllegalArgumentException("Could not read the file");
		}
	}
	
	@Test
	public void emptyFile() {
		String path = "src/test/java/resources/empty.txt";
		String expected = readFileToString(path);
		ShellCommands.CAT.getCommand().executeCommand(env, path);
		
		assertEquals(expected.toString(), out.toString());
	}
	
	@Test
	public void oneLineFile() {
		String path = "src/test/java/resources/oneline.txt";
		String expected = readFileToString(path);
		ShellCommands.CAT.getCommand().executeCommand(env, path);
		
		assertEquals(expected.toString(), out.toString());
	}
	
	@Test
	public void multipleLinesFile() {
		String path = "src/test/java/resources/multipleLines.txt";
		String expected = readFileToString(path);
		ShellCommands.CAT.getCommand().executeCommand(env, path);
		
		assertEquals(expected.toString(), out.toString());
	}
	
	@Test
	public void pathInQuotes() {
		String path = "src/test/java/resources/multipleLines.txt";
		String expected = readFileToString(path);
		ShellCommands.CAT.getCommand().executeCommand(env, "\"" + path + "\"");
		
		assertEquals(expected.toString(), out.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidPath() {
		String path = "src/test/java/resources/multipleLines.txt";
		String expected = readFileToString(path);
		ShellCommands.CAT.getCommand().executeCommand(env, "\"" + path);
		
		assertEquals(expected.toString(), out.toString());
	}
	
	@Test
	public void invalidNumberOfArguments() {
		String path = "src/test/java/resources/multipleLines.txt";
		String expected = "Invalid number of arguments\n";
		ShellCommands.CAT.getCommand().executeCommand(env, path + " UTF-8" + " bla.txt");
		
		assertEquals(expected.toString(), out.toString());
	}

}

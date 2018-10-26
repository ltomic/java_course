package hr.fer.zemris.java.hw07.shell.commands;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.hw07.shell.MyEnvironment;

public class CharsetsShellCommandTest {

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
	public void testCharsets() {
		StringBuilder expected = new StringBuilder("Available charsets:\n");
		Charset.availableCharsets().forEach((key, value) -> expected.append(key + "\n"));
		ShellCommands.CHARSETS.getCommand().executeCommand(env, "");
		
		assertEquals(expected.toString(), out.toString());
	}

}

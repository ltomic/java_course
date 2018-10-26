package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.crypto.Util;

/**
 * Command prints the given file in hexadecimal format. Example
 * 
 * 00000000: 31 2E 20 4F 62 6A 65 63|74 53 74 61 63 6B 20 69 | 1. ObjectStack i
 * 00000010: 6D 70 6C 65 6D 65 6E 74|61 63 69 6A 61 0D 0A 32 | mplementacija..2 
 * @author ltomic
 *
 */
public class HexdumpShellCommand implements ShellCommand{

	/**
	 * Buffer that is used to control the amount of input read from stream
	 */
	private static byte[] buffer = new byte[8];
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, 1, env)) return ShellStatus.CONTINUE;
		
		File file = Utils.resolveDirectory(args[0], env).toFile();		
		try (BufferedInputStream reader = new BufferedInputStream(
				new FileInputStream(file))) {
			for (int i = 0; true; ++i) {
				String line = generateLine(i, reader);
				if (line == null) break;
				env.writeln(line);
			}
		} catch (FileNotFoundException ex) {
			env.writeln("File could not be found");
			return ShellStatus.CONTINUE;
		} catch (IOException ex) {
			env.writeln("Error while reading the file");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Generates a single line of hexadecimal format from line read from given stream.
	 * @param i line index
	 * @param reader stream from which a line should be read
	 * @return single line of hexadecimal format
	 * @throws IOException
	 */
	private String generateLine(int i, BufferedInputStream reader) throws IOException {
		int readSize = reader.read(buffer);
		if (readSize == -1) return null;
		
		StringBuilder line = new StringBuilder();
		String text = new String(buffer);
		line.append(String.format("%010d: ", i*10));
		line.append(generateHex(buffer, readSize)+"| ");
		
		readSize = reader.read(buffer);
		line.append(generateHex(buffer, readSize)+" | ");
		text = text + new String(buffer);
		text = text.replaceAll("\\s+", " ");
		line.append(text);
		
		return line.toString();
	}

	/**
	 * Generates a sequence of hexadecimal number generated from the given byte array
	 * @param bytes array of bytes to generate from
	 * @param readSize size of array
	 * @return string of hexadecimal number generated from the given byte array
	 */
	private String generateHex(byte[] bytes, int readSize) {
		String transformed = "";
		
		if (readSize != -1) {
			transformed = Util.bytetohex(bytes)
				.replaceAll("..", "$0 ")
				.substring(0, readSize*3-1);
		}
		
		return String.format("%-" + (3*8-1) + "s", transformed);
	}

	@Override
	public String getCommandName() {
		return ShellCommands.HEXDUMP.getName();
	}

	private static String commandDescription = 
			"Command prints the given file in hexadecimal format.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}
	
}

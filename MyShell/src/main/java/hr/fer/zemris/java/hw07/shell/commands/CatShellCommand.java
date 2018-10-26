package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * This command prints the file given by file path interpreted with given
 * charset. If no charset is given the command uses a default charset.
 * List of available charsets can be seen with charset command.
 * @author ltomic
 *
 */
public class CatShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		
		if (!Utils.checkNumberOfArguments(args, 1, 2, env)) return ShellStatus.CONTINUE;
		
		Charset charset = Charset.defaultCharset();
		if (args.length == 2) {
			try {
				charset = Charset.forName(args[1]);
			} catch (IllegalCharsetNameException|UnsupportedCharsetException ex) {
				env.writeln("Given charset name is not found or is not supported");
			}
		}
		
		Path dir = Utils.resolveDirectory(args[0], env);
		try (BufferedReader reader = Files.newBufferedReader(dir, charset)) {
			char[] buffer = new char[4096];
			while (true) {
				int readSize = reader.read(buffer);
				if (readSize == -1) break;
				env.write(new String(buffer, 0, readSize));
			}
		} catch (NoSuchFileException ex) {
			env.writeln("Given file cannot be found");
			return ShellStatus.CONTINUE;
		} catch (IOException ex) {
			env.writeln("I/O error happend\n" + ex);
			return ShellStatus.TERMINATE;
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.CAT.getName();
	}
	
	private static String commandDescription = 
			"This command prints the file given by file path interpreted with given\n" + 
			"charset. If no charset is given the command uses a default charset.\n" + 
			"List of available charsets can be seen with charset command.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

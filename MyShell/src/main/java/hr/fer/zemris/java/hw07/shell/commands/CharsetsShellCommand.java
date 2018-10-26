package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints all system available charsets. Receives no arguments. 
 * @author ltomic
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		
		env.writeln("Available charsets:");
		for (String i : charsets.keySet()) {
			env.writeln(i);
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.CHARSETS.getName();
	}
	
	private static String commandDescription = 
			"Prints all system available charsets. Receives no arguments.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

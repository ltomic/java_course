package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Changes the current directory to the one given as argument. Receives only argument -
 * new current directory. Path to the new current directory can be relative to the old
 * current directory or absolute.
 * @author ltomic
 *
 */
public class CdShellCommand implements ShellCommand {
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		
		if (!Utils.checkNumberOfArguments(args, 1, env)) return ShellStatus.CONTINUE;
		
		Path dir = Utils.resolveDirectory(args[0], env);
		
		try {
			env.setCurrentDirectory(dir);
		} catch (IllegalArgumentException ex) {
			env.writeln(ex.getMessage());
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.CD.getName();
	}

	private static String commandDescription = 
			"Changes the current directory to the one given as argument. Receives only argument -\n" + 
			"new current directory. Path to the new current directory can be relative to the old\n" + 
			"current directory or absolute.";
	
	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}
	
}

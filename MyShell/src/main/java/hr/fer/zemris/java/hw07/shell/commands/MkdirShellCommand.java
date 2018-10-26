package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Commands takes a single argument directory name and creates the appropriate
 * directory structure.
 * @author ltomic
 *
 */
public class MkdirShellCommand implements ShellCommand{

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, 1, env)) return ShellStatus.CONTINUE;
		
		File dir = Utils.resolveDirectory(args[0], env).toFile();
		dir.mkdirs();
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.MKDIR.getName();
	}

	private static String commandDescription = 
			"Commands takes a single argument directory name and creates the appropriate\n" + 
			"directory structure.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

	
}

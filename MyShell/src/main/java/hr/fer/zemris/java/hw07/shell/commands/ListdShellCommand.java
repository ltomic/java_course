package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints paths of all saved directories. If there are no saved directories
 * prints an appropriate message.
 * @author ltomic
 *
 */
public class ListdShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>)env.getSharedData(Utils.stackKey);
		
		if (stack.isEmpty()) {
			env.writeln("No saved directories");
			return ShellStatus.CONTINUE;
		}
		
		for (Path i : stack) {
			env.writeln(i.toString());
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.LISTD.getName();
	}
	
	private static final String commandDescription = 
			"Prints paths of all saved directories. If there are no saved directories\n" + 
			"prints an appropriate message.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

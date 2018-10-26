package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Removes the top element from the environments directory stack.
 * @author ltomic
 *
 */
public class DropdShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		PopdShellCommand.popDirStack(env);
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.DROPD.getName();
	}
	
	private static final String commandDescription = 
			"Removes the top element from the environments directory stack.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

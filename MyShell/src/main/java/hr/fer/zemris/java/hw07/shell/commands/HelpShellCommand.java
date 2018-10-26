package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command expects a command name as argument and prints the description of
 * the given command. If no arguments are given, command print all available
 * commands.
 * @author ltomic
 *
 */
public class HelpShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		
		switch (args.length) {
		case 0:
			printAllCommands(env);
			break;
		case 1:
			printCommandDescription(env, args[0]);
			break;
		default:
			env.writeln("Invalid number of arguments given");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.HELP.getName();
	}
	
	private static String commandDescription = 
			"Command expects a command name as argument and prints the description of\n" + 
			"the given command. If no arguments are given, command print all available\n" + 
			"commands.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}
	
	/**
	 * Prints all available commands.
	 * @param env environment in which the commands are
	 */
	private void printAllCommands(Environment env) {
		for (String i : env.commands().keySet()) {
			env.writeln(i);
		}
	}

	/**
	 * Prints the command description of the given command.
	 * @param env environment in which the command is
	 * @param command command whose description should be printed
	 */
	private void printCommandDescription(Environment env, String command) {
		ShellCommand shellCommand = env.commands().get(command);
		
		if (shellCommand == null) {
			env.writeln("Command " + command + " does not exist");
		}
		
		env.writeln(shellCommand.getCommandName());
		for (String i : shellCommand.getCommandDescription()) {
			env.writeln(i);
		}
	}
}

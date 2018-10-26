package hr.fer.zemris.java.hw07.shell;

import java.util.List;

import hr.fer.zemris.java.hw07.shell.commands.ShellCommands;

/**
 * Interface which is used by shell to execute commands.
 * @author ltomic
 *
 */
public interface ShellCommand {

	/**
	 * Executes this command in given {@link Environment} with given arguments
	 * @param env {@link Environment} in which the command should be executed
	 * @param arguments arguments of the command
	 * @return signal to the shell what to do after the command is executed
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	/**
	 * Returns command name. This method is used by help command.
	 * All command names are defined in {@link ShellCommands} enumeration.
	 * @return command name
	 */
	String getCommandName();
	/**
	 * Returns command description. This is used by help command.
	 * @return command description
	 */
	List<String> getCommandDescription();
}

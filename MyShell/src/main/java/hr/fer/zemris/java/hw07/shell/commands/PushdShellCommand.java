package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pushes the current directory to environments stack of directories and
 * sets the directory given through command argument as new current directory.
 * @author ltomic
 *
 */
public class PushdShellCommand implements ShellCommand {
	
	/**
	 * Smallest valid number of arguments this command accepts
	 */
	private static final int leftArgumentsSize = 1;
	/**
	 * Largest valid number of arguments this command accepts
	 */
	private static final int rightArgumentsSize = 1;

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, leftArgumentsSize, rightArgumentsSize, env)) 
			return ShellStatus.CONTINUE;
		
		Path newDir = Utils.resolveDirectory(args[0], env);
		if (!Files.exists(newDir)) {
			env.writeln("Given directory does not exist");
		}
		if (!Files.isDirectory(newDir)) {
			env.writeln("Given path is not a directory");
		}
		
		pushDirToStack(env, env.getCurrentDirectory());
		env.setCurrentDirectory(newDir);
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.PUSHD.getName();
	}

	private static final String commandDescription = 
			"Pushes the current directory to environments stack of directories and\n" + 
			" * sets the directory given through command argument as new current directory.";
	
	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}
	

	
	/**
	 * Pushes the given directory to the stack of directories of the given environment
	 * @param env environment on whose stack of directories should directory be pushed
	 * @param dir directory to be pushed
	 */
	private static void pushDirToStack(Environment env, Path dir) {
		@SuppressWarnings("unchecked")
		Stack<Path> dirStack = (Stack<Path>)env.getSharedData(Utils.stackKey);
		
		if (dirStack == null) {
			dirStack = new Stack<>();
			env.setSharedData(Utils.stackKey, dirStack);
		}
		
		dirStack.push(dir);
	}
}

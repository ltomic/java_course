package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pops the directory from the environments directory stack and sets it as
 * current directory. If there are no directories on stack message is printed
 * and nothing else happens.
 * @author ltomic
 *
 */
public class PopdShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		env.setCurrentDirectory(popDirStack(env));
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Removes the top directory from the environments directory stack and 
	 * returns it.
	 * @param env environments from whose stack directory should be popped
	 * @return top directory of the environments directory stack
	 */
	public static Path popDirStack(Environment env) {
		@SuppressWarnings("unchecked")
		Stack<Path> dirStack = (Stack<Path>)env.getSharedData(Utils.stackKey);
		
		if (dirStack == null || dirStack.isEmpty()) {
			env.writeln("No directories on stack");
			return env.getCurrentDirectory();
		}
		
		return dirStack.pop();
	}

	@Override
	public String getCommandName() {
		return ShellCommands.POPD.getName();
	}

	private static final String commandDescription = 
			"Pops the directory from the environments directory stack and sets it as\n" + 
			"current directory. If there are no directories on stack message is printed\n" + 
			"and nothing else happens.";
	
	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

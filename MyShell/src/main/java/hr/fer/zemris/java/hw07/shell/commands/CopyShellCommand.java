package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Copies file from given by first argument as file path to file path given as
 * second argument. If a file already exists on a given file path, command queries
 * the user should it overwrite. If user answers yes, the command overwrites the
 * existing file, else it does not. If a directory is given as second argument,
 * the command copies the file into that directory with name of the new file
 * same as the source file.
 * @author ltomic
 *
 */
public class CopyShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		
		if (!Utils.checkNumberOfArguments(args, 2, env)) return ShellStatus.CONTINUE;
		
		File source = Utils.resolveDirectory(args[0], env).toFile();
		if (source.isDirectory()) {
			env.writeln("Expected file, given directory");
			return ShellStatus.CONTINUE;
		}
		File target = Utils.resolveDirectory(args[1], env).toFile();
		if (target.isDirectory()) {
			args[1] = Paths.get(args[1]).resolve(source.getName()).toString();
		}
		
		checkOverwriteCopyFile(env, source, target);
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Copies the file given as first argument to the target file path
	 * given as second argument. If a file already exists on the target file path
	 * user is asked if he wishes to overwrite the file.
	 * @param env shell environment
	 * @param source file to be copied
	 * @param target destination file
	 */
	public static void checkOverwriteCopyFile(Environment env, File source, File target) {
		if (target.exists()) {
			env.writeln("Target file " + target.toString() + " already exists. Overwrite? (yes (?)).");
			if (!"yes".equals(env.readLine().trim().toLowerCase())) return;
		}
		
		try {
			copyFile(source, target);
		} catch (IOException ex) {
			env.writeln("I/O error occured while copying");
		}
	}
	
	/**
	 * Copies the file given as first argument to the target file path
	 * given as second argument.
	 * @param source file to be copied
	 * @param target destination file
	 * @throws IOException
	 */
	public static void copyFile(File source, File target) throws IOException {
		try (BufferedInputStream reader = new BufferedInputStream(
				new FileInputStream(source));
			BufferedOutputStream writer = new BufferedOutputStream(
					new FileOutputStream(target, false))) {
				
			while (true) {
				int readSize = reader.read(Utils.buffer);
				if (readSize == -1) break;
				writer.write(Utils.buffer, 0, readSize);
			}
		}
	}

	@Override
	public String getCommandName() {
		return ShellCommands.COPY.getName();
	}

	private static final String commandDescription = 
			"Copies file from given by first argument as file path to file path given as\n" + 
			"second argument. If a file already exists on a given file path, command queries\n" + 
			"the user should it overwrite. If user answers yes, the command overwrites the\n" + 
			"existing file, else it does not. If a directory is given as second argument,\n" + 
			"the command copies the file into that directory with name of the new file\n" + 
			"same as the source file.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}


}

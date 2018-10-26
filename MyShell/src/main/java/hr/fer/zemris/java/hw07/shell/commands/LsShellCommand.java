package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command expects path to a directory as a single argument.
 * Prints all information about files and directories in the given directory :
 * permissions, size, creation timedate and name.
 * @author ltomic
 *
 */
public class LsShellCommand implements ShellCommand {

	
	/**
	 * Format in which creation timedate should be printed
	 */
	private static String TIME_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat TIME_DATE_FORMAT = 
		new SimpleDateFormat(TIME_DATE_FORMAT_PATTERN);
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, 1, env)) return ShellStatus.CONTINUE;
		
		if (args[0].equals("")) {
			args[0] = new String(".");
		}
		
		File dir = Utils.resolveDirectory(args[0], env).toFile();
		if (!dir.isDirectory()) {
			env.writeln("Given path must point to a directory.");
			return ShellStatus.CONTINUE;
		}
		
		for (File child : dir.listFiles()) {
			BasicFileAttributes attr;
			try {
				attr = Files.readAttributes(
						child.toPath(), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
			} catch (IOException ex) {
				env.writeln("Error while reading a file");
				return ShellStatus.CONTINUE;
			}
			
			env.writeln(String.format("%c%c%c%c %10d %s %s",
				attr.isDirectory() ? 'd' : '-',
				child.canRead () ? 'r' : '-',
				child.canWrite() ? 'w' : '-',
				child.canExecute() ? 'x' : '-',
				child.length(),
				TIME_DATE_FORMAT.format(new Date(attr.creationTime().toMillis())),
				child.getName()
			));
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.LS.getName();
	}
	
	private static String commandDescription = 
			"Command expects path to a directory as a single argument.\n" + 
			" Prints all information about files and directories in the given directory :\n" + 
			" permissions, size, creation timedate and name.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

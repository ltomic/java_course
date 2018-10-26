package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Removes the given directory from the file system, even if it is non-empty.
 * @author ltomic
 *
 */
public class RmtreeShellCommand implements ShellCommand {
	
	private static final int argsNumber = 1;

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, argsNumber, env)) return ShellStatus.CONTINUE;
		
		File dir = Utils.resolveDirectory(args[0], env).toFile();
		if (!dir.exists()) {
			env.writeln("Given directory does not exist");
			return ShellStatus.CONTINUE;
		}
		if (!dir.isDirectory()) {
			env.writeln("Given path is not directory");
			return ShellStatus.CONTINUE;
		}
		
		try {
			delete(dir.toPath());
		} catch (IOException ex) {
			env.writeln("There was an IO error while deleting");
		}
		
		return ShellStatus.CONTINUE;
	}

	private void delete(Path dir) throws IOException {
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path f, BasicFileAttributes attr) throws IOException {
				Files.delete(f);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@Override
	public String getCommandName() {
		return ShellCommands.RMTREE.getName();
	}

	private static final String commandDescription = 
			"Removes the given directory from the file system, even if it is non-empty.";
	
	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

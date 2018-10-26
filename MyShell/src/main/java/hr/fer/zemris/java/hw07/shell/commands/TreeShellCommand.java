package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints all subfiles and subdirectories of a given directory in a tree-like format.
 * Command accepts one arguments - directory whose substructure should be printed.
 * @author ltomic
 *
 */
public class TreeShellCommand implements ShellCommand{
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, 1, env)) return ShellStatus.CONTINUE;
		
		File dir = Utils.resolveDirectory(args[0], env).toFile();
		if (!dir.isDirectory()) {
			env.writeln("Given path must point to a directory.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.walkFileTree(Paths.get(args[0]),  new SimpleFileVisitor<Path>() {

				StringBuilder indent = new StringBuilder();
				
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
					indent.delete(indent.length()-2, indent.length());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
					env.writeln(indent.toString() + dir.getFileName().toString());
					indent.append("  ");
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path f, BasicFileAttributes attr) throws IOException {
					env.writeln(indent + f.toString());
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException ex) {
			env.writeln("I/O error while walking file tree");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return ShellCommands.TREE.getName();
	}

	private static String commandDescription = 
			"Prints all subfiles and subdirectories of a given directory in a tree-like format.\n" + 
			"Command accepts one arguments - directory whose substructure should be printed.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}
}

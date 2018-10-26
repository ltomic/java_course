package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Copies directory tree given as first argument into the directory given as second argument.
 * If the final directory in the destination path does not exist, it is treated as a root
 * directory for the copies tree. For example first argument is ../path1/dir1 and 
 * second is path2/dir2 and directory path2 exist but dir2 does not subdirectories and files
 * from dir1 will be copies into dir2.
 * @author ltomic
 *
 */
public class CptreeShellCommand implements ShellCommand {

	private static final int argsNumber = 2;
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (!Utils.checkNumberOfArguments(args, argsNumber, env)) return ShellStatus.CONTINUE;
		
		File source = Utils.resolveDirectory(args[0], env).toFile();
		if (!source.exists()) {
			env.writeln("Given source directory does not exist");
			return ShellStatus.CONTINUE;
		}
		
		if (!source.isDirectory()) {
			env.writeln("Given source must be a directory");
			return ShellStatus.CONTINUE;
		}
		
		File target = Utils.resolveDirectory(args[1], env).toFile();
		if (!target.getParentFile().exists()) {
			env.writeln("Given target directory does not exist");
		}
		
		ArrayList<Entry<File, File>> copyTree = new ArrayList<>();
		FileVisitor<Path> listTree = new SimpleFileVisitor<Path>() {
			
			private Path current = target.toPath();
			
			@Override
			public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
				current = current.getParent();
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes arg1) throws IOException {
				current = current.resolve(dir.getFileName());
				copyTree.add(new AbstractMap.SimpleEntry<File, File>(dir.toFile(), current.toFile()));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes arg1) throws IOException {
				copyTree.add(new AbstractMap.SimpleEntry<File, File>(file.toFile(), current.toFile()));
				return FileVisitResult.CONTINUE;
			}	
		};

		try {
			if (target.exists()) {
				Files.walkFileTree(source.toPath(), listTree);
			} else {
				for (File child : source.listFiles()) {
					Files.walkFileTree(child.toPath(), listTree);
				}
			}
		} catch (IOException ex) {
			env.writeln("Error while listing directory tree");
			return ShellStatus.CONTINUE;
		}

		for (Entry<File, File> i : copyTree) {
			if (i.getKey().isDirectory()) {
				i.getValue().mkdirs();
			} else {
				i.getValue().getParentFile().mkdirs();
				CopyShellCommand.checkOverwriteCopyFile(env, i.getKey(), i.getValue());
			}
		}
		return ShellStatus.CONTINUE;
	}
	
	@Override
	public String getCommandName() {
		return ShellCommands.CPTREE.getName();
	}
	
	private static final String commandDescription = 
			"Copies directory tree given as first argument into the directory given as second argument.\n" + 
			"If the final directory in the destination path does not exist, it is treated as a root\n" + 
			"directory for the copies tree. For example first argument is ../path1/dir1 and \n" + 
			"second is path2/dir2 and directory path2 exist but dir2 does not subdirectories and files\n" + 
			"from dir1 will be copies into dir2.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

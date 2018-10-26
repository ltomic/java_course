package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderInfo;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderParser;

/**
 * This command is used to "massively" rename/move files(not directories) that are directly
 * in directory given as first argument. The files will be moved to the directory given as
 * second argument. This command has 4 subcommands, subcommands are given as third argument
 * 		1. filter - filters the files using pattern from the fourth argument and prints their
 * 					names
 * 		2. groups - prints capturing groups for files matching the pattern
 * 		3. show   - this subcommand receives the additional fifth argument - an expression
 * 					that defines transformation of file names. It prints the original file
 * 					names and transformed file names. Expression can characters and
 * 					substitution groups which are in form of 
 * 					${ _nonnegative_integer_ , (optional_zero)(optional)_nonnegative_integer_), the 
 * 					first number defines the index of a group in the matching pattern which
 * 					will be inserted in the expression, second optional number defines the minimal
 * 					width of that group, if groups length is less than that number, the
 * 					group is padded with spaces or zeros depending on if there is a optional
 * 					zero before the second number and then the group is inserted into the
 * 					expression.
 * 		4. execute	Executes the command.
 * 
 * If whitespace character is needed in some argument wrap the argument with quotes.
 * Quotes are then not supported!
 * 
 * Examples :
 * massrename DIR1 DIR2 filter slika\d+-[^.]+\.jpg
 * massrename DIR1 DIR2 groups slika(\d+)-([^.]+)\.jpg
 * massrename DIR1 DIR2 show slika(\d+)-([^.]+)\.jpg gradovi-${2}-${1,03}.jpg
 * massrename DIR1 DIR2 execute slika(\d+)-([^.]+)\.jpg gradovi-${2}-${1,03}.jpg
 * @author ltomic
 *
 */
public class MassrenameShellCommand implements ShellCommand {

	/**
	 * Lower and upper bound on number of arguments
	 */
	private static final int minArgs = 4;
	private static final int maxArgs = 5;
	
	/**
	 * Names of the subcommands
	 */
	private static final String commandNameFilter = "filter";
	private static final String commandNameGroups = "groups";
	private static final String commandNameShow = "show";
	private static final String commandNameExecute = "execute";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		
		if (!Utils.checkNumberOfArguments(args, minArgs, maxArgs, env)) 
			return ShellStatus.CONTINUE;
		
		File source = new File(args[0]);
		if (!source.isDirectory()) {
			env.writeln("First argument must be a path to directory");
		}
		
		File target = new File(args[1]);
		if (!target.isDirectory()) {
			env.writeln("Second argument must be a path to directory");
		}
		
		try {
			switch (args[2].trim()) {
			case commandNameFilter:
				print_filter(source, args[3], env);
				break;
			case commandNameGroups:
				print_groups(source, args[3], env);
				break;
			case commandNameShow:
				if (args.length != 5) {
					env.writeln("Expected 5 arguments, given " + args.length);
					return ShellStatus.CONTINUE;
				}
				print_show(source, args[3], args[4], env);
				break;
			case commandNameExecute:
				if (args.length != 5) {
					env.writeln("Expected 5 arguments, given " + args.length);
				}
				execute(source, target, args[3], args[4], env);
				break;
			default:
				env.writeln("No matching subcommand name, given " + args[2].trim());
			}
		} catch (PatternSyntaxException ex) {
			env.writeln("Given pattern is not valid");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Method used to execute filter subcommand with given arguments.
	 * @param source parent directory of the files filtered
	 * @param mask pattern used to filter file names
	 * @param env shell environment
	 */
	private static void print_filter(File source, String mask, Environment env) {
		for (Entry<File, String[]> i: filter(source, mask)) {
			env.writeln(i.getKey().getName());
		}
	}
	
	private static final int patternFlags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	
	/**
	 * Method that filters the files of the source directory using mask as pattern.
	 * @param source parent directory of files to be filtered
	 * @param mask pattern used to filter file names
	 * @return list of pairs (file name, groups captured by pattern)
	 */
	private static ArrayList<Entry<File, String[]>> filter(File source, String mask) {
		Pattern pattern = Pattern.compile(mask, patternFlags);		
		ArrayList<Entry<File, String[]>> result = new ArrayList<>();
		
		for (File i: source.listFiles()) {
			if (!i.isFile()) continue;
			Matcher matcher = pattern.matcher(i.getName());
			if (!matcher.matches()) continue;
			
			String[] groups = createGroups(matcher);
			result.add(new AbstractMap.SimpleEntry<File, String[]>(i, groups));			
		}
		
		return result;
	}
	
	/**
	 * Returns an array of strings representing groups captured by matcher
	 * @param matcher matcher that captured the groups
	 * @return a an array of strings representing groups captured by matcher
	 */
	private static String[] createGroups(Matcher matcher) {
		int count = matcher.groupCount()+1;
		String[] groups = new String[count];
		for (int j = 0; j < count; ++j) {
			groups[j] = matcher.group(j);
		}
		
		return groups;
	}
	
	/**
	 * Executes the groups command.
	 * @param source parent directory of the files filtered
	 * @param mask pattern used to filter file names and capture groups
	 * @param env shell environment
	 */
	private static void print_groups(File source, String mask, Environment env) {
		for (Entry<File, String[]> i : filter(source, mask)) {
			env.write(i.getKey().getName());
			String[] groups = i.getValue();
			for (int j = 0, len = groups.length; j < len; ++j) {
				env.write(" " + j + ": " + groups[j]);
			}
			env.writeln("");
		}
	}
	
	/**
	 * Creates a list of pairs (file name, transformed file name) using arguments as in show
	 * command.
	 * @param source parent directory of the files filtered
	 * @param mask pattern used to filter file names and capture groups
	 * @param expr expression used to transform the file name
	 * @return List of pairs (file name, transformed file name)
	 */
	private static ArrayList<Entry<File, String>> show(File source, String mask, String expr) {
		NameBuilderParser parser = new NameBuilderParser(expr);
		NameBuilder builder = parser.getNameBuilder();
		Pattern pattern = Pattern.compile(mask, patternFlags);
		ArrayList<Entry<File, String>> result = new ArrayList<>();
		
		for (File i : source.listFiles()) {
			if (!i.isFile()) continue;
			Matcher matcher = pattern.matcher(i.getName());
			if (!matcher.matches()) continue;
			NameBuilderInfo info = createInfo(matcher);
			builder.execute(info);
			String newName = info.getStringBuilder().toString();
			result.add(new AbstractMap.SimpleEntry<File, String>(i, newName));
		}
		
		return result;
	}
	
	/**
	 * Creates a {@link NameBuilderInfo} that contains information about matching groups
	 * and hold the transforming file name.
	 * @param matcher matcher that matched the filename and pattern
	 * @return a {@link NameBuilderInfo} that contains information about matching groups
	 * 			and hold the transforming file name.
	 */
	private static NameBuilderInfo createInfo(Matcher matcher) {
		String[] groupList = createGroups(matcher);
		return new NameBuilderInfo() {
			
			private String[] groups = groupList;
			private StringBuilder builder = new StringBuilder();
			
			@Override
			public StringBuilder getStringBuilder() {
				return builder;
			}
			
			@Override
			public String getGroup(int index) {
				if (index < 0 || index >= groups.length) throw new IndexOutOfBoundsException(
						"Given index " + index + ", length " + groups.length);
				
				return groups[index];
			}
		};
	}
	
	/**
	 * Executes the show subcommand with given arguments.
	 * @param source parent directory of the files filtered
	 * @param mask pattern used to filter file names and capture groups
	 * @param expr expression used to transform the file name
	 * @param env shell environment
	 */
	private static void print_show(File source, String mask, String expr, Environment env) {
		for (Entry<File, String> i : show(source, mask, expr)) {
			env.writeln(i.getKey().getName() + " => " + i.getValue());
		}
	}
	
	/**
	 * Executes the command with given arguments
	 * @param source parent directory of the files filtered
	 * @param target directory where the filtered files should be moved.
	 * @param mask pattern used to filter file names and capture groups
	 * @param expr expression used to transform the file name
	 * @param env shell environment
	 */
	private static void execute(File source, File target, String mask, String expr, Environment env) {
		for (Entry<File, String> i : show(source, mask, expr)) {
			Path dest = target.toPath().resolve(i.getValue());
			env.writeln(i.getKey() + " => " + dest);
			
			try {
				Files.move(i.getKey().toPath(), dest);
			} catch (IOException ex) {
				env.writeln("Error while moving " + i.getKey() + " to " + dest);
			}
		}
	}
	
	
	@Override
	public String getCommandName() {
		return ShellCommands.MASSRENAME.getName();
	}
	
	private static final String commandDescription =  
			" * This command is used to \"massively\" rename/move files(not directories) that are directly\n" + 
			" * in directory given as first argument. The files will be moved to the directory given as\n" + 
			" * second argument. This command has 4 subcommands, subcommands are given as third argument\n" + 
			" * 		1. filter - filters the files using pattern from the fourth argument and prints their\n" + 
			" * 					names\n" + 
			" * 		2. groups - prints capturing groups for files matching the pattern\n" + 
			" * 		3. show   - this subcommand receives the additional fifth argument - an expression\n" + 
			" * 					that defines transformation of file names. It prints the original file\n" + 
			" * 					names and transformed file names. Expression can characters and\n" + 
			" * 					substitution groups which are in form of \n" + 
			" * 					${ _nonnegative_integer_ , (optional_zero)(optional)_nonnegative_integer_), the \n" + 
			" * 					first number defines the index of a group in the matching pattern which\n" + 
			" * 					will be inserted in the expression, second optional number defines the minimal\n" + 
			" * 					width of that group, if groups length is less than that number, the\n" + 
			" * 					group is padded with spaces or zeros depending on if there is a optional\n" + 
			" * 					zero before the second number and then the group is inserted into the\n" + 
			" * 					expression.\n" + 
			" * 		4. execute	Executes the command.\n" + 
			" * \n" + 
			" * If whitespace character is needed in some argument wrap the argument with quotes.\n" + 
			" * Quotes are then not supported!\n" + 
			" * \n" + 
			" * Examples :\n" + 
			" * massrename DIR1 DIR2 filter slika\\d+-[^.]+\\.jpg\n" + 
			" * massrename DIR1 DIR2 groups slika(\\d+)-([^.]+)\\.jpg\n" + 
			" * massrename DIR1 DIR2 show slika(\\d+)-([^.]+)\\.jpg gradovi-${2}-${1,03}.jpg\n" + 
			" * massrename DIR1 DIR2 execute slika(\\d+)-([^.]+)\\.jpg gradovi-${2}-${1,03}.jpg\n";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}

}

package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * This class changes or prints prompt, morelines or multiline symbol the shell uses.
 * As first arguments command expects : "prompt", "morelines", or "multiline".
 * As an optional second argument command expects a single symbol.
 * If there is no second arguments command prints the symbol whose name was given
 * as first argument. If there is a second arguments symbol whose name was given
 * as first argument is changes to the symbol given as second argument.
 * @author ltomic 
 *
 */
public class SymbolShellCommand implements ShellCommand {
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = Utils.parse_arguments(arguments);
		if (args.length > 2 || args.length < 1) {
			env.writeln("Invalid number of arguments");
			return ShellStatus.CONTINUE;
		}
		
		if (args.length == 2) return setSymbol(env, args);
		
		return getSymbol(env, args[0]);
	}
	
	/**
	 * Helper method that implements the change of symbol and prints the change message.
	 * @param env environment the command is executed in
	 * @param args list of string that should contain two strings - 
	 * 				symbol name and new character
	 *
	 * @return always returns {@link ShellCommand.CONTINUE}
	 */
	private ShellStatus setSymbol(Environment env, String[] args) {
		if (args[1].length() != 1) {
			env.writeln("Expected one symbol, string given");
			return ShellStatus.CONTINUE;
		}
		
		Character newSymbol = args[1].charAt(0);
		Character oldSymbol;
		String symbolName;
		switch(args[0]) {
		case "PROMPT":
			oldSymbol = env.getPromptSymbol();
			env.setPromptSymbol(newSymbol);
			symbolName = "PROMPT";
			break;
		case "MORELINES":
			oldSymbol = env.getMorelinesSymbol();
			env.setMorelinesSymbol(newSymbol);
			symbolName = "MORELINES";
			break;
		case "MULTILINE":
			oldSymbol = env.getMultilineSymbol();
			env.setMultilineSymbol(newSymbol);
			symbolName = "MULTILINE";
			break;
		default: 
			env.writeln("Given string does not describe a parameter symbol");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Symbol for " + symbolName + " changed from '" + 
				oldSymbol + "' to '" + newSymbol + "'");
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Helper method that prints the character that represents the given symbol
	 * @param env environment the command is executed in
	 * @param args symbol to be printed
	 * @return {@link ShellCommand.CONTINUE}
	 */
	private ShellStatus getSymbol(Environment env, String symbol) {
		Character currentSymbol;
		String symbolName;
		
		switch(symbol) {
		case "PROMPT":
			currentSymbol = env.getPromptSymbol();
			symbolName = "PROMPT";
			break;
		case "MORELINES":
			currentSymbol = env.getMorelinesSymbol();
			symbolName = "MORELINES";
			break;
		case "MULTILINE":
			currentSymbol = env.getMultilineSymbol();
			symbolName = "MULTILINE";
			break;
		default: 
			env.writeln("Given string does not describe a parameter symbol");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Symbol for " + symbolName + " is '" + currentSymbol + "'");
		return ShellStatus.CONTINUE;
	} 

	@Override
	public String getCommandName() {
		return ShellCommands.SYMBOL.getName();
	}

	private static String commandDescription = 
			"This class changes or prints prompt, morelines or multiline symbol the shell uses.\n" + 
			"As first arguments command expects : \"prompt\", \"morelines\", or \"multiline\".\n" + 
			"As an optional second argument command expects a single symbol.\n" + 
			"If there is no second arguments command prints the symbol whose name was given\n" + 
			"as first argument. If there is a second arguments symbol whose name was given\n" + 
			"as first argument is changes to the symbol given as second argument.";

	@Override
	public List<String> getCommandDescription() {
		ArrayList<String> desc = new ArrayList<>();
		desc.add(commandDescription);
		return desc;
	}


}

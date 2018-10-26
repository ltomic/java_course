package hr.fer.zemris.java.hw07.shell.commands;

import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellCommand;

/**
 * Enumeration for all commands {@link MyShell} should provide.
 * Each enumeration contains name of command and an instance of
 * a class that implements it.
 * @author ltomic
 *
 */
public enum ShellCommands {

	CAT("cat", new CatShellCommand()),
	CHARSETS("charsets", new CharsetsShellCommand()),
	COPY("copy", new CopyShellCommand()),
	EXIT("exit", new ExitShellCommand()),
	HEXDUMP("hexdump", new HexdumpShellCommand()),
	LS("ls", new LsShellCommand()),
	MKDIR("mkdir", new MkdirShellCommand()),
	SYMBOL("symbol", new SymbolShellCommand()),
	TREE("tree", new TreeShellCommand()),
	HELP("help", new HelpShellCommand()),
	PWD("pwd", new PwdShellCommand()),
	CD("cd", new CdShellCommand()),
	PUSHD("pushd", new PushdShellCommand()),
	POPD("popd", new PopdShellCommand()),
	LISTD("listd", new ListdShellCommand()),
	DROPD("dropd", new DropdShellCommand()),
	RMTREE("rmtree", new RmtreeShellCommand()),
	CPTREE("cptree", new CptreeShellCommand()),
	MASSRENAME("massrename", new MassrenameShellCommand());
	
	private String name;
	private ShellCommand command;
	
	/**
	 * Constructs a {@link ShellCommands} with given name and instance
	 * of a {@link ShellCommand}
	 * @param name name of the command
	 * @param command {@link ShellCommand} instance of the command
	 */
	private ShellCommands(String name, ShellCommand command) {
		this.name = name;
		this.command = command;
	}

	/**
	 * Returns command name
	 * @return command name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns instance of the command
	 * @return instance of the command
	 */
	public ShellCommand getCommand() {
		return command;
	}
	
	
}

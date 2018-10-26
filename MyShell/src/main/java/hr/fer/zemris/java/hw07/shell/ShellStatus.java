package hr.fer.zemris.java.hw07.shell;

/**
 * State of shell commands return to shell. Depending on
 * the state return, shell decides what to do next.
 * @author ltomic
 *
 */
public enum ShellStatus {

	/**
	 * Continue executing.
	 */
	CONTINUE,
	/**
	 * Terminate shell.
	 */
	TERMINATE
}

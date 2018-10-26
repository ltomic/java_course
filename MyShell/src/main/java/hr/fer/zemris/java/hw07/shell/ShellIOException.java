package hr.fer.zemris.java.hw07.shell;

/**
 * Implementation of an exception that {@link Environment} throws when
 * error occurs during reading from input or writing to output
 * @author ltomic
 *
 */

public class ShellIOException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public ShellIOException() {
	}
	
	public ShellIOException(String message) {
		super(message);
	}
	
	public ShellIOException(Throwable cause) {
		super(cause);
	}
	
	public ShellIOException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}

package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Class implements an exception QueryLexer raises if it receives an
 * invalid text.
 * @author ltomic
 *
 */

@SuppressWarnings("javadoc")
public class LexerException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public LexerException() {
		
	}
	
	public LexerException(String message) {
		super(message);
	}
	
	public LexerException(Throwable cause) {
		super(cause);
	}
	
	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}

}

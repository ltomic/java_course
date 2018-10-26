package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Class implements an exception SmartScriptParser raises if it recives an
 * invalid text.
 * @author ltomic
 *
 */
public class SmartScriptParserException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public SmartScriptParserException() {	
	}
	
	public SmartScriptParserException(String message) {
		super(message);
	}
	
	public SmartScriptParserException(Throwable cause) {
		super(cause);
	}
	
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}
	

}

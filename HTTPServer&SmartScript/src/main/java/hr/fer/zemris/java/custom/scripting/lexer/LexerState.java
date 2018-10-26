package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration for lexer states.
 * Lexer states determine how lexer will process the text and which
 * tokens will return.
 * @author ltomic
 *
 */
public enum LexerState {
	/**
	 * Lexer is not in a tag structure
	 */
	BASIC,
	/**
	 * Lexer is in a tag structure
	 */
	TAG
}

package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration for token types. 
 * @author ltomic
 *
 */
public enum TokenType {
	/**
	 * End of file 
	 */
	EOF,
	/**
	 * Any kind of text
	 */
	STRING,
	/**
	 * A variable name - letter followed by letters, digits or underscores 
	 */
	WORD,
	/**
	 * Whole number
	 */
	INTEGER,
	/**
	 * Decimal number
	 */
	DOUBLE,
	/**
	 * Start of a tag structure. "{$"
	 */
	STARTTAG,
	/**
	 * Function call - symbol @ followed by a variable name
	 */
	FUNCTION,
	/**
	 * End of a tag structure. "$}"
	 */
	ENDTAG,
	/**
	 * Any symbol.
	 */
	SYMBOL
}
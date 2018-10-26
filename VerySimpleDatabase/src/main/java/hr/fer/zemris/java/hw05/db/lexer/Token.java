package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * A Token represents a group of characters that have a meaning to a parser.
 * Each token has a type and a value. Token type determines how the value
 * of the token will be processed(or the value of the incoming tokens).
 * @author ltomic
 *
 */
public class Token {
	/**
	 * Type of token this object represents.
	 */
	private TokenType tokenType;
	/**
	 * Value of token this object represents.
	 */
	private String value;
	
	/**
	 * Constructs a token of type <code>type</code> and value <code>value</code>
	 * @param type one of the token types defined in {@link TokenType}
	 * @param value value of the token
	 */
	public Token(TokenType type, String value) {
		Objects.requireNonNull(type, "type cannot be null");
		this.tokenType = type;
		this.value = value;
	}
	
	/**
	 * Getter for property value.
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Getter for propery tokenType.
	 * @return
	 */
	public TokenType getType() {
		return tokenType;
	}
}

package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * This class implements a lexer which tokenizes a query.
 * It is a lazy lexer. Returns tokens defined in {@link TokenType}.
 * @author ltomic
 *
 */

public class QueryLexer {

	/**
	 * Query string stored as array of chars
	 */
	private char data[];
	/**
	 * Last generated token
	 */
	private Token token;
	/**
	 * Index of first unread character
	 */
	private int currentIndex;
	
	/**
	 * Supported symbols
	 */
	public static final char[] SYMBOLS = new char[]{'>', '<', '=', '!'};
	
	/**
	 * Constructs a {@link QueryLexer} with given text
	 * @param text query string
	 */
	public QueryLexer(String text) {
		Objects.requireNonNull(text, "text cannot be null");
		data = text.toCharArray();
	}
	
	/**
	 * Returns the last generated token
	 * @return the last generated token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Generates and returns a new token.
	 * @return returns a new token
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) 
			throw new LexerException("Lexer already reached the EOF");
		
		try {
			return token = parseNextToken();
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage() + " at position " + currentIndex
					+ " in line \n\t " + new String(data) + "\n", ex);
		}
	}
	
	/**
	 * Parses a new token from text.
	 * @return a new token parsed from text
	 */
	public Token parseNextToken() {
		skipWhitespace();
		if (currentIndex >= data.length) return new Token(TokenType.EOF, null);
		
		if (isSymbol())	return new Token(TokenType.SYMBOL, String.valueOf(data[currentIndex++]));
		
		if (data[currentIndex] == '"') {
			currentIndex++;
			String literal = parseLiteral();
			if (currentIndex >= data.length || data[currentIndex] != '"')
				throw new LexerException("Invalid string literal, expected \", current character"
						 + ((currentIndex < data.length) ? "EOF" : data[currentIndex]));
			currentIndex++;
			return new Token(TokenType.STRING, literal);
		}
		
		String text = parseLiteral();
		if (text.length() == 0) throw new LexerException("No matching tokens");
		
		return new Token(TokenType.TEXT, text);
	}
	
	/**
	 * Checks if character at current index is a supported symbol
	 * @return true if character at current index is a supported symbol
	 */
	private boolean isSymbol() {
		for (char c: SYMBOLS) {
			if (c == data[currentIndex]) return true;
		}
		
		return false;
	}
	
	/**
	 * Generates a new string from query text
	 * @return a new string from query text
	 */
	private String parseLiteral() {
		StringBuilder literal = new StringBuilder();
		
		while (currentIndex < data.length) {
			if (!(Character.isDigit(data[currentIndex]) || Character.isLetter(data[currentIndex])
					|| data[currentIndex] == '*'))
				break;
			literal.append(data[currentIndex]);
			currentIndex++;
		}
		
		return literal.toString();
	}

	/**
	 * Moves currentIndex to the next non-whitespace character.
	 */
	private void skipWhitespace() {
		while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) 
			currentIndex++;
	}
}











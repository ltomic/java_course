package hr.fer.zemris.java.custom.scripting.lexer;

import static java.lang.Character.isDigit;

/**
 * This class implements a lexer for SmartScriptParser. It is a "lazy"
 * parser(that means it acquires one token each time a function to get
 * token is called). 
 * @author ltomic
 *
 */

public class SmartScriptLexer {
	
	/**
	 * Text which is parsed stored in a character array.
	 */
	private char[] data;
	/**
	 * Last generated token.
	 */
	private Token token;
	/**
	 * Current position in text of lexer.
	 */
	private int currentIndex;
	/**
	 * Current state of lexer
	 */
	private LexerState state;
	
	/**
	 * Constructs a new lexer with text to be tokenized given as <code>text</code>
	 * Begin state of lexer is <code>BASIC</code>.
	 * @param text text to tokenize
	 */
	public SmartScriptLexer(String text) {
		if (text == null) throw new NullPointerException("text cannot be null.");
		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
	}
	
	/**
	 * Getter for property currentIndex.
	 * @return currentIndex
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	/**
	 * Getter for property state.
	 * @return state
	 */
	public LexerState getState() {
		return state;
	}
	
	/**
	 * Setter for property state
	 * @param state state in which lexer is to be set to
	 */
	public void setState(LexerState state) {
		if (state == null) throw new NullPointerException("state cannot be null.");
		this.state = state;
	}
	
	/**
	 * Returns the nextToken. Method delegates the job to other methods depending
	 * on lexer state. If lexer has processed the whole text it returns EOF
	 * (end-of-file)token. 
	 * @return next token
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Lexer has already reached the end of file");
		}
		if (currentIndex >= data.length) return token = new Token(TokenType.EOF, null);
		
		if (state == LexerState.BASIC) {
			token = getBasicNextToken();
		} else if (state == LexerState.TAG) {
			token = getTagNextToken();
		}
		
		return token;
	}
	
	/**
	 * Returns last generated token.
	 * @return last generated token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Returns the next token when lexer is in BASIC state.
	 * @return next token
	 */
	public Token getBasicNextToken() {
		if (isStartOfTag()) {
			currentIndex += 2;
			return new Token(TokenType.STARTTAG, null);
		}

		StringBuilder text = new StringBuilder();
		for ( ; currentIndex < data.length; currentIndex++) {
			if (isStartOfTag()) break;
			if (data[currentIndex] == '\\') {
				currentIndex++;
				text.append(escapedCharacterInText());
				continue;
			}
			
			text.append(data[currentIndex]);
		}
		
		return new Token(TokenType.STRING, text.toString());		
	}
	
	/**
	 * Returns next token when in TAG state.
	 * @return next token
	 */
	private Token getTagNextToken() {
		skipWhitespace();
		if (data[currentIndex] == '-') {
			if (currentIndex+1 >= data.length || isDigit(data[currentIndex+1]))
				return getTokenNumber();
		}
		
		if (isDigit(data[currentIndex])) return getTokenNumber();
		
		if (Character.isLetter(data[currentIndex])) {
			return new Token(TokenType.WORD, getWord());			
		}
		
		if (isEndOfTag()) {
			currentIndex += 2;
			return new Token(TokenType.ENDTAG, null);
		}
		
		if (data[currentIndex] == '@') {
			currentIndex++;
			return new Token(TokenType.FUNCTION, getWord());
		}
		
		if (data[currentIndex] == '\"') {
			currentIndex++;
			return new Token(TokenType.STRING, getString());
		}
			
		currentIndex++;
		return new Token(TokenType.SYMBOL, data[currentIndex-1]);
	}
	
	/**
	 * Method returns a string of characters between currentIndex and
	 * the next quotation mark("). Usually called when quotation mark is
	 * encountered.
	 * @return string of characters between currentIndex and the next 
	 * 		   quotation mark(").
	 */
	private String getString() {		
		StringBuilder string = new StringBuilder();
		
		for ( ; currentIndex < data.length; currentIndex++) {
			if (data[currentIndex] == '\"') break;
			if (data[currentIndex] == '\\') {
				currentIndex++;
				string.append(escapedCharacterInString());
				continue;
			}
			
			string.append(data[currentIndex]);
		}
		
		if (currentIndex == data.length) 
			throw new LexerException("Expected end of string at " + currentIndex);
		
		currentIndex++;
		
		return string.toString();
	}
	
	/**
	 * Returns an escaped character in text(outside tag). Usually called 
	 * when character for escaping('\') is encountered. Escaped characters
	 * supported : '\', '{'
	 * @return string equal to an escaped character
	 */
	private String escapedCharacterInText() {
		if (currentIndex >= data.length) 
			throw new LexerException("Invalid escaping at position " + currentIndex);

		char current = data[currentIndex];
		
		if (current == '\\') return "\\";
		if (current == '{') return "{";
		
		throw new LexerException("Invalid escaping at position " + currentIndex);
	}
	
	/**
	 * Returns string equal to an escaped character in tag. Usually called 
	 * when character for escaping('\') is encountered. Escaped characters
	 * supported : '\', '{', '\n', '\r', '\t',  
	 * @return string equal to an escaped character
	 */
	private String escapedCharacterInString() {
		if (currentIndex >= data.length) 
			throw new LexerException("Invalid escaping at position " + currentIndex);
		
		char current = data[currentIndex];
		
		if (current == 'n') return "\n";
		if (current == 'r') return "\r";
		if (current == 't') return "\t";
		if (current == '\"') return "\"";
		if (current == '\\') return "\\";
		
		throw new LexerException("Invalid escaping at position " + currentIndex);
	}
	
	/**
	 * Returns a string equal to a processed word(function or variable name).
	 * @return string equal to a processed word(function or variable name)
	 */
	private String getWord() {
		if (currentIndex >= data.length || Character.isWhitespace(data[currentIndex]))
			throw new LexerException("Expected beginning of variable at " + currentIndex);
		
		StringBuilder word = new StringBuilder();
		
		for ( ; currentIndex < data.length; currentIndex++) {
			char current = data[currentIndex];
			if (!Character.isLetter(current) && !isDigit(current) && current != '_') break;
			word.append(current);
		}
		
		return word.toString();
	}
	
	/**
	 * Returns a token for integer or double depending on type of number processed.
	 * If a number has a decimal point and a digit after it will be considered as double.
	 * @return token for integer or double depending on type of number processed
	 */
	private Token getTokenNumber() {
		boolean negative = false;
		boolean hasDecimalPoint = false;
		
		if (data[currentIndex] == '-') {
			negative = true;
			currentIndex++;
		}
		
		int startIndex = currentIndex;
		while (currentIndex < data.length && (isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
			if (data[currentIndex] == '.' && currentIndex+1 < data.length &&
					isDigit(data[currentIndex+1])) 
				hasDecimalPoint = true;
			currentIndex++;
		}
	
		
		if (data[startIndex] == '0' && startIndex+1 < currentIndex && isDigit(data[startIndex+1]))
			throw new LexerException("Format of a number invalid at position " + startIndex);
		
		double number;
		try {
			number = Double.parseDouble(new String(data, startIndex, currentIndex-startIndex));
		} catch (NumberFormatException ex) {
			throw new LexerException("Format of a number invalid at position " + startIndex);
		}
		if (negative) number *= -1;
		
		if (hasDecimalPoint == false && Math.floor(number) == number)
			return new Token(TokenType.INTEGER, Integer.valueOf((int)number));
		
		return new Token(TokenType.DOUBLE, Double.valueOf(number));
	}
	
	/**
	 * Checks if lexer is currently on start of a tag("{$")
	 * @return <code>true</code> if lexer is currently on start of a tag
	 */
	private boolean isStartOfTag() {
		return currentIndex+1 < data.length && data[currentIndex] == '{' &&
				data[currentIndex+1] == '$';
	}
	
	/**
	 * Checks if lexer is currently on end of a tag("$}")
	 * @return <code>true</code> if lexer is currently on end of a tag
	 */
	private boolean isEndOfTag() {
		return currentIndex+1 < data.length && data[currentIndex] == '$' &&
				data[currentIndex+1] == '}';
	}
	
	/**
	 * Increments currentIndex until character that is not whitespace is encountered.
	 */
	private void skipWhitespace() {
		while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) 
			currentIndex++;
	}
}

package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.ArrayList;

/**
 * Parses the given expression which describes how the file name should be transformed
 * and generates {@link NameBuilder}s based on that expression.
 * @author ltomic
 *
 */
public class NameBuilderParser {
	
	/**
	 * The main {@link NameBuilder} which executes all others.
	 */
	private NameBuilderHead head;
	ArrayList<NameBuilder> builders;
	
	/**
	 * Constructs the {@link NameBuilderParser} with provided expression
	 * @param expr expression that describes how the file name should be transformed
	 */
	public NameBuilderParser(String expr) {
		builders = new ArrayList<>();
		parse(expr);
	}
	
	/**
	 * Returns the main generated {@link NameBuilder}
	 * @return
	 */
	public NameBuilder getNameBuilder() {
		return head;
	}
	
	/**
	 * Index of the next unread character from the expression
	 */
	private int ind;
	/**
	 * Expression as array of chars
	 */
	private char[] data;
	/**
	 * Length of expression
	 */
	private int len;
	
	/**
	 * Method used to parse expression and generate {@link NameBuilder}s
	 * @param expr expression to parse
	 */
	private void parse(String expr) {
		data = expr.toCharArray();
		len = data.length;

		for (ind = 0; ind < len; ++ind) {
			String text = parseText();
			if (text.length() != 0) {
				builders.add(new NameBuilderText(text));
			}
			
			if (ind >= len) break;

			ind += 2;
			parseGroupSub();
			
		}
		
		head = new NameBuilderHead(builders.toArray(new NameBuilder[0]));
	}
	
	/**
	 * Returns the next sequence of characters that are not substituting group
	 * @return the next sequence of characters that are not substituting group
	 */
	private String parseText() {
		StringBuilder text = new StringBuilder();
		
		while ((ind < len-1 && (data[ind] != '$' || data[ind+1] != '{')) || ind == len-1) {
			text.append(data[ind]);
			ind++;
		}
		
		return text.toString();
	}
	
	/**
	 * Parses the substituting group and generates the appropriate {@link NameBuilderGroup}
	 * for that group.
	 */
	private void parseGroupSub() {
		skipWhitespace();
		
		int index = parsePositiveInteger();
		if (data[ind] != '}' && data[ind] != ',') {
			throw new IllegalArgumentException("Expected end of group substition or ','");
		}
		skipWhitespace();
		
		int width = 0;
		boolean zero = false;
		if (data[ind] == ',') {
			ind++;
			skipWhitespace();
			if (data[ind] == '0') {
				zero = true;
				ind++;
			}
			width = parsePositiveInteger();
		}
		
		skipWhitespace();
		if (data[ind] != '}') {
			throw new IllegalArgumentException("Expected end of group substition");
		}
		
		builders.add(new NameBuilderGroup(index, width, zero));
	}
	
	/**
	 * Parses positive integer from the expression
	 * @return positive integer that was parsed
	 */
	private int parsePositiveInteger() {
		if (ind >= len || !Character.isDigit(data[ind])) {
			throw new IllegalArgumentException("Expected positive number at index " + ind);
		}
		
		int result = 0;
		while (ind < len && Character.isDigit(data[ind])) {
			result = result * 10 + data[ind] - '0';
			ind++;
		}
		
		return result;
	}
	
	/**
	 * Skips whitespace in the expression
	 */
	private void skipWhitespace() {
		int len = data.length;
		while (ind < len && Character.isWhitespace(data[ind])) ind++;
	}
}








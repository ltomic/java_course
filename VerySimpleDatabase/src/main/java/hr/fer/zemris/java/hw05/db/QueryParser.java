package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

/**
 * Implements a parser that parses queries. Query consists of conditional expressions
 * with "and" operator between them. Example :
 * 		jmbag="0000000005" and firstName="Ratko" 
 * @author ltomic
 *
 */

public class QueryParser {
	
	/**
	 * Defines the AND operator
	 */
	private static final String AND = "and";

	/**
	 * List in which parsed conditional expression are stored.
	 */
	ArrayList<ConditionalExpression> expressions;
	
	/**
	 * Lexer that generates tokens from the query text.
	 */
	private QueryLexer lexer;
	
	/**
	 * Constructs a {@link QueryParser} with given query text and parses the query
	 * to a list of conditional expressions.
	 * @param query query to be parsed
	 */
	public QueryParser(String query) {
		this.lexer = new QueryLexer(query);
		expressions = new ArrayList<ConditionalExpression>();
		parse();
	}
	
	/**
	 * Parses and generates a list of conditional expressions using lexer.
	 */
	private void parse() {
		while (true) {
			IFieldValueGetter getter = generateAttribute(lexer.nextToken());
			
			IComparisonOperator operator = parseOperator(lexer.nextToken());
			
			String literal = parseStringLiteral(lexer.getToken());
			
			expressions.add(new ConditionalExpression(getter, literal, operator));
			
			if (checkIfEndOfQuery(lexer.nextToken())) break;
		}
		if (expressions.size() < 1) 
			throw new IllegalArgumentException("Expected at least one condition");
	}
	
	/**
	 * Tests if a parsed query is a direct query - a query that consists of only one
	 * conditional expression and expressions attribute is jmbag and expressions 
	 * operator is EQUALS
	 * @return true if a query consists of only one conditional expression and 
	 * 			expressions attribute is jmbag and expressions operator is EQUALS
	 */
	public boolean isDirectQuery() {
		if (expressions.size() != 1) return false;
		ConditionalExpression expression = expressions.get(0);
		
		if (!expression.getFieldGetter().equals(FieldValueGetters.JMBAG)) return false;
		if (!expression.getComparisonOperator().equals(ComparisonOperators.EQUALS)) return false;
		
		return true;
	}
	
	/**
	 * If query is a direct query this method returns the jmbag literal of the only
	 * conditional expression in query.
	 * @return the jmbag literal of the only conditional expression in query.
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) throw new IllegalStateException("This is not a direct query");
		
		return expressions.get(0).getStringLiteral();
	}
		
	/**
	 * Returns the list of conditional expressions parsed from query.
	 * @return the list of conditional expressions parsed from query
	 */
	public List<ConditionalExpression> getQuery() {
		return expressions;
	}
	
	/**
	 * Generates the attribute from the given token
	 * @param token the token from which the attribute should be generated
	 * @return the attribute from the given token
	 */
	private IFieldValueGetter generateAttribute(Token token) {
		if (token.getType() != TokenType.TEXT) 
			throw new IllegalArgumentException("Expected attribute, given " + token.getValue());
		String text = token.getValue();
		
		for (AttributeType attribute : AttributeType.values()) {
			if (attribute.name.equals(text)) return attribute.getter;
		}
		
		throw new IllegalArgumentException("Expected attribute, given " + text);
	}
	
	/**
	 * Parses the operator from the given token
	 * @param token the token from which the operator should be parsed
	 * @return the operator from the given token
	 */
	private IComparisonOperator parseOperator(Token token) {		
		if (token.getType() == TokenType.SYMBOL) {
			StringBuilder operatorString = new StringBuilder(token.getValue());
			token = lexer.nextToken();
			if (token.getType() == TokenType.SYMBOL) {
				operatorString.append(token.getValue());
			}
			return generateOperator(operatorString.toString());
		}
			
		if (token.getType() == TokenType.TEXT) {
			String text = token.getValue();
			lexer.nextToken();
			return generateOperator(text);
		}
		
		throw new IllegalArgumentException("Expected operator");
	}
	
	/**
	 * Generates the operator from the given token
	 * @param text the text from which the operator should be generated
	 * @return the operator from the given text
	 */
	private IComparisonOperator generateOperator(String text) {
		for (ComparisonOperatorType operator : ComparisonOperatorType.values()) {
			if (operator.name.equals(text)) return operator.operator;
		}
		
		throw new IllegalArgumentException("Expected operator, given " + text);
	}
	
	/**
	 * Parses a string literal from the given token
	 * @param token the token from which the literal should be parsed
	 * @return string literal from the given token
	 */
	private String parseStringLiteral(Token token) {
		if (token.getType() != TokenType.STRING) 
			throw new IllegalArgumentException("Expected string literal, given " + token.getValue());
		
		return token.getValue();
	}
	
	/**
	 * Returns true if the given token is EOF token. False if the given token is
	 * AND token. Throws exception if token is neither of the two types.
	 * @param token token to be tested 
	 * @return true if the given token is EOF token. False if the given token is AND token
	 */
	private boolean checkIfEndOfQuery(Token token) {
		if (token.getType() == TokenType.EOF) return true;
		if (token.getType() == TokenType.TEXT && AND.equals(token.getValue().toLowerCase())) return false;
		
		throw new IllegalArgumentException("Expected EOF or AND, given \"" + token.getValue() + "\"");
	}
	
}

package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * SmartScriptParser implements a parser for specific types of documents(described
 * in homework pdf). The parser produces a tree which represents the document
 * structure with nodes being constructs of the document. Parser uses SmartScriptLexer
 * as a lexer and works with Token class. Root of the tree is always a DocumentNode
 * @author ltomic
 */
public class SmartScriptParser {
	
	/**
	 * root of the tree representing the document
	 */
	private Node head;
	/**
	 * lexer instance the parser uses
	 */
	private SmartScriptLexer lexer;
	/**
	 * Allowed array of operators in tag
	 */
	private ArrayList<Object> operators;
	
	/** For tag name **/
	public static final String FOR_TAG_NAME = "for";
	/** End tag name **/
	public static final String END_TAG_NAME = "end"; 

	/**
	 * Constructs a SmartScriptParser with <code>body</code> as a document to process
	 * and initializes the <code>operators</code>
	 * @param body document to process
	 * @throws SmartScriptParserException if invalid string document is given
	 */
	public SmartScriptParser(String body) {
		operators = new ArrayList<>();
		addOperators();
		
		this.lexer = new SmartScriptLexer(body);
		
		try {
			parse();
		} catch (LexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		} catch (EmptyStackException ex) {
			throw new SmartScriptParserException("Invalid closing of non-empty tags");
		}
	}
	
	/**
	 * Getter for property head
	 * @return
	 */
	public Node getHead() {
		return head;
	}
	
	/**
	 * Return root of the tree as <code>DocumentNode</code>
	 * @return <code>DocumentNode</code> head
	 */
	public DocumentNode getDocumentNode() {
		return (DocumentNode)head;
	}
	
	/**
	 * Adds +, -, *, /, ^ to allowed operators
	 */
	private void addOperators() {
		operators.add(Character.valueOf('+'));
		operators.add(Character.valueOf('-'));
		operators.add(Character.valueOf('*'));
		operators.add(Character.valueOf('/'));
		operators.add(Character.valueOf('^'));
	}
	
	/**
	 * Methods that does the processing of the document. It checks if
	 * all document constructs are valid and if enclosing of non-empty
	 * tags correct. This method is only called once to parse the document.
	 */
	private void parse() {
		head = new DocumentNode();
		Stack<Object> stack = new Stack<>();
		stack.push(head);
		
		Node front = (Node)stack.peek();
		while (true) {
			Token token = lexer.nextToken();
			
			if (token.getType() == TokenType.EOF) break;
			
			if (token.getType() == TokenType.STRING) {
				front.addChildNode(new TextNode((String)token.getValue()));
				continue;
			}
			
			if (token.getType() == TokenType.STARTTAG) {
				lexer.setState(LexerState.TAG);
				token = lexer.nextToken();
				
				if (token.getType() == TokenType.WORD) {
					String name = ((String)token.getValue()).toLowerCase();
					
					if (name.equals(END_TAG_NAME)) {
						token = lexer.nextToken();
						if (token.getType() != TokenType.ENDTAG)
							throw new SmartScriptParserException("Invalid tag at "
									+ lexer.getCurrentIndex());
						stack.pop();
						front = (Node)stack.peek();
					} else if (name.equals(FOR_TAG_NAME)) {
						stack.push(createForTag());
						front.addChildNode((Node)stack.peek());
						front = (Node)stack.peek();
					} else {
						front.addChildNode(createEchoTag(token));
					}
				} else if (token.getType() == TokenType.SYMBOL && token.getValue().equals('=')) {
					front.addChildNode(createEchoTag(token));
				} else {
					throw new SmartScriptParserException("Invalid tag at " + lexer.getCurrentIndex());
				}
				
				lexer.setState(LexerState.BASIC);
			}
		}
	
		if (stack.size() != 1) throw new SmartScriptParserException("Invalid enclosing of tags");
	}
	
	/**
	 * Creates and returns EchoNode that is processed. Called when tag is encountered.
	 * @param token the current token
	 * @return EchoNode representing the tag from the documents that was processed
	 */
	private EchoNode createEchoTag(Token token) {
		ArrayList<Object> elements = new ArrayList<>();
		
		while (true) {
			token = lexer.nextToken();
			if (token.getType() == TokenType.ENDTAG) break;
			elements.add(getElement(token));
		}

		Element[] returnElements = Arrays.copyOf(elements.toArray(), elements.size(), Element[].class);

		return new EchoNode(returnElements);
	}
	
	/**
	 * Creates and returns EchoNode that is processed. Called when for loop is encountered.
	 * @return ForLoopNode representing the for loop from the documents that was processed
	 */
	private ForLoopNode createForTag() {
		ElementVariable Arg1 = getVariable(lexer.nextToken());
		Element Arg2 = getForArgument(lexer.nextToken());
		Element Arg3 = getForArgument(lexer.nextToken());
		
		Element Arg4 = null;
		Token token = lexer.nextToken();
		if (token.getType() != TokenType.ENDTAG) { 
			Arg4 = getForArgument(token);
			token = lexer.nextToken();
		}
		
		if (token.getType() != TokenType.ENDTAG)
			throw new SmartScriptParserException("Expected end tag symbol at " +
					lexer.getCurrentIndex());
		
		return new ForLoopNode(Arg1, Arg2, Arg3, Arg4);
	}
	
	/**
	 * Creates and return ElementVariable that is processed. Called when expecting a variable.
	 * @param token current token
	 * @return element representing the variable that was processed
	 */
	private ElementVariable getVariable(Token token) {
		Element element = getElement(token);
		
		if (element instanceof ElementVariable == true) return (ElementVariable)element;
		throw new SmartScriptParserException("First argument of for-tag must be a" + 
				" variable, at " + lexer.getCurrentIndex());
	}
	
	/**
	 * Checks if the given token is allowed as for loop argument. If it is, returns
	 * the element created from the token, else throws an exception
	 * @param token token to be check if allowed as for loop argument
	 * @return element created from the token 
	 */
	private Element getForArgument(Token token) {
		Element element = getElement(token);
		
		if (element instanceof ElementVariable || element instanceof ElementConstantInteger
				|| element instanceof ElementConstantDouble
				|| element instanceof ElementString
			) return element;
		
		throw new SmartScriptParserException("First argument of for-tag must be" + 
				" variable, number or string, at " + lexer.getCurrentIndex());
	}
	
	/**
	 * Returns the element created from the given token. If token doesn't match
	 * any of the defined elements exception is thrown
	 * @param token token given for creating an element
	 * @return element created from the given token
	 */
	private Element getElement(Token token) {
		if (token.getType() == TokenType.WORD) 
			return new ElementVariable((String)token.getValue());
		if (token.getType() == TokenType.STRING) 
			return new ElementString((String)token.getValue());
		if (token.getType() == TokenType.DOUBLE)
			return new ElementConstantDouble((double)token.getValue());
		if (token.getType() == TokenType.FUNCTION)
			return new ElementFunction((String)token.getValue());
		if (token.getType() == TokenType.INTEGER)
			return new ElementConstantInteger((int)token.getValue());
		if (token.getType() == TokenType.SYMBOL && operators.contains(token.getValue()))
			return new ElementOperator(String.valueOf(token.getValue()));
		
		throw new SmartScriptParserException("Expected token which is element, at "
			+ lexer.getCurrentIndex());
	}
}








package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.elems.IElementVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Executes the SmartScript file given as DocumentNode, putting information in provided
 * {@link RequestContext}.
 * @author ltomic
 *
 */
public class SmartScriptEngine {
	
	/** Sine function SmartScript name **/
	private static final String SIN_FUNCTION = "sin";
	/** Decimal format function SmartScript name **/
	private static final String DECFMT_FUNCTION = "decfmt";
	/** Duplicate variable function SmartScript name **/
	private static final String DUP_FUNCTION = "dup";
	/** Swap function SmartScript name **/
	private static final String SWAP_FUNCTION = "swap";
	/** Set mime type function SmartScript name **/
	private static final String SET_MIME_TYPE_FUNCTION = "setMimeType";
	/** Get parameter function SmartScript name **/
	private static final String PARAM_GET_FUNCTION = "paramGet";
	/** Get persistent parameter function SmartScript name **/
	private static final String PERSISTENT_PARAMETER_GET_FUNCTION = "pparamGet";
	/** Set persistent parameter function SmartScript name **/
	private static final String PERSISTENT_PARAMETER_SET_FUNCTION = "pparamSet";
	/** Delete persistent parameter function SmartScript name **/
	private static final String PERSISTENT_PARAMETER_DELETE_FUNCTION = "pparamDel";
	/** Get temporary parameter function SmartScript name **/
	private static final String TEMPORARY_PARAMETER_GET_FUNCTION = "tparamGet";
	/** Set temporary parameter function SmartScript name **/
	private static final String TEMPORARY_PARAMETER_SET_FUNCTION = "tparamSet";
	/** Delete temporary parameter function SmartScript name **/
	private static final String TEMPORARY_PARAMETER_DELETE_FUNCTION = "tparamDel";

	/** SmartScript document head node **/
	private DocumentNode documentNode;
	/** Context used **/
	private RequestContext requestContext;
	/** Stack keeping track of variable values **/
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * Constructs {@link SmartScriptEngine} with provided arguments
	 * @param documentNode - head node of the SmartScript document to execute
	 * @param requestContext - context to use
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * {@link INodeVisitor} that executes nodes of the document
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText() );
			} catch (IOException ex) {
				System.err.println("Could not write to context");
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			ElementVariable variable = node.getVariable();
			multistack.push(variable.getName(), new ValueWrapper(node.getStartExpression().toString()));

			while (multistack.peek(variable.getName()).numCompare(node.getEndExpression().toString()) != 1) {
				for (int i = 0, sz = node.numberOfChildren(); i < sz; ++i) {
					node.getChild(i).accept(this);
				}
				multistack.peek(variable.getName()).add(node.getStepExpression().toString());
			}

			multistack.pop(variable.getName());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> tempStack = new Stack<>();
			IElementVisitor elementVisitor = new IElementVisitor() {

				@Override
				public void visitElementVariable(ElementVariable element) {
					tempStack.push(multistack.peek(element.getName()).getValue());
				}

				@Override
				public void visitElementString(ElementString element) {
					tempStack.push(element.getValue());
				}

				@Override
				public void visitElementOperator(ElementOperator element) {
					Object first = tempStack.pop();
					Object second = tempStack.pop();

					ValueWrapper value = new ValueWrapper(first.toString());

					switch (element.getSymbol()) {
					case "+":
						value.add(second.toString());
						break;
					case "-":
						value.sub(second.toString());
						break;
					case "*":
						value.mul(second.toString());
						break;
					case "/":
						value.div(second.toString());
						break;
					default:
						System.err.println("No matching operator");
					}
					
					tempStack.push(value);
				}

				@Override
				public void visitElementFunction(ElementFunction element) {
					switch (element.getName()) {
					case SIN_FUNCTION:
						double doubleValue = ((Number)((ValueWrapper) tempStack.pop()).getValue()).doubleValue();
						tempStack.push(Math.sin(doubleValue));
						break;
					case DECFMT_FUNCTION:
						NumberFormat formatter = new DecimalFormat((String) tempStack.pop());
						tempStack.push(formatter.format(tempStack.pop()));
						break;
					case DUP_FUNCTION:
						tempStack.push(tempStack.peek());
						break;
					case SWAP_FUNCTION:
						Object first = tempStack.pop();
						Object second = tempStack.pop();
						tempStack.push(first);
						tempStack.push(second);
						break;
					case SET_MIME_TYPE_FUNCTION:
						requestContext.setMimeType((String) tempStack.pop());
						break;
					case PARAM_GET_FUNCTION:
						Object defaultValue = tempStack.pop();
						Object value = requestContext.getParameter((String) tempStack.pop());
						tempStack.push(value == null ? defaultValue : value);
						break;
					case PERSISTENT_PARAMETER_GET_FUNCTION:
						defaultValue = tempStack.pop();
						value = requestContext.getPersistentParameter((String) tempStack.pop());
						tempStack.push(value == null ? defaultValue : value);
						break;
					case PERSISTENT_PARAMETER_SET_FUNCTION:
						String name = (String) tempStack.pop();
						requestContext.setPersistentParameter(name, tempStack.pop().toString());
						break;
					case PERSISTENT_PARAMETER_DELETE_FUNCTION:
						requestContext.removePersistentParameter((String) tempStack.pop());
						break;
					case TEMPORARY_PARAMETER_GET_FUNCTION:
						defaultValue = tempStack.pop();
						value = requestContext.getTemporaryParameter((String) tempStack.pop());
						tempStack.push(value == null ? defaultValue : value);
						break;
					case TEMPORARY_PARAMETER_SET_FUNCTION:
						name = (String) tempStack.pop();
						requestContext.setTemporaryParameter(name, tempStack.pop().toString());
						break;
					case TEMPORARY_PARAMETER_DELETE_FUNCTION:
						requestContext.removeTemporaryParameter((String) tempStack.pop());
						break;
					}
				}

				@Override
				public void visitElementConstantInteger(ElementConstantInteger element) {
					tempStack.push(element.getValue());
				}

				@Override
				public void visitElementConstantDouble(ElementConstantDouble element) {
					tempStack.push(element.getValue());
				}
			};

			for (Element i : node.getElements()) {
				i.accept(elementVisitor);
			}
			
			ArrayList<Object> toWrite = new ArrayList<Object>(tempStack);
			
			toWrite.forEach((value) -> {
				try {
					requestContext.write(value.toString());
				} catch (IOException ex) {
					System.err.println("Could not write to context");
				}
			});
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, sz = node.numberOfChildren(); i < sz; ++i) {
				node.getChild(i).accept(this);
			}
		}

	};

	/**
	 * Initiates script execution
	 */
	public void execute() {
		documentNode.accept(visitor);
		System.out.println();
	}
}
package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Implements Visitor pattern for the {@link Node} class.
 * @author ltomic
 *
 */
public interface INodeVisitor {

	/**
	 * Visits {@link TextNode}
	 * @param node - node to visit
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Visits {@link ForLoopNode}
	 * @param node - node to visit
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Visits {@link EchoNode}
	 * @param node - node to visit
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Visits {@link DocumentNode
	 * @param node - node to visit
	 */
	public void visitDocumentNode(DocumentNode node);

}

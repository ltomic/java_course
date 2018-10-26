package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * ForLoopNode represents a single for loop construct. A for loop is a tag
 * with name FOR and 3 or 4 arguments {@see #variable #startExpression #endExpression
 * #stepExpression}. Children of a ForLoopNode represent parts of documented iterated
 * through every iteration of for loop. For loop ends with a tag whose name is END.
 * @author ltomic
 *
 */
public class ForLoopNode extends Node {

	/**
	 * A loop variable
	 */
	private ElementVariable variable;
	/**
	 * Expression to which loop variable is assigned
	 */
	private Element startExpression;
	/**
	 * Upon loop variable reaching this expression for loop terminates 
	 */
	private Element endExpression;
	/**
	 * Expression which is added to loop variable, optional
	 */
	private Element stepExpression;
	
	/**
	 * Constructs a ForLoopNode with appropriate variables.
	 * @param variable
	 * @param startExpression
	 * @param endExpression
	 * @param stepExpression
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable = Objects.requireNonNull(variable);
		this.startExpression = Objects.requireNonNull(startExpression);
		this.endExpression = Objects.requireNonNull(endExpression);
		this.stepExpression = stepExpression;
	}

	/**
	 * Getter for property variable.
	 * @return variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Getter for property startExpression.
	 * @return startExpression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Getter for property endExpression
	 * @return endExpression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Getter for property stepExpression
	 * @return stepExpression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * @return <code>true</code> If all arguments and children <code>other</code>
	 * 							 are equal to arguments and children of this node.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ForLoopNode)) return false;
		ForLoopNode otherFor = (ForLoopNode)other;
		
		if (this.variable.equals(otherFor.getVariable()) == false) return false;
		if (this.startExpression.equals(otherFor.getStartExpression()) == false) return false;
		if (this.endExpression.equals(otherFor.getEndExpression()) == false) return false;
		
		if (this.stepExpression != null && 
				this.stepExpression.equals(otherFor.getStepExpression()) == false) {
			return false;
		}
		
		int size = this.numberOfChildren();
		if (size != otherFor.numberOfChildren()) return false;
		
		for (int i = 0; i < size; ++i) {
			if (this.getChild(i).equals(otherFor.getChild(i)) == false) return false;
		}
		
		return true;
	}
	
	/**
	 * Returns a text representation of a node.
	 * Returns only that this node is a ForLoopNode and a type of class of a 
	 * <code>variable</code>
	 * @return "ForLoopNode: " + type of class of a <code>variable</code>
	 */
	@Override
	public String toString() {
		return "ForLoopNode: " + variable.getClass();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
	
	
}

package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * TextNode represents a piece of textual data in document. 
 * @author ltomic
 *
 */

public class TextNode extends Node {

	/**
	 * Text node is representing.
	 */
	private String text;
	
	/**
	 * Constructs a TextNode with <code>text</code> as argument
	 * @param text
	 */
	public TextNode(String text) {
		this.text = Objects.requireNonNull(text);
	}

	/**
	 * Getter for property text.
	 * @return text
	 */
	public String getText() {
		return text.replace("\\", "\\\\").replace("{$", "\\{$");
	}
	
	/**
	 * @return <code>true</code> if their texts are equal
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TextNode)) return false;
		TextNode otherNode = (TextNode)other;
		
		return this.text.equals(otherNode.getText());
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}

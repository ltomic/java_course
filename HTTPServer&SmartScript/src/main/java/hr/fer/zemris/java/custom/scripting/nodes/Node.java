package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * A Node represents a structural part of a document(e.g. continuos text, tag)
 * A node can have children that represent what is inside the structure of node.
 * One document can be represented as tree of nodes.
 * Class Node is generally a template for more specific classes of Nodes.
 * @author ltomic
 *
 */
public abstract class Node {

	/**
	 * Children of a node are stored in <code>storage</code>
	 */
	private List<Object> storage;
	
	/**
	 * Adds a Node <code>child</code> as a child to this node.
	 * @param child Node to be added
	 */
	public void addChildNode(Node child) {
		if (storage == null) {
			storage = new ArrayList<>();
		}
		
		storage.add(child);
	}
	
	/**
	 * Returns the number of children of this node
	 * @return the number of children of this node
	 */
	public int numberOfChildren() {
		if (storage == null) {
			return 0;
		}
		return storage.size();
	}
	
	/**
	 * Returns a child added as index-th (counting from 0)
	 * @param index number of the child to be returned
	 * @return a child added as index-th (counting from 0)
	 */
	public Node getChild(int index) {
		return (Node)(storage.get(index));
	}
	
	/**
	 * Compares this object with <code>other</code>object.
	 * Equality of two nodes is described in return doc
	 * @return <code>true</code>If <code>other</code> is a Node and their
	 * 			children are equal. 
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Node)) return false;
		
		Node otherNode = (Node)other;

		int size = this.numberOfChildren();
		if (size != otherNode.numberOfChildren()) return false;

		for (int i = 0; i < size; ++i) {
			if ((this.getChild(i)).equals(otherNode.getChild(i)) == false) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Debug print.
	 */
	public void print() {
		System.out.println("Pocetak ||| " + getClass());
		if (storage != null) {
			int size = storage.size();
			System.out.println(size);
			System.out.flush();
			for (int i = 0; i < size; ++i) {
				((Node)storage.get(i)).print();
			}
		}
		System.out.println("Kraj");
	}
	
	/**
	 * Accepts {@link INodeVisitor} from the Visitor pattern for this class.
	 * @param visitor - visitor to accept
	 */
	public abstract void accept(INodeVisitor visitor);
}

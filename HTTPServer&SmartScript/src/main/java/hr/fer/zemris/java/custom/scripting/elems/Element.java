package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This class represents expressions. Every class which extends
 * this class is one type of an expresssion. This class is a template class.
 * @author ltomic
 *
 */
public abstract class Element {
	
	/**
	 * Method accepting {@link IElementVisitor} and calling it on itself.
	 * @param visitor - visitor to call
	 */
	public abstract void accept(IElementVisitor visitor);

}

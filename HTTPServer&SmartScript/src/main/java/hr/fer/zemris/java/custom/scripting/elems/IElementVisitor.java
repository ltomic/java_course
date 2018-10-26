package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class implementing Visitor pattern for Element class.
 * @author ltomic
 *
 */
public interface IElementVisitor {

	/**
	 * Visits {@link ElementConstantDouble}
	 * @param element - element to visit
	 */
	public void visitElementConstantDouble(ElementConstantDouble element);
	/**
	 * Visits {@link ElementConstantInteger}
	 * @param element - element to visit
	 */
	public void visitElementConstantInteger(ElementConstantInteger element);
	/**
	 * Visits {@link ElementFunction}
	 * @param element - element to visit
	 */
	public void visitElementFunction(ElementFunction element);
	/**
	 * Visits {@link ElementOperator}
	 * @param element - element to visit
	 */
	public void visitElementOperator(ElementOperator element);
	/**
	 * Visits {@link ElementString}
	 * @param element - element to visit
	 */
	public void visitElementString(ElementString element);
	/**
	 * Visits {@link ElementVariable}
	 * @param element - element to visit
	 */
	public void visitElementVariable(ElementVariable element);
	
}

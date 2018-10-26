package hr.fer.zemris.java.hw11.jnotepadpp.document;

/**
 * Listener of the {@link MultipleDocumentModel}. Observes new document addition, document removal
 * and current document updates.
 * @author ltomic
 *
 */
public interface MultipleDocumentListener {

	/**
	 * Called upon current document update
	 * @param previousModel previous current document
	 * @param currentModel new current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	/**
	 * Called upon an addition of a new document
	 * @param model - new document added
	 */
	void documentAdded(SingleDocumentModel model);
	/** 
	 * Called upon a removal of a document
	 * @param model - document removed
	 */
	void documentRemoved(SingleDocumentModel model);
}
package hr.fer.zemris.java.hw11.jnotepadpp.document;

/**
 * Observer of the {@link SingleDocumentModel}. Observers modification status update and
 * file path update.
 * @author ltomic
 *
 */
public interface SingleDocumentListener {
	
	/**
	 * Called upon modification status update.
	 * @param model - document whose modification status has been updated
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	/**
	 * Called upon file path update.
	 * @param model - document whose file path has been updated
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
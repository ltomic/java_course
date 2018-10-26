package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.nio.file.Path;

/**
 * Represents a model capable of holding multiple objects and having concept of a current 
 * document - the one which is shown to the user and on which user works.
 * @author ltomic
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

	/**
	 * Creates new document.
	 * @return new {@link SingleDocumentModel}.
	 */
	SingleDocumentModel createNewDocument();
	/**
	 * Gets current document.
	 * @return current document
	 */
	SingleDocumentModel getCurrentDocument();
	/**
	 * Loads the document from the given path.
	 * @param path - path from which the document should be loaded from
	 * @return document loaded from the provided path
	 */
	SingleDocumentModel loadDocument(Path path);
	/**
	 * Saves the provided document to the given path
	 * @param model document to save 
	 * @param newPath path to save to
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	/**
	 * Closes the document.
	 * @param model - document to be closed
	 */
	void closeDocument(SingleDocumentModel model);
	/**
	 * Adds a provided {@link MultipleDocumentListener}.
	 * @param l - listener to be added
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	/**
	 * Removes the provided {@link MultipleDocumentListener} if added before.
	 * @param l - listener to be removed
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	/**
	 * Returns number of opened documents.
	 * @return number of opened documents
	 */
	int getNumberOfDocuments();
	/**
	 * Returns the document stored under the provided index
	 * @param index - index from which the document should be retrieved from
	 * @return document stored under the given index
	 */
	SingleDocumentModel getDocument(int index);
}
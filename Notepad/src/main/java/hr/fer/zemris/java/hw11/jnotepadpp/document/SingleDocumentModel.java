package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Models a single document. A document contains a {@link JTextArea} on which its text is
 * edited and displayed. Track modification status and its file path location.
 * It also supports the observer pattern.
 * @author ltomic
 *
 */
public interface SingleDocumentModel {

	/**
	 * Returns a documents {@link JTextArea} editing area.
	 * @return a documents {@link JTextArea} editing area
	 */
	JTextArea getTextComponent();
	/**
	 * Returns file path. Can be null.
	 * @return file path
	 */
	Path getFilePath();
	/**
	 * Sets documents file path. Cannot be null.
	 * @param path - documents new file path
	 */
	void setFilePath(Path path);
	/**
	 * Returns the modification status
	 * @return modification status
	 */
	boolean isModified();
	/**
	 * Sets the modification status
	 * @param modified - modification status
	 */
	void setModified(boolean modified);
	/**
	 * Adds the provided {@link SingleDocumentListener},
	 * @param l - {@link SingleDocumentListener} to be added
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	/**
	 * Removes the provided {@link SingleDocumentListener}
	 * @param l - {@link SingleDocumentListener} to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}

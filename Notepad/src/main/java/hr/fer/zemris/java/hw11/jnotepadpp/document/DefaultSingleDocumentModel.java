package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Implements a {@link SingleDocumentModel} using {@link JTextArea} as a editing and displaying tool.
 * @author ltomic
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

	/** Document location **/
	private Path filePath;
	/** Documents editing area **/
	private JTextArea textArea;
	/** Documents modification status **/
	private boolean modified;
	
	/** Listener storage **/
	private List<SingleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Constructs a {@link DefaultMultipleDocumentModel} with provided {@link Path} and 
	 * text content. File path can be null.
	 * @param filePath - document location
	 * @param textContent - documents initial text content
	 */
	public DefaultSingleDocumentModel(Path filePath, String textContent) {
		this.filePath = filePath;
		textArea = new JTextArea(textContent);
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (!modified) {
					modified = true;
					notifyDocumentUpdate();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				removeUpdate(arg0);
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				return;
			}
		});
	}
	
	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		Objects.requireNonNull(path);
		
		if (path == this.filePath) return;
		this.filePath = path;
		notifyFilePathUpdated();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyDocumentUpdate();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies the observers about the document modification status update.
	 */
	private void notifyDocumentUpdate() {
		for (SingleDocumentListener i : listeners) {
			i.documentModifyStatusUpdated(this);
		}
	}
	
	/**
	 * Notifies the observers about the documents file path update.
	 */
	private void notifyFilePathUpdated() {
		for (SingleDocumentListener i : listeners) {
			i.documentFilePathUpdated(this);
		}
	}

}

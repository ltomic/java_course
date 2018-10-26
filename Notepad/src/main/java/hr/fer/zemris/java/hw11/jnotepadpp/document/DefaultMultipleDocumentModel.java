package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw11.jnotepadpp.Utils;

/**
 * Implements a {@link MultipleDocumentModel} and uses {@link JTabbedPane} to display them.
 * @author ltomic
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	
	/** Title of the documents that hava no file path **/
	public final static String NO_LOCATION_TITLE = "unnamed";
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	
	/** Document storage **/
	private List<SingleDocumentModel> documents = new ArrayList<>();
	/** Reference to current document **/
	private SingleDocumentModel currentDocument;
	/** Listener storage **/
	private List<MultipleDocumentListener> listeners = new ArrayList<>();

	/**
	 * Constructs a {@link DefaultMultipleDocumentModel} and with one new document.
	 */
	public DefaultMultipleDocumentModel() {
		super();
		
		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				int index = model.getSelectedIndex();
				if (index == -1) return;
				updateCurrentDoc(getDocument(model.getSelectedIndex()));
			}
		});
		
		createNewDocument();
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel(null, "");
		addDoc(newDoc);
		updateCurrentDoc(newDoc);

		return newDoc;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path);
		for (int i = 0; i < documents.size(); ++i) {
			SingleDocumentModel doc = documents.get(i);
			if (doc.getFilePath() == null) continue;
			if (doc.getFilePath().equals(path)) {
				updateCurrentDoc(doc);
				return currentDocument;
			}
		}

		String textContent;
		try {
			textContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Could not read file " + path);
		}

		SingleDocumentModel loadedDoc = new DefaultSingleDocumentModel(path, textContent);
		addDoc(loadedDoc);
		updateCurrentDoc(loadedDoc);
		
		return currentDocument;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if (newPath == null) {
			newPath = model.getFilePath();
		}
		if (newPath == null) { // ovo se ne bi trebalo dogoditi jer je save button disabled
			throw new IllegalArgumentException("Expected a path to save the file");
		}
		for (SingleDocumentModel i : documents) {
			if (!i.equals(model) && newPath.equals(i.getFilePath())) {
				throw new IllegalArgumentException("Specifed file already opened");
			}
		}

		model.setFilePath(newPath);
		model.setModified(false);
		try (PrintWriter out = new PrintWriter(newPath.toFile())) {
			out.print(model.getTextComponent().getText());
		} catch (IOException ex) {
			throw new IllegalArgumentException("Coult not write to file " + newPath);
		}
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);

		if (index == -1) return;
		documents.remove(index);
		this.remove(index);
		notifyDocRemoved(model);

		if (model != null && model.equals(currentDocument)) {
			if (index == documents.size()) index--;
			updateCurrentDoc(documents.size() == 0 ? null : documents.get(index));
		}
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		if (!(0 <= index && index < documents.size())) {
			throw new IllegalArgumentException("Index out of bound, given " + index
					+ " expected between 0 and " + documents.size());
		}
		return documents.get(index);
	}
	
	/**
	 * Stores the provided document and displays it to the user.
	 * @param newDoc - new document to be stored and displayed
	 */
	private void addDoc(SingleDocumentModel newDoc) {
		documents.add(newDoc);
		Path filePath = newDoc.getFilePath();
		String title = filePath == null ? NO_LOCATION_TITLE : filePath.getFileName().toString();
		this.add(title, new JScrollPane(newDoc.getTextComponent()));
		this.setToolTipTextAt(documents.size()-1, filePath == null ? NO_LOCATION_TITLE : filePath.toString());
		this.setIconAt(documents.size()-1, Utils.UNMODIFIED_ICON_FILE);

		newDoc.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				DefaultMultipleDocumentModel.this.setIconAt(documents.indexOf(model), 
						model.isModified() ? Utils.MODIFIED_ICON_FILE: Utils.UNMODIFIED_ICON_FILE);
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				int index = documents.indexOf(model);
				DefaultMultipleDocumentModel.this.setToolTipTextAt(index, 
						model.getFilePath().toString());
				String title = model.getFilePath() == null ? 
						NO_LOCATION_TITLE : model.getFilePath().getFileName().toString();
				DefaultMultipleDocumentModel.this.setTitleAt(index, title);
			}
		});

		
		notifyNewDoc(newDoc);
	}
	
	/**
	 * Updates a current document to the provided one
	 * @param newCurrentDoc - new current document
	 */
	public void updateCurrentDoc(SingleDocumentModel newCurrentDoc) {
		notifyCurrentDocUpdated(newCurrentDoc);
		currentDocument = newCurrentDoc;
		this.setSelectedIndex(documents.indexOf(currentDocument));
	}
	
	/**
	 * Notifies all listeners a new document has been added
	 * @param newDoc - new document that was added
	 */
	private void notifyNewDoc(SingleDocumentModel newDoc) {
		listeners.forEach(l -> {
			l.documentAdded(newDoc);
		});
	}
	
	/**
	 * Notifies all listeners about a new current document
	 * @param newCurrentDoc - new current document
	 */
	private void notifyCurrentDocUpdated(SingleDocumentModel newCurrentDoc) {
		if (newCurrentDoc == null) return;
		listeners.forEach(l -> {
			l.currentDocumentChanged(currentDocument, newCurrentDoc);
		});
	}
	
	/**
	 * Notifies all listeners a document has been removed
	 * @param removedDoc - removed document
	 */
	private void notifyDocRemoved(SingleDocumentModel removedDoc) {
		listeners.forEach(l -> {
			l.documentRemoved(removedDoc);
		});
	}
	


}

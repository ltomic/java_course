package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;


/**
 * Saves the document to its file path. If document has no file path, user is prompted to
 * provide the file path.
 * @author ltomic
 *
 */
public class SaveDocumentAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "Save";

	/**
	 * Constructs a {@link SaveDocumentAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public SaveDocumentAction(MultipleDocumentModel model, ILocalizationProvider lp, JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		SingleDocumentModel current = model.getCurrentDocument();
		if (current.getFilePath() == null) {
			File selectedFile = Utils.selectFile(notepad);
			if (selectedFile == null) return;

			current.setFilePath(selectedFile.toPath());
		}

		model.saveDocument(current, null);
	}

	
}

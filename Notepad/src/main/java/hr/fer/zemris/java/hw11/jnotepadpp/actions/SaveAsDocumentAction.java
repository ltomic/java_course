package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Saves the document to the user provided file path.
 * @author ltomic
 *
 */
public class SaveAsDocumentAction extends JNotepadPPAction {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "SaveAs";

	/**
	 * Constructs a {@link SaveAsDocumentAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public SaveAsDocumentAction(MultipleDocumentModel model, ILocalizationProvider lp, JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		File selectedFile = Utils.selectFile(notepad);
		if (selectedFile == null) return;

		SingleDocumentModel current = model.getCurrentDocument();
		current.setFilePath(selectedFile.toPath());

		model.saveDocument(current, null);
	}

	
}

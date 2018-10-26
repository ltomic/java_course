package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Opens a document from the filepath provided by user in new tab.
 * @author ltomic
 *
 */
public class OpenDocumentAction extends JNotepadPPAction {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization key name **/
	private static final String NAME_KEY = "Open";

	/**
	 * Constructs a {@link OpenDocumentAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public OpenDocumentAction(MultipleDocumentModel model, ILocalizationProvider lp, JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(notepad) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File fileName = fc.getSelectedFile();
		Path filePath = fileName.toPath();

		if (!Files.isReadable(filePath)) {
			JOptionPane.showMessageDialog(notepad,
					"Datoteka " + fileName.getAbsolutePath() + " ne postoji!", "Pogreska",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		model.loadDocument(filePath);
	}

}

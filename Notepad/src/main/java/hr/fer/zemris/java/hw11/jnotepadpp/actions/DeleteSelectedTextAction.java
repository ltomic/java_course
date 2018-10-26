package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Deletes selected part of the text. If nothing is selected, the whole document is
 * considered selected.
 * @author ltomic
 *
 */
public class DeleteSelectedTextAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "DeleteText";

	/**
	 * Constructs a {@link DeleteSelectedTextAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}.
	 * @param model - {@link MultipleDocumentModel} whose current documents selected text should be
	 *            deleted
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP}
	 */
	public DeleteSelectedTextAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		Document doc = editor.getDocument();
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		if (len == 0) return;
		int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

		try {
			doc.remove(offset, len);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

}

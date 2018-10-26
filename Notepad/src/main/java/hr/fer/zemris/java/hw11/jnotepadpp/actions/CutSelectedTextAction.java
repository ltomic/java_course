package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Cuts selected part of the text to the clipboard. If nothing is selected, the whole document is
 * considered selected.
 * @author ltomic
 *
 */
public class CutSelectedTextAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "CutSelectedText";

	/**
	 * Constructs a {@link CutSelectedTextAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}.
	 * @param model - {@link MultipleDocumentModel} whose current documents selected text should be
	 *            cut
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP}
	 */
	public CutSelectedTextAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		model.getCurrentDocument().getTextComponent().cut();
	}

}

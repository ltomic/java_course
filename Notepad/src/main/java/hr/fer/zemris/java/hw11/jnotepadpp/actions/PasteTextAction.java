package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Pastes text from the clipboard.
 * @author ltomic
 *
 */
public class PasteTextAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "PasteText";

	/**
	 * Constructs a {@link PasteTextAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}.
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public PasteTextAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		model.getCurrentDocument().getTextComponent().paste();
	}

}

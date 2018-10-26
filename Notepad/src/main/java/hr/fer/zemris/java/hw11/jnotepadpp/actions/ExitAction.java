package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Exits the application. Implemented as closing of all tabs.
 * @author ltomic
 *
 */
public class ExitAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "Exit";

	/**
	 * Constructs a {@link ExitAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} whose documents should be all closed
	 * @param lp - {@link LocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} which should be closed
	 */
	public ExitAction(MultipleDocumentModel model, ILocalizationProvider lp, JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		notepad.closeAllTabs();
	}

}

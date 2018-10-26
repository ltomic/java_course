package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Closes current tab. If there are no more opened tab, disposes the application window.
 * @author ltomic
 *
 */
public class CloseDocumentAction extends JNotepadPPAction {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Action localization key **/
	private static final String NAME_KEY = "Close";
	
	/**
	 * Constructs a {@link CloseDocumentAction} with provided {@link MultipleDocumentModel} from
	 * which the current tab should be closed, {@link ILocalizationProvider} and {@link JNotepadPP}.
	 * @param model - model whose current tab should be closed
	 * @param lp - localization provider for this action
	 * @param notepad - {@link JNotepadPP} which should be disposed if no more tabs are opened
	 */
	public CloseDocumentAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		notepad.closeTab();
		if (model.getNumberOfDocuments() == 0) notepad.dispose();
	}

}

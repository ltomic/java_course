package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * Abstracts a action for the {@link JNotepadPP}. To construct a {@link JNotepadPPAction} class
 * a {@link MultipleDocumentModel} and {@link JNotepadPP} are needed to perform the implemented
 * actions.
 * @author ltomic
 *
 */
public abstract class JNotepadPPAction extends LocalizableAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** {@link MultipleDocumentModel} on which action operates */
	protected MultipleDocumentModel model;
	/** {@link JNotepadPP} on which action operates*/
	protected JNotepadPP notepad;

	/**
	 * Constructs a {@link JNotepadPPAction} with provided {@link MultipleDocumentModel}, 
	 * localization name key, {@link ILocalizationProvider} and {@link JNotepadPP}.
	 * @param model - {@link MultipleDocumentModel} on which action operates
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider} the action uses
	 * @param notepad - {@link JNotepadPP} - on which action operates
	 */
	public JNotepadPPAction(MultipleDocumentModel model, String key, ILocalizationProvider lp, JNotepadPP notepad) {
		super(key, lp);
		this.model = model;
		this.notepad = notepad;
	}
}

package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.function.UnaryOperator;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Toggles the case of the selected text or the whole document if there is no selected text.
 * @author ltomic
 *
 */
public class ToggleCaseAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization key name **/
	private static final String NAME_KEY = "ToggleCase";

	/** Operator implementing the case toggling **/
	private static final UnaryOperator<Character> OP = (
			c) -> Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c);

	/**
	 * Constructs a {@link ToggleCaseAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public ToggleCaseAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Utils.transformText(model.getCurrentDocument(), OP);
	}

}

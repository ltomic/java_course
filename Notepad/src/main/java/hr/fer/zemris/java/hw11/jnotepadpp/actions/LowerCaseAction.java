package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.function.UnaryOperator;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Lowers the case of the selected text in the current document.
 * @author ltomic
 *
 */
public class LowerCaseAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	
	/** Localization name key **/
	private static final String NAME_KEY = "ToLowerCase";
	/** Operator implementing case lowering */
	private static final UnaryOperator<Character> OP = (c) -> Character.toLowerCase(c);

	/**
	 * Constructs a {@link LowerCaseAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public LowerCaseAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Utils.transformText(model.getCurrentDocument(), OP);
	}

}

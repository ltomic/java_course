package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentAdapter;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Abstracts a action that only operates on selected text thus it is disabled when there is no
 * selected text. Selected text detection is implemented usign {@link CaretListener} on current
 * document.
 * @author ltomic
 *
 */
public abstract class SelectedTextAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Listener that tracks caret position and enables or disables the action **/
	public final CaretListener selectedTextListener = new CaretListener() {

		@Override
		public void caretUpdate(CaretEvent event) {
			JTextArea editor = (JTextArea) event.getSource();
			Caret caret = editor.getCaret();
			SelectedTextAction.this.setEnabled(caret.getDot() != caret.getMark());
		}
	};

	/**
	 * Constructs a {@link SelectedTextAction} with provided {@link MultipleDocumentModel},
	 * localization name key {@link ILocalizationProvider} and {@link JNotepadPP}. Adds listeners to
	 * the {@link MultipleDocumentModel} so that the {@link CaretListener} changes the document it
	 * tracks when current document changes.
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public SelectedTextAction(MultipleDocumentModel model, String key, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, key, lp, notepad);
		model.addMultipleDocumentListener(new MultipleDocumentAdapter() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel,
					SingleDocumentModel currentModel) {
				if (previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(selectedTextListener);
				}
				if (currentModel != null) {
					currentModel.getTextComponent().addCaretListener(selectedTextListener);
				}
			}
		});

		Caret caret = model.getCurrentDocument().getTextComponent().getCaret();
		this.setEnabled(caret.getDot() != caret.getMark());
		model.getCurrentDocument().getTextComponent().addCaretListener(selectedTextListener);
	}
}

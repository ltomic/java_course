package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Filters the unique lines from the selected ones.
 * @author ltomic
 *
 */
public class UniqueLinesAction extends SelectedTextAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "Unique";
	
	/**
	 * Constructs a {@link UniqueLinesAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public UniqueLinesAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		Document doc = editor.getDocument();
		Caret caret = editor.getCaret();
		String text = Utils.getSelectedLines(editor);
		int start = Math.min(caret.getDot(), caret.getMark());
		int end = Math.max(caret.getDot(), caret.getMark());
		
		String[] lines = text.split("\\n");
		String[] uniqueLines = Arrays.stream(lines).distinct().toArray(String[]::new);
		String resultText = String.join("\n", uniqueLines);
		
		try {
			doc.remove(start, end-start);
			doc.insertString(start, resultText, null);
		} catch (BadLocationException ex) {
			System.err.println("Error while inserting or deleting text");
		}
	}

}

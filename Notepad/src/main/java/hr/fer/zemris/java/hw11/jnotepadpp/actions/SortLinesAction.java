package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.Utils;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Sorts selected lines.
 * @author ltomic
 *
 */
public class SortLinesAction extends SelectedTextAction {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Comparator used to compare the lines **/
	private Comparator<String> comp;

	/**
	 * Constructs a {@link SortLinesAction} with provided {@link MultipleDocumentModel}, localization key name
	 * {@link ILocalizationProvider}, {@link JNotepadPP} and a {@link Comparator} used
	 * to compare the lines.
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param key - localization name key
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public SortLinesAction(MultipleDocumentModel model, String key, ILocalizationProvider lp,
			JNotepadPP notepad, Comparator<String> comp) {
		super(model, key, lp, notepad);
		this.comp = comp;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		String text = Utils.getSelectedLines(editor);
		Document doc = editor.getDocument();
		Caret caret = editor.getCaret();
		int start = Math.min(caret.getDot(), caret.getMark());
		int end = Math.max(caret.getDot(), caret.getMark());
	
		
		String[] lines = text.split("\\n");
		Arrays.sort(lines, comp);
		
		String resultText = String.join("\n", lines);
		
		try {
			doc.remove(start, end-start);
			doc.insertString(start, resultText, null);
		} catch (BadLocationException ex) {
			System.err.println("Error while deleting or inserting text");
		}
	}

}

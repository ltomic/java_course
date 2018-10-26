package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Display current document stats.
 * @author ltomic
 *
 */
public class StatsDocumentAction extends JNotepadPPAction {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Localization name key **/
	private static final String NAME_KEY = "DocumentStats";

	/** Stats message format **/
	private static final String STAT_INFO_FORMAT = "Your document has %d characters, %d non-blank characters and %d lines";

	/**
	 * Constructs a {@link NewDocumentAction} with provided {@link MultipleDocumentModel},
	 * {@link ILocalizationProvider} and {@link JNotepadPP}
	 * @param model - {@link MultipleDocumentModel} on which this action operates
	 * @param lp - {@link ILocalizationProvider} for the action
	 * @param notepad - {@link JNotepadPP} on which this action operates
	 */
	public StatsDocumentAction(MultipleDocumentModel model, ILocalizationProvider lp,
			JNotepadPP notepad) {
		super(model, NAME_KEY, lp, notepad);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JTextArea area = model.getCurrentDocument().getTextComponent();
		String text = area.getText();
		int charNum = text.length();
		int nonBlankCharNum = text.replaceAll("\\s+", "").length();
		int numberOfLines = area.getLineCount();
		JOptionPane.showMessageDialog(notepad,
				String.format(STAT_INFO_FORMAT, charNum, nonBlankCharNum, numberOfLines),
				"Statistical information", JOptionPane.INFORMATION_MESSAGE);
	}

}

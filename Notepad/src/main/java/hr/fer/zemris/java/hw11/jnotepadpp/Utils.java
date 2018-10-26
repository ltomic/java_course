package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.UnaryOperator;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;

/**
 * Utility class for the {@link JNotepadPP}. Fetched {@link ImageIcon}s and implements
 * a few helper methods.
 * @author ltomic
 *
 */
@SuppressWarnings("javadoc")
public class Utils {

	public static final ImageIcon UNMODIFIED_ICON_FILE = loadIcon("UnmodifiedFileIcon.png");
	public static final ImageIcon MODIFIED_ICON_FILE = loadIcon("ModifiedFileIcon.png");
	public static final ImageIcon SAVE_DOCUMENT_ICON = loadIcon("SaveIcon.png");
	public static final ImageIcon OPEN_DOCUMENT_ICON = loadIcon("OpenIcon.png");
	public static final ImageIcon CLOSE_DOCUMENT_ICON = loadIcon("CloseTabIcon.png");
	public static final ImageIcon INFO_DOCUMENT_ICON = loadIcon("InfoIcon.png");
	public static final ImageIcon NEW_DOCUMENT_ICON = loadIcon("NewIcon.png");
	public static final ImageIcon COPY_TEXT_ICON = loadIcon("CopyIcon.png");
	public static final ImageIcon CUT_TEXT_ICON = loadIcon("CutIcon.png");
	public static final ImageIcon PASTE_TEXT_ICON = loadIcon("PasteIcon.png");
	public static final ImageIcon DELETE_TEXT_ICON = loadIcon("DeleteIcon.png");
	public static final ImageIcon EXIT_ICON = loadIcon("ExitIcon.png");
	public static final ImageIcon TOGGLE_CASE_ICON = loadIcon("ToggleCaseIcon.png");

	/** Extension used in the .properties files for the action mnemonic,
	 * example : if action name key is "New", key for mnemonic will be "New_MNEMONIC" 
	 */
	public static final String MNEMONIC_EXTENSION = "_MNEMONIC";

	/**
	 * Loads the {@link ImageIcon} with provided name. Icons must be stored
	 * in icons subpackage of this package.
	 * @param name - name of the icon to load
	 * @return loaded {@link ImageIcon} stored under provided name in icons subpackage
	 */
	public static ImageIcon loadIcon(String name) {
		try (InputStream is = Utils.class.getResourceAsStream("icons/" + name)) {
			if (is == null) throw new IllegalArgumentException("No such icon exists" + name);
			return new ImageIcon(is.readAllBytes());
		} catch (IOException ex) {
			throw new IllegalArgumentException("I/O error");
		}
	}

	/**
	 * Returns the provided text with {@link UnaryOperator} applyed to every character
	 * @param text - text to change
	 * @param op - {@link UnaryOperator} to apply to every character of the text
	 * @return - changes text
	 */
	public static String changeText(String text, UnaryOperator<Character> op) {
		char[] znakovi = text.toCharArray();
		for (int i = 0; i < znakovi.length; ++i) {
			znakovi[i] = op.apply(znakovi[i]);
		}

		return new String(znakovi);
	};

	/**
	 * Transforms the selected text in the given {@link SingleDocumentModel} by applying provided
	 * {@link UnaryOperator} to every character in the selected text. If there is no selected
	 * text, it is considered that the whole document is selected.
	 * @param model - model whose selected text should be transformed
	 * @param op - {@link UnaryOperator} to apply to the selected text
	 */
	public static void transformText(SingleDocumentModel model, UnaryOperator<Character> op) {
		JTextArea editor = model.getTextComponent();
		Document doc = editor.getDocument();

		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		int offset = 0;
		if (len != 0) {
			offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		} else {
			len = doc.getLength();
		}

		try {
			String text = doc.getText(offset, len);
			text = changeText(text, op);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns the text containing selected lines in the provided {@link JTextArea}
	 * @param editor - {@link JTextArea} from which the selected lines should be got
	 * @return the text containing selected lines in the provided {@link JTextArea}
	 */
	public static String getSelectedLines(JTextArea editor) {
		Document doc = editor.getDocument();
		Caret caret = editor.getCaret();
		int start = Math.min(caret.getDot(), caret.getMark());
		int end = Math.max(caret.getDot(), caret.getMark());

		if (start == end) {
			start = 0;
			end = doc.getLength();
		}

		try {
			start = editor.getLineStartOffset(editor.getLineOfOffset(start));
			end = editor.getLineEndOffset(editor.getLineOfOffset(end));
			return doc.getText(start, end - start);
		} catch (BadLocationException ex) {
			System.err.println("Error while fetching selected text");
			return null;
		}
	}

	/**
	 * Prompts the user to select a file(that file can be nonexistent) from the file system
	 * and returns it.
	 * @param frame - parent component of the dialog show to the user
	 * @return user selected file
	 */
	public static File selectFile(JFrame frame) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save document");

		if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(frame, "Nista nije snimljeno", "Upozorenje",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		File selectedFile = jfc.getSelectedFile();

		if (selectedFile.exists()) {
			int overwrite = JOptionPane.showConfirmDialog(frame,
					"File already exists? Would you like to overwrite?", "Overwrite?",
					JOptionPane.YES_NO_OPTION);
			
			if (overwrite == JOptionPane.YES_OPTION) {
				return selectedFile;
			}
			JOptionPane.showMessageDialog(frame, "Nista nije snimljeno", "Upozorenje",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return selectedFile;
	}
}

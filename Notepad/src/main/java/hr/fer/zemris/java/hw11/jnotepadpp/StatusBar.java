package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;

/**
 * Status bar of the {@link JNotepadPP}. Displays document length, caret position,
 * selected text length and current time.
 * @author ltomic
 *
 */
public class StatusBar extends JToolBar {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Document length label **/
	private JLabel lengthLabel = new JLabel("init");
	/** Caret position label **/
	private JLabel caretLabel = new JLabel(String.format(CARET_LABEL_FORMAT, 0, 0, 0));
	/** JNotepadPP model it displays **/
	private MultipleDocumentModel model;
	
	/** Document length label format **/
	private static final String LENGTH_LABEL_FORMAT = "length : %d";
	/** Caret position label format **/
	private static final String CARET_LABEL_FORMAT = "Ln : %d Col : %d Sel : %d";
	
	/** Updates document length label when letters are removed or added **/
	private final DocumentListener docListener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent event) {
			updateLengthLabel(model.getCurrentDocument());
		}
		
		@Override
		public void insertUpdate(DocumentEvent event) {
			updateLengthLabel(model.getCurrentDocument());
		}
		
		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}
	};
	
	/** Updates caret position label when caret is moved **/
	private final CaretListener caretListener = new CaretListener() {
		
		@Override
		public void caretUpdate(CaretEvent event) {
			updateCaretLabels(model.getCurrentDocument());
		}
	};

	/**
	 * Constructs a {@link StatusBar} with reference to {@link MultipleDocumentModel}.
	 * @param model - model this status bar tracks
	 * @param clock - status bar clock
	 */
	public StatusBar(MultipleDocumentModel model, JComponent clock) {
		super("Status bar");
		this.setLayout(new GridLayout(0, 3));
		this.setFloatable(true);
		this.model = model;
		initGUI(clock);
	}
	
	/**
	 * Initializes GUI of the status bar.
	 * @param clock - clock to add to the status bar
	 */
	private void initGUI(JComponent clock) {
		updateLengthLabel(model.getCurrentDocument());
		model.getCurrentDocument().getTextComponent().getDocument().addDocumentListener(docListener);
		model.getCurrentDocument().getTextComponent().addCaretListener(caretListener);

		model.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				return;
			}
			
			@Override
			public void documentAdded(SingleDocumentModel model) {
				return;
			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel,
					SingleDocumentModel currentModel) {
				if (previousModel != null) {
					previousModel.getTextComponent().getDocument().removeDocumentListener(docListener);
					previousModel.getTextComponent().removeCaretListener(caretListener);
				}
				currentModel.getTextComponent().getDocument().addDocumentListener(docListener);
				currentModel.getTextComponent().addCaretListener(caretListener);
				updateLengthLabel(currentModel);
				updateCaretLabels(currentModel);
			}
		});
		
		this.add(lengthLabel);
		this.add(caretLabel);
		this.add(clock);
	}
	
	/**
	 * Updates document length label using information from the provided {@link SingleDocumentModel}.
	 * @param doc - doc whose length should be displayed
	 */
	private void updateLengthLabel(SingleDocumentModel doc) {
		if (doc == null) return;
		lengthLabel.setText(String.format(LENGTH_LABEL_FORMAT, doc.getTextComponent().getDocument().getLength()));
	}
	
	/**
	 * Updates caret position label using information from the provided {@link SingleDocumentModel}
	 * @param doc - doc whose caret position should be displayed
	 */
	private void updateCaretLabels(SingleDocumentModel doc) {
		if (doc == null) return;
		JTextArea area = doc.getTextComponent();
		Caret caret = area.getCaret();
		int selectionLength = Math.abs(caret.getDot() - caret.getMark());
		int lineNum = -1;
		int columnNum = -1;
		
		try {
			int caretPos = area.getCaretPosition();
			lineNum = area.getLineOfOffset(area.getCaretPosition());
			columnNum = caretPos - area.getLineStartOffset(lineNum);
		} catch (BadLocationException ex) {
			System.err.println("Could not update caret label");
		}
		
		caretLabel.setText(String.format(CARET_LABEL_FORMAT, lineNum, columnNum, selectionLength));
	}

	
}

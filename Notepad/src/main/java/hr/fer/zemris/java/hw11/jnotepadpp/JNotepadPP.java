package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.ChangeLanguageAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CloseDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CopySelectedTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CutSelectedTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.DeleteSelectedTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ExitAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.LowerCaseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.NewDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.NoAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.PasteTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveAsDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SortLinesAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.StatsDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ToggleCaseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.UniqueLinesAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.UpperCaseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentAdapter;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.FormLocalizationProvider;

/**
 * A simple text editor that supports multiple documents opened in each tab.
 * It can copy, cut and paste text, save and open files, sort the selected lines of the
 * file in ascending or descending order, filter the unique lines among the selected ones
 * and change the case of the selected text. Interface supports three languages : English,
 * Croatian and German.
 * @author ltomic
 *
 */
public class JNotepadPP extends JFrame {

	/** Serial version **/
	private static final long serialVersionUID = 1L;
	/** Window title format **/
	private static final String TITLE_FORMAT = "%s - JNotepad++";

	/** Calls method that updates the window title when a document changes the file path **/
	private final SingleDocumentListener filePathChangeListener = new SingleDocumentListener() {

		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			return;
		}

		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			updateTitle(model);
		}
	};
	
	/** Model of a multiple document structure instance **/
	private MultipleDocumentModel model = new DefaultMultipleDocumentModel();
	/** Provides localization for the application **/
	private FormLocalizationProvider flp = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), this);
	/** Application clock **/
	private MyClock clock = new MyClock();

	/** Used for locale-sensitive String comparison **/
	private Collator collator = Collator.getInstance();

	/**
	 * Constructs a {@link JNotepadPP} and initializes the window GUI.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				closeAllTabs();
			}
		});

		flp.addLocalizationListener(
				() -> collator = Collator.getInstance(new Locale(flp.getString("locale"))));

		setLocation(100, 100);
		setSize(800, 600);

		initGUI();
	}

	/**
	 * Initializes application GUI
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add((JComponent)model, BorderLayout.CENTER);


		model.addMultipleDocumentListener(new MultipleDocumentAdapter() {
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel,
					SingleDocumentModel currentModel) {
				if (previousModel != null) {
					previousModel.removeSingleDocumentListener(filePathChangeListener);
				}
				updateTitle(currentModel);
				if (currentModel != null) {
					currentModel.addSingleDocumentListener(filePathChangeListener);
				}
			}
		});
		updateTitle(model.getCurrentDocument());
		model.getCurrentDocument().addSingleDocumentListener(filePathChangeListener);
		
		createStatusBar();
		initActions();
		createMenus();
		createToolBars();
	}

	/**
	 * Updates the windows title with the provided {@link SingleDocumentModel} file path.
	 * If path is null, "unnamed" is used as title.
	 * @param currentModel - {@link SingleDocumentModel} whose file path should be the window title
	 */
	private void updateTitle(SingleDocumentModel currentModel) {
		Path path = currentModel.getFilePath();
		String filePath = path == null ? "unnamed" : path.toString();
		JNotepadPP.this.setTitle(String.format(TITLE_FORMAT, filePath));
	}

	/** Creates new document in new tab **/
	private final Action newDocumentAction = new NewDocumentAction(model, flp, this);
	/** Opens document in new tab **/
	private final Action openDocumentAction = new OpenDocumentAction(model, flp, this);
	/** Closes the document **/
	private final Action closeDocumentAction = new CloseDocumentAction(model, flp, this);
	/** Saves the document to its file path **/
	private final Action saveDocumentAction = new SaveDocumentAction(model, flp, this);
	/** Saves the document to user chosen file path **/
	private final Action saveAsDocumentAction = new SaveAsDocumentAction(model, flp, this);
	/** Exits the application **/
	private final Action exitAction = new ExitAction(model, flp, this);

	/** Deletes the selected text **/
	private final Action deleteSelectedTextAction = new DeleteSelectedTextAction(model, flp, this);
	/** Cuts the selected text **/
	private final Action cutSelectedTextAction = new CutSelectedTextAction(model, flp, this);
	/** Copies the selected text **/
	private final Action copySelectedTextAction = new CopySelectedTextAction(model, flp, this);
	/** Pastes the text from the clipboard **/
	private final Action pasteTextAction = new PasteTextAction(model, flp, this);

	/** Toggles case of the selected text or whole document **/
	private final Action toggleCaseAction = new ToggleCaseAction(model, flp, this);
	/** Lowers the case of the selected text or whole document **/
	private final Action lowerCaseAction = new LowerCaseAction(model, flp, this);
	/** Uppers the case of the selected text or whole document **/
	private final Action upperCaseAction = new UpperCaseAction(model, flp, this);
	/** Shows stats about the current document **/
	private final Action statsDocumentAction = new StatsDocumentAction(model, flp, this);

	/** Changes the interface to English language **/
	private final Action englishLanguageAction = new ChangeLanguageAction("English", flp, "en");
	/** Changes the interface to Croatian language **/
	private final Action croatianLanguageAction = new ChangeLanguageAction("Croatian", flp, "hr");
	/** Changes the interface to German langauge **/
	private final Action germanLanguageAction = new ChangeLanguageAction("German", flp, "de");

	/** Sorts in ascending order the selected lines in document **/
	private final Action sortLinesAscendingAction = new SortLinesAction(model, "Ascending", flp,
			this, (a, b) -> collator.compare(a, b));
	/** Sorts in descending order the selected lines in document **/
	private final Action sortLinesDescendingAction = new SortLinesAction(model, "Descending", flp,
			this, (a, b) -> collator.compare(b, a));
	/** Filters the unique lines from the selected lines in document **/
	private final Action uniqueLinesAction = new UniqueLinesAction(model, flp, this);

	/**
	 * Initializes properties for all actions
	 */
	private void initActions() {
		initActionProperties(newDocumentAction, KeyStroke.getKeyStroke("control N"),
				"Used to create new blank document.", Utils.NEW_DOCUMENT_ICON);
		initActionProperties(openDocumentAction, KeyStroke.getKeyStroke("control O"),
				"Used to open existing file from disk.", Utils.OPEN_DOCUMENT_ICON);
		initActionProperties(closeDocumentAction, KeyStroke.getKeyStroke("control W"),
				"Used to close current tab.", Utils.CLOSE_DOCUMENT_ICON);
		initActionProperties(saveDocumentAction, KeyStroke.getKeyStroke("control S"),
				"Used to save current file to disk.", Utils.SAVE_DOCUMENT_ICON);
		initActionProperties(saveAsDocumentAction, KeyStroke.getKeyStroke("control Q"),
				"Used to save new file to disk.", Utils.SAVE_DOCUMENT_ICON);
		initActionProperties(deleteSelectedTextAction, KeyStroke.getKeyStroke("control F2"),
				"Used to delete selected part of text.", Utils.DELETE_TEXT_ICON);
		initActionProperties(cutSelectedTextAction, KeyStroke.getKeyStroke("control X"),
				"Used to cut selected part of text.", Utils.CUT_TEXT_ICON);
		initActionProperties(copySelectedTextAction, KeyStroke.getKeyStroke("control C"),
				"Used to copy selected part of text.", Utils.COPY_TEXT_ICON);
		initActionProperties(pasteTextAction, KeyStroke.getKeyStroke("control V"),
				"Used to paste text from clipboard.", Utils.PASTE_TEXT_ICON);
		initActionProperties(toggleCaseAction, KeyStroke.getKeyStroke("control F1"),
				"Used to toggle character case", Utils.TOGGLE_CASE_ICON);
		initActionProperties(statsDocumentAction, KeyStroke.getKeyStroke("control F3"),
				"Used to diplay statistical information about the current file",
				Utils.INFO_DOCUMENT_ICON);
		initActionProperties(exitAction, KeyStroke.getKeyStroke("control X"), "Exit application",
				Utils.EXIT_ICON);
	}

	/**
	 * Used to initialize accelerator key, short description and large icon for the provided action
	 * @param action - action whose properties are setup
	 * @param keyStroke - accelerator key to configure
	 * @param description - description to configure
	 * @param actionIcon - icon to configure
	 */
	private void initActionProperties(Action action, KeyStroke keyStroke, String description,
			ImageIcon actionIcon) {
		action.putValue(Action.ACCELERATOR_KEY, keyStroke);
		action.putValue(Action.SHORT_DESCRIPTION, description);
		action.putValue(Action.LARGE_ICON_KEY, actionIcon);
	}

	/**
	 * Creates window menu bar containing all available actions.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new NoAction("File", flp));
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));

		JMenu editMenu = new JMenu(new NoAction("Edit", flp));
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(deleteSelectedTextAction));
		editMenu.add(new JMenuItem(cutSelectedTextAction));
		editMenu.add(new JMenuItem(copySelectedTextAction));
		editMenu.add(new JMenuItem(pasteTextAction));

		JMenu toolsMenu = new JMenu(new NoAction("Tools", flp));
		menuBar.add(toolsMenu);

		JMenu changeCaseMenu = new JMenu(new NoAction("ChangeCase", flp));
		toolsMenu.add(changeCaseMenu);

		changeCaseMenu.add(new JMenuItem(toggleCaseAction));
		changeCaseMenu.add(new JMenuItem(lowerCaseAction));
		changeCaseMenu.add(new JMenuItem(upperCaseAction));

		toolsMenu.add(new JMenuItem(statsDocumentAction));

		JMenu sortMenu = new JMenu(new NoAction("Sort", flp));
		toolsMenu.add(sortMenu);

		sortMenu.add(new JMenuItem(sortLinesAscendingAction));
		sortMenu.add(new JMenuItem(sortLinesDescendingAction));
		sortMenu.add(new JMenuItem(uniqueLinesAction));

		this.setJMenuBar(menuBar);

		JMenu languageMenu = new JMenu(new NoAction("Language", flp));
		menuBar.add(languageMenu);

		languageMenu.add(new JMenuItem(englishLanguageAction));
		languageMenu.add(new JMenuItem(croatianLanguageAction));
		languageMenu.add(new JMenuItem(germanLanguageAction));
	}

	/**
	 * Creates a toolbar containing common actions
	 */
	private void createToolBars() {
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);

		toolBar.add(createToolbarButton(newDocumentAction));
		toolBar.add(createToolbarButton(openDocumentAction));
		toolBar.add(createToolbarButton(saveDocumentAction));
		toolBar.add(createToolbarButton(closeDocumentAction));

		toolBar.addSeparator();
		toolBar.add(createToolbarButton(deleteSelectedTextAction));
		toolBar.add(createToolbarButton(cutSelectedTextAction));
		toolBar.add(createToolbarButton(copySelectedTextAction));
		toolBar.add(createToolbarButton(pasteTextAction));
		toolBar.add(createToolbarButton(toggleCaseAction));

		toolBar.addSeparator();
		toolBar.add(createToolbarButton(statsDocumentAction));

		toolBar.addSeparator();
		toolBar.add(createToolbarButton(exitAction));

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Creates a JButton with provided action and its text is hidden.
	 * @param action - the {@link Action} used to speficy the button
	 * @return JButton with provided action and its text hidden.
	 */
	private JButton createToolbarButton(Action action) {
		JButton button = new JButton(action);
		button.setHideActionText(true);
		return button;
	}

	/**
	 * Creates application status bar
	 */
	private void createStatusBar() {
		JToolBar statusBar = new StatusBar(model, clock);
		this.getContentPane().add(statusBar, BorderLayout.PAGE_END);
	}

	/**
	 * Closes all opened tabs
	 */
	public void closeAllTabs() {
		while (model.getCurrentDocument() != null) {
			if (!closeTab()) return;
		}
		exit();
	}
	
	/**
	 * Closes the application
	 */
	public void exit() {
		clock.stop();
		dispose();
	}

	/**
	 * Closes a tab that is currently opened. If document displayed in the tab is not saved
	 * user will be prompted to save the document. If user chooses to cancel the closing of
	 * the tab a false is returned as a result of this method. Otherwise true is returned.
	 * @return false, if user chooses to cancel the closing of the tab, else true
	 */
	public boolean closeTab() {
		SingleDocumentModel current = model.getCurrentDocument();
		if (current.isModified()) {
			String fileName = current.getFilePath() == null
					? DefaultMultipleDocumentModel.NO_LOCATION_TITLE
					: current.getFilePath().getFileName().toString();

			int result = JOptionPane.showConfirmDialog(this,
					fileName + " is not saved. Would you like to save it?", "File not saved",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == JOptionPane.CANCEL_OPTION) return false;
			if (result == JOptionPane.YES_OPTION) {
				saveDocumentAction.actionPerformed(null);
				if (current.isModified()) return false; // ako je korisnik odustao tijekom biranja
														// filea
				// ako je dobro, zatvorit ce
			}
			// ako je NO, zatvorit ce
		}

		model.closeDocument(model.getCurrentDocument());
		if (model.getCurrentDocument() == null) {
			exit();
		}
		return true;
	}

	/**
	 * Method called at the beginning of the program. Invokes the construction of {@link JNotepadPP}.
	 * @param args - no arguments needed
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			(new JNotepadPP()).setVisible(true);
		});
	}
}

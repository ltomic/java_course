package hr.fer.zemris.java.hw16.jvdraw;

import static hr.fer.zemris.java.hw16.jvdraw.Util.errorDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import hr.fer.zemris.java.hw16.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.PolygonTool;
import hr.fer.zemris.java.hw16.jvdraw.tools.Tool;

/**
 * Program which offers drawing of lines, circles and filled circles in colors. <br>
 * Line is drawn by clicking on the start point and end point. <br>
 * Circle and filled is drawn by clicking on the center point and clicking of another point thus
 * defining the radius of the circle. <br>
 * {@link GeometricalObject}s can be edited by double clicking on them on the list. Objects are
 * rendered from top to bottom of the list. To move the selected object up the list press '-' and to
 * move object down the list press +. To delete the object selected object press Delete keyboard
 * button.<br>
 * Drawn image can be exported as image by clicking on File->Export. In the provided file name one
 * of the following extensions should be provided "png", "gif", "jpg". <br>
 * Drawn image can be saved as JVD document so it can be opened latter by this program.
 * @author ltomic
 *
 */
public class JVDraw extends JFrame {

	/** Serial version */
	private static final long serialVersionUID = 1L;

	/** Default foreground color */
	private static final Color DEFAULT_FG_COLOR = Color.RED;
	/** Default background color */
	private static final Color DEFAULT_BG_COLOR = Color.YELLOW;

	/** Label of the line tool button */
	private static final String LINE_TOOL_BUTTON_TEXT = "Line";
	/** Label of the circle tool button */
	private static final String CIRCLE_TOOL_BUTTON_TEXT = "Circle";
	/** Filled circle tool button label */
	private static final String FILLEDCIRCLE_TOOL_BUTTON_TEXT = "Filled circle";
	
	private static final String POLYGON_TOOL_BUTTON_TEXT = "Polygon";

	/** Drawing model */
	private DrawingModel drawingModel = new DrawingModelImpl();
	/** Drawing canvas */
	private JDrawingCanvas canvas = new JDrawingCanvas(drawingModel);
	/** Outline color area */
	private JColorArea outlineColorArea = new JColorArea(DEFAULT_FG_COLOR);
	/** Fill color area */
	private JColorArea fillColorArea = new JColorArea(DEFAULT_BG_COLOR);
	/** Current tool */
	private Tool currentTool = new LineTool(outlineColorArea, drawingModel);

	/** Currently loaded file */
	private Path loadedFile;
	/** Is current work saved */
	private boolean unsaved = false;

	/**
	 * Initializes application startup
	 */
	public JVDraw() {

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				EXIT_ACTION.actionPerformed(null);
			}
		});

		setLocation(200, 200);
		setSize(1000, 1200);

		initGUI();
	}

	/**
	 * Initializes application GUI
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		drawingModel.addDrawingModelListener(savedListener);

		canvas.setPreferredSize(new Dimension(600, 600));
		canvas.setCurrentTool(currentTool);

		JToolBar toolbar = initToolbar();

		JScrollPane listPane = new JScrollPane(initList());

		initCanvas();
		createMenus();

		ColorLabel colorLabel = new ColorLabel(outlineColorArea, fillColorArea);

		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(canvas, BorderLayout.CENTER);
		getContentPane().add(colorLabel, BorderLayout.SOUTH);
		getContentPane().add(listPane, BorderLayout.EAST);

		this.pack();

	}

	/**
	 * Creates app menus
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileMenu.add(OPEN_DOCUMENT_ACTION);
		fileMenu.add(SAVE_DOCUMENT_ACTION);
		fileMenu.add(SAVEAS_DOCUMENT_ACTION);
		fileMenu.add(EXPORT_ACTION);
		fileMenu.add(EXIT_ACTION);

		this.setJMenuBar(menuBar);
	}

	/**
	 * Creates app toolbar
	 * @return app toolbar
	 */
	private JToolBar initToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbar.add(outlineColorArea);
		toolbar.add(fillColorArea);

		toolbar.addSeparator();

		ButtonGroup toolButtons = new ButtonGroup();
		JButton lineButton = new JButton(LINE_TOOL_BUTTON_TEXT);
		JButton circleButton = new JButton(CIRCLE_TOOL_BUTTON_TEXT);
		JButton filledCircleButton = new JButton(FILLEDCIRCLE_TOOL_BUTTON_TEXT);
		JButton polygonCircleButton = new JButton(POLYGON_TOOL_BUTTON_TEXT);
		toolButtons.add(lineButton);
		toolButtons.add(circleButton);
		toolButtons.add(filledCircleButton);
		toolButtons.add(polygonCircleButton);
		lineButton.setSelected(true);

		toolbar.add(lineButton);
		toolbar.add(circleButton);
		toolbar.add(filledCircleButton);
		toolbar.add(polygonCircleButton);

		lineButton.addActionListener((action) -> {
			setCurrentTool(new LineTool(outlineColorArea, drawingModel));
		});

		circleButton.addActionListener((action) -> {
			setCurrentTool(new CircleTool(outlineColorArea, drawingModel));
		});

		filledCircleButton.addActionListener((action) -> {
			setCurrentTool(new FilledCircleTool(outlineColorArea, fillColorArea, drawingModel));
		});
		
		polygonCircleButton.addActionListener((action) -> {
			setCurrentTool(new PolygonTool(outlineColorArea, fillColorArea, drawingModel));
		});

		toolbar.setFloatable(true);
		return toolbar;
	}

	/**
	 * Initializes listeners on the app canvas
	 */
	private void initCanvas() {
		canvas.addMouseMotionListener(new MouseInputAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				currentTool.mouseMoved(e);
				canvas.repaint();
			}

		});
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				currentTool.mouseDragged(e);
				canvas.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				currentTool.mouseReleased(e);
				canvas.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				currentTool.mousePressed(e);
				canvas.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				currentTool.mouseClicked(e);
				canvas.repaint();
			}
		});
	}

	/**
	 * Initiliazes listeners on the {@link GeometricalObject}s list
	 * @return {@link GeometricalObject}s list
	 */
	private JList<GeometricalObject> initList() {
		JList<GeometricalObject> list = new JList<>(new DrawingObjectListModel(drawingModel));

		list.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent key) {
				if (list.isSelectionEmpty()) return;
				GeometricalObject selected = list.getSelectedValue();
				switch (key.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					drawingModel.remove(selected);
					break;
				case KeyEvent.VK_PLUS:
					drawingModel.changeOrder(selected, -1);
					list.setSelectedValue(selected, true);
					break;
				case KeyEvent.VK_MINUS:
					drawingModel.changeOrder(selected, 1);
					list.setSelectedValue(selected, true);
					break;
				default:
					break;
				}
			}
		});

		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) return;
				doubleClicked(list.getSelectedValue());
			}

		});

		return list;
	}

	/**
	 * Invokes {@link GeometricalObjectEditor} for the clicked {@link GeometricalObject}
	 */
	private void doubleClicked(GeometricalObject clicked) {
		GeometricalObjectEditor editor = clicked.createGeometricalObjectEditor();

		while (true) {
			if (JOptionPane.showConfirmDialog(this, editor, "Edit " + clicked,
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				try {
					editor.checkEditing();
					editor.acceptEditing();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage());
					continue;
				}
			}
			break;
		}
	}

	/**
	 * Sets the current tool.
	 *
	 * @param tool the new current tool
	 */
	private void setCurrentTool(Tool tool) {
		currentTool = tool;
		canvas.setCurrentTool(tool);
	}

	/**
	 * Method called at the beginning of the program.
	 * @param args - no arguments needed
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			(new JVDraw()).setVisible(true);
		});
	}

	/**
	 * Sets value that tracks if there is unsaved work to true when any objects changes or list is
	 * {@link DrawingModel} is modified
	 */
	private final DrawingModelListener savedListener = new DrawingModelListener() {

		@Override
		public void objectsRemoved(DrawingModel source, int index0, int index1) {
			unsaved = true;
		}

		@Override
		public void objectsChanged(DrawingModel source, int index0, int index1) {
			unsaved = true;
		}

		@Override
		public void objectsAdded(DrawingModel source, int index0, int index1) {
			unsaved = true;
		}
	};

	/**
	 * Opens document
	 */
	private final AbstractAction OPEN_DOCUMENT_ACTION = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		{
			this.putValue(Action.NAME, "Open");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (unsaved) {
				if (!saveWork()) return;
			}

			for (int i = 0, sz = drawingModel.getSize(); i < sz; ++i) {
				drawingModel.remove(drawingModel.getObject(0));
			}
			Path filepath = openFile("Open file");
			if (filepath == null) return;

			if (!Files.isReadable(filepath)) {
				JOptionPane.showMessageDialog(JVDraw.this,
						"Datoteka " + filepath.toAbsolutePath() + " ne postoji!", "Pogreska",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (loadDocument(filepath)) {
				loadedFile = filepath;
				unsaved = false;
			}
		}

	};

	/**
	 * Saves document
	 */
	private final AbstractAction SAVE_DOCUMENT_ACTION = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		{
			this.putValue(Action.NAME, "Save");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			saveDocument();
		}
	};

	/**
	 * Save as document
	 */
	private final AbstractAction SAVEAS_DOCUMENT_ACTION = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		{
			this.putValue(Action.NAME, "Save as");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			saveAsDocument();
		}
	};

	/**
	 * Exits the application
	 */
	private final AbstractAction EXIT_ACTION = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		{
			this.putValue(Action.NAME, "Exit");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (unsaved) {
				if (!saveWork()) return;
			}
			dispose();
		}
	};

	/**
	 * Exports drawn image
	 */
	private final AbstractAction EXPORT_ACTION = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		{
			this.putValue(Action.NAME, "Export");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (drawingModel.getSize() == 0) {
				JOptionPane.showMessageDialog(JVDraw.this, "Cannot export image with zero objects");
				return;
			}
			BufferedImage image = Util.generateImage(drawingModel);
			Path imagePath = Util.chooseImageFile(JVDraw.this);
			String extension = imagePath.toString()
					.substring(imagePath.toString().lastIndexOf(".") + 1);

			try {
				ImageIO.write(image, extension, imagePath.toFile());
			} catch (IOException e) {
				errorDialog(JVDraw.this, "Error while exporting image");
				return;
			}
			JOptionPane.showMessageDialog(JVDraw.this, "Image exported", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Saves document to the file the application opened or if that file does not yet exists prompts
	 * user to provide one.
	 * @return true if user chose to saved the document, else false if user canceled the action
	 */
	private boolean saveDocument() {
		if (loadedFile == null) return saveAsDocument();
		writeObjects(loadedFile);
		return true;
	}

	/**
	 * Saves document to the file user provided.
	 * @return true if user chose to save the document, esle false if user canceled the action
	 */
	private boolean saveAsDocument() {
		loadedFile = Util.selectFile(JVDraw.this, "Save as file");
		if (loadedFile == null) return false;
		writeObjects(loadedFile);
		return true;
	}

	/**
	 * Writes doc representations of {@link GeometricalObject}s from the {@link DrawingModel} to the
	 * provided file
	 * @param loadedFile - file to write in
	 */
	private void writeObjects(Path loadedFile) {
		try (BufferedWriter wr = new BufferedWriter(new FileWriter(loadedFile.toFile()))) {
			for (int i = 0, sz = drawingModel.getSize(); i < sz; ++i) {
				wr.write(drawingModel.getObject(i).toDoc());
				wr.write("\n");
			}
		} catch (IOException ex) {
			errorDialog(this, "Error while saving document");
		}

		unsaved = false;
	}

	/**
	 * Prompts user to choose the file to open and returns that file.
	 * @param message - message to display
	 * @return - null if user canceled the action, else a file user chose to open
	 */
	private Path openFile(String message) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(message);
		if (fc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return fc.getSelectedFile().toPath();
	}

	/**
	 * Loads the JVD document into the app, adding parsed objects into the {@link DrawingModel}
	 * @param filePath
	 * @return
	 */
	private boolean loadDocument(Path filePath) {
		List<String> lines;
		try {
			lines = Files.readAllLines(filePath);
		} catch (IOException ex) {
			errorDialog(this, "File cannot be opened");
			return false;
		}

		for (String line : lines) {
			if (line.isEmpty()) continue;
			
			GeometricalObject parsed = null;
			try {
				parsed = Util.parseStringLine(this, line);
			} catch (IllegalArgumentException|IndexOutOfBoundsException ex) {
				ex.printStackTrace();
				errorDialog(this, "Invalid line: " + line);
			}

			if (parsed == null) return false;
			drawingModel.add(parsed);
		}

		return true;
	}

	/**
	 * Prompts user if he would like to save his work and saves it if user agrees.
	 * @return false if user canceled the action, else true
	 */
	private boolean saveWork() {
		int choice = JOptionPane.showConfirmDialog(JVDraw.this, "Would you like to save your work?",
				"Work unsaved", JOptionPane.YES_NO_CANCEL_OPTION);

		if (choice == JOptionPane.CANCEL_OPTION) return false;
		if (choice == JOptionPane.YES_OPTION) {
			return saveDocument();
		}
		return true;
	}

}

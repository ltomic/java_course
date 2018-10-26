package hr.fer.zemris.java.gui.layouts.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.CalcLayout.RCPosition;

/**
 * Demonstrates the usage of the {@link CalcLayout}. Creates a window with one panel whose layout is
 * {@link CalcLayout} and puts a few label on that panel.
 * @author ltomic
 *
 */
public class CalcLayoutDemo extends JFrame {

	/** Serial version **/
	private static final long serialVersionUID = 1L;

	/** Default color of labels **/
	private static final Color DEFAULT_COLOR = new Color(60, 110, 224);
	/** Text font **/
	private static final Font FONT = new Font("Courier New", Font.BOLD, 20);

	/** Pad for the layout **/
	private static final int PAD = 3;

	/**
	 * Constructs a {@link CalcLayoutDemo} and initializes the windows and GUI.
	 */
	public CalcLayoutDemo() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("CalcLayout Demo");
		initGUI();
		pack();
	}

	/**
	 * Initializes GUI. Creates a panel with {@link CalcLayout} layout and puts a few labels on it.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JPanel p = new JPanel(new CalcLayout(PAD));

		newLabel(p, "x", 1, 1);
		newLabel(p, "y", 2, 3);
		newLabel(p, "z", 2, 7);
		newLabel(p, "w", 4, 2);
		newLabel(p, "a", 4, 5);
		newLabel(p, "b", 4, 7);

		p.setBounds(0, 0, 500, 500);
		getContentPane().add(p);
	}

	/**
	 * Creates a new label with given text and position in {@link CalcLayout} and puts it on a
	 * provided {@link JPanel}
	 * @param p - panel to put on a new label
	 * @param text - text on a new label
	 * @param x - x position in {@link CalcLayout} of a new label
	 * @param y - y position in {@link CalcLayout} of a new label
	 */
	private void newLabel(JPanel p, String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setFont(FONT);
		label.setBackground(DEFAULT_COLOR);
		p.add(label, new RCPosition(x, y));
	}

	/**
	 * Method called at the beginning of the program. No arguments expected.
	 * @param args - no arguments expected
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CalcLayoutDemo prozor = new CalcLayoutDemo();
			prozor.setVisible(true);
		});

	}
}

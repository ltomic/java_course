package hr.fer.zemris.java.gui.calc;

import static hr.fer.zemris.java.gui.calc.DoubleOperators.ADD;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.ARCCOS;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.ARCCTG;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.ARCSIN;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.ARCTAN;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.COS;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.CTG;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.DIVIDE;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.LN;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.LOG;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.MULTIPLY;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.NTH_ROOT;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.POW;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.POW_10;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.POW_E;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.RECIPROCAL;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.SIN;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.SUBTRACT;
import static hr.fer.zemris.java.gui.calc.DoubleOperators.TAN;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.CalcLayout.RCPosition;

/**
 * Displays a simple calculator with mathematical operations and a stack. Numbers are entered digit
 * by digit using the buttons in the window of the calculator. Calculator does not evaluate
 * expression i.e. it does a single operation at a time. For example to calculate 12 + 15 - 10 the
 * user must press the following sequence of buttons on the calculator window : 1, 2, +, 1, 5, -, 1,
 * 0, = . "clr" button clears the current value written on the calculator screen. "res" button
 * clears the pending operation, the active operand and the current value.
 * @author ltomic
 *
 */
public class Calculator extends JFrame {

	/** Serial version **/
	private static final long serialVersionUID = 1L;

	/** Button color **/
	private static final Color BUTTON_COLOR = new Color(60, 110, 224);
	/** Text font **/
	private static final Font FONT = new Font("Courier New", Font.BOLD, 20);
	/** Button border **/
	private static final Border BUTTON_BORDER = new EmptyBorder(10, 1, 10, 1);
	/** Screen color **/
	private static final Color SCREEN_COLOR = new Color(244, 191, 66);
	/** Initial window location **/
	private static final Point INIT_LOCATION = new Point(100, 100);
	/** Window title **/
	private static final String TITLE = "Calculator";
	/** Welcome message printed on the calculator screen **/
	private static final String WELCOME_MESSAGE = "Hello!";

	/** Calculator screen position in CalcLayout **/
	private static final RCPosition SCREEN_POSITION = new RCPosition(1, 1);

	/** Add operation button position in CalcLayout **/
	private static final RCPosition ADD_BUTTON_POSITION = new RCPosition(5, 6);
	/** Subtract operation button position in CalcLayout **/
	private static final RCPosition SUBTRACT_BUTTON_POSITION = new RCPosition(4, 6);
	/** Multiply operation button position in CalcLayout **/
	private static final RCPosition MULTIPLY_BUTTON_POSITION = new RCPosition(3, 6);
	/** Divide operation button position in CalcLayout **/
	private static final RCPosition DIVIDE_BUTTON_POSITION = new RCPosition(2, 6);
	/** Clear button position in CalcLayout **/
	private static final RCPosition CLEAR_BUTTON_POSITION = new RCPosition(1, 7);
	/** Reset button position in CalcLayout **/
	private static final RCPosition RESET_BUTTON_POSITION = new RCPosition(2, 7);
	/** Swapsign button position in CalcLayout **/
	private static final RCPosition SWAPSIGN_BUTTON_POSITION = new RCPosition(5, 4);
	/** Insert decimal point button position in CalcLayout **/
	private static final RCPosition DECIMAL_POINT_BUTTON_POSITION = new RCPosition(5, 5);
	/** Push value to stack button position in CalcLayout **/
	private static final RCPosition PUSH_STACK_BUTTON_POSITION = new RCPosition(3, 7);
	/** Pop value from stack button position in CalcLayout **/
	private static final RCPosition POP_STACK_BUTTON_POSITION = new RCPosition(4, 7);
	/** Invert operations button position in CalcLayout **/
	private static final RCPosition INVERT_OPERATIONS_BUTTON_POSITION = new RCPosition(5, 7);
	/** Power operation button position in CalcLayout **/
	private static final RCPosition POW_BUTTON_POSITION = new RCPosition(5, 1);
	/** Sine operation button position in CalcLayout **/
	private static final RCPosition SIN_BUTTON_POSITION = new RCPosition(2, 2);
	/** Cosine operation button position in CalcLayout **/
	private static final RCPosition COS_BUTTON_POSITION = new RCPosition(3, 2);
	/** Tangent operation button position in CalcLayout **/
	private static final RCPosition TAN_BUTTON_POSITION = new RCPosition(4, 2);
	/** Cotangent operation button position in CalcLayout **/
	private static final RCPosition CTG_BUTTON_POSITION = new RCPosition(5, 2);
	/** Reciprocal operation button position in CalcLayout **/
	private static final RCPosition RECIPROCAL_BUTTON_POSITION = new RCPosition(2, 1);
	/** Logarithm in base 10 operation button position in CalcLayout **/
	private static final RCPosition LOG_BUTTON_POSITION = new RCPosition(3, 1);
	/** Natural logarithm operation button position in CalcLayout **/
	private static final RCPosition LN_BUTTON_POSITION = new RCPosition(4, 1);
	/** Identity operation button position in CalcLayout **/
	private static final RCPosition ID_BUTTON_POSITION = new RCPosition(1, 6);

	/** Simple calculator model used to perform operations **/
	private CalcModelImpl myModel = new CalcModelImpl();
	/** Stack used to save number **/
	private Stack<Double> stack = new Stack<>();
	/** Main container of the created window **/
	private Container cp;

	/**
	 * Initializes the window and calls the GUI initialization
	 */
	public Calculator() {
		setLocation(INIT_LOCATION);
		setTitle(TITLE);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	/**
	 * Initializes GUI and calls the calculator screen, digit buttons, operation buttons and
	 * miscellaneous buttons initialization.
	 */
	private void initGUI() {
		JPanel contentPanel = new JPanel();
		setContentPane(contentPanel);
		cp = getContentPane();

		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(Color.WHITE);
		cp.setLayout(new CalcLayout(7));

		initCalculatorScreen();
		initDigitButtons();
		initOperationButtons();
		initMiscButtons();
	}

	private final ActionListener ID = new ActionListener() {	
		@Override
		public void actionPerformed(ActionEvent event) {
			if (myModel.isActiveOperandSet()) {
				myModel.setValue(myModel.getPendingBinaryOperation().applyAsDouble(
						myModel.getActiveOperand(), myModel.getValue()));
			}
			
			myModel.clearActiveOperand();
			myModel.setPendingBinaryOperation(null);
		}
	};

	/**
	 * Initializes miscellaneous buttons
	 */
	private void initMiscButtons() {
		initButton("=", ID, ID_BUTTON_POSITION);

		initButton("clr", l -> myModel.clear(), CLEAR_BUTTON_POSITION);
		initButton("res", l -> myModel.clearAll(), RESET_BUTTON_POSITION);
		initButton("+/-", l -> myModel.swapSign(), SWAPSIGN_BUTTON_POSITION);
		initButton(".", l -> myModel.insertDecimalPoint(), DECIMAL_POINT_BUTTON_POSITION);

		initButton("push", l -> {
			stack.push(myModel.getValue());
			myModel.clear();
		}, PUSH_STACK_BUTTON_POSITION);
		initButton("pop", l -> {
			if (stack.empty()) {
				errorMessage("Stack is empty");
			} else {
				myModel.setValue(stack.pop());
			}
		}, POP_STACK_BUTTON_POSITION);

		JCheckBox invertOperationsBox = new JCheckBox("Inv");
		invertOperationsBox.addActionListener(l -> myModel.invertOperations());
		initButtonProperties(invertOperationsBox);
		getContentPane().add(invertOperationsBox, INVERT_OPERATIONS_BUTTON_POSITION);
	}

	/**
	 * Initializes calculator button with default properties and provided text,
	 * {@link ActionListener} and position in {@link CalcLayout}.
	 * @param text - text written on the button
	 * @param listener - {@link ActionListener} called on button click
	 * @param position - position of the button in {@link CalcLayout}
	 */
	private void initButton(String text, ActionListener listener, RCPosition position) {
		JButton button = new JButton(text);

		initButtonProperties(button);

		button.addActionListener(listener);
		cp.add(button, position);
	}

	/**
	 * Initializes buttons on which digits are written.
	 */
	private void initDigitButtons() {
		JButton numberButton = new DigitButton(0, myModel);
		initButtonProperties(numberButton);
		getContentPane().add(numberButton, new RCPosition(5, 3));

		for (int i = 0; i < 9; ++i) {
			numberButton = new DigitButton(i + 1, myModel);
			initButtonProperties(numberButton);
			getContentPane().add(numberButton, new RCPosition(i / 3 + 2, i % 3 + 3));
		}
	}

	/**
	 * Sets the default calculator button properties to the provided {@link JComponent}.
	 * @param component - {@link JComponent} whose properties should be set to default calculator
	 *            button properties
	 */
	private void initButtonProperties(JComponent component) {
		component.setOpaque(true);
		component.setBackground(BUTTON_COLOR);
		component.setFont(FONT);
		component.setBorder(BUTTON_BORDER);
	}

	/**
	 * Initializes calculator screen.
	 */
	private void initCalculatorScreen() {
		JLabel calcScreen = new JLabel(WELCOME_MESSAGE, SwingConstants.RIGHT);
		calcScreen.setBackground(SCREEN_COLOR);
		calcScreen.setOpaque(true);
		calcScreen.setFont(FONT);
		calcScreen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		myModel.addCalcValueListener(l -> {
			calcScreen.setText(l.toString());
		});

		getContentPane().add(calcScreen, SCREEN_POSITION);
	}

	/**
	 * Initializes mathematical operations buttons.
	 */
	private void initOperationButtons() {
		initBinaryOperationButton("+", ADD_BUTTON_POSITION, ADD);
		initBinaryOperationButton("-", SUBTRACT_BUTTON_POSITION, SUBTRACT);
		initBinaryOperationButton("*", MULTIPLY_BUTTON_POSITION, MULTIPLY);
		initBinaryOperationButton("/", DIVIDE_BUTTON_POSITION, DIVIDE);

		initBinaryOperationButton("x^n", POW_BUTTON_POSITION, POW, NTH_ROOT);

		initUnaryOperationButton("sin", SIN_BUTTON_POSITION, SIN, ARCSIN);
		initUnaryOperationButton("cos", COS_BUTTON_POSITION, COS, ARCCOS);

		initUnaryOperationButton("tan", TAN_BUTTON_POSITION, TAN, ARCTAN);
		initUnaryOperationButton("ctg", CTG_BUTTON_POSITION, CTG, ARCCTG);

		initUnaryOperationButton("1/x", RECIPROCAL_BUTTON_POSITION, RECIPROCAL);
		initUnaryOperationButton("log", LOG_BUTTON_POSITION, LOG, POW_10);
		initUnaryOperationButton("ln", LN_BUTTON_POSITION, LN, POW_E);
	}

	/**
	 * Displays an error dialog with the provided message
	 * @param message - message to display
	 */
	private void errorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Initializes a button whose operation is unary and has no inverse(or inverse should be same as
	 * the operation itself) with provided symbol, position in {@link CalcLayout} and
	 * {@link DoubleUnaryOperator}.
	 * @param symbol - text to be written on button
	 * @param position - position of the button in {@link CalcLayout}
	 * @param op - {@link DoubleUnaryOperator} to be called on button click
	 */
	private void initUnaryOperationButton(String symbol, RCPosition position,
			DoubleUnaryOperator op) {
		initUnaryOperationButton(symbol, position, op, op);
	}

	/**
	 * Initializes a button whose operation is unary with provided symbol, position in
	 * {@link CalcLayout}, {@link DoubleUnaryOperator} and its inverse.
	 * @param symbol - text to be written on button
	 * @param position - position of the button in {@link CalcLayout}
	 * @param op - {@link DoubleUnaryOperator} to be called on button click
	 * @param invOp - {@link DoubleUnaryOperator} to be called when operations are inverted
	 */
	private void initUnaryOperationButton(String symbol, RCPosition position,
			DoubleUnaryOperator op, DoubleUnaryOperator invOp) {
		initButton(symbol, new CalcUnaryOperation(myModel, op, invOp), position);
	}

	/**
	 * Initializes a button whose operation is binary and has no inverse(or inverse should be same
	 * as the operation itself) with provided symbol, position in {@link CalcLayout} and
	 * {@link DoubleBinaryOperator}.
	 * @param symbol - text to be written on button
	 * @param position - position of the button in {@link CalcLayout}
	 * @param op - {@link DoubleBinaryOperator} to be called on button click
	 */
	private void initBinaryOperationButton(String symbol, RCPosition position,
			DoubleBinaryOperator op) {
		initBinaryOperationButton(symbol, position, op, op);
	}

	/**
	 * Initializes a button whose operation is unary with provided symbol, position in
	 * {@link CalcLayout}, {@link DoubleBinaryOperator} and its inverse.
	 * @param symbol - text to be written on button
	 * @param position - position of the button in {@link CalcLayout}
	 * @param op - {@link DoubleBinaryOperator} to be called on button click
	 * @param invOp - {@link DoubleBinaryOperator} to be called when operations are inverted
	 */
	private void initBinaryOperationButton(String symbol, RCPosition position,
			DoubleBinaryOperator op, DoubleBinaryOperator invOp) {
		initButton(symbol, new CalcBinaryOperation(myModel, op, invOp), position);
	}

	/**
	 * Method called at the beginning of the program. No arguments are used.
	 * Method invokes the creation of the calculator windows and GUI.
	 * @param args - no arguments are needed
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Calculator frame = new Calculator();
			frame.pack();
			frame.setVisible(true);
		});
	}
}

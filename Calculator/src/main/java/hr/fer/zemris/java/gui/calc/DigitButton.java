package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Implements a button on the {@link Calculator} that represents a digit button.
 * The only difference between a {@link JButton} and this object is this objects
 * adds a {@link ActionListener} to the given {@link CalcModel} that adds the
 * digit a certain instance of {@link DigitButton} represents.
 * @author ltomic
 *
 */
public class DigitButton extends JButton {
	
	/** Serial version **/
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link DigitButton} with provided digit and {@link CalcModel}.
	 * @param digit - digit to add to {@link CalcModel} when this button is clicked on
	 * @param calculator - {@link CalcModel} to add the digit this button represents.
	 */
	public DigitButton(int digit, CalcModel calculator) {
		super(String.valueOf(digit));
		
		this.addActionListener(e -> {
			calculator.insertDigit(digit);
		});
	}

}

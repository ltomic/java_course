package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Models {@link DoubleUnaryOperator} as a {@link ActionListener} of a {@link JButton} of a
 * {@link Calculator} that uses {@link MyCalcModel}.
 * @author ltomic
 *
 */
public class CalcUnaryOperation implements ActionListener {
	
	/** Calculator model used **/
	private CalcModelImpl model;
	/** Operator to use **/
	private DoubleUnaryOperator op;
	/** Inverse operator to use when operations in model are inverted **/
	private DoubleUnaryOperator invOp;

	/**
	 * Constructs a {@link CalcUnaryOperation} with provided {@link MyCalcModel}, operation
	 * and inverted operation.
	 * @param model - calculator model
	 * @param op - operation
	 * @param invOp - inverse operation
	 */
	public CalcUnaryOperation(CalcModelImpl model, DoubleUnaryOperator op,
			DoubleUnaryOperator invOp) {
		super();
		this.model = model;
		this.op = op;
		this.invOp = invOp;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		DoubleUnaryOperator currentOp = model.isOperationInverted() ? invOp : op;
		model.setValue(currentOp.applyAsDouble(model.getValue()));
	}

}

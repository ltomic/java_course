package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Models {@link DoubleBinaryOperator} as a {@link ActionListener} of a {@link JButton} of a
 * {@link Calculator} that uses {@link MyCalcModel}.
 * @author ltomic
 *
 */
public class CalcBinaryOperation implements ActionListener {
	
	/** Calculator model used **/
	private CalcModelImpl model;
	/** Operator to use **/
	private DoubleBinaryOperator op;
	/** Inverse operator to use when operations in model are inverted **/
	private DoubleBinaryOperator invOp;

	/**
	 * Constructs a {@link CalcBinaryOperation} with provided {@link MyCalcModel}, operation
	 * and inverted operation.
	 * @param model - calculator model
	 * @param op - operation
	 * @param invOp - inverse operation
	 */
	public CalcBinaryOperation(CalcModelImpl model, DoubleBinaryOperator op,
			DoubleBinaryOperator invOp) {
		super();
		this.model = model;
		this.op = op;
		this.invOp = invOp;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		DoubleBinaryOperator currentOp = model.isOperationInverted() ? invOp : op;
		if (model.isActiveOperandSet() == false) {
			model.setActiveOperand(model.getValue());
			model.setPendingBinaryOperation(currentOp);
		} else {
			DoubleBinaryOperator pendingOp = model.getPendingBinaryOperation();
			double currentValue = model.getValue();

			try {
				if (pendingOp == null) {
					model.setActiveOperand(
							currentOp.applyAsDouble(model.getActiveOperand(), currentValue));
				} else {
					model.setActiveOperand(
							pendingOp.applyAsDouble(model.getActiveOperand(), currentValue));
				}
			} catch (IllegalArgumentException ex) {
				JButton button = (JButton) action.getSource();
				JOptionPane.showMessageDialog(button, ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		model.setPendingBinaryOperation(currentOp);
		model.clear();
	}

}

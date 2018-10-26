package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of the {@link CalcModel}. Additional feature is inverted operation support.
 * @author ltomic
 *
 */
public class CalcModelImpl implements CalcModel {
	
	private static final double PRECISION = 1E-9;
	
	/** Are certain operations inverted **/
	private boolean operationsInverted = false;
	
	/** Current value on the calculator screen */
	private String value;
	/** Current active operand i.e. first operand of the operation **/
	private Double activeOperand;
	/** Current active binary operator **/
	private DoubleBinaryOperator activeOperator;
	/** Storage of value listeners **/
	private List<CalcValueListener> listeners = new ArrayList<>();
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all registered value listeners about the value change
	 */
	private void notifyListeners() {
		listeners.forEach((listener) -> listener.valueChanged(this));
	}
	
	@Override
	public String toString() {
		if (value == null || value.length() == 0) return new String("0");
		
		return value;
	}

	@Override
	public double getValue() {
		if (value == null || value.length() == 0) return 0.0;
		
		return Double.parseDouble(value);
	}

	@Override
	public void setValue(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value)) return;
		
		Double doubleValue = Double.valueOf(value);
		this.value = Math.abs(Math.floor(value)-value) < PRECISION ? 
				String.valueOf(doubleValue.longValue()) :  String.valueOf(value);
		notifyListeners();
	}

	@Override
	public void clear() {
		value = null;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		value = null;
		activeOperand = null;
		activeOperator = null;
		notifyListeners();
	}

	@Override
	public void swapSign() {
		if (value == null) return;
		if (!((value.length() > 0 && !value.contains(".")) || value.length() > 1)) return;
		
		value = value.charAt(0) == '-' ? value.substring(1) : ("-" + value);
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() {
		if (value == null) value = new String("0");
		if (value.contains(".")) return;
		value += '.';
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit) {
		if (! (0 <= digit && digit <= 9)) {
			throw new IllegalArgumentException("Invalid digit value given : " + digit);
		}
		if (value == null) value = new String("");
		
		if (digit == 0 && !value.contains(".") && (value.length() == 1 && value.charAt(0) == '0'))
			return;
		if (digit != 0 && !value.contains(".") && getValue() == 0) {
			value = new String("");
		}

		if (value.length() >= 308) return;
		
		value += digit;
		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	@Override
	public double getActiveOperand() {
		if (!isActiveOperandSet()) throw new IllegalStateException("There is no active operand");
		
		return activeOperand.doubleValue();
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = Double.valueOf(activeOperand);
		notifyListeners();
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = null;
		notifyListeners();
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return activeOperator;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		activeOperator = op;
		notifyListeners();
	}
	
	/**
	 * Changes the flag that some operations are inverted
	 */
	public void invertOperations() {
		this.operationsInverted = !operationsInverted;
	}
	
	/**
	 * Returns the flag that some operations are inverted
	 * @return the flag that some operations are inverted
	 */
	public boolean isOperationInverted() {
		return operationsInverted;
	}

}

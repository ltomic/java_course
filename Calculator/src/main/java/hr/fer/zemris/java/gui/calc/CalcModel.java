package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

/**
 * Models a simple calculator that performs one operation at a time. It stores a current value as a
 * string onto which a digits are added by method and supports decimal values. Value observers are
 * supported via {@link CalcValueListener} class. The model remembers the last used binary operation
 * as a currently pending operation.
 * @author ltomic
 *
 */
public interface CalcModel {
	/**
	 * Adds a provided {@link CalcValueListener}.
	 * @param l - {@link CalcValueListener} to be added
	 */
	void addCalcValueListener(CalcValueListener l);

	/**
	 * Removes the provided {@link CalcValueListener}, if it does not exist among the added one
	 * nothing happens.
	 * @param l - {@link CalcValueListener} to remove
	 */
	void removeCalcValueListener(CalcValueListener l);

	/**
	 * Returns current value as a string.
	 * @return {@link String} of a current value
	 */
	String toString();

	/**
	 * Return current value as double
	 * @return current value
	 */
	double getValue();

	/**
	 * Sets current value to the provided one
	 * @param value - value to be set
	 */
	void setValue(double value);

	/**
	 * Erases current value. String of current value is null.
	 */
	void clear();

	/**
	 * Clears current value, active operand and pending binary operation.
	 */
	void clearAll();

	/**
	 * Changes the sign of the current value if current value is not null.
	 */
	void swapSign();

	/**
	 * Inserts a decimal point. If current value is null then current value becomes "0.".
	 */
	void insertDecimalPoint();

	/**
	 * Inserts a digit to the end of the current value.
	 * @param digit - digit to be inserted
	 */
	void insertDigit(int digit);

	/**
	 * Returns true if active operand is not null, else false
	 * @return true if active operand is not null, else false
	 */
	boolean isActiveOperandSet();

	/**
	 * Return current active operand. If current active operand is null,
	 * {@link IllegalStateException} is thrown
	 * @return current active operand
	 */
	double getActiveOperand();

	/**
	 * Sets current active operand to the provided value
	 * @param activeOperand - value to which the current active operand should be set
	 */
	void setActiveOperand(double activeOperand);

	/**
	 * Sets current active operand to null.
	 */
	void clearActiveOperand();

	/**
	 * Returns current pending binary operation;
	 * @return current pending binary operation.
	 */
	DoubleBinaryOperator getPendingBinaryOperation();

	/**
	 * Sets current pending binary operation to the provided one
	 * @param op - operation to which the current pending binary operation should be set
	 */
	void setPendingBinaryOperation(DoubleBinaryOperator op);
}
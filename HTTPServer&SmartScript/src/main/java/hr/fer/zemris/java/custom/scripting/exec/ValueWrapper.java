package hr.fer.zemris.java.custom.scripting.exec;

import java.util.Scanner;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Predicate;

/**
 * This class implements a wrapper around Object value. Value can even be null.
 * It also implements basic arithmetic operations : addition, subtraction,
 * multiplication and division, but they are supported only if arguments are one
 * of the following : double, integer or a String which can be parsed as a decimal number.
 * It also supports comparison on previously mentioned arguments.
 * @author ltomic
 *
 */
public class ValueWrapper {
	
	/**
	 * Value stored
	 */
	private Object value;
	
	/**
	 * Test if given object can be an Integer
	 */
	private static final Predicate<Object> canBeInteger = 
			t -> t == null || t.getClass() == Integer.class;

	/**
	 * Constructs a {@link ValueWrapper} with value
	 * @param value
	 */
	public ValueWrapper(Object value) {
		super();
		this.value = value;
	}
	
	/**
	 * Returns wrapped value
	 * @return wrapped value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets new value 
	 * @param value new value to be wrapped
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * This method returns a result of given operation op applyed on given arguments
	 * first and second. First and second must be : integer, double or String that
	 * can be parsed as a decimal number. Type of object returned depends of type of
	 * arguments given. If first and second arguments are Integers, returned type
	 * will be Integer.
	 * @param first
	 * @param second
	 * @param op
	 * @return
	 */
	public Number applyOperation(Object first, Object second, DoubleBinaryOperator op) {
		Number firstValue = toNumber(first);
		Number secondValue = toNumber(second);
		Number result = op.applyAsDouble(firstValue.doubleValue(), secondValue.doubleValue());
		
		if (canBeInteger.test(firstValue) && canBeInteger.test(secondValue))
			return (Integer)result.intValue();
		
		return result;
	}
	
	/**
	 * Adds given value to the stored value. Given value must be : integer, 
	 * double or String that can be parsed as a decimal number.
	 * @param incValue value to be added, integer, double or String that 
	 * 			can be parsed as a decimal number.
	 */
	public void add(Object incValue) {
		this.value = applyOperation(this.value, incValue, (left, right) -> left+right);
	}
	
	
	/**
	 * Subtracts given value to the stored value. Given value must be : integer, 
	 * double or String that can be parsed as a decimal number.
	 * @param decValue value to be subtracted, integer, double or String that 
	 * 			can be parsed as a decimal number.
	 */
	public void sub(Object decValue) {
		this.value = applyOperation(this.value, decValue, (left, right) -> left-right);
	}
	
	
	/**
	 * Multiplies given value by the stored value. Given value must be : integer, 
	 * double or String that can be parsed as a decimal number.
	 * @param mulValue value to be multiplied with, integer, double or String that 
	 * 			can be parsed as a decimal number.
	 */
	public void mul(Object mulValue) {
		this.value = applyOperation(this.value, mulValue, (left, right) -> left*right);
	}
	
	
	/**
	 * Divides stored value with the given value. Given value must be : integer, 
	 * double or String that can be parsed as a decimal number.
	 * @param divValue value to be divided with, integer, double or String that 
	 * 			can be parsed as a decimal number.
	 */
	public void div(Object divValue) {
		if (Math.abs(toNumber(divValue).doubleValue()) < 1E-6) 
			throw new ArithmeticException("Cannot divide by zero");

		this.value = applyOperation(this.value, divValue, (left, right) -> left/right);
	}

	/**
	 * Compares stored value with the given value.
	 * @param withValue value to be compared with, integer, double or String that 
	 * 			can be parsed as a decimal number.
	 * @return -1 if stored value is less then given value
	 * 			0 if stored value is equal to the given value
	 * 		   +1 if stored value is greater then given value
	 */
	public int numCompare(Object withValue) {
		double firstValue = toNumber(this.value).doubleValue();
		double secondValue  = toNumber(withValue).doubleValue();
		
		if (firstValue == secondValue) return 0;
		
		return firstValue < secondValue ? -1 : 1;
	}
	
	/**
	 * Parses a given object to a decimal number. If given an integer or double, returns 
	 * it. If given a String, it tries to parse it like a decimal number.
	 * @param value value to be parsed
	 * @return parsed value
	 */
	public static Number toNumber(Object value) {
		if (value == null) return Integer.valueOf(0);
		
		if (value.getClass() == String.class) return stringToNumber((String)value);

		if (value.getClass() == Integer.class) return (Number)value;
		if (value.getClass() == Double.class) return (Number)value;
		
		throw new RuntimeException("value must be one of the following types : String, " + 
				"Integer, Double. Given: " + value.getClass());
	}
	
	/**
	 * Parses a string to a number
	 * @param value value to be parsed
	 * @return parsed value
	 */
	private static Number stringToNumber(String value) {
		try (Scanner scanner = new Scanner(value)) {
			if (scanner.hasNextInt()) return scanner.nextInt();
			if (scanner.hasNextBigDecimal()) return scanner.nextBigDecimal().doubleValue();
			throw new RuntimeException("Operation called on string that is not a number."
					+ "Given :" + value);
		}
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
}

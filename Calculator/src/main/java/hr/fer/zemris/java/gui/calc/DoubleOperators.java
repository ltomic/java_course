package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Defines mathematical operators used in {@link Calculator};
 * @author ltomic
 *
 */
public class DoubleOperators {
	
	/** Addition operator **/
	public static final DoubleBinaryOperator ADD = (a, b) -> a + b;
	/** Subtraction operator **/
	public static final DoubleBinaryOperator SUBTRACT = (a, b) -> a - b;
	/** Multiplication operator **/
	public static final DoubleBinaryOperator MULTIPLY = (a, b) -> a * b;
	/** Division operator **/
	public static final DoubleBinaryOperator DIVIDE = (a, b) -> {
		if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
		return a / b;
	};

	/** Power operator **/
	public static final DoubleBinaryOperator POW = (a, b) -> Math.pow(a, b);
	/** Nth root operator **/
	public static final DoubleBinaryOperator NTH_ROOT = (a, b) -> {
		if (b == 0) throw new IllegalArgumentException("Cannot calculate 0 root");
		return Math.pow(a, 1 / b);
	};

	/** Sine operator **/
	public static final DoubleUnaryOperator SIN = a -> Math.sin(a);
	/** Arcsine operator **/
	public static final DoubleUnaryOperator ARCSIN = a -> {
		double value = Math.asin(a);
		if (value == Double.NaN)
			throw new IllegalArgumentException("arcsin expects a value between -1 and 1");
		return value;
	};

	/** Cosine operator **/
	public static final DoubleUnaryOperator COS = a -> Math.cos(a);
	/** Arccosine operator **/
	public static final DoubleUnaryOperator ARCCOS = a -> {
		double value = Math.acos(a);
		if (value == Double.NaN)
			throw new IllegalArgumentException("arccos expects a value between -1 and 1");
		return value;
	};

	/** Tangent operator **/
	public static final DoubleUnaryOperator TAN = a -> Math.tan(a);
	/** Arctangent operator **/
	public static final DoubleUnaryOperator ARCTAN = a -> Math.atan(a);

	/** Cotangent operator **/
	public static final DoubleUnaryOperator CTG = a -> {
		double value = Math.tan(a);
		if (value == 0) throw new IllegalArgumentException("Invalid argument for ctg function");
		return 1 / value;
	};
	/** Arccotangent operator **/
	public static final DoubleUnaryOperator ARCCTG = a -> Math.PI / 2 - Math.atan(a);

	/** Reciprocal operator **/
	public static final DoubleUnaryOperator RECIPROCAL = a -> {
		if (a == 0) throw new IllegalArgumentException("Cannot divide by 0");
		return 1 / a;
	};

	/** Logarithm in base 10 operator **/
	public static final DoubleUnaryOperator LOG = a -> {
		double value = Math.log10(a);
		if (value == Double.NaN)
			throw new IllegalArgumentException("Value should be a positive number");
		return value;
	};
	/** Exponential in base 10 operator **/
	public static final DoubleUnaryOperator POW_10 = a -> Math.pow(10, a);

	/** Natural logarithm operator **/
	public static final DoubleUnaryOperator LN = a -> {
		double value = Math.log(a);
		if (value == Double.NaN)
			throw new IllegalArgumentException("Value should be a positive number");
		return value;
	};
	/** Exponential operator **/
	public static final DoubleUnaryOperator POW_E = a -> Math.pow(Math.E, a);
}

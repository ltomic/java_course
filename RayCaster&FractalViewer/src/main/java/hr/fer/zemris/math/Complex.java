package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

public class Complex {

	public static long instanceCounter = 0;
	/**
	 * Real component
	 */
	private double real;
	/**
	 * Imaginary component
	 */
	private double imag;
	/**
	 * Minimal difference for two number that are not equal
	 */
	private static final double calcPrecision = 1E-6;

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Default constructor. Creates (0, 0).
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Creates complex number (real, imaginary).
	 * 
	 * @param real - real component of complex number
	 * @param imaginary - imaginary component of complex number
	 */

	public Complex(double real, double imaginary) {
		this.real = real;
		this.imag = imaginary;
		instanceCounter++;
	}

	/**
	 * Computes the module of this Complex
	 * 
	 * @return module of this complex number
	 */
	public double module() {
		return Math.sqrt(real * real + imag * imag);
	}

	/**
	 * Creates a complex number from given magnitude and angle. Magnitude should be a non-negative
	 * number and angle should be between 0 and 2PI, else exception is thrown. Components are
	 * calculated with following formulas: real = magnitude*cos(angle), imaginary =
	 * magnitude*sin(angle)
	 * 
	 * @param magnitude magnitude of complex number in polar form
	 * @param angle counterclockwise angle spanned by positive real axis and ray connecting (0, 0)
	 *            and point representing the complex number
	 * @return Complex object that is represented in polar form by arguments
	 * @throws IllegalArgumentException if magnitude is negative or angle isn't between 0 and 2*pi
	 */
	public static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
		if (magnitude < 0) throw new IllegalArgumentException("Magnitude can't be negative");
		if (!(0 <= angle && angle <= 2 * Math.PI)) {
			throw new IllegalArgumentException("Angle must be between 0 and 2*PI");
		}

		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Adds two complex numbers, the one on which method is called and the other one given as
	 * argument.
	 * 
	 * @param c second complex number to be summed
	 * @return sum of two complex numbers
	 */
	public Complex add(Complex c) {
		return new Complex(this.real + c.real, this.imag + c.imag);
	}

	/**
	 * Subtracts two complex numbers, the one given as argument is subtracted from the one on which
	 * the method is called.
	 * 
	 * @param c second complex number to be subtracted
	 * @return result of subtraction
	 */
	public Complex sub(Complex c) {
		return new Complex(this.real - c.real, this.imag - c.imag);
	}

	/**
	 * Multiplies two complex numbers, the one given as argument and the one on which the method is
	 * called. Multiplication is performed in normal form.
	 * 
	 * @param c second complex number to be multiplied
	 * @return result of multiplication
	 */
	public Complex multiply(Complex c) {
		return new Complex(this.real * c.real - this.imag * c.imag,
				this.real * c.imag + this.imag * c.real);
	}

	/**
	 * Divides two complex numbers, the one on which the method is called is divided by the one
	 * given as argument.
	 * 
	 * @param c complex number the dividor
	 * @return result of division
	 */
	public Complex divide(Complex c) {
		double normal = c.real * c.real + c.imag * c.imag;
		return new Complex((this.real * c.real + this.imag * c.imag) / normal,
				(this.imag * c.real - this.real * c.imag) / normal);
		/*
		 * return fromMagnitudeAndAngle(this.getMagnitude()*c.getMagnitude(),
		 * (this.getNormalAngle()-c.getNormalAngle() + 2*Math.PI) % (2*Math.PI));
		 */
	}

	/**
	 * Returns negation of this complex number i.e. (-real, -imag)
	 * 
	 * @return negated complex number
	 */
	public Complex negate() {
		return new Complex(real * -1, imag * -1);
	}

	/**
	 * Returns this complex number scaled with the provided scalar
	 * 
	 * @param scalar - scalar value to multiply with
	 * @return new multiplied complex number
	 */
	public Complex scale(double scalar) {
		return new Complex(real * scalar, imag * scalar);
	}

	/**
	 * Return <b>counterclockwise</b> angle spanned by ray connecting origin and point representing
	 * the complex number and positive real axis. It is calculated using formula: - for values
	 * between 0 and PI : angle = atan2(imag, real) - for values between PI and 2*PI : angle =
	 * 2*PI+atan2(imag, real)
	 * 
	 * @return <b>counterclockwise</b>angle spanned by ray connecting origin and point representing
	 *         the complex number and positive real axis, number between 0 and 2*PI
	 */
	public double getAngle() {
		double result = Math.atan2(imag, real);
		if (result < 0) result = 2 * Math.PI + result;

		return result;
	}

	/**
	 * Return complex number raised to the power n. It is implemented as: z ^ n = r^n (cos(angle*n)
	 * + i * sin(angle*n)) where : z - the complex number, r - magnitude of complex number in polar
	 * form, angle - counterclockwise angle spanned by positive real axis and ray connecting (0, 0)
	 * and point representing the complex number
	 * 
	 * @param n exponent value, non-negative integer
	 * @return the result of rasing the complex number to the power exponent
	 * @throws IllegalArgumentException if exponent value if negative
	 */
	public Complex power(int n) {
		if (n < 0) throw new IllegalArgumentException("For negative powers call root function.");

		return fromMagnitudeAndAngle(Math.pow(this.module(), n),
				(this.getAngle() * n) % (2 * Math.PI));
	}

	/**
	 * Returns an array of all possible complex number that are n-th roots of a complex number. They
	 * are calculated as: z_i = r ^ (1/n) (cos((angle+2pi*i)/n) + i * sin((angle+2pi*i)/n)) where:
	 * z_i - i-th possible complex number, r - magnitude of complex number in polar form, angle -
	 * counterclockwise angle spanned by positive real axis and ray connecting (0, 0) and point
	 * representing the complex number
	 * 
	 * @param n degree of the root
	 * @return array of n-th roots of a complex number
	 */
	public List<Complex> root(int n) {
		if (n <= 0) throw new IllegalArgumentException("For negative root call power function.");

		ArrayList<Complex> values = new ArrayList<>(n);

		for (int i = 0; i < n; ++i) {
			values.add(fromMagnitudeAndAngle(Math.pow(this.module(), 1.0 / n),
					(this.getAngle() + 2 * i * Math.PI) / n));
		}
		;

		return values;
	}

	private static int index;
	private static final String invalidComplexNumberMessage = "Invalid format of complex number";

	/**
	 * Returns a complex number parsed from a string. Method accepts strings such as: "3.51",
	 * "-3.17", "-i2.71", "i", "1", "-2.71-i3.15".
	 * @param s - string to be parsed
	 * @return complex number parsed from a string
	 * @throws IllegalArgumentException if given string is empty or invalid string given
	 */
	public static Complex parse(String s) {
		index = 0;
		if (s.isEmpty()) throw new IllegalArgumentException(invalidComplexNumberMessage);
		int sign = 1;
		double imag = 0;
		double real = 0;
		if (s.charAt(index) == '-') {
			sign = -1;
			index++;
		}
		if (s.charAt(index) == 'i') {
			index++;
			imag = parseDecimalNumber(s, true) * sign;
			if (index < s.length()) throw new IllegalArgumentException(invalidComplexNumberMessage);
			return new Complex(0, imag);
		}

		real = parseDecimalNumber(s, false) * sign;
		skipWhitespace(s);
		if (index >= s.length()) return new Complex(real, imag);
		if (s.charAt(index) == '+') {
			sign = 1;
		} else if (s.charAt(index) == '-') {
			sign = -1;
		} else {
			throw new IllegalArgumentException(invalidComplexNumberMessage);
		}
		index++;
		skipWhitespace(s);
		if (index >= s.length()) throw new IllegalArgumentException(invalidComplexNumberMessage);
		if (s.charAt(index) != 'i') throw new IllegalArgumentException(invalidComplexNumberMessage);
		index++;
		imag = parseDecimalNumber(s, true) * sign;
		if (index < s.length()) throw new IllegalArgumentException(invalidComplexNumberMessage);
		
		return new Complex(real, imag);
	}

	private static double parseDecimalNumber(String s, boolean canBeEmpty) {
		int start = index;
		while (index < s.length() && (Character.isDigit(s.charAt(index)) || s.charAt(index) == '.'))
			index++;
		String number = s.substring(start, index);
		if (number.isEmpty()) {
			if (canBeEmpty) {
				return 1;
			} else {
				throw new IllegalArgumentException(invalidComplexNumberMessage);
			}
		}
		try {
			return Double.parseDouble(s.substring(start, index));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(invalidComplexNumberMessage);
		}
	}

	private static void skipWhitespace(String s) {
		while (index < s.length() && Character.isWhitespace(s.charAt(index)))
			index++;
	}

	/**
	 * Returns complex number in human-readable form.
	 * @return string in form of "(real, imag)"
	 */
	@Override
	public String toString() {
		return real + (imag < 0 ? "-i" : "+i") + imag;
	}

	/**
	 * Checks if two complex numbers are equal. Uses {@see #equals(double, double)}
	 * @param obj object to compare with
	 * @return true if equal(components can differ up to {@see #calcPrecision}, else false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Complex)) return false;

		Complex c = (Complex) obj;
		return this.equals(c.real, c.imag);
	}

	/**
	 * Checks if two complex numbers are equal. Second complex number is given in point form(x, y).
	 * Components of the number can differ up to {@see #calcPrecision}.
	 * @param real real component of a complex number to compare with
	 * @param imaginary imaginary component of a complex number to compare with
	 * @return true if components differ up to {@see #calcPrecision}, else false
	 */
	public boolean equals(double real, double imaginary) {
		return Math.abs(this.real - real) <= Complex.calcPrecision
				&& Math.abs(this.imag - imag) <= Complex.calcPrecision;
	}
}

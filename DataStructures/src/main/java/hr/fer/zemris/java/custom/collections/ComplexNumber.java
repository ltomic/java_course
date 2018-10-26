package hr.fer.zemris.java.custom.collections;

/**
 * This class implements complex numbers. The number is stored in point form.
 * The precision it ensures through one operation is 1E-7, as set in calcPrecision
 * variable. ComplexNumber objects are immutable.
 * Object can be created by giving the point form to constructor, polar form
 * <see>fromMagnitudeAndAngle</see> or by giving a string in format of complex number
 * <see>parse</see>
 *  
 * Operation between two complex numbers are used by calling an appropriate method
 * on either of the two numbers while the other one is given as argument :
 * Example of usage for multiplication:
 * 
 *  complexNumberA.mul(complexNumberB);
 * 
 * @author ltomic
 * @versio 1.0
 */

public class ComplexNumber {
	
	private double real;
	private double imag;
	final static double calcPrecision = 1E-7;
	
	/**
	 * Default constructor. Creates (0, 0).
	 */
	public ComplexNumber() {
		this(0, 0);
	}
	
	/**
	 * Creates complex number (real, imaginary).
	 * @param real - real component of complex number
	 * @param imaginary - imaginary component of complex number
	 */
	
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imag = imaginary;
	}
	
	/**
	 * Returns a complex number that only has real component(imaginary component is 0).
	 * @param real - real component of complex number
	 * @return ComplexNumber object with only a real component
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Returns a complex number that only has imaginary component(real component is 0).
	 * @param imaginary - imaginary component of complex number
	 * @return ComplexNumber object with only an imaginary component
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * Creates a complex number from given magnitude and angle. Magnitude should be a 
	 * non-negative number and angle should be between 0 and 2PI, else exception is thrown.
	 * Components are calculated with following formulas: 
	 * real = magnitude*cos(angle), imaginary = magnitude*sin(angle) 
	 * @param magnitude magnitude of complex number in polar form
	 * @param angle counterclockwise angle spanned by positive real axis and ray 
	 * 		  connecting (0, 0) and point representing the complex number 
	 * @return ComplexNumber object that is represented in polar form by arguments
	 * @throws IllegalArgumentException if magnitude is negative or angle isn't between
	 * 									0 and 2*pi
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		if (magnitude < 0) throw new IllegalArgumentException("Magnitude can't be negative");
		if (!(0 <= angle && angle <= 2*Math.PI)) {
			throw new IllegalArgumentException("Angle must be between 0 and 2*PI");
		}
		
		return new ComplexNumber(magnitude*Math.cos(angle), magnitude*Math.sin(angle));
	}
	
	/**
	 * Getter for real component of complex number.
	 * @return real component of complex number
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Getter for imaginary component of complex number.
	 * @return imaginary component of complex number
	 */
	public double getImaginary() {
		return imag;
	}
	
	/**
	 * Returns magnitude of a complex number in polar form. It is calculated using formula:
	 * magnitude = sqrt(real*real+imag*imag) 
	 * @return magnitude of a complex number, nonnegative number
	 */
	public double getMagnitude() {
		return Math.sqrt(real*real+imag*imag);
	}
	
	/**
	 * Return angle spanned by ray connecting origin and point representing
	 * the complex number and positive real axis. It is calculated using formula:
	 * angle = atan2(imag, real)
	 * @return angle spanned by ray connecting origin and point representing the
	 * 		   complex number and positive real axis, number between -PI and PI
	 */
	public double getNormalAngle() {
		return Math.atan2(imag, real);
	}
	
	/**
	 * Return <b>counterclockwise</b> angle spanned by ray connecting origin and 
	 * point representing the complex number and positive real axis.
	 * It is calculated using formula:
	 * - for values between 0 and PI : angle = atan2(imag, real)
	 * - for values between PI and 2*PI : angle = 2*PI+atan2(imag, real)
	 * @return <b>counterclockwise</b>angle spanned by ray connecting origin and 
	 * 		   point representing the complex number and positive real axis, 
	 * 		   number between 0 and 2*PI
	 */
	public double getAngle() {
		double result = Math.atan2(imag, real);
		if (result < 0) result = 2*Math.PI+result;
		
		return result;
	}
	
	/**
	 * Adds two complex numbers, the one on which method is called and
	 * the other one given as argument.
	 * @param c second complex number to be summed
	 * @return sum of two complex numbers
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.real+c.real, this.imag+c.imag);
	}
	
	/**
	 * Subtracts two complex numbers, the one given as argument is subtracted
	 * from the one on which the method is called.
	 * @param c second complex number to be subtracted
	 * @return result of subtraction
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(this.real-c.real, this.imag-c.imag);
	}
	
	/**
	 * Multiplies two complex numbers, the one given as argument and the one 
	 * on which the method is called. Multiplication is performed in normal form.
	 * @param c second complex number to be multiplied
	 * @return result of multiplication
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return new ComplexNumber(this.real*c.real-this.imag*c.imag, 
				this.real*c.imag+this.imag*c.real);
	}
	
	/**
	 * Divides two complex numbers, the one on which the method is called is
	 * divided by the one given as argument.
	 * @param c complex number the dividor
	 * @return result of division
	 */
	public ComplexNumber div(ComplexNumber c) {
		double normal = c.real*c.real+c.imag*c.imag;
		return new ComplexNumber((this.real*c.real+this.imag*c.imag)/normal,
				(this.imag*c.real-this.real*c.imag)/normal); 
/*		return fromMagnitudeAndAngle(this.getMagnitude()*c.getMagnitude(),
				(this.getNormalAngle()-c.getNormalAngle() + 2*Math.PI) % (2*Math.PI));*/
	}
	
	/**
	 * Return complex number raised to the power n. It is implemented as:
	 * z ^ n = r^n (cos(angle*n) + i * sin(angle*n)) where :
	 * z - the complex number, r - magnitude of complex number in polar form,
	 * angle - counterclockwise angle spanned by positive real axis and ray 
	 * 		   connecting (0, 0) and point representing the complex number 
	 * @param n exponent value, non-negative integer
	 * @return the result of rasing the complex number to the power exponent
	 * @throws IllegalArgumentException if exponent value if negative
	 */
	public ComplexNumber power(int n) {
		if (n < 0) throw new IllegalArgumentException("For negative powers call root function.");
		
		return fromMagnitudeAndAngle(Math.pow(this.getMagnitude(), n), 
									(this.getAngle()*n) % (2*Math.PI));
	}
	
	/**
	 * Returns an array of all possible complex number that are n-th roots
	 * of a complex number. They are calculated as:
	 * z_i = r ^ (1/n) (cos((angle+2pi*i)/n) + i * sin((angle+2pi*i)/n)) where:
	 * z_i - i-th possible complex number, r - magnitude of complex number in polar form,
	 * angle - counterclockwise angle spanned by positive real axis and ray 
	 * 		   connecting (0, 0) and point representing the complex number 
	 * @param n degree of the root
	 * @return array of n-th roots of a complex number
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) throw new IllegalArgumentException("For negative root call power function.");
		
		ComplexNumber[] values = new ComplexNumber[n];
		
		for (int i = 0; i < n; ++i) {
			values[i] = fromMagnitudeAndAngle(Math.pow(this.getMagnitude(), 1.0/n),
					(this.getAngle() + 2*i*Math.PI)/n);
		};
		
		return values;
	}
	
	/**
	 * Returns complex number in human-readable form.
	 * @return string in form of "(real, imag)"
	 */
	@Override
	public String toString() {
		return new String("(" + real + ", " + imag + ")");
	}
	
	/**
	 * Helper function for the parse method. Returns the index of the
	 * first character which is right of position iterator and is equal
	 * to '+', '-' or 'i' or end of string
	 * @param s string which is currently parsed
	 * @param iterator position in string 
	 * @return the index of the first character which is right of iterator
	 * 		   and is equal to '+', '-', 'i' or end of string
	 */
	private static int moveToEndOfNumber(String s, int iterator) {
		if (s.charAt(iterator) == '-' || s.charAt(iterator) == '+') iterator++;
		for (; iterator < s.length(); iterator++) {
			char current = s.charAt(iterator);
			if (current == '+' || current == '-' || current == 'i') break;
		}
		
		return iterator;
	}
	
	/**
	 * Returns a complex number parsed from a string. Method accepts strings such as:
	 *  "3.51", "-3.17", "-2.71i", "i", "1", "-2.71-3.15i".
	 * @param s string to be parsed
	 * @return complex number parsed from a string
	 * @throws IllegalArgumentException if given string is empty or invalid string given
	 */
	public static ComplexNumber parse(String s) {
		if (s.isEmpty()) throw new IllegalArgumentException("Empty string given");
		
		double real = 0;
		double imaginary = 0;

		if (s.equals("i")) return new ComplexNumber(0, 1);
		if (s.equals("-i")) return new ComplexNumber(0, -1);

		int iterator = moveToEndOfNumber(s, 0);
		try {
			real = Double.parseDouble(s.substring(0, iterator));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid string given.");
		}

		if (iterator == s.length()) return new ComplexNumber(real, 0);
		if (s.charAt(iterator) == 'i') {
			if (iterator + 1 != s.length()) 
				throw new IllegalArgumentException("Invalid string given.");
			imaginary = real;
			return new ComplexNumber(0, imaginary);
		}

		s = s.substring(iterator, s.length());

		if (s.equals("+i")) return new ComplexNumber(real, 1);
		if (s.equals("-i")) return new ComplexNumber(real, -1);

		iterator = moveToEndOfNumber(s, 1);
		if (iterator == s.length() || s.charAt(iterator) != 'i' || iterator + 1 != s.length()) {
			throw new IllegalArgumentException("Invalid string given.");
		}

		try {
			imaginary = Double.parseDouble(s.substring(0, iterator));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid string given.");
		}

		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Checks if two complex numbers are equal. Uses {@see #equals(double, double)}
	 * @param obj object to compare with
	 * @return true if equal(components can differ up to {@see #calcPrecision},
	 * 		   else false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof ComplexNumber)) return false;
		
		ComplexNumber c = (ComplexNumber)obj;
		return this.equals(c.real, c.imag);
	}
	
	/**
	 * Checks if two complex numbers are equal. Second complex number is given in
	 * point form(x, y). Components of the number can differ up to {@see #calcPrecision}.
	 * @param real real component of a complex number to compare with
	 * @param imaginary imaginary component of a complex number to compare with
	 * @return true if components differ up to {@see #calcPrecision}, else false
	 */
	public boolean equals(double real, double imaginary) {
		return Math.abs(this.real-real) <= ComplexNumber.calcPrecision &&
			   Math.abs(this.imag-imag) <= ComplexNumber.calcPrecision;
	}
}



package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ComplexNumber;

/**
 * Usage demonstration of ComplexNumber class.
 * @author ltomic
 *
 */
public class ComplexDemo {

	/**
	 * Method called at the beginning of a program. Method executes some
	 * calculations on ComplexNumber class to demonstrate usage of the class.
	 * Arguments explained below.
	 * @param args arguments from the command-line.
	 */
	public static void main(String[] args) {
		ComplexNumber c1 = new ComplexNumber(2, 3);
		ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57))
		.div(c2).power(3).root(2)[1];
		System.out.println(c3);
	}
}

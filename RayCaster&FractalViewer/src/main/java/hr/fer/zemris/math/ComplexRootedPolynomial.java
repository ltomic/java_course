package hr.fer.zemris.math;

/**
 * Models a polynomial over field of complex numbers. The polynomial is constructed
 * using its roots and is stored in such way. This class enables calculating the value
 * of polynomial in a certain point, transforming into a {@link ComplexPolynomial} and
 * finding the closest root for a certain point.
 * @author ltomic
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Roots of the polynomial
	 */
	private Complex[] roots;
	
	/**
	 * Constructs a {@link ComplexRootedPolynomial} with roots given in argument
	 * @param roots - roots of the polynomial
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		if (roots.length == 0) throw new IllegalArgumentException("Expected at least one root");
		this.roots = roots;
	}
	
	/**
	 * Calculates the value of this polynomial in a given point.
	 * @param z - point in which the value of the polynomial should be calculated
	 * @return the value of this polynomial in a given point
	 */
	public Complex apply(Complex z) {
		Complex result = new Complex(1, 0);
		for (int i = 0; i < roots.length; ++i) {
			result = result.multiply(z.sub(roots[i]));
		}
		
		return result;
	}
	
	/**
	 * Returns the order of this polynomial. In this model, the return value is the number
	 * of roots.
	 * @return order of this polynomial.
	 */
	public short order() {
		return (short)roots.length;
	}
	
	/**
	 * Returns this polynomial as a {@link ComplexPolynomial}. This method
	 * calculates the coefficients of the polynomial thus its complexity is
	 * quadratic in number of roots.
	 * @return this polynomial as a {@link ComplexPolynomial}
	 */
	public ComplexPolynomial toComplexPolynom() {
		Complex[] coef = new Complex[roots.length+1];
		coef[0] = new Complex(1, 0);
		
		for (int i = 0; i < roots.length; ++i) {
			coef[i+1] = Complex.ZERO;
			for (int j = i+1; j >= 0; --j) {
				coef[j] = coef[j].sub(j == 0 ? Complex.ZERO : coef[j-1].multiply(roots[i]));
			}
		}

		for (int i = 0, n = coef.length; i < n/2; ++i) {
			Complex tmp = coef[i];
			coef[i] = coef[n-i-1];
			coef[n-i-1] = tmp;
		}
		
		return new ComplexPolynomial(coef);
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (Complex i : roots) {
			string.append(String.format("(z-%s)", i.toString()));
		}
		
		return string.toString();
	}
	
	/**
	 * Returns the index of closest root of the polynomial within the provided distance
	 * from the provided point. If no roots are within the provided distance -1 is returned.
	 * @param z - point for which the index of closest root should be found
	 * @param treshold - maximal distance the closest root can be from the point
	 * @return the index of the closest root
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		double minn = treshold+1;
		int index = -1;
		
		for (int i = 0; i < roots.length; ++i) {
			double calc = z.sub(roots[i]).module();
			if (calc > treshold) continue;
			if (calc < minn) {
				minn = calc;
				index = i;
			}
		}
		
		return index;
	}
}

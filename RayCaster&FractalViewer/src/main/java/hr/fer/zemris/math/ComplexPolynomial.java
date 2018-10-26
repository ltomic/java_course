package hr.fer.zemris.math;

import java.util.Arrays;

/**
 * Instances of this class represent a polynomial over complex number field.
 * Polynomial is stored by its coefficients.
 * @author ltomic
 *
 */
public class ComplexPolynomial {

	/**
	 * Coefficients of polynomial, coefficient are stored from the ones with
	 * smallest degree to the largest
	 */
	private Complex[] coef;
	
	/**
	 * Constructs a polynomial with given coefficients. Coefficients are in ascending
	 * order of their degree.
	 * @param factors - polynomial coefficients
	 */
	public ComplexPolynomial(Complex ...factors) {
		if (factors.length == 0) throw new IllegalArgumentException(
				"Polynomial has to have at least one coefficent");
		this.coef = factors;
	}
	
	/**
	 * Returns order of this polynomial; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * @return order of this polynomial
	 */
	public short order() {
		return (short)(coef.length-1);
	}
	
	/**
	 * Multiplies this polynomial with the given one. Complexity of this operation
	 * is order of this polynomial * order of the given polynomial.
	 * @param p - polynomial to multiply with
	 * @return this polynomial multiplied with the given one
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newCoef = new Complex[coef.length+p.coef.length-1];
		Arrays.fill(newCoef, Complex.ZERO);
		
		for (int i = 0; i < coef.length; ++i) {
			for (int j = 0; j < p.coef.length; ++j) {
				newCoef[i+j] = newCoef[i+j].add(this.coef[i].multiply(p.coef[j]));
			}
		}
		
		return new ComplexPolynomial(newCoef);
	}
	
	/**
	 * Derives this polynomial.
	 * @return this polynomial derived
	 */
	public ComplexPolynomial derive() {
		if (order() == 0) return new ComplexPolynomial(Complex.ZERO);
		Complex[] newCoef = new Complex[coef.length-1];
		
		for (int i = 1, n = coef.length; i < n; ++i) {
			newCoef[i-1] = coef[i].scale(i);
		}
		
		return new ComplexPolynomial(newCoef);
	}
	
	/**
	 * Calculates the value of this polynomial in the given point
	 * @param z - point in which to calculate the value this polynomial
	 * @return the value of this polynomial in the given point
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		
		for (int i = coef.length-1; i >= 0; --i) {
			result = result.multiply(z).add(coef[i]);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder coefs = new StringBuilder("(");
		for (Complex i : coef) {
			coefs.append(i + ", ");
		}
		coefs.deleteCharAt(coefs.length()-1);
		coefs.deleteCharAt(coefs.length()-1);
		coefs.append(")");
		
		return coefs.toString();
	}

	/**
	 * Compares two polynomials. Two polynomials are equal if their coefficients are equal.
	 * @param obj object to be tested if equal
	 * @return true if all coefficients of the polynomials are equal, else false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexPolynomial other = (ComplexPolynomial) obj;
		if (!Arrays.equals(coef, other.coef))
			return false;
		return true;
	}
	
	
}






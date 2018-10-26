package hr.fer.zemris.java.hw16.trazilica;

// TODO: Auto-generated Javadoc
/**
 * Models a vector over a field of real values with arbitrary dimension and standard dot product.
 * @author ltomic
 *
 */
public class Vector {

	/** Vector elements */
	private double[] data;

	/**
	 * Constructs {@link Vector} with provided arguments
	 * @param n - vector dimension
	 */
	public Vector(int n) {
		data = new double[n];
	}

	/**
	 * Gets element at provided index
	 * @param ind - index of element to get
	 * @return element at provided index
	 */
	public double getElement(int ind) {
		checkBounds(ind);
		return data[ind];
	}

	/**
	 * Sets element at provided index with to provided value
	 * @param ind - index of element to set
	 * @param val - value to set
	 */
	public void setElement(int ind, double val) {
		checkBounds(ind);
		data[ind] = val;
	}

	/**
	 * Checks if provided index is in bounds
	 * @param ind - index to check
	 */
	private void checkBounds(int ind) {
		if (ind < 0 || ind >= data.length) throw new IndexOutOfBoundsException(
				"Expected number between 0 and " + (data.length - 1) + ". Given " + ind);
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public double[] getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(double[] data) {
		this.data = data;
	}

	/**
	 * Returns the vector dimension
	 * @return vector dimension
	 */
	public int dimension() {
		return data.length;
	}

	/**
	 * Returns vector norm. Norm is induced by standard dot product
	 * @return vector norm
	 */
	public double norm() {
		double val = 0;
		for (int i = 0; i < data.length; ++i) {
			val += data[i] * data[i];
		}

		return Math.sqrt(val);
	}

	/**
	 * Return standard dot product of this vector and the provided one. The other vector should be
	 * of the same dimension as this vector.
	 * @param other - second vector of the dot product
	 * @return dot product of this vector and the provided one
	 */
	public double dotProduct(Vector other) {
		if (other.dimension() != this.dimension()) {
			throw new IllegalArgumentException(
					"Vectors dimensions should be the same. This vector size is " + this.dimension()
							+ ". Other vector size is " + other.dimension());
		}

		double val = 0;
		for (int i = 0; i < this.data.length; ++i) {
			val += this.data[i] * other.data[i];
		}

		return val;
	}

	/**
	 * Returns cosine of the angle between this vector and the provided one. The other vector should
	 * be of the same dimension as this vector.
	 * @param other
	 * @return
	 */
	public double cosAngle(Vector other) {
		if (this.norm() == 0 || other.norm() == 0) return 0; 
		return dotProduct(other) / (this.norm() * other.norm());
	}

	/**
	 * Multiplies by element this vector with the provided one. Provided vector should have the same
	 * dimension as this vector.
	 * @param other - vector to multiply with
	 */
	public void mulByElements(Vector other) {
		if (other.dimension() != this.dimension()) {
			throw new IllegalArgumentException(
					"Vectors dimensions should be the same. This vector size is " + this.dimension()
							+ ". Other vector size is " + other.dimension());
		}

		for (int i = 0; i < this.data.length; ++i) {
			this.data[i] *= other.data[i];
		}
	}

}

package hr.fer.zemris.math;

/**
 * Immutable vector with three components. Provides basic operations over three
 * dimensional vectors. All operations return a new instance of Vector3.
 * @author ltomic
 *
 */
public class Vector3 {

	/**
	 * X-axis component
	 */
	double x;
	/**
	 * Y-axis component
	 */
	double y;
	/**
	 * Z-axis component
	 */
	double z;
	
	/**
	 * Difference up to which components of vector as considered equal
	 */
	public static final double PRECISION = 1E-6;
	
	/**
	 * Constructs a Vector3 with provided components.
	 * @param x x-axis component
	 * @param y y-axis component
	 * @param z z-axis component
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Computes length of this vector.
	 * @return length of this vector
	 */
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Returns a vector with length equal to 1 and direction equal to direction of
	 * this vector i.e. this vector normalized.
	 * @return this vector normalized
	 */
	public Vector3 normalized() {
		double norm = norm();
		return new Vector3(x / norm, y / norm, z / norm);
	}
	
	/**
	 * Returns new vector equal to the sum of this vector and the provided one.
	 * @param other vector to be summed with
	 * @return sum of this vector and given one
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	/**
	 * Returns new vector equal to the result of subtraction
	 * of this vector and the provided one.
	 * @param other vector to be subtracted with
	 * @return result of subtraction of this vector and given one
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
	}
	
	/**
	 * Returns a dot product of this vector and the provided one
	 * @param other second vector of the dot product
	 * @return dot product of this vector and the provided one
	 */
	public double dot(Vector3 other) {
		return this.x*other.x + this.y*other.y + this.z*other.z;
	}
	
	/**
	 * Returns a cross product of this vector and the provided one
	 * @param other second vector of the cross product
	 * @return cross product of this vector and the provided one
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(this.y*other.z - this.z*other.y, this.z*other.x - this.x*other.z,
				this.x*other.y - this.y*other.x);
	}
	
	/**
	 * Scales this vector with the provided scalar.
	 * @param s scalar to be scaled with
	 * @return this vector scaled with provided scalar
	 */
	public Vector3 scale(double s) {
		return new Vector3(s*this.x, s*this.y, s*this.z);
	}

	/**
	 * Computes the cosine of the angle between this vector and the provided one.
	 * Uses the definition of dot product to compute the cosine.
	 * @param other - other vector
	 * @return cosine of the angle between this vector and the provided one
	 */
	public double cosAngle(Vector3 other) {
		return dot(other) / (norm() * other.norm());
	}

	/**
	 * Returns x-axis component.
	 * @return x-axis component
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns y-axis component.
	 * @return y-axis component
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns z-axis component.
	 * @return z-axis component
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Transforms the vector into an array of three elements - the components of the vector
	 * @return an array of three elements - the components of the vector
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector3 other = (Vector3) obj;
		if (Math.abs(x-other.x) > PRECISION) return false;
		if (Math.abs(y-other.y) > PRECISION) return false;
		if (Math.abs(z-other.z) > PRECISION) return false;
		return true;
	}

	
	
	
	
}








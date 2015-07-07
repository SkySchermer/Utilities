/**
 * File: Quaternion.java    
 * License:
 */
package schermer.math;

import static java.lang.Math.abs;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;


/**
 * An implementation of an immutable quaternion for use in graphics
 * calculations.
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Usage: </b>
 * <p>
 * Equality comparisons for floating point numbers are inexact, so comparison
 * errors may occur if a suitable precision level is not set. To ensure the {
 * {@link #equals(Object)} method behaves as expeted, use
 * {@link #setFloatingPointPrecision(float)} to set the desired level of
 * precision first.
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Constructors: </b>
 * <p>
 * {@link #of(float, float, float, float)}: Constructs a Quaternion from its
 * components.
 * <p>
 * {@link #of(float, Vector3f)}: Constructs a Quaternion from its real part and
 * its vector (imaginary) part.
 * <p>
 * {@link #fromRotationVector(Vector3f)}: Constructs a Quaternion (versor) from
 * a rotation vector.
 * 
 * @version 0.1.4 (25 June 2015)
 * @author Skylor R Schermer
 */
public class Quaternion implements Serializable {

	// Constants =============================================================
	private static final long serialVersionUID = 14L;

	/** The number of components in the quaternion. */
	public static final int DIMENSION = 4;

	/** The zero quaternion. */
	public static final Quaternion ZERO = Quaternion.of(0, 0, 0, 0);
	/** The unit quaternion. */
	public static final Quaternion ONE = Quaternion.of(1, 0, 0, 0);
	/** The quaternion with unit i comonent. */
	public static final Quaternion UNIT_I = Quaternion.of(0, 1, 0, 0);
	/** The quaternion with unit j comonent. */
	public static final Quaternion UNIT_J = Quaternion.of(0, 0, 1, 0);
	/** The quaternion with unit k comonent. */
	public static final Quaternion UNIT_K = Quaternion.of(0, 0, 0, 1);


	// Static Fields =========================================================
	/** Floating point precision for equality comparisons. */
	private static float epsilon = 0;


	// Fields ================================================================
	private float r;
	private float i;
	private float j;
	private float k;


	// Constructors ==========================================================
	/**
	 * Constructs a Quaternion from its total internal state.
	 * 
	 * @param r
	 *        the real component
	 * @param i
	 *        the i component
	 * @param j
	 *        the j component
	 * @param k
	 *        the k component
	 */
	private Quaternion(float r, float i, float j, float k) {
		/* Remove -0.0 from possible values. */
		if (abs(r) <= epsilon) r += 0.0f;
		if (abs(i) <= epsilon) i += 0.0f;
		if (abs(j) <= epsilon) j += 0.0f;
		if (abs(k) <= epsilon) k += 0.0f;

		this.r = r;
		this.i = i;
		this.j = j;
		this.k = k;
	}

	// Static Constructors ===================================================
	/**
	 * Constructs a Quaternion from its components.
	 * 
	 * @param r
	 *        the real component
	 * @param i
	 *        the i component
	 * @param j
	 *        the j component
	 * @param k
	 *        the k component
	 * @return a new Quaternion
	 */
	public static Quaternion of(float r, float i, float j, float k) {
		return new Quaternion(r, i, j, k);
	}

	/**
	 * Constructs a Quaternion from its real part and its vector (imaginary)
	 * part.
	 * 
	 * @param scalar
	 *        the real part
	 * @param vector
	 *        the imaginary parts
	 * @return a new Quaternion
	 */
	public static Quaternion of(float scalar, Vector3f vector) {
		return new Quaternion(scalar,
							  vector.getX(),
							  vector.getY(),
							  vector.getZ());
	}

	/**
	 * Constructs a Quaternion (versor) from a rotation vector.
	 * 
	 * @param rotationVector
	 *        the rotation vector in radians
	 * @return a new Quaternion
	 */
	public static Quaternion fromRotationVector(Vector3f rotationVector) {
		if (rotationVector.equals(Vector3f.ZERO)) return ONE;
		float cosHalfθ = (float) Math.cos(rotationVector.magnitude() / 2);
		float sinHalfθ = (float) Math.sin(rotationVector.magnitude() / 2);
		return Quaternion.of(cosHalfθ,
							 rotationVector.getX() * sinHalfθ,
							 rotationVector.getY() * sinHalfθ,
							 rotationVector.getZ() * sinHalfθ);
	}

	// Accessor Methods ======================================================
	/**
	 * Returns the i component of the quaternion.
	 * 
	 * @return the i component
	 */
	public float getI() {
		return i;
	}

	/**
	 * Returns the j component of the quaternion.
	 * 
	 * @return the j component
	 */
	public float getJ() {
		return j;
	}

	/**
	 * Returns the k component of the quaternion.
	 * 
	 * @return the k component
	 */
	public float getK() {
		return k;
	}

	/**
	 * Returns the scalar (real) component of the quaternion.
	 * 
	 * @return the scalar part
	 */
	public float getScalarPart() {
		return r;
	}

	/**
	 * Returns the imaginary components of the quaternion as a vector.
	 * 
	 * @return the vector part
	 */
	public Vector3f getVectorPart() {
		return Vector3f.rectangular(i, j, k);
	}

	// Methods ===============================================================
	/**
	 * Returns the conjugate of the quaternion.
	 * 
	 * @return the conjugate of the quaternion
	 */
	public Quaternion conjugate() {
		return Quaternion.of(r, -i, -j, -k);
	}

	/**
	 * Returns the norm (magnitude) of the quaternion.
	 * 
	 * @return the norm of the quaternion
	 */
	public float norm() {
		return (float) Math.sqrt(i * i + j * j + k * k + r * r);
	}


	/**
	 * Returns the sum of the quaternions.
	 * 
	 * @param q
	 *        the quaternion to add
	 * @return the quaternion sum
	 */
	public Quaternion add(Quaternion q) {
		return Quaternion.of(r + q.r,
							 i + q.i,
							 j + q.j,
							 k + q.k);
	}

	/**
	 * Returns the quaternion produced by adding the given value to each
	 * component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new quaternion
	 */
	public Quaternion add(float value) {
		return this.eachComponent(c -> {
			return c + value;
		});
	}

	/**
	 * Returns the difference of the quaternions.
	 * 
	 * @param q
	 *        the quaternion to subtract
	 * @return the quaternion difference
	 */
	public Quaternion subtract(Quaternion q) {
		return Quaternion.of(r - q.r,
							 i - q.i,
							 j - q.j,
							 k - q.k);
	}

	/**
	 * Returns the quaternion produced by subtracting the given value from each
	 * component.
	 * 
	 * @param value
	 *        the value to subtract
	 * @return the new quaternion
	 */
	public Quaternion subtract(float value) {
		return this.eachComponent(c -> {
			return c - value;
		});
	}

	/**
	 * Returns the product of two quaternions.
	 * 
	 * @param q
	 *        the quaternion to multiply
	 * @return the new quaternion
	 */
	public Quaternion multiply(Quaternion q) {
		return Quaternion.of(r * q.r - i * q.i - j * q.j - k * q.k,
							 r * q.i + i * q.r + j * q.k - k * q.j,
							 r * q.j - i * q.k + j * q.r + k * q.i,
							 r * q.k + i * q.j - j * q.i + k * q.r);
	}

	/**
	 * Returns the quaternion produced by multiplying the given value to each
	 * component.
	 * 
	 * @param value
	 *        the value to multiply
	 * @return the new quaternion
	 */
	public Quaternion multiply(float value) {
		return this.eachComponent(c -> {
			return c * value;
		});
	}

	/**
	 * Returns the quaternion produced by dividing on the left by the given
	 * quaternion.
	 * 
	 * @param q
	 *        the quaternion to divide by
	 * @return the new quaternion
	 */
	public Quaternion leftDivide(Quaternion q) {
		return q.reciprocal().multiply(this);
	}

	/**
	 * Returns the quaternion produced by dividing on the right by the given
	 * quaternion.
	 * 
	 * @param q
	 *        the quaternion to divide by
	 * @return the new quaternion
	 */
	public Quaternion rightDivide(Quaternion q) {
		return this.multiply(q.reciprocal());
	}

	/**
	 * Returns the quaternion produced by dividing each component by the given
	 * value.
	 * 
	 * @param value
	 *        the value to divide by
	 * @return the new quaternion
	 */
	public Quaternion divide(float value) {
		return this.eachComponent(c -> {
			return c / value;
		});
	}

	/**
	 * Returns the reciprocal of the quaternion.
	 * 
	 * @return the reciprocal of the quaternion
	 */
	public Quaternion reciprocal() {
		if (this.equals(ZERO)) {
			throw new UnsupportedOperationException("Reciprocal of zero is undefined.");
		}
		return this.conjugate().divide(norm() * norm());
	}

	/**
	 * Returns a new quaternion with the same component proportions, with
	 * magnitude 1.
	 * 
	 * @return the new quaternion
	 */
	public Quaternion versor() {
		if (this.equals(ZERO)) {
			throw new UnsupportedOperationException("Versor of zero is undefined.");
		}

		if (Float.isInfinite(this.norm())) {
			throw new UnsupportedOperationException("Versor of quaternion with infinite norm is undefined.");
		}

		return this.divide(norm());
	}

	/**
	 * Returns the forward-facing vector of the versor of the quaternion.
	 * 
	 * @return the forward-facing vector
	 */
	public Vector3f getVersorForwardVector() {
		return Vector3f.UNIT_Z.rotated(this);
	}

	/**
	 * Returns the backward-facing vector of the versor of the quaternion.
	 * 
	 * @return the backward-facing vector
	 */
	public Vector3f getVersorBackwardVector() {
		return Vector3f.UNIT_Z.rotated(this).opposite();
	}

	/**
	 * Returns the upward-facing vector of the versor of the quaternion.
	 * 
	 * @return the upward-facing vector
	 */
	public Vector3f getVersorUpVector() {
		return Vector3f.UNIT_Y.rotated(this);
	}

	/**
	 * Returns the downward-facing vector of the versor of the quaternion.
	 * 
	 * @return the downward-facing vector
	 */
	public Vector3f getVersorDownVector() {
		return Vector3f.UNIT_Y.rotated(this).opposite();
	}

	/**
	 * Returns the left-facing vector of the versor of the quaternion.
	 * 
	 * @return the left-facing vector
	 */
	public Vector3f getVersorLeftVector() {
		return Vector3f.UNIT_X.rotated(this);
	}

	/**
	 * Returns the right-facing vector of the versor of the quaternion.
	 * 
	 * @return the right-facing vector
	 */
	public Vector3f getVersorRightVector() {
		return Vector3f.UNIT_X.rotated(this).opposite();
	}

	/**
	 * Returns the vector produced by applying the given function to each
	 * component.
	 * 
	 * @param f
	 *        the function to apply
	 * @return the new quaternion
	 */
	public Quaternion eachComponent(ToDoubleFunction<Float> f) {
		return Quaternion.of((float) f.applyAsDouble(r),
							 (float) f.applyAsDouble(i),
							 (float) f.applyAsDouble(j),
							 (float) f.applyAsDouble(k));
	}

	/**
	 * Returns the quaternion produced by applying the given function to each
	 * component.
	 * This method provides each component as a unit quaternion in the second
	 * argument to
	 * the function.
	 * 
	 * @param f
	 *        the function to apply
	 * @return the new quaternion
	 */
	public Quaternion eachComponent(ToDoubleBiFunction<Float, Quaternion> f) {
		return Quaternion.of((float) f.applyAsDouble(r, ONE),
							 (float) f.applyAsDouble(j, UNIT_I),
							 (float) f.applyAsDouble(k, UNIT_J),
							 (float) f.applyAsDouble(k, UNIT_K));
	}


	// Overridden Methods ====================================================
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Quaternion(" + r + " + " + i + "i + " + j + "j + " + k + "k)";
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(r);
		result = prime * result + Float.floatToIntBits(i);
		result = prime * result + Float.floatToIntBits(j);
		result = prime * result + Float.floatToIntBits(k);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Quaternion other = (Quaternion) obj;
		if (Math.abs(r - other.r) > epsilon) return false;
		if (Math.abs(i - other.i) > epsilon) return false;
		if (Math.abs(j - other.j) > epsilon) return false;
		if (Math.abs(k - other.k) > epsilon) return false;
		return true;
	}


	// Static Methods ========================================================
	/**
	 * Sets the floating point precision for Quaternion equality comparisons.
	 * 
	 * @param epsilon
	 *        the maximum acceptable difference between components
	 */
	public static void setFloatingPointPrecision(float epsilon) {
		Quaternion.epsilon = Math.abs(epsilon);
	}
}

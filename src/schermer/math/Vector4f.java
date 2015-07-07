/**
 * File: Vector4f.java    
 * License:
 */
package schermer.math;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * An implementation of an immutable four-dimensional float vector for use in
 * graphics calculations.
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
 * {@link Vector4f#rectangular(float, float, float, float)}: Constructs a
 * Vector4f from its components in rectangluar coordinates.
 * <p>
 * {@link Vector4f#rectangular(Vector3f, float)}: Constructs a Vector4f from a
 * rectangular three-component vector by extending with a fourth component.
 * <p>
 * {@link Vector4f#spherical(float, float, float, float)}: Constructs a Vector4f
 * from its components in spherical coordinates.
 * 
 * @version 0.1.9 (25 June 2015)
 * @author Skylor R Schermer
 */
public final class Vector4f implements Serializable {

	// Constants =============================================================
	private static final long serialVersionUID = 19L;

	/** The number of components in the vector. */
	public static final int DIMENSION = 4;

	/** The zero vector. */
	public static final Vector4f ZERO = Vector4f.rectangular(0, 0, 0, 0);
	/** The x component unit vector. */
	public static final Vector4f UNIT_X = Vector4f.rectangular(1, 0, 0, 0);
	/** The y component unit vector. */
	public static final Vector4f UNIT_Y = Vector4f.rectangular(0, 1, 0, 0);
	/** The z component unit vector. */
	public static final Vector4f UNIT_Z = Vector4f.rectangular(0, 0, 1, 0);
	/** The w component unit vector. */
	public static final Vector4f UNIT_W = Vector4f.rectangular(0, 0, 0, 1);
	/** The vector of unit components. */
	public static final Vector4f ONE = Vector4f.rectangular(1, 1, 1, 1);


	// Static Fields =========================================================
	/** Floating point precision for equality comparisons. */
	private static float epsilon = 0;


	// Fields ================================================================
	private float x;
	private float y;
	private float z;
	private float w;


	// Constructors ==========================================================
	/**
	 * Constructs a Vector4f from its components.
	 * 
	 * @param x
	 *        the x component
	 * @param y
	 *        the y component
	 * @param z
	 *        the z component
	 * @param w
	 *        the w component
	 */
	private Vector4f(float x, float y, float z, float w) {
		/* Remove -0.0 from possible values. */
		if (abs(x) <= epsilon) x += 0.0f;
		if (abs(y) <= epsilon) y += 0.0f;
		if (abs(z) <= epsilon) z += 0.0f;
		if (abs(w) <= epsilon) w += 0.0f;

		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}


	// Static Constructors ===================================================
	/**
	 * Creates a Vector4f from rectangular coordinates.
	 * 
	 * @param x
	 *        the x coordinate
	 * @param y
	 *        the y coordinate
	 * @param z
	 *        the z coordinate
	 * @param w
	 *        the w coordinate
	 * @return a new Vector4f
	 */
	public static Vector4f rectangular(float x, float y, float z, float w) {
		return new Vector4f(x, y, z, w);
	}

	/**
	 * Creates a Vector4f from a rectangular three-component vector by extending
	 * with a fourth component.
	 * 
	 * @param vector
	 *        the initial vector
	 * @param w
	 *        the w coordinate
	 * @return a new Vector4f
	 */
	public static Vector4f rectangular(Vector3f vector, float w) {
		return new Vector4f(vector.getX(),
							vector.getY(),
							vector.getZ(),
							w);
	}

	/**
	 * Creates a Vector4f from spherical coordinates.
	 * 
	 * @param r
	 *        the magnitude
	 * @param φ1
	 *        the first angle component in radians
	 * @param φ2
	 *        the second angle component in radians
	 * @param φ3
	 *        the third angle component in radians
	 * @return a new Vector4f
	 */
	public static Vector4f spherical(float r, float φ1, float φ2, float φ3) {
		return new Vector4f(r * (float) cos(φ1),
							r * (float) (sin(φ1) * cos(φ2)),
							r * (float) (sin(φ1) * sin(φ2) * cos(φ3)),
							r * (float) (sin(φ1) * sin(φ2) * sin(φ3)));

	}

	// Accessor Methods ======================================================
	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return the magnitude of the vector
	 */
	public float magnitude() {
		return (float) sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * Returns the x component of the vector.
	 * 
	 * @return the x component
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Returns the y component of the vector.
	 * 
	 * @return the y component
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * Returns the z component of the vector.
	 * 
	 * @return the z component
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Returns the w component of the vector.
	 * 
	 * @return the w component
	 */
	public float getW() {
		return this.w;
	}

	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return the magnitude
	 */
	public float getR() {
		return magnitude();
	}

	/**
	 * Returns the first angle component of the vector
	 * 
	 * @return the first angle component
	 */
	public float getφ1() {
		if (x == 0 && y == 0 && z == 0 && w == 0) return 0;
		return (float) acos(x / magnitude());
	}

	/**
	 * Returns the second angle component of the vector
	 * 
	 * @return the second angle component
	 */
	public float getφ2() {
		if (x == 0 && y == 0 && z == 0 && w == 0) return 0;
		return (float) acos(y / magnitude());
	}

	/**
	 * Returns the third angle component of the vector
	 * 
	 * @return the third angle component
	 */
	public float getφ3() {
		if (x == 0 && y == 0 && z == 0 && w == 0) return 0;
		float res = (float) acos(z / magnitude());
		if (w < 0) res = (float) (2 * PI - res);
		return res;
	}

	// Methods ===============================================================
	/**
	 * Returns the sum of the vectors.
	 * 
	 * @param v
	 *        the vector to add
	 * @return the vector sum
	 */
	public Vector4f add(Vector4f v) {
		return new Vector4f(this.x + v.x,
							this.y + v.y,
							this.z + v.z,
							this.w + v.w);
	}

	/**
	 * Returns the vector produced by adding the given value to each component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new vector
	 */
	public Vector4f add(float value) {
		return this.eachComponentRectangular(c -> {
			return c + value;
		});
	}

	/**
	 * Returns the difference of the vectors.
	 * 
	 * @param v
	 *        the vector to subtract
	 * @return the vector difference
	 */
	public Vector4f subtract(Vector4f v) {
		return new Vector4f(this.x - v.x,
							this.y - v.y,
							this.z - v.z,
							this.w - v.w);
	}

	/**
	 * Returns the vector produced by subtracting the given value from each
	 * component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new vector
	 */
	public Vector4f subtract(float value) {
		return this.eachComponentRectangular(c -> {
			return c - value;
		});
	}

	/**
	 * Returns the vector produced by multiplying the given value to each
	 * component.
	 * 
	 * @param value
	 *        the value to multiply
	 * @return the new vector
	 */
	public Vector4f multiply(float value) {
		return this.eachComponentRectangular(c -> {
			return c * value;
		});
	}

	/**
	 * Returns the vector produced by dividing each component by the given
	 * value.
	 * 
	 * @param value
	 *        the value to divide by
	 * @return the new vector
	 */
	public Vector4f divide(float value) {
		return this.eachComponentRectangular(c -> {
			return c / value;
		});
	}

	/**
	 * Returns the scalar product of the given vector with this one.
	 * 
	 * @param v
	 *        the vector to take the scalar product with
	 * @return the scalar product
	 */
	public float dot(Vector4f v) {
		return x * v.x + y * v.y + z * v.z + w * v.w;
	}


	/**
	 * Returns a vector in the same direction with magnitude 1.
	 * 
	 * @return the normalized vector
	 */
	public Vector4f normalized() {
		if (this.equals(ZERO))
							  throw new UnsupportedOperationException("Normalized zero vector is undefined.");

		if (Float.isInfinite(this.magnitude()))
											   throw new UnsupportedOperationException("Normalized vector of infinite magnitude is undefined.");

		return this.divide(this.magnitude());
	}

	/**
	 * Returns a vector with the same magnitude in the opposite direction.
	 * 
	 * @return the opposite vector
	 */
	public Vector4f opposite() {
		return this.multiply(-1);
	}

	/**
	 * Returns the vector produced by applying the given function to each
	 * rectangular component.
	 * 
	 * @param f
	 *        the function to apply
	 * @return the new vector
	 */
	public Vector4f eachComponentRectangular(ToDoubleFunction<Float> f) {
		return Vector4f.rectangular((float) f.applyAsDouble(x),
									(float) f.applyAsDouble(y),
									(float) f.applyAsDouble(z),
									(float) f.applyAsDouble(w));
	}

	/**
	 * Returns the vector produced by applying the given function to each
	 * rectangular component. This method provides each component as a unit
	 * vector in the second argument to the function.
	 * 
	 * @param f
	 *        the function to apply
	 * @return the new vector
	 */
	public Vector4f eachComponentRectangular(ToDoubleBiFunction<Float, Vector4f> f) {
		return Vector4f.rectangular((float) f.applyAsDouble(x, UNIT_X),
									(float) f.applyAsDouble(y, UNIT_Y),
									(float) f.applyAsDouble(z, UNIT_Z),
									(float) f.applyAsDouble(w, UNIT_W));
	}

	/**
	 * Returns the Hyperoctant of the given vector, or null if it lies on an
	 * axis.
	 * 
	 * @return the hyperoctant
	 */
	public Hyperoctant hyperoctant() {
		for (Hyperoctant o : Hyperoctant.values()) {
			if (Math.signum(x) != Math.signum(o.signVector.x)) continue;
			if (Math.signum(y) != Math.signum(o.signVector.y)) continue;
			if (Math.signum(z) != Math.signum(o.signVector.z)) continue;
			if (Math.signum(w) != Math.signum(o.signVector.w)) continue;
			return o;
		}
		return null;
	}

	/**
	 * Returns a string representation of the vector in rectangular coordinates.
	 * 
	 * @return a string representation of the vector
	 */
	public String toRectangularString() {
		return "Vector4f[x= " + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", w=" + this.getW() + "]";
	}

	/**
	 * Returns a string representation of the vector in spherical coordinates.
	 * 
	 * @return a string representation of the vector
	 */
	public String toSphericalString() {
		return "Vector4f[r= " +
			   this.getR() + ", φ1=" + this.getφ1() + ", φ2=" + this.getφ2() + ", φ3=" + this.getφ3() + "]";
	}


	// Overridden Methods ====================================================
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Vector4f[" + x + ", " + y + ", " + z + ", " + w + "]";
	}


	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}


	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector4f other = (Vector4f) obj;
		if (Math.abs(x - other.x) > epsilon) return false;
		if (Math.abs(y - other.y) > epsilon) return false;
		if (Math.abs(z - other.z) > epsilon) return false;
		if (Math.abs(w - other.w) > epsilon) return false;
		return true;
	}


	// Static Methods ========================================================
	/**
	 * Sets the floating point precision for Vector2f equality comparisons.
	 * 
	 * @param epsilon
	 *        the maximum acceptable difference between components
	 */
	public static void setFloatingPointPrecision(float epsilon) {
		Vector4f.epsilon = Math.abs(epsilon);
	}


	// Enumerations ==========================================================
	public static enum Hyperoctant {
		// @formatter:off
		PPPP (Vector4f.rectangular( 1,  1,  1,  1)),
		NPPP (Vector4f.rectangular(-1,  1,  1,  1)),
		NNPP (Vector4f.rectangular(-1, -1,  1,  1)),
		PNPP (Vector4f.rectangular( 1, -1,  1,  1)),
		PPNP (Vector4f.rectangular( 1,  1, -1,  1)),
		NPNP (Vector4f.rectangular(-1,  1, -1,  1)),
		NNNP (Vector4f.rectangular(-1, -1, -1,  1)),
		PNNP (Vector4f.rectangular( 1, -1, -1,  1)),
		PPPN (Vector4f.rectangular( 1,  1,  1, -1)),
		NPPN (Vector4f.rectangular(-1,  1,  1, -1)),
		NNPN (Vector4f.rectangular(-1, -1,  1, -1)),
		PNPN (Vector4f.rectangular( 1, -1,  1, -1)),
		PPNN (Vector4f.rectangular( 1,  1, -1, -1)),
		NPNN (Vector4f.rectangular(-1,  1, -1, -1)),
		NNNN (Vector4f.rectangular(-1, -1, -1, -1)),
		PNNN (Vector4f.rectangular( 1, -1, -1, -1));
		// @formatter:on

		public Vector4f signVector;

		Hyperoctant(Vector4f signVector) {
			this.signVector = signVector;
		}
	}
}

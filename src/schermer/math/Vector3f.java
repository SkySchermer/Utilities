/**
 * File: Vector3f.java 
 * License: The MIT License (MIT)
 *  
 *  Copyright (c) 2015 Skylor R. Schermer
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package schermer.math;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * An implementation of an immutable three-dimensional float vector for use in
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
 * {@link Vector3f#rectangular(float, float, float)}: Constructs a Vector3f from
 * its components in rectangluar coordinates.
 * <p>
 * {@link Vector3f#rectangular(Vector2f, float)}: Constructs a Vector3f from a
 * rectangular two-component vector by extending with a third component.
 * <p>
 * {@link Vector3f#cylindrical(float, float, float)}: Constructs a Vector3f from
 * its components in cylindrical coordinates.
 * <p>
 * {@link Vector3f#spherical(float, float, float)}: Constructs a Vector3f from
 * its components in spherical coordinates.
 * 
 * @version 0.1.9 (25 June 2015)
 * @author Skylor R Schermer
 */
public final class Vector3f implements Serializable {

	// Constants =============================================================
	private static final long serialVersionUID = 19L;

	/** The number of components in the vector. */
	public static final int DIMENSION = 3;

	/** The zero vector. */
	public static final Vector3f ZERO = Vector3f.rectangular(0, 0, 0);
	/** The x component unit vector. */
	public static final Vector3f UNIT_X = Vector3f.rectangular(1, 0, 0);
	/** The y component unit vector. */
	public static final Vector3f UNIT_Y = Vector3f.rectangular(0, 1, 0);
	/** The z component unit vector. */
	public static final Vector3f UNIT_Z = Vector3f.rectangular(0, 0, 1);
	/** The vector of unit components. */
	public static final Vector3f ONE = Vector3f.rectangular(1, 1, 1);


	// Static Fields =========================================================
	/** Floating point precision for equality comparisons. */
	private static float epsilon = 0;


	// Fields ================================================================
	private float x;
	private float y;
	private float z;


	// Constructors ==========================================================
	/**
	 * Constructs a Vector3f from its components.
	 * 
	 * @param x
	 *        the x component
	 * @param y
	 *        the y component
	 * @param z
	 *        the z component
	 */
	private Vector3f(float x, float y, float z) {
		/* Remove -0.0 from possible values. */
		if (abs(x) <= epsilon) x += 0.0f;
		if (abs(y) <= epsilon) y += 0.0f;
		if (abs(z) <= epsilon) z += 0.0f;

		this.x = x;
		this.y = y;
		this.z = z;
	}


	// Static Constructors ===================================================
	/**
	 * Creates a Vector3f from rectangular coordinates.
	 * 
	 * @param x
	 *        the x coordinate
	 * @param y
	 *        the y coordinate
	 * @param z
	 *        the z coordinate
	 * @return a new Vector3f
	 */
	public static Vector3f rectangular(float x, float y, float z) {
		return new Vector3f(x, y, z);
	}

	/**
	 * Creates a Vector3f from a rectangular two-component vector by extending
	 * with a third component.
	 * 
	 * @param vector
	 *        the initial vector
	 * @param z
	 *        the z coordinate
	 * @return a new Vector3f
	 */
	public static Vector3f rectangular(Vector2f vector, float z) {
		return new Vector3f(vector.getX(),
							vector.getY(),
							z);
	}

	/**
	 * Creates a Vector3f from cylindrical coordinates.
	 * 
	 * @param ρ
	 *        the radial component
	 * @param φ
	 *        the azimuthal angle in radians
	 * @param z
	 *        the z coordinate
	 * @return a new Vector3f
	 */
	public static Vector3f cylindrical(float ρ, float φ, float z) {
		return new Vector3f(ρ * (float) cos(φ),
							ρ * (float) sin(φ),
							z);

	}

	/**
	 * Creates a Vector3f from spherical coordinates.
	 * 
	 * @param r
	 *        the magnitude
	 * @param φ
	 *        the asimuthal angle in radians
	 * @param θ
	 *        the inclination angle in radians
	 * @return
	 */
	public static Vector3f spherical(float r, float φ, float θ) {
		return new Vector3f(r * (float) (sin(θ) * cos(φ)),
							r * (float) (sin(θ) * sin(φ)),
							r * (float) cos(θ));
	}

	// Accessor Methods ======================================================
	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return the magnitude of the vector
	 */
	public float magnitude() {
		return (float) sqrt(x * x + y * y + z * z);
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
	 * Returns the magnitude of the vector.
	 * 
	 * @return the magnitude
	 */
	public float getR() {
		return magnitude();
	}

	/**
	 * Returns the radial component of the vector in cylindrical coordinates.
	 * 
	 * @return the radial component
	 */
	public float getρ() {
		return (float) sqrt(x * x + y * y);
	}

	/**
	 * Returns the (azimuthal) angle with respect to the positive x (polar) axis
	 * of the vector.
	 * 
	 * @return the azimuthal angle
	 */
	public float getφ() {
		return (float) atan2(y, x);
	}

	/**
	 * Returns the (inclination) angle with respect to the cylindrical axis (x-y
	 * plane) of the vector.
	 * 
	 * @return the inclination angle
	 */
	public float getθ() {
		return (float) acos(z / magnitude());
	}

	// Methods ===============================================================
	/**
	 * Returns the sum of the vectors.
	 * 
	 * @param v
	 *        the vector to add
	 * @return the vector sum
	 */
	public Vector3f add(Vector3f v) {
		return new Vector3f(this.x + v.x,
							this.y + v.y,
							this.z + v.z);
	}

	/**
	 * Returns the vector produced by adding the given value to each component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new vector
	 */
	public Vector3f add(float value) {
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
	public Vector3f subtract(Vector3f v) {
		return new Vector3f(this.x - v.x,
							this.y - v.y,
							this.z - v.z);
	}

	/**
	 * Returns the vector produced by subtracting the given value from each
	 * component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new vector
	 */
	public Vector3f subtract(float value) {
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
	public Vector3f multiply(float value) {
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
	public Vector3f divide(float value) {
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
	public float dot(Vector3f v) {
		return x * v.x + y * v.y + z * v.z;
	}

	/**
	 * Returns the cross product of the given vector with this one.
	 * 
	 * @param v
	 *        the vector to take the cross product with
	 * @return the cross product
	 */
	public Vector3f cross(Vector3f v) {
		return new Vector3f(y * v.z - z * v.y,
							x * v.z - z * v.x,
							x * v.y - y * v.x);
	}

	/**
	 * Returns the vector produced by rotating the vector about the given axis
	 * by the given angle.
	 * 
	 * @param axis
	 *        the axis of rotation
	 * @param angle
	 *        the angle of the rotation in radians
	 * @return the rotated vector
	 */
	public Vector3f rotated(Vector3f axis, float angle) {
		if (axis.equals(ZERO)) throw new IllegalArgumentException("Zero vector is not a valid rotation axis.");
		return this.rotated(Quaternion.of((float) cos(angle / 2),
										  axis.normalized().multiply((float) sin(angle / 2))));
	}

	/**
	 * Returns the vector produced by rotating the vector using the given
	 * rotation quaternion.
	 * 
	 * @param rotation
	 *        the rotation quaternion
	 * @return the rotated vector
	 */
	public Vector3f rotated(Quaternion rotation) {
		Quaternion rot = rotation.versor();
		return rot.multiply(Quaternion.of(0, this))
				  .multiply(rot.conjugate())
				  .getVectorPart();
	}

	/**
	 * Returns a vector in the same direction with magnitude 1.
	 * 
	 * @return the normalized vector
	 */
	public Vector3f normalized() {
		// @formatter:off
		if (this.equals(ZERO)) 
			throw new UnsupportedOperationException("Normalized zero vector is undefined.");

		if (Float.isInfinite(this.magnitude())) 
			throw new UnsupportedOperationException("Normalized vector of infinite magnitude is undefined.");
		// @formatter:on

		return this.divide(this.magnitude());
	}

	/**
	 * Returns a vector with the same magnitude in the opposite direction.
	 * 
	 * @return the opposite vector
	 */
	public Vector3f opposite() {
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
	public Vector3f eachComponentRectangular(ToDoubleFunction<Float> f) {
		return Vector3f.rectangular((float) f.applyAsDouble(x),
									(float) f.applyAsDouble(y),
									(float) f.applyAsDouble(z));
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
	public Vector3f eachComponentRectangular(ToDoubleBiFunction<Float, Vector3f> f) {
		return Vector3f.rectangular((float) f.applyAsDouble(x, UNIT_X),
									(float) f.applyAsDouble(y, UNIT_Y),
									(float) f.applyAsDouble(z, UNIT_Z));
	}

	/**
	 * Returns the Octant of the given vector, or null if it lies on an axis.
	 * 
	 * @return the octant
	 */
	public Octant octant() {
		for (Octant o : Octant.values()) {
			if (Math.signum(x) != Math.signum(o.signVector.x) ||
				Math.signum(y) != Math.signum(o.signVector.y) ||
				Math.signum(z) != Math.signum(o.signVector.z)) continue;
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
		return "Vector3f[x= " + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + "]";
	}

	/**
	 * Returns a string representation of the vector in cylinrical coordinates.
	 * 
	 * @return a string representation of the vector
	 */
	public String toCylindricalString() {
		return "Vector3f[ρ= " + this.getρ() + ", φ=" + this.getφ() + ", z=" + this.getZ() + "]";
	}

	/**
	 * Returns a string representation of the vector in spherical coordinates.
	 * 
	 * @return a string representation of the vector
	 */
	public String toSphericalString() {
		return "Vector3f[r= " + this.getR() + ", φ=" + this.getφ() + ", θ=" + this.getθ() + "]";
	}


	// Overridden Methods ====================================================
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Vector3f[" + x + ", " + y + ", " + z + "]";
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Vector3f other = (Vector3f) obj;
		if (Math.abs(x - other.x) > epsilon) return false;
		if (Math.abs(y - other.y) > epsilon) return false;
		if (Math.abs(z - other.z) > epsilon) return false;
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
		Vector3f.epsilon = Math.abs(epsilon);
	}


	// Enumerations ==========================================================
	public static enum Octant {
		// @formatter:off
		PPP (Vector3f.rectangular( 1,  1,  1)),
		NPP (Vector3f.rectangular(-1,  1,  1)),
		NNP (Vector3f.rectangular(-1, -1,  1)),
		PNP (Vector3f.rectangular( 1, -1,  1)),
		PPN (Vector3f.rectangular( 1,  1, -1)),
		NPN (Vector3f.rectangular(-1,  1, -1)),
		NNN (Vector3f.rectangular(-1, -1, -1)),
		PNN (Vector3f.rectangular( 1, -1, -1));
		// @formatter:on

		public Vector3f signVector;

		Octant(Vector3f signVector) {
			this.signVector = signVector;
		}
	}
}

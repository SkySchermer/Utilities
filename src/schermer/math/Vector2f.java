/**
 * File: Vector2f.java
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

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.abs;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * An implementation of an immutable two-dimensional float vector for use in
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
 * {@link Vector2f#rectangular(float, float)}: Constructs a Vector2f from its
 * components in rectangluar coordinates.
 * <p>
 * {@link Vector2f#polar(float, float)}: Constructs a Vector2f from its
 * components in polar coordinates.
 * 
 * @version 0.1.9 (25 June 2015)
 * @author Skylor R Schermer
 */
public final class Vector2f implements Serializable {

	// Constants =============================================================
	private static final long serialVersionUID = 19L;

	/** The number of components in the vector. */
	public static final int DIMENSION = 2;

	/** The zero vector. */
	public static final Vector2f ZERO = Vector2f.rectangular(0, 0);
	/** The x component unit vector. */
	public static final Vector2f UNIT_X = Vector2f.rectangular(1, 0);
	/** The y component unit vector. */
	public static final Vector2f UNIT_Y = Vector2f.rectangular(0, 1);
	/** The vector of unit components. */
	public static final Vector2f ONE = Vector2f.rectangular(1, 1);


	// Static Fields =========================================================
	/** Floating point precision for equality comparisons. */
	private static float epsilon = 0;


	// Fields ================================================================
	private float x;
	private float y;


	// Constructors ==========================================================
	/**
	 * Constructs a Vector2f from its components.
	 * 
	 * @param x
	 *        the x component
	 * @param y
	 *        the y component
	 */
	private Vector2f(float x, float y) {
		/* Remove -0.0 from possible values. */
		if (abs(x) <= epsilon) x += 0.0f;
		if (abs(y) <= epsilon) y += 0.0f;

		this.x = x;
		this.y = y;
	}


	// Static Constructors ===================================================
	/**
	 * Creates a Vector2f from rectangular coordinates.
	 * 
	 * @param x
	 *        the x coordinate
	 * @param y
	 *        the y coordinate
	 * @return a new Vector2f
	 */
	public static Vector2f rectangular(float x, float y) {
		return new Vector2f(x, y);
	}

	/**
	 * Creates a Vector2f from polar coordinates.
	 * 
	 * @param radius
	 *        the magnitude of the vector
	 * @param angle
	 *        the angle in radians with respect to the polar axis (positive
	 *        x-axis in rectangular coordinates.)
	 * @return a new Vector2f
	 */
	public static Vector2f polar(float radius, float angle) {
		if (abs(radius) <= epsilon) return Vector2f.ZERO;

		return new Vector2f((float) (radius * cos(angle)),
							(float) (radius * sin(angle)));

	}


	// Accessor Methods ======================================================
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
	 * Returns the magnitude of the vector.
	 * 
	 * @return the magnitude
	 */
	public float getRadius() {
		return magnitude();
	}

	/**
	 * Returns the angle with respect to the positive x (polar) axis of the
	 * vector. The angle will lie in the range [-π,π], as determined by the
	 * {@link Math#atan2(double, double)} function.
	 * 
	 * @return the angle
	 */
	public float getAngle() {
		return (float) atan2(y, x);
	}

	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return the magnitude of the vector
	 */
	public float magnitude() {
		return (float) sqrt(x * x + y * y);
	}


	// Methods ===============================================================
	/**
	 * Returns the sum of the vectors.
	 * 
	 * @param v
	 *        the vector to add
	 * @return the vector sum
	 */
	public Vector2f add(Vector2f v) {
		return new Vector2f(this.x + v.x,
							this.y + v.y);
	}

	/**
	 * Returns the vector produced by adding the given value to each component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new vector
	 */
	public Vector2f add(float value) {
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
	public Vector2f subtract(Vector2f v) {
		return new Vector2f(this.x - v.x,
							this.y - v.y);
	}

	/**
	 * Returns the vector produced by subtracting the given value from each
	 * component.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new vector
	 */
	public Vector2f subtract(float value) {
		return this.eachComponentRectangular(c -> {
			return c - value;
		});
	}

	/**
	 * Returns the scalar product of the given vector with this one.
	 * 
	 * @param v
	 *        the vector to take the scalar product with
	 * @return the scalar product
	 */
	public float dot(Vector2f v) {
		return x * v.x + y * v.y;
	}

	/**
	 * Returns the vector produced by multiplying the given value to each
	 * component.
	 * 
	 * @param value
	 *        the value to multiply
	 * @return the new vector
	 */
	public Vector2f multiply(float value) {
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
	public Vector2f divide(float value) {
		return this.eachComponentRectangular(c -> {
			return c / value;
		});
	}

	/**
	 * Returns the vector produced by rotating the given vector by the given
	 * angle.
	 * 
	 * @param angle
	 *        the angle of the rotation in radians
	 * @return the rotated vector
	 */
	public Vector2f rotated(float angle) {
		if (abs(angle) <= epsilon) return this;
		return Vector2f.rectangular((float) (x * cos(angle) - y * sin(angle)),
									(float) (x * sin(angle) + y * cos(angle)));
	}

	/**
	 * Returns a vector in the same direction with magnitude 1.
	 * 
	 * @return the normalized vector
	 */
	public Vector2f normalized() {
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
	public Vector2f opposite() {
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
	public Vector2f eachComponentRectangular(ToDoubleFunction<Float> f) {
		return Vector2f.rectangular((float) f.applyAsDouble(x),
									(float) f.applyAsDouble(y));
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
	public Vector2f eachComponentRectangular(ToDoubleBiFunction<Float, Vector2f> f) {
		return Vector2f.rectangular((float) f.applyAsDouble(x, UNIT_X),
									(float) f.applyAsDouble(y, UNIT_Y));
	}

	/**
	 * Returns the quadrant of the given vector, or null if it lies on an axis.
	 * 
	 * @return the quadrant
	 */
	public Quadrant quadrant() {
		for (Quadrant q : Quadrant.values()) {
			if (Math.signum(x) != Math.signum(q.signVector.x)) continue;
			if (Math.signum(y) != Math.signum(q.signVector.y)) continue;
			return q;
		}
		return null;
	}

	/**
	 * Returns a string representation of the vector in rectangular coordinates.
	 * 
	 * @return a string representation of the vector
	 */
	public String toRectangularString() {
		return "Vector2f[x= " + this.getX() + ", y=" + this.getY() + "]";
	}

	/**
	 * Returns a string representation of the vector in polar coordinates.
	 * 
	 * @return a string representation of the vector
	 */
	public String toPolarString() {
		return "Vector2f[radius=" + this.getRadius() + ", angle=" + this.getAngle() + "]";
	}


	// Overridden Methods ====================================================
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Vector2f[" + x + ", " + y + "]";
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector2f other = (Vector2f) obj;
		if (Math.abs(x - other.x) > epsilon) return false;
		if (Math.abs(y - other.y) > epsilon) return false;
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
		Vector2f.epsilon = Math.abs(epsilon);
	}


	// Enumerations ==========================================================
	public static enum Quadrant {
		// @formatter:off
		PP (Vector2f.rectangular( 1,  1)),
		PN (Vector2f.rectangular( 1, -1)),
		NN (Vector2f.rectangular(-1, -1)),
		NP (Vector2f.rectangular(-1,  1));
		// @formatter:on

		public Vector2f signVector;

		Quadrant(Vector2f signVector) {
			this.signVector = signVector;
		}
	}
}

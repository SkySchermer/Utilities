/**
 * File: Matrix4f.java
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * An implementation of a 4x4 float matrix for use in graphics calculations.
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
 * {@link Matrix4f#of(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)}
 * : Constructs a Matrix4f from its entries.
 * <p>
 * {@link Matrix4f#ofRows(Vector4f[])}: Constructs a Matrix4f from an array of
 * row vectors.
 * <p>
 * {@link Matrix4f#ofColumns(Vector4f[])}: Constructs a Matrix4f from an array
 * of column vectors.
 * <p>
 * {@link Matrix4f#identity()}: Constructs a mutable identity matrix.
 * <p>
 * {@link Matrix4f#filledWith(float)}: Constructs a Matrix4f filled with the
 * given value.
 * <p>
 * {@link Matrix4f#filledUsing(ToDoubleFunction)}: Constructs a Matrix4f with
 * each entry determined by the given function.
 * 
 * @version 0.1.8 (25 June 2015)
 * @author Skylor R Schermer
 */
public class Matrix4f implements Serializable {

	// Constants =============================================================
	private static final long serialVersionUID = 18L;

	/** The number of rows in the matrix. */
	public static final int ROWS = 4;
	/** The number of columns in the matrix. */
	public static final int COLUMNS = 4;

	/** The zero matrix. */
	public static final Matrix4f ZERO = new Matrix4f(0, 0, 0, 0,
													 0, 0, 0, 0,
													 0, 0, 0, 0,
													 0, 0, 0, 0);
	/** The identity matrix. */
	public static final Matrix4f IDENTITY = new Matrix4f(1, 0, 0, 0,
														 0, 1, 0, 0,
														 0, 0, 1, 0,
														 0, 0, 0, 1);
	/** An upper triangular matrix mask. */
	public static final Matrix4f UPPER_TRIANGLE = new Matrix4f(0, 1, 1, 1,
															   0, 0, 1, 1,
															   0, 0, 0, 1,
															   0, 0, 0, 0);
	/** An lower triangular matrix mask. */
	public static final Matrix4f LOWER_TRIANGLE = new Matrix4f(0, 0, 0, 0,
															   1, 0, 0, 0,
															   1, 1, 0, 0,
															   1, 1, 1, 0);


	// Static Fields =========================================================
	/** Floating point precision for equality comparisons. */
	private static float epsilon = 0;


	// Fields ================================================================
	private float[] entries;


	// Constructors ==========================================================
	/**
	 * Constructs a Matrix4f from its total internal state.
	 */
	private Matrix4f(float... entries) {
		this.entries = entries;
	}

	/**
	 * Constructs an empty Matrix4f.
	 */
	private Matrix4f() {
		this.entries = new float[ROWS * COLUMNS];
	}


	// Static Constructors ===================================================
	/**
	 * Constructs a Matrix4f from its entries;
	 * 
	 * @param entry00
	 *        the entry in the 1st row and 1st column
	 * @param entry01
	 *        the entry in the 1st row and 2nd column
	 * @param entry02
	 *        the entry in the 1st row and 3rd column
	 * @param entry03
	 *        the entry in the 1st row and 4th column
	 * @param entry10
	 *        the entry in the 2nd row and 1st column
	 * @param entry11
	 *        the entry in the 2nd row and 2nd column
	 * @param entry12
	 *        the entry in the 2nd row and 3rd column
	 * @param entry13
	 *        the entry in the 2nd row and 4th column
	 * @param entry20
	 *        the entry in the 3rd row and 1st column
	 * @param entry21
	 *        the entry in the 3rd row and 2nd column
	 * @param entry22
	 *        the entry in the 3rd row and 3rd column
	 * @param entry23
	 *        the entry in the 3rd row and 4th column
	 * @param entry30
	 *        the entry in the 4th row and 1st column
	 * @param entry31
	 *        the entry in the 4th row and 2nd column
	 * @param entry32
	 *        the entry in the 4th row and 3rd column
	 * @param entry33
	 *        the entry in the 4th row and 4th column
	 */
	public static Matrix4f of(float entry00, float entry01, float entry02, float entry03,
							  float entry10, float entry11, float entry12, float entry13,
							  float entry20, float entry21, float entry22, float entry23,
							  float entry30, float entry31, float entry32, float entry33) {

		return new Matrix4f(entry00, entry01, entry02, entry03,
							entry10, entry11, entry12, entry13,
							entry20, entry21, entry22, entry23,
							entry30, entry31, entry32, entry33);
	}

	/**
	 * Constructs a Matrix4f from an array of row vectors.
	 * 
	 * @param rows
	 *        an array of row vectors
	 * @return the new matrix
	 */
	public static Matrix4f ofRows(Vector4f... rows) {
		if (rows.length != ROWS) {
			throw new IllegalArgumentException("Invalid row dimension: expected " +
											   Matrix4f.ROWS + " rows.");
		}

		Matrix4f mat = Matrix4f.filledWith(0);

		for (int i = 0; i < ROWS; i++) {
			mat.setEntryDirect(0, i, rows[i].getX());
			mat.setEntryDirect(1, i, rows[i].getY());
			mat.setEntryDirect(2, i, rows[i].getZ());
			mat.setEntryDirect(3, i, rows[i].getW());
		}

		return mat;
	}

	/**
	 * Constructs a Matrix4f from an array of column vectors.
	 * 
	 * @param rows
	 *        an array of column vectors
	 * @return the new matrix
	 */
	public static Matrix4f ofColumns(Vector4f... columns) {
		if (columns.length != COLUMNS) {
			throw new IllegalArgumentException("Invalid column dimension: expected " +
											   COLUMNS + " columns.");
		}
		Matrix4f mat = Matrix4f.filledWith(0);

		for (int i = 0; i < COLUMNS; i++) {
			mat.setEntryDirect(i, 0, columns[i].getX());
			mat.setEntryDirect(i, 1, columns[i].getY());
			mat.setEntryDirect(i, 2, columns[i].getZ());
			mat.setEntryDirect(i, 3, columns[i].getW());
		}

		return mat;
	}

	/**
	 * Constructs a Matrix4f as the identity matrix.
	 * 
	 * @return the new matrix
	 */
	public static Matrix4f identity() {
		return new Matrix4f(1, 0, 0, 0,
							0, 1, 0, 0,
							0, 0, 1, 0,
							0, 0, 0, 1);
	}

	/**
	 * Constructs a Matrix4f with each entry set to the given value.
	 * 
	 * @param value
	 *        the value to use for each entry
	 * @return the new matrix
	 */
	public static Matrix4f filledWith(float value) {
		return Matrix4f.filledUsing(entry -> {
			return value;
		});
	}

	/**
	 * Constructs a Matrix4f with each entry determined by the given function.
	 * 
	 * @param f
	 *        a function taking a {@link Matrix4f.Entry} and returning a value
	 *        for that entry
	 * @return the new matrix
	 */
	public static Matrix4f filledUsing(ToDoubleFunction<Entry> f) {
		/* This uses the zero matrix as a convenience. The value of each entry
		 * is simply ignored. */
		return ZERO.eachEntry((value, entry) -> {
			return f.applyAsDouble(entry);
		});
	}

	/**
	 * Constructs a Matrix4f from the product of an array of matrices. The
	 * matrices are multiplied left associatively. That is, given matrices
	 * {@code a}, {@code b}, and {@code c}, the result will be equivalent to
	 * {@code c.multiply(b.multiply(a))}. If no matrices are supplied, the
	 * result is the identity matrix, and if only one is supplied, it is
	 * returned unchanged.
	 * 
	 * @param matrices
	 *        the matrices to multiply
	 * @return the product matrix
	 */
	public static Matrix4f product(Matrix4f... matrices) {
		/* Return a new identity matrix for empty product. */
		if (matrices.length == 0) return Matrix4f.identity();
		/* Return the original matrix for singular product. */
		if (matrices.length == 1) return matrices[0];

		Matrix4f mat = matrices[0];
		for (int i = 1; i < matrices.length; i++) {
			mat = matrices[i].multiply(mat);
		}
		return mat;
	}

	// Accessor Methods ======================================================
	/**
	 * Returns the entry in the ith row and jth column.
	 * 
	 * @param row
	 *        the row of the entry
	 * @param column
	 *        the column of the entry
	 * @return the value of the entry
	 */
	public float getEntry(int row, int column) {
		if (row < 0 || row >= ROWS || column < 0 || column >= COLUMNS) {
			throw new IllegalArgumentException("Matrix index out of bounds.");
		}

		return entries[row + ROWS * column];
	}

	/**
	 * Returns the entry corresponding to the given {@link Matrix4f.Entry}.
	 * 
	 * @param index
	 *        the index of the entry
	 * @return the value of the entry
	 */
	public float getEntry(Entry index) {
		return getEntry(index.row, index.column);
	}

	/**
	 * Sets the entry in the ith row and jth column to the given value.
	 * 
	 * @param row
	 *        the row of the entry
	 * @param column
	 *        the column of the entry
	 * @param value
	 *        the value to set the entry to
	 */
	public void setEntry(int row, int column, float value) {
		if (this == ZERO) throw new UnsupportedOperationException("Matrix4f.ZERO is immutable.");
		if (this == IDENTITY) throw new UnsupportedOperationException("Matrix4f.IDENTITY is immutable.");
		if (this == UPPER_TRIANGLE) throw new UnsupportedOperationException("Matrix4f.UPPER_TRIANGLE is immutable.");
		if (this == LOWER_TRIANGLE) throw new UnsupportedOperationException("Matrix4f.LOWER_TRIANGLE is immutable.");

		if (row < 0 || row >= ROWS || column < 0 || column >= COLUMNS) {
			throw new IllegalArgumentException("Matrix index out of bounds.");
		}

		setEntryDirect(row, column, value);
	}

	/**
	 * Sets the entry corresponding to the given {@link Matrix4f.Entry} to the
	 * given value.
	 * 
	 * @param index
	 *        the index of the entry
	 * @param value
	 *        the value to set the entry to
	 */
	public void setEntry(Entry index, float value) {
		setEntry(index.row, index.column, value);
	}

	/**
	 * Sets the entry in the ith row and jth column to the given value without
	 * performing immutability and bounds checks.
	 * 
	 * @param row
	 *        the row of the entry
	 * @param column
	 *        the column of the entry
	 * @param value
	 *        the value to set the entry to
	 */
	private void setEntryDirect(int row, int column, float value) {
		/* Remove -0.0 from possible values. */
		if (abs(value) <= epsilon) value += 0.0f;

		entries[row + ROWS * column] = value;
	}


	// Methods ===============================================================
	/**
	 * Returns the trace of the matrix. (The sum of the entries on the main
	 * diagonal.)
	 * 
	 * @return the trace
	 */
	public float trace() {
		return this.getEntry(0, 0) +
			   this.getEntry(1, 1) +
			   this.getEntry(2, 2) +
			   this.getEntry(3, 3);
	}

	/**
	 * Returns the determinant of the matrix.
	 * 
	 * @return the determinant
	 */
	public float determinant() {
		// @formatter:off
		float a11 = getEntry(0,0), a12 = getEntry(0,1), a13 = getEntry(0,2), a14 = getEntry(0,3);
		float a21 = getEntry(1,0), a22 = getEntry(1,1), a23 = getEntry(1,2), a24 = getEntry(1,3);
		float a31 = getEntry(2,0), a32 = getEntry(2,1), a33 = getEntry(2,2), a34 = getEntry(2,3);
		float a41 = getEntry(3,0), a42 = getEntry(3,1), a43 = getEntry(3,2), a44 = getEntry(3,3);
		
		return   a11*a22*a33*a44 + a11*a23*a34*a42 + a11*a24*a32*a43
			   + a12*a21*a34*a43 + a12*a23*a31*a44 + a12*a24*a33*a41
			   + a13*a21*a32*a44 + a13*a22*a34*a41 + a13*a24*a31*a42
			   + a14*a21*a33*a42 + a14*a22*a31*a43 + a14*a23*a32*a41
			   - a11*a22*a34*a43 - a11*a23*a32*a44 - a11*a24*a33*a42
			   - a12*a21*a33*a44 - a12*a23*a34*a41 - a12*a24*a31*a43
			   - a13*a21*a34*a43 - a13*a22*a31*a44 - a13*a24*a32*a41
			   - a14*a21*a32*a42 - a14*a22*a33*a41 - a14*a23*a31*a42;
		// @formatter:on
	}

	/**
	 * Returns the sum of two matrices.
	 * 
	 * @param matrix
	 *        the matrix to add
	 * @return the matrix sum
	 */
	public Matrix4f add(Matrix4f matrix) {
		return this.eachEntry((entry, index) -> {
			return entry + matrix.getEntry(index.row, index.column);
		});
	}

	/**
	 * Returns the matrix produced by adding the value to each entry.
	 * 
	 * @param value
	 *        the value to add
	 * @return the new matrix
	 */
	public Matrix4f add(float value) {
		return this.eachEntry(entry -> {
			return (float) entry + value;
		});
	}

	/**
	 * Returns the difference of two matrices.
	 * 
	 * @param matrix
	 *        the matrix to subtract
	 * @return the matrix difference
	 */
	public Matrix4f subtract(Matrix4f matrix) {
		return this.eachEntry((entry, index) -> {
			return entry - matrix.getEntry(index.row, index.column);
		});
	}

	/**
	 * Returns the matrix produced by subtracting the value from each entry.
	 * 
	 * @param value
	 *        the value to subtract
	 * @return the new matrix
	 */
	public Matrix4f subtract(float value) {
		return this.eachEntry(entry -> {
			return (float) entry - value;
		});
	}

	/**
	 * Returns the product of two matrices.
	 * 
	 * @param matrix
	 *        the matrix to multiply
	 * @return the matrix product
	 */
	public Matrix4f multiply(Matrix4f matrix) {
		return Matrix4f.filledUsing(index -> {
			return this.getEntry(0, index.column) * matrix.getEntry(index.row, 0) +
				   this.getEntry(1, index.column) * matrix.getEntry(index.row, 1) +
				   this.getEntry(2, index.column) * matrix.getEntry(index.row, 2) +
				   this.getEntry(3, index.column) * matrix.getEntry(index.row, 3);
		});
	}

	/**
	 * Returns the vector produced my multiplying the matrix by a vector.
	 * 
	 * @param vector
	 *        the vector to multiply
	 * @return the vector result
	 */
	public Vector4f multiply(Vector4f vector) {
		// @formatter:off
		return Vector4f.rectangular(getEntry(0, 0) * vector.getX() + getEntry(1, 0) * vector.getY() +
									getEntry(2, 0) * vector.getZ() + getEntry(3, 0) * vector.getW(),

									getEntry(0, 1) * vector.getX() + getEntry(1, 1) * vector.getY() +
									getEntry(2, 1) * vector.getZ() + getEntry(3, 1) * vector.getW(),

									getEntry(0, 2) * vector.getX() + getEntry(1, 2) * vector.getY() +
									getEntry(2, 2) * vector.getZ() + getEntry(3, 2) * vector.getW(),

									getEntry(0, 3) * vector.getX() + getEntry(1, 3) * vector.getY() +
									getEntry(2, 3) * vector.getZ() + getEntry(3, 3) * vector.getW());
		// @formatter:on 
	}

	/**
	 * Returns the matrix produced by multiplying the value to each entry.
	 * 
	 * @param value
	 *        the value to multiply
	 * @return the new matrix
	 */
	public Matrix4f multiply(float value) {
		return this.eachEntry(entry -> {
			return (float) entry * value;
		});
	}

	/**
	 * Returns the matrix produced by taking the absolute value of each entry.
	 * 
	 * @param value
	 *        the value to divide by
	 * @return the new matrix
	 */
	public Matrix4f divide(float value) {
		return this.eachEntry(entry -> {
			return (float) entry / value;
		});
	}

	/**
	 * Returns the matrix produced by applying the given function to each entry.
	 * 
	 * @param f
	 *        the function to apply
	 * @return the new matrix
	 */
	public Matrix4f eachEntry(DoubleFunction<Float> f) {
		return Matrix4f.filledUsing(entry -> {
			return f.apply(this.getEntry(entry));
		});
	}

	/**
	 * Returns the matrix produced by applying the given function to each entry.
	 * This method provides the row and column indices in a
	 * {@link Matrix4f.Entry} object as the second argument to the function.
	 * Each entry is processed in parallel.
	 * 
	 * @param f
	 *        the function to apply
	 * @return the new matrix
	 */
	public Matrix4f eachEntry(ToDoubleBiFunction<Float, Entry> f) {
		Matrix4f mat = new Matrix4f();
		/* Process each entry in parallel. */
		IntStream.range(0, ROWS).parallel().forEach(row -> {
			IntStream.range(0, COLUMNS).parallel().forEach(column -> {
				mat.setEntryDirect(row, column, (float) f.applyAsDouble(this.entries[row],
																		new Entry(row, column)));
			});
		});
		return mat;
	}

	/**
	 * Returns the transpose of the matrix.
	 * 
	 * @return the transpose of the matrix
	 */
	public Matrix4f transpose() {
		return new Matrix4f(getEntry(0, 0), getEntry(1, 0), getEntry(2, 0), getEntry(3, 0),
							getEntry(0, 1), getEntry(1, 1), getEntry(2, 1), getEntry(3, 1),
							getEntry(0, 2), getEntry(1, 2), getEntry(2, 2), getEntry(3, 2),
							getEntry(0, 3), getEntry(1, 3), getEntry(2, 3), getEntry(3, 3));
	}

	/**
	 * Returns the matrix with the entries arranged in an array with row-major
	 * order.
	 * 
	 * @return an array of entries
	 */
	public float[] asRowMajorArray() {
		return Arrays.copyOf(entries, ROWS * COLUMNS);
	}

	/**
	 * Returns the matrix with the entries arranged in an array with
	 * column-major order.
	 * 
	 * @return an array of entries
	 */
	public float[] asColumnMajorArray() {
		return Arrays.copyOf(this.transpose().entries, ROWS * COLUMNS);
	}

	/**
	 * Returns the matrix with the entries arranged in an array of row arrays.
	 * 
	 * @return an array of arrays of entries
	 */
	public float[][] asRowArray() {
		return new float[][] {new float[] {getEntry(0, 0), getEntry(1, 0), getEntry(2, 0), getEntry(3, 0)},
							  new float[] {getEntry(0, 1), getEntry(1, 1), getEntry(2, 1), getEntry(3, 1)},
							  new float[] {getEntry(0, 2), getEntry(1, 2), getEntry(2, 2), getEntry(3, 2)},
							  new float[] {getEntry(0, 3), getEntry(1, 3), getEntry(2, 3), getEntry(3, 3)}};
	}

	/**
	 * Returns the matrix with the entries arranged in an array of column
	 * arrays.
	 * 
	 * @return an array of arrays of entries
	 */
	public float[][] asColumnArray() {
		return new float[][] {new float[] {getEntry(0, 0), getEntry(0, 1), getEntry(0, 2), getEntry(0, 3)},
							  new float[] {getEntry(1, 0), getEntry(1, 1), getEntry(1, 2), getEntry(1, 3)},
							  new float[] {getEntry(2, 0), getEntry(2, 1), getEntry(2, 2), getEntry(2, 3)},
							  new float[] {getEntry(3, 0), getEntry(3, 1), getEntry(3, 2), getEntry(3, 3)}};
	}


	// Overridden Methods ====================================================
	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Matrix4f[");
		for (float[] row : this.asRowArray()) {
			for (float entry : row) {
				sb.append(entry);
				sb.append(", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append("; ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("]");
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(entries);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Matrix4f other = (Matrix4f) obj;
		if (entries.length != other.entries.length) return false;
		for (int i = 0; i < entries.length; i++) {
			if (abs(entries[i] - other.entries[i]) > epsilon) return false;
		}
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
		Matrix4f.epsilon = Math.abs(epsilon);
	}


	// Internal Classes ======================================================
	/**
	 * Represents row and column indices for matrix entries.
	 */
	public static class Entry {
		private int row;
		private int column;

		private Entry(int row, int column) {
			this.row = row;
			this.column = column;
		}

		/**
		 * Returns the row index for the entry.
		 * 
		 * @return the row index
		 */
		public int getRow() {
			return row;
		}

		/**
		 * Returns the column index for the entry.
		 * 
		 * @return the column index
		 */
		public int getColumn() {
			return column;
		}
	}
}

/**
 * File: Utility.java
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

package schermer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Provides common static functions.
 * 
 * @version 0.1.5 (12 June 2014)
 * @author Skylor R Schermer
 */
public class Utility {
	// Constructors ==========================================================
	/** Private constructor prevents instantiation. */
	private Utility() {
	}

	// Static Methods =========================================================
	/**
	 * Clamps the value between upper and lower bounds.
	 * 
	 * @param n
	 *        the number to clamp
	 * @param lowerBound
	 *        the lower bound
	 * @param upperBound
	 *        the upper bound
	 * @return the given value clamped between the lower and upper bounds
	 */
	public static int clamp(int n, int lowerBound, int upperBound) {
		int v;
		v = n < lowerBound ? lowerBound : n;
		v = n > upperBound ? upperBound : v;
		return v;
	}

	/**
	 * Clamps the value between upper and lower bounds.
	 * 
	 * @param n
	 *        the number to clamp
	 * @param lowerBound
	 *        the lower bound
	 * @param upperBound
	 *        the upper bound
	 * @return the given value clamped between the lower and upper bounds
	 */
	public static double clamp(double n, double lowerBound, double upperBound) {
		double v;
		v = n < lowerBound ? lowerBound : n;
		v = n > upperBound ? upperBound : v;
		return v;
	}
	
	/**
	 * Clamps the value between upper and lower bounds.
	 * 
	 * @param n
	 *        the number to clamp
	 * @param lowerBound
	 *        the lower bound
	 * @param upperBound
	 *        the upper bound
	 * @return the given value clamped between the lower and upper bounds
	 */
	public static float clamp(float n, float lowerBound, float upperBound) {
		float v;
		v = n < lowerBound ? lowerBound : n;
		v = n > upperBound ? upperBound : v;
		return v;
	}

	/**
	 * Returns a copy of the given {@link BufferedImage}.
	 * 
	 * @param image
	 *        the image to copy
	 * @return a copy of the given image
	 */
	public static BufferedImage copyImage(BufferedImage image) {
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Copy the graphics.
		Graphics gfx = bi.getGraphics();
		gfx.drawImage(image, 0, 0, null);
		gfx.dispose();

		return bi;
	}

	/**
	 * Returns a new array of the concatenation of the given arrays.
	 * 
	 * @param arrays
	 *        the arrays to concatenate
	 * @return a new concatenated array
	 */
	public static byte[] join(byte[]... arrays) {
		int newLength = 0;
		for (byte[] array : arrays) {
			newLength += array.length;
		}
		byte[] newArray = new byte[newLength];

		int i = 0;
		for (byte[] array : arrays) {
			for (byte b : array) {
				newArray[i] = b;
				i++;
			}
		}

		return newArray;
	}

	/**
	 * Returns the index of the greatest number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the index of the greatest number.
	 */
	public static int indexOfMax(double... nums) {
		int maxIndex = 0;
		double max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > max) {
				max = nums[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Returns the index of the greatest number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the index of the greatest number.
	 */
	public static int indexOfMax(float... nums) {
		int maxIndex = 0;
		float max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > max) {
				max = nums[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Returns the index of the greatest number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the index of the greatest number.
	 */
	public static int indexOfMax(int... nums) {
		int maxIndex = 0;
		double max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > max) {
				max = nums[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Returns the index of the least number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the index of the least number.
	 */
	public static int indexOfMin(double... nums) {
		int maxIndex = 0;
		double max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] < max) {
				max = nums[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Returns the index of the least number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the index of the least number.
	 */
	public static int indexOfMin(float... nums) {
		int maxIndex = 0;
		float max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] < max) {
				max = nums[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Returns the index of the least number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the index of the least number.
	 */
	public static int indexOfMin(int... nums) {
		int maxIndex = 0;
		double max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] < max) {
				max = nums[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Returns the greatest number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the greatest number
	 */
	public static double maxOf(double... nums) {
		return nums[indexOfMax(nums)];
	}
	
	/**
	 * Returns the greatest number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the greatest number
	 */
	public static float maxOf(float... nums) {
		return nums[indexOfMax(nums)];
	}

	/**
	 * Returns the greatest number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the greatest number
	 */
	public static int maxOf(int... nums) {
		return nums[indexOfMax(nums)];
	}
	
	/**
	 * Returns the least number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the greatest number
	 */
	public static double minOf(double... nums) {
		return nums[indexOfMin(nums)];
	}
	
	/**
	 * Returns the least number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the greatest number
	 */
	public static float minOf(float... nums) {
		return nums[indexOfMin(nums)];
	}

	/**
	 * Returns the least number in an array.
	 * 
	 * @param nums
	 *        the numbers
	 * @return the greatest number
	 */
	public static int minOf(int... nums) {
		return nums[indexOfMin(nums)];
	}
	
	/**
	 * Unboxes an array of {@link Integer}s into an array of {@code int}s.
	 * @param array the array to unbox
	 * @return an array of ints
	 */
	public static int[] unboxAll(Integer[] array) {
		int[] result = new int[array.length];
		for (int i =0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
}

/**
 * File: Color.java
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
package schermer.color;

import static schermer.Utility.clamp;
import static schermer.Utility.indexOfMax;
import static schermer.Utility.maxOf;
import static schermer.Utility.minOf;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import schermer.UnreachableCodeException;
import schermer.math.Matrix3f;
import schermer.math.Vector3f;


/**
 * The Color class is used to encapsulate a single color.
 * <p>
 * <b> Constructors: </b>
 * <p>
 * <p>
 * {@link Color#named(String)}: Constructs a color by looking up the given name
 * in a color database.
 * <p>
 * {@link Color#fromAwtColor(java.awt.Color)}: Constructs a color from a given
 * awt.Color instance.
 * <p>
 * {@link Color#parseRgbHex(int)}: Constructs a color from a 6-digit RGB hex
 * value string.
 * <p>
 * {@link Color#fromRgbHex(int)}: Constructs a color from a 6-digit RGB hex
 * value.
 * <p>
 * {@link Color#fromRgbOctet(int, int, int)}: Constructs a color from RGB values
 * in the range 0-255.
 * <p>
 * {@link Color#fromRgb(float, float, float)}: Constructs a color from RGB
 * proportions.
 * <p>
 * {@link Color#fromRgbPercent(int, int, int)}: Constructs a color from RGB
 * proportions as percents.
 * <p>
 * {@link Color#fromRgbVector(Vector3f)}: Construcs a color from an RGB
 * component vector.
 * <p>
 * {@link Color#fromHsv(float, float, float)}: Constructs a color from HSV
 * proportions.
 * <p>
 * {@link Color#fromHsvPercent(float, int, int)}: Constructs a color from HSV
 * proportions as percents.
 * <p>
 * {@link Color#fromHsl(float, float, float)}: Constructs a color from HSL
 * proportions.
 * <p>
 * {@link Color#fromHslPercent(float, int, int)}: Constructs a color from RSL
 * proportions as percents.
 * <p>
 * {@link Color#fromCmyk(float, float, float, float)}: Constructs a color from
 * CMYK proportions.
 * <p>
 * {@link Color#fromCmykPercent(int, int, int, int)}: Constructs a color from
 * CMYK proportions as percents.
 * 
 * @version 0.2.2 (8 July 2015)
 * @author Skylor R Schermer
 */
public class Color implements Serializable {

	// Constants =============================================================
	private static final long serialVersionUID = 22L;
	/** Regex pattern string for parsers. Matches a six-digit hex code. */
	public static final String COLOR_PATTERN = "#([0-9a-fA-F]{6})";

	private static final int COMPONENT_BIT_MASK = 0xFF;

	private static final int ENCODING_RED_OFFSET = 16;
	private static final int ENCODING_BLUE_OFFSET = 8;
	private static final int ENCODING_GREEN_OFFSET = 0;

	private static final int ENCODING_RED_BIT_MASK = COMPONENT_BIT_MASK << ENCODING_RED_OFFSET;
	private static final int ENCODING_BLUE_BIT_MASK = COMPONENT_BIT_MASK << ENCODING_BLUE_OFFSET;
	private static final int ENCODING_GREEN_BIT_MASK = COMPONENT_BIT_MASK << ENCODING_GREEN_OFFSET;
	private static final int ENCODING_RGB_BIT_MASK = ENCODING_RED_BIT_MASK |
													 ENCODING_GREEN_BIT_MASK |
													 ENCODING_BLUE_BIT_MASK;

	/** Function for computing the distance between two colors in RGB space. */
	public static final ToDoubleBiFunction<Color, Color> RGB_DISTANCE_FUNCTION = (a, b) -> {
		return a.hslDistanceTo(b);
	};

	/** Function for computing the distance between two colors in HSL space. */
	public static final ToDoubleBiFunction<Color, Color> HSL_DISTANCE_FUNCTION = (a, b) -> {
		return a.hslDistanceTo(b);
	};

	// Static Fields =========================================================
	private static ColorNameSource colorNameSource = null;
	private static String colorNameFile = ColorNameSource.DEFAULT_COLOR_NAME_FILE;


	// Fields ================================================================
	private int srgbEncoding;


	// Constructors ==========================================================
	/**
	 * Constructs a Color from its total internal state.
	 * 
	 * @param srgbaEncoding
	 *        the SRGB+A encoding of the color.
	 */
	private Color(int srgbaEncoding) {

		this.srgbEncoding = srgbaEncoding;
	}


	// Static Constructors ===================================================
	/**
	 * Constructs a Color by looking in a color name database.
	 * 
	 * @param name
	 *        the name of the color
	 * @return a Color instance with the associated name or {@code null} if the
	 *         named color is not present.
	 */
	public static Color named(String name) {
		if (colorNameSource == null) colorNameSource = ColorNameSource.fromFile(colorNameFile);
		return colorNameSource.getColor(name);
	}

	/**
	 * Constructs a Color by converting from a {@link java.awt.Color} instance.
	 * 
	 * @param color
	 *        the color to convert
	 * @return a new Color
	 * @see java.awt.Color
	 */
	public static Color fromAwtColor(java.awt.Color color) {
		return Color.fromRgbOctet(color.getRed(),
								  color.getGreen(),
								  color.getBlue());
	}

	/**
	 * Constructs a color from a six digit RGB hex code string.
	 * 
	 * @param hexString
	 *        the hexcode of the color as a # prefixed string
	 * @return a new Color
	 */
	public static Color parseRgbHex(String hexString) {
		Matcher m = Pattern.compile(COLOR_PATTERN, Pattern.CASE_INSENSITIVE)
						   .matcher(hexString);
		if (m.matches()) {
			return Color.fromRgbHex(Integer.parseInt(m.group(1), 16));
		}
		throw new IllegalArgumentException("Unkown RGB hex format.");
	}

	/**
	 * Constructs a color from a six digit RGB hex code.
	 * 
	 * @param hexCode
	 *        the hexcode of the color
	 * @return a new Color
	 */
	public static Color fromRgbHex(int hexCode) {
		// @formatter:off
		return Color.fromRgb(((float) ((hexCode & 0xFF0000) >> 16) / 255),
		                     ((float) ((hexCode & 0x00FF00) >>  8) / 255),
		                     ((float) ( hexCode & 0x0000FF       ) / 255));
		// @formatter:on
	}

	/**
	 * Constructs a color from 8-bit RGB values. The input values are clamped to
	 * the range 0-255.
	 * 
	 * @param red
	 *        the red component of the color
	 * @param green
	 *        the green component of the color
	 * @param blue
	 *        the blue component of the color
	 * @return a new Color
	 */
	public static Color fromRgbOctet(int red, int green, int blue) {
		// @formatter:off
		return Color.fromRgb((float) red   / 255,
		                     (float) green / 255,
		                     (float) blue  / 255);
		// @formatter:on
	}

	/**
	 * Constructs a color from RGB proportions. The input values are clamped to
	 * the
	 * range 0-1.
	 * 
	 * @param red
	 *        the red component of the color
	 * @param green
	 *        the green component of the color
	 * @param blue
	 *        the blue component of the color
	 * @return a new Color
	 */
	public static Color fromRgb(float red, float green, float blue) {
		// @formatter:off		
		red   = clamp(red,   0, 1);
		green = clamp(green, 0, 1);
		blue  = clamp(blue,  0, 1);
		return new Color( ((int) (red   * COMPONENT_BIT_MASK) << ENCODING_RED_OFFSET)
		                | ((int) (green * COMPONENT_BIT_MASK) << ENCODING_GREEN_OFFSET)
						| ((int) (blue  * COMPONENT_BIT_MASK) << ENCODING_BLUE_OFFSET));
		// @formatter:on
	}

	/**
	 * Constructs a color from RGB percentages. The input values are clamped to
	 * the
	 * range 0-100.
	 * 
	 * @param red
	 *        the red component of the color
	 * @param green
	 *        the green component of the color
	 * @param blue
	 *        the blue component of the color
	 * @return a new Color
	 */
	public static Color fromRgbPercent(int red, int green, int blue) {
		// @formatter:off
		return Color.fromRgb((float) red   / 100,
		                     (float) green / 100,
		                     (float) blue  / 100);
		// @formatter:on
	}


	/**
	 * Constructs a color from an RGB component vector. The components are
	 * clamped to the range 0-1.
	 * 
	 * @param colorVector
	 *        the color vector
	 * @return a new Color
	 */
	public static Color fromRgbVector(Vector3f colorVector) {
		return Color.fromRgb(colorVector.getX(),
							 colorVector.getY(),
							 colorVector.getZ());
	}


	/**
	 * Constructs a color from HSV proportions. The saturation and key values
	 * are
	 * clamped to the range 0-1.
	 * 
	 * @param hue
	 *        the hue angle of the color
	 * @param saturation
	 *        the saturation of the color in the HSV color space
	 * @param value
	 *        the value component of the color
	 * @return a new Color
	 */
	public static Color fromHsv(float hue, float saturation, float value) {
		// @formatter:off		
		hue        = (hue < 0) ? (hue + 360) % 360 : hue % 360;
		saturation = clamp(saturation, 0, 1);
		value      = clamp(value,      0, 1);
		// @formatter:on

		float c = saturation * value;
		float x = c * (1 - Math.abs((hue / 60) % 2 - 1));
		float m = value - c;
		// @formatter:off
		if (  0 <= hue && hue <  60) return Color.fromRgb(c+m, x+m,   m);
		if ( 60 <= hue && hue < 120) return Color.fromRgb(x+m, c+m,   m);
		if (120 <= hue && hue < 180) return Color.fromRgb(  m, c+m, x+m);
		if (180 <= hue && hue < 240) return Color.fromRgb(  m, x+m, c+m);
		if (240 <= hue && hue < 300) return Color.fromRgb(x+m,   m, c+m);
	  /*if (300 <= hue && hue < 360) return Color.fromRGB(c+m,   m, x+m);*/
									 return Color.fromRgb(c+m,   m, x+m);
		// @formatter:on
	}

	/**
	 * Constructs a color from HSV percentages. The saturation and key values
	 * are
	 * clamped to the range 0-100.
	 * 
	 * @param hue
	 *        the hue angle of the color
	 * @param saturation
	 *        the saturation of the color in the HSV color space
	 * @param value
	 *        the value component of the color
	 * @return a new Color
	 */
	public static Color fromHsvPercent(float hue, int saturation, int value) {
		return Color.fromHsv((float) hue * 3.6f,
							 (float) saturation / 100,
							 (float) value / 100);
	}


	/**
	 * Constructs a color from HSL proportions. The saturation and lightness
	 * values
	 * are clamped to the range 0-1.
	 * 
	 * @param hue
	 *        the hue angle of the color
	 * @param saturation
	 *        the saturation of the color in the HSL color space
	 * @param lightnesss
	 *        the lightness component of the color
	 * @return a new Color
	 */
	public static Color fromHsl(float hue, float saturation, float lightness) {
		// @formatter:off		
		hue        = (hue < 0) ? (hue + 360) % 360 : hue % 360;
		saturation = clamp(saturation, 0, 1);
		lightness  = clamp(lightness,  0, 1);
		// @formatter:on

		float c = saturation * (1 - Math.abs(2 * lightness - 1));
		float x = c * (1 - Math.abs(hue / 60 % 2 - 1));
		float m = lightness - c / 2;

		// @formatter:off
		if (  0 <= hue && hue <  60) return Color.fromRgb(c+m, x+m,   m);
		if ( 60 <= hue && hue < 120) return Color.fromRgb(x+m, c+m,   m);
		if (120 <= hue && hue < 180) return Color.fromRgb(  m, c+m, x+m);
		if (180 <= hue && hue < 240) return Color.fromRgb(  m, x+m, c+m);
		if (240 <= hue && hue < 300) return Color.fromRgb(x+m,   m, c+m);
	  /*if (300 <= hue && hue < 360) return Color.fromRGB(c+m,   m, x+m);*/
									 return Color.fromRgb(c+m,   m, x+m);
		// @formatter:on
	}

	/**
	 * Constructs a color from HSL percentages. The saturation and lightness
	 * values
	 * are clamped to the range 0-100.
	 * 
	 * @param hue
	 *        the hue angle of the color
	 * @param saturation
	 *        the saturation of the color in the HSV color space
	 * @param lightness
	 *        the lightness component of the color
	 * @return a new Color
	 */
	public static Color fromHslPercent(float hue, int saturation, int lightness) {
		// @formatter:off
		return Color.fromHsl((float) hue        * 3.6f, 
		                     (float) saturation / 100, 
		                     (float) lightness  / 100);
		// @formatter:on
	}


	/**
	 * Construcs a color from CMYK proportions. The inpput values are clamped to
	 * the range 0-1.
	 * 
	 * @param cyan
	 *        the cyan component of the color
	 * @param magenta
	 *        the magenta component of the color
	 * @param yellow
	 *        the yellow component of the color
	 * @param key
	 *        the key (black) component of the color
	 * @return a new Color
	 */
	public static Color fromCmyk(float cyan, float magenta, float yellow, float key) {
		// @formatter:off
		cyan    = clamp(cyan,    0, 1);
		magenta = clamp(magenta, 0, 1);
		yellow  = clamp(yellow,  0, 1);
		key     = clamp(key,     0, 1);
		
		return Color.fromRgb((1-cyan)    * (1-key),
		                     (1-magenta) * (1-key),
		                     (1-yellow)  * (1-key));
		// @formatter:on
	}

	/**
	 * Constructs a color from CMYK percentages. The inpput values are clamped
	 * to
	 * the range 0-100.
	 * 
	 * @param cyan
	 *        the cyan component of the color
	 * @param magenta
	 *        the magenta component of the color
	 * @param yellow
	 *        the yellow component of the color
	 * @param key
	 *        the key (black) component of the color
	 * @return a new Color
	 */
	public static Color fromCmykPercent(int cyan, int magenta, int yellow, int key) {
		// @formatter:off
		return Color.fromCmyk((float) cyan    / 100, 
		                      (float) magenta / 100, 
		                      (float) yellow  / 100, 
		                      (float) key     / 100);
		// @formatter:on
	}


	// Accessor Methods ======================================================
	/**
	 * Returns the red component of the color in RGB color space.
	 * 
	 * @return the red component of the color
	 */
	public float getRed() {
		return (float) ((srgbEncoding & ENCODING_RED_BIT_MASK) >> ENCODING_RED_OFFSET) / COMPONENT_BIT_MASK;
	}

	/**
	 * Returns the green component of the color in RGB color space.
	 * 
	 * @return the green component of the color
	 */
	public float getGreen() {
		return (float) ((srgbEncoding & ENCODING_GREEN_BIT_MASK) >> ENCODING_GREEN_OFFSET) / COMPONENT_BIT_MASK;
	}

	/**
	 * Returns the blue component of the color in RGB color space.
	 * 
	 * @return the blue component of the color
	 */
	public float getBlue() {
		return (float) ((srgbEncoding & ENCODING_BLUE_BIT_MASK) >> ENCODING_BLUE_OFFSET) / COMPONENT_BIT_MASK;
	}


	/**
	 * Returns the RGB hex code of the color.
	 * 
	 * @return the hex code of the color
	 */
	public int getRgbHex() {
		return srgbEncoding & ENCODING_RGB_BIT_MASK;
	}


	/**
	 * Returns the 8 bit red component of the color.
	 * 
	 * @return the 8 bit red component of the color
	 */
	public int getRedOctet() {
		return (int) (getRed() * 255 + 0.5);
	}

	/**
	 * Returns the 8 bit green component of the color.
	 * 
	 * @return the 8 bit green component of the color
	 */
	public int getGreenOctet() {
		return (int) (getGreen() * 255 + 0.5);
	}

	/**
	 * Returns the 8 bit blue component of the color.
	 * 
	 * @return the 8 bit blue component of the color
	 */
	public int getBlueOctet() {
		return (int) (getBlue() * 255 + 0.5);
	}

	/**
	 * Returns the red component of the color as a percentage.
	 * 
	 * @return the red component of the color
	 */
	public int getRedPercent() {
		return (int) (getRed() * 100 + 0.5);
	}

	/**
	 * Returns the green component of the color as a percentage.
	 * 
	 * @return the green component of the color
	 */
	public int getGreenPercent() {
		return (int) (getGreen() * 100 + 0.5);
	}

	/**
	 * Returns the blue component of the color as a percentage.
	 * 
	 * @return the blue component of the color
	 */
	public int getBluePercent() {
		return (int) (getBlue() * 100 + 0.5);
	}

	/**
	 * Returns the hue angle of the color.
	 * 
	 * @return the hue angle of the color
	 */
	public float getHue() {
		/* No hue for grays. */
		if (Float.compare(getRed(), getBlue()) == 0 &&
			Float.compare(getBlue(), getGreen()) == 0) return 0;

		float res = 0;
		float delta = maxOf(getRed(), getGreen(), getBlue()) - minOf(getRed(), getGreen(), getBlue());

		// @formatter:off
		switch (indexOfMax(getRed(), getGreen(), getBlue())) {
			case 0: res = ((getGreen()-getBlue())/delta) % 6; break;
			case 1: res = ((getBlue()-getRed())/delta) + 2; break;
			case 2: res = ((getRed()-getGreen())/delta) + 4; break;
			default: throw new UnreachableCodeException();
		}
		// @formatter:on
		return (res * 60) % 360;
	}

	/**
	 * Returns the HSV saturation of the color.
	 * 
	 * @return the HSV saturation of the color
	 */
	public float getHsvSaturation() {
		float delta = maxOf(getRed(), getGreen(), getBlue()) - minOf(getRed(), getGreen(), getBlue());
		if (Float.compare(delta, 0) == 0) return 0;

		return delta / getValue();
	}

	/**
	 * Returns the HSL saturation of the color.
	 * 
	 * @return the HSL saturation of the color
	 */
	public float getHslSaturation() {
		float delta = maxOf(getRed(), getGreen(), getBlue()) - minOf(getRed(), getGreen(), getBlue());
		if (Float.compare(delta, 0) == 0) return 0;

		if (getLightness() > 0.5) {
			return delta / (2 - delta);
		} else {
			return delta / (maxOf(getRed(),
								  getGreen(),
								  getBlue()) + minOf(getRed(),
													 getGreen(),
													 getBlue()));
		}
	}

	/**
	 * Returns the value component of the color.
	 * 
	 * @return the value component of the color
	 */
	public float getValue() {
		return maxOf(getRed(), getGreen(), getBlue());
	}

	/**
	 * Returns the lightness component of the color.
	 * 
	 * @return the lightness component of the color
	 */
	public float getLightness() {
		return (maxOf(getRed(), getGreen(), getBlue()) + minOf(getRed(), getGreen(), getBlue())) / 2;
	}

	/**
	 * Returns the HSV saturation of the color.
	 * 
	 * @return the HSV saturation of the color
	 */
	public int getHsvSaturationPercent() {
		return (int) (getHsvSaturation() * 100 + 0.5);
	}

	/**
	 * Returns the HSL saturation of the color as a percent.
	 * 
	 * @return the HSL saturation of the color
	 */
	public int getHslSaturationPercent() {
		return (int) (getHslSaturation() * 100 + 0.5);
	}

	/**
	 * Returns the value component of the color as a percent.
	 * 
	 * @return the value component of the color
	 */
	public int getValuePercent() {
		return (int) (getValue() * 100 + 0.5);
	}

	/**
	 * Returns the lightness component of the color as a percent.
	 * 
	 * @return the lightness component of the color
	 */
	public int getLightnessPercent() {
		return (int) (getLightness() * 100 + 0.5);
	}

	/**
	 * Returns the cyan component of the color.
	 * 
	 * @return the cyan component of the color
	 */
	public float getCyan() {
		return (1 - getRed() - getKey()) / (1 - getKey());
	}

	/**
	 * Returns the magenta component of the color.
	 * 
	 * @return the magenta component of the color
	 */
	public float getMagenta() {
		return (1 - getGreen() - getKey()) / (1 - getKey());
	}

	/**
	 * Returns the yellow component of the color.
	 * 
	 * @return the yellow component of the color
	 */
	public float getYellow() {
		return (1 - getBlue() - getKey()) / (1 - getKey());
	}

	/**
	 * Returns the key component of the color.
	 * 
	 * @return the key component of the color
	 */
	public float getKey() {
		return 1 - maxOf(getRed(), getGreen(), getBlue());
	}

	/**
	 * Returns the cyan component of the color as a percentage.
	 * 
	 * @return the cyan component of the color
	 */
	public int getCyanPercent() {
		return (int) ((getCyan() * 100) + 0.5);
	}

	/**
	 * Returns the magenta component of the color as a percentage.
	 * 
	 * @return the magenta component of the color
	 */
	public int getMagentaPercent() {
		return (int) ((getMagenta() * 100) + 0.5);
	}

	/**
	 * Returns the yellow component of the color as a percentage.
	 * 
	 * @return the yellow component of the color
	 */
	public int getYellowPercent() {
		return (int) ((getYellow() * 100) + 0.5);
	}

	/**
	 * Returns the key component of the color as a percentage.
	 * 
	 * @return the key component of the color
	 */
	public int getKeyPercent() {
		return (int) ((getKey() * 100) + 0.5);
	}

	/**
	 * Returns the 8 bit cyan component of the color.
	 * 
	 * @return the 8 bit cyan component of the color
	 */
	public int getCyanOctet() {
		return (int) (getCyan() * 255 + 0.5);
	}

	/**
	 * Returns the 8 bit magenta component of the color.
	 * 
	 * @return the 8 bit magenta component of the color
	 */
	public int getMagentaOctet() {
		return (int) (getMagenta() * 255 + 0.5);
	}

	/**
	 * Returns the 8 bit yellow component of the color.
	 * 
	 * @return the 8 bit yellow component of the color
	 */
	public int getYellowOctet() {
		return (int) (getYellow() * 255 + 0.5);
	}

	/**
	 * Returns the 8 bit key component of the color.
	 * 
	 * @return the 8 bit key component of the color
	 */
	public int getKeyOctet() {
		return (int) (getKey() * 255 + 0.5);
	}

	// Methods ===============================================================
	/**
	 * Returns the color as a {@link java.awt.Color}.
	 * 
	 * @return the AWT representation of the color.
	 * @see java.awt.Color
	 */
	public java.awt.Color asAwtColor() {
		return new java.awt.Color(getRgbHex());
	}

	/**
	 * Returns the color as an RGB {@code Vector3f}.
	 * 
	 * @return a vector of the color components
	 * @see schermer.math.Vector3f
	 */
	public Vector3f asRgbVector() {
		return Vector3f.rectangular(getRed(), getGreen(), getBlue());
	}

	/**
	 * Returns the color as an RGB array.
	 * 
	 * @return an array of the color components
	 */
	public float[] asRgbArray() {
		return new float[] {getRed(), getGreen(), getBlue()};
	}

	/**
	 * Returns a copy of the color lightened by the given amount. The amount is
	 * taken as the proportion (in the range 0-1) of the difference between the
	 * color and white.
	 * 
	 * @param amount
	 *        the amount to lighten by
	 * @return a lightened copy of the color
	 */
	public Color lighten(float amount) {
		float l = getLightness();
		return Color.fromHsl(getHue(), getHslSaturation(), l + (1 - l) * amount);
	}

	/**
	 * Returns a copy of the color darkened by the given amount. The amount is
	 * taken as the proportion (in the range 0-1) of the difference between the
	 * color and black.
	 * 
	 * @param amount
	 *        the amount to darken by
	 * @return a darkened copy of the color
	 */
	public Color darken(float amount) {
		float l = getLightness();
		return Color.fromHsl(getHue(), getHslSaturation(), l + (-l) * amount);
	}

	/**
	 * Returns a copy of the color saturated by the given amount. The amount is
	 * taken as the ratio in the range 0-1 of the proportion by which to
	 * increase the saturation.
	 * 
	 * @param amount
	 *        the amount to saturate by
	 * @return a saturated copy of the color
	 */
	public Color saturate(float amount) {
		float s = getHslSaturation();
		return Color.fromHsl(getHue(), s + (s * amount), getLightness());
	}

	/**
	 * Returns a copy of the color desaturated by the given amount. The amount
	 * is taken as the ratio in the range 0-1 of the proportion by which to
	 * decrease the saturation.
	 * 
	 * @param amount
	 *        the amount to desaturate by
	 * @return a desaturated copy of the color
	 */
	public Color desaturate(float amount) {
		float s = getHslSaturation();
		return Color.fromHsl(getHue(), s + (-s * amount), getLightness());
	}

	/**
	 * Returns a copy of the color with the hue shifted by the given number of
	 * degrees.
	 * 
	 * @param degrees
	 *        the number of degrees
	 * @return a hue shifted copy of the color
	 */
	public Color shiftHue(float degrees) {
		return Color.fromHsv(getHue() + degrees, getHsvSaturation(), getValue());
	}

	/**
	 * returns an RGB inverted copy of the color.
	 * 
	 * @return an inverted copy of the color.
	 */
	public Color invertRgb() {
		return Color.fromRgb(1 - getRed(), 1 - getGreen(), 1 - getBlue());
	}

	/**
	 * Returns a copy of the color 'rounded' to a nearby color. Each RGB
	 * component will be rounded down to the given precision.
	 * 
	 * @param precision
	 *        the precision that each component will be rounded to
	 * @return a reduced copy of the color
	 */
	public Color reduce(float precision) {
		if (precision <= 0) return this;
		return Color.fromRgb((getRed() + precision / 2) - getRed() % precision,
							 (getGreen() + precision / 2) - getGreen() % precision,
							 (getBlue() + precision / 2) - getBlue() % precision);
	}

	/**
	 * Returns {@code true} if the color is a gray scale color, {@code false}
	 * otherwise.
	 * 
	 * @return {@code true} if the color is a gray scale color, {@code false}
	 *         otherwise
	 */
	public boolean isGrayScale() {
		return (Float.compare(getRed(), getGreen()) == 0 && Float.compare(getGreen(), getBlue()) == 0);
	}

	/**
	 * Returns a copy of the color with the given channel component dropped.
	 * Returns the color unchanged if a null channel is provided.
	 * 
	 * @param channel
	 *        the channel to drop
	 * @return a copy of the color with the given channel component dropped
	 */
	public Color dropChannel(Channel channel) {
		if (channel == null) return this;
		// @formatter:off
		switch (channel) {
			case RED:     return Color.fromRgb(0, getGreen(), getBlue());
			case GREEN:   return Color.fromRgb(getRed(), 0, getBlue());
			case BLUE:    return Color.fromRgb(getRed(), getGreen(), 0);
			case CYAN:    return Color.fromCmyk(0        , getMagenta(), getYellow(), getKey());
			case MAGENTA: return Color.fromCmyk(getCyan(), 0           , getYellow(), getKey());
			case YELLOW:  return Color.fromCmyk(getCyan(), getMagenta(), 0          , getKey());
			case KEY:     return Color.fromCmyk(getCyan(), getMagenta(), getYellow(), 0       );
		}
		// @formatter:on
		throw new UnsupportedOperationException("Drop channel operation not supported for channel: " + channel);
	}

	/**
	 * Returns a copy of the color with only the given channel component.
	 * Returns black if a null channel is provided.
	 * 
	 * @param channel
	 *        the channel to drop
	 * @return a copy of the color with the complementary channels component
	 *         dropped
	 */
	public Color isolateChannel(Channel channel) {
		if (channel == null) return Color.fromRgb(0, 0, 0);
		// @formatter:off
		switch (channel) {
			case RED:     return Color.fromRgb(getRed(), 0, 		 0);
			case GREEN:   return Color.fromRgb(0, 		 getGreen(), 0);
			case BLUE:    return Color.fromRgb(0, 		 0, 		 getBlue());
			case CYAN:    return Color.fromCmyk(getCyan() , 0			 , 0		   , 0		 );
			case MAGENTA: return Color.fromCmyk(0         , getMagenta() , 0		   , 0		 );
			case YELLOW:  return Color.fromCmyk(0         , 0			 , getYellow() , 0		 );
			case KEY:     return Color.fromCmyk(0         , 0			 , 0		   , getKey());
		}
		// @formatter:on
		throw new UnsupportedOperationException("Isolate channel operation not supported for channel: " + channel);
	}

	/**
	 * Returns the distance between two colors in the RGB color space.
	 * 
	 * @param color
	 *        the color to get the distance to
	 * @return the distance between the given color
	 */
	public float rgbDistanceTo(Color color) {
		return this.asRgbVector().subtract(color.asRgbVector()).magnitude();
	}

	/**
	 * Returns the distance between two colors in the HSL color space.
	 * 
	 * @param color
	 *        the color to get the distance to
	 * @return the distance between the given color
	 */
	public float hslDistanceTo(Color color) {
		float c1x = (float) (this.getLightness() * Math.cos(this.getHue()) * 2);
		float c1y = (float) (this.getLightness() * Math.sin(this.getHue()) * 2);
		float c2x = (float) (color.getLightness() * Math.cos(color.getHue()) * 2);
		float c2y = (float) (color.getLightness() * Math.cos(color.getHue()) * 2);

		return (float) (Math.sqrt((this.getHslSaturation() - color.getHslSaturation()) *
								  (this.getHslSaturation() - color.getHslSaturation()) +
								  (c1x - c2x) * (c1x - c2x) +
								  (c1y - c2y) * (c1y - c2y)) / Math.sqrt(6));
	}

	/**
	 * Returns the color produced by applying the given RGB color space transformation.
	 * 
	 * @param transformation the tranformation matrix
	 * @return a new Color
	 */
	public Color transform(Matrix3f transformation) {
		return Color.fromRgbVector(transformation.multiply(this.asRgbVector()));
	}
	

	// Overridden Methods ====================================================
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.format("#%06X", getRgbHex());
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return srgbEncoding;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Color color = (Color) o;
		return srgbEncoding == color.srgbEncoding;
	}


	// Static Methods ========================================================
	/**
	 * Sets the color name file to load and unloads the existing file.
	 * 
	 * @param fileName
	 *        the file name of the new color name file
	 */
	public static void setColorNameFile(String fileName) {
		colorNameFile = fileName;
		colorNameSource = null;
	}
}

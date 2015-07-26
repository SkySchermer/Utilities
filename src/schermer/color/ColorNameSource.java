/**
 * File: ColorNameSource.java
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


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import schermer.CoverTree;

/**
 * The ColorNameSource class encapsulates a source of human readable names for
 * colors.
 * <p>
 * <b> Usage: </b>
 * <p>
 * A ColorNameSource depends on pre-loaded color name data. Constructing a
 * ColorNameSource can be time-consuming, and the resulting data can use a lot
 * of memory, so there is a clear trade-off between creating the object when
 * necessary and pre-loading it for ready access.
 * <p>
 * <b> Constructors: </b>
 * <p>
 * {@link ColorNameSource#fromFile(String)}: Reads color name data from a file
 * using the default metric.
 * <p>
 * {@link ColorNameSource#fromFile(String, ToDoubleBiFunction)}: Reads color
 * name data from a file and a metric function.
 * <p>
 * {@link ColorNameSource#empty()}: Constructs a new ColorNameSource with no
 * data.
 * <p>
 * {@link ColorNameSource#empty(ToDoubleBiFunction)}: Constructs a new
 * ColorNameSource with no data using the given metric function.
 *
 * @version 0.1.3 (26 July 2015)
 * @author Skylor R Schermer
 */
public class ColorNameSource {

	// Constants =============================================================
	/** The name of the recommended color name file. */
	public static final String DEFAULT_COLOR_NAME_FILE = "colornames.txt";

	private static final String COLOR_NAME_REGEX = "(#[0-9a-fA-F]{6}) (.*)";
	private static final String COLOR_NAME_FORMAT = "#%06X %s";

	private static final ToDoubleBiFunction<Color, Color> DEFAULT_COLOR_METRIC = Color.HSL_DISTANCE_FUNCTION;


	// Fields ================================================================
	private Map<String, NamedColor> colorNames;
	private CoverTree<NamedColor> colorCoverTree;
	private ToDoubleBiFunction<Color, Color> colorMetric;


	// Constructors ==========================================================
	/**
	 * Constructs a ColorNameSource from its total internal state.
	 * 
	 * @param colorNames
	 *        the color names map
	 * @param colorCoverTree
	 *        the CoverTree for nearest-neighbor lookups
	 * @param colorMetric
	 *        the color distance function for computing nearness
	 */
	private ColorNameSource(Map<String, NamedColor> colorNames,
							CoverTree<NamedColor> colorCoverTree,
							ToDoubleBiFunction<Color, Color> colorMetric) {
		this.colorNames = colorNames;
		this.colorCoverTree = colorCoverTree;
		this.colorMetric = colorMetric;
	}


	// Static Constructors ===================================================
	/**
	 * Constructs a ColorNameSource from a file of color names using the default
	 * metric.
	 * 
	 * @param fileName
	 *        the name of the color name file
	 */
	public static ColorNameSource fromFile(String colorNameFile) {
		return ColorNameSource.fromFile(colorNameFile, DEFAULT_COLOR_METRIC);
	}

	/**
	 * Constructs a ColorNameSource from a file of color names.
	 * 
	 * @param fileName
	 *        the name of the color name file
	 * @param colorMetric
	 *        the metric function for determining color nearness
	 */
	public static ColorNameSource fromFile(String colorNameFile,
										   ToDoubleBiFunction<Color, Color> colorMetric) {
		ColorNameSource cns = new ColorNameSource(new TreeMap<>(), null, colorMetric);
		try (FileReader fr = new FileReader(colorNameFile);
			 BufferedReader br = new BufferedReader(fr)) {

			Pattern pattern = Pattern.compile(COLOR_NAME_REGEX);
			Matcher matcher;
			String line;
			String name;

			/* Match on each line of the file. */
			while ((line = br.readLine()) != null) {
				matcher = pattern.matcher(line);
				if (matcher.matches()) {
					name = matcher.group(2).trim().toUpperCase();
					/* Read the entry from the line. */
					cns.colorNames.put(name,
									   new NamedColor(name, Color.parseRgbHex(matcher.group(1))));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Build CoverTree. */
		cns.colorCoverTree = CoverTree.of(cns.colorNames.values(), (cn1, cn2) -> {
			return cns.colorMetric.applyAsDouble(cn1.color, cn2.color);
		});

		return cns;
	}

	/**
	 * Constructs a ColorNameSource with no data using the default metric.
	 */
	public static ColorNameSource empty() {
		return ColorNameSource.empty(DEFAULT_COLOR_METRIC);
	}

	/**
	 * Constructs a ColorNameSource with no data.
	 * 
	 * @param colorMetric
	 *        the metric function for determining color nearness
	 */
	public static ColorNameSource empty(ToDoubleBiFunction<Color, Color> colorMetric) {
		ColorNameSource cns = new ColorNameSource(new TreeMap<>(),
												  null,
												  colorMetric);
		
		cns.colorCoverTree = CoverTree.of(Collections.emptyList(), (cn1, cn2) -> {
			return cns.colorMetric.applyAsDouble(cn1.color, cn2.color);
		});
		return cns;
	}

	// Methods ===============================================================
	/**
	 * Writes the current color name database into a file.
	 * 
	 * @param fileName
	 *        the name of the file to write to
	 */
	public void saveColorNames(String fileName) {
		try (FileWriter fw = new FileWriter(fileName);
			 BufferedWriter bw = new BufferedWriter(fw)) {

			for (String name : colorNames.keySet()) {
				bw.write(String.format(COLOR_NAME_FORMAT + "\n",
									   colorNames.get(name).color.getRgbHex(),
									   name));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addColorName(Color color, String name) {
		NamedColor nc = new NamedColor(name, color);
		this.colorNames.put(name, nc);
		this.colorCoverTree.insert(nc);
	}

	/**
	 * Returns the hex value of a color with the given name.
	 * 
	 * @param name
	 *        the name of the color
	 * @return The color or {@code null} if the named color is not present.
	 */
	public Color getColor(String colorName) {
		NamedColor cn = colorNames.getOrDefault(colorName.trim().toUpperCase(), null);
		if (cn != null)
			return cn.color;
		return null;
	}

	public Color getNearestColor(Color color) {
		NamedColor cn = colorCoverTree.findNearest(new NamedColor(null, color));
		if (cn != null)
			return cn.color;
		return null;
	}

	public String getNearestColorName(Color color) {
		NamedColor cn = colorCoverTree.findNearest(new NamedColor(null, color));
		if (cn != null)
			return cn.name;
		return null;
	}


	private static class NamedColor {
		public String name;
		public Color color;

		public NamedColor(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String toString() {
			return color.toString() + "-" + this.name;
		}
	}
	
	public static void main(String[] args) {
		ColorNameSource cns = ColorNameSource.fromFile(DEFAULT_COLOR_NAME_FILE, Color.RGB_DISTANCE_FUNCTION);
		
		Color c = Color.fromRgbHex(0x1134FF).transform(Color.SRGB_GAMMA_CORRECTION);
		System.out.println(c);
		System.out.println(cns.getNearestColorName(c));
		System.out.println(cns.getNearestColor(c));
		
	}
}

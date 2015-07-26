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
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import schermer.CoverTree;

/**
 * The ColorNameSource class encapsulates a source of human readable names for
 * colors.
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Usage: </b>
 * <p>
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Known Issues: </b>
 * <p>
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Constructors: </b>
 * <p>
 *
 * @version 0.1.1 (12 June 2015)
 * @author Skylor R Schermer
 */
public class ColorNameSource {

	// Constants =============================================================
	private static final String COLOR_NAME_REGEX = "(#[0-9a-fA-F]{6}) (.*)";
	private static final String COLOR_NAME_FORMAT = "#%06X %s";

	protected static final String DEFAULT_COLOR_NAME_FILE = "colornames.txt";


	// Fields ================================================================
	private Map<String, NamedColor> colorNames;
	private CoverTree<NamedColor> colorCoverTree;

	/**
	 * Constructs a ColorNameSource from its total internal state.
	 * 
	 * @param colorNames
	 */
	public ColorNameSource(Map<String, NamedColor> colorNames,
						   CoverTree<NamedColor> colorCoverTree) {
		this.colorNames = colorNames;
		this.colorCoverTree = colorCoverTree;
	}


	// Static Constructors ===================================================
	/**
	 * Creates a ColorNameSource from a file of color names.
	 * 
	 * @param fileName
	 *        the name of the color name file
	 */
	public static ColorNameSource fromFile(String colorNameFile) {
		ColorNameSource cns = new ColorNameSource(new TreeMap<>(), null);
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
			return cn1.color.rgbDistanceTo(cn2.color);
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

	/**
	 * Returns the hex value of a color with the given name.
	 * 
	 * @param name
	 *        the name of the color
	 * @return The color or {@code null} if the named color is not present.
	 */
	public Color getColor(String colorName) {
		NamedColor cn = colorNames.getOrDefault(colorName.trim().toUpperCase(), null);
		if (cn != null) return cn.color;
		return null;
	}
	
	public Color getNearestColor(Color color) {
		NamedColor cn = colorCoverTree.findNearest(new NamedColor(null, color));
		if (cn != null) return cn.color;
		return null;
	}
	
	public String getNearestColorName(Color color) {
		NamedColor cn = colorCoverTree.findNearest(new NamedColor(null, color));
		if (cn != null) return cn.name;
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
	
}

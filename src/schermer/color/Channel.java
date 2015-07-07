/**
 * File: Channel.java
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


/**
 * The Channel enum is used to describe color channels.
 * 
 * @version 0.1.1 (31 December 2014)
 * @author Skylor R Schermer
 */
public enum Channel {
	// Constants =============================================================
	/** The red channel in the RGB color space. */
	RED ("Red"),
	/** The green channel in the RGB color space. */
	GREEN ("Green"),
	/** The blue channel in the RGB color space. */
	BLUE ("Blue"),
	/** The cyan channel in the CMYK color space. */
	CYAN ("Cyan"),
	/** The magenta channel in the CMYK color space. */
	MAGENTA ("Magenta"),
	/** The yellow channel in the CMYK color space. */
	YELLOW ("Yellow"),
	/** The key channel in the CMYK color space. */
	KEY ("Key");


	// Fields ================================================================
	/** The name of the Channel. */
	public String name;


	// Constructors ==========================================================
	/** Enumeration constructor. */
	Channel(String name) {
		this.name = name;
	}
}

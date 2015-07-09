/**
 * File: TreePrinter.java  
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

import java.util.List;
import java.util.function.Function;

/**
 * Provides a simple, portable means of printing tree-like data structures.
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Usage: </b>
 * <p>
 * Call {@link #getPrintString(Object)} on a node to get the print string for
 * the tree using the given node as root. The output of this function can be
 * configured with the following functions:
 * <p>
 * {@link #setMaxDepth(int)}: Sets the maximum depth to traverse from the node.
 * <p>
 * ---------------------------------------------------------------------------
 * <p>
 * <b> Constructors: </b>
 * <p>
 * {@link TreePrinter#of(Function, Function)}: Constructs a TreePrinter from a
 * node accessor function and a child accessor function.
 *
 * @version 0.1.0 (2015)
 * @author Skylor R Schermer
 */
public class TreePrinter<N> {

	// Fields ================================================================
	private Function<N, String> getData;
	private Function<N, List<N>> getChildren;
	private int maxDepth;


	// Constructors ==========================================================
	/**
	 * Constructs a TreePrinter from its total internal state.
	 * 
	 * @param getData
	 *        the function for retrieving a string representation for a single
	 *        node
	 * @param getChildren
	 *        the function for retrieving a list of child nodes
	 * @param maxDepth
	 *        the maximum depth of tree traversal
	 */
	private TreePrinter(Function<N, String> getData,
						Function<N, List<N>> getChildren,
						int maxDepth) {

		this.getData = getData;
		this.getChildren = getChildren;
		this.maxDepth = maxDepth;
	}


	// Static Constructors ===================================================
	/**
	 * Constructs a TreePrinter from node accessor functions.
	 * 
	 * @param getData
	 *        the function for retrieving a string representation for a single
	 *        node
	 * @param getChildren
	 *        the function for retrieving a list of child nodes
	 */
	public static <N> TreePrinter<N> of(Function<N, String> getData,
										Function<N, List<N>> getChildren) {

		return new TreePrinter<N>(getData, getChildren, -1);
	}


	// Accessor Methods ======================================================
	/**
	 * Sets the maximum recursion depth.
	 * 
	 * @param maxDepth
	 *        the maximum recursion depth
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Returns the print string for the given node.
	 * 
	 * @param node
	 *        the root node
	 * @return the print string
	 */
	public String getPrintString(N node) {
		return getPrintStringRecursive(node, 0);
	}


	// Methods ===============================================================
	/**
	 * Returns the print string for the given node.
	 * 
	 * @param node
	 *        the root node
	 * @param depth
	 *        the depth of the current traversal
	 * @return the print string
	 */
	private String getPrintStringRecursive(N node, int depth) {
		StringBuilder sb = new StringBuilder();

		sb.append(indent(depth));

		sb.append(getData.apply(node));


		for (N child : getChildren.apply(node)) {
			sb.append('\n');
			if (maxDepth != -1 && depth + 1 >= maxDepth) {
				sb.append(indent(depth + 1));
				sb.append("...");
				break;
			} else {
				sb.append(getPrintStringRecursive(child, depth + 1));
			}
		}

		return sb.toString();
	}

	/**
	 * Returns an indentation buffer for the given tree depth.
	 * 
	 * @param depth
	 *        the depth of the tree
	 * @return the indentation string
	 */
	private String indent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			if (i == 0) {
				sb.append("    ");
			} else {
				sb.append("|   ");
			}
		}
		return sb.toString();
	}
}

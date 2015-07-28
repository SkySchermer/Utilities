/**
 * File: CoverTree.java    
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class CoverTree<T> {

	// Constants =============================================================
	private static final double SPAN_FACTOR = 1.3;


	// Fields ================================================================
	ToDoubleBiFunction<T, T> metricFunction;
	CoverTreeNode<T> root;
	double maxDistance;


	// Constructors ==========================================================
	private CoverTree(CoverTreeNode<T> root,
					  ToDoubleBiFunction<T, T> distanceFunction,
					  int minLevel,
					  int maxLevel) {
		this.root = root;
		this.metricFunction = distanceFunction;
	}

	
	// Static Constructors ===================================================
	public static <T> CoverTree<T> of(Collection<T> data,
									  ToDoubleBiFunction<T, T> metricFunction) {

		CoverTree<T> tree = new CoverTree<>(null, metricFunction, 0, 1);
		for (T item : data) {
			tree.insert(item);
		}

		return tree;
	}
	

	// Methods ===============================================================
	public T findNearest(T point) {
		return findNearest(root, point, root.data);
	}

	private T findNearest(CoverTreeNode<T> node, T point, T nearestYet) {
		/* Pseudocode from paper:
		 * function findNearestNeighbor(cover tree p, query point x, nearest neighbor so far y)
		 * 
		 * 	if d(p, x) < d(y, x) then
		 * 		y←p
		 * 	for each child q of p sorted by distance to x do
		 * 		if d(y, x) > d(y, q) − maxdist(q) then
		 * 			y ← findNearestNeighbor(q, x, y)
		 * 		return y 
		 */

		if (distance(node.data, point) < distance(point, nearestYet))
			nearestYet = node.data;


		Collections.sort(node.children, (a, b) -> {
			return Double.compare(distance(a.data, point),
								  distance(b.data, point));
		});

		for (CoverTreeNode<T> child : node.children) {
			if (distance(point, nearestYet) > distance(child.data, nearestYet) - maxDistance(child))
				nearestYet = findNearest(child, point, nearestYet);
		}

		return nearestYet;
	}

	public void insert(T point) {
		if (root == null) {
			root = new CoverTreeNode<T>(new ArrayList<>(), point, 0);
			maxDistance = 0;
			return;
		}
		double d = distance(root.data, point);
		if (d > maxDistance)
			maxDistance = d;
		root = insert(root, point);
	}

	private CoverTreeNode<T> insert(CoverTreeNode<T> node, T point) {
		/* Pseudocode from paper:
		 * function insert(cover tree p, data point x)
		 * 
		 * 	if d(p, x) > covdist(p) then
		 * 		while d(p, x) > 2*covdist(p) do
		 * 			Remove any leaf q from p
		 * 			p′ ← tree with root q and p as only child
		 * 			p ← p′
		 * 		return tree with x as root and p as only child
		 * 	return insert_(p, x) 
		 */
		if (distance(point, node.data) > node.coverDistance()) {
			while (distance(point, node.data) > 2 * node.coverDistance()) {
				node = node.raise();
			}
			return new CoverTreeNode<T>(node, point);
		}
		return insertRecursive(node, point);
	}

	private CoverTreeNode<T> insertRecursive(CoverTreeNode<T> node, T point) {
		/* Pseudocode from paper:
		 * function insert_(cover tree p, data point x)
		 * prerequisites: d(p,x) ≤ covdist(p)
		 * 
		 * 	for q ∈ children(p) do
		 * 		if d(q, x) ≤ covdist(q) then
		 * 			q′ ← insert_(q, x)
		 * 			p′ ← p with child q replaced with q′
		 * 			return p′
		 * 	return p with x added as a child 
		 */
		if (distance(node.data, point) > node.coverDistance()) { throw new IllegalStateException("Insertion violates invariant."); }
		for (CoverTreeNode<T> child : node.children) {
			if (distance(child.data, point) <= child.coverDistance()) {
				CoverTreeNode<T> newChild = insertRecursive(child, point);
				node.children.remove(child);
				node.addChild(newChild);
				return node;
			}
		}

		node.addChild(new CoverTreeNode<T>(new ArrayList<>(), point, node.level - 1));
		return node;
	}

	private void insertNearestAncestor(T point) {
		
	}
	private void insertNearestAncestor() {
		/* Pseudocode from paper:
		 * Nearest ancestor cover tree insertion 
		 * 
		 * function insert (cover tree p, data point x)
		 * 
		 *   for q ∈ children(p) sorted by distance to x do 
		 *   	if d(q,x) ≤ covdist(q) then
		 *   		q′ ← insert (q, x)
		 *   		p′ ← p with child q replaced with q′ 
		 *   		return p′
		 *   return rebalance( p, x)
		 */
	}
	
	private void rebalance() {
		/* Pseudocode from paper:
		 * 
		 * function rebalance(cover trees p, data point x)
		 * prerequisites: x can be added as a child of p without violating the covering or separating invariants
		 * 
		 * 	create tree x′ with root node x at level level(p) − 1 x′ contains no other points
		 * 	p′ ← p
		 * 	for q ∈ children(p) do
		 * 		(q′, moveset, stayset) ← rebalance_(p,q,x) 
		 * 		p′ ← p′ with child q replaced with q′
		 * 		for r ∈ moveset do
		 * 			x′ ← insert(x′,r) 
		 * 	return p′ with x′ added as a child
		 */
	}
	
	private void rebalanceRecursive() {
		/* Pseudocode from paper:
		 * 
		 * function rebalance (cover trees p and q, point x) 
		 * prerequisites: p is an ancestor of q
		 * 
		 * 	if d(p,q) > d(q,x) then 
		 * 		moveset, stayset ← 0
		 * 		for r ∈ descendants(q) do
		 * 			if d(r, p) > d(r,x) then 
		 * 				moveset ← moveset ∪ {r}
		 * 			else
		 * 				stayset ← stayset ∪ {r}
		 * 		return (null, moveset, stayset) 
		 * 	else
		 * 		moveset′,stayset′ ← 0
		 * 		q′ ← q
		 * 		for r ∈ children(q) do
		 * 			(r′, moveset, stayset) ← rebalance_(p,r,x) 
		 * 			moveset′ ← moveset ∪ moveset′
		 * 			stayset′ ← stayset ∪ stayset′
		 * 			if r′ = null then
		 * 				q′ ← q with the subtree r removed 
		 * 			else
		 * 				q′ ← q with the subtree r replaced by r′
		 * 		for r ∈ stayset′ do
		 * 			if d(r,q)′ ≤ covdist(q)′ then
		 * 				q′ ← insert(q′,r) stayset′ ← stayset′ − {r}
		 * 		return (q′,moveset′,stayset′)
		 * 
		 */
	}
	
	private void merge() {
		/* Pseudocode from paper:
		 * 
		 * function merge(cover tree p, cover tree q)
		 * 	if level(q) > level(p) then 
		 * 		swap p and q
		 * 	while level(q) < level(p) do
		 * 		move a node from the leaf of q to the root; this raises the level of q by 1
		 * 		(p,leftovers) ← mergeHelper(p,q) 
		 * 	for r ∈ leftovers do
		 * 		p ← insert(p,r) 
		 * 	return p
		 * 
		 */
	}

	private void mergeHelper() {
		/* Pseudocode from paper:
		 * 
		 * function mergeHelper(cover tree p, cover tree q) 
		 * prereqs: level(p) = level(q), d(p,q) ≤ covdist(p)
		 * 
		 * 	children′ ← children(p) 			//◃ Phase 1
		 * 	uncovered, sepcov, leftovers ← 0 
		 * 	for r ∈ children(q) do
		 * 		if d(p,r) < covdist(p) then 
		 * 			foundmatch ← false
		 * 			for s ∈ children′ do
		 * 				if d(s, r) ≤ sepdist(p) then 
		 * 					(s′, leftovers_s) ← mergeHelper(s,r) 
		 * 					children′ ← children′ ∪ {s′ } − {s} 
		 * 					leftovers ← leftovers ∪ leftoverss 
		 * 					foundmatch ← true
		 * 					break from inner loop
		 * 			if not foundmatch then 
		 * 				sepcov ← sepcov ∪ {r}
		 * 		else
		 * 			uncovered ← uncovered ∪ {r} 
		 * 	children′ ← children′ ∪ sepcov	//◃ Phase 2
		 * 	p′ ← tree rooted at p with children(p’) = children′
		 * 	p′ ← insert(p′,q) 
		 * 	leftovers′ ← 0 
		 * 	for r ∈ leftovers do
		 * 		if d(r, p)′ ≤ covdist(p)′ then 
		 * 			p′ ← insert(p′,r)
		 * 		else
		 * 			leftovers′ ← leftovers′ ∪ {r} 
		 * 	return (p′, leftovers′ ∪ uncovered)
		 */
	}
	
	private double distance(T a, T b) {
		return metricFunction.applyAsDouble(a, b);
	}


	private double maxDistance(CoverTreeNode<T> node) {
		return maxDistance;
	}

	
	// Internal Classes ======================================================
	private static class CoverTreeNode<P> {

		public List<CoverTreeNode<P>> children;
		public P data;
		public int level;

		public CoverTreeNode(List<CoverTreeNode<P>> children, P data, int level) {
			this.children = children;
			this.data = data;
			this.level = level;
		}

		public CoverTreeNode(CoverTreeNode<P> child, P data) {
			this.children = new ArrayList<>();
			this.children.add(child);
			this.data = data;
			this.level = child.level + 1;
		}

		public void addChild(CoverTreeNode<P> child) {
			this.children.add(child);
		}

		public CoverTreeNode<P> raise() {
			/* Remove any leaf q from p
			 * p′ ← tree with root q and p as only child
			 * p ← p′
			 */
			if (children.size() == 0) {
				this.level += 1;
				return this;
			}
			CoverTreeNode<P> newRoot = children.remove(0);
			newRoot.addChild(this);
			newRoot.level += 1;
			return newRoot;
		}

		public double coverDistance() {
			return Math.pow(SPAN_FACTOR, level);
		}

		public String toString() {
			return this.data.toString();
		}
	}

	
	public String debugString() {
		// @formatter:off
		TreePrinter<CoverTreeNode<T>> tp = TreePrinter.of((n) -> { return n.toString(); },
		                                                  (n) -> { return n.children; });
		// @formatter:on
		return tp.getPrintString(this.root);
	}
}

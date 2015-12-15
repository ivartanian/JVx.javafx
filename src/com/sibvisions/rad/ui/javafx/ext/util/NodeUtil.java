/*
 * Copyright 2015 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sibvisions.rad.ui.javafx.ext.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Region;

/**
 * {@link NodeUtil} provides various helper functions for working with
 * {@link Node}s.
 * 
 * @author Robert Zenz
 */
public final class NodeUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private NodeUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Finds the first focusable {@link Node} in the given {@link Parent}.
	 * <p>
	 * A {@link Node} is considered focusable if the
	 * {@link Node#focusTraversableProperty()} is set to {@code true}.
	 * 
	 * @param pParent the {@link Parent}.
	 * @return the first focusable {@link Node}.
	 */
	public static Node findFirstFocusableNode(Parent pParent)
	{
		for (Node node : pParent.getChildrenUnmodifiable())
		{
			if (node.isFocusTraversable())
			{
				return node;
			}
			
			if (node instanceof Parent)
			{
				Node foundNode = findFirstFocusableNode((Parent)node);
				
				if (foundNode != null)
				{
					return foundNode;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the maximum height of the given {@link Node}.
	 * <p>
	 * If the given {@link Node} is an instance of {@link Region} and the
	 * {@link Region#maxHeightProperty()} is set and greater or equal to zero,
	 * the value of that will be returned. If otherwise, the value of
	 * {@link Node#maxHeight(double)} will be returned.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the maximum height of the {@link Node}.
	 */
	public static double getMaxHeight(Node pNode)
	{
		if (pNode instanceof Region)
		{
			double maxHeight = ((Region)pNode).getMaxHeight();
			
			if (maxHeight >= 0)
			{
				return maxHeight;
			}
		}
		
		return pNode.maxHeight(-1);
	}
	
	/**
	 * Gets the maximum minimum height of the given {@link Node}s.
	 * <p>
	 * Iterates over all given {@link Node}s and returns the maximum value for
	 * the minimum height all {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @return the maximum minimum height of all {@link Node}s.
	 */
	public static double getMaxMinHeight(Iterable<Node> pNodes)
	{
		return getMax(pNodes, (pNode) ->
		{
			return Double.valueOf(getMinHeight(pNode));
		});
	}
	
	/**
	 * Gets the maximum minimum width of the given {@link Node}s.
	 * <p>
	 * Iterates over all given {@link Node}s and returns the maximum value for
	 * the minimum width all {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @return the maximum minimum width of all {@link Node}s.
	 */
	public static double getMaxMinWidth(Iterable<Node> pNodes)
	{
		return getMax(pNodes, (pNode) ->
		{
			return Double.valueOf(getMinWidth(pNode));
		});
	}
	
	/**
	 * Gets the maximum preferred height of the given {@link Node}s.
	 * <p>
	 * Iterates over all given {@link Node}s and returns the maximum value for
	 * the preferred height all {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @return the maximum preferred height of all {@link Node}s.
	 */
	public static double getMaxPrefHeight(Iterable<Node> pNodes)
	{
		return getMax(pNodes, (pNode) ->
		{
			return Double.valueOf(getPrefHeight(pNode));
		});
	}
	
	/**
	 * Gets the maximum preferred width of the given {@link Node}s.
	 * <p>
	 * Iterates over all given {@link Node}s and returns the maximum value for
	 * the preferred width all {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @return the maximum preferred width of all {@link Node}s.
	 */
	public static double getMaxPrefWidth(Iterable<Node> pNodes)
	{
		return getMax(pNodes, (pNode) ->
		{
			return Double.valueOf(getPrefWidth(pNode));
		});
	}
	
	/**
	 * Gets the maximum width of the given {@link Node}.
	 * <p>
	 * If the given {@link Node} is an instance of {@link Region} and the
	 * {@link Region#maxWidthProperty()} is set and greater or equal to zero,
	 * the value of that will be returned. If otherwise, the value of
	 * {@link Node#maxWidth(double)} will be returned.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the maximum height of the {@link Node}.
	 */
	public static double getMaxWidth(Node pNode)
	{
		if (pNode instanceof Region)
		{
			double maxWidth = ((Region)pNode).getMaxWidth();
			
			if (maxWidth >= 0)
			{
				return maxWidth;
			}
		}
		
		return pNode.maxWidth(-1);
	}
	
	/**
	 * Gets the minimum height of the given {@link Node}.
	 * <p>
	 * If the given {@link Node} is an instance of {@link Region} and the
	 * {@link Region#minHeightProperty()} is set and greater or equal to zero,
	 * the value of that will be returned. If otherwise, the value of
	 * {@link Node#minHeight(double)} will be returned.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the minimum width of the {@link Node}.
	 */
	public static double getMinHeight(Node pNode)
	{
		if (pNode instanceof Region)
		{
			double minHeight = ((Region)pNode).getMinHeight();
			
			if (minHeight >= 0)
			{
				return minHeight;
			}
		}
		
		return pNode.minHeight(-1);
	}
	
	/**
	 * Gets the minimum size of the given {@link Node} as {@link Dimension2D}.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the minimum size of the given {@link Node} as {@link Dimension2D}
	 *         .
	 */
	public static Dimension2D getMinSize(Node pNode)
	{
		return new Dimension2D(getMinWidth(pNode), getMinHeight(pNode));
	}
	
	/**
	 * Gets the minimum width of the given {@link Node}.
	 * <p>
	 * If the given {@link Node} is an instance of {@link Region} and the
	 * {@link Region#minWidthProperty()} is set and greater or equal to zero,
	 * the value of that will be returned. If otherwise, the value of
	 * {@link Node#minWidth(double)} will be returned.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the minimum width of the {@link Node}.
	 */
	public static double getMinWidth(Node pNode)
	{
		if (pNode instanceof Region)
		{
			double minWidth = ((Region)pNode).getMinWidth();
			
			if (minWidth >= 0)
			{
				return minWidth;
			}
		}
		
		return pNode.minWidth(-1);
	}
	
	/**
	 * Gets the preferred height of the given {@link Node}.
	 * <p>
	 * If the given {@link Node} is an instance of {@link Region} and the
	 * {@link Region#prefHeightProperty()} is set and greater or equal to zero,
	 * the value of that will be returned. If otherwise, the value of
	 * {@link Node#prefHeight(double)} will be returned.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the preferred height of the {@link Node}.
	 */
	public static double getPrefHeight(Node pNode)
	{
		double prefHeight = -1;
		
		if (pNode instanceof Region)
		{
			prefHeight = ((Region)pNode).getPrefHeight();
		}
		
		if (prefHeight < 0)
		{
			prefHeight = pNode.prefHeight(-1);
		}
		
		double minHeight = getMinHeight(pNode);
		
		if (prefHeight > minHeight)
		{
			return prefHeight;
		}
		else
		{
			return minHeight;
		}
	}
	
	/**
	 * Gets the preferred size of the given {@link Node} as {@link Dimension2D}.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the preferred size of the given {@link Node} as
	 *         {@link Dimension2D} .
	 */
	public static Dimension2D getPrefSize(Node pNode)
	{
		return new Dimension2D(getPrefWidth(pNode), getPrefHeight(pNode));
	}
	
	/**
	 * Gets the preferred width of the given {@link Node}.
	 * <p>
	 * If the given {@link Node} is an instance of {@link Region} and the
	 * {@link Region#prefWidthProperty()} is set and greater or equal to zero,
	 * the value of that will be returned. If otherwise, the value of
	 * {@link Node#prefWidth(double)} will be returned.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the preferred width of the {@link Node}.
	 */
	public static double getPrefWidth(Node pNode)
	{
		double prefWidth = -1;
		
		if (pNode instanceof Region)
		{
			prefWidth = ((Region)pNode).getPrefWidth();
		}
		
		if (prefWidth < 0)
		{
			prefWidth = pNode.prefWidth(-1);
		}
		
		double minWidth = getMinWidth(pNode);
		
		if (prefWidth > minWidth)
		{
			return prefWidth;
		}
		else
		{
			return minWidth;
		}
	}
	
	/**
	 * Gets the sum of all minimum heights of the given {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @param pRound if the value should be rounded.
	 * @return the sum of all minimum heights.
	 */
	public static double getSumMinHeight(Iterable<Node> pNodes, boolean pRound)
	{
		return getSum(pNodes, pRound, NodeUtil::getMinHeight);
	}
	
	/**
	 * Gets the sum of all minimum widths of the given {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @param pRound if the value should be rounded.
	 * @return the sum of all minimum widths.
	 */
	public static double getSumMinWidth(Iterable<Node> pNodes, boolean pRound)
	{
		return getSum(pNodes, pRound, NodeUtil::getMinWidth);
	}
	
	/**
	 * Gets the sum of all preferred heights of the given {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @param pRound if the value should be rounded.
	 * @return the sum of all preferred heights.
	 */
	public static double getSumPrefHeight(Iterable<Node> pNodes, boolean pRound)
	{
		return getSum(pNodes, pRound, NodeUtil::getPrefHeight);
	}
	
	/**
	 * Gets the sum of all preferred widths of the given {@link Node}s.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @param pRound if the value should be rounded.
	 * @return the sum of all preferred widths.
	 */
	public static double getSumPrefWidth(Iterable<Node> pNodes, boolean pRound)
	{
		return getSum(pNodes, pRound, NodeUtil::getPrefWidth);
	}
	
	/**
	 * Gets the surrounding space for the given {@link Node}.
	 * <p>
	 * The surrounding space is the space between the given {@link Node} and the
	 * borders of the given {@link Parent}. Additionally scrollbars are already
	 * taken into account.
	 * 
	 * @param pParent the {@link Parent} (or any higher up the hierarchy).
	 * @param pNode the {@link Node}.
	 * @return the surrounding space.
	 */
	public static Insets getSurroundingSpace(Parent pParent, Node pNode)
	{
		Bounds parentBounds = pParent.localToScene(pParent.getLayoutBounds());
		Bounds nodeBounds = pNode.localToScene(pNode.getLayoutBounds());
		
		double magicScrollBarValue = 2;
		
		for (Node node : lookupAll(pParent, ScrollBar.class, ".scroll-bar", null))
		{
			if (node.isVisible())
			{
				ScrollBar scrollBar = (ScrollBar)node;
				
				if (scrollBar.getOrientation() == Orientation.VERTICAL)
				{
					double width = scrollBar.getWidth();
					
					if (width > 0)
					{
						parentBounds = new BoundingBox(
								parentBounds.getMinX(),
								parentBounds.getMinY(),
								parentBounds.getMinZ(),
								parentBounds.getWidth() - width - magicScrollBarValue,
								parentBounds.getHeight(),
								parentBounds.getDepth());
					}
				}
				else
				{
					double height = scrollBar.getHeight();
					
					if (height > 0)
					{
						parentBounds = new BoundingBox(
								parentBounds.getMinX(),
								parentBounds.getMinY(),
								parentBounds.getMinZ(),
								parentBounds.getWidth(),
								parentBounds.getHeight() - height - magicScrollBarValue,
								parentBounds.getDepth());
					}
				}
			}
		}
		
		return new Insets(
				nodeBounds.getMinY() - parentBounds.getMinY(),
				parentBounds.getMaxX() - nodeBounds.getMaxX(),
				parentBounds.getMaxY() - nodeBounds.getMaxY(),
				nodeBounds.getMinX() - parentBounds.getMinX());
	}
	
	/**
	 * Checks if the given {@link Node} is a child (grand-child,
	 * grand-grand-child, etc.) of the given parent.
	 * 
	 * @param pParent the {@link Node parent}. Can be {@code null}.
	 * @param pNodeToTest the {@link Node} to test. Can be {@code null}.
	 * @return {@code true} if the given {@link Node} is a child of the given
	 *         parent or the same {@link Node}. {@code false} if the given
	 *         parent or the given {@link Node} to test are {@code null}.
	 */
	public static boolean isSameOrChild(Node pParent, Node pNodeToTest)
	{
		if (pParent == null || pNodeToTest == null)
		{
			return false;
		}
		
		if (pParent == pNodeToTest)
		{
			return true;
		}
		
		if (pNodeToTest != null && pNodeToTest.getParent() != null)
		{
			return isSameOrChild(pParent, pNodeToTest.getParent());
		}
		
		return false;
	}
	
	/**
	 * Looks up the first {@link Node} in the given {@link Node} that is of the
	 * given class (or extend it), match the given selector and the given
	 * {@link Predicate}.
	 * 
	 * @param <T> the type of the class.
	 * @param pContainer the {@link Node} that contains the node.
	 * @param pClass the class.
	 * @param pSelector the selector.
	 * @param pPredicate The {@link Predicate}, can be {@code null} for all.
	 * @return the first {@link Node} that matches the given criteria.
	 */
	public static <T> T lookup(Node pContainer, Class<T> pClass, String pSelector, Predicate<T> pPredicate)
	{
		List<T> nodes = lookupAll(pContainer, pClass, pSelector, pPredicate);
		
		if (!nodes.isEmpty())
		{
			return nodes.get(0);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Looks up the first {@link Node} in the given {@link Region} that has the
	 * {@link Region} as {@link Node#getParent() parent} and matches the given
	 * selector.
	 * 
	 * @param pContainer the {@link Region}.
	 * @param pSelector the selector.
	 * @return the first {@link Node} that matches.
	 * @see Node#lookup(String)
	 */
	public static Node lookup(Region pContainer, String pSelector)
	{
		for (Node node : pContainer.lookupAll(pSelector))
		{
			if (node.getParent() == pContainer)
			{
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * Looks up all {@link Node}s in the given {@link Node} that are of the
	 * given class (or extend it), match the given selector and the given
	 * {@link Predicate}.
	 * 
	 * @param <T> the type of the class.
	 * @param pContainer the {@link Node} that contains the nodes.
	 * @param pClass the class.
	 * @param pSelector the selector.
	 * @param pPredicate The {@link Predicate}, can be {@code null} for all.
	 * @return all {@link Node}s that match the given criteria.
	 * @see Node#lookupAll(String)
	 */
	public static <T> List<T> lookupAll(Node pContainer, Class<T> pClass, String pSelector, Predicate<T> pPredicate)
	{
		List<T> nodes = new ArrayList<>();
		
		for (Node node : pContainer.lookupAll(pSelector))
		{
			if (pClass.isAssignableFrom(node.getClass()))
			{
				T item = pClass.cast(node);
				
				if (pPredicate == null || pPredicate.test(item))
				{
					nodes.add(item);
				}
			}
		}
		
		return nodes;
	}
	
	/**
	 * Corrects the given scene coordinates by applying the scaling of all
	 * parent {@link Node}s.
	 * 
	 * @param pFromNode the starting {@link Node}.
	 * @param pSceneX the scene x coordinate.
	 * @param pSceneY the scene y coordinate.
	 * @return the corrected coordinates.
	 */
	public static Point2D correctScaling(Node pFromNode, double pSceneX, double pSceneY)
	{
		double x = pSceneX;
		double y = pSceneY;
		
		Node currentNode = pFromNode;
		
		while (currentNode != null && currentNode.getParent() != null)
		{
			x = x * (1 / currentNode.getScaleX());
			y = y * (1 / currentNode.getScaleY());
			
			currentNode = currentNode.getParent();
		}
		
		return new Point2D(x, y);
	}
	
	/**
	 * Triggers the {@link Button#defaultButtonProperty()} of the first
	 * {@link Button} that is found which has it set to {@code true}.
	 * <p>
	 * At the moment of writing this (8u40) there is no way to have multiple
	 * default buttons in JavaFX without them
	 * <a href="https://javafx-jira.kenai.com/browse/RT-40306">either failing
	 * (RT-40306)</a> or not working. This is especially true with the custom
	 * MDI system that has been put in place.
	 * <p>
	 * This function will start at the given {@link Node} and walk the hierarchy
	 * downwards until it finds a {@link Button} which has its
	 * {@link Button#defaultButtonProperty()} set to {@code true}. If one is
	 * found, it will trigger (meaning the property will be set to {@code false}
	 * and {@code true} again), to make it the default button again.
	 * {@code true} will be returned in that case. If there are multiple default
	 * {@link Button}s in the hierarchy, only the first will be triggered.
	 * 
	 * @param pStartingNode the {@link Node} at which to start the search.
	 * @return {@code true} if there was a {@link Button} found and the
	 *         {@link Button#defaultButtonProperty()} has been triggered.
	 */
	public static boolean triggerDefaultButton(Node pStartingNode)
	{
		if (pStartingNode instanceof Button)
		{
			Button button = (Button)pStartingNode;
			if (button.isDefaultButton())
			{
				button.setDefaultButton(false);
				button.setDefaultButton(true);
				
				return true;
			}
		}
		else if (pStartingNode instanceof Parent)
		{
			for (Node child : ((Parent)pStartingNode).getChildrenUnmodifiable())
			{
				if (triggerDefaultButton(child))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the maximum value from the given {@link Nodes} by using the given
	 * function.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @param pGetValueFunction the {@link Function} used to get the value.
	 * @return the maximum value.
	 */
	private static double getMax(Iterable<Node> pNodes, Function<Node, Double> pGetValueFunction)
	{
		double max = 0;
		
		for (Node node : pNodes)
		{
			double value = pGetValueFunction.apply(node).doubleValue();
			
			if (value > max)
			{
				max = value;
			}
		}
		
		return max;
	}
	
	/**
	 * Gets the sum of the values from the given {@link Nodes} by using the
	 * given function.
	 * 
	 * @param pNodes the {@link Node}s.
	 * @param pRound if the values should be rounded.
	 * @param pGetValueFunction the {@link Function} used to get the value.
	 * @return the sum of all values.
	 */
	private static double getSum(Iterable<Node> pNodes, boolean pRound, Function<Node, Double> pGetValueFunction)
	{
		double sum = 0;
		
		for (Node node : pNodes)
		{
			double value = pGetValueFunction.apply(node).doubleValue();
			
			if (pRound)
			{
				value = Math.round(value);
			}
			
			sum = sum + value;
		}
		
		return sum;
	}
	
}	// NodeUtil

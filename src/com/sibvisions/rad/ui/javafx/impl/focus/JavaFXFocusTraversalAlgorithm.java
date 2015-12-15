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
package com.sibvisions.rad.ui.javafx.impl.focus;

import java.util.Comparator;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalContext;

/**
 * An {@link Algorithm} implementation that allows to have custom tab index set
 * on {@link Node}s.
 * 
 * @author Robert Zenz
 */
public class JavaFXFocusTraversalAlgorithm implements Algorithm
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Comparator} that is used.. */
	private Comparator<Node> comparator;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXFocusTraversalAlgorithm}.
	 */
	public JavaFXFocusTraversalAlgorithm()
	{
		super();
		
		comparator = new JavaFXNodeByTabIndexComparator();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node select(Node pOwner, Direction pDir, TraversalContext pContext)
	{
		// The following is a three step approach:
		//
		// 1. Find the next node in the indexed nodes list.
		//    a. If the current node is neither the start nor the end of
		//       of the indexed nodes list, we can return the next one.
		//       If it is the start or end, well have to walk through
		//       all nodes.
		// 2. If that failed, find a node in the non-indexed nodes list.
		//    a. Loop through all nodes and find the "next" that is not also
		//       in the indexed list, then return that.
		//    b. If the loop wraps around or hits the "beginning" of the indexed
		//       nodes, the loop will exit.
		// 3. If that failed, get the first or last of the indexed nodes,
		//    if there are any.
		
		Parent root = JavaFXFocusUtil.findRoot(pOwner.getParent());
		
		List<Node> focusableIndexedNodes = JavaFXFocusUtil.gatherFocusableNodes(root, true);
		
		if (!focusableIndexedNodes.isEmpty())
		{
			focusableIndexedNodes.sort(comparator);
			
			// If the current node is "within" the indexed nodes,
			// means it is neither at the start nor at the end, return the next.
			Node nextNode = getNextNode(focusableIndexedNodes, pOwner, pDir);
			if (nextNode != null)
			{
				return nextNode;
			}
		}
		
		List<Node> focusableNodes = JavaFXFocusUtil.gatherFocusableNodes(root, false);
		
		if (!focusableNodes.isEmpty())
		{
			Node nextNode = null;
			int nextIndex = focusableNodes.indexOf(pOwner);
			int stopIndex = nextIndex;
			int indexedStopIndex = -1;
			
			if (!focusableIndexedNodes.isEmpty())
			{
				Node indexedStopNode = null;
				
				if (pDir.isForward())
				{
					indexedStopNode = focusableIndexedNodes.get(focusableIndexedNodes.size() - 1);
				}
				else
				{
					indexedStopNode = focusableIndexedNodes.get(0);
				}
				
				indexedStopIndex = focusableNodes.indexOf(indexedStopNode);
			}
			
			// We are looping until we are finding a node that is not in
			// indexed nodes list (obviously if the indexed nodes list is empty,
			// that will be the first node we find). There is an additional
			// check within the loop if we've either looped around once,
			// or encountered the "start" node from which we should return
			// the first or last indexed node.
			do
			{
				nextIndex = nextIndexSafe(nextIndex, pDir, focusableNodes.size());
				nextNode = focusableNodes.get(nextIndex);
				
				if (nextIndex == stopIndex || nextIndex == indexedStopIndex)
				{
					nextNode = null;
					break;
				}
			}
			while (focusableIndexedNodes.contains(nextNode));
			
			if (nextNode != null)
			{
				return nextNode;
			}
		}
		
		// We haven't found a matching node, so either there are no non-indexed
		// nodes, or we've wrapped around to start again with the indexed nodes.
		if (!focusableIndexedNodes.isEmpty())
		{
			if (pDir.isForward())
			{
				return focusableIndexedNodes.get(0);
			}
			else
			{
				return focusableIndexedNodes.get(focusableIndexedNodes.size() - 1);
			}
		}
		
		// Return the previous owner if we did not find a suitable node.
		// Makes a NullPointerException go away that is non-explainable to me.
		//
		// java.lang.NullPointerException
		// at javafx.scene.Scene$ScenePulseListener.synchronizeSceneProperties(Scene.java:????)
		// at javafx.scene.Scene$ScenePulseListener.pulse(Scene.java:????)
		//
		// RT-40250
		return pOwner;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node selectFirst(TraversalContext pContext)
	{
		List<Node> focusableNodes = JavaFXFocusUtil.gatherFocusableNodes(pContext.getRoot(), true);
		
		if (focusableNodes.isEmpty())
		{
			focusableNodes = JavaFXFocusUtil.gatherFocusableNodes(pContext.getRoot(), false);
		}
		
		if (!focusableNodes.isEmpty())
		{
			return focusableNodes.get(0);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node selectLast(TraversalContext pContext)
	{
		List<Node> focusableNodes = JavaFXFocusUtil.gatherFocusableNodes(pContext.getRoot(), false);
		
		if (!focusableNodes.isEmpty())
		{
			return focusableNodes.get(focusableNodes.size() - 1);
		}
		else
		{
			return null;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the next {@link Node} in the given list starting by the index of
	 * the given {@link Node} into the given {@link Direction}.
	 * 
	 * @param pNodes the {@link List} of {@link Node}s.
	 * @param pCurrentNode the current {@link Node} at which to start.
	 * @param pDir the {@link Direction}.
	 * @return the next {@link Node} based on the given {@link Direction}.
	 *         {@code null} if the start or end of the {@link List} has been
	 *         reached.
	 */
	private Node getNextNode(List<Node> pNodes, Node pCurrentNode, Direction pDir)
	{
		int currentIndex = pNodes.indexOf(pCurrentNode);
		
		if (currentIndex >= 0)
		{
			int nextIndex = nextIndex(currentIndex, pDir);
			
			if (nextIndex >= 0 && nextIndex < pNodes.size())
			{
				return pNodes.get(nextIndex);
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the next index based on the given {@link Direction}.
	 * 
	 * @param pCurrentIndex the current index.
	 * @param pDir the {@link Direction}.
	 * @return the next index.
	 */
	private int nextIndex(int pCurrentIndex, Direction pDir)
	{
		int nextIndex = pCurrentIndex;
		
		if (pDir.isForward())
		{
			nextIndex++;
		}
		else
		{
			nextIndex--;
		}
		
		return nextIndex;
	}
	
	/**
	 * Returns the next index based on the given {@link Direction}, wrapped
	 * around if the given maximum value or zero has been reached.
	 * 
	 * @param pCurrentIndex the current index.
	 * @param pDir the {@link Direction}.
	 * @param pMax the maximum value after which the index wraps around.
	 * @return the next index.
	 */
	private int nextIndexSafe(int pCurrentIndex, Direction pDir, int pMax)
	{
		int nextIndex = nextIndex(pCurrentIndex, pDir);
		
		if (nextIndex >= pMax)
		{
			nextIndex = 0;
		}
		else if (nextIndex < 0)
		{
			nextIndex = pMax - 1;
		}
		
		return nextIndex;
	}
	
}	// JavaFXFocusTraversalAlgorithm

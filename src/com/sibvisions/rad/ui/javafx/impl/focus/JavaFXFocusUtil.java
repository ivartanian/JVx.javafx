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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXDesktopPane;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.impl.JavaFXResource;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXFocusUtil} is a helper utility for implementing a custom
 * focus traversal policy.
 * 
 * @author Robert Zenz
 */
public final class JavaFXFocusUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not needed.
	 */
	private JavaFXFocusUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Finds the root {@link Parent} of the given {@link Parent}. The root in
	 * this case is either the topmost {@link Parent} or a
	 * {@link FXInternalWindow}.
	 *
	 * @param pCurrentNode the current node.
	 * @return the root {@link Parent}.
	 */
	public static Parent findRoot(Parent pCurrentNode)
	{
		Parent parent = pCurrentNode.getParent();
		
		if (parent == null)
		{
			return pCurrentNode;
		}
		
		if (parent instanceof FXInternalWindow)
		{
			return ((FXInternalWindow)parent).getContent();
		}
		
		return findRoot(pCurrentNode.getParent());
	}
	
	/**
	 * Gathers all focusable {@link Node}s from the given {@link Parent} and all
	 * its children.
	 * 
	 * @param pParent the {@link Parent} at which to start.
	 * @param pIndexedOnly {@code true} only {@link Node}s which do have a tab
	 *            index set will be returned. {@code false} will return all
	 *            focusable {@link Node}s.
	 * @return all focusable {@link Node}s.
	 */
	public static List<Node> gatherFocusableNodes(Parent pParent, boolean pIndexedOnly)
	{
		if (pParent instanceof FXDesktopPane)
		{
			FXInternalWindow activeWindow = ((FXDesktopPane)pParent).getActiveWindow();
			
			if (activeWindow != null)
			{
				return gatherFocusableNodes(activeWindow, pIndexedOnly);
			}
		}
		
		List<Node> nodes = pParent.getChildrenUnmodifiable();
		
		if (nodes.isEmpty())
		{
			return Collections.emptyList();
		}
		
		if (isReverseOrderNeeded(pParent))
		{
			nodes = new ArrayList<>(nodes);
			Collections.reverse(nodes);
		}
		
		List<Node> focusableNodes = new ArrayList<>();
		
		for (Node node : nodes)
		{
			if (isTraversable(node))
			{
				if (node instanceof ComboBoxBase)
				{
					// The ComboBoxBase does contain a text field (and some other stuff)
					// which we might accidentally include in our list.
					if (isFocusable(node) && (!pIndexedOnly || getTabIndex(node) != null))
					{
						focusableNodes.add(node);
					}
				}
				else if (node instanceof TabPane)
				{
					// We need to check for a TabPane because Nodes on a tab that
					// is not the selected one can not be distinguished from others.
					
					TabPane tabPane = (TabPane)node;
					
					focusableNodes.add(tabPane);
					
					Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
					
					if (selectedTab != null && selectedTab.getContent() != null)
					{
						focusableNodes.addAll(gatherFocusableNodes((Parent)selectedTab.getContent(), pIndexedOnly));
					}
				}
				else
				{
					List<Node> newFocusableNodes = gatherFocusableNodes((Parent)node, pIndexedOnly);
					focusableNodes.addAll(newFocusableNodes);
					
					if (newFocusableNodes.isEmpty() && isFocusable(node) && (!pIndexedOnly || getTabIndex(node) != null))
					{
						focusableNodes.add(node);
					}
				}
			}
		}
		
		return focusableNodes;
	}
	
	/**
	 * Gets the tab index (if any) from the given {@link Node}.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the tab index. {@code null} if there is none.
	 */
	public static Integer getTabIndex(Node pNode)
	{
		Object userData = pNode.getProperties().get(JavaFXResource.COMPONENT_KEY);
		
		if (userData instanceof JavaFXComponent<?>)
		{
			return ((JavaFXComponent<?>)userData).getTabIndex();
		}
		
		return null;
	}
	
	/**
	 * Checks if the given {@link Node} is focusable.
	 * 
	 * @param pNode the {@link Node} to check.
	 * @return {@code true} if the given {@link Node} is focusable.
	 */
	public static boolean isFocusable(Node pNode)
	{
		return pNode != null
				&& pNode.isFocusTraversable()
				&& pNode.isVisible()
				&& pNode.isManaged()
				&& !pNode.isDisabled()
				&& !(pNode instanceof TitledPane)
				&& !(pNode instanceof Pane);
	}
	
	/**
	 * Checks if the given {@link Parent}s children have been added in reverse
	 * order.
	 * 
	 * @param pParent the {@link Parent} to check.
	 * @return {@code true} if the children have been added in reverse order.
	 */
	public static boolean isReverseOrderNeeded(Parent pParent)
	{
		Object userData = pParent.getProperties().get(JavaFXResource.COMPONENT_KEY);
		
		if (userData instanceof JavaFXILayoutContainerHybrid)
		{
			return ((JavaFXILayoutContainerHybrid)userData).isReverseOrderNeeded();
		}
		
		return false;
	}
	
	/**
	 * Checks if the given {@link Node} should be traversed.
	 * 
	 * @param pNode the {@link Node} to check.
	 * @return {@code true} if the {@link Node} should be traversed.
	 */
	public static boolean isTraversable(Node pNode)
	{
		return pNode instanceof Parent && !(pNode instanceof TableView<?>);
	}
	
}	// JavaFXFocusUtil

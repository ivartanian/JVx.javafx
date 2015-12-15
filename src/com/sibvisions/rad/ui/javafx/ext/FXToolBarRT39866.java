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
package com.sibvisions.rad.ui.javafx.ext;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;

/**
 * {@link ToolBar} extension which fixes
 * <a href="https://javafx-jira.kenai.com/browse/RT-39866">RT-39866</a> which is
 * that the {@link ToolBar} does calculate its size and layout including the
 * not-managed components. Which results in a wrong size and overflow menu.
 * <p>
 * This extension does fix this issue by simply adding/removing items whenever
 * the managed property of these changes. Note that a listener will be attached
 * to any item that you add to this toolbar.
 * <p>
 * Also note that you should not use {@link #getItems()} directly in this
 * toolbar, but {@link #addItem(Node)}, {@link #addItems(Node...)},
 * {@link #removeAll()} and {@link #removeItem(Node)}.
 * 
 * @author Robert Zenz
 */
public class FXToolBarRT39866 extends ToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link List} of all items that have been added to this toolbar. */
	private List<Node> items;
	
	/**
	 * The listener for when the managed property of a {@link Node item}
	 * changes.
	 */
	private ChangeListener<Boolean> itemManagedListener;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXToolBarRT39866}.
	 */
	public FXToolBarRT39866()
	{
		super();
		
		itemManagedListener = this::onItemManagedChanged;
		
		items = new ArrayList<>();
	}
	
	/**
	 * Creates a new instance of {@link FXToolBarRT39866}.
	 *
	 * @param pItems the items to add.
	 */
	public FXToolBarRT39866(Node... pItems)
	{
		this();
		
		addItems(pItems);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight)
	{
		if (getOrientation() == Orientation.HORIZONTAL)
		{
			// TODO HACK Magic number needed for correct size.
			// I'm not sure what is going in ToolBarSkin that makes this necessary,
			// the HBox/VBox used by the skin is sometimes not correctly sized
			// and is off by roughly 9 pixels (give or take two).
			//
			// This might or might not be related to the overflow button, which
			// is coincidentally also 9 pixels width. Though, after reading
			// the code of ToolBarSkin, I fail to see how.
			double magicNumberThatFixesWidthIssues = 9;
			
			return super.computePrefWidth(pHeight) + magicNumberThatFixesWidthIssues;
		}
		else
		{
			return super.computePrefWidth(pHeight);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given item to the toolbar.
	 *
	 * @param pIndex the index at which the item should be inserted.
	 * @param pItem the item to add.
	 */
	public void addItem(int pIndex, Node pItem)
	{
		if (pIndex >= 0)
		{
			items.add(pIndex, pItem);
		}
		else
		{
			items.add(pItem);
		}
		
		rebuildToolBar();
		
		pItem.managedProperty().addListener(itemManagedListener);
	}
	
	/**
	 * Adds the given item to the toolbar.
	 *
	 * @param pItem the item to add.
	 */
	public void addItem(Node pItem)
	{
		addItem(-1, pItem);
	}
	
	/**
	 * Adds the given items to the toolbar.
	 *
	 * @param pItems the items to add.
	 */
	public void addItems(Node... pItems)
	{
		for (Node item : pItems)
		{
			addItem(-1, item);
		}
	}
	
	/**
	 * Removes all items from the toolbar.
	 */
	public void removeAll()
	{
		for (Node item : items)
		{
			item.managedProperty().removeListener(itemManagedListener);
		}
		
		items.clear();
		
		rebuildToolBar();
	}
	
	/**
	 * Removes the given item from the toolbar.
	 * 
	 * @param pItem the item to remove.
	 */
	public void removeItem(Node pItem)
	{
		pItem.managedProperty().removeListener(itemManagedListener);
		
		items.remove(pItem);
		
		rebuildToolBar();
	}
	
	/**
	 * Invoked if the managed property of a {@link Node item} changes.
	 * 
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onItemManagedChanged(ObservableValue<? extends Boolean> pObservableValue, Boolean pOldValue, Boolean pNewValue)
	{
		rebuildToolBar();
	}
	
	/**
	 * Rebuilds the toolbar, removes all {@link Node item}s and adds only those
	 * that are managed.
	 */
	private void rebuildToolBar()
	{
		getItems().clear();
		
		for (Node item : items)
		{
			if (item.isManaged())
			{
				getItems().add(item);
			}
		}
	}
	
}	// FXToolBarRT39866

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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/**
 * {@link MenuBar} extension which fixes
 * <a href="https://javafx-jira.kenai.com/browse/RT-40597">RT-40597</a> which is
 * that the {@link MenuBar} does not update correctly if the visibility of menus
 * changes that have been added after the skin was created.
 * <p>
 * This extension does fix this issue by simply adding/removing items whenever
 * the visibility property of these changes. Note that a listener will be
 * attached to any item that you add to this menubar.
 * <p>
 * Also note that you should not use {@link #getMenus()} directly in this
 * menubar, but {@link #addMenu(Menu)}, {@link #addMenu(Menu...)},
 * {@link #removeAll()} and {@link #removeMenu(Menu)}.
 * 
 * @author Robert Zenz
 */
public class FXMenuBarRT40597 extends MenuBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link List} of all menus that have been added to this menubar. */
	private List<Menu> menus;
	
	/** The listener for when the visibility of a {@link Menu} changes. */
	private ChangeListener<Boolean> menuVisibleListener;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXMenuBarRT40597}.
	 */
	public FXMenuBarRT40597()
	{
		super();
		
		menuVisibleListener = this::onMenuVisibleChanged;
		
		menus = new ArrayList<>();
	}
	
	/**
	 * Creates a new instance of {@link FXMenuBarRT40597}.
	 *
	 * @param pMenus the menus.
	 */
	public FXMenuBarRT40597(Menu... pMenus)
	{
		this();
		
		addMenu(pMenus);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given menu to the toolbar.
	 *
	 * @param pIndex the index at which the menu should be inserted.
	 * @param pMenu the menu to add.
	 */
	public void addMenu(int pIndex, Menu pMenu)
	{
		if (pIndex >= 0)
		{
			menus.add(pIndex, pMenu);
		}
		else
		{
			menus.add(pMenu);
		}
		
		rebuildMenu();
		
		pMenu.visibleProperty().addListener(menuVisibleListener);
	}
	
	/**
	 * Adds the given menu to the toolbar.
	 *
	 * @param pMenu the menu to add.
	 */
	public void addMenu(Menu pMenu)
	{
		addMenu(-1, pMenu);
	}
	
	/**
	 * Adds the given menus to the toolbar.
	 *
	 * @param pMenus the menus to add.
	 */
	public void addMenu(Menu... pMenus)
	{
		for (Menu menu : pMenus)
		{
			addMenu(-1, menu);
		}
	}
	
	/**
	 * Removes all menus from the toolbar.
	 */
	public void removeAll()
	{
		for (Menu menu : menus)
		{
			menu.visibleProperty().removeListener(menuVisibleListener);
		}
		
		menus.clear();
		
		rebuildMenu();
	}
	
	/**
	 * Removes the given menu from the toolbar.
	 * 
	 * @param pMenu the menu to remove.
	 */
	public void removeMenu(Menu pMenu)
	{
		pMenu.visibleProperty().removeListener(menuVisibleListener);
		
		menus.remove(pMenu);
		
		rebuildMenu();
	}
	
	/**
	 * Invoked if the visibility of a {@link Menu} changes.
	 * 
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onMenuVisibleChanged(ObservableValue<? extends Boolean> pObservableValue, Boolean pOldValue, Boolean pNewValue)
	{
		rebuildMenu();
	}
	
	/**
	 * Rebuilds the menu, removes all {@link Menu}s and adds only those that are
	 * visible.
	 */
	private void rebuildMenu()
	{
		getMenus().clear();
		
		for (Menu menu : menus)
		{
			if (menu.isVisible())
			{
				getMenus().add(menu);
			}
		}
	}
	
}	// FXMenuBarRT40597

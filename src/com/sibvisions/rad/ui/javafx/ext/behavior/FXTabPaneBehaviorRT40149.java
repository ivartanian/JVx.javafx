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
package com.sibvisions.rad.ui.javafx.ext.behavior;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.sun.javafx.scene.control.behavior.TabPaneBehavior;

/**
 * {@link FXTabPaneBehaviorRT40149} fixes
 * <a href="https://javafx-jira.kenai.com/browse/RT-40149">RT-40149</a>,
 * Ctrl+Page Up/Page Down is misbehaving if the last tab is disabled.
 * <p>
 * This extension also allows to disable these shortcuts completely.
 * 
 * @author Robert Zenz
 * @see TabPaneBehavior
 */
public class FXTabPaneBehaviorRT40149 extends TabPaneBehavior
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property if the Ctrl+Page Up/Page Down navigation is enabled. */
	private BooleanProperty pageUpDownEnabled;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTabPaneBehaviorRT40149}.
	 *
	 * @param pTabPane the tab pane.
	 */
	public FXTabPaneBehaviorRT40149(TabPane pTabPane)
	{
		super(pTabPane);
		
		pageUpDownEnabled = new SimpleBooleanProperty(false);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectNextTab()
	{
		int currentIndex = getControl().getSelectionModel().getSelectedIndex();
		int nextIndex = currentIndex + 1;
		
		List<Tab> tabs = getControl().getTabs();
		
		while (nextIndex != currentIndex)
		{
			if (nextIndex >= tabs.size())
			{
				nextIndex = 0;
			}
			
			if (!tabs.get(nextIndex).isDisabled())
			{
				getControl().getSelectionModel().select(nextIndex);
				return;
			}
			
			nextIndex++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectPreviousTab()
	{
		int currentIndex = getControl().getSelectionModel().getSelectedIndex();
		int previousIndex = currentIndex - 1;
		
		List<Tab> tabs = getControl().getTabs();
		
		while (previousIndex != currentIndex)
		{
			if (previousIndex < 0)
			{
				previousIndex = tabs.size() - 1;
			}
			
			if (!tabs.get(previousIndex).isDisabled())
			{
				getControl().getSelectionModel().select(previousIndex);
				return;
			}
			
			previousIndex--;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void callAction(String pName)
	{
		if ("Ctrl_Page_Up".equals(pName) || "Ctrl_Page_Down".equals(pName))
		{
			if (pageUpDownEnabled.get())
			{
				if ("Ctrl_Page_Up".equals(pName))
				{
					selectPreviousTab();
					getControl().requestFocus();
				}
				else if ("Ctrl_Page_Down".equals(pName))
				{
					selectNextTab();
					getControl().requestFocus();
				}
			}
		}
		else
		{
			super.callAction(pName);
		}
		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets if the Ctrl+Page Up/Page Down navigation is enabled.
	 * 
	 * @return {@code true} if the Ctrl+Page Up/Page Down navigation is enabled.
	 */
	public boolean isPageUpDownEnabled()
	{
		return pageUpDownEnabled.get();
	}
	
	/**
	 * Gets property if the Ctrl+Page Up/Page Down navigation is enabled.
	 * 
	 * @return the property if the Ctrl+Page Up/Page Down navigation is enabled.
	 */
	public BooleanProperty pageUpDownEnabledProperty()
	{
		return pageUpDownEnabled;
	}
	
	/**
	 * Sets if the Ctrl+Page Up/Page Down navigation should be enabled.
	 * 
	 * @param pEnabled {@code true} if the Ctrl+Page Up/Page Down navigation
	 *            should be enabled.
	 */
	public void setPageUpDownEnable(boolean pEnabled)
	{
		pageUpDownEnabled.set(pEnabled);
	}
	
}	// FXTabPaneBehaviorRT40149

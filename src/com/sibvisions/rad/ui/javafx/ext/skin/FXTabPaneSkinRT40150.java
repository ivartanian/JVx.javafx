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
package com.sibvisions.rad.ui.javafx.ext.skin;

import javafx.scene.control.TabPane;

import org.eclipse.fx.ui.controls.tabpane.skin.DnDTabPaneSkin;

import com.sibvisions.rad.ui.javafx.ext.behavior.FXTabPaneBehaviorRT40149;
import com.sibvisions.rad.ui.javafx.ext.util.FXBehaviorInjector;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;

/**
 * The {@link FXTabPaneSkinRT40150} provides a way to inject a custom behavior
 * into the {@link com.sun.javafx.scene.control.skin.TabPaneSkin}. See
 * <a href="https://javafx-jira.kenai.com/browse/RT-40150">RT-40150</a>.
 *
 * @author Robert Zenz
 * @see com.sun.javafx.scene.control.skin.TabPaneSkin
 */
public class FXTabPaneSkinRT40150 extends DnDTabPaneSkin
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTabPaneSkinRT40150}.
	 *
	 * @param pTabPane the tab pane.
	 */
	public FXTabPaneSkinRT40150(TabPane pTabPane)
	{
		this(pTabPane, new FXTabPaneBehaviorRT40149(pTabPane));
	}
	
	/**
	 * Creates a new instance of {@link FXTabPaneSkinRT40150}.
	 *
	 * @param pTabPane the tab pane.
	 * @param pCustomBehavior the custom behavior.
	 */
	public FXTabPaneSkinRT40150(TabPane pTabPane, TabPaneBehavior pCustomBehavior)
	{
		super(pTabPane);
		
		FXBehaviorInjector.inject(this, pCustomBehavior);
	}
	
}	// FXTabPaneSkinRT40150

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
package com.sibvisions.rad.ui.javafx.ext.mdi.skin;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXDesktopPane;
import com.sibvisions.rad.ui.javafx.ext.mdi.behavior.FXDesktopPaneBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * The default skin for the {@link FXDesktopPane}.
 * 
 * @author Robert Zenz
 */
public class FXDesktopPaneSkin extends BehaviorSkinBase<FXDesktopPane, FXDesktopPaneBehavior>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDesktopPaneSkin}.
	 *
	 * @param pDesktopPane the desktop pane.
	 */
	public FXDesktopPaneSkin(FXDesktopPane pDesktopPane)
	{
		this(pDesktopPane, new FXDesktopPaneBehavior(pDesktopPane));
	}
	
	/**
	 * Creates a new instance of {@link FXDesktopPaneSkin}.
	 *
	 * @param pDesktopPane the desktop pane.
	 * @param pBehavior the behavior.
	 */
	public FXDesktopPaneSkin(FXDesktopPane pDesktopPane, FXDesktopPaneBehavior pBehavior)
	{
		super(pDesktopPane, pBehavior);
	}
	
}	// FXDesktopPaneSkin

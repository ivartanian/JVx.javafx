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
package com.sibvisions.rad.ui.javafx.ext.mdi.behavior;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXDesktopPane;
import com.sun.javafx.scene.control.behavior.BehaviorBase;

/**
 * The default behavior for the {@link FXDesktopPane}.
 * <p>
 * Also provides navigation key support.
 * 
 * @author Robert Zenz
 */
public class FXDesktopPaneBehavior extends BehaviorBase<FXDesktopPane>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The event filter for key pressed events. */
	private EventHandler<KeyEvent> keyPressedFilter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDesktopPaneBehavior}.
	 *
	 * @param pDesktopPane the desktop pane.
	 */
	public FXDesktopPaneBehavior(FXDesktopPane pDesktopPane)
	{
		super(pDesktopPane, null);
		
		keyPressedFilter = this::onKeyPressedFilter;
		
		getControl().addEventFilter(KeyEvent.KEY_PRESSED, keyPressedFilter);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		getControl().removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedFilter);
		
		super.dispose();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked on key pressed.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyPressedFilter(KeyEvent pKeyEvent)
	{
		if (getControl().isNavigationKeysEnabled())
		{
			if (pKeyEvent.isControlDown() && pKeyEvent.getCode() == KeyCode.TAB)
			{
				pKeyEvent.consume();
				
				if (pKeyEvent.isShiftDown())
				{
					getControl().selectPreviousWindow();
				}
				else
				{
					getControl().selectNextWindow();
				}
			}
		}
	}
	
}	// FXDesktopPaneBehavior

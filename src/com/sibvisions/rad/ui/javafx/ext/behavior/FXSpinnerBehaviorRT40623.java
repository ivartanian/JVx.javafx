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

import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

import com.sun.javafx.scene.control.behavior.SpinnerBehavior;

/**
 * The {@link FXSpinnerBehaviorRT40623} is a {@link SpinnerBehavior} extensions
 * that allows to use the up/down keys to increment/decrement the values.
 * 
 * @author Robert Zenz
 * @param <T> the type of the items.
 */
public class FXSpinnerBehaviorRT40623<T> extends SpinnerBehavior<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link EventHandler} that is used for the key events. */
	private EventHandler<KeyEvent> keyPressedHandler;
	
	/** The {@link EventHandler} that is used for the mouse wheel events. */
	private EventHandler<ScrollEvent> scrollHandler;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXSpinnerBehaviorRT40623}.
	 *
	 * @param pSpinner the spinner.
	 */
	public FXSpinnerBehaviorRT40623(Spinner<T> pSpinner)
	{
		super(pSpinner);
		
		keyPressedHandler = this::onKeyPressed;
		scrollHandler = this::onScroll;
		
		pSpinner.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedHandler);
		pSpinner.addEventFilter(ScrollEvent.SCROLL, scrollHandler);
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
		getControl().removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedHandler);
		
		super.dispose();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked if a key is pressed.
	 * <p>
	 * Increments and decrements the value if the up or down key is pressed.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyPressed(KeyEvent pKeyEvent)
	{
		if (pKeyEvent.getCode() == KeyCode.UP)
		{
			getControl().getValueFactory().increment(1);
			pKeyEvent.consume();
		}
		else if (pKeyEvent.getCode() == KeyCode.DOWN)
		{
			getControl().getValueFactory().decrement(1);
			pKeyEvent.consume();
		}
	}
	
	/**
	 * Invoked if the mouse wheel is rotated.
	 * <p>
	 * Increments and decrements the value according to the mouse wheel.
	 * 
	 * @param pScrollEvent the event.
	 */
	private void onScroll(ScrollEvent pScrollEvent)
	{
		if (pScrollEvent.getDeltaY() >= 0)
		{
			getControl().getValueFactory().increment((int)(pScrollEvent.getDeltaY() / pScrollEvent.getMultiplierY()));
		}
		else
		{
			// Minus is needed because the decrement step count needs to be positive.
			getControl().getValueFactory().decrement((int)(-pScrollEvent.getDeltaY() / pScrollEvent.getMultiplierY()));
		}
		
		pScrollEvent.consume();
	}
	
}	// FXSpinnerBehaviorRT40623

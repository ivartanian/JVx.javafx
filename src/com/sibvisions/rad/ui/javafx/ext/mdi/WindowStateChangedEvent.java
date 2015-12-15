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
package com.sibvisions.rad.ui.javafx.ext.mdi;

import javafx.event.Event;
import javafx.event.EventType;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;

/**
 * The event if a {@link FXInternalWindow} changes its state.
 * 
 * @author Robert Zenz
 */
public class WindowStateChangedEvent extends WindowEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The serial version UID. */
	private static final long serialVersionUID = -5142034314014394576L;
	
	/** The window state changed event type. */
	public static final EventType<WindowStateChangedEvent> WINDOW_STATE_CHANGED = new EventType<>(Event.ANY, "WINDOW_STATE_CHANGED");
	
	/** The previous state. */
	private State previousState;
	
	/** The new state. */
	private State newState;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link WindowStateChangedEvent}.
	 *
	 * @param pWindow the window.
	 * @param pPreviousState the previous state.
	 * @param pNewState the new state.
	 */
	public WindowStateChangedEvent(FXInternalWindow pWindow, State pPreviousState, State pNewState)
	{
		super(pWindow, WINDOW_STATE_CHANGED);
		
		source = pWindow;
		previousState = pPreviousState;
		newState = pNewState;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the previous state.
	 * 
	 * @return the previous state.
	 */
	public State getPreviousState()
	{
		return previousState;
	}
	
	/**
	 * Gets the new state.
	 * 
	 * @return the new state.
	 */
	public State getNewState()
	{
		return newState;
	}
	
}	// WindowStateChangedEvent

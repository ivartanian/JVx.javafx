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

/**
 * The event if a {@link FXInternalWindow} has changed its modal state.
 * 
 * @author Robert Zenz
 */
public class WindowModalChangedEvent extends WindowEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The serial version UID. */
	private static final long serialVersionUID = 8892628023668850958L;
	
	/** The window state changed event type. */
	public static final EventType<WindowModalChangedEvent> WINDOW_MODAL_CHANGED = new EventType<>(Event.ANY, "WINDOW_MODAL_CHANGED");
	
	/** The new/next value. */
	private boolean newValue;
	
	/** The old/previous value. */
	private boolean oldValue;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link WindowModalChangedEvent}.
	 *
	 * @param pWindow the window.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	public WindowModalChangedEvent(FXInternalWindow pWindow, boolean pOldValue, boolean pNewValue)
	{
		super(pWindow, WINDOW_MODAL_CHANGED);
		
		oldValue = pOldValue;
		newValue = pNewValue;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the new value.
	 * 
	 * @return the new value.
	 */
	public boolean getNewValue()
	{
		return newValue;
	}
	
	/**
	 * Gets the old value.
	 * 
	 * @return the old value.
	 */
	public boolean getOldValue()
	{
		return oldValue;
	}
	
}	// WindowClosedEvent

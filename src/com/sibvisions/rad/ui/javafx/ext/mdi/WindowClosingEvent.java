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
 * The event if a {@link FXInternalWindow} is closing.
 * 
 * @author Robert Zenz
 */
public class WindowClosingEvent extends WindowEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The serial version UID. */
	private static final long serialVersionUID = -1913487288804451978L;
	
	/** The window state changed event type. */
	public static final EventType<WindowClosingEvent> WINDOW_CLOSING = new EventType<>(Event.ANY, "WINDOW_CLOSING");
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link WindowClosingEvent}.
	 *
	 * @param pWindow the window.
	 */
	public WindowClosingEvent(FXInternalWindow pWindow)
	{
		super(pWindow, WINDOW_CLOSING);
	}
	
}	// WindowClosingEvent

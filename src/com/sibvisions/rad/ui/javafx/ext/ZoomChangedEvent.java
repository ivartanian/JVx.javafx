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

import javafx.event.Event;
import javafx.event.EventType;

/**
 * The event if an {@link IFXZoomable} changes its zoom.
 * 
 * @author Robert Zenz
 */
public class ZoomChangedEvent extends Event
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The serial version UID. */
	private static final long serialVersionUID = 7203419785487230519L;
	
	/** The zoom changed event type. */
	public static final EventType<ZoomChangedEvent> ZOOM_CHANGED = new EventType<>(Event.ANY, "ZOOM_CHANGED");
	
	/**
	 * The new value of the {@link IFXZoomable#zoomProperty() zoom property}.
	 */
	protected double newValue;
	
	/**
	 * The old value of the {@link IFXZoomable#zoomProperty() zoom property}.
	 */
	protected double oldValue;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ZoomChangedEvent}.
	 *
	 * @param pZoomable the zoomable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	public ZoomChangedEvent(IFXZoomable pZoomable, double pOldValue, double pNewValue)
	{
		super(pZoomable, NULL_SOURCE_TARGET, ZOOM_CHANGED);
		
		oldValue = pOldValue;
		newValue = pNewValue;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link double old value} of the
	 * {@link IFXZoomable#zoomProperty() zoom property}.
	 *
	 * @return the {@link double old value} of the
	 *         {@link IFXZoomable#zoomProperty() zoom property}.
	 */
	public double getOldValue()
	{
		return oldValue;
	}
	
	/**
	 * Gets the {@link double new value} of the
	 * {@link IFXZoomable#zoomProperty() zoom property}.
	 *
	 * @return the {@link double new value} of the
	 *         {@link IFXZoomable#zoomProperty() zoom property}.
	 */
	public double getNewValue()
	{
		return newValue;
	}
	
	/**
	 * Gets the {@link IFXZoomable} that is the source of this event.
	 *
	 * @return the {@link IFXZoomable} that is the source of this event.
	 */
	public IFXZoomable getZoomable()
	{
		return (IFXZoomable)source;
	}
	
}	// ZoomChangedEvent

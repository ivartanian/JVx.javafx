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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.ui.container.IWindow;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.event.type.window.IWindowActivatedListener;
import javax.rad.ui.event.type.window.IWindowClosedListener;
import javax.rad.ui.event.type.window.IWindowClosingListener;
import javax.rad.ui.event.type.window.IWindowDeactivatedListener;
import javax.rad.ui.event.type.window.IWindowDeiconifiedListener;
import javax.rad.ui.event.type.window.IWindowIconifiedListener;
import javax.rad.ui.event.type.window.IWindowOpenedListener;

import javafx.scene.layout.Region;

/**
 * The {@link JavaFXAbstractWindowBase} is the abstract base for any component
 * that implements {@link IWindow}.
 * 
 * @author Robert Zenz
 * @param <C> the type of the component.
 * @see IWindow
 */
public abstract class JavaFXAbstractWindowBase<C extends Region> extends JavaFXAbstractForwardingContainer<C> implements IWindow
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link WindowHandler} for the activated event. */
	private WindowHandler<IWindowActivatedListener> eventWindowActivated;
	
	/** The {@link WindowHandler} for the closed event. */
	private WindowHandler<IWindowClosedListener> eventWindowClosed;
	
	/** The {@link WindowHandler} for the closing event. */
	private WindowHandler<IWindowClosingListener> eventWindowClosing;
	
	/** The {@link WindowHandler} for the deactivated event. */
	private WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated;
	
	/** The {@link WindowHandler} for the deiconified event. */
	private WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified;
	
	/** The {@link WindowHandler} for the iconified event. */
	private WindowHandler<IWindowIconifiedListener> eventWindowIconified;
	
	/** The {@link WindowHandler} for the opened event. */
	private WindowHandler<IWindowOpenedListener> eventWindowOpened;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractWindowBase}.
	 *
	 * @param pContainer the container.
	 */
	protected JavaFXAbstractWindowBase(C pContainer)
	{
		super(pContainer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowActivatedListener> eventWindowActivated()
	{
		if (eventWindowActivated == null)
		{
			eventWindowActivated = new WindowHandler<>(IWindowActivatedListener.class);
		}
		
		return eventWindowActivated;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowClosedListener> eventWindowClosed()
	{
		if (eventWindowClosed == null)
		{
			eventWindowClosed = new WindowHandler<>(IWindowClosedListener.class);
		}
		
		return eventWindowClosed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowClosingListener> eventWindowClosing()
	{
		if (eventWindowClosing == null)
		{
			eventWindowClosing = new WindowHandler<>(IWindowClosingListener.class);
		}
		
		return eventWindowClosing;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated()
	{
		if (eventWindowDeactivated == null)
		{
			eventWindowDeactivated = new WindowHandler<>(IWindowDeactivatedListener.class);
		}
		
		return eventWindowDeactivated;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified()
	{
		if (eventWindowDeiconified == null)
		{
			eventWindowDeiconified = new WindowHandler<>(IWindowDeiconifiedListener.class);
		}
		
		return eventWindowDeiconified;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowIconifiedListener> eventWindowIconified()
	{
		if (eventWindowIconified == null)
		{
			eventWindowIconified = new WindowHandler<>(IWindowIconifiedListener.class);
		}
		
		return eventWindowIconified;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler<IWindowOpenedListener> eventWindowOpened()
	{
		if (eventWindowOpened == null)
		{
			eventWindowOpened = new WindowHandler<>(IWindowOpenedListener.class);
		}
		
		return eventWindowOpened;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Fires the window activated event.
	 */
	protected void fireWindowActivated()
	{
		fireWindowEvent(eventWindowActivated, UIWindowEvent.WINDOW_ACTIVATED);
	}
	
	/**
	 * Fires the window closed event.
	 */
	protected void fireWindowClosed()
	{
		fireWindowEvent(eventWindowClosed, UIWindowEvent.WINDOW_CLOSED);
	}
	
	/**
	 * Fires the window closing event.
	 */
	protected void fireWindowClosing()
	{
		fireWindowEvent(eventWindowClosing, UIWindowEvent.WINDOW_CLOSING);
	}
	
	/**
	 * Fires the window deactivated event.
	 */
	protected void fireWindowDeactivated()
	{
		fireWindowEvent(eventWindowDeactivated, UIWindowEvent.WINDOW_DEACTIVATED);
	}
	
	/**
	 * Fires the window deiconified event.
	 */
	protected void fireWindowDeiconified()
	{
		fireWindowEvent(eventWindowDeiconified, UIWindowEvent.WINDOW_DEICONIFIED);
	}
	
	/**
	 * Fires the window iconified event.
	 */
	protected void fireWindowIconified()
	{
		fireWindowEvent(eventWindowIconified, UIWindowEvent.WINDOW_ICONIFIED);
	}
	
	/**
	 * Fires the window opened event.
	 */
	protected void fireWindowOpened()
	{
		fireWindowEvent(eventWindowOpened, UIWindowEvent.WINDOW_OPENED);
	}
	
	/**
	 * Fires a window event.
	 * 
	 * @param pWindowHandler the {@link WindowHandler} to sent the event to.
	 * @param pEventId the ID of the event.
	 */
	private void fireWindowEvent(WindowHandler<?> pWindowHandler, int pEventId)
	{
		if (pWindowHandler != null)
		{
			UIWindowEvent event = new UIWindowEvent(
					eventSource,
					pEventId,
					System.currentTimeMillis(),
					0);
					
			pWindowHandler.dispatchEvent(event);
		}
	}
	
}	// JavaFXAbstractWindowBase

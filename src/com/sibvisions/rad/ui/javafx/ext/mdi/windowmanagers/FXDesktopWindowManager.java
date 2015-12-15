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
package com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javafx.collections.ListChangeListener.Change;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.ZoomChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;
import com.sibvisions.rad.ui.javafx.ext.mdi.IFXWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowModalChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowStateChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.panes.FXPositioningPane;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;

/**
 * The {@link FXDesktopWindowManager} manages {@link FXInternalWindow}s in a
 * traditional desktop-like metaphor.
 * 
 * @author Robert Zenz
 */
public class FXDesktopWindowManager implements IFXWindowManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The {@link Pane} that is used as main container for the
	 * {@link FXInternalWindpw}s.
	 */
	private FXCustomToFrontPositioningPane pane;
	
	/**
	 * The listener for if a {@link FXInternalWindow} changes its modal state.
	 */
	private EventHandler<WindowModalChangedEvent> windowModalChangedListener;
	
	/** The listener for if a {@link FXInternalWindow} changes its state. */
	private EventHandler<WindowStateChangedEvent> windowStateChangedListener;
	
	/** The listener for if the zoom changes. */
	private EventHandler<ZoomChangedEvent> windowZoomChangedListener;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDesktopWindowManager}.
	 */
	public FXDesktopWindowManager()
	{
		windowModalChangedListener = this::onWindowModalChanged;
		windowStateChangedListener = this::onWindowStateChanged;
		windowZoomChangedListener = this::onWindowZoomChanged;
		
		pane = new FXCustomToFrontPositioningPane(this);
		pane.setAutoSizeOnlyOnPositioning(true);
		pane.getChildren().addListener(this::onPaneChildrenListChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addWindow(FXInternalWindow pWindow)
	{
		pWindow.setBorderless(pWindow.getState() == State.MAXIMIZED);
		
		pWindow.addEventHandler(WindowModalChangedEvent.WINDOW_MODAL_CHANGED, windowModalChangedListener);
		pWindow.addEventHandler(WindowStateChangedEvent.WINDOW_STATE_CHANGED, windowStateChangedListener);
		pWindow.addEventHandler(ZoomChangedEvent.ZOOM_CHANGED, windowZoomChangedListener);
		
		pane.getChildren().add(pWindow);
		
		if (pWindow.isModal())
		{
			FXSceneLocker.addLock(pWindow);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		pane.getChildren().clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pane getPane()
	{
		return pane;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeWindow(FXInternalWindow pWindow)
	{
		pWindow.removeEventHandler(WindowModalChangedEvent.WINDOW_MODAL_CHANGED, windowModalChangedListener);
		pWindow.removeEventHandler(WindowStateChangedEvent.WINDOW_STATE_CHANGED, windowStateChangedListener);
		pWindow.removeEventHandler(ZoomChangedEvent.ZOOM_CHANGED, windowZoomChangedListener);
		
		FXSceneLocker.removeLock(pWindow);
		
		pane.getChildren().remove(pWindow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState(Object pState)
	{
		if (pState != null)
		{
			for (Node window : pane.getChildren())
			{
				if (((SessionState)pState).restoreState((FXInternalWindow)window))
				{
					pane.doNotMove(window);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object storeState()
	{
		SessionState state = new SessionState();
		
		for (Node window : pane.getChildren())
		{
			state.storeState((FXInternalWindow)window);
		}
		
		return state;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateActiveWindow()
	{
		List<Node> children = pane.getChildren();
		
		if (!children.isEmpty())
		{
			for (int idx = 0; idx < children.size() - 1; idx++)
			{
				((FXInternalWindow)children.get(idx)).setActive(false);
			}
			
			((FXInternalWindow)children.get(children.size() - 1)).setActive(true);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked if the list of children in the {@link Pane} changes.
	 * 
	 * @param pChange the changes.
	 */
	private void onPaneChildrenListChanged(Change<? extends Node> pChange)
	{
		if (!pane.isIgnoreChildrenListEvents())
		{
			updateActiveWindow();
		}
	}
	
	/**
	 * Invoked if the modal state of a {@link FXInternalWindow} changed.
	 * 
	 * @param pWindowModalChangedEvent the event.
	 */
	private void onWindowModalChanged(WindowModalChangedEvent pWindowModalChangedEvent)
	{
		FXInternalWindow window = pWindowModalChangedEvent.getWindow();
		
		FXSceneLocker.removeLock(window);
		
		if (pWindowModalChangedEvent.getNewValue())
		{
			FXSceneLocker.addLock(window);
		}
	}
	
	/**
	 * Invoked if the state of a {@link FXInternalWindow} changed.
	 * 
	 * @param pWindowStateChangedEvent the event.
	 */
	private void onWindowStateChanged(WindowStateChangedEvent pWindowStateChangedEvent)
	{
		FXInternalWindow window = pWindowStateChangedEvent.getWindow();
		
		switch (pWindowStateChangedEvent.getNewState())
		{
			case MAXIMIZED:
				window.setBorderless(true);
				window.setMovable(false);
				setContentVisible(window, true);
				break;
				
			case MINIMIZED:
				window.setBorderless(false);
				window.setMovable(true);
				setContentVisible(window, false);
				window.autosize();
				break;
				
			case NORMAL:
			default:
				window.setBorderless(false);
				window.setMovable(true);
				setContentVisible(window, true);
				restoreBounds(window);
				
		}
	}
	
	/**
	 * Invoked if the zoom of a {@link FXInternalWindow} changed.
	 * 
	 * @param pZoomChangedEvent the event.
	 */
	private void onWindowZoomChanged(ZoomChangedEvent pZoomChangedEvent)
	{
		FXInternalWindow window = (FXInternalWindow)pZoomChangedEvent.getZoomable();
		double newZoomValue = pZoomChangedEvent.getNewValue();
		
		Parent parent = window.getParent();
		Bounds bounds = window.localToScene(window.getLayoutBounds());
		Bounds parentBounds = parent.localToScene(parent.getLayoutBounds());
		
		if (parentBounds.getMinY() > bounds.getMinY())
		{
			window.relocate(window.getLayoutX(), (bounds.getHeight() - window.getHeight()) / 2);
		}
		else if (bounds.getMinY() + bounds.getHeight() / 3 > parentBounds.getMaxY())
		{
			window.relocate(window.getLayoutX(), parentBounds.getHeight() - (bounds.getHeight() / newZoomValue) / 3);
		}
		
		if (bounds.getMinX() + bounds.getWidth() * 0.66 < parentBounds.getMinX())
		{
			window.relocate(parentBounds.getMinX() - (bounds.getWidth() / newZoomValue) * 0.66, window.getLayoutY());
		}
		else if (bounds.getMinX() + bounds.getWidth() / 3 > parentBounds.getMaxX())
		{
			window.relocate(parentBounds.getWidth() - (bounds.getWidth() / newZoomValue) / 3, window.getLayoutY());
		}
	}
	
	/**
	 * Restores the bounds of the given {@link FXInternalWindow}.
	 * 
	 * @param pWindow the {@link FXInternalWindow}.
	 */
	private void restoreBounds(FXInternalWindow pWindow)
	{
		Bounds previousBounds = pWindow.getPreviousBounds(pWindow.getState());
		
		if (previousBounds != null)
		{
			pWindow.resizeRelocate(
					previousBounds.getMinX(),
					previousBounds.getMinY(),
					previousBounds.getWidth(),
					previousBounds.getHeight());
		}
	}
	
	/**
	 * Sets the content of the given {@link FXInternalWindow} visible or
	 * invisible.
	 * 
	 * @param pWindow the {@link FXInternalWindow}.
	 * @param pVisible {@code true} if the content should be visible.
	 */
	private void setContentVisible(FXInternalWindow pWindow, boolean pVisible)
	{
		Node content = pWindow.getContent();
		
		if (content != null)
		{
			content.setManaged(pVisible);
			content.setVisible(pVisible);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Encapsulates the state of the session.
	 * 
	 * @author Robert Zenz
	 */
	protected static class SessionState
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link Map} of bounds. */
		private Map<FXInternalWindow, Bounds> windowBounds;
		
		/** The {@link Map} of states. */
		private Map<FXInternalWindow, State> windowStates;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link SessionState}.
		 */
		public SessionState()
		{
			windowBounds = new WeakHashMap<>();
			windowStates = new WeakHashMap<>();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Restores the values for/on the given {@link FXInternalWindow}.
		 * 
		 * @param pWindow the {@link FXInternalWindow}.
		 * @return {@code true} if there was something restored.
		 */
		public boolean restoreState(FXInternalWindow pWindow)
		{
			Bounds bounds = windowBounds.get(pWindow);
			if (bounds != null)
			{
				pWindow.resizeRelocate(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
			}
			
			State state = windowStates.get(pWindow);
			if (state != null)
			{
				pWindow.setState(state);
			}
			
			return bounds != null || state != null;
		}
		
		/**
		 * Stores the values of the given {@link FXInternalWindow}.
		 * 
		 * @param pWindow the {@link FXInternalWindow}.
		 */
		public void storeState(FXInternalWindow pWindow)
		{
			if (pWindow.getState() != State.NORMAL)
			{
				windowBounds.put(pWindow, pWindow.getPreviousBounds(State.NORMAL));
			}
			else
			{
				Bounds bounds = new BoundingBox(pWindow.getLayoutX(), pWindow.getLayoutY(), pWindow.getWidth(), pWindow.getHeight());
				windowBounds.put(pWindow, bounds);
			}
			
			windowStates.put(pWindow, pWindow.getState());
		}
		
	}	// SessionState
	
	/**
	 * The {@link FXCustomToFrontPositioningPane} is an
	 * {@link FXPositioningPane} extension that knows how to handled maximized
	 * {@link FXInternalWindow}s.
	 * 
	 * @author Robert Zenz
	 */
	private static final class FXCustomToFrontPositioningPane extends FXPositioningPane
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** If the parent should ignore events of the children list. */
		private boolean ignoreChildrenListEvents;
		
		/** The parent window manager. */
		private FXDesktopWindowManager windowManager;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link FXCustomToFrontPositioningPane}.
		 * 
		 * @param pWindowManager the {@link FXDesktopWindowManager} to use.
		 */
		public FXCustomToFrontPositioningPane(FXDesktopWindowManager pWindowManager)
		{
			windowManager = pWindowManager;
			
			// Needed to make this panel transparent to events.
			// Otherwise nodes that are added to the desktop panel, which are
			// not in a window, will not receive any events because
			// the FXCustomToFrontPositioningPane would consume them first.
			setPickOnBounds(false);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void toFront(Node pChild)
		{
			if (getChildren().indexOf(pChild) != getChildren().size() - 1)
			{
				ignoreChildrenListEvents = true;
				
				super.toFront(pChild);
				
				ignoreChildrenListEvents = false;
				
				// Inform the parent that it can now select the correct window.
				windowManager.updateActiveWindow();
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void layoutChildren()
		{
			super.layoutChildren();
			
			for (Node node : getManagedChildren())
			{
				if (node instanceof FXInternalWindow)
				{
					FXInternalWindow window = (FXInternalWindow)node;
					
					if (window.getState() == State.MAXIMIZED)
					{
						window.resizeRelocate(0, 0, getWidth(), getHeight());
					}
				}
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets if the parent should ignore changes in the children list.
		 * 
		 * @return {@code true} if the parent should ignore changes.
		 */
		public boolean isIgnoreChildrenListEvents()
		{
			return ignoreChildrenListEvents;
		}
		
	}	// FXCustomToFrontPositioningPane
	
}	// FXDesktopWindowManager

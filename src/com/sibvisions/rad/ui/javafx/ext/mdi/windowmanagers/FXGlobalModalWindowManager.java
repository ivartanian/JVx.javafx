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

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.FXOverlayRegion;
import com.sibvisions.rad.ui.javafx.ext.FXZoomRegion;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.IFXWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowModalChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.scene.StackedScenePane;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;

/**
 * The {@link FXGlobalModalWindowManager} allows to forward modal windows to a
 * {@link FXOverlayRegion} which is either the root of the parent {@link Scene},
 * or the root parent {@link StackedScenePane}.
 * <p>
 * It is a thin layer on top of any other {@link IFXWindowManager}.
 * 
 * @author Robert Zenz
 */
public class FXGlobalModalWindowManager implements IFXWindowManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The encapsulated {@link IFXWindowManager}. */
	private IFXWindowManager internalWindowManager;
	
	/** The handler for when a {@link FXInternalWindow} changes its modal state. */
	private EventHandler<WindowModalChangedEvent> windowModalChangedHandler;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXGlobalModalWindowManager}.
	 *
	 * @param pWindowManager the {@link IFXWindowManager}.
	 */
	public FXGlobalModalWindowManager(IFXWindowManager pWindowManager)
	{
		windowModalChangedHandler = this::onWindowModalChanged;
		
		internalWindowManager = pWindowManager;
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
		if (pWindow.isModal())
		{
			Pane overlayPane = getOverlayPane();
			
			if (overlayPane != null)
			{
				overlayPane.getChildren().add(pWindow);
				FXSceneLocker.addLock(pWindow);
				return;
			}
		}
		
		internalWindowManager.addWindow(pWindow);
		
		pWindow.addEventHandler(WindowModalChangedEvent.WINDOW_MODAL_CHANGED, windowModalChangedHandler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		internalWindowManager.dispose();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pane getPane()
	{
		return internalWindowManager.getPane();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeWindow(FXInternalWindow pWindow)
	{
		if (pWindow.isModal())
		{
			Pane overlayPane = getOverlayPane();
			
			if (overlayPane != null)
			{
				FXSceneLocker.removeLock(pWindow);
				overlayPane.getChildren().remove(pWindow);
				return;
			}
		}
		
		internalWindowManager.removeWindow(pWindow);
		
		pWindow.removeEventHandler(WindowModalChangedEvent.WINDOW_MODAL_CHANGED, windowModalChangedHandler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState(Object pState)
	{
		internalWindowManager.restoreState(pState);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object storeState()
	{
		return internalWindowManager.storeState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateActiveWindow()
	{
		internalWindowManager.updateActiveWindow();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked if a {@link FXInternalWindow} changes it modal state.
	 * 
	 * @param pWindowModalChangedEvent the event.
	 */
	private void onWindowModalChanged(WindowModalChangedEvent pWindowModalChangedEvent)
	{
		Pane overlayPane = getOverlayPane();
		
		if (overlayPane != null)
		{
			FXInternalWindow window = pWindowModalChangedEvent.getWindow();
			
			if (pWindowModalChangedEvent.getOldValue())
			{
				FXSceneLocker.removeLock(window);
				overlayPane.getChildren().remove(window);
				internalWindowManager.addWindow(window);
			}
			
			if (pWindowModalChangedEvent.getNewValue())
			{
				internalWindowManager.removeWindow(window);
				overlayPane.getChildren().add(pWindowModalChangedEvent.getWindow());
				FXSceneLocker.addLock(window);
				
				window.setBorderless(false);
				window.setDecorated(true);
			}
		}
	}
	
	/**
	 * Gets the overlay {@link Pane} for modal {@link FXInternalWindow}s, if
	 * there is any.
	 * 
	 * @return the overlay {@link Pane}. {@code null} if there is none.
	 */
	private Pane getOverlayPane()
	{
		Scene scene = internalWindowManager.getPane().getScene();
		
		if (scene != null)
		{
			Node root = scene.getRoot();
			
			if (root instanceof StackedScenePane)
			{
				root = ((StackedScenePane) root).getRoot();
			}
			
			if (root instanceof FXZoomRegion)
			{
				root = ((FXZoomRegion) root).getContent();
			}
			
			if (root instanceof FXOverlayRegion)
			{
				return ((FXOverlayRegion) root).getOverlayPane();
			}
		}
		
		return null;
	}
	
}	// FXGlobalModalWindowManager

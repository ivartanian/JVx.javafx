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

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.IFXWindowManager;
import com.sibvisions.rad.ui.javafx.ext.panes.FXStackPane;

/**
 * The {@link FXSingleWindowManager} is an {@link IFXWindowManager}
 * implementation that does display every single window in its full size and
 * without decoration or border.
 * <p>
 * It is basically a {@link javafx.scene.layout.StackPane}.
 * 
 * @author Robert Zenz
 */
public class FXSingleWindowManager implements IFXWindowManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link FXStackPane} that is used for layouting. */
	private FXStackPane pane;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXSingleWindowManager}.
	 */
	public FXSingleWindowManager()
	{
		pane = new FXStackPane();
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
		pWindow.setBorderless(true);
		pWindow.setDecorated(false);
		
		pane.getChildren().add(pWindow);
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
		pane.getChildren().remove(pWindow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState(Object pState)
	{
		// NOOP
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object storeState()
	{
		// NOOP
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateActiveWindow()
	{
		if (pane.getChildren().isEmpty())
		{
			return;
		}
		
		FXInternalWindow lastChild = (FXInternalWindow)pane.getChildren().get(pane.getChildren().size() - 1);
		
		for (Node child : pane.getChildren())
		{
			if (child != lastChild)
			{
				((FXInternalWindow)child).setActive(false);
			}
			else
			{
				((FXInternalWindow)child).setActive(true);
			}
		}
	}
	
}	// FXSingleWindowManager

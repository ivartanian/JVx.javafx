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

import javax.rad.ui.IComponent;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXDesktopPane;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXDesktopWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXGlobalModalWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXTabWindowManager;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXDesktopPanel} is the JavaFX specific implementation of
 * {@link IDesktopPanel}.
 * 
 * @author Robert Zenz
 * @see IDesktopPanel
 * @see FXDesktopPane
 */
public class JavaFXDesktopPanel extends JavaFXAbstractForwardingContainer<FXDesktopPane> implements IDesktopPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link StackPane} that will be used for "normal" components. */
	private StackPane componentPane;
	
	/** Whether tab mode is activated. */
	private boolean tabMode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXDesktopPanel}.
	 */
	public JavaFXDesktopPanel()
	{
		super(new FXDesktopPane(new FXGlobalModalWindowManager(new FXDesktopWindowManager())));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNavigationKeysEnabled()
	{
		return resource.isNavigationKeysEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTabMode()
	{
		return tabMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		resource.setNavigationKeysEnabled(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabMode(boolean pTabMode)
	{
		if (pTabMode != tabMode)
		{
			if (pTabMode)
			{
				resource.setWindowManager(new FXGlobalModalWindowManager(new FXTabWindowManager()));
			}
			else
			{
				resource.setWindowManager(new FXGlobalModalWindowManager(new FXDesktopWindowManager()));
			}
		}
		
		tabMode = pTabMode;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addInternal(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pComponent instanceof IInternalFrame)
		{
			resource.getWindows().add((FXInternalWindow)pComponent.getResource());
		}
		else
		{
			super.addInternal(pComponent, pConstraints, pIndex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeAllInternal()
	{
		resource.getWindows().clear();
		super.removeAllInternal();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeInternal(IComponent pComponent)
	{
		if (pComponent instanceof IInternalFrame)
		{
			resource.getWindows().remove(pComponent.getResource());
		}
		else
		{
			super.removeInternal(pComponent);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pHybrid)
	{
		if (componentPane == null)
		{
			componentPane = new StackPane();
			resource.setBackgroundPane(componentPane);
		}
		
		componentPane.getChildren().add((Node)pHybrid.getResource());
	}
	
}	// JavaFXDesktopPanel

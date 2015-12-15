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

import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.menu.IMenuBar;

import javafx.scene.layout.Region;

/**
 * The {@link JavaFXAbstractFrameBase} is the abstract base for any component
 * that implements {@link IFrame}.
 * 
 * @author Robert Zenz
 * @param <C> the type of the component.
 * @see IFrame
 */
public abstract class JavaFXAbstractFrameBase<C extends Region> extends JavaFXAbstractWindowBase<C>
		implements IFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link JavaFXToolBarPanel} that is used. */
	protected JavaFXToolBarPanel toolBarPanel;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractFrameBase}.
	 *
	 * @param pContainer the container.
	 */
	protected JavaFXAbstractFrameBase(C pContainer)
	{
		super(pContainer);
		
		toolBarPanel = new JavaFXToolBarPanel();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToolBar(IToolBar pToolBar)
	{
		toolBarPanel.addToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		toolBarPanel.addToolBar(pToolBar, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMenuBar getMenuBar()
	{
		return toolBarPanel.getEmbeddedMenuBar();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IToolBar getToolBar(int pIndex)
	{
		return toolBarPanel.getToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getToolBarArea()
	{
		return toolBarPanel.getToolBarArea();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getToolBarCount()
	{
		return toolBarPanel.getToolBarCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolBarPanel.indexOfToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllToolBars()
	{
		toolBarPanel.removeAllToolBars();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeToolBar(int pIndex)
	{
		toolBarPanel.removeToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeToolBar(IToolBar pToolBar)
	{
		toolBarPanel.removeToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMenuBar(IMenuBar pMenuBar)
	{
		IMenuBar mbar = toolBarPanel.getEmbeddedMenuBar();
		
		// First, unset an already existing parent.
		if (mbar != null)
		{
			mbar.setParent(null);
		}
		
		toolBarPanel.setEmbeddedMenuBar(pMenuBar);
		
		if (pMenuBar != null)
		{
			pMenuBar.setParent(this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolBarArea(int pArea)
	{
		toolBarPanel.setToolBarArea(pArea);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
}	// JavaFXAbstractFrameBase

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
package com.sibvisions.rad.ui.javafx.impl.menu;

import javafx.scene.control.Menu;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.menu.IMenuBar;

import com.sibvisions.rad.ui.javafx.ext.FXMenuBarRT40597;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXAbstractContainer;

/**
 * The {@link JavaFXMenuBar} is the JavaFX specific implementation of
 * {@link IMenuBar}.
 * 
 * @author Robert Zenz
 * @see IMenuBar
 * @see javafx.scene.control.MenuBar
 */
public class JavaFXMenuBar extends JavaFXAbstractContainer<FXMenuBarRT40597> implements IMenuBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXMenuBar}.
	 */
	public JavaFXMenuBar()
	{
		super(new FXMenuBarRT40597());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not used, always returns {@code null}.
	 * 
	 * @return {@code null}.
	 */
	@Override
	public ILayout<?> getLayout()
	{
		// NOOP
		return null;
	}
	
	/**
	 * Not used.
	 * 
	 * @param pLayout not used.
	 */
	@Override
	public void setLayout(@SuppressWarnings("rawtypes") ILayout pLayout)
	{
		// NOOP
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
		if (pIndex >= 0)
		{
			resource.addMenu(pIndex, (Menu) pComponent.getResource());
		}
		else
		{
			resource.addMenu((Menu) pComponent.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isReverseAddOrderNeeded()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeAllInternal()
	{
		resource.removeAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeInternal(IComponent pComponent)
	{
		resource.removeMenu((Menu) pComponent.getResource());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		if (pParent == null || pParent instanceof IFrame)
		{
			parent = pParent;
		}
		else
		{
			throw new IllegalArgumentException("Only 'IFrame' instances are allowed as parent of a 'MenuBar'");
		}
	}
	
}	// JavaFXMenuBar

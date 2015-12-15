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
import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IToolBar;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.FXToolBarRT39866;
import com.sibvisions.rad.ui.javafx.impl.JavaFXInsets;

/**
 * The {@link JavaFXToolBar} is the JavaFX specific implementation of
 * {@link IToolBar}.
 * 
 * @author Robert Zenz
 * @see IToolBar
 * @see FXToolBarRT39866
 */
public class JavaFXToolBar extends JavaFXAbstractContainer<FXToolBarRT39866> implements IToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXToolBar}.
	 */
	public JavaFXToolBar()
	{
		super(new FXToolBarRT39866());
		
		// Set better padding, to make the toolbar smaller.
		resource.setPadding(new Insets(1, 1, 2, 2));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILayout<?> getLayout()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInsets getMargins()
	{
		return new JavaFXInsets(resource.getPadding());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrientation()
	{
		if (resource.getOrientation() == Orientation.HORIZONTAL)
		{
			return HORIZONTAL;
		}
		else
		{
			return VERTICAL;
		}
	}
	
	/**
	 * Moving the toolbars is not available in JavaFX.
	 * 
	 * @return always {@code false}.
	 */
	@Override
	public boolean isMovable()
	{
		// TODO Moving the toolbars by the user is not available.
		return false;
	}
	
	/**
	 * Not used by this container.
	 * 
	 * @param pLayout not used.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void setLayout(ILayout pLayout)
	{
		// Not used by this container.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMargins(IInsets pMargin)
	{
		if (pMargin != null)
		{
			resource.setPadding((Insets)pMargin.getResource());
		}
		else
		{
			resource.setPadding(null);
		}
	}
	
	/**
	 * Moving the toolbars is not available in JavaFX.
	 * 
	 * @param pMovable ignored.
	 */
	@Override
	public void setMovable(boolean pMovable)
	{
		// TODO Moving the toolbars by the user is not available.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOrientation(int pOrientation)
	{
		if (pOrientation == HORIZONTAL)
		{
			resource.setOrientation(Orientation.HORIZONTAL);
		}
		else
		{
			resource.setOrientation(Orientation.VERTICAL);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addInternal(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pIndex >= 0)
		{
			resource.addItem(pIndex, (Node)pComponent.getResource());
		}
		else
		{
			resource.addItem((Node)pComponent.getResource());
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
		resource.removeItem((Node)pComponent.getResource());
	}
	
}	// JavaFXToolBar

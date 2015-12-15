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

import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;

import javax.rad.ui.IColor;
import javax.rad.ui.ICursor;
import javax.rad.ui.IFont;
import javax.rad.ui.menu.ISeparator;

import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;

/**
 * The {@link JavaFXSeparator} is the JavaFX specific implementation of
 * {@link ISeparator}.
 * 
 * @author Robert Zenz
 * @see ISeparator
 * @see Separator
 * @see SeparatorMenuItem
 */
public class JavaFXSeparator extends JavaFXComponent<Separator> implements ISeparator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The separator menu item. */
	private JavaFXMenuBase<SeparatorMenuItem> separatorMenuItem;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXSeparator}.
	 */
	public JavaFXSeparator()
	{
		super(new Separator());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pBackground)
	{
		super.setBackground(pBackground);
		
		if (separatorMenuItem != null)
		{
			separatorMenuItem.setBackground(pBackground);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCursor(ICursor pCursor)
	{
		super.setCursor(pCursor);
		
		if (separatorMenuItem != null)
		{
			separatorMenuItem.setCursor(pCursor);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(IFont pFont)
	{
		super.setFont(pFont);
		
		if (separatorMenuItem != null)
		{
			separatorMenuItem.setFont(pFont);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(IColor pForeground)
	{
		super.setForeground(pForeground);
		
		if (separatorMenuItem != null)
		{
			separatorMenuItem.setForeground(pForeground);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		super.setVisible(pVisible);
		
		if (separatorMenuItem != null)
		{
			separatorMenuItem.setVisible(pVisible);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets this separator as separator menu item.
	 * 
	 * @return this separator as separator menu item.
	 */
	public SeparatorMenuItem getAsMenuItem()
	{
		if (separatorMenuItem == null)
		{
			separatorMenuItem = new JavaFXMenuBase<>(new SeparatorMenuItem());
			
			separatorMenuItem.setBackground(getBackground());
			separatorMenuItem.setCursor(getCursor());
			separatorMenuItem.setFont(getFont());
			separatorMenuItem.setForeground(getForeground());
			separatorMenuItem.setVisible(isVisible());
		}
		
		return (SeparatorMenuItem) separatorMenuItem.getResource();
	}
	
}	// JavaFXSeparator

/*
 * Copyright 2014 SIB Visions GmbH
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
package com.sibvisions.rad.ui.javafx.impl;

import javax.rad.genui.UIColor;
import javax.rad.ui.IColor;
import javax.rad.ui.ICursor;
import javax.rad.ui.IFont;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link JavaFXStyleContainer} is an extension of the
 * {@link StyleContainer} that can handle JVx specific interfaces.
 * 
 * @author Robert Zenz
 */
public class JavaFXStyleContainer extends StyleContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXStyleContainer}.
	 */
	public JavaFXStyleContainer()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link JavaFXStyleContainer}.
	 *
	 * @param pParent the parent.
	 */
	public JavaFXStyleContainer(MenuItem pParent)
	{
		super(pParent);
	}

	/**
	 * Creates a new instance of {@link JavaFXStyleContainer}.
	 *
	 * @param pParent the parent.
	 */
	public JavaFXStyleContainer(Node pParent)
	{
		super(pParent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the background color property.
	 * 
	 * @param pColor the {@link IColor} to set.
	 */
	public void setBackground(IColor pColor)
	{
		if (pColor != null)
		{
			set("-fx-background-color", UIColor.toHex(pColor), pColor.getResource());
		}
		else
		{
			clear("-fx-background-color");
		}
	}
	
	/**
	 * Sets the background cursor property.
	 * 
	 * @param pCursor the {@link ICursor} to set.
	 */
	public void setCursor(ICursor pCursor)
	{
		if (pCursor != null)
		{
			if (pCursor instanceof JavaFXCursor)
			{
				set("-fx-cursor", ((JavaFXCursor)pCursor).getCSSName());
			}
			else
			{
				set("-fx-cursor", ((JavaFXCursor)pCursor.getResource()).getCSSName());
			}
		}
		else
		{
			clear("-fx-cursor");
		}
	}
	
	/**
	 * Sets the font property.
	 * 
	 * @param pFont the {@link IColor} to set.
	 */
	public void setFont(IFont pFont)
	{
		setAutomaticUpdatesEnabled(false);
		
		clear("-fx-font-family");
		clear("-fx-font-size");
		clear("-fx-font-weight");
		clear("-fx-font-style");
		
		if (pFont != null)
		{
			if (!StringUtil.isEmpty(pFont.getFamily()))
			{
				setQuoted("-fx-font-family", pFont.getFamily(), pFont.getResource());
			}
			if (pFont.getSize() > 0)
			{
				set("-fx-font-size", Integer.toString(pFont.getSize()) + "px ", pFont.getResource());
			}
			if ((pFont.getStyle() & IFont.BOLD) == IFont.BOLD)
			{
				set("-fx-font-weight", "bold", pFont.getResource());
			}
			if ((pFont.getStyle() & IFont.ITALIC) == IFont.ITALIC)
			{
				set("-fx-font-style", "italic", pFont.getResource());
			}
		}
		
		setAutomaticUpdatesEnabled(true);
		updateStyle();
	}
	
	/**
	 * Sets the inner background color property.
	 * 
	 * @param pColor the {@link IColor} to set.
	 */
	public void setInnerBackground(IColor pColor)
	{
		if (pColor != null)
		{
			set("-fx-control-inner-background", UIColor.toHex(pColor), pColor.getResource());
		}
		else
		{
			clear("-fx-control-inner-background");
		}
	}
	
	/**
	 * Sets the foreground color property.
	 * 
	 * @param pColor the {@link IColor} to set.
	 */
	public void setForeground(IColor pColor)
	{
		if (pColor != null)
		{
			set("-fx-text-fill", UIColor.toHex(pColor), pColor.getResource());
		}
		else
		{
			clear("-fx-text-fill");
		}
	}
	
}	// JavaFXStyleContainer

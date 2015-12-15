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
package com.sibvisions.rad.ui.javafx.impl;

import javax.rad.ui.IFont;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * The {@link JavaFXFont} is the JavaFX specific implementation of {@link IFont}
 * .
 * 
 * @author Robert Zenz
 * @see IFont
 * @see Font
 */
public class JavaFXFont extends JavaFXResource<Font> implements IFont
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXFont}.
	 *
	 * @param pFont the font.
	 */
	public JavaFXFont(Font pFont)
	{
		super(pFont);
	}
	
	/**
	 * Creates a new instance of {@link JavaFXFont}.
	 *
	 * @param pFontName the font name.
	 * @param pStyle the style.
	 * @param pSize the size.
	 */
	public JavaFXFont(String pFontName, int pStyle, int pSize)
	{
		super(Font.font(pFontName,
				(pStyle & IFont.BOLD) == IFont.BOLD ? FontWeight.BOLD : FontWeight.NORMAL,
				(pStyle & IFont.ITALIC) == IFont.ITALIC ? FontPosture.ITALIC : FontPosture.REGULAR,
				pSize));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFamily()
	{
		return resource.getFamily();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFontName()
	{
		return resource.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return resource.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize()
	{
		return (int)resource.getSize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getStyle()
	{
		String sStyle = resource.getStyle();
		
		int iStyle = IFont.PLAIN;
		
		if (sStyle != null)
		{
			sStyle = sStyle.toLowerCase();
			
			if (sStyle.indexOf("bold") >= 0)
			{
				iStyle |= IFont.BOLD;
			}
			
			if (sStyle.indexOf("italic") >= 0)
			{
				iStyle |= IFont.ITALIC;
			}
		}
		
		return iStyle;
	}
	
}	// JavaFXFont

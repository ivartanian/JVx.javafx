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
package com.sibvisions.rad.ui.javafx.impl.control;

import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.control.ICellFormat;

import com.sibvisions.rad.ui.javafx.impl.JavaFXResource;

/**
 * The {@link JavaFXCellFormat} is the JavaFX specific implementation of
 * {@link ICellFormat}.
 * 
 * @author Robert Zenz
 * @see ICellFormat
 */
public class JavaFXCellFormat extends JavaFXResource<Object> implements ICellFormat
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The background {@link IColor}. */
	private IColor background;
	
	/** The {@link IFont}. */
	private IFont font;
	
	/** The foreground {@link IColor}. */
	private IColor foreground;
	
	/** The {@link IImage}. */
	private IImage image;
	
	/** The left indent. */
	private int leftIndent;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXCellFormat}.
	 *
	 * @param pBackground the background {@link IColor}.
	 * @param pForeground the foreground {@link IColor}.
	 * @param pFont the {@link IFont}.
	 * @param pImage the {@link IImage}.
	 * @param pLeftIndent the left indent.
	 */
	public JavaFXCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, int pLeftIndent)
	{
		super(null);
		
		background = pBackground;
		foreground = pForeground;
		font = pFont;
		image = pImage;
		leftIndent = pLeftIndent;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IColor getBackground()
	{
		return background;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFont getFont()
	{
		return font;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IColor getForeground()
	{
		return foreground;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getImage()
	{
		return image;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLeftIndent()
	{
		return leftIndent;
	}
	
}	// JavaFXCellFormat

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

import javax.rad.ui.IColor;

import javafx.scene.paint.Color;

/**
 * The {@link JavaFXColor} is the JavaFX specific implementation of
 * {@link IColor}.
 * 
 * @author Robert Zenz
 * @see IColor
 * @see JavaFXColor
 */
public class JavaFXColor extends JavaFXResource<Color> implements IColor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The alpha component of the color. */
	private int alpha;
	
	/** The red component of the color. */
	private int red;
	
	/** The green component of the color. */
	private int green;
	
	/** The blue component of the color. */
	private int blue;
	
	/** The rgba value of the color. */
	private int rgba;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXColor}.
	 *
	 * @param pColor the color.
	 */
	public JavaFXColor(Color pColor)
	{
		super(pColor);
		
		red = (int)Math.round(resource.getRed() * 255);
		green = (int)Math.round(resource.getGreen() * 255);
		blue = (int)Math.round(resource.getBlue() * 255);
		alpha = (int)Math.round(resource.getOpacity() * 255);
		
		rgba = alpha << 24 | red << 16 | green << 8 | blue;
	}
	
	/**
	 * Creates a new instance of {@link JavaFXColor}.
	 *
	 * @param pRGBA the rgba.
	 */
	public JavaFXColor(int pRGBA)
	{
		super(new Color(((pRGBA >> 16) & 0xFF) / 255f, ((pRGBA >> 8) & 0xFF) / 255f, (pRGBA & 0xFF) / 255f, (pRGBA >> 24 & 0xFF) / 255f));
		
		rgba = pRGBA;
		
		alpha = rgba >> 24 & 0xFF;
		red = (rgba >> 16) & 0xFF;
		green = (rgba >> 8) & 0xFF;
		blue = rgba & 0xFF;
	}
	
	/**
	 * Creates a new instance of {@link JavaFXColor}.
	 *
	 * @param pRed the red part.
	 * @param pGreen the green part.
	 * @param pBlue the blue part.
	 * @param pAlpha the alpha part.
	 */
	public JavaFXColor(int pRed, int pGreen, int pBlue, int pAlpha)
	{
		super(new Color(pRed / 255f, pGreen / 255f, pBlue / 255f, pAlpha / 255f));
		
		red = pRed;
		green = pGreen;
		blue = pBlue;
		alpha = pAlpha;
		
		rgba = alpha << 24 | red << 16 | green << 8 | blue;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAlpha()
	{
		return alpha;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBlue()
	{
		return blue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGreen()
	{
		return green;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRed()
	{
		return red;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRGBA()
	{
		return rgba;
	}
	
}	// JavaFXColor

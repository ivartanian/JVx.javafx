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

import javax.rad.ui.IRectangle;

import javafx.geometry.Rectangle2D;

/**
 * The {@link JavaFXRectangle} is the JavaFX specific implementation of
 * {@link IRectangle}.
 * 
 * @author Robert Zenz
 * @see IRectangle
 * @see Rectangle2D
 */
public class JavaFXRectangle extends JavaFXResource<Rectangle2D> implements IRectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXRectangle}.
	 *
	 * @param pRectangle the rectangle.
	 */
	public JavaFXRectangle(Rectangle2D pRectangle)
	{
		super(pRectangle);
	}
	
	/**
	 * Creates a new instance of {@link JavaFXRectangle}.
	 *
	 * @param pLayoutX the layout x.
	 * @param pLayoutY the layout y.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	public JavaFXRectangle(double pLayoutX, double pLayoutY, double pWidth, double pHeight)
	{
		super(new Rectangle2D(pLayoutX, pLayoutY, pWidth, pHeight));
	}
	
	/**
	 * Creates a new instance of {@link JavaFXRectangle}.
	 *
	 * @param pX the x.
	 * @param pY the y.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	public JavaFXRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		super(new Rectangle2D(pX, pY, pWidth, pHeight));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight()
	{
		return (int)resource.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth()
	{
		return (int)resource.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getX()
	{
		return (int)resource.getMinX();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getY()
	{
		return (int)resource.getMinY();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeight(int pHeight)
	{
		setResource(new Rectangle2D(resource.getMinX(), resource.getMinY(), resource.getWidth(), pHeight));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidth(int pWidth)
	{
		setResource(new Rectangle2D(resource.getMinX(), resource.getMinY(), pWidth, resource.getHeight()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setX(int pX)
	{
		setResource(new Rectangle2D(pX, resource.getMinY(), resource.getWidth(), resource.getHeight()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setY(int pY)
	{
		setResource(new Rectangle2D(resource.getMinX(), pY, resource.getWidth(), resource.getHeight()));
	}
	
} 	// JavaFXRectangle

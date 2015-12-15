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

import javax.rad.ui.IPoint;

import javafx.geometry.Point2D;

/**
 * The {@link JavaFXPoint} is the JavaFX specific implementation of
 * {@link IPoint}.
 * 
 * @author Robert Zenz
 * @see IPoint
 * @see Point2D
 */
public class JavaFXPoint extends JavaFXResource<Point2D> implements IPoint
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXPoint}.
	 * 
	 * @param pPoint the point.
	 */
	public JavaFXPoint(Point2D pPoint)
	{
		super(pPoint);
	}
	
	/**
	 * Creates a new instance of {@link JavaFXPoint}.
	 *
	 * @param pX the x.
	 * @param pY the y.
	 */
	public JavaFXPoint(double pX, double pY)
	{
		super(new Point2D(pX, pY));
	}
	
	/**
	 * Creates a new instance of {@link JavaFXPoint}.
	 *
	 * @param pX the x.
	 * @param pY the y.
	 */
	public JavaFXPoint(int pX, int pY)
	{
		super(new Point2D(pX, pY));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getX()
	{
		return (int)resource.getX();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getY()
	{
		return (int)resource.getY();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setX(int pX)
	{
		setResource(new Point2D(pX, resource.getY()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setY(int pY)
	{
		setResource(new Point2D(resource.getX(), pY));
	}
	
}	// JavaFXPoint

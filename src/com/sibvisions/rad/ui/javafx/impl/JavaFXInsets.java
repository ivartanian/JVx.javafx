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

import javax.rad.ui.IInsets;

import javafx.geometry.Insets;

/**
 * The {@link JavaFXInsets} is the JavaFX specific implementation of
 * {@link IInsets}.
 * 
 * @author Robert Zenz
 * @see IInsets
 * @see Insets
 */
public class JavaFXInsets extends JavaFXResource<Insets> implements IInsets
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXInsets}.
	 *
	 * @param pInsets the insets.
	 */
	public JavaFXInsets(Insets pInsets)
	{
		super(pInsets);
	}
	
	/**
	 * Creates a new instance of {@link JavaFXInsets}.
	 *
	 * @param pTop the top.
	 * @param pLeft the left.
	 * @param pBottom the bottom.
	 * @param pRight the right.
	 */
	public JavaFXInsets(double pTop, double pLeft, double pBottom, double pRight)
	{
		super(new Insets(pTop, pRight, pBottom, pLeft));
	}
	
	/**
	 * Creates a new instance of {@link JavaFXInsets}.
	 *
	 * @param pTop the top.
	 * @param pLeft the left.
	 * @param pBottom the bottom.
	 * @param pRight the right.
	 */
	public JavaFXInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
		super(new Insets(pTop, pRight, pBottom, pLeft));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBottom()
	{
		return (int)resource.getBottom();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLeft()
	{
		return (int)resource.getLeft();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRight()
	{
		return (int)resource.getRight();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTop()
	{
		return (int)resource.getTop();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBottom(int pBottom)
	{
		setResource(new Insets(resource.getTop(), resource.getRight(), pBottom, resource.getLeft()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLeft(int pLeft)
	{
		setResource(new Insets(resource.getTop(), resource.getRight(), resource.getBottom(), pLeft));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRight(int pRight)
	{
		setResource(new Insets(resource.getTop(), pRight, resource.getBottom(), resource.getLeft()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTop(int pTop)
	{
		setResource(new Insets(pTop, resource.getRight(), resource.getBottom(), resource.getLeft()));
	}
	
}	// JavaFXInsets

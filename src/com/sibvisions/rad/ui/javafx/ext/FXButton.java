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
package com.sibvisions.rad.ui.javafx.ext;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;

import com.sibvisions.rad.ui.javafx.ext.util.LayoutUtil;

/**
 * A {@link Button} extension that allows to set the text alignment relative to
 * the displayed graphics node, if any.
 * 
 * @author Robert Zenz
 */
public class FXButton extends Button implements IFXContentAlignable, IFXLayoutForwarding
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property for the horizontal alignment. */
	private ObjectProperty<HPos> relativeHorizontalTextAlignment;
	
	/** The property for the vertical alignment. */
	private ObjectProperty<VPos> relativeVerticalTextAlignment;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXButton}.
	 */
	public FXButton()
	{
		this("");
	}
	
	/**
	 * Creates a new instance of {@link FXButton}.
	 *
	 * @param pText the text.
	 */
	public FXButton(String pText)
	{
		this(pText, null);
	}
	
	/**
	 * Creates a new instance of {@link FXButton}.
	 *
	 * @param pText the text.
	 * @param pGraphic the graphic node.
	 */
	public FXButton(String pText, Node pGraphic)
	{
		super(pText, pGraphic);
		
		relativeHorizontalTextAlignment = new SimpleObjectProperty<>(HPos.CENTER);
		relativeHorizontalTextAlignment.addListener(pObservable -> requestLayout());
		
		relativeVerticalTextAlignment = new SimpleObjectProperty<>(VPos.CENTER);
		relativeVerticalTextAlignment.addListener(pObservable -> requestLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double calculatePreferredHeight(double pWidth)
	{
		return super.computePrefHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double calculatePreferredWidth(double pHeight)
	{
		return super.computePrefWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doLayout()
	{
		super.layoutChildren();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HPos getRelativeHorizontalTextAlignment()
	{
		return relativeHorizontalTextAlignment.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VPos getRelativeVerticalTextAlignment()
	{
		return relativeVerticalTextAlignment.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObjectProperty<HPos> relativeHorizontalTextAlignment()
	{
		return relativeHorizontalTextAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObjectProperty<VPos> relativeVerticalTextAlignment()
	{
		return relativeVerticalTextAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelativeHorizontalTextAlignment(HPos pRelativeHorizontalTextAlignment)
	{
		relativeHorizontalTextAlignment.set(pRelativeHorizontalTextAlignment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelativeVerticalTextAlignment(VPos pRelativeVerticalTextAlignment)
	{
		relativeVerticalTextAlignment.set(pRelativeVerticalTextAlignment);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		LayoutUtil.layoutContent(this, this, this, null);
	}
	
}	// FXButton

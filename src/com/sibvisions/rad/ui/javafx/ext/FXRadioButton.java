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
import javafx.scene.control.RadioButton;

import com.sibvisions.rad.ui.javafx.ext.util.LayoutUtil;

/**
 * An {@link RadioButton} extension that treats the radiobutton as content if
 * there is no graphic node set, and also allows to align the text relative to
 * the content.
 * <p>
 * This is a rather shallow implementation which mostly serves as a container
 * for the properties. The important stuff is happening in {@link LayoutUtil}.
 * 
 * @author Robert Zenz
 */
public class FXRadioButton extends RadioButton implements IFXContentAlignable, IFXLayoutForwarding
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
	 * Creates a new instance of {@link FXRadioButton}.
	 */
	public FXRadioButton()
	{
		this("");
	}
	
	/**
	 * Creates a new instance of {@link FXRadioButton}.
	 *
	 * @param pText the text.
	 */
	public FXRadioButton(String pText)
	{
		this(pText, null);
	}
	
	/**
	 * Creates a new instance of {@link FXRadioButton}.
	 *
	 * @param pText the text.
	 * @param pGraphicNode the graphic node.
	 */
	public FXRadioButton(String pText, Node pGraphicNode)
	{
		super(pText);
		
		setGraphic(pGraphicNode);
		
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
	protected double computePrefHeight(double pWidth)
	{
		return LayoutUtil.computeContentPreferredHeight(this, this, this, "radio");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight)
	{
		return LayoutUtil.computeContentPreferredWidth(this, this, this, "radio");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		LayoutUtil.layoutContent(this, this, this, "radio");
	}
	
}	// FXRadioButton

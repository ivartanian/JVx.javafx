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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.control.Control;

/**
 * The {@link FXAbstractZoomableControl} provides base properties for a zoomable
 * {@link Control}.
 * 
 * @author Robert Zenz
 */
public abstract class FXAbstractZoomableControl extends Control implements IFXZoomable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link FXZoomableHelper} that is used. */
	protected FXZoomableHelper zoomHelper;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXAbstractZoomableControl}.
	 *
	 * @param pMinZoomValue the minimum zoom value.
	 * @param pMaxZoomValue the maximum zoom value.
	 */
	protected FXAbstractZoomableControl(double pMinZoomValue, double pMaxZoomValue)
	{
		super();
		
		zoomHelper = new FXZoomableHelper(pMinZoomValue, pMaxZoomValue, this, this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZoom()
	{
		return zoomHelper.getZoom();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZoomFactor()
	{
		return zoomHelper.getZoomFactor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomEnabled()
	{
		return zoomHelper.isZoomEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomInPossible()
	{
		return zoomHelper.isZoomInPossible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomOutPossible()
	{
		return zoomHelper.isZoomOutPossible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomResetPossible()
	{
		return zoomHelper.isZoomResetPossible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoom(double pZoom)
	{
		zoomHelper.setZoom(pZoom);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomEnabled(boolean pZoomEnabled)
	{
		zoomHelper.setZoomEnabled(pZoomEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomFactor(double pZoomFactor)
	{
		zoomHelper.setZoomFactor(pZoomFactor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoom(int pSteps)
	{
		zoomHelper.zoom(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BooleanProperty zoomEnabledProperty()
	{
		return zoomHelper.zoomEnabledProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleProperty zoomFactorProperty()
	{
		return zoomHelper.zoomFactorProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn()
	{
		zoomHelper.zoomIn();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn(int pSteps)
	{
		zoomHelper.zoomIn(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomInPossibleProperty()
	{
		return zoomHelper.zoomInPossibleProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut()
	{
		zoomHelper.zoomOut();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut(int pSteps)
	{
		zoomHelper.zoomOut(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomOutPossibleProperty()
	{
		return zoomHelper.zoomOutPossibleProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleProperty zoomProperty()
	{
		return zoomHelper.zoomProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomReset()
	{
		zoomHelper.zoomReset();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomResetPossibleProperty()
	{
		return zoomHelper.zoomResetPossibleProperty();
	}
	
}	// FXAbstractZoomableControl

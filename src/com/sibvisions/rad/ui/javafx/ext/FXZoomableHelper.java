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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.beans.property.LimitedDoubleProperty;

/**
 * The {@link FXZoomableHelper} provides base properties for zoom.
 * 
 * @author Robert Zenz
 */
public class FXZoomableHelper implements IFXZoomable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default value if zoom should be enabled. */
	private static boolean defaultZoomEnabled;
	
	/** The {@link Node} which is used for dispatching events. */
	private Node eventNode;
	
	/** The {@link IFXZoomable} that serves as event source. */
	private IFXZoomable eventSource;
	
	/** The property that holds the zoom value. */
	protected LimitedDoubleProperty zoom;
	
	/** The property for if zooming is enabled. */
	protected BooleanProperty zoomEnabled;
	
	/** The factor by which each step changes the zoom. */
	protected DoubleProperty zoomFactor;
	
	/** The property used for if zooming in is possible. */
	private BooleanProperty zoomInPossible;
	
	/** The property used for if zooming out is possible. */
	private BooleanProperty zoomOutPossible;
	
	/** The property used for if resetting the zoom is possible. */
	private BooleanProperty zoomResetPossible;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		defaultZoomEnabled = true;
	}
	
	/**
	 * Creates a new instance of {@link FXZoomableHelper}.
	 *
	 * @param pMinZoomValue the minimum zoom value.
	 * @param pMaxZoomValue the maximum zoom value.
	 */
	public FXZoomableHelper(double pMinZoomValue, double pMaxZoomValue)
	{
		super();
		
		zoom = new LimitedDoubleProperty(1, pMinZoomValue, pMaxZoomValue);
		zoom.addListener(this::onZoomChanged);
		
		zoomEnabled = new SimpleBooleanProperty(defaultZoomEnabled);
		
		zoomFactor = new SimpleDoubleProperty(0.1);
		
		zoomInPossible = new SimpleBooleanProperty(zoom.get() < zoom.getMaxValue());
		zoomOutPossible = new SimpleBooleanProperty(zoom.get() > zoom.getMinValue());
		zoomResetPossible = new SimpleBooleanProperty(zoom.get() != 1.0);
	}
	
	/**
	 * Creates a new instance of {@link FXZoomableHelper}.
	 *
	 * @param pMinZoomValue the minimum zoom value.
	 * @param pMaxZoomValue the maximum zoom value.
	 * @param pEventNode the {@link Node} used for dispatching events.
	 * @param pEventSource the {@link IFXZoomable} to use as source for events.
	 *            Can be {@code null} to use this.
	 */
	public FXZoomableHelper(double pMinZoomValue, double pMaxZoomValue, Node pEventNode, IFXZoomable pEventSource)
	{
		this(pMinZoomValue, pMaxZoomValue);
		
		eventNode = pEventNode;
		eventSource = pEventSource;
		
		if (eventSource == null)
		{
			eventSource = this;
		}
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
		return zoom.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZoomFactor()
	{
		return zoomFactor.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomEnabled()
	{
		return zoomEnabled.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomInPossible()
	{
		return zoomInPossible.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomOutPossible()
	{
		return zoomOutPossible.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomResetPossible()
	{
		return zoom.get() != 1.0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoom(double pZoom)
	{
		zoom.set(pZoom);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomEnabled(boolean pZoomEnabled)
	{
		zoomEnabled.set(pZoomEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomFactor(double pZoomFactor)
	{
		zoomFactor.set(pZoomFactor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoom(int pSteps)
	{
		double toAdd = pSteps * zoomFactor.get();
		double newZoomValue = zoom.get() + toAdd;
		newZoomValue = Math.max(newZoomValue, 0);
		
		zoom.set(newZoomValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BooleanProperty zoomEnabledProperty()
	{
		return zoomEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleProperty zoomFactorProperty()
	{
		return zoomFactor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn()
	{
		zoomIn(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn(int pSteps)
	{
		zoom(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomInPossibleProperty()
	{
		return ReadOnlyBooleanProperty.readOnlyBooleanProperty(zoomInPossible);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut()
	{
		zoomOut(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut(int pSteps)
	{
		zoom(-pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomOutPossibleProperty()
	{
		return ReadOnlyBooleanProperty.readOnlyBooleanProperty(zoomOutPossible);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleProperty zoomProperty()
	{
		return zoom;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomReset()
	{
		zoom.set(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomResetPossibleProperty()
	{
		return ReadOnlyBooleanProperty.readOnlyBooleanProperty(zoomResetPossible);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets if the zoom is by default enabled.
	 * 
	 * @return {@code true} if the zoom is by default enabled.
	 * @see #zoomEnabledProperty()
	 */
	public static boolean isDefaultZoomEnabled()
	{
		return defaultZoomEnabled;
	}
	
	/**
	 * Sets if the zoom feature is by default enabled or disabled. Settings this
	 * will only change the default values of newly created
	 * {@link FXZoomableHelper}s. and not change already existing instances.
	 * 
	 * @param pEnabled {@code true} if the zoom should be enabled by default.
	 * @see #zoomEnabledProperty()
	 */
	public static void setDefaultZoomEnabled(boolean pEnabled)
	{
		defaultZoomEnabled = pEnabled;
	}
	
	/**
	 * Invoked if the value of the {@link #zoom} property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onZoomChanged(ObservableValue<? extends Number> pObservable, Number pOldValue, Number pNewValue)
	{
		zoomInPossible.set(pNewValue.doubleValue() < zoom.getMaxValue());
		zoomOutPossible.set(pNewValue.doubleValue() > zoom.getMinValue());
		zoomResetPossible.set(pNewValue.doubleValue() != 1.0);
		
		if (eventNode != null)
		{
			eventNode.fireEvent(new ZoomChangedEvent(eventSource, pOldValue.doubleValue(), pNewValue.doubleValue()));
		}
	}
	
}	// FXZoomableHelper

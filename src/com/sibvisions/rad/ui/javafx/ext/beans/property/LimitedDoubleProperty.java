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
package com.sibvisions.rad.ui.javafx.ext.beans.property;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * The {@link LimitedDoubleProperty} is a {@link SimpleDoubleProperty} extension
 * that allows to clamp the value between a {@link #getMinValue() min value} and
 * {@link #getMaxValue() max value}.
 * 
 * @author Robert Zenz
 */
public class LimitedDoubleProperty extends SimpleDoubleProperty
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The minimum value. */
	private double maxValue;
	
	/** The maximum value. */
	private double minValue;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link LimitedDoubleProperty}.
	 *
	 * @param pMinValue the minimum value.
	 * @param pMaxValue the maximum value.
	 */
	public LimitedDoubleProperty(double pMinValue, double pMaxValue)
	{
		this(pMinValue, pMinValue, pMaxValue);
	}
	
	/**
	 * Creates a new instance of {@link LimitedDoubleProperty}.
	 *
	 * @param pInitialValue the initial value.
	 * @param pMinValue the minimum value.
	 * @param pMaxValue the maximum value.
	 */
	public LimitedDoubleProperty(double pInitialValue, double pMinValue, double pMaxValue)
	{
		super(pInitialValue);
		
		minValue = pMinValue;
		maxValue = pMaxValue;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the.
	 *
	 * @param pNewValue the new value.
	 */
	@Override
	public void set(double pNewValue)
	{
		super.set(Math.max(minValue, Math.min(maxValue, pNewValue)));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link double maximum value}.
	 *
	 * @return the {@link double maximum value}.
	 */
	public double getMaxValue()
	{
		return maxValue;
	}
	
	/**
	 * Gets the {@link double minimum value}.
	 *
	 * @return the {@link double minimum value}.
	 */
	public double getMinValue()
	{
		return minValue;
	}
	
}	// LimitedDoubleProperty

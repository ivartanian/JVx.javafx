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

import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import com.sibvisions.rad.ui.javafx.ext.skin.FXSpinnerSkinRT40623;

/**
 * The {@link FXSpinnerRT40623} is a {@link Spinner} extensions that allows to
 * use the up/down keys to increment/decrement the values.
 * 
 * @author Robert Zenz
 * @param <T> the type of the items.
 */
public class FXSpinnerRT40623<T> extends Spinner<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 */
	public FXSpinnerRT40623()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 *
	 * @param pMin the min.
	 * @param pMax the max.
	 * @param pInitialValue the initial value.
	 * @param pAmountToStepBy the amount to step by.
	 */
	public FXSpinnerRT40623(double pMin, double pMax, double pInitialValue, double pAmountToStepBy)
	{
		super(pMin, pMax, pInitialValue, pAmountToStepBy);
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 *
	 * @param pMin the min.
	 * @param pMax the max.
	 * @param pInitialValue the initial value.
	 */
	public FXSpinnerRT40623(double pMin, double pMax, double pInitialValue)
	{
		super(pMin, pMax, pInitialValue);
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 *
	 * @param pMin the min.
	 * @param pMax the max.
	 * @param pInitialValue the initial value.
	 * @param pAmountToStepBy the amount to step by.
	 */
	public FXSpinnerRT40623(int pMin, int pMax, int pInitialValue, int pAmountToStepBy)
	{
		super(pMin, pMax, pInitialValue, pAmountToStepBy);
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 *
	 * @param pMin the min.
	 * @param pMax the max.
	 * @param pInitialValue the initial value.
	 */
	public FXSpinnerRT40623(int pMin, int pMax, int pInitialValue)
	{
		super(pMin, pMax, pInitialValue);
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 *
	 * @param pItems the items.
	 */
	public FXSpinnerRT40623(ObservableList<T> pItems)
	{
		super(pItems);
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerRT40623}.
	 *
	 * @param pValueFactory the value factory.
	 */
	public FXSpinnerRT40623(SpinnerValueFactory<T> pValueFactory)
	{
		super(pValueFactory);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new FXSpinnerSkinRT40623<>(this);
	}
	
}	// FXSpinnerRT40623

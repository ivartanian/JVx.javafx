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
package com.sibvisions.rad.ui.javafx.ext.skin;

import java.time.LocalTime;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

import com.sibvisions.rad.ui.javafx.ext.FXSpinnerRT40623;
import com.sibvisions.rad.ui.javafx.ext.FXTimePicker;
import com.sibvisions.rad.ui.javafx.ext.behavior.FXTimePickerBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * The {@link FXTimePickerSkin} is the default skin for the {@link FXTimePicker}
 * .
 * 
 * @author Robert Zenz
 */
public class FXTimePickerSkin extends BehaviorSkinBase<FXTimePicker, FXTimePickerBehavior>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Spinner} used for selection the hour. */
	private Spinner<Integer> hourSpinner;
	
	/** The {@link Spinner} used for selection the minute. */
	private Spinner<Integer> minuteSpinner;
	
	/** The {@link Spinner} used for selection the second. */
	private Spinner<Integer> secondSpinner;
	
	/** The listener for when the value changes. */
	private ChangeListener<LocalTime> valueChangedListener;
	
	/** If events of the spinners or the control should be ignored. */
	private boolean ignoreEvents;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTimePickerSkin}.
	 *
	 * @param pControl the control.
	 */
	public FXTimePickerSkin(FXTimePicker pControl)
	{
		this(pControl, new FXTimePickerBehavior(pControl));
	}
	
	/**
	 * Creates a new instance of {@link FXTimePickerSkin}.
	 *
	 * @param pControl the control.
	 * @param pBehavior the behavior.
	 */
	public FXTimePickerSkin(FXTimePicker pControl, FXTimePickerBehavior pBehavior)
	{
		super(pControl, pBehavior);
		
		valueChangedListener = this::onValueChanged;
		
		hourSpinner = createSpinner("hour-selector", 0, 23);
		minuteSpinner = createSpinner("minute-selector", 0, 59);
		secondSpinner = createSpinner("second-selector", 0, 59);
		
		GridPane gridPane = new GridPane();
		gridPane.getStyleClass().add("content");
		gridPane.add(hourSpinner, 0, 0);
		gridPane.add(new Label(":"), 1, 0);
		gridPane.add(minuteSpinner, 2, 0);
		gridPane.add(new Label(":"), 3, 0);
		gridPane.add(secondSpinner, 4, 0);
		
		getChildren().add(gridPane);
		
		getSkinnable().valueProperty().addListener(valueChangedListener);
		
		onValueChanged(null, null, getSkinnable().getValue());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Dispose.
	 */
	@Override
	public void dispose()
	{
		getSkinnable().valueProperty().removeListener(valueChangedListener);
		
		super.dispose();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a default {@link Spinner}.
	 * 
	 * @param pStyleClassName the class name used for styling.
	 * @param pMinValue the minimum value.
	 * @param pMaxValue the maximum value.
	 * @return the {@link Spinner}.
	 */
	protected Spinner<Integer> createSpinner(String pStyleClassName, int pMinValue, int pMaxValue)
	{
		Spinner<Integer> spinner = new FXSpinnerRT40623<>(pMinValue, pMaxValue, pMinValue);
		spinner.getEditor().setAlignment(Pos.CENTER_RIGHT);
		spinner.getStyleClass().add(pStyleClassName);
		spinner.getStylesheets().add(FXTimePicker.DEFAULT_STYLE);
		spinner.getValueFactory().setWrapAround(true);
		spinner.setEditable(true);
		spinner.valueProperty().addListener(this::onSpinnerValueChanged);
		
		return spinner;
	}
	
	/**
	 * Invoked if the value of a {@link Spinner} changes.
	 * 
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSpinnerValueChanged(ObservableValue<? extends Integer> pObservableValue, Integer pOldValue, Integer pNewValue)
	{
		if (!ignoreEvents)
		{
			ignoreEvents = true;
			
			LocalTime value = LocalTime.of(hourSpinner.getValue().intValue(), minuteSpinner.getValue().intValue(), secondSpinner.getValue().intValue());
			getSkinnable().setValue(value);
			
			ignoreEvents = false;
		}
	}
	
	/**
	 * Invoked if the value of the control changes.
	 * 
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onValueChanged(ObservableValue<? extends LocalTime> pObservableValue, LocalTime pOldValue, LocalTime pNewValue)
	{
		if (!ignoreEvents)
		{
			ignoreEvents = true;
			
			if (pNewValue != null)
			{
				hourSpinner.getValueFactory().setValue(Integer.valueOf(pNewValue.getHour()));
				minuteSpinner.getValueFactory().setValue(Integer.valueOf(pNewValue.getMinute()));
				secondSpinner.getValueFactory().setValue(Integer.valueOf(pNewValue.getSecond()));
			}
			else
			{
				hourSpinner.getValueFactory().setValue(Integer.valueOf(0));
				minuteSpinner.getValueFactory().setValue(Integer.valueOf(0));
				secondSpinner.getValueFactory().setValue(Integer.valueOf(0));
			}
			
			ignoreEvents = false;
		}
	}
	
}	// FXTimePickerSkin

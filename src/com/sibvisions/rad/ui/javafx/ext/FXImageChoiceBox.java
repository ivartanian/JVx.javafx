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

import java.util.function.Supplier;

import javax.rad.genui.UIImage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import com.sibvisions.util.ArrayUtil;

/**
 * The {@link FXImageChoiceBox} is basically a multi-state checkbox, with each
 * state represented by an image and an object/value.
 * 
 * @param <T> the type of the value.
 * @author Robert Zenz
 */
public class FXImageChoiceBox<T> extends FXImageRegion
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The getter that returns the allowed values. */
	private Supplier<T[]> allowedValuesGetter;
	
	/** The getter that returns the name of the default image. */
	private Supplier<String> defaultImageNameGetter;
	
	/** The getter that returns the image names. */
	private Supplier<String[]> imageNamesGetter;
	
	/** The currently selected object/value. */
	private ObjectProperty<T> value;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXImageChoiceBox}.
	 *
	 * @param pAllowedValuesGetter the allowed values getter.
	 * @param pImageNamesGetter the image names getter.
	 * @param pDefaultImageNameGetter the default image name getter.
	 */
	public FXImageChoiceBox(Supplier<T[]> pAllowedValuesGetter, Supplier<String[]> pImageNamesGetter, Supplier<String> pDefaultImageNameGetter)
	{
		super();
		
		setCursor(Cursor.HAND);
		setFocusTraversable(true);
		setOnKeyPressed(this::onKeyPressed);
		setOnMouseClicked(this::onMouseClicked);
		
		value = new SimpleObjectProperty<>();
		value.addListener(this::onValueChanged);
		
		allowedValuesGetter = pAllowedValuesGetter;
		imageNamesGetter = pImageNamesGetter;
		defaultImageNameGetter = pDefaultImageNameGetter;
		
		updateDisplay();
	}
	
	/**
	 * Creates a new instance of {@link FXImageChoiceBox}.
	 *
	 * @param pAllowedValues the allowed values.
	 * @param pImageNames the image names.
	 * @param pDefaultImageName the default image name.
	 */
	public FXImageChoiceBox(T[] pAllowedValues, String[] pImageNames, String pDefaultImageName)
	{
		this(() -> pAllowedValues, () -> pImageNames, () -> pDefaultImageName);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the currently selected value.
	 *
	 * @return the currently selected value.
	 */
	public Object getValue()
	{
		return value.get();
	}
	
	/**
	 * Forwards the selection by one/selects the next value.
	 */
	public void selectNextValue()
	{
		T[] allowedValues = allowedValuesGetter.get();
		
		int index = ArrayUtil.indexOf(allowedValues, value.get());
		index = index + 1;
		index = index % allowedValues.length;
		
		value.set(allowedValues[index]);
	}
	
	/**
	 * Sets the currently selected value.
	 *
	 * @param pValue the new value.
	 */
	public void setValue(T pValue)
	{
		value.set(pValue);
	}
	
	/**
	 * The value property that holds the currently selected value.
	 * 
	 * @return the value property.
	 */
	public ObjectProperty<T> valueProperty()
	{
		return value;
	}
	
	/**
	 * Event handler for the key pressed event, which does forward the selection
	 * by one.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyPressed(KeyEvent pKeyEvent)
	{
		if (!isDisabled() && pKeyEvent.getCode() == KeyCode.SPACE)
		{
			selectNextValue();
			
			pKeyEvent.consume();
		}
	}
	
	/**
	 * Event handler for the mouse clicked event, which does forward the
	 * selection by one.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onMouseClicked(MouseEvent pMouseEvent)
	{
		if (!isDisabled())
		{
			requestFocus();
			
			selectNextValue();
			
			pMouseEvent.consume();
		}
	}
	
	/**
	 * Event handler for if the value changed.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onValueChanged(ObservableValue<? extends Object> pObservable, Object pOldValue, Object pNewValue)
	{
		updateDisplay();
	}
	
	/**
	 * Updates the display, meaning it selects the fitting image to the current
	 * value and shows that.
	 */
	private void updateDisplay()
	{
		setImage(null);
		
		if (value != null)
		{
			int index = ArrayUtil.indexOf(allowedValuesGetter.get(), value.get());
			
			if (index >= 0)
			{
				setImage((Image)UIImage.getImage(imageNamesGetter.get()[index]).getResource());
			}
		}
		
		if (getImage() == null)
		{
			setImage((Image)UIImage.getImage(defaultImageNameGetter.get()).getResource());
		}
	}
	
}	// FXImageChoiceBox

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

import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import com.sibvisions.rad.ui.javafx.ext.skin.FXDateTimePickerSkin;

/**
 * The {@link FXDateTimePicker} allows to select a date and time.
 * 
 * @author Robert Zenz
 */
public class FXDateTimePicker extends Control
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/css/fxdatetimepicker.css";
	
	/** The default class name used for styling. */
	public static final String DEFAULT_STYLE_CLASS = "datetime-picker";
	
	/** The property for if the time picker should be visible or not. */
	private BooleanProperty timePickerVisible;
	
	/** The property used for the value. */
	private ObjectProperty<LocalDateTime> value;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDateTimePicker}.
	 */
	public FXDateTimePicker()
	{
		super();
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		timePickerVisible = new SimpleBooleanProperty(true);
		
		value = new SimpleObjectProperty<>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserAgentStylesheet()
	{
		return DEFAULT_STYLE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new FXDateTimePickerSkin(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the value.
	 * 
	 * @return the value.
	 */
	public LocalDateTime getValue()
	{
		return value.get();
	}
	
	/**
	 * Gets if the time picker part should be visible.
	 * 
	 * @return {@code true} if the time picker part should be visible.
	 */
	public boolean isTimePickerVisible()
	{
		return timePickerVisible.get();
	}
	
	/**
	 * Sets if the time picker part should be visible.
	 * 
	 * @param pTimePickerVisible {@code true} if the time picker part should be
	 *            visible.
	 */
	public void setTimePickerVisible(boolean pTimePickerVisible)
	{
		timePickerVisible.set(pTimePickerVisible);
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param pValue the value.
	 */
	public void setValue(LocalDateTime pValue)
	{
		value.set(pValue);
	}
	
	/**
	 * Gets the property for if the time picker part should be visible.
	 * 
	 * @return the property for if the time picker part should be visible.
	 */
	public BooleanProperty timePickerVisibleProperty()
	{
		return timePickerVisible;
	}
	
	/**
	 * Gets the property for the value.
	 * 
	 * @return the property for the value.
	 */
	public ObjectProperty<LocalDateTime> valueProperty()
	{
		return value;
	}
	
}	// FXDateTimePicker

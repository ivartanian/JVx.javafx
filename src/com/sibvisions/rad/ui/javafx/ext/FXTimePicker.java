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

import java.time.LocalTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import com.sibvisions.rad.ui.javafx.ext.skin.FXTimePickerSkin;

/**
 * The {@link FXTimePicker} is a simple time picker control.
 * 
 * @author Robert Zenz
 */
public class FXTimePicker extends Control
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/css/fxtimepicker.css";
	
	/** The default class name used for styling. */
	public static final String DEFAULT_STYLE_CLASS = "time-picker";
	
	/** The property for the current value. */
	private ObjectProperty<LocalTime> value;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTimePicker}.
	 */
	public FXTimePicker()
	{
		super();
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
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
		return new FXTimePickerSkin(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for the value.
	 * 
	 * @return the property for the value.
	 */
	public ObjectProperty<LocalTime> valueProperty()
	{
		return value;
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value.
	 */
	public LocalTime getValue()
	{
		return value.get();
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param pValue the value.
	 */
	public void setValue(LocalTime pValue)
	{
		value.set(pValue);
	}
	
}	// FXTimePicker

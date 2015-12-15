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

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import com.sibvisions.rad.ui.javafx.ext.skin.FXMonthViewSkin;

/**
 * The {@link FXMonthView} is a simple calendar like control that displays a
 * single month, together with controls at the top to change what month is
 * displayed.
 * <p>
 * It can be used as a simple date picker.
 * 
 * @author Robert Zenz
 */
public class FXMonthView extends Control
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/css/fxmonthview.css";
	
	/** The default class name used for styling. */
	public static final String DEFAULT_STYLE_CLASS = "monthview";
	
	/** The property for the selected date. */
	private ObjectProperty<LocalDate> selectedDate;
	
	/** The property for the currently visible month. */
	private ObjectProperty<LocalDate> visibleMonth;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXMonthView}.
	 */
	public FXMonthView()
	{
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		selectedDate = new SimpleObjectProperty<>();
		
		visibleMonth = new SimpleObjectProperty<>(LocalDate.now());
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
		return new FXMonthViewSkin(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the currently selected {@link LocalDate}.
	 * 
	 * @return the currently selected {@link LocalDate}. {@code null} if there
	 *         is none selected.
	 */
	public LocalDate getSelectedDate()
	{
		return selectedDate.get();
	}
	
	/**
	 * Gets the currently visible month.
	 * 
	 * @return the currently visible month.
	 */
	public LocalDate getVisibleMonth()
	{
		return visibleMonth.get();
	}
	
	/**
	 * Gets the property for the currently selected {@link LocalDate}.
	 * 
	 * @return the property for the currently selected {@link LocalDate}.
	 */
	public ObjectProperty<LocalDate> selectedDateProperty()
	{
		return selectedDate;
	}
	
	/**
	 * Sets the currently selected {@link LocalDate}.
	 * 
	 * @param pSelectedDate the currently selected {@link LocalDate}.
	 *            {@code null} if there is none selected.
	 */
	public void setSelectedDate(LocalDate pSelectedDate)
	{
		selectedDate.set(pSelectedDate);
	}
	
	/**
	 * Sets the currently visible month.
	 * 
	 * @param pVisibleMonth the currently visible month.
	 */
	public void setVisibleMonth(LocalDate pVisibleMonth)
	{
		visibleMonth.set(pVisibleMonth);
	}
	
	/**
	 * Gets the property for the currently visible month.
	 * 
	 * @return the property for the currently visible month.
	 */
	public ObjectProperty<LocalDate> visibleMonthProperty()
	{
		return visibleMonth;
	}
	
}	// FXMonthView

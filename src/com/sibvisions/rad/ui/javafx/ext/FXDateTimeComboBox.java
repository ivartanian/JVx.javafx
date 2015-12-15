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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 * The {@link FXDateTimeComboBox} is a {@link FXCustomComboBox} extension that
 * allows to pick a date and a time.
 * 
 * @author Robert Zenz
 */
public class FXDateTimeComboBox extends FXCustomComboBox<LocalDateTime>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link DateTimeFormatter} used. */
	private ObjectProperty<DateTimeFormatter> formatter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDateTimeComboBox}.
	 */
	public FXDateTimeComboBox()
	{
		super(new DateTimePickerPopUpProvider());
		
		formatter = new SimpleObjectProperty<>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property used for the {@link DateTimeFormatter}.
	 * 
	 * @return the property for the {@link DateTimeFormatter}.
	 */
	public ObjectProperty<DateTimeFormatter> formatterProperty()
	{
		return formatter;
	}
	
	/**
	 * Gets the {@link DateTimeFormatter}.
	 * 
	 * @return the {@link DateTimeFormatter}.
	 */
	public DateTimeFormatter getFormatter()
	{
		return formatter.get();
	}
	
	/**
	 * Sets the {@link DateTimeFormatter}.
	 * 
	 * @param pFormatter the {@link DateTimeFormatter}.
	 */
	public void setFormatter(DateTimeFormatter pFormatter)
	{
		formatter.set(pFormatter);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link IFXComboBoxPopupProvider} that is used.
	 * 
	 * @author Robert Zenz
	 */
	private static final class DateTimePickerPopUpProvider implements IFXComboBoxPopupProvider<LocalDateTime>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link FXDateTimePicker} used for the popup. */
		private FXDateTimePicker dateTimePicker;
		
		/** The parent {@link FXDateTimeComboBox}. */
		private FXDateTimeComboBox parent;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link DateTimePickerPopUpProvider}.
		 */
		public DateTimePickerPopUpProvider()
		{
			// Nothing to be done.
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public LocalDateTime fromString(String pString)
		{
			if (pString == null || pString.isEmpty())
			{
				return null;
			}
			
			DateTimeFormatter formatter = parent.getFormatter();
			
			LocalDate localDate = null;
			LocalTime localTime = null;
			
			LocalDateTime value = parent.getValue();
			
			if (value != null)
			{
				localDate = value.toLocalDate();
				localTime = value.toLocalTime();
			}
			
			try
			{
				if (formatter != null)
				{
					localDate = LocalDate.parse(pString, formatter);
				}
				else
				{
					localDate = LocalDate.parse(pString);
				}
			}
			catch (DateTimeParseException e)
			{
				// Ignore any exception.
			}
			
			try
			{
				if (formatter != null)
				{
					localTime = LocalTime.parse(pString, formatter);
				}
				else
				{
					localTime = LocalTime.parse(pString);
				}
			}
			catch (DateTimeParseException e)
			{
				// Ignore any exception.
			}
			
			return LocalDateTime.of(localDate, localTime);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void hidePopup()
		{
			if (dateTimePicker != null)
			{
				dateTimePicker.valueProperty().unbindBidirectional(parent.valueProperty());
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void init(FXCustomComboBox<LocalDateTime> pParentComboBox)
		{
			parent = ((FXDateTimeComboBox)pParentComboBox);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void selectNext()
		{
			if (dateTimePicker != null && dateTimePicker.getValue() != null)
			{
				dateTimePicker.setValue(dateTimePicker.getValue().plusDays(1));
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void selectPrevious()
		{
			if (dateTimePicker != null && dateTimePicker.getValue() != null)
			{
				dateTimePicker.setValue(dateTimePicker.getValue().minusDays(1));
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Node showPopup()
		{
			if (dateTimePicker == null)
			{
				dateTimePicker = new FXDateTimePicker();
			}
			
			dateTimePicker.valueProperty().bindBidirectional(parent.valueProperty());
			
			if (parent.getFormatter() != null)
			{
				// TODO HACK We rely on the string representation of the formatter to figure out if the time picker should be displayed or not.
				String format = parent.getFormatter().toString();
				dateTimePicker.setTimePickerVisible(format.contains("HourOfDay")
						|| format.contains("MinuteOfHour")
						|| format.contains("SecondOfMinute"));
			}
			else
			{
				dateTimePicker.setTimePickerVisible(false);
			}
			
			return dateTimePicker;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(LocalDateTime pValue)
		{
			if (pValue == null)
			{
				return null;
			}
			
			DateTimeFormatter formatter = parent.getFormatter();
			
			try
			{
				if (formatter != null)
				{
					return pValue.format(formatter);
				}
				else
				{
					return pValue.toString();
				}
			}
			catch (DateTimeParseException e)
			{
				// Ignore any exception.
			}
			
			return null;
		}
		
	}	// DateTimePickerPopUpProvider
	
}	// FXDateTimeComboBox

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

import com.sibvisions.rad.ui.javafx.ext.FXDateTimePicker;
import com.sibvisions.rad.ui.javafx.ext.FXMonthView;
import com.sibvisions.rad.ui.javafx.ext.FXSpinnerRT40623;
import com.sibvisions.rad.ui.javafx.ext.FXTimePicker;
import com.sibvisions.rad.ui.javafx.ext.behavior.FXDateTimePickerBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * The {@link FXDateTimePickerSkin} is the default skin for the
 * {@link FXDateTimePicker} .
 * 
 * @author Robert Zenz
 */
public class FXDateTimePickerSkin extends BehaviorSkinBase<FXDateTimePicker, FXDateTimePickerBehavior>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link FXMonthView} used for selecting a day. */
	private FXMonthView daySelector;
	
	/** If any events should be ignored. */
	private boolean ignoreEvents;
	
	/** The {@link ComboBox} used for selecting a month. */
	private ComboBox<Month> monthSelector;
	
	/** The {@link FXTimePicker} used for selecting a time. */
	private FXTimePicker timeSelector;
	
	/** The listener for when the value changes. */
	private ChangeListener<LocalDateTime> valueChangedListener;
	
	/** The {@link Spinner} used for selecting a year. */
	private Spinner<Integer> yearSelector;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDateTimePickerSkin}.
	 *
	 * @param pControl the control.
	 */
	public FXDateTimePickerSkin(FXDateTimePicker pControl)
	{
		this(pControl, new FXDateTimePickerBehavior(pControl));
	}
	
	/**
	 * Creates a new instance of {@link FXDateTimePickerSkin}.
	 *
	 * @param pControl the control.
	 * @param pBehavior the behavior.
	 */
	public FXDateTimePickerSkin(FXDateTimePicker pControl, FXDateTimePickerBehavior pBehavior)
	{
		super(pControl, pBehavior);
		
		valueChangedListener = this::onValueChanged;
		
		monthSelector = createMonthSelector();
		yearSelector = createYearSelector();
		timeSelector = createTimeSelector();
		
		daySelector = createDaySelector();
		
		GridPane mainPane = new GridPane();
		mainPane.getStyleClass().add("main-pane");
		
		mainPane.add(createLegendLabel("Month"), 0, 0);
		mainPane.add(createLegendLabel("Year"), 1, 0);
		
		mainPane.add(monthSelector, 0, 1);
		GridPane.setHgrow(monthSelector, Priority.ALWAYS);
		GridPane.setHalignment(monthSelector, HPos.LEFT);
		
		mainPane.add(yearSelector, 1, 1);
		GridPane.setHgrow(yearSelector, Priority.NEVER);
		GridPane.setHalignment(yearSelector, HPos.LEFT);
		
		mainPane.add(daySelector, 0, 2, 2, 1);
		GridPane.setHgrow(daySelector, Priority.ALWAYS);
		GridPane.setVgrow(daySelector, Priority.ALWAYS);
		
		// This is a hack because I could not find a better way to resize
		// the control in the way I want it. It was always left aligned.
		BorderPane timeSelectorInnerContainer = new BorderPane();
		timeSelectorInnerContainer.setTop(timeSelector);
		timeSelectorInnerContainer.setBottom(createLegendLabel("Hour / Minute / Second"));
		
		BorderPane timeSelectorContainer = new BorderPane();
		timeSelectorContainer.setRight(timeSelectorInnerContainer);
		timeSelectorContainer.managedProperty().bind(getSkinnable().timePickerVisibleProperty());
		timeSelectorContainer.visibleProperty().bind(getSkinnable().timePickerVisibleProperty());
		
		mainPane.add(timeSelectorContainer, 0, 3, 2, 1);
		GridPane.setHgrow(timeSelectorContainer, Priority.NEVER);
		GridPane.setHalignment(timeSelectorContainer, HPos.RIGHT);
		
		getChildren().add(mainPane);
		
		getSkinnable().valueProperty().addListener(valueChangedListener);
		
		updateSelectors(getSkinnable().getValue());
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
	 * Creates a {@link Label} that is used a legend.
	 * 
	 * @param pText the text of the {@link label}.
	 * @return the {@link Label}.
	 */
	private Label createLegendLabel(String pText)
	{
		Label label = new Label(pText);
		label.getStyleClass().add("legend-label");
		
		return label;
	}
	
	/**
	 * Creates the {@link ComboBox} used for selection a month.
	 * 
	 * @return the {@link ComboBox} for selecting a month.
	 */
	private ComboBox<Month> createMonthSelector()
	{
		ComboBox<Month> monthsComboBox = new ComboBox<>();
		monthsComboBox.getItems().addAll(Month.values());
		monthsComboBox.getStyleClass().add("month-selector");
		monthsComboBox.setConverter(new MonthStringConverter());
		monthsComboBox.getSelectionModel().selectedItemProperty().addListener(this::onSelectedMonthChanged);
		
		return monthsComboBox;
	}
	
	/**
	 * Creates the {@link FXMonthView} used for selection a day.
	 * 
	 * @return the {@link FXMonthView} for selecting a day.
	 */
	private FXMonthView createDaySelector()
	{
		FXMonthView monthView = new FXMonthView();
		monthView.getStyleClass().add("date-selector");
		monthView.getStylesheets().add(FXDateTimePicker.DEFAULT_STYLE);
		monthView.selectedDateProperty().addListener(this::onSelectedDayChanged);
		
		return monthView;
	}
	
	/**
	 * Creates the {@link FXTimePicker} used for selection a time.
	 * 
	 * @return the {@link FXTimePicker} for selecting a time.
	 */
	private FXTimePicker createTimeSelector()
	{
		FXTimePicker timePicker = new FXTimePicker();
		timePicker.getStyleClass().add("time-selector");
		timePicker.getStylesheets().add(FXDateTimePicker.DEFAULT_STYLE);
		timePicker.valueProperty().addListener(this::onSelectedTimeChanged);
		
		return timePicker;
	}
	
	/**
	 * Creates the {@link ComboBox} used for selection a year.
	 * 
	 * @return the {@link ComboBox} for selecting a year.
	 */
	private Spinner<Integer> createYearSelector()
	{
		Spinner<Integer> yearSpinner = new FXSpinnerRT40623<>(new IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
		yearSpinner.getStyleClass().add("year-selector");
		yearSpinner.setEditable(true);
		yearSpinner.valueProperty().addListener(this::onSelectedYearChanged);
		
		return yearSpinner;
	}
	
	/**
	 * Invoked if the selected day changed.
	 * <p>
	 * Updates the value.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedDayChanged(ObservableValue<? extends LocalDate> pObservable, LocalDate pOldValue, LocalDate pNewValue)
	{
		updateValue();
	}
	
	/**
	 * Invoked of the visible month changed.
	 * <p>
	 * Updates the {@link FXMonthView} with the new month.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedMonthChanged(ObservableValue<? extends Month> pObservable, Month pOldValue, Month pNewValue)
	{
		LocalDate visibleMonth = daySelector.getVisibleMonth();
		
		if (visibleMonth != null)
		{
			daySelector.setVisibleMonth(visibleMonth.withMonth(pNewValue.getValue()));
		}
	}
	
	/**
	 * Invoked if the selected time changed.
	 * <p>
	 * Updates the value.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedTimeChanged(ObservableValue<? extends LocalTime> pObservable, LocalTime pOldValue, LocalTime pNewValue)
	{
		updateValue();
	}
	
	/**
	 * Invoked if the visible year changed.
	 * <p>
	 * Updates the {@link FXMonthView} with the new year.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedYearChanged(ObservableValue<? extends Integer> pObservable, Integer pOldValue, Integer pNewValue)
	{
		LocalDate visibleMonth = daySelector.getVisibleMonth();
		
		if (visibleMonth != null)
		{
			daySelector.setVisibleMonth(visibleMonth.withYear(pNewValue.intValue()));
		}
	}
	
	/**
	 * Invoked if the value of the control changes.
	 * 
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onValueChanged(ObservableValue<? extends LocalDateTime> pObservableValue, LocalDateTime pOldValue, LocalDateTime pNewValue)
	{
		updateSelectors(pNewValue);
	}
	
	/**
	 * Updates all selectors with the given value.
	 * 
	 * @param pValue the value.
	 */
	private void updateSelectors(LocalDateTime pValue)
	{
		if (pValue != null)
		{
			if (!ignoreEvents)
			{
				ignoreEvents = true;
				
				monthSelector.getSelectionModel().select(pValue.getMonth());
				yearSelector.getValueFactory().setValue(Integer.valueOf(pValue.getYear()));
				timeSelector.setValue(pValue.toLocalTime());
				
				daySelector.setSelectedDate(pValue.toLocalDate());
				
				ignoreEvents = false;
			}
		}
		else
		{
			updateSelectors(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
		}
	}
	
	/**
	 * Updates the value from the selectors.
	 */
	private void updateValue()
	{
		if (!ignoreEvents)
		{
			ignoreEvents = true;
			
			LocalDateTime value = null;
			
			if (daySelector.getSelectedDate() != null)
			{
				value = LocalDateTime.of(daySelector.getSelectedDate(), timeSelector.getValue());
			}
			
			getSkinnable().setValue(value);
			
			ignoreEvents = false;
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link MonthStringConverter} converts between a {@link String} and a
	 * {@link Month}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class MonthStringConverter extends StringConverter<Month>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Month fromString(String pString)
		{
			for (Month value : Month.values())
			{
				if (value.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(pString))
				{
					return value;
				}
			}
			
			return Month.JANUARY;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(Month pObject)
		{
			return pObject.getDisplayName(TextStyle.FULL, Locale.getDefault());
		}
		
	}	// MonthStringConverter
	
}	// FXTimePickerSkin

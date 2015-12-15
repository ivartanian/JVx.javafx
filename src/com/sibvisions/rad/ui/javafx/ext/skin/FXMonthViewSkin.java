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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import com.sibvisions.rad.ui.javafx.ext.FXMonthView;
import com.sibvisions.rad.ui.javafx.ext.FXSelectableLabel;

/**
 * The default skin for the {@link FXMonthView}.
 * 
 * @author Robert Zenz
 */
public class FXMonthViewSkin extends SkinBase<FXMonthView>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The {@link List} of {@link DateLabel}s that are for displaying the month.
	 */
	private List<DateLabel> dayLabels;
	
	/**
	 * The {@link List} of {@link Label}s that are for displaying the week
	 * numbers.
	 */
	private List<Label> weekNumberItems;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXMonthViewSkin}.
	 *
	 * @param pControl the control.
	 */
	public FXMonthViewSkin(FXMonthView pControl)
	{
		super(pControl);
		
		dayLabels = new ArrayList<>();
		weekNumberItems = new ArrayList<>();
		
		getChildren().add(createMonthView());
		
		updateMonthView(getSkinnable().getVisibleMonth());
		
		getSkinnable().selectedDateProperty().addListener(this::onSelectedDateChanged);
		getSkinnable().visibleMonthProperty().addListener(this::onVisibleMonthChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link Node} to the given {@link GridPane} at the given
	 * location with the given parameters.
	 * 
	 * @param pGridPane the {@link GridPane}.
	 * @param pNode the {@link Node} to add.
	 * @param pColumn the index of the column.
	 * @param pRow the index of the row.
	 * @param pHGrow if the {@link Node} should grow horizontally.
	 * @param pVGrow if the {@link Node} should grow vertically.
	 */
	private void addToGridPane(GridPane pGridPane, Node pNode, int pColumn, int pRow, boolean pHGrow, boolean pVGrow)
	{
		pGridPane.add(pNode, pColumn, pRow);
		
		if (pHGrow)
		{
			GridPane.setHgrow(pNode, Priority.ALWAYS);
		}
		if (pVGrow)
		{
			GridPane.setVgrow(pNode, Priority.ALWAYS);
		}
		
		GridPane.setHalignment(pNode, HPos.CENTER);
		GridPane.setValignment(pNode, VPos.CENTER);
	}
	
	/**
	 * Creates a {@link DateLabel} used for showing a day.
	 * 
	 * @param pWeekDay the day of the week.
	 * @return a {@link DateLabel}.
	 */
	private Label createDayDateLabel(int pWeekDay)
	{
		DateLabel day = new DateLabel();
		day.getStyleClass().add("day");
		if (pWeekDay == 6 || pWeekDay == 7)
		{
			day.getStyleClass().add("weekend");
		}
		// Add the stylesheet of the month view so that these labels are styled.
		// Because they have their own (default) stylesheets, the current one
		// will not be propagated to them.
		day.getStylesheets().add(FXMonthView.DEFAULT_STYLE);
		day.setAlignment(Pos.CENTER);
		day.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		day.setOnSelectedChanged(this::onSelectedDayChanged);
		
		dayLabels.add(day);
		
		return day;
	}
	
	/**
	 * Creates the month view, meaning the main part with all the days.
	 * 
	 * @return the node used as month view.
	 */
	private Node createMonthView()
	{
		GridPane monthPane = new GridPane();
		monthPane.getStyleClass().add("month");
		monthPane.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		
		for (int weekDay = 1; weekDay <= 7; weekDay++)
		{
			String weekDayNameShort = DayOfWeek.of(weekDay).getDisplayName(TextStyle.SHORT, Locale.getDefault());
			String weekDayNameFull = DayOfWeek.of(weekDay).getDisplayName(TextStyle.FULL, Locale.getDefault());
			
			addToGridPane(monthPane, createWeekDayLabel(weekDayNameShort, weekDayNameFull), weekDay, 0, true, false);
		}
		
		for (int row = 1; row <= 6; row++)
		{
			addToGridPane(monthPane, createWeekNumber(), 0, row, false, true);
			
			for (int column = 1; column <= 7; column++)
			{
				addToGridPane(monthPane, createDayDateLabel(column), column, row, true, true);
			}
		}
		
		return monthPane;
	}
	
	/**
	 * Creates a {@link Label} that is used as header and contains the name of a
	 * weekday.
	 * 
	 * @param pText the text.
	 * @param pToolTipText the text for the tooltip.
	 * @return the {@link Label}.
	 */
	private Label createWeekDayLabel(String pText, String pToolTipText)
	{
		Label weekDayLabel = new Label(pText);
		weekDayLabel.getStyleClass().add("weekday");
		weekDayLabel.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		
		Tooltip.install(weekDayLabel, new Tooltip(pToolTipText));
		
		return weekDayLabel;
	}
	
	/**
	 * Creates a {@link Label} that is used as for displaying a week number.
	 * 
	 * @return the {@link Label}.
	 */
	private Label createWeekNumber()
	{
		Label weekNumber = new Label();
		weekNumber.getStyleClass().add("weeknumber");
		weekNumber.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		
		weekNumberItems.add(weekNumber);
		
		return weekNumber;
	}
	
	/**
	 * Invoked of the selected date changed.
	 * <p>
	 * Updates the complete view.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedDateChanged(ObservableValue<? extends LocalDate> pObservable, LocalDate pOldValue, LocalDate pNewValue)
	{
		if (pNewValue != null)
		{
			if (pOldValue == null || pOldValue.getMonth() != pNewValue.getMonth() || pOldValue.getYear() != pNewValue.getYear())
			{
				updateMonthView(pNewValue);
			}
		}
		
		updateSelection(pNewValue);
	}
	
	/**
	 * Invoked of the selected day changed.
	 * <p>
	 * Updates the {@link FXMonthView} with the new date.
	 * 
	 * @param pDateLabel the {@link DateLabel} that changed.
	 */
	private void onSelectedDayChanged(DateLabel pDateLabel)
	{
		getSkinnable().setSelectedDate(pDateLabel.getDate());
	}
	
	/**
	 * Invoked of the visible month property changed.
	 * <p>
	 * Updates the complete view.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onVisibleMonthChanged(ObservableValue<? extends LocalDate> pObservable, LocalDate pOldValue, LocalDate pNewValue)
	{
		if (pNewValue != null)
		{
			updateMonthView(pNewValue);
		}
		else if (getSkinnable().getSelectedDate() != null)
		{
			updateMonthView(getSkinnable().getSelectedDate());
		}
	}
	
	/**
	 * Updates the complete view.
	 * 
	 * @param pLocalDate the new {@link LocalDate} to update to.
	 */
	private void updateMonthView(LocalDate pLocalDate)
	{
		LocalDate startOfMonth = pLocalDate.withDayOfMonth(1);
		
		TemporalField week = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		
		for (int weekNumber = 1; weekNumber <= weekNumberItems.size(); weekNumber++)
		{
			weekNumberItems.get(weekNumber - 1).setText(Integer.toString(startOfMonth.plusWeeks(weekNumber - 1).get(week)));
		}
		
		int offset = startOfMonth.getDayOfWeek().getValue() - 1;
		int lengthOfMonth = startOfMonth.lengthOfMonth();
		
		for (int index = 0; index < dayLabels.size(); index++)
		{
			DateLabel day = dayLabels.get(index);
			
			day.getStyleClass().removeAll("current-month-day", "next-month-day", "previous-month-day");
			
			if (index < offset)
			{
				day.setDate(startOfMonth.minusDays(offset - index));
				day.getStyleClass().add("previous-month-day");
			}
			else if (index - offset >= lengthOfMonth)
			{
				day.setDate(startOfMonth.plusDays(index - offset));
				day.getStyleClass().add("next-month-day");
			}
			else
			{
				day.setDate(startOfMonth.plusDays(index - offset));
				day.getStyleClass().add("current-month-day");
			}
		}
		
		updateSelection(getSkinnable().getSelectedDate());
	}
	
	/**
	 * Updates the selection of all days according to the given
	 * {@link LocalDate}.
	 * 
	 * @param pSelectedDate the {@link LocalDate} to which to update.
	 */
	private void updateSelection(LocalDate pSelectedDate)
	{
		for (DateLabel day : dayLabels)
		{
			day.setSelected(Objects.equals(day.getDate(), pSelectedDate));
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link DateLabel} is a {@link FXSelectableLabel} extension that also
	 * holds a {@link LocalDate}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class DateLabel extends FXSelectableLabel
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The property for the {@link LocalDate}. */
		private ObjectProperty<LocalDate> date;
		
		/** The action that is invoked if the selected state changes. */
		private Consumer<DateLabel> onSelectedChanged;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link DateLabel}.
		 */
		public DateLabel()
		{
			super();
			
			date = new SimpleObjectProperty<>();
			date.addListener(this::onDateChanged);
			
			selectedProperty().addListener(this::onSelectedChanged);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the associated {@link LocalDate}.
		 * 
		 * @return the {@link LocalDate}.
		 */
		public LocalDate getDate()
		{
			return date.get();
		}
		
		/**
		 * Sets the associated {@link LocalDate}.
		 * 
		 * @param pDate the {@link LocalDate}.
		 */
		public void setDate(LocalDate pDate)
		{
			date.set(pDate);
		}
		
		/**
		 * Sets the action is called of the {@link #selectedProperty()} changes.
		 * The action is only invoked if the property changes to {@link true}.
		 * 
		 * @param pOnSelectedChanged the action to invoke.
		 */
		public void setOnSelectedChanged(Consumer<DateLabel> pOnSelectedChanged)
		{
			onSelectedChanged = pOnSelectedChanged;
		}
		
		/**
		 * Invoked if the associated {@link #date} changes.
		 * <p>
		 * Updates the text.
		 * 
		 * @param pObservable the observable value.
		 * @param pOldValue the old value.
		 * @param pNewValue the new value.
		 */
		private void onDateChanged(ObservableValue<? extends LocalDate> pObservable, LocalDate pOldValue, LocalDate pNewValue)
		{
			if (pNewValue != null)
			{
				setText(Integer.toString(pNewValue.getDayOfMonth()));
			}
			else
			{
				setText("");
			}
		}
		
		/**
		 * Invoked if the {@link #selectedProperty()} changes.
		 * <p>
		 * Invokes the {@link #onSelectedChanged} action if the new value is
		 * {@code true}.
		 * 
		 * @param pObservable the observable value.
		 * @param pOldValue the old value.
		 * @param pNewValue the new value.
		 */
		private void onSelectedChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
		{
			if (onSelectedChanged != null && pNewValue.booleanValue())
			{
				onSelectedChanged.accept(this);
			}
		}
		
	}	// DateLabel
	
}	// FXMonthViewSkin

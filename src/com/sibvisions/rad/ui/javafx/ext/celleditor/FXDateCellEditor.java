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
package com.sibvisions.rad.ui.javafx.ext.celleditor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;

import javafx.beans.value.ObservableValue;

import com.sibvisions.rad.ui.celleditor.AbstractDateCellEditor;
import com.sibvisions.rad.ui.javafx.ext.FXDateTimeComboBox;
import com.sibvisions.rad.ui.javafx.ext.util.FXFrameWaitUtil;

/**
 * The {@link FXDateCellEditor} is the JavaFX specific implementation of
 * {@link javax.rad.ui.celleditor.IDateCellEditor}.
 * 
 * @author Robert Zenz
 * @see javax.rad.ui.celleditor.IDateCellEditor
 */
public class FXDateCellEditor extends AbstractDateCellEditor implements ICellRenderer<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link DateTimeFormatter} used for converting dates. */
	private DateTimeFormatter formatter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDateCellEditor}.
	 */
	public FXDateCellEditor()
	{
		super();
		
		formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellEditorHandler<?> createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
	{
		return new DatePickerCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellRendererComponent(String pParentComponent, IDataPage pDataPage, int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected,
			boolean pHasFocus)
	{
		try
		{
			Timestamp value = (Timestamp)pDataRow.getValue(pColumnName);
			
			if (value != null)
			{
				return formatter.format(toLocalDateTime(value));
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
		
		return null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDateFormat(String pDateFormat)
	{
		super.setDateFormat(pDateFormat);
		
		formatter = DateTimeFormatter.ofPattern(format);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Converts the given date to a {@link LocalDateTime}.
	 * 
	 * @param pDate the {@link Date}
	 * @return the {@link LocalDateTime}
	 */
	private static LocalDateTime toLocalDateTime(Date pDate)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		
		return LocalDateTime.of(
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));
	}

	/**
	 * Converts the given date to a {@link Timestamp}.
	 * 
	 * @param pDateTime the {@link LocalDateTime}.
	 * @return the {@link Timestamp}.
	 */
	private static Timestamp toTimestamp(LocalDateTime pDateTime)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(
				pDateTime.getYear(),
				pDateTime.getMonthValue() - 1,
				pDateTime.getDayOfMonth(),
				pDateTime.getHour(),
				pDateTime.getMinute(),
				pDateTime.getSecond());
				
		return new Timestamp(cal.getTimeInMillis());
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link DatePickerCellEditorHandler} is the
	 * {@link FXAbstractCellEditorHandler} extension for the
	 * {@link FXDateCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class DatePickerCellEditorHandler extends FXAbstractCellEditorHandler<FXDateTimeComboBox, FXDateCellEditor> implements
			ICellEditorHandler<FXDateTimeComboBox>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** Whether {@link #cancelEditing()} was called once. */
		private boolean firstCancel;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link DatePickerCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 */
		public DatePickerCellEditorHandler(FXDateCellEditor pCellEditor, ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
		{
			super(pCellEditor, pCellEditorListener, new FXDateTimeComboBox(), pDataRow, pColumnName);
			
			component.showingProperty().addListener(this::onShowingChanged);
			
			firstCancel = true;
			
			component.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			
			if (isSavingImmediate())
			{
				attachValueChangeListener(component.valueProperty());
			}
			
			registerKeyEventFilter();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void cancelEditing() throws ModelException
		{
			component.setFormatter(cellEditor.formatter);
			
			Timestamp value = getValue();
			
			removeValueChangeListener(component.valueProperty());
			
			if (value != null)
			{
				component.setValue(toLocalDateTime(value));
			}
			else
			{
				component.setValue(null);
			}
			
			if (isSavingImmediate())
			{
				attachValueChangeListener(component.valueProperty());
			}
			
			if (cellEditor.isAutoOpenPopup())
			{
				component.show();
			}
			
			component.setDisable(!shouldBeEnabled());
			
			if (component.isFocused() && firstCancel)
			{
				FXFrameWaitUtil.runLater(() -> component.show());
			}
			
			firstCancel = false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			// Make sure that the DatePicker holds the latest value.
			removeValueChangeListener(component.valueProperty());
			
			if (isSavingImmediate())
			{
				attachValueChangeListener(component.valueProperty());
			}
			
			LocalDateTime value = component.getValue();
			
			if (value != null)
			{
				setValue(toTimestamp(value));
			}
			else if (getLastValue() != null)
			{
				setValue(null);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void uninstallEditor()
		{
			super.uninstallEditor();
			
			removeValueChangeListener(component.valueProperty());
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Invoked if the visibility of the popup changes.
		 * 
		 * @param pObservable the observable.
		 * @param pOldValue the old value.
		 * @param pNewValue the new value.
		 */
		private void onShowingChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
		{
			if (!pNewValue.booleanValue())
			{
				fireEditingComplete(ICellEditorListener.FOCUS_LOST);
			}
		}
		
	}	// DatePickerCellEditorHandler
	
}	// FXDateCellEditor

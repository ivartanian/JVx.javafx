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

import java.util.Objects;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.ui.IColor;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.rad.ui.javafx.ext.util.FXNodeFocusedHelper;
import com.sibvisions.rad.ui.javafx.impl.util.JavaFXColorUtil;

/**
 * The {@link FXAbstractCellEditorHandler} is an {@link ICellEditorHandler}
 * implementation that implements some basic functionality.
 * 
 * @author Robert Zenz
 * @param <CO> The type of he component.
 * @param <CE> The type of the cell editor.
 */
public abstract class FXAbstractCellEditorHandler<CO extends Node, CE extends ICellEditor> implements ICellEditorHandler<CO>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The parent {@link ICellEditor}. */
	protected CE cellEditor;
	
	/** The {@link ICellEditorListener}. */
	protected ICellEditorListener cellEditorListener;
	
	/** The name of the bound column. */
	protected String columnName;
	
	/** The component itself. */
	protected CO component;
	
	/** The bound {@link IDataBook}. */
	protected IDataBook dataBook;
	
	/** The bound {@link IDataRow}. */
	protected IDataRow dataRow;
	
	/** The {@link FXNodeFocusedHelper} that is used. */
	private FXNodeFocusedHelper focusHelper;
	
	/** The listener for focus changes. */
	private ChangeListener<Boolean> focusListener;
	
	/** The handler for key events. */
	private EventHandler<KeyEvent> keyPressedEventFilter;
	
	/** The last value that was fetched from the bound {@link IDataRow}. */
	private Object lastValue;
	
	/** The {@link StyleContainer} used for the cell editor. */
	private StyleContainer styleContainer;
	
	/** The listener for value changes. */
	private ChangeListener<Object> valueListener;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXAbstractCellEditorHandler}.
	 *
	 * @param pCellEditor the {@link ICellEditorListener}.
	 * @param pCellEditorListener the {@link ICellEditorListener}.
	 * @param pComponent the component.
	 * @param pDataRow the {@link IDataRow}.
	 * @param pColumnName the column name.
	 */
	protected FXAbstractCellEditorHandler(CE pCellEditor, ICellEditorListener pCellEditorListener, CO pComponent, IDataRow pDataRow, String pColumnName)
	{
		super();
		
		focusListener = this::onFocusChanged;
		keyPressedEventFilter = this::onKeyPressedEventFilter;
		valueListener = this::onValueChanged;
		
		cellEditor = pCellEditor;
		cellEditorListener = pCellEditorListener;
		component = pComponent;
		dataRow = pDataRow;
		columnName = pColumnName;
		
		if (dataRow instanceof IDataBook)
		{
			dataBook = (IDataBook)dataRow;
		}
		
		if (component instanceof Region)
		{
			// Make sure that the component can be of any size.
			((Region)component).setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
		
		styleContainer = new StyleContainer(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CE getCellEditor()
	{
		return cellEditor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CO getCellEditorComponent()
	{
		return component;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellEditorListener getCellEditorListener()
	{
		return cellEditorListener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName()
	{
		return columnName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataRow getDataRow()
	{
		return dataRow;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uninstallEditor()
	{
		if (focusHelper != null)
		{
			focusHelper.focusedProperty().removeListener(focusListener);
			focusHelper.setNode(null);
		}
		
		component.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventFilter);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Applies the default styling to the {@link #component}. This includes
	 * getting the default styling and inheriting the styling of the
	 * {@link ICellEditorListener}.
	 * 
	 * @throws ModelException if accessing the {@link #dataRow} or any of its
	 *             information failed.
	 */
	protected void applyDefaultStyling() throws ModelException
	{
		styleContainer.clear();
		
		ColumnDefinition columnDefinition = dataRow.getRowDefinition().getColumnDefinition(columnName);
		
		if (columnDefinition.isNullable())
		{
			styleContainer.setInnerBackground(null);
		}
		else
		{
			styleContainer.setInnerBackground(((Color)JavaFXColorUtil.getSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND).getResource()));
		}
		
		if (cellEditorListener instanceof Node)
		{
			Node cellEditorListenerNode = (Node)cellEditorListener;
			
			StyleContainer parentStyleContainer = (StyleContainer)cellEditorListenerNode.getProperties().get(StyleContainer.class);
			
			if (parentStyleContainer != null)
			{
				if (parentStyleContainer.getInnerBackground() != null)
				{
					styleContainer.setInnerBackground(parentStyleContainer.getInnerBackground());
				}
				else if (parentStyleContainer.getBackgroundColor() != null)
				{
					styleContainer.setInnerBackground(parentStyleContainer.getBackgroundColor());
				}
				
				styleContainer.setCursor(parentStyleContainer.getCursor());
				styleContainer.setForeground(parentStyleContainer.getForeground());
				styleContainer.setFont(parentStyleContainer.getFont());
			}
			else
			{
				styleContainer.setAfter(cellEditorListenerNode.getStyle());
			}
		}
	}
	
	/**
	 * Attaches a listener to the given property, which automatically triggers a
	 * save everytime the property changes.
	 * 
	 * @param pProperty the property to attach to.
	 */
	protected void attachValueChangeListener(Property<?> pProperty)
	{
		pProperty.addListener(valueListener);
	}
	
	/**
	 * Informs the {@link #cellEditorListener} (if any) that editing has been
	 * completed.
	 * 
	 * @param pCompleteType the type.
	 */
	protected void fireEditingComplete(String pCompleteType)
	{
		if (cellEditorListener != null)
		{
			cellEditorListener.editingComplete(pCompleteType);
		}
	}
	
	/**
	 * Informs the {@link #cellEditorListener} (if any) that editing has been
	 * started.
	 */
	protected void fireEditingStarted()
	{
		if (cellEditorListener != null)
		{
			cellEditorListener.editingStarted();
		}
	}
	
	/**
	 * Gets the last value that was got.
	 * 
	 * @return the last value.
	 * @param <T> the type of the value.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getLastValue()
	{
		return (T)lastValue;
	}
	
	/**
	 * Gets the value from the bound {@link IDataRow}.
	 * 
	 * @param <T> the type of the value.
	 * @return the value.
	 * @throws ModelException if getting of the value failed.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getValue() throws ModelException
	{
		lastValue = dataRow.getValue(columnName);
		return (T)lastValue;
	}
	
	/**
	 * Gets the value as string.
	 * 
	 * @return the value as string.
	 * @throws ModelException if getting the value failed.
	 */
	protected String getValueAsString() throws ModelException
	{
		lastValue = dataRow.getValue(columnName);
		
		return dataRow.getValueAsString(columnName);
	}
	
	/**
	 * Checks if changes should be saved immediately.
	 * 
	 * @return {@code true} if changes should be saved immediately.
	 */
	protected boolean isSavingImmediate()
	{
		return cellEditorListener != null && cellEditorListener.isSavingImmediate();
	}
	
	/**
	 * Registers a listener at the focus property of {@link #component} and
	 * invokes save every time it loses the focus.
	 */
	protected void registerFocusChangedListener()
	{
		focusHelper = new FXNodeFocusedHelper(component);
		focusHelper.focusedProperty().addListener(focusListener);
	}
	
	/**
	 * Registers a listener for key events of {@link #component} and invokes
	 * save every time ENTER is pressed and cancels every time ESCAPE is
	 * pressed.
	 */
	protected void registerKeyEventFilter()
	{
		component.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventFilter);
	}
	
	/**
	 * Removes the listener from the property (if any).
	 * 
	 * @param pProperty the property from which to remove the listener.
	 */
	protected void removeValueChangeListener(Property<?> pProperty)
	{
		pProperty.removeListener(valueListener);
	}
	
	/**
	 * Sets the given value into the {@link IDataRow} is it is not the same as
	 * the last value.
	 * 
	 * @param pValue the value to set.
	 * @throws ModelException if setting the value failed.
	 */
	protected void setValue(Object pValue) throws ModelException
	{
		if ((dataBook == null || dataBook.getSelectedRow() >= 0) && !Objects.equals(lastValue, pValue))
		{
			dataRow.setValue(columnName, pValue);
		}
	}
	
	/**
	 * Checks if this editor should be enabled or not.
	 * 
	 * @return {@code true} if it should be enabled.
	 * @throws ModelException if querying various information from the bound
	 *             {@link IDataRow} failed.
	 */
	protected boolean shouldBeEnabled() throws ModelException
	{
		boolean parentEnabled = component.getParent() == null || !component.getParent().isDisable();
		
		if (dataBook != null)
		{
			return parentEnabled
					&& dataBook.isUpdateAllowed()
					&& !dataBook.getRowDefinition().getColumnDefinition(columnName).isReadOnly()
					&& dataBook.getSelectedRow() >= 0;
		}
		else if (dataRow != null)
		{
			return parentEnabled
					&& !dataRow.getRowDefinition().getColumnDefinition(columnName).isReadOnly();
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Invoked every time the component does lose the focus, and invokes a save.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onFocusChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (pOldValue.booleanValue() && !pNewValue.booleanValue())
		{
			fireEditingComplete(ICellEditorListener.FOCUS_LOST);
		}
	}
	
	/**
	 * Invoked every time a key is pressed and saves on ENTER and cancels on
	 * ESCAPE.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyPressedEventFilter(KeyEvent pKeyEvent)
	{
		if (pKeyEvent.getCode() == KeyCode.ENTER)
		{
			if (pKeyEvent.isShiftDown())
			{
				fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
			}
			else
			{
				fireEditingComplete(ICellEditorListener.ENTER_KEY);
			}
			
			pKeyEvent.consume();
		}
		else if (pKeyEvent.getCode() == KeyCode.TAB)
		{
			if (pKeyEvent.isShiftDown())
			{
				fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY);
			}
			else
			{
				fireEditingComplete(ICellEditorListener.TAB_KEY);
			}
			
			pKeyEvent.consume();
		}
		else if (pKeyEvent.getCode() == KeyCode.ESCAPE)
		{
			fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
			
			pKeyEvent.consume();
		}
	}
	
	/**
	 * Invoked every time the value changes, and invokes a save.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onValueChanged(ObservableValue<? extends Object> pObservable, Object pOldValue, Object pNewValue)
	{
		try
		{
			saveEditing();
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
}	// FXAbstractCellEditorHandler

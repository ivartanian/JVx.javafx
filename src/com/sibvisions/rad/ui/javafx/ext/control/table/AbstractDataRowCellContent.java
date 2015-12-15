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
package com.sibvisions.rad.ui.javafx.ext.control.table;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;

import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 * The {@link AbstractDataRowCellContent} allows to embed a control into a
 * {@link DataRowCell}.
 * 
 * @author Robert Zenz
 * @param <C> the type of the content.
 */
public abstract class AbstractDataRowCellContent<C extends Node> extends Region
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the column. */
	protected String columnName;
	
	/** The component that is embedded. */
	protected C component;
	
	/** The {@link IDataBook} of the parent {@link FXDataBookView}. */
	protected IDataBook dataBook;
	
	/** The parent {@link DataRowCell}. */
	protected DataRowCell parentCell;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractDataRowCellContent}.
	 *
	 * @param pComponent the component.
	 */
	protected AbstractDataRowCellContent(C pComponent)
	{
		component = pComponent;
		
		component.focusedProperty().addListener(this::onFocusChanged);
		
		getChildren().add(component);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked if the {@link #component} should update its state, for example
	 * get a new value or redraw itself.
	 * 
	 * @throws ModelException if accessing the {@link #dataBook} failed for some
	 *             reason.
	 */
	protected abstract void updateComponentState() throws ModelException;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxHeight(double pWidth)
	{
		return component.maxHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxWidth(double pHeight)
	{
		return component.maxWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double pWidth)
	{
		return component.minHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double pHeight)
	{
		return component.minWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double pWidth)
	{
		return component.prefHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight)
	{
		return component.prefWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		component.autosize();
		layoutInArea(component, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.BOTTOM);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Updates this content with the given {@link DataRowCell} as new parent.
	 * 
	 * @param pParentCell the parent {@link DataRowCell}.
	 */
	public void update(DataRowCell pParentCell)
	{
		parentCell = pParentCell;
		dataBook = ((FXDataBookView) parentCell.getTableView()).getDataBook();
		columnName = (String) parentCell.getTableColumn().getUserData();
		
		try
		{
			updateComponentState();
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the value from the {@link IDataBook} and the row that is associated
	 * with the parent cell.
	 * 
	 * @param <T> the type of the value.
	 * @return the value from the {@link IDataBook}.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getValue()
	{
		try
		{
			return (T) dataBook.getDataRow(parentCell.getIndex()).getValue(columnName);
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Selects the row in the {@link IDataBook} that this content and its parent
	 * cell is associated with. Only selects the row if it is no already
	 * selected.
	 * 
	 * @throws ModelException if selecting the row failed.
	 */
	protected void selectDataBookRow() throws ModelException
	{
		int rowIndex = parentCell.getIndex();
		
		if (dataBook.getSelectedRow() != rowIndex)
		{
			dataBook.setSelectedRow(rowIndex);
		}
	}
	
	/**
	 * Sets the given into the {@link IDataBook} as new value.
	 * 
	 * @param pNewValue the new value.
	 */
	protected void setValue(Object pNewValue)
	{
		try
		{
			selectDataBookRow();
			
			dataBook.setValue(columnName, pNewValue);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Invoked if the focus of the {@link #component} changes.
	 * <p>
	 * Makes sure that the correct row is selected if the {@link #component}
	 * receives the focus.
	 * 
	 * @param pObservableValue the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onFocusChanged(ObservableValue<? extends Boolean> pObservableValue, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			try
			{
				selectDataBookRow();
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
}	// AbstractDataRowCellContent

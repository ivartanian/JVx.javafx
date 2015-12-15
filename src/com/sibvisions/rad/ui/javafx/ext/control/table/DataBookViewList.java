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

import java.util.Comparator;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;

import com.sibvisions.rad.ui.javafx.ext.control.ObservableDataBookList;
import com.sun.javafx.collections.SortableList;

/**
 * The {@link DataBookViewList} is an {@link ObservableDataBookList} extension
 * that allows easy sorting of the data.
 * 
 * @author Robert Zenz
 */
public class DataBookViewList extends ObservableDataBookList implements SortableList<IDataRow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The associated {@link FXDataBookView}. */
	protected FXDataBookView dataBookView;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataBookViewList}.
	 *
	 * @param pDataBookView the data book view.
	 * @param pDataBook the data book.
	 */
	public DataBookViewList(FXDataBookView pDataBookView, IDataBook pDataBook)
	{
		this(pDataBookView, pDataBook, FetchMode.AUTOMATIC, 500);
	}
	
	/**
	 * Creates a new instance of {@link DataBookViewList}.
	 *
	 * @param pDataBookView the data book view.
	 * @param pDataBook the data book.
	 * @param pFetchMode the fetch mode.
	 * @param pFetchBatchSize the fetch batch size.
	 */
	public DataBookViewList(FXDataBookView pDataBookView, IDataBook pDataBook, FetchMode pFetchMode, int pFetchBatchSize)
	{
		super(pDataBook, pFetchMode, pFetchBatchSize);
		
		dataBookView = pDataBookView;
		
		setAutomaticUpdates(false);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sort()
	{
		sort(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sort(Comparator<? super IDataRow> pComparator)
	{
		ObservableList<TableColumn<IDataRow, ?>> sortOrder = dataBookView.getSortOrder();
		
		try
		{
			if (sortOrder == null || sortOrder.isEmpty())
			{
				dataBook.setSort(null);
			}
			else
			{
				String[] sortColumns = new String[sortOrder.size()];
				boolean[] sortColumnsAscending = new boolean[sortColumns.length];
				
				for (int index = 0; index < sortOrder.size(); index++)
				{
					TableColumn<IDataRow, ?> column = sortOrder.get(index);
					String columnName = (String) column.getUserData();
					
					sortColumns[index] = columnName;
					sortColumnsAscending[index] = column.getSortType() == SortType.ASCENDING;
				}
				
				dataBook.setSort(new SortDefinition(sortColumns, sortColumnsAscending));
			}
			
			String currentColumn = dataBook.getSelectedColumn();
			
			fireChangedEvent(0, dataBook.getRowCount());
			
			dataBook.setSelectedColumn(currentColumn);
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Fires the changed event.
	 */
	public void notifyChanged()
	{
		try
		{
			fireChangedEvent(0, dataBook.getRowCount());
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
}	// DataBookViewList

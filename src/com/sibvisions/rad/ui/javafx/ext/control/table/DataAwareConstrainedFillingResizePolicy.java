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

import java.util.List;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.text.Text;
import javafx.util.Callback;

import com.sibvisions.rad.ui.javafx.ext.control.util.FXControlUtil;

/**
 * The {@link DataAwareConstrainedFillingResizePolicy} is a resize policy that
 * can resize the columns based on the data in the backing {@link IDataBook}.
 * <p>
 * It only works in conjunction with the {@link FXDataBookView}.
 * 
 * @author Robert Zenz
 */
@SuppressWarnings("rawtypes")
public class DataAwareConstrainedFillingResizePolicy implements Callback<ResizeFeatures, Boolean>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The initial threshold width. */
	private double thresholdWidth = 0;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataAwareConstrainedFillingResizePolicy}
	 * .
	 */
	public DataAwareConstrainedFillingResizePolicy()
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
	public Boolean call(ResizeFeatures pResizeFeatures)
	{
		double delta = pResizeFeatures.getDelta().doubleValue();
		
		if (delta != 0)
		{
			FXDataBookView dataBookView = (FXDataBookView) pResizeFeatures.getTable();
			TableColumn resizedColumn = pResizeFeatures.getColumn();
			
			if (delta == dataBookView.getWidth())
			{
				// Initial/First Resize
			}
			else if (resizedColumn == null)
			{
				// TableView has been resized.
				if (dataBookView.getWidth() - dataBookView.getVerticalScrollBarWidth() >= thresholdWidth)
				{
					resizeProportial(dataBookView);
				}
			}
			else
			{
				// Column has been resized.
				thresholdWidth = thresholdWidth + delta;
				
				TableColumn column = pResizeFeatures.getColumn();
				column.setPrefWidth(column.getPrefWidth() + delta);
				
				resizeLastColumn(dataBookView);
			}
		}
		
		return Boolean.TRUE;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Performs the initial sizing of the columns, calculates the sizes of the
	 * columns based on the data.
	 * 
	 * @param pDataBookView the {@link FXDataBookView}.
	 * @param pMaxRowCount the maximum amount of rows to consider.
	 */
	public void initialSizeColumns(FXDataBookView pDataBookView, int pMaxRowCount)
	{
		IDataBook dataBook = pDataBookView.getDataBook();
		
		if (dataBook != null)
		{
			try
			{
				IRowDefinition rowDefinition = dataBook.getRowDefinition();
				
				Text measuringText = new Text();
				
				// TODO HACK Magic number is used for calculating the size of the columns.
				double magicNumber = 14;
				
				for (TableColumn<IDataRow, ?> column : pDataBookView.getColumns())
				{
					String columnName = (String) column.getUserData();
					
					measuringText.setText(column.getText());
					column.setPrefWidth(measuringText.prefWidth(-1) + magicNumber);
					
					if (FXControlUtil.isStringColumn(rowDefinition.getColumnDefinition(columnName)))
					{
						double width = 0;
						
						for (int rowIndex = 0; rowIndex < Math.min(pMaxRowCount, dataBook.getRowCount()); rowIndex++)
						{
							measuringText.setText(dataBook.getDataRow(rowIndex).getValueAsString(columnName));
							width = Math.max(width, measuringText.prefWidth(-1) + magicNumber);
						}
						
						if (width > 0 && width > column.getPrefWidth())
						{
							column.setPrefWidth(width);
						}
					}
				}
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		thresholdWidth = 0;
		
		for (TableColumn<IDataRow, ?> column : pDataBookView.getColumns())
		{
			thresholdWidth = thresholdWidth + column.getWidth();
		}
		
		if (thresholdWidth < pDataBookView.getWidth() - pDataBookView.getVerticalScrollBarWidth())
		{
			resizeProportial(pDataBookView);
		}
	}
	
	/**
	 * Resizes the last column.
	 * 
	 * @param pDataBookView the {@link FXDataBookView}.
	 */
	public void resizeLastColumn(FXDataBookView pDataBookView)
	{
		double columnsWidth = 0;
		
		List<TableColumn<IDataRow, ?>> columns = pDataBookView.getColumns();
		
		for (int index = 0; index < columns.size() - 1; index++)
		{
			columnsWidth = columnsWidth + columns.get(index).getPrefWidth();
		}
		
		if (columnsWidth < pDataBookView.getWidth())
		{
			double lastColumnWidth = pDataBookView.getWidth() - pDataBookView.getVerticalScrollBarWidth() - columnsWidth;
			lastColumnWidth = Math.floor(lastColumnWidth);
			
			TableColumn<IDataRow, ?> lastColumn = columns.get(columns.size() - 1);
			lastColumn.setPrefWidth(lastColumnWidth);
		}
	}
	
	/**
	 * Resizes all columns proportional to their current size to the new width.
	 * 
	 * @param pDataBookView the {@link FXDataBookView}.
	 */
	public void resizeProportial(FXDataBookView pDataBookView)
	{
		List<TableColumn<IDataRow, ?>> columns = pDataBookView.getColumns();
		
		double columnsWidth = 0;
		
		for (TableColumn<IDataRow, ?> column : columns)
		{
			columnsWidth = columnsWidth + column.getPrefWidth();
		}
		
		double width = pDataBookView.getWidth() - pDataBookView.getVerticalScrollBarWidth();
		// TODO HACK Magic number is used to hinder the scrollbar from flickering.
		width = width - 3;
		
		for (TableColumn<IDataRow, ?> column : columns)
		{
			column.setPrefWidth(column.getPrefWidth() / columnsWidth * width);
		}
	}
	
}	// DataAwareConstrainedFillingResizePolicy

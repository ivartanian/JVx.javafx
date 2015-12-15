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
package com.sibvisions.rad.ui.javafx.ext.control.tree;

import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.ui.IImage;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.control.ITree;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.rad.ui.javafx.ext.control.DataPageList;
import com.sibvisions.rad.ui.javafx.ext.control.DataPageList.FetchMode;
import com.sibvisions.rad.ui.javafx.ext.panes.FXBorderPane;

/**
 * The {@link DataPageTreeItem} is a {@link TreeItem} extension that gets its
 * data and children from a {@link DataPageList}.
 * <p>
 * It also implements two mechanism to limit the amount of data displayed. The
 * first is the lazy loading of the {@link DataPageList} itself, which will
 * limit the amount of data fetched from the storage. The second is a limitation
 * on the number of children created from the provided data.
 * 
 * @author Robert Zenz
 */
public class DataPageTreeItem extends TreeItem<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The current {@link IDataBook} that is used as data source. */
	private IDataBook currentDataBook;
	
	/** The current {@link IDataPage} that is used as data source. */
	private IDataPage currentDataPage;
	
	/** The index of the current {@link IDataBook}. */
	private int dataBookIndex;
	
	/** The {@link List} of all {@link IDataBook}s. */
	private List<IDataBook> dataBooks;
	
	/** The backing {@link IDataPageList}. */
	private DataPageList dataPageList;
	
	/**
	 * The {@link FXBorderPane} that is used as container for the
	 * {@link #valueDisplayLabel} and {@link #imageDisplayRegion}.
	 */
	private FXBorderPane displayContainer;
	
	/** The amount of data to fetch with each batch. */
	private int fetchBatchSize;
	
	/** The {@link FXImageRegion} used for displaying the image. */
	private FXImageRegion imageDisplayRegion;
	
	/**
	 * If the amount of data that is directly converted to children should be
	 * limited.
	 * <p>
	 * If {@code true} the backing {@link DataPageList} can fetch more data than
	 * this {@link DataPageTreeItem} has children. The children will be expanded
	 * with each {@link #fetchNextBatch()} call until the moment there isn't
	 * enough data, at which more data will be fetched.
	 */
	private boolean limitFetchedChildCount;
	
	/** The index of the next {@link IDataBook}. */
	private int nextDataBookIndex;
	
	/** The next {@link IDataPage}. */
	private IDataPage nextDataPage;
	
	/** The parent {@link FXDataBooksTree}. */
	private FXDataBooksTree parentTree;
	
	/** The row index of this. */
	private int rowIndex;
	
	/** The {@link FXLabel} used for displaying the value. */
	private Label valueDisplayLabel;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataPageTreeItem}.
	 *
	 * @param pParentTree The parent {@link FXDataBooksTree}.
	 * @param pDataBooks the {@link List} of all {@link IDataBook}s.
	 * @param pDataBookIndex the index of the {@link IDataBook}.
	 * @param pDataPage the current {@link IDataPage} that is used as data
	 *            source..
	 * @param pRowIndex the row index.
	 */
	public DataPageTreeItem(FXDataBooksTree pParentTree, List<IDataBook> pDataBooks, int pDataBookIndex, IDataPage pDataPage, int pRowIndex)
	{
		super();
		
		currentDataPage = pDataPage;
		dataBooks = pDataBooks;
		dataBookIndex = pDataBookIndex;
		parentTree = pParentTree;
		rowIndex = pRowIndex;
		
		valueDisplayLabel = new Label();
		
		imageDisplayRegion = new FXImageRegion();
		
		displayContainer = new FXBorderPane();
		displayContainer.setCenter(valueDisplayLabel);
		displayContainer.setLeft(imageDisplayRegion);
		setGraphic(displayContainer);
		
		fetchBatchSize = 500;
		limitFetchedChildCount = false;
		
		currentDataBook = dataBooks.get(dataBookIndex);
		nextDataBookIndex = dataBookIndex;
		
		expandedProperty().addListener(this::onExpandedChanged);
		
		try
		{
			IDataRow primaryRow = currentDataPage.getDataRow(rowIndex);
			if (currentDataBook.isSelfJoined() && dataBookIndex == dataBooks.size() - 1)
			{
				nextDataPage = currentDataBook.getDataPage(primaryRow);
			}
			else if (dataBookIndex < dataBooks.size() - 1)
			{
				nextDataBookIndex = nextDataBookIndex + 1;
				IDataBook nextDataBook = dataBooks.get(nextDataBookIndex);
				nextDataPage = nextDataBook.getDataPage(primaryRow);
			}
		}
		catch (ModelException e)
		{
			// Ignore any exception.
		}
		
		if (nextDataPage != null)
		{
			dataPageList = new DataPageList(nextDataPage, FetchMode.MANUAL, fetchBatchSize);
		}
		
		updateValue();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeaf()
	{
		if (nextDataPage != null)
		{
			try
			{
				if (!nextDataPage.isAllFetched() && nextDataPage.getRowCount() == 0)
				{
					if (!parentTree.isLeafDetectionEnabled())
					{
						return false;
					}
					
					try
					{
						nextDataPage.getDataRow(0);
					}
					catch (ModelException innerE)
					{
						// Ignore this exception, it is if no interest to us.
					}
				}
				
				// Here we can safely say that we either have data, or we don't.
				return nextDataPage.getRowCount() == 0;
			}
			catch (ModelException e)
			{
				// Ignore any exception.
			}
		}
		
		// If there is no datapage we can safely assume that this node is a leaf.
		return true;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new {@link DataPageTreeItem child}.
	 * 
	 * @param pRowIndex the row index to use for the child.
	 * @return a new {@link DataPageTreeItem child}.
	 */
	public DataPageTreeItem createChild(int pRowIndex)
	{
		return new DataPageTreeItem(parentTree, dataBooks, nextDataBookIndex, nextDataPage, pRowIndex);
	}
	
	/**
	 * Fetches the next batch of data.
	 * 
	 * @return {@code true} if the amount of children changed.
	 */
	public boolean fetchNextBatch()
	{
		if (dataPageList != null)
		{
			List<TreeItem<String>> children = getChildren();
			
			int childrenSize = children.size();
			int nextSize = childrenSize + fetchBatchSize;
			
			if (nextSize > dataPageList.size() && !dataPageList.isAllFetched())
			{
				// Our next batch would request more data than the datapage
				// currently holds, also there isn't everything fetched by now,
				// so we go and get more data.
				dataPageList.fetchNextBatch();
			}
			
			// Determine how much of the data the datapage can provide should
			// be turned into children.
			int fetchUntil = dataPageList.getRowCount();
			if (limitFetchedChildCount)
			{
				fetchUntil = Math.min(nextSize, dataPageList.getRowCount());
			}
			
			// Now convert all that sweet data into children.
			for (int index = children.size(); index < fetchUntil; index++)
			{
				children.add(createChild(index));
			}
			
			return children.size() != childrenSize;
		}
		
		return false;
	}
	
	/**
	 * Gets the {@link IDataPage current data page}.
	 *
	 * @return the {@link IDataPage current data page}.
	 */
	public IDataPage getCurrentDataPage()
	{
		return currentDataPage;
	}
	
	/**
	 * Gets the {@link int data book index}.
	 *
	 * @return the {@link int data book index}.
	 */
	public int getDataBookIndex()
	{
		return dataBookIndex;
	}
	
	/**
	 * Gets the {@link List} of {@link IDataBook}s.
	 *
	 * @return the {@link List} of {@link IDataBook}s.
	 */
	public List<IDataBook> getDataBooks()
	{
		return dataBooks;
	}
	
	/**
	 * Gets the next {@link IDataBook} index.
	 * 
	 * @return the next {@link IDataBook} index.
	 */
	public int getNextDataBookIndex()
	{
		return nextDataBookIndex;
	}
	
	/**
	 * Gets the next {@link IDataPage}.
	 *
	 * @return the next {@link IDataPage}.
	 */
	public IDataPage getNextDataPage()
	{
		return nextDataPage;
	}
	
	/**
	 * Gets the current row index.
	 * 
	 * @return the current row index.
	 */
	public int getRowIndex()
	{
		return rowIndex;
	}
	
	/**
	 * Gets if everything is fetched.
	 * 
	 * @return {@code true} if everything is fetched.
	 */
	public boolean isAllFetched()
	{
		return dataPageList == null || dataPageList.isAllFetched();
	}
	
	/**
	 * Gets if all available data is turned into children.
	 * 
	 * @return {@code true} if all available data is turned into children.
	 */
	public boolean isAllItems()
	{
		return dataPageList == null || dataPageList.getRowCount() == getChildren().size();
	}
	
	/**
	 * Updates the value.
	 */
	public void updateValue()
	{
		try
		{
			ColumnView columnView = currentDataBook.getRowDefinition().getColumnView(ITree.class);
			String firstColumnName = columnView.getColumnName(0);
			
			IDataRow dataRow = currentDataPage.getDataRow(rowIndex);
			String value = dataRow.getValueAsString(firstColumnName);
			
			valueDisplayLabel.setText(value);
			setValue("");
			
			valueDisplayLabel.setStyle(null);
			imageDisplayRegion.setImage(null);
			FXBorderPane.setMargin(valueDisplayLabel, null);
			
			INodeFormatter nodeFormatter = parentTree.getNodeFormatter();
			if (nodeFormatter != null)
			{
				IImage image = nodeFormatter.getNodeImage(currentDataBook, currentDataPage, dataRow, firstColumnName, rowIndex, isExpanded(), isLeaf());
				if (image != null)
				{
					imageDisplayRegion.setImage((Image) image.getResource());
				}
			}
			
			ICellFormatter cellFormatter = parentTree.getCellFormatter();
			if (cellFormatter != null)
			{
				ICellFormat cellFormat = cellFormatter.getCellFormat(currentDataBook, currentDataPage, dataRow, firstColumnName, rowIndex, 0);
				if (cellFormat != null)
				{
					StyleContainer styleContainer = new StyleContainer();
					
					if (cellFormat.getBackground() != null)
					{
						styleContainer.setBackground((Color) cellFormat.getBackground().getResource());
					}
					if (cellFormat.getForeground() != null)
					{
						styleContainer.setForeground((Color) cellFormat.getForeground().getResource());
					}
					if (cellFormat.getFont() != null)
					{
						styleContainer.setFont((Font) cellFormat.getFont().getResource());
					}
					if (cellFormat.getImage() != null)
					{
						imageDisplayRegion.setImage((Image) cellFormat.getImage().getResource());
					}
					if (cellFormat.getLeftIndent() >= 0)
					{
						FXBorderPane.setMargin(valueDisplayLabel, new Insets(0, 0, 0, cellFormat.getLeftIndent()));
					}
					
					valueDisplayLabel.setStyle(styleContainer.getStyle());
				}
			}
			
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Invoked if the {@link #expandedProperty()} changes.
	 * <p>
	 * Makes sure that items are fetched the first time the item is expanded.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onExpandedChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			if (getChildren().isEmpty() && dataPageList.getRowCount() >= 0)
			{
				fetchNextBatch();
			}
		}
		
		updateValue();
	}
	
}	// DataPageTreeItem

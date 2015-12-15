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
package com.sibvisions.rad.ui.javafx.ext.control;

import java.util.AbstractList;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;

/**
 * The {@link DataPageList} is an {@link AbstractList} extension that is backed
 * by an {@link javax.rad.model.IDataBook} and holds the {@link IDataRow}s. It
 * is lazy, and can either automatically or manually fetch additional rows on
 * the fly.
 * 
 * @author Robert Zenz
 * @see IDataRow
 */
public class DataPageList extends AbstractList<IDataRow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The backing {@link IDataPage}. */
	protected IDataPage dataPage;
	
	/** The (default) size of the next batch of data to get. */
	protected int fetchBatchSize;
	
	/** The {@link FetchMode}. */
	private FetchMode fetchMode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataPageList}, that automatically
	 * fetches data.
	 *
	 * @param pDataPage the backing {@link IDataPage}.
	 */
	public DataPageList(IDataPage pDataPage)
	{
		this(pDataPage, FetchMode.AUTOMATIC, 500);
	}
	
	/**
	 * Creates a new instance of {@link DataPageList}.
	 *
	 * @param pDataPage the backing {@link IDataPage}.
	 * @param pFetchMode the {@link FetchMode}.
	 * @param pFetchBatchSize the (default) size of the batches of data to
	 *            fetch.
	 */
	public DataPageList(IDataPage pDataPage, FetchMode pFetchMode, int pFetchBatchSize)
	{
		super();
		
		dataPage = pDataPage;
		fetchMode = pFetchMode;
		fetchBatchSize = pFetchBatchSize;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataRow get(int pIndex)
	{
		try
		{
			if (fetchMode == FetchMode.AUTOMATIC)
			{
				int rowCount = dataPage.getRowCount();
				
				if (dataPage.isAllFetched())
				{
					if (pIndex < rowCount)
					{
						return dataPage.getDataRow(pIndex);
					}
					else
					{
						return dataPage.getDataRow(rowCount - 1);
					}
				}
				else
				{
					try
					{
						return dataPage.getDataRow(pIndex);
					}
					catch (ModelException e)
					{
						// Getting the row failed, given that we only wanted it
						// to fetch to the given index, we'll ignore any exception
						// that has occurred.
					}
					
					return dataPage.getDataRow(dataPage.getRowCount() - 1);
				}
			}
			else
			{
				return dataPage.getDataRow(pIndex);
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		try
		{
			if (fetchMode == FetchMode.AUTOMATIC)
			{
				if (dataPage.isAllFetched())
				{
					return dataPage.getRowCount();
				}
				else
				{
					// Return a bigger row count so that the rest gets fetched.
					return dataPage.getRowCount() + fetchBatchSize;
				}
			}
			else
			{
				return dataPage.getRowCount();
			}
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
	 * Fetches the next batch of data.
	 *
	 * @return {@code true} if there was data to be fetched.
	 */
	public boolean fetchNextBatch()
	{
		return fetchNextBatch(fetchBatchSize);
	}
	
	/**
	 * Fetches the next batch of data.
	 *
	 * @param pBatchSize the size of the batch of data to fetch.
	 * @return {@code true} if there was data to be fetched.
	 */
	public boolean fetchNextBatch(int pBatchSize)
	{
		try
		{
			if (dataPage.isAllFetched())
			{
				return false;
			}
			
			dataPage.getDataRow(dataPage.getRowCount() + pBatchSize);
		}
		catch (ModelException e)
		{
			// Getting the row failed, given that we only wanted it
			// to fetch more rows, we ignore the exception.
		}
		
		return true;
	}
	
	/**
	 * Gets the backing {@link IDataPage}.
	 * 
	 * @return the backing {@link IDataPage}.
	 */
	public IDataPage getDataPage()
	{
		return dataPage;
	}
	
	/**
	 * Gets the {@link FetchMode fetch mode}.
	 *
	 * @return the {@link FetchMode fetch mode}.
	 */
	public FetchMode getFetchMode()
	{
		return fetchMode;
	}
	
	/**
	 * Gets the {@link IDataPage#getRowCount() row count} of the backing
	 * {@link IDataPage}.
	 * 
	 * @return the {@link IDataPage#getRowCount() row count} of the backing
	 *         {@link IDataPage}.
	 */
	public int getRowCount()
	{
		try
		{
			return dataPage.getRowCount();
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets if all data has been fetched.
	 *
	 * @return {@code true} if all data has been fetched.
	 */
	public boolean isAllFetched()
	{
		try
		{
			return dataPage.isAllFetched();
		}
		catch (ModelException e)
		{
			// Ignore any exception, we really do not care in this case.
		}
		
		return true;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The fetch mode.
	 * 
	 * @author Robert Zenz
	 */
	public enum FetchMode
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * In automatic mode, the {@link DataPageList} will automatically fetch
		 * data as needed. It might report sizes that are too big.
		 */
		AUTOMATIC,
		
		/**
		 * In manual mode, the {@link DataPageList} does only fetch data if it
		 * has been {@link DataPageList#fetchNextBatch() request to do so}.
		 */
		MANUAL
		
	}	// FetchMode
	
}	// DataPageList

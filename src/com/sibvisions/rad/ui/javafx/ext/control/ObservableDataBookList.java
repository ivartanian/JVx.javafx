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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.event.IDataRowListener;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.NonIterableChange;

/**
 * The {@link ObservableDataBookList} is an extension of the
 * {@link DataPageList} which implements {@link ObservableList}.
 * 
 * @author Robert Zenz
 * @see DataPageList
 * @see ObservableList
 */
public class ObservableDataBookList extends DataPageList implements ObservableList<IDataRow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The backing {@link IDataBook}. */
	protected IDataBook dataBook;
	
	/**
	 * The {@link IDataBookListener} that is used for listening for any changes
	 * in the backing {@link DataPageList#dataPage}.
	 */
	protected IDataBookListener dataBookListener;
	
	/**
	 * The {@link IDataRowListener} that listens for value changes in the
	 * backing {@link DataPageList#dataPage}.
	 */
	protected IDataRowListener dataRowListener;
	
	/** The {@link ListListenerHelper} used for the events. */
	protected ListListenerHelper<IDataRow> listListenerHelper;
	
	/**
	 * The property used for determining if the listeners should be attached to
	 * the {@link #dataBook} or not.
	 */
	private BooleanProperty automaticUpdates;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ObservableDataBookList}.
	 *
	 * @param pDataBook the backing {@link IDataBook}.
	 */
	public ObservableDataBookList(IDataBook pDataBook)
	{
		this(pDataBook, FetchMode.AUTOMATIC, 500);
	}
	
	/**
	 * Creates a new instance of {@link ObservableDataBookList}.
	 *
	 * @param pDataBook the backing {@link IDataBook}.
	 * @param pFetchMode the fetch mode.
	 * @param pFetchBatchSize the fetch batch size.
	 */
	public ObservableDataBookList(IDataBook pDataBook, FetchMode pFetchMode, int pFetchBatchSize)
	{
		super(pDataBook, pFetchMode, pFetchBatchSize);
		
		dataBookListener = this::onDataBookEvent;
		dataRowListener = this::onDataRowEvent;
		
		automaticUpdates = new SimpleBooleanProperty(true);
		automaticUpdates.addListener(this::onAutomaticUpdatesChanged);
		
		dataBook = pDataBook;
		
		attachListeners();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(IDataRow... pElements)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListener(InvalidationListener pListener)
	{
		listListenerHelper = ListListenerHelper.addListener(listListenerHelper, pListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListener(ListChangeListener<? super IDataRow> pListener)
	{
		listListenerHelper = ListListenerHelper.addListener(listListenerHelper, pListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pFrom, int pTo)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(IDataRow... pElements)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeListener(InvalidationListener pListener)
	{
		listListenerHelper = ListListenerHelper.removeListener(listListenerHelper, pListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeListener(ListChangeListener<? super IDataRow> pListener)
	{
		listListenerHelper = ListListenerHelper.removeListener(listListenerHelper, pListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(IDataRow... pElements)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setAll(Collection<? extends IDataRow> pCol)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setAll(IDataRow... pElements)
	{
		throw new UnsupportedOperationException();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean fetchNextBatch(int pBatchSize)
	{
		int previousRowCount = getRowCount();
		
		boolean fetched = super.fetchNextBatch(pBatchSize);
		
		if (fetched)
		{
			int newRowCount = getRowCount();
			
			fireAddEvent(previousRowCount - 1, newRowCount);
		}
		
		return fetched;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Attaches all listeners to the {@link #dataBook}.
	 */
	protected void attachListeners()
	{
		dataBook.eventAfterInserted().addListener(dataBookListener);
		dataBook.eventAfterDeleted().addListener(dataBookListener);
		dataBook.eventAfterUpdated().addListener(dataBookListener);
		dataBook.eventAfterReload().addListener(dataBookListener);
		dataBook.eventAfterRestore().addListener(dataBookListener);
		
		dataBook.eventValuesChanged().addListener(dataRowListener);
	}
	
	/**
	 * Gets the property for if automatic updates of the list are enabled.
	 * <p>
	 * The {@link ObservableDataBookList} has the possibility to listen to
	 * events of the {@link IDataBook} and notify all listeners of the changes.
	 * 
	 * @return the property for if automatic updates of the list are enabled.
	 * @see #isAutomaticUpdates()
	 * @see #setAutomaticUpdates(boolean)
	 */
	protected BooleanProperty automaticUpdatesProperty()
	{
		return automaticUpdates;
	}
	
	/**
	 * Fires and {@link AddChange} event for the given range.
	 * 
	 * @param pFromIndex the index at which the change starts (inclusive).
	 * @param pToIndex the index at which the change ends (exclusive).
	 */
	protected void fireAddEvent(int pFromIndex, int pToIndex)
	{
		fireChangedEvent(new AddChange(this, pFromIndex, pToIndex));
	}
	
	/**
	 * Fires an add/remove event with the given range.
	 * 
	 * @param pFromIndex the index at which the change starts (inclusive).
	 * @param pToIndex the index at which the change ends (exclusive).
	 */
	protected void fireAddRemovedEvent(int pFromIndex, int pToIndex)
	{
		fireChangedEvent(new NonIterableChange.GenericAddRemoveChange<>(pFromIndex, pToIndex, Collections.emptyList(), this));
	}
	
	/**
	 * Fires the given {@link Change} as event.
	 * 
	 * @param pChange the {@link Change}.
	 */
	protected void fireChangedEvent(Change<IDataRow> pChange)
	{
		ListListenerHelper.fireValueChangedEvent(listListenerHelper, pChange);
	}
	
	/**
	 * Fires a changed event with the given range.
	 * 
	 * @param pFromIndex the index at which the change starts (inclusive).
	 * @param pToIndex the index at which the change ends (exclusive).
	 */
	protected void fireChangedEvent(int pFromIndex, int pToIndex)
	{
		fireChangedEvent(new NonIterableChange.SimpleUpdateChange<>(pFromIndex, pToIndex, this));
	}
	
	/**
	 * Gets if the automatic updates are enabled.
	 * 
	 * @return {@code true} if automatic updates are enabled.
	 * @see #automaticUpdatesProperty()
	 * @see #setAutomaticUpdates(boolean)
	 */
	protected boolean isAutomaticUpdates()
	{
		return automaticUpdates.get();
	}
	
	/**
	 * Removes all listeners to the {@link #dataBook}.
	 */
	protected void removeListeners()
	{
		dataBook.eventAfterInserted().removeListener(dataBookListener);
		dataBook.eventAfterDeleted().removeListener(dataBookListener);
		dataBook.eventAfterUpdated().removeListener(dataBookListener);
		dataBook.eventAfterReload().removeListener(dataBookListener);
		dataBook.eventAfterRestore().removeListener(dataBookListener);
		
		dataBook.eventValuesChanged().removeListener(dataRowListener);
	}
	
	/**
	 * Sets if automatic updates are enabled.
	 * 
	 * @param pAutomaticUpdates {@code true} if automatic updates should be
	 *            enabled.
	 * @see #automaticUpdatesProperty()
	 * @see #isAutomaticUpdates()
	 */
	protected void setAutomaticUpdates(boolean pAutomaticUpdates)
	{
		automaticUpdates.set(pAutomaticUpdates);
	}
	
	/**
	 * Invoked if the {@link #automaticUpdates} property changes.
	 * <p>
	 * Attaches or removes the listeners.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onAutomaticUpdatesChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			attachListeners();
		}
		else
		{
			removeListeners();
		}
	}
	
	/**
	 * Invoked if there are any changes in the {@link DataPageList#dataPage}.
	 * 
	 * @param pDataBookEvent the event.
	 */
	private void onDataBookEvent(DataBookEvent pDataBookEvent)
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
	
	/**
	 * Invoked if there are any value changes in the
	 * {@link DataPageList#dataPage}.
	 * 
	 * @param pDataRowEvent the event.
	 */
	private void onDataRowEvent(DataRowEvent pDataRowEvent)
	{
		try
		{
			fireChangedEvent(dataBook.getSelectedRow(), dataBook.getSelectedRow());
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple {@link Change} extension for if items have been added.
	 * 
	 * @author Robert Zenz
	 */
	protected static final class AddChange extends Change<IDataRow>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The index at which the change starts (inclusive). */
		private int fromIndex;
		
		/** If the (one and only) change is selected. */
		private boolean isOnChange;
		
		/** The index at which the change ends (exclusive). */
		private int toIndex;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link AddChange}.
		 *
		 * @param pList the list.
		 * @param pFromIndex the index at which the change starts (inclusive).
		 * @param pToIndex the index at which the change ends (exclusive).
		 */
		public AddChange(ObservableList<IDataRow> pList, int pFromIndex, int pToIndex)
		{
			super(pList);
			
			fromIndex = pFromIndex;
			toIndex = pToIndex;
			
			isOnChange = false;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getFrom()
		{
			return fromIndex;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<IDataRow> getRemoved()
		{
			return Collections.emptyList();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getTo()
		{
			return toIndex;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean next()
		{
			if (!isOnChange)
			{
				isOnChange = true;
				
				return true;
			}
			
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reset()
		{
			isOnChange = false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected int[] getPermutation()
		{
			return new int[] {};
		}
		
	}	// AddChange
	
}	// ObservableDataBookList

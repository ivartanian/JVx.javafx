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

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.ui.IControl;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.javafx.ext.control.util.FXNotifyHelper;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXTranslationHelper;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;
import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * The {@link FXDataBooksTree} is a {@link TreeView} extension that can be bound
 * against {@link IDataBook}s.
 * <p>
 * It supports either one, multiple or self-joined {@link IDataBook}s as
 * backend.
 * 
 * @author Robert Zenz
 */
public class FXDataBooksTree extends TreeView<String> implements IControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property for the active {@link IDataBook}. */
	private ObjectProperty<IDataBook> activeDataBook;
	
	/** The property for the {@link ICellFormatter}. */
	private ObjectProperty<ICellFormatter> cellFormatter;
	
	/** The {@link IDataBook}s that will be used. */
	private ListProperty<IDataBook> dataBooks;
	
	/**
	 * The {@link IDataBookListener} that is attach to the
	 * {@link IDataBook#eventAfterRowSelected() after row selected event}.
	 */
	private IDataBookListener dataBookAfterRowSelectedListener;
	
	/**
	 * The {@link ListChangeListener} that is attached to the {@link #dataBooks
	 * list of databooks}.
	 */
	private ListChangeListener<IDataBook> dataBooksListChangeListener;
	
	/** If any events should be ignored. */
	private boolean ignoreEvents;
	
	/** If the next event of the scrollbar should be ignored. */
	private boolean ignoreNextScrollBarValueChange;
	
	/** The property for if leaf detection is enabled. */
	private SimpleBooleanProperty leafDetection;
	
	/** The property for the {@link INodeFormatter}. */
	private ObjectProperty<INodeFormatter> nodeFormatter;
	
	/** The {@link FXNotifyHelper} that is used. */
	private FXNotifyHelper notify;
	
	/** If a complete rebuild of the control is necessary. */
	private boolean rebuildNeeded;
	
	/** The {@link FXTranslationHelper} that is used. */
	private FXTranslationHelper translation;
	
	/** The vertical {@link ScrollBar} of this control. */
	private ScrollBar verticalScrollBar;
	
	/** The vertical {@link VirtualFlow} of this control. */
	private VirtualFlow<TreeCell<?>> virtualFlow;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDataBooksTree}.
	 */
	public FXDataBooksTree()
	{
		super();
		
		dataBookAfterRowSelectedListener = this::onDataBookAfterRowSelectedChanged;
		dataBooksListChangeListener = this::onDataBooksListChanged;
		
		activeDataBook = new SimpleObjectProperty<>();
		cellFormatter = new SimpleObjectProperty<>();
		ignoreEvents = false;
		ignoreNextScrollBarValueChange = false;
		leafDetection = new SimpleBooleanProperty();
		nodeFormatter = new SimpleObjectProperty<>();
		notify = new FXNotifyHelper(this::reload);
		translation = new FXTranslationHelper();
		
		dataBooks = new SimpleListProperty<>(FXCollections.observableArrayList());
		dataBooks.addListener(this::onDataBooksChanged);
		dataBooks.get().addListener(dataBooksListChangeListener);
		
		getSelectionModel().selectedItemProperty().addListener(this::onSelectionChanged);
		
		setEditable(false);
		setRoot(new TreeItem<>("ROOT"));
		setShowRoot(false);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelEditing()
	{
		reload();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		return translation.getMap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTranslationEnabled()
	{
		return translation.isEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyRepaint()
	{
		// notifyRepaint might be called when the selection changed,
		// but we should not rebuild the tree in that case.
		if (!ignoreEvents)
		{
			notify.notifyRepaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveEditing() throws ModelException
	{
		// The tree can't be edited, so changes can't be saved.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		translation.setMap(pTranslation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslationEnabled(boolean pEnabled)
	{
		translation.setEnabled(pEnabled);
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public String translate(String pText)
	{
		return translation.translate(pText);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for the active {@link IDataBook}.
	 * 
	 * @return the property for the active {@link IDataBook}.
	 * @see #getActiveDataBook()
	 */
	public ReadOnlyObjectProperty<IDataBook> activeDataBookProperty()
	{
		return activeDataBook;
	}
	
	/**
	 * The property for the {@link ICellFormatter}.
	 * 
	 * @return the property for the {@link ICellFormatter}.
	 */
	public ObjectProperty<ICellFormatter> cellFormatterProperty()
	{
		return cellFormatter;
	}
	
	/**
	 * Gets the property for the {@link IDataBook}s.
	 * 
	 * @return the property for the {@link IDataBook}s.
	 */
	public ListProperty<IDataBook> dataBooksProperty()
	{
		return dataBooks;
	}
	
	/**
	 * Gets the currently active {@link IDataBook}.
	 * 
	 * @return the currently active {@link IDataBook}. {@code null} if there is
	 *         none.
	 * @see #activeDataBook
	 */
	public IDataBook getActiveDataBook()
	{
		return activeDataBook.get();
	}
	
	/**
	 * Gets the {@link ICellFormatter}.
	 * 
	 * @return the {@link ICellFormatter}.
	 */
	public ICellFormatter getCellFormatter()
	{
		return cellFormatter.get();
	}
	
	/**
	 * Gets the {@link List} of {@link IDataBook}s.
	 * 
	 * @return the {@link List} of {@link IDataBook}s.
	 */
	public ObservableList<IDataBook> getDataBooks()
	{
		return dataBooks.get();
	}
	
	/**
	 * Gets the {@link INodeFormatter}.
	 * 
	 * @return the {@link INodeFormatter}.
	 */
	public INodeFormatter getNodeFormatter()
	{
		return nodeFormatter.get();
	}
	
	/**
	 * Gets if leaf detection is enabled.
	 * 
	 * @return {@code true} if leaf detection is enabled.
	 * @see #leafDetectionEnabledProperty()
	 * @see #setLeafDetectionEnabled(boolean)
	 */
	public boolean isLeafDetectionEnabled()
	{
		return leafDetection.get();
	}
	
	/**
	 * Gets the property for if leaf detection is enabled.
	 * <p>
	 * If leaf detection is enabled data will be prefetched to determine if the
	 * node is a leaf or not. If it is disabled, all nodes will appear as nodes
	 * instead of leafs.
	 * 
	 * @return the property for if the leaf detection is enabled.
	 * @see #isLeafDetectionEnabled()
	 * @see #setLeafDetectionEnabled(boolean)
	 */
	public BooleanProperty leafDetectionEnabledProperty()
	{
		return leafDetection;
	}
	
	/**
	 * The property for the {@link INodeFormatter}.
	 * 
	 * @return the property for the {@link INodeFormatter}.
	 */
	public ObjectProperty<INodeFormatter> nodeFormatterProperty()
	{
		return nodeFormatter;
	}
	
	/**
	 * Sets the {@link ICellFormatter}.
	 * 
	 * @param pFormatter the {@link ICellFormatter}.
	 */
	public void setCellFormatter(ICellFormatter pFormatter)
	{
		cellFormatter.set(pFormatter);
	}
	
	/**
	 * Sets the {@link List} of {@link IDataBook}s.
	 * 
	 * @param pDataBooks the {@link List} of {@link IDataBook}s.
	 */
	public void setDataBooks(ObservableList<IDataBook> pDataBooks)
	{
		dataBooks.set(pDataBooks);
	}
	
	/**
	 * Sets if the leaf detection is enabled.
	 * 
	 * @param pEnabled {@code true} if leaf detection should be enabled.
	 * @see #isLeafDetectionEnabled()
	 * @see #leafDetectionEnabledProperty()
	 */
	public void setLeafDetectionEnabled(boolean pEnabled)
	{
		leafDetection.set(pEnabled);
	}
	
	/**
	 * Sets the {@link INodeFormatter}.
	 * 
	 * @param pFormatter the {@link INodeFormatter}.
	 */
	public void setNodeFormatter(INodeFormatter pFormatter)
	{
		nodeFormatter.set(pFormatter);
	}
	
	/**
	 * Gets the path of the given {@link TreeItem}.
	 * 
	 * @param pTreeItem the {@link TreeItem}.
	 * @return the path to the given {@link TreeItem}.
	 */
	private List<TreeItem<?>> getPath(TreeItem<?> pTreeItem)
	{
		List<TreeItem<?>> items = new ArrayList<>();
		
		TreeItem<?> currentTreeItem = pTreeItem;
		
		while (currentTreeItem != null && getRoot() != currentTreeItem)
		{
			items.add(0, currentTreeItem);
			currentTreeItem = currentTreeItem.getParent();
		}
		
		return items;
	}
	
	/**
	 * Gets the number of {@link TreeItem}s that could be visible.
	 * 
	 * @return the number of {@link TreeItem}s that could be visible.
	 */
	private int getTotalTreeItemCount()
	{
		return getTotalTreeItemCount(getRoot(), 0);
	}
	
	/**
	 * Gets the number of {@link TreeItem}s that could be visible.
	 * 
	 * @param pStartItem the {@link TreeItem} at which to start.
	 * @param pPreviousCount the already counted count.
	 * @return the number of {@link TreeItem}s that could be visible.
	 */
	private int getTotalTreeItemCount(TreeItem<?> pStartItem, int pPreviousCount)
	{
		int currentCount = pPreviousCount;
		currentCount++;
		
		if (pStartItem.isExpanded())
		{
			for (TreeItem<?> child : pStartItem.getChildren())
			{
				currentCount = getTotalTreeItemCount(child, currentCount);
			}
		}
		
		return currentCount;
	}
	
	/**
	 * Invoked if the {@link #dataBooks} changes.
	 * <p>
	 * Will remove and add the needed listeners and rebuild the control.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onDataBooksChanged(ObservableValue<? extends ObservableList<IDataBook>> pObservable, ObservableList<IDataBook> pOldValue, ObservableList<IDataBook> pNewValue)
	{
		if (pOldValue != null)
		{
			pOldValue.removeListener(dataBooksListChangeListener);
			
			for (IDataBook dataBook : pOldValue)
			{
				dataBook.eventAfterRowSelected().removeListener(dataBookAfterRowSelectedListener);
				dataBook.removeControl(this);
			}
		}
		
		if (pNewValue != null)
		{
			pNewValue.addListener(dataBooksListChangeListener);
			
			for (IDataBook dataBook : pOldValue)
			{
				dataBook.addControl(this);
				dataBook.eventAfterRowSelected().addListener(dataBookAfterRowSelectedListener);
			}
		}
		
		rebuildNeeded = true;
		notifyRepaint();
	}
	
	/**
	 * Invoked after a row has been selected in the {@link IDataBook}.
	 * <p>
	 * Updates the selection.
	 * 
	 * @param pDataBookEvent the event.
	 */
	private void onDataBookAfterRowSelectedChanged(DataBookEvent pDataBookEvent)
	{
		if (!ignoreEvents)
		{
			ignoreEvents = true;
			updateSelectionFromDataBook(pDataBookEvent.getChangedDataBook());
			ignoreEvents = false;
		}
	}
	
	/**
	 * Invoked if the {@link List} of {@link IDataBooks} changes.
	 * <p>
	 * Removes and add this control from the {@link IDataBook}s and also
	 * rebuilds this control.
	 * 
	 * @param pChange the change.
	 */
	private void onDataBooksListChanged(Change<? extends IDataBook> pChange)
	{
		while (pChange.next())
		{
			for (IDataBook dataBook : pChange.getAddedSubList())
			{
				dataBook.addControl(this);
			}
			
			for (IDataBook dataBook : pChange.getRemoved())
			{
				dataBook.removeControl(this);
			}
		}
		
		rebuildNeeded = true;
		notifyRepaint();
	}
	
	/**
	 * Invoked if the mouse button is released on then
	 * {@link #verticalScrollBar}.
	 * <p>
	 * Fetches additional data if needed.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onScrollBarMouseReleased(MouseEvent pMouseEvent)
	{
		int itemCountBefore = getTotalTreeItemCount();
		
		updateTreeItems();
		
		int itemCountAfter = getTotalTreeItemCount();
		
		if (itemCountBefore != itemCountAfter)
		{
			ignoreNextScrollBarValueChange = true;
			verticalScrollBar.setValue((double) itemCountBefore / itemCountAfter);
		}
	}
	
	/**
	 * Invoked if the {@link #verticalScrollBar} changes it value.
	 * <p>
	 * Fetches additional data if needed.
	 * 
	 * @param pObservableValue the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onScrollBarValueChanged(ObservableValue<? extends Number> pObservableValue, Number pOldValue, Number pNewValue)
	{
		if (!ignoreNextScrollBarValueChange)
		{
			updateTreeItems();
		}
		
		ignoreNextScrollBarValueChange = false;
	}
	
	/**
	 * Invoked if the selection of this control changes.
	 * <p>
	 * Propagates the new selection to all {@link IDataBook}s.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectionChanged(ObservableValue<? extends TreeItem<?>> pObservable, TreeItem<?> pOldValue, TreeItem<?> pNewValue)
	{
		if (!ignoreEvents)
		{
			if (pNewValue != null)
			{
				ignoreEvents = true;
				
				List<TreeItem<?>> itemPath = getPath(pNewValue);
				
				try
				{
					TreePath treePath = new TreePath();
					
					for (int index = 0; index < itemPath.size(); index++)
					{
						DataPageTreeItem treeItem = (DataPageTreeItem) itemPath.get(index);
						
						IDataBook dataBook = null;
						
						if (index >= dataBooks.size())
						{
							dataBook = dataBooks.get(dataBooks.size() - 1);
						}
						else
						{
							dataBook = dataBooks.get(index);
						}
						
						if (dataBook.isSelfJoined())
						{
							dataBook.setTreePath(treePath);
							treePath = treePath.getChildPath(treeItem.getRowIndex());
						}
						
						dataBook.setSelectedDataPageRow(treeItem.getRowIndex());
					}
				}
				catch (ModelException e)
				{
					ExceptionHandler.raise(e);
				}
				
				if (pNewValue instanceof DataPageTreeItem)
				{
					int dataBookIndex = ((DataPageTreeItem) pNewValue).getDataBookIndex();
					
					if (dataBookIndex >= dataBooks.size())
					{
						activeDataBook.set(dataBooks.get(dataBooks.size() - 1));
					}
					else
					{
						activeDataBook.set(dataBooks.get(dataBookIndex));
					}
				}
				else
				{
					activeDataBook.set(null);
				}
				
				// TODO Hack with runLater to make sure that the correct item is selected.
				Platform.runLater(() ->
				{
					ignoreEvents = false;
				});
			}
			else
			{
				activeDataBook.set(null);
			}
		}
	}
	
	/**
	 * Rebuilds the complete control.
	 */
	@SuppressWarnings("unchecked")
	private void rebuild()
	{
		if (verticalScrollBar == null)
		{
			verticalScrollBar = NodeUtil.lookup(this, ScrollBar.class, ".scroll-bar", (scrollBar) -> scrollBar.getOrientation() == Orientation.VERTICAL);
			virtualFlow = NodeUtil.lookup(this, VirtualFlow.class, ".virtual-flow", null);
			
			if (verticalScrollBar != null)
			{
				verticalScrollBar.valueProperty().addListener(this::onScrollBarValueChanged);
				verticalScrollBar.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onScrollBarMouseReleased);
			}
		}
		
		IDataBook dataBook = dataBooks.get(0);
		
		try
		{
			List<TreeItem<String>> items = getRoot().getChildren();
			
			for (int index = 0; index < dataBook.getRowCount(); index++)
			{
				items.add(new DataPageTreeItem(this, dataBooks, 0, dataBook, index));
			}
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
		
		ignoreEvents = true;
		updateSelectionFromDataBook(dataBooks.get(dataBooks.size() - 1));
		ignoreEvents = false;
	}
	
	/**
	 * Reloads the control if needed, otherwise performs only an update.
	 */
	private void reload()
	{
		if (dataBooks.isEmpty()) // Also does null check.
		{
			return;
		}
		
		for (IDataBook dataBook : dataBooks)
		{
			if (!dataBook.isOpen())
			{
				return;
			}
		}
		
		if (rebuildNeeded)
		{
			rebuildNeeded = false;
			
			rebuild();
		}
		else
		{
			// TODO The amount of rows might have changed, how to handle that?
			updateValues(getRoot());
		}
	}
	
	/**
	 * Updates the selection of this control with the one from the given
	 * {@link IDataBook}.
	 * 
	 * @param pDataBook the {@link IDataBook}.
	 */
	private void updateSelectionFromDataBook(IDataBook pDataBook)
	{
		int selectedDataBookIndex = dataBooks.indexOf(pDataBook);
		
		try
		{
			TreeItem<String> lastItem = getRoot();
			
			for (int index = 0; index < selectedDataBookIndex; index++)
			{
				int selectedRow = dataBooks.get(index).getSelectedRow();
				lastItem = lastItem.getChildren().get(selectedRow);
				lastItem.setExpanded(true);
			}
			
			if (pDataBook.isSelfJoined())
			{
				TreePath treePath = pDataBook.getTreePath();
				
				for (int index = 0; index < treePath.length(); index++)
				{
					lastItem = lastItem.getChildren().get(treePath.get(index));
					lastItem.setExpanded(true);
				}
			}
			
			if (pDataBook.getSelectedRow() >= 0 && pDataBook.getSelectedRow() < lastItem.getChildren().size())
			{
				// TODO Why is it possible that the view does not have the same rows as the databook at this point?
				getSelectionModel().select(lastItem.getChildren().get(pDataBook.getSelectedRow()));
			}
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
	}
	
	/**
	 * Updates all tree items above the current visible one, fetching new items
	 * if needed.
	 */
	private void updateTreeItems()
	{
		TreeCell<?> treeCell = virtualFlow.getLastVisibleCell();
		
		if (treeCell != null)
		{
			TreeItem<?> treeItem = treeCell.getTreeItem();
			TreeItem<?> parent = treeItem.getParent();
			
			// Update all items above the current parent.
			int indexAboveParent = treeCell.getIndex() - parent.getChildren().size();
			updateTreeItemsAbove(indexAboveParent);
			
			if (parent instanceof DataPageTreeItem)
			{
				DataPageTreeItem parentDataPageTreeItem = (DataPageTreeItem) parent;
				
				int childrenRemaining = parentDataPageTreeItem.getChildren().size() - parentDataPageTreeItem.getChildren().indexOf(treeItem);
				
				if (childrenRemaining <= 25)
				{
					if (parentDataPageTreeItem.fetchNextBatch())
					{
						// Notify the tree that it needs to rebuild its list.
						fireEvent(new Event(TreeItem.expandedItemCountChangeEvent()));
					}
				}
			}
		}
	}
	
	/**
	 * Updates all tree items which are above the given index.
	 * 
	 * @param pAboveIndex the index.
	 */
	private void updateTreeItemsAbove(int pAboveIndex)
	{
		updateTreeItemsAbove(getRoot(), 0, pAboveIndex);
	}
	
	/**
	 * Updates all tree items which are above the given index, starts at the
	 * given item.
	 * 
	 * @param pStartItem the {@link TreeItem} at which to start.
	 * @param pStartIndex the index of the start item.
	 * @param pAboveIndex the end index.
	 * @return the last index.
	 */
	private int updateTreeItemsAbove(TreeItem<?> pStartItem, int pStartIndex, int pAboveIndex)
	{
		if (pStartIndex >= pAboveIndex)
		{
			return pStartIndex;
		}
		
		int currentIndex = pStartIndex;
		currentIndex++;
		
		if (pStartItem.isExpanded())
		{
			for (TreeItem<?> child : pStartItem.getChildren())
			{
				currentIndex = updateTreeItemsAbove(child, currentIndex, pAboveIndex);
			}
			
			if (pStartItem instanceof DataPageTreeItem)
			{
				DataPageTreeItem dataPageTreeItem = (DataPageTreeItem) pStartItem;
				
				if (!dataPageTreeItem.isAllItems() || !dataPageTreeItem.isAllFetched())
				{
					if (dataPageTreeItem.fetchNextBatch())
					{
						// Inform the tree that it needs to update. 
						fireEvent(new Event(TreeItem.expandedItemCountChangeEvent()));
					}
				}
			}
		}
		
		return currentIndex;
	}
	
	/**
	 * Updates all values of the given {@link TreeItem} and all its children.
	 * 
	 * @param pTreeItem the {@link TreeItem}.
	 */
	private void updateValues(TreeItem<?> pTreeItem)
	{
		if (pTreeItem instanceof DataPageTreeItem)
		{
			((DataPageTreeItem) pTreeItem).updateValue();
		}
		
		for (TreeItem<?> child : pTreeItem.getChildren())
		{
			updateValues(child);
		}
	}
	
}	// FXDataBooksTree

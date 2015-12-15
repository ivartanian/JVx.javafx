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

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

import com.sibvisions.rad.ui.javafx.ext.control.DataPageList.FetchMode;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXControlUtil;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXNotifyHelper;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXTranslationHelper;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link FXDataBookView} is a {@link TableView} extension that can be bound
 * against an {@link IDataBook} to display data.
 * 
 * @author Robert Zenz
 */
public class FXDataBookView extends TableView<IDataRow> implements IControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/control/table/css/fxdatabookview.css";
	
	/** The property for the {@link ICellFormatter}. */
	private ObjectProperty<ICellFormatter> cellFormatter;
	
	/** The {@link ColumnView} that should be used. */
	private ObjectProperty<ColumnView> columnView;
	
	/** The backing {@link IDataBook}. */
	private ObjectProperty<IDataBook> dataBook;
	
	/** The backing {@link DataBookViewList}. */
	private DataBookViewList dataBookViewList;
	
	/**
	 * The property used for if the selection should only be displayed and not
	 * changeable from the view.
	 */
	private BooleanProperty displaySelectionOnly;
	
	/** If there is currently an edit going on. */
	private BooleanProperty editing;
	
	/** The {@link DataRowCell} that is currently edited. */
	private ObjectProperty<DataRowCell> editingCell;
	
	/** If selection events should be ignored. */
	private boolean ignoreSelectionEvents;
	
	/** If the next event of the scrollbar should be ignored. */
	private boolean ignoreNextScrollBarValueChange;
	
	/** The {@link FXNotifyHelper} that is used. */
	private FXNotifyHelper notify;
	
	/** If the columns should be resized. */
	private boolean resizeColumns;
	
	/** If sorting by clicking the column headers is enabled. */
	private BooleanProperty sortingEnabled;
	
	/** The {@link FXTranslationHelper} that is used. */
	private FXTranslationHelper translation;
	
	/** The vertical {@link ScrollBar} of this control. */
	private ScrollBar verticalScrollBar;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDataBookView}.
	 */
	public FXDataBookView()
	{
		ignoreSelectionEvents = false;
		notify = new FXNotifyHelper(this::reload);
		translation = new FXTranslationHelper();
		
		setEditable(true);
		setFocusModel(new FXDataBookViewFocusModel(this));
		
		cellFormatter = new SimpleObjectProperty<>();
		
		columnView = new SimpleObjectProperty<>();
		
		dataBook = new SimpleObjectProperty<>();
		dataBook.addListener(this::onDataBookChanged);
		
		displaySelectionOnly = new SimpleBooleanProperty(false);
		
		editing = new SimpleBooleanProperty(false);
		
		editingCell = new SimpleObjectProperty<>(null);
		
		sortingEnabled = new SimpleBooleanProperty(true);
		sortingEnabled.addListener(this::onSortingEnabledChanged);
		
		widthProperty().addListener(this::onWidthChanged);
		
		getSelectionModel().setCellSelectionEnabled(true);
		getSelectionModel().selectedIndexProperty().addListener(this::onSelectionChanged);
		setColumnResizePolicy(new DataAwareConstrainedFillingResizePolicy());
	}
	
	/**
	 * Creates a new instance of {@link FXDataBookView}.
	 *
	 * @param pDataBook the backing {@link IDataBook}.
	 */
	public FXDataBookView(IDataBook pDataBook)
	{
		this();
		
		setDataBook(pDataBook);
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
		if (editing.get())
		{
			editingCell.get().cancelEdit();
			endEditing();
		}
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
		notify.notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveEditing() throws ModelException
	{
		if (editing.get())
		{
			editingCell.get().commitEdit(null);
			endEditing();
		}
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
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserAgentStylesheet()
	{
		return DEFAULT_STYLE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double pWidth)
	{
		// TODO HACK There is no way to get the height of a row.
		return 100;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		if (verticalScrollBar == null)
		{
			verticalScrollBar = NodeUtil.lookup(this, ScrollBar.class, ".scroll-bar", (scrollBar) -> scrollBar.getOrientation() == Orientation.VERTICAL);
			
			if (verticalScrollBar != null)
			{
				verticalScrollBar.valueProperty().addListener(this::onScrollBarValueChanged);
				verticalScrollBar.visibleProperty().addListener(this::onScrollBarVisibleChanged);
			}
		}
		
		super.layoutChildren();
		
		if (resizeColumns)
		{
			resizeColumns = false;
			
			if (getColumnResizePolicy() instanceof DataAwareConstrainedFillingResizePolicy)
			{
				((DataAwareConstrainedFillingResizePolicy) getColumnResizePolicy()).initialSizeColumns(this, 20);
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
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
	 * Gets the property for the {@link ColumnView}.
	 * 
	 * @return the property for the {@link ColumnView}.
	 */
	public ObjectProperty<ColumnView> columnViewProperty()
	{
		return columnView;
	}
	
	/**
	 * Gets the property for the backing {@link IDataBook}.
	 * 
	 * @return the property for the backing {@link IDataBook}.
	 */
	public ObjectProperty<IDataBook> dataBookProperty()
	{
		return dataBook;
	}
	
	/**
	 * Gets the property for if the selection of the {@link FXDataBookView}
	 * should be propagated to the {@link IDataBook} or not.
	 * 
	 * @return the property for if the selection of the {@link FXDataBookView}
	 *         should be propagated to the {@link IDataBook} or not.
	 * @see #isDisplaySelectionOnly()
	 * @see #setDisplaySelectionOnly(boolean)
	 */
	public BooleanProperty displaySelectionOnlyProperty()
	{
		return displaySelectionOnly;
	}
	
	/**
	 * Starts the edit at the currently selected position.
	 */
	public void edit()
	{
		@SuppressWarnings("unchecked")
		TablePosition<IDataRow, ?> focusedCell = getFocusModel().getFocusedCell();
		
		if (focusedCell != null)
		{
			edit(focusedCell.getRow(), focusedCell.getTableColumn());
		}
	}
	
	/**
	 * Gets the {@link ICellFormat} for the given row.
	 * 
	 * @param pDataRow the {@link IDataRow}.
	 * @param pTableColumn the {@link TableColumn}.
	 * @param pRowIndex the index of the row.
	 * @return the {@link ICellFormat}. {@code null} if there is either no
	 *         {@link #cellFormatterProperty() cell formatter} set or it did
	 *         return {@code null}.
	 */
	public ICellFormat getCellFormat(IDataRow pDataRow, TableColumn<?, ?> pTableColumn, int pRowIndex)
	{
		if (cellFormatter.get() != null)
		{
			String columnName = (String) pTableColumn.getUserData();
			int columnIndex = dataBook.get().getRowDefinition().getColumnDefinitionIndex(columnName);
			
			return cellFormatter.get().getCellFormat(dataBook.get(), dataBook.get(), pDataRow, columnName, pRowIndex, columnIndex);
		}
		
		return null;
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
	 * Gets the {@link ColumnView}.
	 * 
	 * @return the {@link ColumnView}.
	 */
	public ColumnView getColumnView()
	{
		return columnView.get();
	}
	
	/**
	 * Gets the backing {@link IDataBook}.
	 * 
	 * @return the backing {@link IDataBook}.
	 */
	public IDataBook getDataBook()
	{
		return dataBook.get();
	}
	
	/**
	 * Gets the width of the vertical {@link ScrollBar}.
	 * 
	 * @return the width of the vertical {@link ScrollBar}.
	 */
	public double getVerticalScrollBarWidth()
	{
		if (verticalScrollBar != null && verticalScrollBar.isVisible())
		{
			return verticalScrollBar.getWidth();
		}
		
		return 0;
	}
	
	/**
	 * Gets if the selection is only displayed and changes in the selection are
	 * not propagated to the {@link IDataBook}.
	 * 
	 * @return {@code true} if the selection is only displayed and changes in
	 *         the selection are not propagated to the {@link IDataBook}.
	 * @see #displaySelectionOnlyProperty()
	 * @see #setDisplaySelectionOnly(boolean)
	 */
	public boolean isDisplaySelectionOnly()
	{
		return displaySelectionOnly.get();
	}
	
	/**
	 * Gets if sorting by clicking on the column headers is enabled.
	 * 
	 * @return {@code true} if sorting by clicking on the column headers is
	 *         enabled.
	 */
	public boolean isSortingEnabled()
	{
		return sortingEnabled.get();
	}
	
	/**
	 * Resizes the columns.
	 */
	public void resizeColumns()
	{
		resizeColumns = true;
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
	 * Sets the {@link ColumnView}.
	 * 
	 * @param pColumnView the {@link ColumnView}.
	 */
	public void setColumnView(ColumnView pColumnView)
	{
		columnView.set(pColumnView);
	}
	
	/**
	 * Sets the backing {@link IDataBook}.
	 * 
	 * @param pDataBook the backing {@link IDataBook}.
	 */
	public void setDataBook(IDataBook pDataBook)
	{
		dataBook.set(pDataBook);
	}
	
	/**
	 * Sets if the selection is only displayed and changes in the selection are
	 * not propagated to the {@link IDataBook}.
	 * 
	 * @param pDisplaySelectionOnly {@code true} if the selection is only
	 *            displayed and changes in the selection are not propagated to
	 *            the {@link IDataBook}.
	 * @see #displaySelectionOnlyProperty()
	 * @see #isDisplaySelectionOnly()
	 */
	public void setDisplaySelectionOnly(boolean pDisplaySelectionOnly)
	{
		displaySelectionOnly.set(pDisplaySelectionOnly);
	}
	
	/**
	 * Sets if sorting by clicking on the column headers is enabled.
	 * 
	 * @param pEnabled {@code true} if sorting by clicking on the column headers
	 *            should be enabled.
	 */
	public void setSortingEnabled(boolean pEnabled)
	{
		sortingEnabled.set(pEnabled);
	}
	
	/**
	 * Gets the property for if sorting by clicking on the column headers should
	 * be enabled.
	 * 
	 * @return the property for if sorting by clicking on the column headers
	 *         should be enabled.
	 */
	public BooleanProperty sortingEnabledProperty()
	{
		return sortingEnabled;
	}
	
	/**
	 * Ends the current edit.
	 */
	protected void endEditing()
	{
		editing.set(false);
		editingCell.set(null);
	}
	
	/**
	 * Starts the edit on the given {@link DataRowCell}.
	 * 
	 * @param pEditingCell the edited {@link DataRowCell}.
	 */
	protected void startEditing(DataRowCell pEditingCell)
	{
		editing.set(true);
		editingCell.set(pEditingCell);
	}
	
	/**
	 * Creates a {@link TableColumn} from the given {@link ColumnDefinition}.
	 * 
	 * @param pColumnDefinition the {@link ColumnDefinition} to use as template.
	 * @return the {@link TableColumn}.
	 */
	private TableColumn<IDataRow, Object> createColumn(ColumnDefinition pColumnDefinition)
	{
		TableColumn<IDataRow, Object> column = new TableColumn<>();
		
		column.setEditable(true);
		if (!StringUtil.isEmpty(pColumnDefinition.getLabel()))
		{
			column.setText(pColumnDefinition.getLabel());
		}
		else
		{
			column.setText(pColumnDefinition.getName());
		}
		
		column.setPrefWidth(pColumnDefinition.getWidth());
		
		column.setUserData(pColumnDefinition.getName());
		
		if (FXControlUtil.isNumberColumn(pColumnDefinition))
		{
			column.getStyleClass().add("number-cell");
		}
		else if (FXControlUtil.isTimestampColumn(pColumnDefinition))
		{
			column.getStyleClass().add("timestamp-cell");
		}
		
		column.setCellFactory(DataRowCell.FACTORY);
		column.setSortable(sortingEnabled.get());
		column.setMinWidth(0);

		return column;
	}
	
	/**
	 * Creates/rebuilds all columns.
	 */
	private void createColumns()
	{
		IRowDefinition rowDefinition = dataBook.get().getRowDefinition();
		
		String[] columnNames = null;
		
		if (columnView.get() != null)
		{
			columnNames = columnView.get().getColumnNames();
		}
		else if (rowDefinition.getColumnView(ITableControl.class) != null)
		{
			columnNames = rowDefinition.getColumnView(ITableControl.class).getColumnNames();
		}
		else if (rowDefinition.getColumnView(null) != null)
		{
			columnNames = rowDefinition.getColumnView(null).getColumnNames();
		}
		else
		{
			columnNames = rowDefinition.getColumnNames();
		}
		
		for (String columnName : columnNames)
		{
			try
			{
				TableColumn<IDataRow, ?> column = createColumn(rowDefinition.getColumnDefinition(columnName));
				getColumns().add(column);
			}
			catch (ModelException e)
			{
				ExceptionHandler.raise(e);
			}
		}
		
		resizeColumns();
	}
	
	/**
	 * Invoked if the {@link #dataBook} changes.
	 * <p>
	 * Does all necessary step to unwire the old {@link IDataBook} and rebuild
	 * this control based on the new one.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onDataBookChanged(ObservableValue<? extends IDataBook> pObservable, IDataBook pOldValue, IDataBook pNewValue)
	{
		itemsProperty().set(null);
		
		getColumns().clear();
		
		if (pOldValue != null)
		{
			pOldValue.removeControl(this);
		}
		
		if (pNewValue != null)
		{
			pNewValue.addControl(this);
			
			createColumns();
			
			dataBookViewList = new DataBookViewList(this, pNewValue, FetchMode.MANUAL, 500);
			
			itemsProperty().set(dataBookViewList);
			
			updateSelectionFromDataBook();
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
			if (dataBookViewList.getRowCount() * (1 - pNewValue.doubleValue()) <= 25)
			{
				int rowCountBefore = dataBookViewList.getRowCount();
				
				if (dataBookViewList.fetchNextBatch())
				{
					int rowCountAfter = dataBookViewList.getRowCount();
					
					ignoreNextScrollBarValueChange = true;
					
					// TODO HACK runLater is used to set the value of the scrollbar.
					Platform.runLater(() ->
					{
						verticalScrollBar.setValue((double) rowCountBefore / rowCountAfter);
					});
				}
			}
		}
		
		ignoreNextScrollBarValueChange = false;
	}
	
	/**
	 * Invoked if the visible property of the vertical scrollbar changes.
	 * 
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onScrollBarVisibleChanged(ObservableValue<? extends Boolean> pObservableValue, Boolean pOldValue, Boolean pNewValue)
	{
		if (getColumnResizePolicy() instanceof DataAwareConstrainedFillingResizePolicy)
		{
			((DataAwareConstrainedFillingResizePolicy) getColumnResizePolicy()).resizeLastColumn(this);
		}
	}
	
	/**
	 * Invoked if the selected row changed.
	 * <p>
	 * Propagates the new selection to the backing {@link #dataBook}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectionChanged(ObservableValue<? extends Number> pObservable, Number pOldValue, Number pNewValue)
	{
		if (!ignoreSelectionEvents)
		{
			if (dataBook.get() != null && pNewValue != null && !pNewValue.equals(pOldValue) && !displaySelectionOnly.get())
			{
				ignoreSelectionEvents = true;
				
				try
				{
					saveEditing();
					
					if (dataBook.get().getSelectedRow() != pNewValue.intValue())
					{
						dataBook.get().setSelectedRow(pNewValue.intValue());
					}
				}
				catch (ModelException e)
				{
					// Make sure that the correct selection is restored.
					notifyRepaint();
					
					ExceptionHandler.show(e);
				}
				finally
				{
					ignoreSelectionEvents = false;
				}
			}
		}
	}
	
	/**
	 * If {@link #sortingEnabled} changes.
	 * <p>
	 * Propagates the new value to all columns.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSortingEnabledChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		boolean enabled = pNewValue.booleanValue();
		
		for (TableColumn<?, ?> column : getColumns())
		{
			column.setSortable(enabled);
		}
	}
	
	/**
	 * Invoked if the width changes.
	 * <p>
	 * Needed as fix for RT-40309, so that the columns can be sized correctly.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onWidthChanged(ObservableValue<? extends Number> pObservable, Number pOldValue, Number pNewValue)
	{
		// Fixes RT-40309. The delta of the default event is always zero if the table view is resized.
		if (getColumnResizePolicy() instanceof DataAwareConstrainedFillingResizePolicy)
		{
			double delta = pNewValue.doubleValue() - pOldValue.doubleValue();
			getColumnResizePolicy().call(new ResizeFeatures<>(this, null, Double.valueOf(delta)));
		}
	}
	
	/**
	 * Reloads this table.
	 */
	private void reload()
	{
		ignoreSelectionEvents = false;
		
		if (getItems() != null)
		{
			((DataBookViewList) getItems()).notifyChanged();
			updateSelectionFromDataBook();
		}
	}
	
	/**
	 * Updates the selection of this table with the one from the backing
	 * {@link #dataBook}.
	 */
	private void updateSelectionFromDataBook()
	{
		if (!ignoreSelectionEvents)
		{
			ignoreSelectionEvents = true;
			
			try
			{
				int rowIndex = dataBook.get().getSelectedRow();
				
				if (rowIndex >= 0 && !getSelectionModel().isSelected(rowIndex))
				{
					@SuppressWarnings("unchecked")
					TablePosition<IDataRow, ?> focusedPosition = getFocusModel().getFocusedCell();
					
					if (focusedPosition != null && focusedPosition.getTableColumn() != null)
					{
						getSelectionModel().clearAndSelect(rowIndex, focusedPosition.getTableColumn());
					}
					else if (!getColumns().isEmpty())
					{
						getSelectionModel().clearAndSelect(rowIndex, getColumns().get(0));
					}
					else
					{
						getSelectionModel().clearAndSelect(rowIndex);
					}
				}
			}
			catch (ModelException e)
			{
				ExceptionHandler.raise(e);
			}
			finally
			{
				ignoreSelectionEvents = false;
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link FXDataBookViewFocusModel} is a simple extension of the
	 * {@link TableViewFocusModel} that saves any edit if the selection changes.
	 * 
	 * @author Robert Zenz
	 */
	public static class FXDataBookViewFocusModel extends TableViewFocusModel<IDataRow>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The parent {@link FXDataBookView}. */
		private FXDataBookView dataBookView;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link FXDataBookViewFocusModel}.
		 *
		 * @param pDataBookView the parent {@link FXDataBookView}.
		 */
		public FXDataBookViewFocusModel(FXDataBookView pDataBookView)
		{
			super(pDataBookView);
			
			dataBookView = pDataBookView;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void focus(int pRow, TableColumn<IDataRow, ?> pColumn)
		{
			save();
			
			super.focus(pRow, pColumn);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void focus(int pIndex)
		{
			save();
			
			super.focus(pIndex);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Saves any edit.
		 */
		private void save()
		{
			if (dataBookView != null)
			{
				try
				{
					dataBookView.saveEditing();
				}
				catch (ModelException e)
				{
					ExceptionHandler.raise(e);
				}
			}
		}
		
	}	// FXDataBookViewFocusModel
	
}	// FXDataBookView

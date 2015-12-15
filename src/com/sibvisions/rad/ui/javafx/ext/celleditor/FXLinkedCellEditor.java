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

import java.util.List;
import java.util.Objects;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.OperatorCondition;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.ui.IDimension;
import javax.rad.ui.celleditor.ILinkedCellEditor;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import com.sibvisions.rad.ui.celleditor.AbstractLinkedCellEditor;
import com.sibvisions.rad.ui.javafx.ext.FXCustomComboBox;
import com.sibvisions.rad.ui.javafx.ext.IFXComboBoxPopupProvider;
import com.sibvisions.rad.ui.javafx.ext.control.table.FXDataBookView;
import com.sibvisions.rad.ui.javafx.ext.util.FXFrameWaitUtil;
import com.sibvisions.util.ArrayUtil;

/**
 * The {@link FXLinkedCellEditor} is the JavaFX specific implementation of
 * {@link ILinkedCellEditor}.
 * 
 * @author Robert Zenz
 * @see ILinkedCellEditor
 */
public class FXLinkedCellEditor extends AbstractLinkedCellEditor implements ICellRenderer<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXLinkedCellEditor}.
	 */
	public FXLinkedCellEditor()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link FXLinkedCellEditor}.
	 *
	 * @param pLinkReference the link reference.
	 */
	public FXLinkedCellEditor(ReferenceDefinition pLinkReference)
	{
		this();
		
		setLinkReference(pLinkReference);
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
		return new ComboBoxCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName, displayReferencedColumnName);
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
			return getDisplayValue(pDataRow, pColumnName);
		}
		catch (ModelException e)
		{
			// Ignore any exception.
		}
		
		return null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ComboBoxCellEditorHandler} is an
	 * {@link FXAbstractCellEditorHandler} that is specific for the
	 * {@link FXLinkedCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ComboBoxCellEditorHandler extends FXAbstractCellEditorHandler<FXCustomComboBox<String>, FXLinkedCellEditor> implements
			ICellEditorHandler<FXCustomComboBox<String>>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The search Columns. */
		private String[] additionalClearColumns;
		
		/** The column index. */
		private int columnIndex;
		
		/** The column names. */
		private String[] columnNames;
		
		/** The name of the displayed column. */
		private String displayColumnName;
		
		/** True, if it's the first editing started event. */
		private boolean firstEditingStarted = false;
		
		/** True, if it's the first editing started event. */
		private boolean popupChanged = false;
		
		/** The column name referenced by the edited column. */
		private String referencedColumnName;
		
		/** The column names referenced. */
		private String[] referencedColumnNames;
		
		/** The DataBook referenced by the edited column. */
		private IDataBook referencedDataBook;
		
		/** The referenced search Columns. */
		private String[] referencedSearchColumns;
		
		/** The search Columns. */
		private String[] searchColumns;
		
		/** The last value that was selected. */
		private String lastValue;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ComboBoxCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 * @param pDisplayColumnName the display column name.
		 */
		public ComboBoxCellEditorHandler(FXLinkedCellEditor pCellEditor, ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName, String pDisplayColumnName)
		{
			super(pCellEditor, pCellEditorListener, new FXCustomComboBox<>(), pDataRow, pColumnName);
			
			component.setEditable(true);
			component.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			component.setPopupProvider(new ReferencePopupProvider(this));
			component.showingProperty().addListener(this::onShowingChanged);
			component.valueProperty().addListener(this::onComponentValueChanged);
			
			registerFocusChangedListener();
			registerKeyEventFilter();
			
			displayColumnName = pDisplayColumnName;
			
			columnNames = cellEditor.linkReference.getColumnNames();
			referencedColumnNames = cellEditor.linkReference.getReferencedColumnNames();
			if (columnNames.length == 0 && referencedColumnNames.length == 1)
			{
				columnNames = new String[] { pColumnName };
			}
			
			columnIndex = ArrayUtil.indexOf(columnNames, columnName);
			if (columnIndex < 0)
			{
				throw new IllegalArgumentException("The edited column " + columnName + " has to be part of in column names list in LinkReference!");
			}
			else
			{
				referencedColumnName = referencedColumnNames[columnIndex];
			}
			
			if (cellEditor.searchColumnMapping == null)
			{
				searchColumns = null;
				referencedSearchColumns = null;
			}
			else
			{
				searchColumns = cellEditor.searchColumnMapping.getColumnNames();
				referencedSearchColumns = cellEditor.searchColumnMapping.getReferencedColumnNames();
			}
			
			referencedDataBook = cellEditor.linkReference.getReferencedDataBook();
			try
			{
				additionalClearColumns = searchForAdditionalClearColumns(dataRow.getRowDefinition(), columnNames,
						getConditionColumns(cellEditor.searchColumnMapping, cellEditor.additionalCondition));
						
				referencedDataBook.setReadOnly(cellEditor.isTableReadonly());
			}
			catch (ModelException pModelException)
			{
				// Do nothing, it only tries to set data book readonly.
			}
			referencedDataBook.setSelectionMode(IDataBook.SelectionMode.DESELECTED);
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
			component.setDisable(!shouldBeEnabled());
			
			lastValue = cellEditor.getDisplayValue(dataRow, columnName);
			
			setSelectedItem(lastValue);
			component.setValue(lastValue);
			
			if (!firstEditingStarted)
			{
				referencedDataBook.setFilter(getSearchCondition(null));
			}
			
			if (component.isFocused() && !firstEditingStarted)
			{
				FXFrameWaitUtil.runLater(() -> component.show());
			}
			
			firstEditingStarted = true;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			if (popupChanged && referencedDataBook.getSelectedRow() >= 0)
			{
				setValuesAndClearIfNecessary(referencedDataBook.getValues(referencedColumnNames));
			}
			else
			{
				// Make sure the correct selection is propagated.
				component.getPopupProvider().hidePopup();
				
				Object item = component.getValue();
				
				if (item == null || "".equals(item))
				{
					Object[] result = new Object[referencedColumnNames.length];
					
					setValuesAndClearIfNecessary(result);
				}
				else
				{
					referencedDataBook.setFilter(getSearchCondition(new Like(getRelevantSearchColumnName(), item)));
					if (referencedDataBook.getDataRow(0) != null && referencedDataBook.getDataRow(1) == null)
					{
						setValuesAndClearIfNecessary(referencedDataBook.getDataRow(0).getValues(referencedColumnNames));
					}
					else
					{
						if (cellEditor.isValidationEnabled())
						{
							referencedDataBook.setFilter(getSearchCondition(new LikeIgnoreCase(getRelevantSearchColumnName(), getWildCardString(item))));
							if (referencedDataBook.getDataRow(0) != null && referencedDataBook.getDataRow(1) == null)
							{
								setValuesAndClearIfNecessary(referencedDataBook.getDataRow(0).getValues(referencedColumnNames));
							}
							else
							{
								setValuesAndClearIfNecessary(dataRow.getValues(columnNames));
							}
						}
						else
						{
							Object[] result = new Object[referencedColumnNames.length];
							result[columnIndex] = item;
							setValuesAndClearIfNecessary(result);
						}
					}
				}
			}
			popupChanged = false;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Searches the condition columns.
		 * 
		 * @param pCondition the condition.
		 * @param pConditionColumns the list of all found condition columns.
		 */
		private void fillInConditionColumns(ICondition pCondition, List<String> pConditionColumns)
		{
			if (pCondition instanceof CompareCondition)
			{
				String colName = ((CompareCondition)pCondition).getDataRowColumnName();
				if (!pConditionColumns.contains(colName))
				{
					pConditionColumns.add(colName);
				}
			}
			else if (pCondition instanceof OperatorCondition)
			{
				ICondition[] conditions = ((OperatorCondition)pCondition).getConditions();
				
				for (int i = 0; i < conditions.length; i++)
				{
					fillInConditionColumns(conditions[i], pConditionColumns);
				}
			}
		}
		
		/**
		 * Searches the condition columns.
		 * 
		 * @param pColumnMapping the columnMapping.
		 * @param pCondition the additional condition.
		 * @return the list of all found condition columns.
		 */
		private String[] getConditionColumns(ColumnMapping pColumnMapping, ICondition pCondition)
		{
			ArrayUtil<String> conditionColumns = new ArrayUtil<>();
			
			if (pColumnMapping != null)
			{
				conditionColumns.addAll(pColumnMapping.getColumnNames());
			}
			
			fillInConditionColumns(pCondition, conditionColumns);
			
			int size = conditionColumns.size();
			if (size == 0)
			{
				return null;
			}
			else
			{
				return conditionColumns.toArray(new String[size]);
			}
		}
		
		/**
		 * Returns the relevant search column. If a {@code displayColumnName} is
		 * set, then that one will be returned, otherwise it will return
		 * {@code referencedColumnName}.
		 * 
		 * @return the relevant search column name.
		 */
		private String getRelevantSearchColumnName()
		{
			if (displayColumnName != null)
			{
				return displayColumnName;
			}
			else
			{
				return referencedColumnName;
			}
		}
		
		/**
		 * Creates a Condition including the search columns.
		 * 
		 * @param pCondition the base condition.
		 * @return a Condition including the search columns.
		 */
		private ICondition getSearchCondition(ICondition pCondition)
		{
			ICondition originalCondition = pCondition;
			
			if (originalCondition == null)
			{
				originalCondition = cellEditor.additionalCondition;
			}
			else if (cellEditor.additionalCondition != null)
			{
				originalCondition = originalCondition.and(cellEditor.additionalCondition);
			}
			
			if (searchColumns != null)
			{
				for (int i = 0; i < searchColumns.length; i++)
				{
					ICondition condition = new Equals(dataRow, searchColumns[i], referencedSearchColumns[i]);
					
					if (pCondition == null)
					{
						originalCondition = condition;
					}
					else
					{
						originalCondition = pCondition.and(condition);
					}
				}
			}
			
			return originalCondition;
		}
		
		/**
		 * Creates a wildcarded search string.
		 * 
		 * @param pItem the item to wildcard.
		 * @return the search string.
		 */
		private String getWildCardString(Object pItem)
		{
			if (cellEditor.isSearchTextAnywhere())
			{
				return "*" + pItem + "*";
			}
			else
			{
				return pItem + "*";
			}
		}
		
		/**
		 * Invoked if the {@link #component} changes it value.
		 * 
		 * @param pObservable the observable.
		 * @param pOldValue the old value.
		 * @param pNewValue the new value.
		 */
		private void onComponentValueChanged(ObservableValue<? extends String> pObservable, String pOldValue, String pNewValue)
		{
			fireEditingStarted();
			
			try
			{
				if (pNewValue != null && !pNewValue.isEmpty() && !Objects.equals(pNewValue, lastValue))
				{
					referencedDataBook.setFilter(getSearchCondition(new LikeIgnoreCase(getRelevantSearchColumnName(), getWildCardString(pNewValue))));
				}
				else
				{
					referencedDataBook.setFilter(getSearchCondition(null));
				}
				
				lastValue = pNewValue;
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
		
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
				popupChanged = true;
				fireEditingComplete(ICellEditorListener.FOCUS_LOST);
			}
			else
			{
				fireEditingStarted();
			}
		}
		
		/**
		 * Searches for additional columns to be cleared on save.
		 * 
		 * @param pRowDef the row definition.
		 * @param pRefDefColumns the columns of this reference definition.
		 * @param pSearchColumns the own search columns.
		 * @return additional clear columns.
		 * @throws ModelException if it fails.
		 */
		private String[] searchForAdditionalClearColumns(IRowDefinition pRowDef, String[] pRefDefColumns, String[] pSearchColumns) throws ModelException
		{
			ArrayUtil<String> columns = new ArrayUtil<>();
			
			String[] allColumns = pRowDef.getColumnNames();
			
			for (String colName : allColumns)
			{
				ICellEditor ce = pRowDef.getColumnDefinition(colName).getDataType().getCellEditor();
				
				if (ce instanceof ILinkedCellEditor)
				{
					ILinkedCellEditor linkCe = (ILinkedCellEditor)ce;
					ReferenceDefinition refDef = linkCe.getLinkReference();
					String[] searchCols = ArrayUtil.removeAll(
							getConditionColumns(linkCe.getSearchColumnMapping(), linkCe.getAdditionalCondition()), pSearchColumns);
							
					if (refDef != null && searchCols != null
							&& ArrayUtil.intersect(pRefDefColumns, searchCols).length > 0)
					{
						String[] addCols = ArrayUtil.removeAll(refDef.getColumnNames(), pRefDefColumns);
						
						for (String col : addCols)
						{
							if (!columns.contains(col))
							{
								columns.add(col);
							}
						}
					}
				}
			}
			return columns.toArray(new String[columns.size()]);
		}
		
		/**
		 * Sets the selected item to the given item.
		 * 
		 * @param pItem the item so select.
		 */
		private void setSelectedItem(Object pItem)
		{
			try
			{
				if (firstEditingStarted)
				{
					referencedDataBook.setFilter(getSearchCondition(null));
					if (cellEditor.isSortByColumnName())
					{
						referencedDataBook.setSort(new SortDefinition(referencedColumnName));
					}
					
					if (pItem != null)
					{
						String[] compareColumns = new String[] { referencedColumnName };
						IDataRow searchRow = referencedDataBook.createEmptyRow(null);
						searchRow.setValue(referencedColumnName, pItem);
						
						long start = System.currentTimeMillis();
						
						referencedDataBook.setSelectedRow(-1);
						int i = 0;
						IDataRow row = referencedDataBook.getDataRow(i);
						while (row != null && referencedDataBook.getSelectedRow() < 0 && System.currentTimeMillis() - start < 1000)
						{
							if (searchRow.equals(row, compareColumns))
							{
								referencedDataBook.setSelectedRow(i);
							}
							else
							{
								i++;
								row = referencedDataBook.getDataRow(i);
							}
						}
					}
				}
				else
				{
					if (pItem == null)
					{
						referencedDataBook.setFilter(getSearchCondition(null));
					}
					else
					{
						referencedDataBook.setFilter(getSearchCondition(new LikeIgnoreCase(getRelevantSearchColumnName(), getWildCardString(pItem))));
					}
				}
			}
			catch (ModelException pModelException)
			{
				// Do Nothing
			}
			
			popupChanged = false;
		}
		
		/**
		 * Sets the values and clears the additionalClearColumns, if values are
		 * changed.
		 * 
		 * @param pNewValues the new values.
		 * @throws ModelException if it fails.
		 */
		private void setValuesAndClearIfNecessary(Object[] pNewValues) throws ModelException
		{
			IDataRow oldRow = dataRow.createDataRow(columnNames);
			
			dataRow.setValues(columnNames, pNewValues);
			
			if (!dataRow.equals(oldRow, columnNames))
			{
				dataRow.setValues(additionalClearColumns, null);
			}
		}
		
		/**
		 * The {@link ReferencePopupProvider} provides a popup with a
		 * {@link FXDataBookView} and the reference of its parent
		 * {@link ComboBoxCellEditorHandler} in it.
		 * 
		 * @author Robert Zenz
		 */
		private static final class ReferencePopupProvider implements IFXComboBoxPopupProvider<String>
		{
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Class members
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/** The parent {@link ComboBoxCellEditorHandler}. */
			private ComboBoxCellEditorHandler cellEditorHandler;
			
			/** The used {@link FXDataBookView}. */
			private FXDataBookView dataBookView;
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Initialization
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/**
			 * Creates a new instance of {@link ReferencePopupProvider}.
			 *
			 * @param pCellEditorHandler the cell editor handler.
			 */
			public ReferencePopupProvider(ComboBoxCellEditorHandler pCellEditorHandler)
			{
				cellEditorHandler = pCellEditorHandler;
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Interface implementation
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public String fromString(String pValue)
			{
				return pValue;
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void hidePopup()
			{
				// hidePopup might be called before showPopup.
				if (dataBookView != null)
				{
					// Make sure that the control is correctly destroyed.
					dataBookView.setDataBook(null);
				}
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void init(FXCustomComboBox<String> pParentComboBox)
			{
				// Nothing to be done.
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void selectNext()
			{
				if (dataBookView != null && dataBookView.getItems().size() > 0)
				{
					int nextSelectedRow = dataBookView.getSelectionModel().getSelectedIndex();
					nextSelectedRow = nextSelectedRow + 1;
					
					if (nextSelectedRow >= dataBookView.getItems().size())
					{
						nextSelectedRow = 0;
					}
					
					dataBookView.getSelectionModel().select(nextSelectedRow);
				}
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void selectPrevious()
			{
				if (dataBookView != null && dataBookView.getItems().size() > 0)
				{
					int nextSelectedRow = dataBookView.getSelectionModel().getSelectedIndex();
					nextSelectedRow = nextSelectedRow - 1;
					
					if (nextSelectedRow < 0)
					{
						nextSelectedRow = dataBookView.getItems().size() - 1;
					}
					
					dataBookView.getSelectionModel().select(nextSelectedRow);
				}
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public Node showPopup()
			{
				if (dataBookView == null)
				{
					dataBookView = new FXDataBookView();
				}
				
				dataBookView.setDataBook(cellEditorHandler.referencedDataBook);
				dataBookView.setEditable(!cellEditorHandler.cellEditor.tableReadOnly);
				
				IDimension size = cellEditorHandler.cellEditor.popupSize;
				if (size != null)
				{
					dataBookView.setPrefSize(size.getWidth(), size.getHeight());
				}
				
				return dataBookView;
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public String toString(String pValue)
			{
				return pValue;
			}
			
		}	// ReferencePopupProvider
		
	}	// ComboBoxCellEditorHandler
	
}	// FXLinkedCellEditor

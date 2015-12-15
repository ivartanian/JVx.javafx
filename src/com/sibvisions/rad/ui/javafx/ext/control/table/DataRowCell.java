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
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IControl;
import javax.rad.ui.IResource;
import javax.rad.ui.celleditor.IDateCellEditor;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.control.ICellFormat;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.FXRowFocusingCell;
import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXControlUtil;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;

/**
 * The {@link DataRowCell} is a {@link FXRowFocusingCell} extension and
 * {@link ICellEditorListener} implementation that allows to be bound against a
 * {@link IDataRow}. It can contain a renderer and an editor and knows how to
 * interact with a {@link FXDataBookView}.
 * 
 * @author Robert Zenz
 */
public class DataRowCell extends FXRowFocusingCell<IDataRow, Object> implements ICellEditorListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The factory method for {@link DataRowCell}s. */
	public static final Callback<TableColumn<IDataRow, Object>, TableCell<IDataRow, Object>> FACTORY = (pParams) -> new DataRowCell();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link ICellEditorHandler} that is used. */
	private ICellEditorHandler<?> cellEditorHandler;
	
	/** The cached value of the {@link Node#clipProperty()} of the cell. */
	private Node clipper;
	
	/** The small dropdown arrow. */
	private FXImageRegion dropdownArrow;
	
	/** The {@link Node} that is used as editor. */
	private Node editor;
	
	/** The {@link Node} that is used as renderer. */
	private Node renderer;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataRowCell}.
	 */
	public DataRowCell()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editingComplete(String pCompleteType)
	{
		if (pCompleteType.equals(ICellEditorListener.ESCAPE_KEY))
		{
			cancelEdit();
		}
		else
		{
			commitEdit(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editingStarted()
	{
		// TODO Something to do here?
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSavingImmediate()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IControl getControl()
	{
		return (FXDataBookView)getTableView();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelEdit()
	{
		if (cellEditorHandler != null)
		{
			try
			{
				cellEditorHandler.cancelEditing();
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		endEditing();
		super.cancelEdit();
		
		updateItem(null, false);
		requestLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commitEdit(Object pNewValue)
	{
		if (cellEditorHandler != null)
		{
			try
			{
				cellEditorHandler.saveEditing();
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		endEditing();
		
		try
		{
			// TODO This call might throw a NullPointerException, which is currently silenced.
			super.commitEdit(pNewValue);
		}
		catch (Exception e)
		{
			// Ignore the exception.
		}
		
		updateItem(null, false);
		requestLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startEdit()
	{
		if (!isEnabled())
		{
			return;
		}
		
		if (!initCellEditorHandlerAndEditor())
		{
			// If we could not acquire an editor, return immediately and do not
			// even try to start the edit mode.
			return;
		}
		
		super.startEdit();
		
		if (!isEditing())
		{
			// We could not go to edit mode for some reason, destroy any created
			// things again.
			editor = null;
			cellEditorHandler = null;
			
			return;
		}
		
		((FXDataBookView) getTableView()).startEditing(this);
		
		// Move the cell to the front to make sure that expanded editors will
		// not be overlapped by other cells.
		toFront();
		
		getChildren().remove(editor);
		getChildren().add(editor);
		
		editor.requestFocus();
		
		// Save the clipper of the cell, if any. The editor will most likely
		// be bigger than the cell, so the clipper is only standing in our way.
		clipper = getClip();
		setClip(null);
		
		if (cellEditorHandler != null)
		{
			try
			{
				cellEditorHandler.cancelEditing();
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		
		if (renderer != null)
		{
			layoutChild(renderer, false, HPos.CENTER);
			renderer.setDisable(!isEnabled());
		}
		
		if (dropdownArrow != null)
		{
			layoutChild(dropdownArrow, false, HPos.RIGHT);
			dropdownArrow.setVisible(isEnabled());
		}
		
		if (editor != null)
		{
			layoutChild(editor, true, HPos.CENTER);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateItem(Object pItem, boolean pEmpty)
	{
		super.updateItem(pItem, pEmpty);
		
		if (pEmpty || getTableRow() == null)
		{
			getChildren().clear();
			
			renderer = null;
			editor = null;
			
			// Hide the dropdown if this is an empty cell.
			setGraphic(null);
			dropdownArrow = null;
			
			setText(null);
			return;
		}
		
		hideOrShowDropdownArrow();
		
		try
		{
			if (renderer == null)
			{
				Object rendererComponent = getCellRendererComponent();
				
				if (rendererComponent instanceof Node)
				{
					setEditable(false);
					
					renderer = (Node) rendererComponent;
					getChildren().add(renderer);
				}
			}
			
			if (renderer == null)
			{
				Object rendererComponent = getCellRendererComponent();
				
				if (rendererComponent instanceof String)
				{
					setText((String) rendererComponent);
				}
				else
				{
					String value = getDataRow().getValueAsString(getColumnName());
					setText(value);
				}
			}
			else
			{
				if (renderer instanceof AbstractDataRowCellContent<?>)
				{
					((AbstractDataRowCellContent<?>) renderer).update(this);
				}
				
				setText(null);
			}
			
			ICellFormat cellFormat = ((FXDataBookView) getTableView()).getCellFormat(getDataRow(), getTableColumn(), getIndex());
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
					// TODO DataRowCell does not support images.
				}
				if (cellFormat.getLeftIndent() >= 0)
				{
					// TODO DataRowCell does not support indentation.
				}
				
				setStyle(styleContainer.getStyle());
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
	 * Ends the editing mode.
	 */
	private void endEditing()
	{
		getChildren().remove(editor);
		editor = null;
		
		if (cellEditorHandler != null)
		{
			cellEditorHandler.uninstallEditor();
		}
		cellEditorHandler = null;
		
		setClip(clipper);
		
		((FXDataBookView) getTableView()).endEditing();
		getTableView().requestFocus();
	}
	
	/**
	 * Tries to find an {@link ICellEditorHandler} suitable for the associated
	 * {@link IDataType}.
	 * 
	 * @return the {@link ICellEditorHandler}.
	 * @throws ModelException if accessing the model failed.
	 */
	private ICellEditorHandler<?> getCellEditorHandler() throws ModelException
	{
		IDataRow dataRow = getDataRow();
		String columnName = getColumnName();
		ICellEditor cellEditor = FXControlUtil.findCellEditor(getDataType(dataRow, columnName));
		
		return cellEditor.createCellEditorHandler(this, getDataBook(), columnName);
	}
	
	/**
	 * Tries to find an renderer {@link Node} suitable for the associated
	 * {@link IDataType}.
	 * 
	 * @return the render {@link Node}. {@code null} if none was found.
	 * @throws ModelException if accessing the model failed.
	 */
	private Object getCellRendererComponent() throws ModelException
	{
		IDataRow dataRow = getDataRow();
		String columnName = getColumnName();
		IDataType dataType = getDataType(dataRow, columnName);
		ICellRenderer<?> cellRenderer = dataType.getCellRenderer();
		
		if (cellRenderer == null)
		{
			ICellEditor cellEditor = FXControlUtil.findCellEditor(dataType);
			
			if (cellEditor instanceof ICellRenderer)
			{
				cellRenderer = (ICellRenderer<?>) cellEditor;
			}
			else if (cellEditor instanceof IResource && ((IResource) cellEditor).getResource() instanceof ICellRenderer)
			{
				cellRenderer = (ICellRenderer<?>) ((IResource) cellEditor).getResource();
			}
		}
		
		if (cellRenderer != null)
		{
			return cellRenderer.getCellRendererComponent(null, getDataBook(), getIndex(), dataRow, columnName, false, false);
		}
		
		return null;
	}
	
	/**
	 * Gets the name of the data column.
	 * 
	 * @return the name of the data column.
	 */
	private String getColumnName()
	{
		return (String) getTableColumn().getUserData();
	}
	
	/**
	 * Gets the {@link IDataBook} from the parent {@link FXDataBookView}.
	 * 
	 * @return the {@link IDataBook}.
	 */
	private IDataBook getDataBook()
	{
		return ((FXDataBookView) getTableView()).getDataBook();
	}
	
	/**
	 * Gets the {@link IDataRow} associated with this.
	 * 
	 * @return the {@link IDataRow}.
	 * @throws ModelException if getting the {@link IDataRow} failed.
	 */
	private IDataRow getDataRow() throws ModelException
	{
		IDataRow dataRow = (IDataRow) getTableRow().getItem();
		
		if (dataRow == null)
		{
			// TODO Why is the datarow null?
			return getDataBook().getDataRow(getIndex());
		}
		
		return dataRow;
	}
	
	/**
	 * Gets the {@link IDataType} for the given {@link IDataRow} and column.
	 * 
	 * @param pDataRow the {@link IDataRow} from which to get the
	 *            {@link ColumnDefinition}.
	 * @param pColumnName the name of the column.
	 * @return the {@link IDataType}.
	 * @throws ModelException if accessing the {@link IDataRow} or
	 *             {@link ColumnDefinition} failed.
	 */
	private IDataType getDataType(IDataRow pDataRow, String pColumnName) throws ModelException
	{
		ColumnDefinition columnDefinition = pDataRow.getRowDefinition().getColumnDefinition(pColumnName);
		return columnDefinition.getDataType();
	}
	
	/**
	 * Determines if the cell should be enabled based on the databook.
	 * 
	 * @return @{code true} if the cell should be editable based on the
	 *         databook.
	 */
	private boolean isEnabled()
	{
		try
		{
			return getDataBook().isUpdateAllowed() || (getDataBook().isInserting() && getDataBook().isInsertAllowed());
		}
		catch (ModelException e)
		{
			// Ignore any exception, we really do not care at this point.
		}
		
		return false;
	}
	
	/**
	 * Lays out the given child based on the boundaries of this cell and/or its
	 * parent.
	 * 
	 * @param pChild the child to layout.
	 * @param pStretch if the child should be stretched to the size of the cell.
	 * @param pHorizontalPosition the horizontal position of the child.
	 */
	private void layoutChild(Node pChild, boolean pStretch, HPos pHorizontalPosition)
	{
		// It could be that our renderer/editor has been removed by the update
		// of the cell.
		if (!getChildren().contains(pChild))
		{
			getChildren().add(pChild);
		}
		
		// Push it to the front.
		pChild.toFront();
		
		if (pChild instanceof Region)
		{
			pChild.autosize();
			
			double childWidth = ((Region) pChild).getWidth();
			double childHeight = ((Region) pChild).getHeight();
			
			if (!pStretch && childWidth <= getWidth() && childHeight <= getHeight())
			{
				positionInArea(pChild, 0, 0, getWidth(), getHeight(), 0, ((Region) pChild).getInsets(), pHorizontalPosition, VPos.CENTER, isSnapToPixel());
			}
			else
			{
				Insets surroundingSpace = NodeUtil.getSurroundingSpace(getTableView(), this);
				double spaceToRightEdge = getWidth() + surroundingSpace.getRight();
				
				// TODO Magic number used to fix the alignment of the right edge.
				spaceToRightEdge = spaceToRightEdge - 2;
				
				if (childWidth >= spaceToRightEdge)
				{
					double x = spaceToRightEdge - childWidth;
					pChild.resizeRelocate(x, 0, Math.max(childWidth, getWidth()), Math.max(childHeight, getHeight()));
				}
				else
				{
					pChild.resizeRelocate(0, 0, Math.max(childWidth, getWidth()), Math.max(childHeight, getHeight()));
				}
			}
		}
		else
		{
			pChild.resizeRelocate(0, 0, getWidth(), getHeight());
		}
	}
	
	/**
	 * Hides or shows the small dropdown arrow if needed.
	 */
	private void hideOrShowDropdownArrow()
	{
		try
		{
			ICellEditor cellEditor = FXControlUtil.findCellEditor(getDataType(getDataRow(), getColumnName()));
			
			if (cellEditor instanceof ILinkedCellEditor || cellEditor instanceof IDateCellEditor)
			{
				if (dropdownArrow == null)
				{
					dropdownArrow = new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/control/table/css/combobox.png");
					dropdownArrow.setPadding(new Insets(1, 4, 3, 4));
					dropdownArrow.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onDropdownMouseClicked);
					setGraphic(dropdownArrow);
					setContentDisplay(ContentDisplay.RIGHT);
				}
			}
			else if (dropdownArrow != null)
			{
				setGraphic(null);
				dropdownArrow = null;
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Tries to initialize the {@link #cellEditorHandler} and {@link #editor}.
	 * 
	 * @return {@code true} if the initialization worked and both are now
	 *         usable.
	 */
	private boolean initCellEditorHandlerAndEditor()
	{
		try
		{
			if (cellEditorHandler == null)
			{
				cellEditorHandler = getCellEditorHandler();
			}
			
			if (cellEditorHandler != null && editor == null)
			{
				Object cellEditorComponent = cellEditorHandler.getCellEditorComponent();
				
				if (cellEditorComponent instanceof Node)
				{
					editor = (Node) cellEditorComponent;
				}
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
		
		if (cellEditorHandler == null || editor == null)
		{
			cellEditorHandler = null;
			editor = null;
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Invoked of the small dropdown arrow is clicked.
	 * <p>
	 * Starts the edit.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onDropdownMouseClicked(MouseEvent pMouseEvent)
	{
		if (pMouseEvent.getButton() == MouseButton.PRIMARY)
		{
			pMouseEvent.consume();
			startEdit();
		}
	}
	
}	// DataRowCell

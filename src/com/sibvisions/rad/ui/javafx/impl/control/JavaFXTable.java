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
package com.sibvisions.rad.ui.javafx.impl.control;

import java.util.List;

import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.ITable;
import javax.rad.util.TranslationMap;

import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import com.sibvisions.rad.ui.javafx.ext.control.table.DataAwareConstrainedFillingResizePolicy;
import com.sibvisions.rad.ui.javafx.ext.control.table.FXDataBookView;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;
import com.sun.javafx.scene.traversal.Direction;

/**
 * The {@link JavaFXTable} is the JavaFX specific implementation of
 * {@link ITable}.
 * 
 * @author Robert Zenz
 */
public class JavaFXTable extends JavaFXComponent<FXDataBookView> implements ITable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * If the columns are autosizing, meaning filling the width of the table.
	 */
	private boolean autoResize;
	
	/** The navigation mode for the {@code enter} key. */
	private int enterNavigationMode;
	
	/** If the current mouse event is over the selected cell. */
	private boolean mouseEventOnSelectedCell;
	
	/** The navigation mode for the {@code tab} key. */
	private int tabNavigationMode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXTable}.
	 */
	public JavaFXTable()
	{
		super(new FXDataBookView());
		
		autoResize = false;
		enterNavigationMode = NAVIGATION_CELL_AND_FOCUS;
		tabNavigationMode = NAVIGATION_CELL_AND_FOCUS;
		
		resource.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
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
		resource.cancelEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellFormatter getCellFormatter()
	{
		return resource.getCellFormatter();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnView getColumnView()
	{
		return resource.getColumnView();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataBook getDataBook()
	{
		return resource.getDataBook();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getEnterNavigationMode()
	{
		return enterNavigationMode;
	}
	
	/**
	 * It is not possible to set or influence the maximum row height.
	 * 
	 * @return always zero.
	 */
	@Override
	public int getMaxRowHeight()
	{
		// TODO It is not possible to set the maximum row height.
		return 0;
	}
	
	/**
	 * It is not possible to set or influence the minimum row height.
	 * 
	 * @return always zero.
	 */
	@Override
	public int getMinRowHeight()
	{
		// TODO It is not possible to set the minimum row height.
		return 0;
	}
	
	/**
	 * It is not possible to set or influence the row height.
	 * 
	 * @return always zero.
	 */
	@Override
	public int getRowHeight()
	{
		// TODO It is not possible to set the row height.
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabNavigationMode()
	{
		return tabNavigationMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		return resource.getTranslation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoResize()
	{
		return autoResize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEditable()
	{
		return resource.isEditable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMouseEventOnSelectedCell()
	{
		return mouseEventOnSelectedCell;
	}
	
	/**
	 * It is not possible to hide the focus rectangle.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isShowFocusRect()
	{
		// TODO It is not possible to hide the focus rectangle.
		return true;
	}
	
	/**
	 * It is not possible to hide the horizontal lines.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isShowHorizontalLines()
	{
		// TODO It is not possible to hide the horizontal lines of a TableView.
		return true;
	}
	
	/**
	 * It is not possible to hide the selection.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isShowSelection()
	{
		// TODO It is not possible to hide the selection.
		return true;
	}
	
	/**
	 * It is not possible to hide the vertical lines.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isShowVerticalLines()
	{
		// TODO It is not possible to hide the vertical lines of a TableView.
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSortOnHeaderEnabled()
	{
		return resource.isSortingEnabled();
	}
	
	/**
	 * It is not possible to hide the header.
	 * <p>
	 * See <a href="https://javafx-jira.kenai.com/browse/RT-32673">RT-32673</a>.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isTableHeaderVisible()
	{
		// TODO The header of the table can't be made invisible.
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTranslationEnabled()
	{
		return resource.isTranslationEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyRepaint()
	{
		resource.notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveEditing() throws ModelException
	{
		resource.saveEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutoResize(boolean pAutoResize)
	{
		autoResize = pAutoResize;
		
		if (pAutoResize)
		{
			resource.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		}
		else
		{
			resource.setColumnResizePolicy(new DataAwareConstrainedFillingResizePolicy());
			resource.resizeColumns();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCellFormatter(ICellFormatter pCellFormatter)
	{
		resource.setCellFormatter(pCellFormatter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnView(ColumnView pColumnView)
	{
		resource.setColumnView(pColumnView);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataBook(IDataBook pDataBook)
	{
		resource.setDataBook(pDataBook);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(boolean pEditable)
	{
		resource.setEditable(pEditable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnterNavigationMode(int pNavigationMode)
	{
		enterNavigationMode = pNavigationMode;
	}
	
	/**
	 * It is not possible to set or influence the maximum row height.
	 * 
	 * @param pMaxRowHeight ignored.
	 */
	@Override
	public void setMaxRowHeight(int pMaxRowHeight)
	{
		// TODO It is not possible to set the maximum row height.
	}
	
	/**
	 * It is not possible to set or influence the minimum row height.
	 * 
	 * @param pMinRowHeight ignored.
	 */
	@Override
	public void setMinRowHeight(int pMinRowHeight)
	{
		// TODO It is not possible to set the minimum row height.
	}
	
	/**
	 * It is not possible to set or influence the row height.
	 * 
	 * @param pRowHeight ignored.
	 */
	@Override
	public void setRowHeight(int pRowHeight)
	{
		// TODO It is not possible to set the row height.
	}
	
	/**
	 * It is not possible to hide the focus rectangle.
	 * 
	 * @param pShowFocusRect ignored.
	 */
	@Override
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		// TODO It is not possible to hide the focus rectangle.
	}
	
	/**
	 * It is not possible to hide the horizontal lines.
	 * 
	 * @param pShowHorizontalLines ignored.
	 */
	@Override
	public void setShowHorizontalLines(boolean pShowHorizontalLines)
	{
		// TODO It is not possible to hide the horizontal lines of a TableView.
	}
	
	/**
	 * It is not possible to hide the selection.
	 * 
	 * @param pShowSelection ignored.
	 */
	@Override
	public void setShowSelection(boolean pShowSelection)
	{
		// TODO It is not possible to hide the selection.
	}
	
	/**
	 * It is not possible to hide the vertical lines.
	 * 
	 * @param pShowVerticalLines ignored.
	 */
	@Override
	public void setShowVerticalLines(boolean pShowVerticalLines)
	{
		// TODO It is not possible to hide the vertical lines of a TableView.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
		resource.setSortingEnabled(pSortOnHeaderEnabled);
	}
	
	/**
	 * It is not possible to hide the header.
	 * <p>
	 * See <a href="https://javafx-jira.kenai.com/browse/RT-32673">RT-32673</a>.
	 * 
	 * @param pTableHeaderVisible ignored.
	 */
	@Override
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		// TODO The header of the table can't be made invisible.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabNavigationMode(int pNavigationMode)
	{
		tabNavigationMode = pNavigationMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		resource.setTranslation(pTranslation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslationEnabled(boolean pEnabled)
	{
		resource.setTranslationEnabled(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String translate(String pText)
	{
		return resource.translate(pText);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startEditing()
	{
		resource.edit();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireMouseClickedEvent(MouseEvent pMouseEvent)
	{
		mouseEventOnSelectedCell = isOnSelectedCell(pMouseEvent);
		
		super.fireMouseClickedEvent(pMouseEvent);
		
		mouseEventOnSelectedCell = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireMousePressedEvent(MouseEvent pMouseEvent)
	{
		mouseEventOnSelectedCell = isOnSelectedCell(pMouseEvent);
		
		super.fireMousePressedEvent(pMouseEvent);
		
		mouseEventOnSelectedCell = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireMouseReleasedEvent(MouseEvent pMouseEvent)
	{
		mouseEventOnSelectedCell = isOnSelectedCell(pMouseEvent);
		
		super.fireMouseReleasedEvent(pMouseEvent);
		
		mouseEventOnSelectedCell = false;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if the given {@link MouseEvent} is on the selected cell.
	 * 
	 * @param pMouseEvent the {@link MouseEvent}.
	 * @return {@code true} if the given event is on the selected cell.
	 */
	private boolean isOnSelectedCell(MouseEvent pMouseEvent)
	{
		EventTarget target = pMouseEvent.getTarget();
		
		if (target instanceof Node)
		{
			boolean isCellSelection = resource.getSelectionModel().isCellSelectionEnabled();
			
			Node targetNode = (Node)target;
			
			while (targetNode != null && !(targetNode instanceof FXDataBookView))
			{
				if (isCellSelection && targetNode instanceof TableCell<?, ?>)
				{
					return ((TableCell<?, ?>)targetNode).isSelected();
				}
				else if (!isCellSelection && targetNode instanceof TableRow<?>)
				{
					return ((TableRow<?>)targetNode).isSelected();
				}
				
				targetNode = targetNode.getParent();
			}
		}
		
		return false;
	}
	
	/**
	 * Invoked if a key is pressed.
	 * <p>
	 * Implements the behavior of the {@code ENTER} and {@code TAB} key.
	 * 
	 * @param pKeyEvent the event.
	 */
	@SuppressWarnings("deprecation")
	private void onKeyPressed(KeyEvent pKeyEvent)
	{
		int navigationMode = NAVIGATION_NONE;
		
		if (pKeyEvent.getCode() == KeyCode.ENTER)
		{
			navigationMode = enterNavigationMode;
		}
		else if (pKeyEvent.getCode() == KeyCode.TAB)
		{
			navigationMode = tabNavigationMode;
		}
		
		if (navigationMode != NAVIGATION_NONE)
		{
			TablePosition<?, ?> currentSelection = resource.getFocusModel().getFocusedCell();
			
			if (currentSelection != null)
			{
				List<TableColumn<IDataRow, ?>> columns = resource.getColumns();
				
				int currentRow = currentSelection.getRow();
				int currentColumn = columns.indexOf(currentSelection.getTableColumn());
				
				switch (navigationMode)
				{
					case NAVIGATION_CELL_AND_FOCUS:
						pKeyEvent.consume();
						
						if (currentColumn < columns.size() - 1)
						{
							resource.getFocusModel().focus(currentRow, columns.get(currentColumn + 1));
							resource.getSelectionModel().select(currentRow, columns.get(currentColumn + 1));
						}
						else
						{
							resource.impl_traverse(Direction.NEXT);
						}
						
						break;
						
					case NAVIGATION_CELL_AND_ROW_AND_FOCUS:
						pKeyEvent.consume();
						
						if (currentColumn < columns.size() - 1)
						{
							resource.getFocusModel().focus(currentRow, columns.get(currentColumn + 1));
							resource.getSelectionModel().select(currentRow, columns.get(currentColumn + 1));
						}
						else if (currentRow < resource.getItems().size() - 1)
						{
							resource.getFocusModel().focus(currentRow + 1, columns.get(0));
							resource.getSelectionModel().select(currentRow + 1, columns.get(0));
						}
						else
						{
							resource.impl_traverse(Direction.NEXT);
						}
						
						break;
						
					case NAVIGATION_ROW_AND_FOCUS:
						pKeyEvent.consume();
						
						if (currentRow < resource.getItems().size() - 1)
						{
							resource.getFocusModel().focus(currentRow + 1, columns.get(0));
							resource.getSelectionModel().select(currentRow + 1, columns.get(0));
						}
						else
						{
							resource.impl_traverse(Direction.NEXT);
						}
						
						break;
						
					default:
						// Nothing to be done.
						
				}
			}
		}
	}
	
}	// JavaFXTable

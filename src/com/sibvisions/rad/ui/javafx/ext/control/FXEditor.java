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

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.control.ICellFormatable;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXControlUtil;
import com.sibvisions.util.type.StringUtil;
import com.sun.javafx.scene.traversal.Direction;

/**
 * The {@link FXEditor} is a bound editor for JavaFX.
 * 
 * @author Robert Zenz
 */
public class FXEditor extends Region implements IEditorControl, ICellEditorListener, ICellFormatable, IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property for if the border of the editor is visible or not. */
	private BooleanProperty borderVisible;
	
	/** The property that holds the {@link ICellEditor}. */
	private ObjectProperty<ICellEditor> cellEditor;
	
	/** The {@link ICellEditorHandler}. */
	private ICellEditorHandler<?> cellEditorHandler;
	
	/** The property that holds the {@link ICellFormatter}. */
	private ObjectProperty<ICellFormatter> cellFormatter;
	
	/** The property for the column name. */
	private StringProperty columnName;
	
	/** The property for the {@link IDataRow}. */
	private ObjectProperty<IDataRow> dataRow;
	
	/** If the editor is currently being edited. */
	private boolean editingStarted;
	
	/** The editor itself. */
	private Node editor;
	
	/** The {@link StyleContainer} that is paired with the {@link #editor}. */
	private StyleContainer editorStyleContainer;
	
	/** The horizontal alignment. */
	private IntegerProperty horizontalAlignment;
	
	/** If changes in cell editor property should be ignored. */
	private boolean ignoreCellEditorChange;
	
	/** If this has been notified that a repaint is needed. */
	private boolean notified;
	
	/** The property for if the editor should save immediate. */
	private BooleanProperty savingImmediate;
	
	/** The property for if the translation is enabled. */
	private BooleanProperty translationEnabled;
	
	/** The property for the {@link TranslationmMap}. */
	private ObjectProperty<TranslationMap> translationMap;
	
	/** The vertical alignment. */
	private IntegerProperty verticalAlignment;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXEditor}.
	 */
	public FXEditor()
	{
		editingStarted = false;
		ignoreCellEditorChange = false;
		notified = false;
		
		borderVisible = new SimpleBooleanProperty(true);
		borderVisible.addListener(this::onBorderVisibleChanged);
		
		cellEditor = new SimpleObjectProperty<>();
		cellEditor.addListener(this::onCellEditorChanged);
		
		cellFormatter = new SimpleObjectProperty<>();
		
		columnName = new SimpleStringProperty();
		columnName.addListener(this::onColumnNameChanged);
		
		dataRow = new SimpleObjectProperty<>();
		dataRow.addListener(this::onDataRowChanged);
		
		horizontalAlignment = new SimpleIntegerProperty(ALIGN_DEFAULT);
		
		savingImmediate = new SimpleBooleanProperty();
		
		translationEnabled = new SimpleBooleanProperty();
		
		translationMap = new SimpleObjectProperty<>();
		translationMap.addListener(this::onTranslationChanged);
		
		verticalAlignment = new SimpleIntegerProperty(ALIGN_DEFAULT);
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
		editingStarted = false;
		
		try
		{
			if (cellEditorHandler != null)
			{
				cellEditorHandler.cancelEditing();
			}
		}
		catch (ModelException e)
		{
			// Ignore any exception.
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void editingComplete(String pCompleteType)
	{
		try
		{
			if (pCompleteType == ESCAPE_KEY)
			{
				cellEditorHandler.cancelEditing();
				return;
			}
			
			if (editingStarted)
			{
				// Only save if editing actually started.
				// TODO: Focus lost is firing before cancelEditing is called.
				// If the selection in the table is changed, the table will
				// cause a save on the databook and the editor, but before
				// cancelEditing() can be called, the focus lost event of
				// the editor will be fired, because cancelEditing() is invoked
				// later. Or at least I believe that these are the exact
				// circumstances.
				cellEditorHandler.saveEditing();
			}
			
			switch (pCompleteType)
			{
				case ACTION_KEY:
					editor.impl_traverse(Direction.NEXT);
					break;
					
				case ENTER_KEY:
					editor.impl_traverse(Direction.NEXT);
					break;
					
				case FOCUS_LOST:
					// Nothing to be done.
					break;
					
				case SHIFT_ENTER_KEY:
					editor.impl_traverse(Direction.PREVIOUS);
					break;
					
				case SHIFT_TAB_KEY:
					editor.impl_traverse(Direction.PREVIOUS);
					break;
					
				case TAB_KEY:
					editor.impl_traverse(Direction.NEXT);
					break;
					
				default:
					// No action required
					
			}
		}
		catch (ModelException e)
		{
			cancelEditing();
			
			ExceptionHandler.raise(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editingStarted()
	{
		try
		{
			editingStarted = true; // first set editingStarted true, to prevent events on update.
			
			if (dataRow instanceof IDataBook)
			{
				IDataRow oldDataRow = dataRow.get().createDataRow(null);
				
				((IDataBook)dataRow.get()).update();
				
				if (!oldDataRow.equals(dataRow.get(), new String[] { columnName.get() })) // Only if value is changed, cancel editing.
				{
					editingStarted = false;
					notifyRepaint();
				}
			}
		}
		catch (ModelException pModelException)
		{
			editingStarted = false;
			notifyRepaint();
			
			ExceptionHandler.raise(pModelException);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellEditor getCellEditor()
	{
		return cellEditor.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellFormatter getCellFormatter()
	{
		return cellFormatter.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName()
	{
		return columnName.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataRow getDataRow()
	{
		return dataRow.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
	{
		return horizontalAlignment.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		return translationMap.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalAlignment()
	{
		return verticalAlignment.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSavingImmediate()
	{
		return savingImmediate.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IControl getControl()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTranslationEnabled()
	{
		return translationEnabled.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyRepaint()
	{
		if (!notified)
		{
			notified = true;
			
			Platform.runLater(() ->
			{
				cancelEditing();
				notified = false;
			});
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveEditing() throws ModelException
	{
		if (editingStarted)
		{
			// Set immediate to false to avoid recursion, if DataRowListener stores again.
			editingStarted = false;
			
			cellEditorHandler.saveEditing();
			
			// In case of saving immediate, it not necessarily causes an event, to avoid a values changed on last key pressed.
			// so call notifyRepaint, it will not cause an additional cancelEditing, if saveEditing already caused one.
			// The event is needed, so that the cellEditorHandler can reset firstEditing flag in cancelEditing!
			notifyRepaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException
	{
		cellEditor.set(pCellEditor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCellFormatter(ICellFormatter pCellFormatter)
	{
		cellFormatter.set(pCellFormatter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnName(String pColumnName) throws ModelException
	{
		columnName.set(pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataRow(IDataRow pDataRow) throws ModelException
	{
		dataRow.set(pDataRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment.set(pHorizontalAlignment);
	}
	
	/**
	 * Sets the saving immediate.
	 *
	 * @param pSavingImmediate the new saving immediate.
	 */
	public void setSavingImmediate(boolean pSavingImmediate)
	{
		savingImmediate.set(pSavingImmediate);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		translationMap.set(pTranslation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslationEnabled(boolean pEnabled)
	{
		translationEnabled.set(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String translate(String pText)
	{
		TranslationMap map = translationMap.get();
		if (translationEnabled.get() && map != null)
		{
			return map.translate(pText);
		}
		else
		{
			return pText;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment.set(pVerticalAlignment);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxHeight(double pWidth)
	{
		if (!getChildren().isEmpty() && getChildren().get(0) instanceof Region)
		{
			return ((Region)getChildren().get(0)).maxHeight(pWidth);
		}
		
		return super.computeMaxHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxWidth(double pHeight)
	{
		if (!getChildren().isEmpty() && getChildren().get(0) instanceof Region)
		{
			return ((Region)getChildren().get(0)).maxWidth(pHeight);
		}
		
		return super.computeMaxWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double pWidth)
	{
		if (!getChildren().isEmpty() && getChildren().get(0) instanceof Region)
		{
			return ((Region)getChildren().get(0)).minHeight(pWidth);
		}
		
		return super.computeMinHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double pHeight)
	{
		if (!getChildren().isEmpty() && getChildren().get(0) instanceof Region)
		{
			return ((Region)getChildren().get(0)).minWidth(pHeight);
		}
		
		return super.computeMinWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double pWidth)
	{
		if (!getChildren().isEmpty() && getChildren().get(0) instanceof Region)
		{
			return ((Region)getChildren().get(0)).prefHeight(pWidth);
		}
		
		return super.computePrefHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight)
	{
		if (!getChildren().isEmpty() && getChildren().get(0) instanceof Region)
		{
			return ((Region)getChildren().get(0)).prefWidth(pHeight);
		}
		
		return super.computePrefWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		if (!getChildren().isEmpty())
		{
			getChildren().get(0).resizeRelocate(0, 0, getWidth(), getHeight());
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for if the border of the editor should be visible.
	 * 
	 * @return the property for if the border of the editor should be visible.
	 */
	public final BooleanProperty borderVisibleProperty()
	{
		return borderVisible;
	}
	
	/**
	 * If the border of editor should be visible.
	 * 
	 * @return {@code true} if the border of the editor should be visible.
	 */
	public final boolean isBorderVisible()
	{
		return borderVisible.get();
	}
	
	/**
	 * Sets if the border of the editor should be visible.
	 * 
	 * @param pBorderVisible {@code true} if the border of the editor should be
	 *            visible.
	 */
	public final void setBorderVisible(boolean pBorderVisible)
	{
		borderVisible.set(pBorderVisible);
	}
	
	/**
	 * Installs/Adds the new editor (if possible).
	 */
	protected void installEditor()
	{
		if (dataRow.get() != null && !StringUtil.isEmpty(columnName.get()))
		{
			dataRow.get().addControl(this);
			
			try
			{
				if (cellEditor.get() == null)
				{
					IRowDefinition rowDefinition = dataRow.get().getRowDefinition();
					ColumnDefinition columnDefinition = rowDefinition.getColumnDefinition(columnName.get());
					IDataType dataType = columnDefinition.getDataType();
					
					ignoreCellEditorChange = true;
					cellEditor.set(FXControlUtil.findCellEditor(dataType));
				}
				
				cellEditorHandler = cellEditor.get().createCellEditorHandler(this, dataRow.get(), columnName.get());
				
				if (cellEditorHandler != null)
				{
					editor = (Node)cellEditorHandler.getCellEditorComponent();
					editorStyleContainer = new StyleContainer(editor);
					
					if (!borderVisible.get())
					{
						editorStyleContainer.set("-fx-text-box-border", "transparent");
					}
					
					getChildren().clear();
					getChildren().add(editor);
					
					cancelEditing();
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				ignoreCellEditorChange = false;
			}
		}
	}
	
	/**
	 * Uninstalls/removes the editor (if any).
	 */
	protected void uninstallEditor()
	{
		getChildren().clear();
		
		cellEditorHandler = null;
		
		if (dataRow.get() != null)
		{
			dataRow.get().removeControl(this);
		}
		
		editorStyleContainer = null;
		editor = null;
	}
	
	/**
	 * Invoked if the {@link #borderVisible} property changes and propagates the
	 * changes to the {@link #editor} (if any).
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onBorderVisibleChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (editorStyleContainer != null)
		{
			if (!pNewValue.booleanValue())
			{
				editorStyleContainer.set("-fx-text-box-border", "transparent");
			}
			else
			{
				editorStyleContainer.clear("-fx-text-box-border");
			}
		}
	}
	
	/**
	 * Invoked if the {@link #cellEditor} property changes and invokes
	 * {@link #uninstallEditor()} and {@link #installEditor()}.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onCellEditorChanged(ObservableValue<? extends ICellEditor> pObservable, ICellEditor pOldValue, ICellEditor pNewValue)
	{
		if (!ignoreCellEditorChange)
		{
			uninstallEditor();
			installEditor();
		}
	}
	
	/**
	 * Invoked if the {@link #columnName} property changes and invokes
	 * {@link #uninstallEditor()} and {@link #installEditor()}.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onColumnNameChanged(ObservableValue<? extends String> pObservable, String pOldValue, String pNewValue)
	{
		uninstallEditor();
		installEditor();
	}
	
	/**
	 * Invoked if the {@link #dataRow} property changes and invokes
	 * {@link #uninstallEditor()} and {@link #installEditor()}.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onDataRowChanged(ObservableValue<? extends IDataRow> pObservable, IDataRow pOldValue, IDataRow pNewValue)
	{
		uninstallEditor();
		installEditor();
	}
	
	/**
	 * Invoked if the {@link #translationMap} property changes.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onTranslationChanged(ObservableValue<? extends TranslationMap> pObservable, TranslationMap pOldValue, TranslationMap pNewValue)
	{
		notifyRepaint();
	}
	
}	// FXEditor

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

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import com.sibvisions.rad.ui.celleditor.AbstractCheckBoxCellEditor;
import com.sibvisions.rad.ui.javafx.ext.FXCheckBox;
import com.sibvisions.rad.ui.javafx.ext.control.table.AbstractDataRowCellContent;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link FXCheckBoxCellEditor} is the JavaFX specific implementation of
 * {@link javax.rad.ui.celleditor.ICheckBoxCellEditor}.
 * 
 * @author Robert Zenz
 * @see javax.rad.ui.celleditor.ICheckBoxCellEditor
 */
public class FXCheckBoxCellEditor extends AbstractCheckBoxCellEditor<Node>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXCheckBoxCellEditor}.
	 */
	public FXCheckBoxCellEditor()
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
	public ICellEditorHandler<?> createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
	{
		return new CheckBoxCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getCellRendererComponent(Node pParentComponent, IDataPage pDataPage, int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected, boolean pHasFocus)
	{
		return new CellCheckBoxEditor(this);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link CellCheckBoxEditor} is the {@link AbstractDataRowCellContent}
	 * extension for the {@link FXCheckBoxCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static class CellCheckBoxEditor extends AbstractDataRowCellContent<CheckBox> implements ChangeListener<Boolean>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link CheckBoxCellEditor}. */
		private ICheckBoxCellEditor<?> cellEditor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link CellCheckBoxEditor}.
		 *
		 * @param pCellEditor the cell editor.
		 */
		public CellCheckBoxEditor(ICheckBoxCellEditor<?> pCellEditor)
		{
			super(new FXCheckBox());
			
			cellEditor = pCellEditor;
			
			component.setAlignment(FXAlignmentUtil.alignmentsToPos(cellEditor, Pos.CENTER_LEFT));
			component.setText(cellEditor.getText());
			
			component.selectedProperty().addListener(this);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void changed(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
		{
			if (pNewValue.booleanValue())
			{
				setValue(cellEditor.getSelectedValue());
			}
			else
			{
				setValue(cellEditor.getDeselectedValue());
			}
			
			parentCell.getTableView().requestFocus();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		@Override
		protected void updateComponentState() throws ModelException
		{
			component.selectedProperty().removeListener(this);
			component.setSelected(cellEditor.getSelectedValue() != null && cellEditor.getSelectedValue().equals(getValue()));
			component.selectedProperty().addListener(this);
			
			component.setAlignment(FXAlignmentUtil.alignmentsToPos(cellEditor, Pos.CENTER_LEFT));
			component.setDisable(!(dataBook.isUpdateAllowed() && !dataBook.getRowDefinition().getColumnDefinition(columnName).isReadOnly()));
			component.setText(cellEditor.getText());
		}
		
	}	// CellCheckBoxEditor
	
	/**
	 * The {@link CheckBoxCellEditorHandler} is the
	 * {@link FXAbstractCellEditorHandler} extension for the
	 * {@link FXCheckBoxCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class CheckBoxCellEditorHandler extends FXAbstractCellEditorHandler<CheckBox, FXCheckBoxCellEditor> implements ICellEditorHandler<CheckBox>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link CheckBoxCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 */
		public CheckBoxCellEditorHandler(FXCheckBoxCellEditor pCellEditor, ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
		{
			super(pCellEditor, pCellEditorListener, new FXCheckBox(), pDataRow, pColumnName);
			
			registerKeyEventFilter();
			
			component.setAlignment(FXAlignmentUtil.alignmentsToPos(cellEditor, Pos.CENTER_LEFT));
			attachValueChangeListener(component.selectedProperty());
			component.setText(cellEditor.getText());
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
			removeValueChangeListener(component.selectedProperty());
			component.setSelected(cellEditor.getSelectedValue() != null && cellEditor.getSelectedValue().equals(getValue()));
			attachValueChangeListener(component.selectedProperty());
			
			if (cellEditorListener.getControl() instanceof IEditorControl)
			{
				if (cellEditor.text == null)
				{
					component.setText(dataRow.getRowDefinition().getColumnDefinition(columnName).getLabel());
				}
				else
				{
					component.setText(cellEditor.getText());
				}
			}
			
			component.setDisable(!shouldBeEnabled());
			
			component.setAlignment(FXAlignmentUtil.alignmentsToPos(cellEditor, Pos.CENTER_LEFT));
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			if (component.isSelected())
			{
				setValue(cellEditor.getSelectedValue());
			}
			else
			{
				setValue(cellEditor.getDeselectedValue());
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void uninstallEditor()
		{
			super.uninstallEditor();
			
			removeValueChangeListener(component.selectedProperty());
		}
		
	}	// CheckBoxCellEditorHandler
	
}	// FXCheckBoxCellEditor

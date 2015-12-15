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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;

import com.sibvisions.rad.ui.celleditor.AbstractChoiceCellEditor;
import com.sibvisions.rad.ui.javafx.ext.FXImageChoiceBox;
import com.sibvisions.rad.ui.javafx.ext.control.table.AbstractDataRowCellContent;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link FXChoiceCellEditor} is the JavafX specific implementation of
 * {@link javax.rad.ui.celleditor.IChoiceCellEditor}.
 * 
 * @author Robert Zenz
 * @see javax.rad.ui.celleditor.IChoiceCellEditor
 * @see FXImageChoiceBox
 */
public class FXChoiceCellEditor extends AbstractChoiceCellEditor<Node>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXChoiceCellEditor}.
	 */
	public FXChoiceCellEditor()
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
		return new ChoiceBoxCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getCellRendererComponent(Node pParentComponent, IDataPage pDataPage, int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected, boolean pHasFocus)
	{
		return new CellImageChoiceEditor(this);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link CellImageChoiceEditor} is the
	 * {@link AbstractDataRowCellContent} extension specific for the
	 * {@link FXChoiceCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class CellImageChoiceEditor extends AbstractDataRowCellContent<FXImageChoiceBox<?>> implements ChangeListener<Object>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link CellImageChoiceEditor}.
		 *
		 * @param pCellEditor the cell editor.
		 */
		public CellImageChoiceEditor(FXChoiceCellEditor pCellEditor)
		{
			super(new FXImageChoiceBox<>(pCellEditor::getAllowedValues, pCellEditor::getImageNames, pCellEditor::getDefaultImageName));
			
			component.setHorizontalAlignment(FXAlignmentUtil.alignmentToHPos(pCellEditor, HPos.CENTER));
			component.setVerticalAlignment(FXAlignmentUtil.alignmentToVPos(pCellEditor, VPos.CENTER));
			component.valueProperty().addListener(this);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void changed(ObservableValue<? extends Object> pObservable, Object pOldValue, Object pNewValue)
		{
			setValue(pNewValue);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void updateComponentState() throws ModelException
		{
			component.valueProperty().removeListener(this);
			component.setValue(getValue());
			component.valueProperty().addListener(this);
		}
		
	}	// CellImageChoiceEditor
	
	/**
	 * The {@link ChoiceBoxCellEditorHandler} is the
	 * {@link FXAbstractCellEditorHandler} extension for the
	 * {@link FXChoiceCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ChoiceBoxCellEditorHandler extends FXAbstractCellEditorHandler<FXImageChoiceBox<?>, FXChoiceCellEditor> implements
			ICellEditorHandler<FXImageChoiceBox<?>>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ChoiceBoxCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 */
		public ChoiceBoxCellEditorHandler(FXChoiceCellEditor pCellEditor, ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
		{
			super(pCellEditor, pCellEditorListener, new FXImageChoiceBox<>(pCellEditor::getAllowedValues, pCellEditor::getImageNames, pCellEditor::getDefaultImageName),
					pDataRow,
					pColumnName);
					
			registerKeyEventFilter();
			
			component.setHorizontalAlignment(FXAlignmentUtil.alignmentToHPos(cellEditor, HPos.CENTER));
			component.setVerticalAlignment(FXAlignmentUtil.alignmentToVPos(cellEditor, VPos.CENTER));
			
			if (isSavingImmediate())
			{
				attachValueChangeListener(component.valueProperty());
			}
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
			component.setHorizontalAlignment(FXAlignmentUtil.alignmentToHPos(cellEditor, HPos.CENTER));
			component.setVerticalAlignment(FXAlignmentUtil.alignmentToVPos(cellEditor, VPos.CENTER));
			
			component.setValue(getValue());
			component.setDisable(!shouldBeEnabled());
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			setValue(component.getValue());
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void uninstallEditor()
		{
			super.uninstallEditor();
			
			removeValueChangeListener(component.valueProperty());
		}
		
	}	// ChoiceBoxCellEditorHandler
	
}	// FXChoiceCellEditor

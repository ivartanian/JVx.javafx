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

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;

import com.sibvisions.rad.ui.celleditor.AbstractNumberCellEditor;
import com.sibvisions.rad.ui.javafx.ext.FXNumberField;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link FXNumberCellEditor} is the JavaFX specific implementation of
 * {@link javax.rad.ui.celleditor.INumberCellEditor}.
 * 
 * @author Robert Zenz
 */
public class FXNumberCellEditor extends AbstractNumberCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXNumberCellEditor}.
	 */
	public FXNumberCellEditor()
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
		return new NumberFieldCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link NumberFieldCellEditorHandler} is an
	 * {@link FXAbstractCellEditorHandler} extension specific for the
	 * {@link FXNumberCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class NumberFieldCellEditorHandler extends FXAbstractCellEditorHandler<FXNumberField, FXNumberCellEditor>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The listener for when the text property changes. */
		private ChangeListener<String> textChangedListener;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link NumberFieldCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 */
		public NumberFieldCellEditorHandler(FXNumberCellEditor pCellEditor, ICellEditorListener pCellEditorListener, IDataRow pDataRow,
				String pColumnName)
		{
			super(pCellEditor, pCellEditorListener, new FXNumberField(), pDataRow, pColumnName);
			
			registerFocusChangedListener();
			registerKeyEventFilter();
			
			textChangedListener = this::onTextChanged;
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
			component.textProperty().removeListener(textChangedListener);
			
			applyDefaultStyling();
			
			component.setAlignment(FXAlignmentUtil.alignmentsToPos(cellEditor, Pos.TOP_RIGHT));
			component.setDisable(!shouldBeEnabled());
			component.setFormat(cellEditor.getNumberFormat());
			
			component.setNumber(getValue());
			
			component.textProperty().addListener(textChangedListener);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			setValue(component.getNumber());
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Invoked if the text in the component changes.
		 * 
		 * @param pObservable the observable value.
		 * @param pOldValue the old value.
		 * @param pNewValue the new value.
		 */
		private void onTextChanged(ObservableValue<? extends String> pObservable, String pOldValue, String pNewValue)
		{
			cellEditorListener.editingStarted();
		}
		
	}	// NumberFieldCellEditorHandler
	
}	// FXNumberCellEditor

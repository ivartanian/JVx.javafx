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
import javax.rad.model.ui.ICellRenderer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import com.sibvisions.rad.ui.celleditor.AbstractTextCellEditor;
import com.sibvisions.rad.ui.javafx.ext.FXPasswordFieldRT39954;
import com.sibvisions.rad.ui.javafx.ext.FXTextArea;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link FXTextCellEditor} is the JavaFX specific implementation of
 * {@link javax.rad.ui.celleditor.ITextCellEditor}.
 * 
 * @author Robert Zenz
 * @see javax.rad.ui.celleditor.ITextCellEditor
 */
public class FXTextCellEditor extends AbstractTextCellEditor implements ICellRenderer<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTextCellEditor}.
	 */
	public FXTextCellEditor()
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
		TextInputControl textComponent = null;
		
		if (contentType != null)
		{
			switch (contentType)
			{
				case TEXT_PLAIN_MULTILINE:
					textComponent = new FXTextArea();
					break;
					
				case TEXT_PLAIN_PASSWORD:
					textComponent = new FXPasswordFieldRT39954();
					setEchoChar(((FXPasswordFieldRT39954)textComponent).getMaskChar());
					break;
					
				case TEXT_PLAIN_WRAPPEDMULTILINE:
					textComponent = new FXTextArea();
					((TextArea)textComponent).setWrapText(true);
					break;
					
				case TEXT_PLAIN_SINGLELINE:
				default:
					textComponent = new TextField();
			}
		}
		else
		{
			textComponent = new TextField();
		}
		
		return new TextCellEditorHandler(this, pCellEditorListener, textComponent, pDataRow, pColumnName);
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
			String value = pDataRow.getValueAsString(pColumnName);
			
			if (value != null && getContentType() == TEXT_PLAIN_PASSWORD)
			{
				value = maskPassword(value);
			}
			
			return value;
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
	 * The {@link TextCellEditorHandler} is a
	 * {@link FXAbstractCellEditorHandler} extension specific for the
	 * {@link FXTextCellEditor}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class TextCellEditorHandler extends FXAbstractCellEditorHandler<TextInputControl, FXTextCellEditor> implements ICellEditorHandler<TextInputControl>
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
		 * Creates a new instance of {@link TextCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pTextComponent the text component.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 */
		public TextCellEditorHandler(FXTextCellEditor pCellEditor, ICellEditorListener pCellEditorListener, TextInputControl pTextComponent, IDataRow pDataRow, String pColumnName)
		{
			super(pCellEditor, pCellEditorListener, pTextComponent, pDataRow, pColumnName);
			
			// TODO Set a dynamic preferred size based on the datatype and hint
			// in the column definition.
			if (component instanceof TextField)
			{
				((TextField)component).setPrefColumnCount(10);
			}
			else if (component instanceof TextArea)
			{
				((TextArea)component).setPrefColumnCount(12);
				((TextArea)component).setPrefRowCount(4);
			}
			
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
			
			if (component instanceof TextField)
			{
				((TextField)component).setAlignment(FXAlignmentUtil.alignmentsToPos(cellEditor));
			}
			else if (component instanceof FXTextArea)
			{
				((FXTextArea)component).setAlignment(FXAlignmentUtil.alignmentToHPos(cellEditor));
			}
			
			component.setDisable(!shouldBeEnabled());
			
			component.setText(getValueAsString());
			
			component.textProperty().addListener(textChangedListener);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			setValue(component.getText());
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
		
	}	// TextCellEditorHandler
	
}	// FXTextCellEditor

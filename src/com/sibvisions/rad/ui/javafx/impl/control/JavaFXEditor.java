/*
 * Copyright 2014 SIB Visions GmbH
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

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.IEditor;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.javafx.ext.control.FXEditor;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;

/**
 * The {@link JavaFXEditor} is the JavaFX specific implementation of
 * {@link IEditor}.
 * 
 * @author Robert Zenz
 * @see IEditor
 * @see FXEditor
 */
public class JavaFXEditor extends JavaFXComponent<FXEditor> implements IEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXEditor}.
	 */
	public JavaFXEditor()
	{
		super(new FXEditor());
		
		styleContainer.setParent((FXEditor)null);
		styleContainer.addUpdateListener(this::notifyRepaint);
		
		resource.setSavingImmediate(true);
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
	public ICellEditor getCellEditor()
	{
		return resource.getCellEditor();
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
	public String getColumnName()
	{
		return resource.getColumnName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataRow getDataRow()
	{
		return resource.getDataRow();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
	{
		return resource.getHorizontalAlignment();
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
	public int getVerticalAlignment()
	{
		return resource.getVerticalAlignment();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBorderVisible()
	{
		return resource.isBorderVisible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSavingImmediate()
	{
		return resource.isSavingImmediate();
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
	public void setBorderVisible(boolean pVisible)
	{
		resource.setBorderVisible(pVisible);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException
	{
		resource.setCellEditor(pCellEditor);
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
	public void setColumnName(String pColumnName) throws ModelException
	{
		resource.setColumnName(pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataRow(IDataRow pDataRow) throws ModelException
	{
		resource.setDataRow(pDataRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		resource.setHorizontalAlignment(pHorizontalAlignment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSavingImmediate(boolean pSavingImmediate)
	{
		resource.setSavingImmediate(pSavingImmediate);
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
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		resource.setVerticalAlignment(pVerticalAlignment);
	}
	
}	// JavaFXEditor

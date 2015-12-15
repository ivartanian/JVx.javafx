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

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.control.IChart;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.javafx.ext.control.chart.FXChart;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;

/**
 * The {@link JavaFXChart} is the JavaFX specific implementation of
 * {@link IChart}.
 */
public class JavaFXChart extends JavaFXComponent<FXChart> implements IChart
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXChart}.
	 */
	public JavaFXChart()
	{
		super(new FXChart());
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
	public int getChartStyle()
	{
		return resource.getChartStyle();
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
	public String getTitle()
	{
		return resource.getTitle();
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
	public String getXAxisTitle()
	{
		return resource.getXAxisTitle();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getXColumnName()
	{
		return resource.getXColumnName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getYAxisTitle()
	{
		return resource.getYAxisTitle();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getYColumnNames()
	{
		return resource.getYColumnNames();
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
	public void setChartStyle(int pChartStyle)
	{
		resource.setChartStyle(pChartStyle);
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
	public void setTitle(String pTitle)
	{
		resource.setTitle(pTitle);
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
	public void setXAxisTitle(String pXAxisTitle)
	{
		resource.setXAxisTitle(pXAxisTitle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setXColumnName(String pXColumnName)
	{
		resource.setXColumnName(pXColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setYAxisTitle(String pYAxisTitle)
	{
		resource.setYAxisTitle(pYAxisTitle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setYColumnNames(String[] pYColumnNames)
	{
		resource.setYColumnNames(pYColumnNames);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startEditing()
	{
		resource.startEditing();
	}
	
}	// JavaFXChart

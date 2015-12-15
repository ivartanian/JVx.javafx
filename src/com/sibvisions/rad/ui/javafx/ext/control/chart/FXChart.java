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
package com.sibvisions.rad.ui.javafx.ext.control.chart;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Region;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.ui.ITableControl;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.javafx.ext.chart.TimestampAxis;
import com.sibvisions.rad.ui.javafx.ext.control.util.FXControlUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link FXChart} is a chart implementation that can be bound to an
 * {@link IDataBook}.
 * 
 * @author Robert Zenz
 */
public class FXChart extends Region implements ITableControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Style constant for showing lines. */
	public static final int STYLE_LINES = 0;
	
	/** Style constant for showing areas. */
	public static final int STYLE_AREA = 1;
	
	/** Style constant for showing bars. */
	public static final int STYLE_BARS = 2;
	
	/** Style constant for showing a pie chart. */
	public static final int STYLE_PIE = 3;
	
	/** If the {@link Chart} should be animated. */
	private boolean chartAnimation;
	
	/** The {@link Chart}. */
	private Chart chart;
	
	/** The style of this chart. */
	private int chartStyle;
	
	/** The {@link IDataBook}. */
	private IDataBook dataBook;
	
	/** The {@link IDataBookListener} for the reload event. */
	private IDataBookListener dataBookListener;
	
	/** The format for dates. */
	private String dateFormat;
	
	/** If this component has been notified and should update. */
	private boolean notified;
	
	/** The title of the chart. */
	private String title;
	
	/** The column name used for the x axis. */
	private String xColumnName;
	
	/** The column names used for the y axis. */
	private String[] yColumnNames;
	
	/** The title of the x axis. */
	private String xAxisTitle;
	
	/** The title of the y axis. */
	private String yAxisTitle;
	
	/** The {@link TranslationMap}. */
	private TranslationMap translationMap;
	
	/** If the translation is enabled. */
	private boolean translationEnabled;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXChart}.
	 */
	public FXChart()
	{
		super();
		
		dataBookListener = this::onDataBookReload;
		
		chartStyle = STYLE_LINES;
		dateFormat = "dd.MM.yyyy";
		translationEnabled = true;
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
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataBook getDataBook()
	{
		return dataBook;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		return translationMap;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTranslationEnabled()
	{
		return translationEnabled;
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
				chartAnimation = false;
				updateChart();
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
		// Nothing to do as charts can't be edited.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataBook(IDataBook pDataBook)
	{
		if (dataBook != null)
		{
			dataBook.eventAfterReload().removeListener(dataBookListener);
			dataBook.removeControl(this);
		}
		
		dataBook = pDataBook;
		
		if (pDataBook != null)
		{
			pDataBook.addControl(this);
			dataBook.eventAfterReload().addListener(dataBookListener);
		}
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		translationMap = pTranslation;
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslationEnabled(boolean pEnabled)
	{
		translationEnabled = pEnabled;
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startEditing()
	{
		// Charts can't be edited.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		if (chart != null)
		{
			chart.resizeRelocate(0, 0, getWidth(), getHeight());
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the chart style.
	 * 
	 * @return the chart style.
	 * @see #STYLE_AREA
	 * @see #STYLE_BARS
	 * @see #STYLE_LINES
	 * @see #STYLE_PIE
	 */
	public int getChartStyle()
	{
		return chartStyle;
	}
	
	/**
	 * Gets the format used for formatting dates.
	 * 
	 * @return the format for dates.
	 */
	public String getDateFormat()
	{
		return dateFormat;
	}
	
	/**
	 * Gets the title.
	 * 
	 * @return the title.
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Gets the title of the x axis.
	 * 
	 * @return the title of the x axis.
	 */
	public String getXAxisTitle()
	{
		return xAxisTitle;
	}
	
	/**
	 * Gets the name of the column for the x axis.
	 * 
	 * @return the name of the column for the x axis.
	 */
	public String getXColumnName()
	{
		return xColumnName;
	}
	
	/**
	 * Gets the title of the y axis.
	 * 
	 * @return the title of the y axis.
	 */
	public String getYAxisTitle()
	{
		return yAxisTitle;
	}
	
	/**
	 * Gets the names of the columns for the y axis.
	 * 
	 * @return the names of the columns for the y axis.
	 */
	public String[] getYColumnNames()
	{
		return yColumnNames;
	}
	
	/**
	 * Sets the chart style.
	 * 
	 * @param pChartStyle the chart style.
	 * @see #STYLE_AREA
	 * @see #STYLE_BARS
	 * @see #STYLE_LINES
	 * @see #STYLE_PIE
	 */
	public void setChartStyle(int pChartStyle)
	{
		chartStyle = pChartStyle;
		
		notifyRepaint();
	}
	
	/**
	 * Sets the format used for formatting dates.
	 * 
	 * @param pDateFormat the format for dates.
	 */
	public void setDateFormat(String pDateFormat)
	{
		dateFormat = pDateFormat;
	}
	
	/**
	 * Sets the title.
	 * 
	 * @param pTitle the title.
	 */
	public void setTitle(String pTitle)
	{
		title = pTitle;
	}
	
	/**
	 * Sets the title of the x axis.
	 * 
	 * @param pXAxisTitle the title of the x axis.
	 */
	public void setXAxisTitle(String pXAxisTitle)
	{
		xAxisTitle = pXAxisTitle;
		
		notifyRepaint();
	}
	
	/**
	 * Sets the name of the column for the x axis.
	 * 
	 * @param pXColumnName the name of the column for the x axis.
	 */
	public void setXColumnName(String pXColumnName)
	{
		xColumnName = pXColumnName;
		
		notifyRepaint();
	}
	
	/**
	 * Sets the title of the y axis.
	 * 
	 * @param pYAxisTitle the title of the y axis.
	 */
	public void setYAxisTitle(String pYAxisTitle)
	{
		yAxisTitle = pYAxisTitle;
		
		notifyRepaint();
	}
	
	/**
	 * Sets the names of the columns for the y axis.
	 * 
	 * @param pYColumnNames the names of the columns for the y axis.
	 */
	public void setYColumnNames(String[] pYColumnNames)
	{
		yColumnNames = pYColumnNames;
		
		notifyRepaint();
	}
	
	/**
	 * Gets the data from the {@link IDataBook} and converts it into a
	 * {@link XYChart.Series}.
	 * 
	 * @param <T> the type of the value.
	 * @param pXValueConverter the converter used for the values of the x axis.
	 * @return the {@link XYChart.Series}.
	 */
	private <T> Collection<XYChart.Series<T, Number>> getDataAsSeries(Function<Object, T> pXValueConverter)
	{
		List<XYChart.Series<T, Number>> seriess = new ArrayList<>();
		
		String sLabel;
		
		for (String yColumnName : yColumnNames)
		{
			Series<T, Number> series = new Series<>();
			
			try
			{
				sLabel = translate(dataBook.getRowDefinition().getColumnDefinition(yColumnName).getLabel());
				
				if (StringUtil.isEmpty(sLabel))
				{
					sLabel = StringUtil.convertMemberNameToText(yColumnName);
				}
				
				series.setName(sLabel);
				
				List<XYChart.Data<T, Number>> data = series.getData();
				
				dataBook.fetchAll();
				
				for (int index = 0; index < dataBook.getRowCount(); index++)
				{
					IDataRow dataRow = dataBook.getDataRow(index);
					
					T xValue = null;
					if (dataRow.getValue(xColumnName) != null)
					{
						xValue = pXValueConverter.apply(dataRow.getValue(xColumnName));
					}
					BigDecimal yValue = (BigDecimal) dataRow.getValue(yColumnName);
					
					data.add(new XYChart.Data<>(xValue, yValue));
				}
			}
			catch (ModelException e)
			{
				e.printStackTrace();
			}
			
			seriess.add(series);
		}
		
		return seriess;
	}
	
	/**
	 * Creates the correct x axis.
	 * 
	 * @return the x axis.
	 */
	private Axis<?> createXAxis()
	{
		if (FXControlUtil.isTimestampColumn(dataBook, xColumnName))
		{
			return new TimestampAxis(dateFormat);
		}
		else
		{
			return new NumberAxis();
		}
	}
	
	/**
	 * Updates the given x axis.
	 * 
	 * @param pXAxis the x axis.
	 */
	private void updateXAxis(Axis<?> pXAxis)
	{
		pXAxis.setLabel(translate(xAxisTitle));
	}
	
	/**
	 * Updates the given y axis.
	 * 
	 * @param pYAxis the y axis.
	 */
	private void updateYAxis(Axis<?> pYAxis)
	{
		pYAxis.setLabel(translate(yAxisTitle));
	}
	
	/**
	 * Invoked if the {@link IDataBook} is reloaded.
	 * 
	 * @param pDataBookEvent the event.
	 */
	private void onDataBookReload(DataBookEvent pDataBookEvent)
	{
		chartAnimation = true;
		
		cancelEditing();
	}
	
	/**
	 * Updates or creates the chart.
	 */
	@SuppressWarnings("unchecked")
	private void updateChart()
	{
		if (dataBook != null && dataBook.isOpen() && !StringUtil.isEmpty(xColumnName) && yColumnNames != null && yColumnNames.length > 0)
		{
			switch (chartStyle)
			{
				case STYLE_AREA:
					if (!(chart instanceof AreaChart))
					{
						chart = new AreaChart<>(createXAxis(), new NumberAxis());
						((AreaChart<?, ?>) chart).setLegendSide(Side.RIGHT);
						chartAnimation = true;
					}
					updateXAxis(((XYChart<?, Number>) chart).getXAxis());
					updateYAxis(((XYChart<?, Number>) chart).getYAxis());
					updateXYChart((XYChart<?, Number>) chart);
					break;
				
				case STYLE_BARS:
					if (!(chart instanceof BarChart))
					{
						chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
						((BarChart<?, ?>) chart).setLegendSide(Side.RIGHT);
						((BarChart<?, ?>) chart).setBarGap(3);
						((BarChart<?, ?>) chart).setCategoryGap(20);
						chartAnimation = true;
					}
					updateXAxis(((XYChart<?, Number>) chart).getXAxis());
					updateYAxis(((XYChart<?, Number>) chart).getYAxis());
					updateXYChart((XYChart<?, Number>) chart);
					break;
				
				case STYLE_PIE:
					if (!(chart instanceof PieChart))
					{
						chart = new PieChart();
						chartAnimation = true;
					}
					updatePieChart((PieChart) chart);
					break;
				
				case STYLE_LINES:
				default:
					if (!(chart instanceof LineChart))
					{
						chart = new LineChart<>(createXAxis(), new NumberAxis());
						((LineChart<?, ?>) chart).setLegendSide(Side.RIGHT);
						chartAnimation = true;
					}
					updateXAxis(((XYChart<?, Number>) chart).getXAxis());
					updateYAxis(((XYChart<?, Number>) chart).getYAxis());
					updateXYChart((XYChart<?, Number>) chart);
					
			}
			
			chart.setTitle(translate(title));
			
			if (getChildren().isEmpty() || getChildren().get(0) != chart)
			{
				getChildren().clear();
				getChildren().add(chart);
			}
		}
		else
		{
			getChildren().clear();
			chart = null;
		}
	}
	
	/**
	 * Translates (if possible, allowed or needed) the given value.
	 * 
	 * @param pValue the value to translate.
	 * @return the translated value.
	 */
	public String translate(String pValue)
	{
		if (translationEnabled && translationMap != null)
		{
			return translationMap.translate(pValue);
		}
		
		return pValue;
	}
	
	/**
	 * Updates the given {@link PieChart} with new data.
	 * 
	 * @param pPieChart the {@link PieChart} to update.
	 */
	private void updatePieChart(PieChart pPieChart)
	{
		pPieChart.setAnimated(chartAnimation);
		
		List<PieChart.Data> data = pPieChart.getData();
		
		for (String yColumnName : yColumnNames)
		{
			try
			{
				dataBook.fetchAll();
				
				for (int index = 0; index < dataBook.getRowCount(); index++)
				{
					IDataRow dataRow = dataBook.getDataRow(index);
					data.add(new PieChart.Data(dataRow.getValueAsString(xColumnName), ((BigDecimal) dataRow.getValue(yColumnName)).doubleValue()));
				}
			}
			catch (ModelException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Updates the given {@link XYChart} with new data.
	 * 
	 * @param pChart the {@link XYChart} to update.
	 */
	@SuppressWarnings("unchecked")
	private void updateXYChart(XYChart<?, Number> pChart)
	{
		pChart.setAnimated(chartAnimation);
		
		pChart.getData().clear();
		
		if (pChart instanceof BarChart)
		{
			if (FXControlUtil.isTimestampColumn(dataBook, xColumnName))
			{
				SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				((XYChart<String, Number>) pChart).getData().addAll(getDataAsSeries(pValue -> formatter.format((Timestamp) pValue)));
			}
			else if (FXControlUtil.isNumberColumn(dataBook, xColumnName))
			{
				((XYChart<String, Number>) pChart).getData().addAll(getDataAsSeries(pValue -> ((Number) pValue).toString()));
			}
			else
			{
				((XYChart<String, Number>) pChart).getData().addAll(getDataAsSeries(pValue -> Objects.toString(pValue)));
			}
		}
		else if (FXControlUtil.isTimestampColumn(dataBook, xColumnName))
		{
			((XYChart<Timestamp, Number>) pChart).getData().addAll(getDataAsSeries(pValue -> (Timestamp) pValue));
		}
		else
		{
			((XYChart<Number, Number>) pChart).getData().addAll(getDataAsSeries(pValue -> (Number) pValue));
		}
	}
	
}	// FXChart

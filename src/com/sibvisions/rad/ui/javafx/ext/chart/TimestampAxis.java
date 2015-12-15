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
package com.sibvisions.rad.ui.javafx.ext.chart;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.chart.Axis;

/**
 * The {@link TimestampAxis} is an {@link Axis} extension that allows
 * {@link Timestamp}s as values.
 * 
 * @author Robert Zenz
 */
public class TimestampAxis extends Axis<Timestamp>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** One day represented in milliseconds. */
	private static final long DAY_IN_MILLISECONDS = 60L * 60L * 24L * 1000L;
	
	/** The current {@link Bounds}. */
	private Bounds bounds;
	
	/** The {@link SimpleDateFormat} used for formatting. */
	private SimpleDateFormat formatter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link TimestampAxis}.
	 */
	public TimestampAxis()
	{
		this("dd.MM.yyyy");
	}
	
	/**
	 * Creates a new instance of {@link TimestampAxis}.
	 *
	 * @param pFormat the format of the ticks/labels (date format).
	 */
	public TimestampAxis(String pFormat)
	{
		bounds = new Bounds();
		formatter = new SimpleDateFormat(pFormat);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDisplayPosition(Timestamp pValue)
	{
		if (bounds.getLower() != null && bounds.getUpper() != null)
		{
			double lower = bounds.getLower().getTime();
			double upper = bounds.getUpper().getTime();
			
			double value = pValue.getTime();
			
			return (value - lower) / (upper - lower) * getWidth();
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getValueForDisplay(double pDisplayPosition)
	{
		if (bounds.getLower() != null && bounds.getUpper() != null)
		{
			long lower = bounds.getLower().getTime();
			long upper = bounds.getUpper().getTime();
			
			return new Timestamp(Double.valueOf(pDisplayPosition / getWidth() * (upper - lower) + lower).longValue());
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZeroPosition()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValueOnAxis(Timestamp pValue)
	{
		if (bounds.getLower() != null && bounds.getUpper() != null)
		{
			return (bounds.getLower().equals(pValue) || bounds.getLower().before(pValue))
					&& (bounds.getUpper().equals(pValue) || bounds.getUpper().after(pValue));
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double toNumericValue(Timestamp pValue)
	{
		return pValue.getTime();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp toRealValue(double pValue)
	{
		return new Timestamp(Double.valueOf(pValue).longValue());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object autoRange(double pLength)
	{
		return bounds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Timestamp> calculateTickValues(double pLength, Object pRange)
	{
		Bounds providedBounds = (Bounds)pRange;
		
		if (providedBounds.getLower() != null && providedBounds.getUpper() != null)
		{
			List<Timestamp> ticks = new ArrayList<>();
			
			ticks.add(providedBounds.getLower());
			
			Timestamp tick = providedBounds.getLower();
			// Make the timestamp midnight.
			tick = new Timestamp(tick.getTime() - tick.getTime() % DAY_IN_MILLISECONDS);
			
			while (tick.before(providedBounds.getUpper()))
			{
				tick = new Timestamp(tick.getTime() + DAY_IN_MILLISECONDS);
				ticks.add(tick);
			}
			
			ticks.add(providedBounds.getUpper());
			
			return ticks;
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getRange()
	{
		return bounds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getTickMarkLabel(Timestamp pValue)
	{
		return formatter.format(pValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setRange(Object pRange, boolean pAnimate)
	{
		bounds = (Bounds)pRange;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidateRange(List<Timestamp> pData)
	{
		bounds.setLower(null);
		bounds.setUpper(null);
		
		for (Timestamp timestamp : pData)
		{
			if (bounds.getLower() == null || timestamp.before(bounds.getLower()))
			{
				bounds.setLower(timestamp);
			}
			
			if (bounds.getUpper() == null || timestamp.after(bounds.getUpper()))
			{
				bounds.setUpper(timestamp);
			}
		}
		
		super.invalidateRange(pData);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple container for lower and upper bounds.
	 * 
	 * @author Robert Zenz
	 */
	private static final class Bounds
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The lower bound. */
		private Timestamp lower;
		
		/** The upper bound. */
		private Timestamp upper;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link Bounds}.
		 */
		public Bounds()
		{
			super();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the lower bound.
		 *
		 * @return the lower bound.
		 */
		public Timestamp getLower()
		{
			return lower;
		}
		
		/**
		 * Gets the upper bound.
		 *
		 * @return the upper bound.
		 */
		public Timestamp getUpper()
		{
			return upper;
		}
		
		/**
		 * Sets the lower bound.
		 *
		 * @param pLower the new lower bound.
		 */
		public void setLower(Timestamp pLower)
		{
			lower = pLower;
		}
		
		/**
		 * Sets the upper bound.
		 *
		 * @param pUpper the new upper bound.
		 */
		public void setUpper(Timestamp pUpper)
		{
			upper = pUpper;
		}
		
	}	// Bounds
	
}	// TimestampAxis

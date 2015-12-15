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
package com.sibvisions.rad.ui.javafx.impl.layout;

import java.util.HashMap;
import java.util.Map;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IGridLayout;
import javax.rad.ui.layout.IGridLayout.IGridConstraints;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import com.sibvisions.rad.ui.javafx.impl.JavaFXResource;

/**
 * The {@link JavaFXGridLayout} is the JavaFX specific implementation of
 * {@link IGridLayout}.
 * 
 * @author Robert Zenz
 * @see IGridLayout
 * @see GridPane
 */
public class JavaFXGridLayout extends JavaFXAbstractLayoutContainerHybrid<GridPane, IGridConstraints> implements IGridLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The number of columns. */
	private int columns;
	
	/** The {@link Map} of constraints. */
	private Map<IComponent, IGridConstraints> constraints;
	
	/** The number of rows. */
	private int rows;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXGridLayout}.
	 *
	 * @param pColumns the number of columns.
	 * @param pRows the number of rows.
	 */
	public JavaFXGridLayout(int pColumns, int pRows)
	{
		super(new GridPane());
		
		columns = pColumns;
		rows = pRows;
		
		constraints = new HashMap<>(columns * rows);
		
		horizontalGap = (int)resource.getHgap();
		verticalGap = (int)resource.getVgap();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pConstraints instanceof IGridConstraints)
		{
			IGridConstraints gridConstraints = (IGridConstraints)pConstraints;
			constraints.put(pComponent, gridConstraints);
			
			resource.add((Node)pComponent.getResource(),
					gridConstraints.getGridX(),
					gridConstraints.getGridY(),
					gridConstraints.getGridWidth(),
					gridConstraints.getGridHeight());
					
			setGrowth(pComponent);
		}
		else if (!resource.getChildren().isEmpty())
		{
			// We got no usable constraints, so let's try to insert it after
			// the last added component.
			int componentCount = resource.getChildren().size();
			int column = componentCount % columns;
			int row = componentCount / columns;
			
			add(pComponent, getConstraints(column, row), pIndex);
		}
		else
		{
			// First component, that one's easy.
			add(pComponent, getConstraints(0, 0), pIndex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumns()
	{
		return columns;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGridConstraints getConstraints(IComponent pComp)
	{
		return constraints.get(pComp);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGridConstraints getConstraints(int pColumns, int pRows)
	{
		return getConstraints(pColumns, pRows, 1, 1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight)
	{
		return getConstraints(pColumns, pRows, pWidth, pHeight, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, IInsets pInsets)
	{
		return new JavaFXGridConstraints(pColumns, pRows, pWidth, pHeight, pInsets);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRows()
	{
		return rows;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		resource.getChildren().remove(pComponent.getResource());
		constraints.remove(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		resource.getChildren().clear();
		constraints.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumns(int pColumns)
	{
		columns = pColumns;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConstraints(IComponent pComponent, IGridConstraints pConstraints)
	{
		constraints.put(pComponent, pConstraints);
		
		GridPane.setConstraints((Node)pComponent.getResource(),
				pConstraints.getGridX(),
				pConstraints.getGridY(),
				pConstraints.getGridWidth(),
				pConstraints.getGridHeight());
				
		setGrowth(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRows(int pRows)
	{
		rows = pRows;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGaps()
	{
		resource.setHgap(horizontalGap);
		resource.setVgap(verticalGap);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReverseOrderNeeded()
	{
		return true;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the correct growth for the given {@link IComponent}.
	 * 
	 * @param pComponent the {@link IComponent}.
	 */
	private void setGrowth(IComponent pComponent)
	{
		GridPane.setHgrow((Node)pComponent.getResource(), Priority.ALWAYS);
		GridPane.setVgrow((Node)pComponent.getResource(), Priority.ALWAYS);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link JavaFXGridConstraints} is the JavaFX specific implementation
	 * of {@link IGridConstraints}.
	 * 
	 * @see IGridConstraints
	 */
	public static class JavaFXGridConstraints extends JavaFXResource<IGridConstraints> implements IGridConstraints
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The number of cells this constraint spans vertically. */
		private int height;
		
		/** The insets. */
		private IInsets insets;
		
		/** The number of cells this constraint spans horizontally. */
		private int width;
		
		/** The starting column. */
		private int x;
		
		/** The starting row. */
		private int y;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link JavaFXGridConstraints}.
		 *
		 * @param pX the starting column.
		 * @param pY the starting row.
		 * @param pWidth the width.
		 * @param pHeight the number of cells this constraint spans vertically.
		 * @param pInsets the number of cells this constraint spans
		 *            horizontally.
		 */
		public JavaFXGridConstraints(int pX, int pY, int pWidth, int pHeight, IInsets pInsets)
		{
			super(null);
			
			x = pX;
			y = pY;
			width = pWidth;
			height = pHeight;
			insets = pInsets;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getGridHeight()
		{
			return height;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getGridWidth()
		{
			return width;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getGridX()
		{
			return x;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getGridY()
		{
			return y;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IInsets getInsets()
		{
			return insets;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setGridHeight(int pGridHeight)
		{
			height = pGridHeight;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setGridWidth(int pGridWidth)
		{
			width = pGridWidth;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setGridX(int pGridX)
		{
			x = pGridX;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setGridY(int pGridY)
		{
			y = pGridY;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setInsets(IInsets pInsets)
		{
			insets = pInsets;
		}
		
	}	// JavaFXGridConstraints
	
}	// JavaFXGridLayout

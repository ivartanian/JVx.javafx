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

import java.io.ByteArrayInputStream;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.ui.celleditor.IImageViewer;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;

import com.sibvisions.rad.ui.celleditor.AbstractImageViewer;
import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.control.table.AbstractDataRowCellContent;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link FXImageViewer} is the JavaFX specific implementation of
 * {@link IImageViewer}.
 * 
 * @author Robert Zenz
 * @see IImageViewer
 */
public class FXImageViewer extends AbstractImageViewer<Node>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXImageViewer}.
	 */
	public FXImageViewer()
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
		return new ImageRegionCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getCellRendererComponent(Node pParentComponent, IDataPage pDataPage, int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected, boolean pHasFocus)
	{
		return new CellImageRegionEditor(this);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link CellImahgeRegionEditor} is the
	 * {@link AbstractDataRowCellContent} extension for the
	 * {@link FXImageViewer}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class CellImageRegionEditor extends AbstractDataRowCellContent<FXImageRegion>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The parent {@link IImageViewer}. */
		private IImageViewer<?> imageViewer;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link CellImageRegionEditor}.
		 *
		 * @param pImageViewer the parent {@link IImageViewer}.
		 */
		public CellImageRegionEditor(IImageViewer<?> pImageViewer)
		{
			super(new FXImageRegion());
			
			imageViewer = pImageViewer;
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
			component.setHorizontalAlignment(FXAlignmentUtil.alignmentToHPos(imageViewer, HPos.CENTER));
			component.setVerticalAlignment(FXAlignmentUtil.alignmentToVPos(imageViewer, VPos.CENTER));
			
			byte[] value = getValue();
			
			if (value != null)
			{
				component.setImage(new Image(new ByteArrayInputStream(value)));
			}
			else
			{
				component.setImage(null);
			}
		}
		
	}	// CellImageRegionEditor
	
	/**
	 * The {@link ImageRegionCellEditorHandler} is the
	 * {@link FXAbstractCellEditorHandler} extension for {@link FXImageViewer}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ImageRegionCellEditorHandler extends FXAbstractCellEditorHandler<FXImageRegion, FXImageViewer> implements ICellEditorHandler<FXImageRegion>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ImageRegionCellEditorHandler}.
		 *
		 * @param pCellEditor the cell editor.
		 * @param pCellEditorListener the cell editor listener.
		 * @param pDataRow the data row.
		 * @param pColumnName the column name.
		 */
		public ImageRegionCellEditorHandler(FXImageViewer pCellEditor, ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
		{
			super(pCellEditor, pCellEditorListener, new FXImageRegion(), pDataRow, pColumnName);
			
			component.setHorizontalAlignment(FXAlignmentUtil.alignmentToHPos(cellEditor, HPos.CENTER));
			component.setVerticalAlignment(FXAlignmentUtil.alignmentToVPos(cellEditor, VPos.CENTER));
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
			
			byte[] value = getValue();
			
			if (value != null)
			{
				component.setImage(new Image(new ByteArrayInputStream(value)));
			}
			else
			{
				component.setImage(null);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException
		{
			// There is no editing here.
		}
		
	}	// ImageRegionCellEditorHandler
	
}	// FXImageViewer

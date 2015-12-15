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
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.control.ITree;
import javax.rad.util.TranslationMap;

import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;

import com.sibvisions.rad.ui.javafx.ext.control.tree.FXDataBooksTree;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;

/**
 * The {@link JavaFXTree} is the JavaFX specific implementation of {@link ITree}
 * .
 * 
 * @author Robert Zenz
 */
public class JavaFXTree extends JavaFXComponent<FXDataBooksTree> implements ITree
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The array of {@link IDataBook}s that are used. */
	private IDataBook[] dataBooks;
	
	/** If the current mouse event is over the selected cell. */
	private boolean mouseEventOnSelectedCell;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXTree}.
	 */
	public JavaFXTree()
	{
		super(new FXDataBooksTree());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * At the moment the tree can't be edited.
	 */
	@Override
	public void cancelEditing()
	{
		// TODO The tree can't be edited.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataBook getActiveDataBook()
	{
		return resource.getActiveDataBook();
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
	public IDataBook[] getDataBooks()
	{
		return dataBooks;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public INodeFormatter getNodeFormatter()
	{
		return resource.getNodeFormatter();
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
	public boolean isDetectEndNode()
	{
		return resource.isLeafDetectionEnabled();
	}
	
	/**
	 * At the moment the tree can't be edited.
	 * 
	 * @return always {@code false}.
	 */
	@Override
	public boolean isEditable()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMouseEventOnSelectedCell()
	{
		return mouseEventOnSelectedCell;
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
	 * At the moment the tree can't be edited.
	 */
	@Override
	public void saveEditing() throws ModelException
	{
		// TODO The tree can't be edited.
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
	public void setDataBooks(IDataBook... pDataBooks)
	{
		dataBooks = pDataBooks;
		
		resource.getDataBooks().clear();
		
		if (pDataBooks != null)
		{
			resource.getDataBooks().addAll(pDataBooks);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDetectEndNode(boolean pDetectEndNode)
	{
		resource.setLeafDetectionEnabled(pDetectEndNode);
	}
	
	/**
	 * At the moment the tree can't be edited.
	 * 
	 * @param pEditable Ignored.
	 */
	@Override
	public void setEditable(boolean pEditable)
	{
		// TODO The tree can't be edited.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNodeFormatter(INodeFormatter pNodeFormatter)
	{
		resource.setNodeFormatter(pNodeFormatter);
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
	 * At the moment the tree can't be edited.
	 */
	@Override
	public void startEditing()
	{
		// TODO The tree can't be edited.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireMouseClickedEvent(MouseEvent pMouseEvent)
	{
		mouseEventOnSelectedCell = isOnSelectedCell(pMouseEvent);
		
		super.fireMouseClickedEvent(pMouseEvent);
		
		mouseEventOnSelectedCell = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireMousePressedEvent(MouseEvent pMouseEvent)
	{
		mouseEventOnSelectedCell = isOnSelectedCell(pMouseEvent);
		
		super.fireMousePressedEvent(pMouseEvent);
		
		mouseEventOnSelectedCell = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireMouseReleasedEvent(MouseEvent pMouseEvent)
	{
		mouseEventOnSelectedCell = isOnSelectedCell(pMouseEvent);
		
		super.fireMouseReleasedEvent(pMouseEvent);
		
		mouseEventOnSelectedCell = false;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if the given {@link MouseEvent} is on the selected cell.
	 * 
	 * @param pMouseEvent the {@link MouseEvent}.
	 * @return {@code true} if the given event is on the selected cell.
	 */
	private boolean isOnSelectedCell(MouseEvent pMouseEvent)
	{
		EventTarget target = pMouseEvent.getTarget();
		
		if (target instanceof Node)
		{
			Node targetNode = (Node)target;
			
			while (targetNode != null && !(targetNode instanceof FXDataBooksTree))
			{
				if (targetNode instanceof TreeCell<?>)
				{
					return ((TreeCell<?>)targetNode).isSelected();
				}
				
				targetNode = targetNode.getParent();
			}
		}
		
		return false;
	}
	
}	// JavaFXTree

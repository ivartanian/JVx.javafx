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
package com.sibvisions.rad.ui.javafx.ext.scene;

import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The {@link MouseController} controls the behavior of the
 * {@link StackedScenePane}.
 * 
 * @author René Jahn
 */
public class MouseController
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the styled scene. */
	private StackedScenePane pane;
	
	/** the bounds before moving via header. */
	private Bounds boHeaderStart;
	
	/** the current drag start X coordinate. */
	private double dCurrentDragX;
	
	/** the current drag start Y coordinate. */
	private double dCurrentDragY;
	
	/** the drag start. */
	private double dDragStartY;
	
	/** the current resize start X coordinate. */
	private double dCurrentResizeX;
	
	/** the current resize start Y coordinate. */
	private double dCurrentResizeY;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>MouseController</code> for the given
	 * styled scene.
	 * 
	 * @param pPane the scene pane.
	 */
	MouseController(StackedScenePane pPane)
	{
		pane = pPane;
		
		install();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Installs mouse event listeners.
	 */
	private void install()
	{
		//--------------------------------------------------------------
		// Header -> maximize, Drag around the stage
		//--------------------------------------------------------------
		
		Node header = pane.getHeaderPane();
		
		header.setOnMousePressed(e ->
		{
			if (e.isPrimaryButtonDown())
			{
				Stage stage = pane.getStage();
				
				boHeaderStart = new BoundingBox(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
				
				dCurrentDragX = e.getScreenX();
				dCurrentDragY = e.getScreenY();
				
				dDragStartY = dCurrentDragY - e.getY() - pane.getBorderSize();
			}
			else
			{
				boHeaderStart = null;
				
				dCurrentDragX = -1;
				dCurrentDragY = -1;
			}
		});
		
		header.setOnMouseReleased(e ->
		{
			if (dCurrentDragX != -1)
			{
				((Node)e.getSource()).setCursor(Cursor.DEFAULT);
				
				pane.setCache(false);
				
				boHeaderStart = null;
			}
			
			boHeaderStart = null;
			
			dCurrentDragX = -1;
			dCurrentDragY = -1;
			
			dDragStartY = -1;
		});
		
		header.setOnMouseDragged(e ->
		{
			Stage stage = pane.getStage();
			
			if (!pane.isCache())
			{
				pane.setCache(true);
			}
			
			// Checks
			//-----------------------------
			
			if (dCurrentDragX == -1
					|| !e.isPrimaryButtonDown()
					|| stage.isFullScreen())
			{
				return;
			}
			
			//an event will be generated while pressed (wtf)
			if (e.isStillSincePress())
			{
				return;
			}
			
			// Drag handling
			//-----------------------------
			
			//was maximized -> back to normal
			if (stage.isMaximized())
			{
				pane.setRestoreBounds(null);
				
				Bounds boundsFull = pane.getBoundsInParent();
				
				//after: reduces jumping
				pane.maximizeProperty().set(false);
				
				Bounds boundsNormal = pane.getBoundsInParent();
				
				if (boundsNormal != null)
				{
					double dMouseX = boundsNormal.getWidth() * (e.getScreenX() / boundsFull.getWidth());
					
					stage.setX(e.getScreenX() - dMouseX);
					stage.setY(e.getScreenY() - e.getY() - pane.getActiveShadowSize() - pane.getBorderSize());
				}
			}
			else
			{
				double dX = e.getScreenX();
				double dY = e.getScreenY();
				
				double dTopDockArea = pane.getActiveShadowSize() + pane.getBorderSize();
				
				//support top-docking if border is in dock area
				if ((dY <= 3 || dY - e.getY() - pane.getBorderSize() <= 3) && dDragStartY > dTopDockArea)
				{
					e.consume();
					
					pane.setRestoreBounds(boHeaderStart);
					
					dCurrentDragX = -1;
					dCurrentDragY = -1;
					
					pane.setCache(false);
					
					pane.maximizeProperty().set(true);
					
					((Node)e.getSource()).setCursor(Cursor.DEFAULT);
					
					return;
				}
				
				//if dragging was started in top dock area and moved out of the area -> allow top docking again!
				if (dDragStartY <= dTopDockArea && dY > dTopDockArea)
				{
					dDragStartY = dCurrentDragY - e.getY() - pane.getBorderSize();
				}
				
				double dDiffX = dX - dCurrentDragX;
				double dDiffY = dY - dCurrentDragY;
				
				double dOldStartY = dCurrentDragY;
				
				dCurrentDragX = dX;
				dCurrentDragY = dY;
				
				((Node)e.getSource()).setCursor(Cursor.MOVE);
				
				stage.setX(stage.getX() + dDiffX);
				
				double dCalculatedY = stage.getY() + dDiffY;
				
				//don't drag under menubars (win, mac, ...)
				try
				{
					ObservableList<Screen> olScreens = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
					
					if (olScreens.size() > 0)
					{
						Screen screen = olScreens.get(0);
						Rectangle2D visualBounds = screen.getVisualBounds();
						
						if (dCalculatedY < visualBounds.getHeight() - 30 && dCalculatedY + pane.getActiveShadowSize() >= visualBounds.getMinY())
						{
							stage.setY(dCalculatedY);
						}
						else
						{
							dCurrentDragY = dOldStartY;
						}
					}
					else
					{
						stage.setY(dCalculatedY);
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			e.consume();
		});
		
		header.setOnMouseClicked(e ->
		{
			Stage stage = pane.getStage();
			
			if (!stage.isFullScreen()
					&& stage.isResizable()
					&& e.getButton() == MouseButton.PRIMARY
					&& e.getClickCount() > 1)
			{
				pane.maximizeProperty().set(!pane.maximizeProperty().get());
			}
		});
		
		//--------------------------------------------------------------
		// Header -> resize
		//--------------------------------------------------------------
		
		Rectangle back = pane.getBackgroundRect();
		
		back.setOnMouseExited(e ->
		{
			if (dCurrentResizeX == -1)
			{
				back.setCursor(Cursor.DEFAULT);
			}
		});
		
		back.setOnMouseMoved(e ->
		{
			Stage stage = pane.getStage();
			
			if (stage.isMaximized()
					|| stage.isFullScreen()
					|| !stage.isResizable())
			{
				return;
			}
			
			double x = e.getX();
			double y = e.getY();
			
			Bounds boundsInParent = back.getBoundsInParent();
			
			int iBorderSize = pane.getBorderSize();
			double iArcWidth = back.getArcWidth() / 2;
			double iArcHeight = back.getArcHeight() / 2;
			
			if (x <= Math.max(iBorderSize, iArcWidth) && y <= Math.max(iBorderSize, iArcHeight))
			{
				//top-left corner
				back.setCursor(Cursor.NW_RESIZE);
			}
			else if (x >= boundsInParent.getWidth() - Math.max(iBorderSize, iArcWidth)
					&& y <= Math.max(iBorderSize, iArcHeight))
			{
				//top-right corner
				back.setCursor(Cursor.NE_RESIZE);
			}
			else if (x <= Math.max(iBorderSize, iArcWidth) && y >= boundsInParent.getHeight() - Math.max(iBorderSize, iArcHeight))
			{
				//bottom-left corner
				back.setCursor(Cursor.SW_RESIZE);
			}
			else if (x >= boundsInParent.getWidth() - Math.min(iBorderSize, iArcWidth)
					&& y >= boundsInParent.getHeight() - Math.max(iBorderSize, iArcHeight))
			{
				//bottom-right corner
				back.setCursor(Cursor.SE_RESIZE);
			}
			else if (x <= iBorderSize)
			{
				//left border
				back.setCursor(Cursor.W_RESIZE);
			}
			else if (x >= boundsInParent.getWidth() - iBorderSize)
			{
				//right border
				back.setCursor(Cursor.E_RESIZE);
			}
			else if (y <= iBorderSize)
			{
				//top
				back.setCursor(Cursor.N_RESIZE);
			}
			else if (y >= boundsInParent.getHeight() - iBorderSize)
			{
				//bottom
				back.setCursor(Cursor.S_RESIZE);
			}
		});
		
		back.setOnMousePressed(e ->
		{
			if (e.isPrimaryButtonDown())
			{
				dCurrentResizeX = e.getScreenX();
				dCurrentResizeY = e.getScreenY();
				
				e.consume();
			}
		});
		
		back.setOnMouseReleased(e ->
		{
			dCurrentResizeX = -1;
			dCurrentResizeY = -1;
		});
		
		back.setOnMouseDragged(e ->
		{
			Stage stage = pane.getStage();
			
			// Checks
			//-----------------------------
			
			if (dCurrentResizeX == -1
					|| !e.isPrimaryButtonDown()
					|| stage.isFullScreen())
			{
				return;
			}
			
			//an event will be generated while pressed (wtf)
			if (e.isStillSincePress())
			{
				return;
			}
			
			// Drag handling
			//-----------------------------
			
			double dX = e.getScreenX();
			double dY = e.getScreenY();
			double dDiffX = dX - dCurrentResizeX;
			double dDiffY = dY - dCurrentResizeY;
			
			Cursor cursor = back.getCursor();
			
			if (Cursor.E_RESIZE.equals(cursor))
			{
				if (setAndCheckWidth(stage, stage.getWidth() + dDiffX))
				{
					dCurrentResizeX = dX;
				}
				
				e.consume();
			}
			else if (Cursor.W_RESIZE.equals(cursor))
			{
				if (setAndCheckWidth(stage, stage.getWidth() - dDiffX))
				{
					dCurrentResizeX = dX;
					
					stage.setX(stage.getX() + dDiffX);
				}
				
				e.consume();
			}
			else if (Cursor.N_RESIZE.equals(cursor))
			{
				if (setAndCheckHeight(stage, stage.getHeight() - dDiffY))
				{
					dCurrentResizeY = dY;
					
					setY(stage, stage.getY() + dDiffY);
				}
				
				e.consume();
			}
			else if (Cursor.S_RESIZE.equals(cursor))
			{
				if (setAndCheckHeight(stage, stage.getHeight() + dDiffY))
				{
					dCurrentResizeY = dY;
				}
				
				e.consume();
			}
			else if (Cursor.NW_RESIZE.equals(cursor))
			{
				if (setAndCheckWidth(stage, stage.getWidth() - dDiffX))
				{
					dCurrentResizeX = dX;
					
					stage.setX(stage.getX() + dDiffX);
				}
				
				if (setAndCheckHeight(stage, stage.getHeight() - dDiffY))
				{
					dCurrentResizeY = dY;
					
					setY(stage, stage.getY() + dDiffY);
				}
				
				e.consume();
			}
			else if (Cursor.NE_RESIZE.equals(cursor))
			{
				if (setAndCheckHeight(stage, stage.getHeight() - dDiffY))
				{
					dCurrentResizeY = dY;
					
					setY(stage, stage.getY() + dDiffY);
				}
				
				if (setAndCheckWidth(stage, stage.getWidth() + dDiffX))
				{
					dCurrentResizeX = dX;
				}
				
				e.consume();
			}
			else if (Cursor.SW_RESIZE.equals(cursor))
			{
				if (setAndCheckWidth(stage, stage.getWidth() - dDiffX))
				{
					dCurrentResizeX = dX;
					
					stage.setX(stage.getX() + dDiffX);
				}
				
				if (setAndCheckHeight(stage, stage.getHeight() + dDiffY))
				{
					dCurrentResizeY = dY;
				}
				
				e.consume();
			}
			else if (Cursor.SE_RESIZE.equals(cursor))
			{
				if (setAndCheckWidth(stage, stage.getWidth() + dDiffX))
				{
					dCurrentResizeX = dX;
				}
				
				if (setAndCheckHeight(stage, stage.getHeight() + dDiffY))
				{
					dCurrentResizeY = dY;
				}
				
				e.consume();
			}
		});
	}
	
	/**
	 * Sets the width of the given stage, if possible.
	 * 
	 * @param pStage the stage
	 * @param pWidth the calculated new width
	 * @return <code>true</code> if width was changed, <code>false</code>
	 *         otherwise
	 */
	private boolean setAndCheckWidth(Stage pStage, double pWidth)
	{
		if (pWidth >= Math.max(pane.getDefaultMinStageWidth(), pStage.getMinWidth()))
		{
			pStage.setWidth(pWidth);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sets the height of the given stage, if possible.
	 * 
	 * @param pStage the stage
	 * @param pHeight the calculated new height
	 * @return <code>true</code> if height was changed, <code>false</code>
	 *         otherwise
	 */
	private boolean setAndCheckHeight(Stage pStage, double pHeight)
	{
		if (pHeight >= Math.max(pane.getDefaultMinStageHeight(), pStage.getMinHeight()))
		{
			pStage.setHeight(pHeight);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sets the Y position of the given stage, if possible.
	 * 
	 * @param pStage the stage
	 * @param pY the calculated new Y position
	 */
	private void setY(Stage pStage, double pY)
	{
		try
		{
			ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(pStage.getX(), pStage.getY(), pStage.getWidth(), pStage.getHeight());
			
			if (screensForRectangle.size() > 0)
			{
				Screen screen = screensForRectangle.get(0);
				
				Rectangle2D visualBounds = screen.getVisualBounds();
				
				if (pY < visualBounds.getHeight() - pane.getActiveShadowSize() * 2 && pY + pane.getActiveShadowSize() >= visualBounds.getMinY())
				{
					pStage.setY(pY);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}	// MouseController

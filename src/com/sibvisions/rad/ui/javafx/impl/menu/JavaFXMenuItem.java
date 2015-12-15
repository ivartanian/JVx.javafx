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
package com.sibvisions.rad.ui.javafx.impl.menu;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.util.FXFrameWaitUtil;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;
import com.sibvisions.rad.ui.javafx.impl.JavaFXFactory;
import com.sibvisions.rad.ui.javafx.impl.JavaFXUtil;

/**
 * The {@link JavaFXMenuItem} is the JavaFX specific implementation of
 * {@link IMenuItem}.
 * 
 * @author Robert Zenz
 * @see IMenuItem
 * @see MenuItem
 */
public class JavaFXMenuItem extends JavaFXMenuBase<MenuItem> implements IMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Key} used as accelerator. */
	private Key accelerator;
	
	/** The action command. */
	private String actionCommand;
	
	/** The {@link ActionHandler} for the action event. */
	private ActionHandler eventAction;
	
	/** The {@link IImage}. */
	private IImage image;
	
	/** The {@link IInsets margins}. */
	private IInsets margins;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXMenuItem}.
	 */
	public JavaFXMenuItem()
	{
		super(new MenuItem());
		
		resource.setOnAction(this::onAction);
	}
	
	/**
	 * Creates a new instance of {@link JavaFXMenuItem}.
	 *
	 * @param pMenuItem the menu item top use as resource.
	 */
	protected JavaFXMenuItem(MenuItem pMenuItem)
	{
		super(pMenuItem);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionHandler eventAction()
	{
		if (eventAction == null)
		{
			eventAction = new ActionHandler();
		}
		
		return eventAction;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Key getAccelerator()
	{
		return accelerator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getActionCommand()
	{
		return actionCommand;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@link #ALIGN_DEFAULT}.
	 */
	@Override
	public int getHorizontalAlignment()
	{
		// Not supported/possible.
		return ALIGN_DEFAULT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getImage()
	{
		return image;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInsets getMargins()
	{
		return margins;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText()
	{
		return resource.getText();
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@link #ALIGN_DEFAULT}.
	 */
	@Override
	public int getVerticalAlignment()
	{
		// Not supported/possible.
		return ALIGN_DEFAULT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccelerator(Key pKey)
	{
		accelerator = pKey;
		
		resource.setAccelerator(JavaFXFactory.keyToKeyCombination(accelerator));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActionCommand(String pActionCommand)
	{
		actionCommand = pActionCommand;
	}
	
	/**
	 * Not supported, does nothing.
	 * 
	 * @param pHorizontalAlignment ignored.
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		// Not supported/possible.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImage(IImage pImage)
	{
		image = pImage;
		
		if (image != null)
		{
			FXImageRegion imageRegion = new FXImageRegion((Image) image.getResource());
			imageRegion.setHorizontalStretched(true);
			imageRegion.setMaxSize(16, 16);
			imageRegion.setPreserveRatio(true);
			imageRegion.setVerticalStretched(true);
			
			resource.setGraphic(imageRegion);
		}
		else
		{
			resource.setGraphic(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMargins(IInsets pMargins)
	{
		margins = pMargins;
		
		if (margins != null)
		{
			styleContainer.set("-fx-padding", margins.getTop() + "px " + margins.getRight() + "px " + margins.getBottom() + "px " + margins.getLeft() + "px");
		}
		else
		{
			styleContainer.clear("-fx-padding");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
		resource.setText(pText);
	}
	
	/**
	 * Not supported, does nothing.
	 * 
	 * @param pVerticalAlignment ignored.
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		// Not supported/possible.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Is invoked every time the menu items is clicked.
	 * 
	 * @param pActionEvent the event.
	 */
	private void onAction(ActionEvent pActionEvent)
	{
		if (eventAction != null)
		{
			// We need to extract the owner node, because if the menu item
			// is removed in the action, we would have no way to make the lock
			// go away.
			final Node ownerNode = resource.getParentPopup().getOwnerNode();
			
			JavaFXUtil.setGlobalCursor(this, Cursor.WAIT);
			FXSceneLocker.addHardLock(ownerNode);
			
			FXFrameWaitUtil.runLater(() ->
			{
				UIActionEvent actionEvent = new UIActionEvent(
						eventSource,
						UIActionEvent.ACTION_PERFORMED,
						System.currentTimeMillis(),
						0,
						actionCommand);
				
				try
				{
					eventAction.dispatchEvent(actionEvent);
				}
				catch (Exception e)
				{
					ExceptionHandler.show(e);
				}
				finally
				{
					FXSceneLocker.removeHardLock(ownerNode);
					JavaFXUtil.setGlobalCursor(this, null);
				}
			});
		}
	}
	
}	// JavaFXMenuItem

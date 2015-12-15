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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.rad.ui.IImage;
import javax.rad.ui.menu.ICheckBoxMenuItem;

/**
 * The {@link JavaFXCheckBoxMenuItem} is the JavaFX specific implementation of
 * {@link ICheckBoxMenuItem}.
 * 
 * @author Robert Zenz
 * @see ICheckBoxMenuItem
 * @see CheckMenuItem
 */
public class JavaFXCheckBoxMenuItem extends JavaFXMenuItem implements ICheckBoxMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The pressed image. */
	private IImage pressedImage;
	
	/** The (cached) pressed image view. */
	private ImageView pressedImageView;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXCheckBoxMenuItem}.
	 */
	public JavaFXCheckBoxMenuItem()
	{
		super(new CheckMenuItem());
		
		((CheckMenuItem) resource).selectedProperty().addListener(this::onSelectedChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelected()
	{
		return ((CheckMenuItem) resource).isSelected();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelected(boolean pPressed)
	{
		((CheckMenuItem) resource).setSelected(pPressed);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPressedImage(IImage pImage)
	{
		pressedImage = pImage;
		
		if (pressedImage != null)
		{
			pressedImageView = new ImageView(((Image) pressedImage.getResource()));
		}
		else
		{
			pressedImageView = null;
		}
		
		if (isSelected())
		{
			resource.setGraphic(pressedImageView);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getPressedImage()
	{
		return pressedImage;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Occurs when the selected property changes.
	 *
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedChanged(ObservableValue<? extends Boolean> pObservableValue, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			resource.setGraphic(pressedImageView);
		}
		else
		{
			resource.setGraphic(null);
		}
	}
	
}	// JavaFXCheckBoxMenuItem

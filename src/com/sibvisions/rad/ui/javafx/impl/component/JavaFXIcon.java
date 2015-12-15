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
package com.sibvisions.rad.ui.javafx.impl.component;

import javax.rad.ui.IImage;
import javax.rad.ui.component.IIcon;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link JavaFXIcon} is the JavaFX specific implementation if {@link IIcon}
 * .
 * <p>
 * It can display images in the given region and does normally not react to
 * input.
 * 
 * @author Robert Zenz
 * @see IIcon
 * @see FXImageRegion
 */
public class JavaFXIcon extends JavaFXComponent<FXImageRegion> implements IIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The value for the horizontal alignment.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int horizontalAlignment = ALIGN_DEFAULT;
	
	/**
	 * The image that is currently displayed.
	 */
	private IImage image;
	
	/**
	 * The value for the vertical alignment.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int verticalAlignment = ALIGN_DEFAULT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXIcon}.
	 */
	public JavaFXIcon()
	{
		super(new FXImageRegion());
		
		// We want it to *not* preserve the ratio by default, obviously.
		resource.setHorizontalStretched(false);
		resource.setPreserveRatio(false);
		resource.setVerticalStretched(false);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
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
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		if (pHorizontalAlignment == ALIGN_STRETCH)
		{
			resource.setHorizontalAlignment(HPos.CENTER);
			resource.setHorizontalStretched(true);
		}
		else
		{
			resource.setHorizontalAlignment(FXAlignmentUtil.alignmentToHPos(horizontalAlignment, HPos.CENTER));
			resource.setHorizontalStretched(false);
		}
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
			resource.setImage((Image)image.getResource());
		}
		else
		{
			resource.setImage(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		
		if (pVerticalAlignment == ALIGN_STRETCH)
		{
			resource.setVerticalAlignment(VPos.CENTER);
			resource.setVerticalStretched(true);
		}
		else
		{
			resource.setVerticalAlignment(FXAlignmentUtil.alignmentToVPos(verticalAlignment, VPos.CENTER));
			resource.setVerticalStretched(false);
		}
	}
	
}	// JavaFXIcon

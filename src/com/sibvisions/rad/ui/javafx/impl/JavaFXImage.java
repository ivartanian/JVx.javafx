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
package com.sibvisions.rad.ui.javafx.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.rad.ui.IImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import com.sibvisions.util.type.ImageUtil;
import com.sibvisions.util.type.ImageUtil.ImageFormat;

/**
 * The {@link JavaFXImage} is the JavaFX specific implementation of
 * {@link IImage}.
 * 
 * @author Robert Zenz
 * @see IImage
 * @see Image
 */
public class JavaFXImage extends JavaFXResource<Image> implements IImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The image name. */
	private String imageName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXImage}.
	 *
	 * @param pImageName the image name.
	 * @param pImage the image.
	 */
	public JavaFXImage(String pImageName, Image pImage)
	{
		super(pImage);
		
		imageName = pImageName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight()
	{
		return (int)resource.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getImageName()
	{
		return imageName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth()
	{
		return (int)resource.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveAs(OutputStream pOut, ImageType pType) throws IOException
	{
		BufferedImage image = new BufferedImage((int)resource.getWidth(), (int)resource.getHeight(), BufferedImage.TYPE_INT_ARGB);
		SwingFXUtils.fromFXImage(resource, image);
		
		ImageFormat format;
		
		if (pType == null)
		{
			format = ImageFormat.PNG;
		}
		else
		{
			switch (pType)
			{
				case JPG:
					format = ImageFormat.JPG;
					break;
					
				case GIF:
					format = ImageFormat.GIF;
					break;
					
				case BMP:
					format = ImageFormat.BMP;
					break;
					
				default:
					format = ImageFormat.PNG;
			}
		}
		
		ImageUtil.save(image, format, pOut);
	}
	
}	// JavaFXImage

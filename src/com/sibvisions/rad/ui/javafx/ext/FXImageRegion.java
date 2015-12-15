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
package com.sibvisions.rad.ui.javafx.ext;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * The {@link FXImageRegion} is an extended {@link Region} that can display an
 * image. Unlike the {@link ImageView}, the {@link FXImageRegion} can be resized
 * , honors the layout and the displayed {@link Image} will not overflow/leak
 * into other elements of the GUI.
 * <p>
 * Additionally it supports horizontal and vertical stretching, keeping or not
 * keeping the aspect ratio and also positioning the {@link Image} within
 * itself.
 * <p>
 * Usage is rather simple:
 * 
 * <pre>
 * {@code
 * yourContainer.getChildren().add(new ImageRegion(yourImage));
 * }
 * </pre>
 * 
 * @author Robert Zenz
 */
public class FXImageRegion extends Region
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property for the horizontal alignment. */
	private ObjectProperty<HPos> horizontalAlignment;
	
	/** The property for the horizontal stretch. */
	private BooleanProperty horizontalStretched;
	
	/**
	 * The {@link ImageView} that is internally used for displaying the image.
	 */
	private ImageView imageView;
	
	/** The property for the vertical alignment. */
	private ObjectProperty<VPos> verticalAlignment;
	
	/** The property for the vertical stretch. */
	private BooleanProperty verticalStretched;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXImageRegion}.
	 */
	public FXImageRegion()
	{
		super();
		
		setCache(true);
		
		imageView = new ImageView();
		imageView.setPreserveRatio(true);
		
		getChildren().add(imageView);
		
		imageView.imageProperty().addListener(pObservable -> requestLayout());
		
		imageView.preserveRatioProperty().addListener(pObservable -> requestLayout());
		
		horizontalAlignment = new SimpleObjectProperty<>(HPos.CENTER);
		horizontalAlignment.addListener(pObservable -> requestLayout());
		
		verticalAlignment = new SimpleObjectProperty<>(VPos.CENTER);
		verticalAlignment.addListener(pObservable -> requestLayout());
		
		horizontalStretched = new SimpleBooleanProperty(false);
		horizontalStretched.addListener(pObservable -> requestLayout());
		
		verticalStretched = new SimpleBooleanProperty(false);
		verticalStretched.addListener(pObservable -> requestLayout());
	}
	
	/**
	 * Creates a new instance of {@link FXImageRegion}.
	 * 
	 * @param pImage The {@link Image} to be displayed.
	 */
	public FXImageRegion(Image pImage)
	{
		this();
		
		imageView.setImage(pImage);
	}
	
	/**
	 * Creates a new instance of {@link FXImageRegion}.
	 * 
	 * @param pImageURL The URL of the {@link Image} to be displayed.
	 */
	public FXImageRegion(String pImageURL)
	{
		this();
		
		imageView.setImage(new Image(pImageURL));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double width)
	{
		if (imageView.getImage() != null)
		{
			Insets padding = getPadding();
			if (padding == null)
			{
				padding = Insets.EMPTY;
			}
			
			return imageView.getImage().getHeight() + padding.getTop() + padding.getBottom();
		}
		
		return super.computePrefHeight(width);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double height)
	{
		if (imageView.getImage() != null)
		{
			Insets padding = getPadding();
			if (padding == null)
			{
				padding = Insets.EMPTY;
			}
			
			return imageView.getImage().getWidth() + padding.getLeft() + padding.getRight();
		}
		
		return super.computePrefWidth(height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		// Reset some values
		imageView.setFitWidth(-1);
		imageView.setFitHeight(-1);
		
		Insets padding = getPadding();
		if (padding == null)
		{
			padding = Insets.EMPTY;
		}
		
		double width = getWidth() - padding.getLeft() - padding.getRight();
		double height = getHeight() - padding.getTop() - padding.getBottom();
		
		Image image = imageView.getImage();
		
		if (image != null)
		{
			// The width of the currently displayed image.
			// Because of stretching the displayed image might have a different
			// size than the original one. 
			double displayImageWidth = image.getWidth();
			double displayImageHeight = image.getHeight();
			
			// Variables needed for the viewport, as the Rectangle2D is immutable.
			double viewportX = 0;
			double viewportY = 0;
			double viewportWidth = image.getWidth();
			double viewportHeight = image.getHeight();
			
			if (preserveRatioProperty().get())
			{
				// If we should preserve the ratio, we need to check if the image
				// is either stretched horizontally or vertically. If it is not
				// stretched we don't need to do anything, but if it is stretched
				// into one direction and not the other, we need to changed the
				// displayedImage* to make sure that the following checks use
				// the correct size to clip the image.
				
				if (horizontalStretched.get() && !verticalStretched.get())
				{
					displayImageHeight = displayImageHeight * (width / image.getWidth());
				}
				
				if (verticalStretched.get() && !horizontalStretched.get())
				{
					displayImageWidth = displayImageWidth * (height / image.getHeight());
				}
			}
			
			if (horizontalStretched.get())
			{
				imageView.setFitWidth(width);
			}
			else if (displayImageWidth > width)
			{
				// If the image is stretched, this makes sure that it is clipped
				// to the correct size.
				viewportWidth = (width * image.getWidth() / displayImageWidth);
				
				switch (horizontalAlignment.get())
				{
					case CENTER:
						viewportX = Math.round((image.getWidth() - viewportWidth) / 2);
						break;
						
					case RIGHT:
						viewportX = image.getWidth() - viewportWidth;
						break;
						
					case LEFT:
					default:
						viewportX = 0;
						break;
						
				}
			}
			
			if (verticalStretched.get())
			{
				imageView.setFitHeight(height);
			}
			else if (displayImageHeight > height)
			{
				// If the image is stretched, this makes sure that it is clipped
				// to the correct size.
				viewportHeight = (height * image.getHeight() / displayImageHeight);
				
				switch (verticalAlignment.get())
				{
					case CENTER:
						viewportY = Math.round((image.getHeight() - viewportHeight) / 2);
						break;
						
					case BOTTOM:
						viewportY = image.getHeight() - viewportHeight;
						break;
						
					case TOP:
					default:
						viewportY = 0;
						break;
						
				}
			}
			
			// A viewport with a width of 0 or less is not allowed.
			viewportWidth = Math.max(viewportWidth, 1);
			viewportHeight = Math.max(viewportHeight, 1);
			
			// Set the viewport so that only those parts of the image are visible
			// which should be visible according to the layout.
			Rectangle2D viewport = new Rectangle2D(viewportX, viewportY, viewportWidth, viewportHeight);
			imageView.setViewport(viewport);
		}
		
		// Perform the layout for the image view.
		layoutInArea(imageView, padding.getLeft(), padding.getTop(), width, height, 0, horizontalAlignment.get(), verticalAlignment.get());
		
		super.layoutChildren();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the horizontal alignment.
	 *
	 * @return the horizontal alignment.
	 * 		
	 * @see FXImageRegion#horizontalAlignmentProperty()
	 * @see FXImageRegion#setHorizontalAlignment(HPos)
	 */
	public HPos getHorizontalAlignment()
	{
		return horizontalAlignment.get();
	}
	
	/**
	 * Gets the image.
	 *
	 * @return the image.
	 * 		
	 * @see #setImage(Image)
	 */
	public Image getImage()
	{
		return imageView.getImage();
	}
	
	/**
	 * Gets the vertical alignment.
	 *
	 * @return the vertical alignment.
	 * 		
	 * @see #verticalAlignmentProperty()
	 * @see #setVerticalAlignment(VPos)
	 */
	public VPos getVerticalAlignment()
	{
		return verticalAlignment.get();
	}
	
	/**
	 * Gets the horizontal alignment property which defines the horizontal
	 * position of the image within this {@link FXImageRegion} if the image is
	 * (currently) smaller than this.
	 *
	 * @return the object property for the horizontal alignment.
	 * 		
	 * @see #getHorizontalAlignment()
	 * @see #setHorizontalAlignment(HPos)
	 */
	public ObjectProperty<HPos> horizontalAlignmentProperty()
	{
		return horizontalAlignment;
	}
	
	/**
	 * Gets the horizontal stretch property which defines if the image will
	 * always resize itself to fit into this.
	 *
	 * @return the boolean property for the horizontal stretch.
	 * 		
	 * @see #getHorizontalAlignment()
	 * @see #setHorizontalStretched(boolean)
	 */
	public BooleanProperty horizontalStretchedProperty()
	{
		return horizontalStretched;
	}
	
	/**
	 * Gets the image property.
	 *
	 * @return the object property for the image.
	 * 		
	 * @see #setImage(Image)
	 * @see #getImage()
	 */
	public ObjectProperty<Image> imageProperty()
	{
		return imageView.imageProperty();
	}
	
	/**
	 * Gets if the image should be horizontally stretched.
	 *
	 * @return {@code true}, if the image should be horizontally stretched.
	 * 		
	 * @see #horizontalStretchedProperty()
	 * @see #setHorizontalStretched(boolean)
	 */
	public boolean isHorizontalStretch()
	{
		return horizontalStretched.get();
	}
	
	/**
	 * Gets if the ratio of the image should be preserved. Only applies if
	 * either {@link #horizontalStretched} or {@link #verticalStretched} are
	 * {@code true}.
	 * 
	 * @return {@code true} if the ratio of the image should be preserved.
	 * 		
	 * @see #preserveRatioProperty()
	 * @see #setPreserveRatio(boolean)
	 */
	public boolean isPreserveRatio()
	{
		return imageView.isPreserveRatio();
	}
	
	/**
	 * Gets if the image should be vertically stretched.
	 *
	 * @return {@code true}, if the image should be vertically stretched.
	 * 		
	 * @see #verticalStretchedProperty()
	 * @see #setVerticalStretched(boolean)
	 */
	public boolean isVerticalStretched()
	{
		return verticalStretched.get();
	}
	
	/**
	 * Gets the preserve ratio property. This property only applies if either
	 * {@link #horizontalStretchedProperty()} or
	 * {@link #verticalStretchedProperty()} are {@code true}.
	 *
	 * @return the boolean property for if the ratio image should be preserved.
	 * 		
	 * @see #isPreserveRatio()
	 * @see #setPreserveRatio(boolean)
	 */
	public BooleanProperty preserveRatioProperty()
	{
		return imageView.preserveRatioProperty();
	}
	
	/**
	 * Sets the horizontal alignment.
	 *
	 * @param pHPos the new horizontal alignment.
	 * 			
	 * @see #getHorizontalAlignment()
	 * @see #horizontalAlignmentProperty()
	 */
	public void setHorizontalAlignment(HPos pHPos)
	{
		horizontalAlignment.set(pHPos);
	}
	
	/**
	 * Gets if the image should be horizontally stretched.
	 *
	 * @param pStretched {@code true}, if the image should be horizontally
	 *            stretched.
	 * 
	 * @see #horizontalStretchedProperty()
	 * @see #isHorizontalStretch()
	 */
	public void setHorizontalStretched(boolean pStretched)
	{
		horizontalStretched.set(pStretched);
	}
	
	/**
	 * Sets the image.
	 *
	 * @param pImage the new image.
	 * 			
	 * @see #getImage()
	 * @see #imageProperty()
	 */
	public void setImage(Image pImage)
	{
		imageView.setImage(pImage);
	}
	
	/**
	 * Sets if the ratio of the image should be preserved. Only applies if
	 * either {@link #horizontalStretched} or {@link #verticalStretched} are
	 * {@code true}.
	 *
	 * @param pPreserveRatio {@code true} if the ratio of the image should be
	 *            preserved.
	 * 
	 * @see #isPreserveRatio()
	 * @see #preserveRatioProperty()
	 */
	public void setPreserveRatio(boolean pPreserveRatio)
	{
		imageView.setPreserveRatio(pPreserveRatio);
	}
	
	/**
	 * Sets the vertical alignment.
	 *
	 * @param pVPos the new vertical alignment.
	 * 			
	 * @see #getVerticalAlignment()
	 * @see #verticalAlignmentProperty()
	 */
	public void setVerticalAlignment(VPos pVPos)
	{
		verticalAlignment.set(pVPos);
	}
	
	/**
	 * Gets if the image should be vertically stretched.
	 *
	 * @param pStretched {@code true}, if the image should be vertically
	 *            stretched.
	 * 
	 * @see #isVerticalStretched()
	 */
	public void setVerticalStretched(boolean pStretched)
	{
		verticalStretched.set(pStretched);
	}
	
	/**
	 * Gets the vertical alignment property which defines the vertical position
	 * of the image within this {@link FXImageRegion} if the image is
	 * (currently) smaller than this.
	 *
	 * @return the object property for the vertical alignment.
	 * 		
	 * @see #getVerticalAlignment()
	 * @see #setVerticalAlignment(VPos)
	 */
	public ObjectProperty<VPos> verticalAlignmentProperty()
	{
		return verticalAlignment;
	}
	
	/**
	 * Gets the vertical stretch property which defines if the image will always
	 * resize itself to fit into this.
	 *
	 * @return the boolean property for the vertical stretch.
	 * 		
	 * @see #getVerticalAlignment()
	 * @see #setVerticalStretched(boolean)
	 */
	public BooleanProperty verticalStretchedProperty()
	{
		return verticalStretched;
	}
	
}	// FXImageRegion

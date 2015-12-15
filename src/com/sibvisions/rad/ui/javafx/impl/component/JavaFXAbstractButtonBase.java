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
import javax.rad.ui.IInsets;
import javax.rad.ui.component.IButton;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.util.ExceptionHandler;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import com.sibvisions.rad.ui.javafx.ext.IFXContentAlignable;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;
import com.sibvisions.rad.ui.javafx.ext.util.FXFrameWaitUtil;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;
import com.sibvisions.rad.ui.javafx.impl.JavaFXFactory;
import com.sibvisions.rad.ui.javafx.impl.JavaFXInsets;
import com.sibvisions.rad.ui.javafx.impl.JavaFXUtil;

/**
 * {@link JavaFXAbstractButtonBase} is the JavaFX specific abstract base
 * implementation for {@link javax.rad.ui.component.IActionComponent} for all
 * components derived from {@link ButtonBase}.
 * 
 * @author Robert Zenz
 * @param <C> the type of the component.
 */
public abstract class JavaFXAbstractButtonBase<C extends ButtonBase> extends JavaFXLabeled<C> implements IButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The accelerator {@link Key} which triggers the action event of this
	 * component.
	 */
	protected Key accelerator;
	
	/** The action command. */
	protected String actionCommand;
	
	/** The {@link ActionHandler}. */
	protected ActionHandler eventAction;
	
	/** The {@link IImage} that this component might display. */
	protected IImage image;
	
	/** The cached {@link ImageView} that is cached for the image. */
	protected ImageView imageView;
	
	/** If the border is visible. */
	private boolean border = true;
	
	/**
	 * If the border should only be visible if the mouse hovers of the button.
	 */
	private boolean borderOnMouseEntered = false;
	
	/**
	 * The value for the horizontal alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int horizontalTextAlignment = ALIGN_DEFAULT;
	
	/**
	 * The {@link IImage} that is displayed if the mouse hovers over this
	 * button.
	 */
	private IImage mouseOverImage;
	
	/** The {@link ImageView} that contains the hover {@link IImage}, if any. */
	private ImageView mouseOverImageView;
	
	/**
	 * The {@link IImage} that is displayed if the mouse is pressed over this
	 * button.
	 */
	private IImage pressedImage;
	
	/**
	 * The {@link ImageView} that contains the pressed {@link IImage}, if any.
	 */
	private ImageView pressedImageView;
	
	/**
	 * The {@link EventHandler} function that is called if a key is pressed in
	 * the scene.
	 * <p>
	 * Needs to be saved in a variable so that it can also be removed again.
	 */
	private EventHandler<? super KeyEvent> sceneKeyPressedHandler;
	
	/**
	 * The value for the vertical alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int verticalTextAlignment = ALIGN_DEFAULT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractButtonBase}.
	 *
	 * @param resource the resource.
	 */
	protected JavaFXAbstractButtonBase(C resource)
	{
		super(resource);
		
		// Variable needed so that it can also be removed again.
		sceneKeyPressedHandler = this::onSceneKeyPressed;
		
		resource.sceneProperty().addListener(this::updateSceneKeyHandler);
		
		resource.setOnMousePressed((pMouseEvent) ->
		{
			if (pressedImage != null)
			{
				resource.setGraphic(pressedImageView);
			}
			
			// If the button receives focus during the click event,
			// the border will vanish again. This is the workaround for that.
			if (borderOnMouseEntered)
			{
				setBorderVisible(true);
			}
		});
		
		resource.setOnTouchReleased((pMouseEvent) ->
		{
			//because no exited event will follow
			if (borderOnMouseEntered)
			{
				setBorderVisible(false);
			}
		});
		
		resource.setOnMouseReleased((pMouseEvent) ->
		{
			if (pressedImage != null)
			{
				if (mouseOverImage != null && NodeUtil.isSameOrChild(resource, pMouseEvent.getPickResult().getIntersectedNode()))
				{
					resource.setGraphic(mouseOverImageView);
				}
				else
				{
					resource.setGraphic(imageView);
				}
			}
		});
		
		resource.setOnMouseEntered((pMouseEvent) ->
		{
			if (mouseOverImage != null)
			{
				if (pMouseEvent.isPrimaryButtonDown() && pressedImage != null)
				{
					resource.setGraphic(pressedImageView);
				}
				else
				{
					resource.setGraphic(mouseOverImageView);
				}
			}
			
			if (borderOnMouseEntered)
			{
				setBorderVisible(true);
			}
		});
		
		resource.setOnMouseExited((pMouseEvent) ->
		{
			if (mouseOverImage != null)
			{
				resource.setGraphic(imageView);
			}
			
			if (borderOnMouseEntered)
			{
				setBorderVisible(false);
			}
		});
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
			
			resource.setOnAction(this::fireAction);
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
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalTextPosition()
	{
		return horizontalTextAlignment;
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
	public int getImageTextGap()
	{
		return (int)resource.getGraphicTextGap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInsets getMargins()
	{
		return new JavaFXInsets(resource.getPadding());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getMouseOverImage()
	{
		return mouseOverImage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getPressedImage()
	{
		return pressedImage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalTextPosition()
	{
		return verticalTextAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBorderOnMouseEntered()
	{
		return borderOnMouseEntered;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBorderPainted()
	{
		return border;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccelerator(Key pKey)
	{
		if (pKey != null && accelerator == null)
		{
			// Newly registered key, install the handler
			updateSceneKeyHandler(null, null, resource.getScene());
		}
		else if (pKey == null && accelerator != null)
		{
			// Remove the handler
			updateSceneKeyHandler(null, resource.getScene(), null);
		}
		
		accelerator = pKey;
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
	 * {@inheritDoc}
	 */
	@Override
	public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
	{
		borderOnMouseEntered = pBorderOnMouseEntered;
		
		setBorderVisible(border && !borderOnMouseEntered);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBorderPainted(boolean pBorderPainted)
	{
		border = pBorderPainted;
		
		setBorderVisible(border && !borderOnMouseEntered);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalTextPosition(int pHorizontalPosition)
	{
		horizontalTextAlignment = pHorizontalPosition;
		
		setTextPositionAndAlignment();
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
			imageView = new ImageView((Image)image.getResource());
			resource.setGraphic(imageView);
		}
		else
		{
			imageView = null;
			resource.setGraphic(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImageTextGap(int pImageTextGap)
	{
		resource.setGraphicTextGap(pImageTextGap);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMargins(IInsets pMargins)
	{
		resource.setPadding((Insets)pMargins.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMouseOverImage(IImage pImage)
	{
		mouseOverImage = pImage;
		
		if (mouseOverImage == null)
		{
			mouseOverImageView = null;
		}
		else
		{
			mouseOverImageView = new ImageView((Image)mouseOverImage.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPressedImage(IImage pImage)
	{
		pressedImage = pImage;
		
		if (pressedImage == null)
		{
			pressedImageView = null;
		}
		else
		{
			pressedImageView = new ImageView((Image)pressedImage.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalTextPosition(int pVerticalPosition)
	{
		verticalTextAlignment = pVerticalPosition;
		
		setTextPositionAndAlignment();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Fires the action event for this component.
	 * 
	 * @param pActionEvent the {@link ActionEvent} that is used as source. Can
	 *            be {@code null}.
	 */
	protected void fireAction(ActionEvent pActionEvent)
	{
		if (eventAction != null)
		{
			JavaFXUtil.setGlobalCursor(this, Cursor.WAIT);
			FXSceneLocker.addHardLock(resource);
			
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
					FXSceneLocker.removeHardLock(resource);
					JavaFXUtil.setGlobalCursor(this, null);
				}
			});
		}
	}
	
	/**
	 * Sets whether the border is visible or not.
	 * 
	 * @param pBorderVisible {@code true} if the border should be visible.
	 */
	protected void setBorderVisible(boolean pBorderVisible)
	{
		if (pBorderVisible)
		{
			if (!isBackgroundSet())
			{
				styleContainer.clear("-fx-background-color");
			}
			
			styleContainer.clear("-fx-focus-color");
		}
		else
		{
			if (!isBackgroundSet())
			{
				styleContainer.set("-fx-background-color", "transparent");
			}
			
			styleContainer.set("-fx-focus-color", "transparent");
		}
	}
	
	/**
	 * Sets the position of the text.
	 */
	protected void setTextPositionAndAlignment()
	{
		if (resource instanceof IFXContentAlignable)
		{
			IFXContentAlignable contentAlignable = (IFXContentAlignable)resource;
			
			contentAlignable.setRelativeHorizontalTextAlignment(HPos.CENTER);
			
			if (getHorizontalTextPosition() == ALIGN_CENTER)
			{
				// switch is needed as the values are mirrored.
				switch (getVerticalTextPosition())
				{
					case ALIGN_BOTTOM:
						resource.setContentDisplay(ContentDisplay.TOP);
						break;
						
					case ALIGN_CENTER:
						resource.setContentDisplay(ContentDisplay.CENTER);
						break;
						
					case ALIGN_TOP:
						resource.setContentDisplay(ContentDisplay.BOTTOM);
						break;
						
					default:
						resource.setContentDisplay(ContentDisplay.CENTER);
						break;
						
				}
				
				contentAlignable.setRelativeVerticalTextAlignment(VPos.CENTER);
			}
			else
			{
				if (getHorizontalTextPosition() == ALIGN_LEFT)
				{
					resource.setContentDisplay(ContentDisplay.RIGHT);
				}
				else if (getHorizontalTextPosition() == ALIGN_RIGHT)
				{
					resource.setContentDisplay(ContentDisplay.LEFT);
				}
				
				contentAlignable.setRelativeVerticalTextAlignment(FXAlignmentUtil.alignmentToVPos(getVerticalTextPosition(), VPos.CENTER));
			}
		}
	}
	
	/**
	 * Occurs if there's a key pressed on the scene. Used for firing the action
	 * event if an accelerator key is installed in this component.
	 * 
	 * @param pKeyEvent the key event that occurred.
	 */
	private void onSceneKeyPressed(KeyEvent pKeyEvent)
	{
		if (accelerator != null && !pKeyEvent.isConsumed())
		{
			if (JavaFXFactory.compare(pKeyEvent, accelerator))
			{
				pKeyEvent.consume();
				resource.fire();
			}
		}
	}
	
	/**
	 * Updates the key handler/listener that is installed in the scene.
	 * 
	 * @param pObservable not used.
	 * @param pOldScene if not {@code null}, the listener is removed from this
	 *            scene.
	 * @param pNewScene if not {@code null}, the listener is removed form this
	 *            scene and if there is an accelerator set, added to it again.
	 */
	private void updateSceneKeyHandler(ObservableValue<? extends Scene> pObservable, Scene pOldScene, Scene pNewScene)
	{
		if (pOldScene != null)
		{
			pOldScene.removeEventHandler(KeyEvent.KEY_PRESSED, sceneKeyPressedHandler);
		}
		
		if (pNewScene != null)
		{
			pNewScene.removeEventHandler(KeyEvent.KEY_PRESSED, sceneKeyPressedHandler);
			
			if (accelerator != null)
			{
				pNewScene.addEventHandler(KeyEvent.KEY_PRESSED, sceneKeyPressedHandler);
			}
		}
	}
	
}	// JavaFXAbstractButtonBase

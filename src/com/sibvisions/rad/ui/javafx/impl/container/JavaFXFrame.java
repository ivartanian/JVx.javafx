/*
 * Copyright 2014 SIB Visions GmbH
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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.ICursor;
import javax.rad.ui.IImage;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;
import javax.rad.util.TranslationMap;

import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.sibvisions.rad.ui.javafx.ext.FXOverlayRegion;
import com.sibvisions.rad.ui.javafx.ext.FXZoomableHelper;
import com.sibvisions.rad.ui.javafx.ext.panes.FXPositioningPane;
import com.sibvisions.rad.ui.javafx.ext.scene.StyledScene;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;
import com.sibvisions.rad.ui.javafx.ext.util.ScenicViewLoader;
import com.sibvisions.rad.ui.javafx.impl.JavaFXPoint;
import com.sibvisions.rad.ui.javafx.impl.JavaFXRectangle;
import com.sibvisions.rad.ui.javafx.impl.JavaFXUtil;
import com.sibvisions.rad.ui.javafx.impl.focus.JavaFXFocusTraversalAlgorithm;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;

/**
 * The {@link JavaFXFrame} is the JavaFX specific implementation of
 * {@link javax.rad.ui.container.IFrame}.
 * 
 * @author Robert Zenz
 * @see javax.rad.ui.container.IFrame
 * @see Stage
 */
public class JavaFXFrame extends JavaFXAbstractFrameBase<Region>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The scene height property. */
	public static final String SYSPROP_SCENE_HEIGHT = "scene.height";
	
	/** The scene styled property. */
	public static final String SYSPROP_SCENE_STYLED = "scene.styled";
	
	/** The scene width property. */
	public static final String SYSPROP_SCENE_WIDTH = "scene.width";
	
	/** The stage hidden property. */
	public static final String SYSPROP_STAGE_HIDDEN = "stage.hidden";
	
	/** The name of the system property to disable or enable zooming. */
	public static final String SYSPROP_ZOOM = "zoom";
	
	/** The icon. */
	private IImage icon;
	
	/** The {@link Scene}. */
	private Scene scene;
	
	/** The {@link Stage}. */
	private Stage stage;
	
	/** The custom height. */
	private double customHeight;
	
	/** The custom width. */
	private double customWidth;
	
	/** The root node. */
	private FXOverlayRegion root;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXFrame}.
	 */
	public JavaFXFrame()
	{
		this(new Stage());
	}
	
	/**
	 * Creates a new instance of {@link JavaFXFrame}.
	 *
	 * @param pStage the stage.
	 */
	@SuppressWarnings("deprecation")
	protected JavaFXFrame(Stage pStage)
	{
		super(null);
		
		stage = pStage;
		stage.hide();
		setResource((Region)toolBarPanel.getResource());
		
		// Add a style class to the new resource, so that the background
		// (and other stuff) can be set on it.
		resource.getStyleClass().add("root-node");
		
		FXPositioningPane overlayPane = new FXPositioningPane();
		overlayPane.setAutoSizeOnlyOnPositioning(true);
		
		root = new FXOverlayRegion((Pane)resource, overlayPane);
		
		FXSceneLocker.makeLockRoot(root);
		
		customWidth = getDouble(SYSPROP_SCENE_WIDTH);
		customHeight = getDouble(SYSPROP_SCENE_HEIGHT);
		
		FXZoomableHelper.setDefaultZoomEnabled(System.getProperty(SYSPROP_ZOOM) == null || Boolean.getBoolean(SYSPROP_ZOOM));
		
		boolean isStageHidden = System.getProperty(SYSPROP_STAGE_HIDDEN) != null && Boolean.getBoolean(SYSPROP_STAGE_HIDDEN);
		boolean isSceneStyled = System.getProperty(SYSPROP_SCENE_STYLED) == null || Boolean.getBoolean(SYSPROP_SCENE_STYLED);
		
		if (!isStageHidden && isSceneStyled)
		{
			scene = new StyledScene(pStage, root, customWidth, customHeight);
		}
		else
		{
			scene = new Scene(root, customWidth, customHeight);
			pStage.setScene(scene);
			
			if (isStageHidden)
			{
				stage.initStyle(StageStyle.TRANSPARENT);
				scene.setFill(Color.TRANSPARENT);
			}
		}
		
		scene.setCamera(new PerspectiveCamera());
		scene.getStylesheets().add("/com/sibvisions/rad/ui/javafx/impl/css/default.css");
		
		stage.setOnCloseRequest(this::onStageClose);
		
		// TODO HACK JavaFX doesn't have an official API for setting a focus policy. RT-19379/RT-21209/RT-25538
		resource.setImpl_traversalEngine(new ParentTraversalEngine(resource, new JavaFXFocusTraversalAlgorithm()));
		
		ScenicViewLoader.attach(scene, new KeyCodeCombination(
				KeyCode.S,			// S
				ModifierValue.DOWN,	// Shift
				ModifierValue.DOWN,	// Control
				ModifierValue.UP,	// Alt
				ModifierValue.UP,	// Meta
				ModifierValue.UP	// Shortcut
		));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void centerRelativeTo(IComponent pComponent)
	{
		stage.centerOnScreen();
		
		if (pComponent != null)
		{
			IPoint componentLocation = pComponent.getLocation();
			stage.setX(stage.getX() + componentLocation.getX());
			stage.setY(stage.getY() + componentLocation.getY());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		fireWindowClosing();
		
		stage.hide();
		
		fireWindowClosed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRectangle getBounds()
	{
		return new JavaFXRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getIconImage()
	{
		return icon;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getLocation()
	{
		return new JavaFXPoint(stage.getX(), stage.getY());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getResource()
	{
		return stage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getState()
	{
		if (stage.isIconified())
		{
			return ICONIFIED;
		}
		else if (stage.isMaximized())
		{
			return MAXIMIZED_BOTH;
		}
		
		return NORMAL;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle()
	{
		return stage.getTitle();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		if (scene instanceof StyledScene)
		{
			return ((StyledScene)scene).translationProperty().get();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive()
	{
		return stage.isFocused();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDisposed()
	{
		return stage.isShowing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResizable()
	{
		return stage.isResizable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pack()
	{
		resource.autosize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBounds(IRectangle pBounds)
	{
		stage.setX(pBounds.getX());
		stage.setY(pBounds.getY());
		stage.setWidth(pBounds.getWidth());
		stage.setHeight(pBounds.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconImage(IImage pIconImage)
	{
		icon = pIconImage;
		
		stage.getIcons().clear();
		
		if (icon != null)
		{
			stage.getIcons().add((Image)pIconImage.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(IPoint pLocation)
	{
		stage.setX(pLocation.getX());
		stage.setY(pLocation.getY());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResizable(boolean pResizable)
	{
		stage.setResizable(pResizable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(int pState)
	{
		switch (pState)
		{
			case ICONIFIED:
				stage.setIconified(true);
				break;
				
			case MAXIMIZED_BOTH:
			case MAXIMIZED_HORIZ:
			case MAXIMIZED_VERT:
				stage.setMaximized(true);
				break;
				
			case NORMAL:
			default:
				stage.setMaximized(false);
				stage.setIconified(false);
				
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String pTitle)
	{
		stage.setTitle(pTitle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		if (scene instanceof StyledScene)
		{
			((StyledScene)scene).translationProperty().set(pTranslation);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		if (pVisible)
		{
			stage.show();
		}
		else
		{
			stage.hide();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toBack()
	{
		stage.toBack();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toFront()
	{
		stage.toFront();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pLayoutContainerHybrid)
	{
		if (toolBarPanel != null)
		{
			toolBarPanel.setLayoutInternal(pLayoutContainerHybrid);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCursor(ICursor pCursor)
	{
		if (pCursor == null)
		{
			JavaFXUtil.setGlobalCursor(this, null);
		}
		else
		{
			JavaFXUtil.setGlobalCursor(this, (Cursor)pCursor.getResource());
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the the double value of a given system property.
	 * 
	 * @param pSystemProperty the name of the system property.
	 * @return the value or {@code -1} if either the system property was not set
	 *         or {@code null} the value was not a double.
	 */
	protected static double getDouble(String pSystemProperty)
	{
		try
		{
			return Double.parseDouble(System.getProperty(pSystemProperty));
		}
		catch (Exception e)
		{
			// Use default value
		}
		
		return -1;
	}
	
	/**
	 * Gets the scene.
	 *
	 * @return the scene.
	 */
	public Scene getScene()
	{
		return scene;
	}
	
	/**
	 * Gets the stage.
	 *
	 * @return the stage.
	 */
	public Stage getStage()
	{
		return stage;
	}
	
	/**
	 * If this {@link JavaFXFrame} does have a custom size set.
	 * 
	 * @return {@code true} if a custom size is set.
	 */
	protected boolean hasCustomSize()
	{
		return customWidth >= 0 || customHeight >= 0;
	}
	
	/**
	 * Invoked when the {@link Stage} is closing, and it will call
	 * {@link #dispose()}.
	 * 
	 * @param pWindowEvent the event.
	 */
	private void onStageClose(WindowEvent pWindowEvent)
	{
		dispose();
	}
	
}	// JavaFXFrame

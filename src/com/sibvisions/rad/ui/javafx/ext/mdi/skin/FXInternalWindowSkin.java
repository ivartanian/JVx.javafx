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
package com.sibvisions.rad.ui.javafx.ext.mdi.skin;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowStateChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.mdi.behavior.FXInternalWindowBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * The default skin for the {@link FXInternalWindow}.
 * <p>
 * This skin provides a basic window like appearance.
 * 
 * @author Robert Zenz
 */
public class FXInternalWindowSkin extends BehaviorSkinBase<FXInternalWindow, FXInternalWindowBehavior>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The autosize {@link MenuItem}. */
	private MenuItem autoSizeMenuItem;
	
	/** The {@link Button} for closing the {@link FXInternalWindow}. */
	private HoverRemovableButton closeButton;
	
	/** The close {@link MenuItem}. */
	private MenuItem closeMenuItem;
	
	/** The {@link HBox} used for hosting the control buttons (close etc.). */
	private HBox controls;
	
	/** The {@link FXImageRegion} used for the icon. */
	private FXImageRegion icon;
	
	/** The {@link Button} for maximizing the {@link FXInternalWindow}. */
	private Button maximizeButton;
	
	/** The maximize {@link MenuItem}. */
	private MenuItem maximizeMenuItem;
	
	/** The window menu. */
	private ContextMenu menu;
	
	/** The separator for the context menu. */
	private SeparatorMenuItem menuSeparator;
	
	/** The {@link Button} for minimizing the {@link FXInternalWindow}. */
	private Button minimizeButton;
	
	/** The minimize {@link MenuItem}. */
	private MenuItem minimizeMenuItem;
	
	/** The {@link BorderPane} used as title bar. */
	private BorderPane titleBar;
	
	/** The {@link Label} used for the title. */
	private Label titleLabel;
	
	/** The {@link Button} used for zooming in. */
	private Button zoomInButton;
	
	/** The {@link Button} used for zooming out. */
	private Button zoomOutButton;
	
	/** The {@link Button} used for reseting the zoom. */
	private Button zoomResetButton;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXInternalWindowSkin}.
	 *
	 * @param pInternalWindow the internal window.
	 */
	public FXInternalWindowSkin(FXInternalWindow pInternalWindow)
	{
		this(pInternalWindow, new FXInternalWindowBehavior(pInternalWindow));
		
		getBehavior().init(titleBar, menu);
	}
	
	/**
	 * Creates a new instance of {@link FXInternalWindowSkin}.
	 *
	 * @param pInternalWindow the internal window.
	 * @param pBehavior the behavior.
	 */
	public FXInternalWindowSkin(FXInternalWindow pInternalWindow, FXInternalWindowBehavior pBehavior)
	{
		super(pInternalWindow, pBehavior);
		
		FXInternalWindow skinnable = getSkinnable();
		
		titleBar = new BorderPane();
		titleBar.getStyleClass().setAll("titlebar");
		titleBar.setId("internalwindow_titlebar");
		getChildren().add(titleBar);
		
		icon = new FXImageRegion();
		icon.getStyleClass().add("titlebar-icon");
		icon.setId("internalwindow_titlebar_icon");
		icon.setHorizontalStretched(true);
		icon.setOnMouseClicked(this::onIconMouseClicked);
		icon.setPreserveRatio(true);
		icon.setVerticalStretched(true);
		icon.setImage(pInternalWindow.getIcon());
		icon.imageProperty().bind(pInternalWindow.iconProperty());
		titleBar.setLeft(icon);
		BorderPane.setAlignment(icon, Pos.CENTER);
		
		titleLabel = new Label(skinnable.getTitle());
		titleLabel.getStyleClass().add("titlebar-name");
		titleLabel.setId("internalwindow_titlebar_name");
		titleLabel.setAlignment(Pos.CENTER);
		titleLabel.textProperty().bind(skinnable.titleProperty());
		titleBar.setCenter(titleLabel);
		BorderPane.setAlignment(titleLabel, Pos.CENTER);
		
		controls = new HBox();
		controls.getStyleClass().add("titlebar-controls");
		controls.setId("internalwindow_titlebar_controls");
		controls.setAlignment(Pos.CENTER);
		titleBar.setRight(controls);
		BorderPane.setAlignment(controls, Pos.CENTER);
		
		closeButton = new HoverRemovableButton();
		closeButton.getStyleClass().addAll("titlebar-control", "titlebar-control-close");
		closeButton.setId("internalwindow_titlebar_control_close");
		closeButton.setFocusTraversable(false);
		closeButton.setOnAction(this::onCloseButtonAction);
		
		maximizeButton = new Button();
		maximizeButton.getStyleClass().addAll("titlebar-control", "titlebar-control-maximize");
		maximizeButton.setId("internalwindow_titlebar_control_maximize");
		maximizeButton.setFocusTraversable(false);
		maximizeButton.setOnAction(this::onMaximizeButtonAction);
		
		minimizeButton = new Button();
		minimizeButton.getStyleClass().addAll("titlebar-control", "titlebar-control-minimize");
		minimizeButton.setId("internalwindow_titlebar_control_minimize");
		minimizeButton.setFocusTraversable(false);
		minimizeButton.setOnAction(this::onMinimizeButtonAction);
		
		zoomInButton = new Button();
		zoomInButton.getStyleClass().addAll("titlebar-control", "titlebar-control-zoom-in");
		zoomInButton.setId("internalwindow_titlebar_control_zoom_in");
		zoomInButton.setFocusTraversable(false);
		zoomInButton.setOnAction((pActionEvent) -> getSkinnable().zoomIn());
		zoomInButton.disableProperty().bind(getSkinnable().zoomInPossibleProperty().not());
		zoomInButton.managedProperty().bind(getSkinnable().zoomEnabledProperty());
		zoomInButton.visibleProperty().bind(getSkinnable().zoomEnabledProperty());
		
		zoomOutButton = new Button();
		zoomOutButton.getStyleClass().addAll("titlebar-control", "titlebar-control-zoom-out");
		zoomOutButton.setId("internalwindow_titlebar_control_zoom_out");
		zoomOutButton.setFocusTraversable(false);
		zoomOutButton.setOnAction((pActionEvent) -> getSkinnable().zoomOut());
		zoomOutButton.disableProperty().bind(getSkinnable().zoomOutPossibleProperty().not());
		zoomOutButton.managedProperty().bind(getSkinnable().zoomEnabledProperty());
		zoomOutButton.visibleProperty().bind(getSkinnable().zoomEnabledProperty());
		
		zoomResetButton = new Button();
		zoomResetButton.getStyleClass().addAll("titlebar-control", "titlebar-control-zoom-reset");
		zoomResetButton.setId("internalwindow_titlebar_control_zoom_reset");
		zoomResetButton.setFocusTraversable(false);
		zoomResetButton.setOnAction((pActionEvent) -> getSkinnable().zoomReset());
		zoomResetButton.disableProperty().bind(getSkinnable().zoomResetPossibleProperty().not());
		zoomResetButton.managedProperty().bind(getSkinnable().zoomEnabledProperty());
		zoomResetButton.visibleProperty().bind(getSkinnable().zoomEnabledProperty());
		
		menu = new ContextMenu();
		menu.getStyleClass().add("windowmenu-controls");
		
		closeMenuItem = new MenuItem("Close");
		closeMenuItem.getStyleClass().addAll("windowmenu-control", "windowmenu-control-close");
		closeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/close_menu.png"));
		closeMenuItem.setOnAction(this::onCloseButtonAction);
		
		autoSizeMenuItem = new MenuItem("Autosize");
		autoSizeMenuItem.getStyleClass().addAll("windowmenu-control", "windowmenu-control-autosize");
		autoSizeMenuItem.setOnAction(this::onAutoSizeButtonAction);
		
		maximizeMenuItem = new MenuItem("Maximize");
		maximizeMenuItem.getStyleClass().addAll("windowmenu-control", "windowmenu-control-maximize");
		maximizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/max_menu.png"));
		maximizeMenuItem.setOnAction(this::onMaximizeButtonAction);
		
		menuSeparator = new SeparatorMenuItem();
		
		minimizeMenuItem = new MenuItem("Minimize");
		minimizeMenuItem.getStyleClass().addAll("windowmenu-control", "windowmenu-control-minimize");
		minimizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/iconify_menu.png"));
		minimizeMenuItem.setOnAction(this::onMinimizeButtonAction);
		
		menu.getItems().addAll(maximizeMenuItem, minimizeMenuItem, menuSeparator, autoSizeMenuItem, closeMenuItem);
		
		controls.getChildren().addAll(zoomInButton, zoomResetButton, zoomOutButton, minimizeButton, maximizeButton, closeButton);
		HBox.setMargin(zoomOutButton, new Insets(0, 16, 0, 0));
		
		closeButton.managedProperty().bind(skinnable.closeableProperty());
		closeButton.visibleProperty().bind(skinnable.closeableProperty());
		
		titleBar.managedProperty().bind(skinnable.decoratedProperty());
		titleBar.visibleProperty().bind(skinnable.decoratedProperty());
		
		maximizeButton.managedProperty().bind(skinnable.maximizeableProperty());
		maximizeButton.visibleProperty().bind(skinnable.maximizeableProperty());
		
		closeMenuItem.visibleProperty().bind(skinnable.closeableProperty());
		
		maximizeMenuItem.visibleProperty().bind(skinnable.maximizeableProperty());
		
		minimizeMenuItem.visibleProperty().bind(skinnable.minimizeableProperty());
		
		minimizeButton.managedProperty().bind(skinnable.minimizeableProperty());
		minimizeButton.visibleProperty().bind(skinnable.minimizeableProperty());
		
		skinnable.closeableProperty().addListener(this::onControlPropertyChanged);
		skinnable.maximizeableProperty().addListener(this::onControlPropertyChanged);
		skinnable.minimizeableProperty().addListener(this::onControlPropertyChanged);
		
		skinnable.addEventHandler(WindowStateChangedEvent.WINDOW_STATE_CHANGED, this::onWindowStateChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxHeight(double pWidth, double pTopInset, double pRightInset, double pBottomInset, double pLeftInset)
	{
		Parent content = getSkinnable().getContent();
		
		if (content instanceof Region && content.isManaged())
		{
			return ((Region)content).maxHeight(pWidth) + titleBar.maxHeight(pWidth) + pTopInset + pBottomInset;
		}
		
		return super.computeMaxHeight(pWidth, pTopInset, pRightInset, pBottomInset, pLeftInset);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxWidth(double pHeight, double pTopInset, double pRightInset, double pBottomInset, double pLeftInset)
	{
		Parent content = getSkinnable().getContent();
		
		if (content instanceof Region && content.isManaged())
		{
			return ((Region)content).maxWidth(pHeight) + pLeftInset + pRightInset;
		}
		
		return super.computeMaxWidth(pHeight, pTopInset, pRightInset, pBottomInset, pLeftInset);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double pWidth, double pTopInset, double pRightInset, double pBottomInset, double pLeftInset)
	{
		Parent content = getSkinnable().getContent();
		
		if (content instanceof Region && content.isManaged())
		{
			return ((Region)content).minHeight(pWidth) + titleBar.minHeight(pWidth) + pTopInset + pBottomInset;
		}
		
		return super.computeMinHeight(pWidth, pTopInset, pRightInset, pBottomInset, pLeftInset);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double pHeight, double pTopInset, double pRightInset, double pBottomInset, double pLeftInset)
	{
		Parent content = getSkinnable().getContent();
		
		if (content instanceof Region && content.isManaged())
		{
			return ((Region)content).minWidth(pHeight) + pLeftInset + pRightInset;
		}
		
		return super.computeMinWidth(pHeight, pTopInset, pRightInset, pBottomInset, pLeftInset);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double pWidth, double pTopInset, double pRightInset, double pBottomInset, double pLeftInset)
	{
		Parent content = getSkinnable().getContent();
		
		if (content instanceof Region && content.isManaged())
		{
			return ((Region)content).prefHeight(pWidth) + titleBar.prefHeight(pWidth) + pTopInset + pBottomInset;
		}
		
		return super.computePrefHeight(pWidth, pTopInset, pRightInset, pBottomInset, pLeftInset);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight, double pTopInset, double pRightInset, double pBottomInset, double pLeftInset)
	{
		Parent content = getSkinnable().getContent();
		
		if (content instanceof Region && content.isManaged())
		{
			return ((Region)content).prefWidth(pHeight) + pLeftInset + pRightInset;
		}
		
		return super.computePrefWidth(pHeight, pTopInset, pRightInset, pBottomInset, pLeftInset);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link MenuItem} for the autosize item.
	 * 
	 * @return the {@link MenuItem} for the autosize item.
	 */
	protected MenuItem getAutoSizeMenuItem()
	{
		return autoSizeMenuItem;
	}
	
	/**
	 * Gets the {@link Button} for the close item.
	 * 
	 * @return the {@link Button} for the close item.
	 */
	protected Button getCloseButton()
	{
		return closeButton;
	}
	
	/**
	 * Gets the {@link MenuItem} for the close item.
	 * 
	 * @return the {@link MenuItem} for the close item.
	 */
	protected MenuItem getCloseMenuItem()
	{
		return closeMenuItem;
	}
	
	/**
	 * Gets the {@link Button} for the maximize item.
	 * 
	 * @return the {@link Button} for the maximize item.
	 */
	protected Button getMaximizeButton()
	{
		return maximizeButton;
	}
	
	/**
	 * Gets the {@link MenuItem} for the maximize item.
	 * 
	 * @return the {@link MenuItem} for the maximize item.
	 */
	protected MenuItem getMaximizeMenuItem()
	{
		return maximizeMenuItem;
	}
	
	/**
	 * Gets the {@link Button} for the minimize item.
	 * 
	 * @return the {@link Button} for the minimize item.
	 */
	protected Button getMinimizeButton()
	{
		return minimizeButton;
	}
	
	/**
	 * Gets the {@link MenuItem} for the minimize item.
	 * 
	 * @return the {@link MenuItem} for the minimize item.
	 */
	protected MenuItem getMinimizeMenuItem()
	{
		return minimizeMenuItem;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren(double pContentX, double pContentY, double pContentWidth, double pContentHeight)
	{
		titleBar.autosize();
		titleBar.resizeRelocate(pContentX, pContentY, pContentWidth, titleBar.getHeight());
		
		FXInternalWindow skinnable = getSkinnable();
		
		if (skinnable.isDecorated())
		{
			if (skinnable.getContent() != null && skinnable.getContent().isManaged())
			{
				skinnable.getContent().resizeRelocate(pContentX, pContentY + titleBar.getHeight(), pContentWidth, pContentHeight - titleBar.getHeight());
			}
			else
			{
				// TODO HACK Multiplying pContentX/Y by two is not a solution for getting the padding.
				skinnable.resize(pContentX * 2 + titleBar.getWidth(), pContentY * 2 + titleBar.getHeight());
			}
		}
		else
		{
			if (skinnable.getContent() != null)
			{
				skinnable.getContent().resizeRelocate(pContentX, pContentY, pContentWidth, pContentHeight);
			}
		}
	}
	
	/**
	 * Invoked if the close {@link #autoSizeMenuItem} is clicked.
	 * 
	 * @param pActionEvent the event.
	 */
	private void onAutoSizeButtonAction(ActionEvent pActionEvent)
	{
		getSkinnable().autosize();
	}
	
	/**
	 * Invoked if the close {@link #closeButton} is clicked.
	 * 
	 * @param pActionEvent the event.
	 */
	private void onCloseButtonAction(ActionEvent pActionEvent)
	{
		// We will remove the hover state from the button to make sure that
		// it does not stay in that state (for example if an error dialog
		// opens which locks the screen with the FXSceneLocker, the button
		// won't notice that the mouse leaves it).
		closeButton.unsetHover();
		
		getSkinnable().close();
	}
	
	/**
	 * Invoked if any of the control properties (
	 * {@link FXInternalWindow#closeableProperty()},
	 * {@link FXInternalWindow#maximizeableProperty()},
	 * {@link FXInternalWindow#minimizeableProperty()}) changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onControlPropertyChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (getSkinnable().isCloseable() || getSkinnable().isMaximizeable() || getSkinnable().isMinimizeable())
		{
			HBox.setMargin(zoomOutButton, new Insets(0, 16, 0, 0));
		}
		else
		{
			HBox.setMargin(zoomOutButton, null);
		}
		
		menuSeparator.setVisible(getSkinnable().isMaximizeable() || getSkinnable().isMinimizeable());
	}
	
	/**
	 * Invoked if the icon is clicked.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onIconMouseClicked(MouseEvent pMouseEvent)
	{
		if (pMouseEvent.getButton() == MouseButton.PRIMARY)
		{
			if (pMouseEvent.getClickCount() >= 2 && getSkinnable().isCloseable())
			{
				getSkinnable().close();
			}
			else if (!menu.isShowing())
			{
				menu.hide();
				
				Point2D iconScreenPoint = icon.localToScreen(0, icon.getHeight());
				
				menu.show(icon, iconScreenPoint.getX(), iconScreenPoint.getY());
				
				pMouseEvent.consume();
			}
		}
	}
	
	/**
	 * Invoked if the {@link #maximizeButton} is clicked.
	 * 
	 * @param pActionEvent the event.
	 */
	private void onMaximizeButtonAction(ActionEvent pActionEvent)
	{
		FXInternalWindow skinnable = getSkinnable();
		
		if (skinnable.getState() == State.MAXIMIZED)
		{
			// We only have one previous state, so if we revert here to
			// the previous state it is possible that we can't restore
			// the window back to NORMAL state anymore.
			skinnable.setState(State.NORMAL);
			
			maximizeMenuItem.setText("Maximize");
			maximizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/max.png"));
		}
		else
		{
			skinnable.setState(State.MAXIMIZED);
			
			maximizeMenuItem.setText("Restore");
			maximizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/max_restore.png"));
			
			minimizeMenuItem.setText("Minimize");
			minimizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/iconify_menu.png"));
		}
	}
	
	/**
	 * Invoked if the {@link #minimizeButton} is clicked.
	 * 
	 * @param pActionEvent the event.
	 */
	private void onMinimizeButtonAction(ActionEvent pActionEvent)
	{
		FXInternalWindow skinnable = getSkinnable();
		
		if (skinnable.getState() == State.MINIMIZED)
		{
			skinnable.setState(skinnable.getPreviousState());
			
			if (skinnable.getState() == State.MAXIMIZED)
			{
				maximizeMenuItem.setText("Restore");
				maximizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/max_restore.png"));
			}
			
			minimizeMenuItem.setText("Minimize");
			minimizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/iconify_menu.png"));
		}
		else
		{
			skinnable.setState(State.MINIMIZED);
			
			minimizeMenuItem.setText("Restore");
			minimizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/max_restore_menu.png"));
			
			maximizeMenuItem.setText("Maximize");
			maximizeMenuItem.setGraphic(new FXImageRegion("/com/sibvisions/rad/ui/javafx/ext/mdi/css/max.png"));
		}
	}
	
	/**
	 * Invoked if the window changes its state.
	 * 
	 * @param pWindowStateChangedEvent the event.
	 */
	private void onWindowStateChanged(WindowStateChangedEvent pWindowStateChangedEvent)
	{
		boolean zoomEnabled = pWindowStateChangedEvent.getNewState() == State.NORMAL;
		
		if (zoomEnabled)
		{
			if (!zoomInButton.disableProperty().isBound())
			{
				zoomInButton.disableProperty().bind(getSkinnable().zoomInPossibleProperty().not());
				zoomOutButton.disableProperty().bind(getSkinnable().zoomOutPossibleProperty().not());
				zoomResetButton.disableProperty().bind(getSkinnable().zoomResetPossibleProperty().not());
			}
		}
		else
		{
			zoomInButton.disableProperty().unbind();
			zoomOutButton.disableProperty().unbind();
			zoomResetButton.disableProperty().unbind();
			
			zoomInButton.setDisable(true);
			zoomOutButton.setDisable(true);
			zoomResetButton.setDisable(true);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link HoverRemovableButton} is a {@link Button} extension which
	 * allows to remove the hover state.
	 * 
	 * @author Robert Zenz
	 */
	private static final class HoverRemovableButton extends Button
	{
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link HoverRemovableButton}.
		 */
		public HoverRemovableButton()
		{
			super();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Unsets the hover property.
		 */
		public void unsetHover()
		{
			setHover(false);
		}
		
	}	// HoverRemovableButton
	
}	// FXInternalWindowSkin

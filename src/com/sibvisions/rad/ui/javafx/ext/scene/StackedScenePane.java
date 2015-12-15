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

import java.util.Hashtable;

import javax.rad.util.TranslationMap;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.FXZoomRegion;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;

/**
 * The {@link StackedScenePane} is responsible for styling of scenes. It adds a
 * custom border around the scene and adds additional controls.
 * 
 * @author René Jahn
 */
public class StackedScenePane extends StackPane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** inactive (not focused) pseudo css class. */
	private final PseudoClass pseudoInactive = PseudoClass.getPseudoClass("inactive");
	
	/** maximized pseudo css class. */
	private final PseudoClass pseudoMaximized = PseudoClass.getPseudoClass("maximized");
	
	/** fullscreen pseudo css class. */
	private final PseudoClass pseudoFullScreen = PseudoClass.getPseudoClass("fullscreen");
	
	/** the header button animations. */
	private Hashtable<Button, Transition> htButtonAnimations = new Hashtable<>();
	
	/** the stage. */
	private Stage stage;
	
	/** the mouse controller. */
	private MouseController mouseController;
	
	/** the content pane. */
	private BorderPane panContent;
	
	/** the header pane. */
	private AnchorPane panHeader;
	
	/** the bounds for restore operations (optional). */
	private Bounds boundsRestore;
	
	/** The region used as root. */
	private FXZoomRegion zoomRoot;
	
	/** the stage title. */
	private HBox hbxTitle;
	
	/** the stage buttons. */
	private HBox hbxButtons;
	
	/** the stage icon. */
	private FXImageRegion irIcon;
	
	/** maximize restore menu item. */
	private FXImageRegion irMaxRestoreMenu;
	
	/** maximize menu item. */
	private FXImageRegion irMaxMenu;
	
	/** the title. */
	private Label lblTitle = new Label();
	
	/** the button used for zooming in. */
	private Button butZoomIn;
	
	/** the button used for zooming out. */
	private Button butZoomOut;
	
	/** the button used for reseting the zoom. */
	private Button butZoomReset;
	
	/** the close button. */
	private Button butClose;
	
	/** the maximize button. */
	private Button butMaximize;
	
	/** the iconify button. */
	private Button butIconify;
	
	/** the fullscreen button. */
	private Button butFullScreen;
	
	/** the space between minimize and full-screen. */
	private Label lblFullScreenSpacer;
	
	/** the space between zoom and full-screen. */
	private Label lblZoomSpacer;
	
	/** maximize property. */
	private SimpleBooleanProperty propMax;
	
	/** iconify property. */
	private SimpleBooleanProperty propIcon;
	
	/** close property. */
	private SimpleBooleanProperty propClose;
	
	/** full-screen property. */
	private SimpleBooleanProperty propFullScreen;
	
	/** the translation property. */
	private SimpleObjectProperty<TranslationMap> propTranslation;
	
	/** the shadow. */
	private Rectangle rectShadow;
	
	/** the background. */
	private Rectangle rectBackground;
	
	/** the border. */
	private Rectangle rectBorder;
	
	/** the frame area. */
	final Rectangle internal = new Rectangle();
	
	/** the are in the frame area. */
	final Rectangle internal2 = new Rectangle();
	
	/** the external frame area. */
	final Rectangle external = new Rectangle();
	
	/** the color adjust effect for header buttons. */
	private ColorAdjust effButtonColorAdjust;
	
	/** the active stage shadow. */
	private Effect effActiveShadow;
	
	/** the inactive stage shadow. */
	private Effect effInactiveShadow;
	
	/** the context menu. */
	private ContextMenu contextMenu;
	
	/** the full-screen menu item. */
	private MenuItem miFullScreen;
	
	/** the minimize menu item. */
	private MenuItem miIconify;
	
	/** the maximize menu item. */
	private MenuItem miMaximize;
	
	/** the close menu item. */
	private MenuItem miClose;
	
	/** the separator before full-screen menu item. */
	private SeparatorMenuItem sepFullScreen;
	
	/** the separator before close menu item. */
	private SeparatorMenuItem sepClose;
	
	/** the size of the active shadow. */
	private int iActiveShadowSize = 15;
	
	/** the size of the inactive shadow. */
	private int iInactiveShadowSize = 8;
	
	/** the max header height. */
	private int iHeaderHeight = 40;
	
	/** the border size. */
	private int iBorderSize = 5;
	
	/** whether maximizing is active. */
	private boolean bMaximizing;
	
	/** whether maximize or full-screen was delegated. */
	private boolean bDelegated;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link StackedScenePane}.
	 * 
	 * @param pStage the stage
	 * @param pRoot the root content
	 */
	public StackedScenePane(Stage pStage, Parent pRoot)
	{
		getStyleClass().add("styledscene-root");
		
		stage = pStage;
		
		zoomRoot = new FXZoomRegion(1, 2);
		zoomRoot.setContent(pRoot);
		
		stage.maximizedProperty().addListener((o, l, n) -> maximizeOrRestore(n.booleanValue(), true));
		stage.fullScreenProperty().addListener((o, l, n) -> fullScreenOrRestore(n.booleanValue(), true));
		stage.iconifiedProperty().addListener((o, l, n) ->
		{
			if (!n.booleanValue())
			{
				propIcon.set(false);
			}
		});
		stage.resizableProperty().addListener((o, l, n) -> setResizable(n.booleanValue()));
		
		lblTitle.textProperty().bind(stage.titleProperty());
		
		stage.getIcons().addListener((javafx.collections.ListChangeListener.Change<? extends Image> c) ->
		{
			irIcon.setImage(c.getList().get(0));
		});
		
		stage.focusedProperty().addListener((l, o, n) ->
		{
			pseudoClassStateChanged(pseudoInactive, !n.booleanValue());
			
			if (stage.isFullScreen() || stage.isMaximized())
			{
				return;
			}
			
			if (n.booleanValue())
			{
				rectShadow.setEffect(effActiveShadow);
			}
			else
			{
				rectShadow.setEffect(effInactiveShadow);
			}
		});
		
		// Properties 
		propMax = new SimpleBooleanProperty(false);
		propMax.addListener((l, o, n) -> maximizeOrRestore(n.booleanValue(), false));
		
		propIcon = new SimpleBooleanProperty(false);
		propIcon.addListener((l, o, n) ->
		{
			if (n.booleanValue())
			{
				iconify();
			}
		});
		
		propClose = new SimpleBooleanProperty(false);
		propClose.addListener((l, o, n) ->
		{
			if (n.booleanValue())
			{
				close();
			}
		});
		
		propFullScreen = new SimpleBooleanProperty(false);
		propFullScreen.addListener((l, o, n) -> fullScreenOrRestore(n.booleanValue(), false));
		
		propTranslation = new SimpleObjectProperty<>();
		propTranslation.addListener((l, o, n) -> updateTranslation());
		
		effActiveShadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, iActiveShadowSize, 0.2, 0, 0);
		effInactiveShadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(65, 65, 65, 1), iInactiveShadowSize, 0.1, 0, 0);
		
		effButtonColorAdjust = new ColorAdjust();
		effButtonColorAdjust.setBrightness(16);
		
		//Layers
		
		rectShadow = new Rectangle();
		rectShadow.getStyleClass().add("styledscene-shadow");
		rectShadow.setMouseTransparent(true);
		rectShadow.setEffect(effActiveShadow);
		rectShadow.setManaged(false);
		
		rectBackground = new Rectangle();
		rectBackground.getStyleClass().add("styledscene-background");
		rectBackground.setManaged(false);
		
		rectBorder = new Rectangle();
		rectBorder.setFill(Color.TRANSPARENT);
		rectBorder.getStyleClass().add("styledscene-border");
		rectBorder.setMouseTransparent(true);
		rectBorder.setManaged(false);
		
		irIcon = new FXImageRegion();
		irIcon.setVerticalStretched(true);
		irIcon.setHorizontalStretched(true);
		irIcon.setMaxHeight(34);
		irIcon.setVerticalAlignment(VPos.CENTER);
		irIcon.setHorizontalAlignment(HPos.LEFT);
		irIcon.setOnMouseClicked(e ->
		{
			if (e.getButton() == MouseButton.PRIMARY)
			{
				if (e.getClickCount() == 2)
				{
					closeProperty().set(true);
					e.consume();
				}
				else
				{
					toggleStageMenu();
				}
			}
		});
		
		irMaxMenu = new FXImageRegion(new Image("/com/sibvisions/rad/ui/javafx/ext/scene/css/max_menu.png"));
		irMaxRestoreMenu = new FXImageRegion(new Image("/com/sibvisions/rad/ui/javafx/ext/scene/css/max_restore_menu.png"));
		
		lblTitle.getStyleClass().add("titletext");
		lblTitle.setPrefHeight(iHeaderHeight - iBorderSize - 1);
		lblTitle.setAlignment(Pos.CENTER_LEFT);
		
		panContent = new BorderPane();
		panContent.getStyleClass().add("styledscene-content");
		panContent.setManaged(false);
		
		butZoomIn = new Button();
		butZoomIn.setTooltip(createTooltip("Zoom in"));
		butZoomIn.getStyleClass().addAll("styledscene-header-button", "styledscene-header-zoom-in");
		butZoomIn.setFocusTraversable(false);
		butZoomIn.setOnAction(e -> zoomRoot.zoomIn());
		butZoomIn.disableProperty().bind(zoomRoot.zoomInPossibleProperty().not());
		butZoomIn.managedProperty().bind(zoomRoot.zoomEnabledProperty());
		butZoomIn.visibleProperty().bind(zoomRoot.zoomEnabledProperty());
		
		butZoomReset = new Button();
		butZoomReset.setTooltip(createTooltip("Zoom reset"));
		butZoomReset.getStyleClass().addAll("styledscene-header-button", "styledscene-header-zoom-reset");
		butZoomReset.setFocusTraversable(false);
		butZoomReset.setOnAction(e -> zoomRoot.reset());
		butZoomReset.disableProperty().bind(zoomRoot.zoomResetPossibleProperty().not());
		butZoomReset.managedProperty().bind(zoomRoot.zoomEnabledProperty());
		butZoomReset.visibleProperty().bind(zoomRoot.zoomEnabledProperty());
		
		butZoomOut = new Button();
		butZoomOut.setTooltip(createTooltip("Zoom out"));
		butZoomOut.getStyleClass().addAll("styledscene-header-button", "styledscene-header-zoom-out");
		butZoomOut.setFocusTraversable(false);
		butZoomOut.setOnAction(e -> zoomRoot.zoomOut());
		butZoomOut.disableProperty().bind(zoomRoot.zoomOutPossibleProperty().not());
		butZoomOut.managedProperty().bind(zoomRoot.zoomEnabledProperty());
		butZoomOut.visibleProperty().bind(zoomRoot.zoomEnabledProperty());
		
		butClose = new Button();
		butClose.setTooltip(createTooltip("Close"));
		butClose.getStyleClass().addAll("styledscene-header-button", "styledscene-header-close");
		butClose.setFocusTraversable(false);
		butClose.setOnAction(e -> propClose.set(!propClose.get()));
		
		butMaximize = new Button();
		butMaximize.setTooltip(createTooltip("Maximize"));
		butMaximize.getStyleClass().addAll("styledscene-header-button", "styledscene-header-maximize");
		butMaximize.setFocusTraversable(false);
		butMaximize.setOnAction(e -> propMax.set(!propMax.get()));
		
		butIconify = new Button();
		butIconify.setTooltip(createTooltip("Minimize"));
		butIconify.getStyleClass().addAll("styledscene-header-button", "styledscene-header-iconify");
		butIconify.setFocusTraversable(false);
		butIconify.setOnAction(e -> propIcon.set(true));
		
		butFullScreen = new Button();
		butFullScreen.setTooltip(createTooltip("Fullscreen"));
		butFullScreen.getStyleClass().addAll("styledscene-header-button", "styledscene-header-fullscreen");
		butFullScreen.setFocusTraversable(false);
		butFullScreen.setOnAction(e -> propFullScreen.set(true));
		
		configureHeaderButton(butZoomIn);
		configureHeaderButton(butZoomOut);
		configureHeaderButton(butZoomReset);
		configureHeaderButton(butClose);
		configureHeaderButton(butMaximize);
		configureHeaderButton(butIconify);
		configureHeaderButton(butFullScreen);
		
		lblFullScreenSpacer = new Label();
		lblFullScreenSpacer.getStyleClass().add("styledscene-header-fullscreenspacer");
		
		lblZoomSpacer = new Label();
		lblZoomSpacer.getStyleClass().add("styledscene-header-zoomspacer");
		
		hbxButtons = new HBox(1);
		hbxButtons.setAlignment(Pos.CENTER_RIGHT);
		hbxButtons.getStyleClass().add("styledscene-header-buttons");
		hbxButtons.setPrefHeight(iHeaderHeight - iBorderSize - 1);
		hbxButtons.getChildren().addAll(butZoomIn, butZoomReset, butZoomOut, lblZoomSpacer, butFullScreen, lblFullScreenSpacer, butIconify, butMaximize, butClose);
		
		hbxTitle = new HBox(2);
		hbxTitle.setAlignment(Pos.TOP_LEFT);
		hbxTitle.getStyleClass().add("styledscene-header-title");
		hbxTitle.setPrefHeight(iHeaderHeight - iBorderSize - 1);
		hbxTitle.getChildren().addAll(irIcon, lblTitle);
		
		panHeader = new AnchorPane();
		panHeader.getStyleClass().add("styledscene-header");
		panHeader.setMaxHeight(iHeaderHeight);
		panHeader.setPrefHeight(iHeaderHeight);
		panHeader.getChildren().addAll(hbxTitle, hbxButtons);
		AnchorPane.setLeftAnchor(hbxTitle, Double.valueOf(0));
		AnchorPane.setRightAnchor(hbxButtons, Double.valueOf(0));
		
		panContent.setTop(panHeader);
		panContent.setCenter(zoomRoot);
		
		mouseController = new MouseController(this);
		
		createStageMenu();
		
		getChildren().addAll(rectShadow, rectBackground, rectBorder, panContent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double d)
	{
		return NodeUtil.getPrefWidth(panContent.getCenter()) + iActiveShadowSize * 2 + iBorderSize * 2 + 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double d)
	{
		//we didn't add bordersize and shadowsize -> make the stage a little bit smaller than calculated
		return NodeUtil.getPrefHeight(panContent.getCenter()) + iHeaderHeight + 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxWidth(double d)
	{
		return NodeUtil.getMaxWidth(panContent.getCenter()) + iActiveShadowSize * 2 + iBorderSize * 2 + 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxHeight(double d)
	{
		return NodeUtil.getMaxHeight(panContent.getCenter()) + iHeaderHeight + iActiveShadowSize * 2 + iBorderSize * 2 + 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double d)
	{
		return NodeUtil.getMinWidth(panContent.getCenter());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double d)
	{
		return NodeUtil.getMinHeight(panContent.getCenter());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutChildren()
	{
		Bounds bounds = super.getLayoutBounds();
		
		double w = bounds.getWidth();
		double h = bounds.getHeight();
		
		if (stage.isMaximized() && bDelegated && !stage.isFullScreen())
		{
			w += iActiveShadowSize;
			h -= iActiveShadowSize + iBorderSize * 2;
		}
		
		rectShadow.setLayoutX(iActiveShadowSize);
		rectShadow.setLayoutY(iActiveShadowSize);
		rectShadow.setWidth(w - iActiveShadowSize * 2);
		rectShadow.setHeight(h - iActiveShadowSize * 2);
		
		rectBackground.setLayoutX(iActiveShadowSize);
		rectBackground.setLayoutY(iActiveShadowSize);
		rectBackground.setWidth(w - iActiveShadowSize * 2);
		rectBackground.setHeight(h - iActiveShadowSize * 2);
		
		rectBorder.setLayoutX(iActiveShadowSize + iBorderSize - 1);
		rectBorder.setLayoutY(iActiveShadowSize + iBorderSize - 1);
		rectBorder.setWidth(w - iActiveShadowSize * 2 - iBorderSize * 2 + 2);
		rectBorder.setHeight(h - iActiveShadowSize * 2 - iBorderSize * 2 + 2);
		
		panContent.setLayoutX(iActiveShadowSize + iBorderSize);
		panContent.setLayoutY(iActiveShadowSize + iBorderSize);
		
		if (stage.isFullScreen())
		{
			panContent.resize(w - iActiveShadowSize - iBorderSize, h - iActiveShadowSize - iBorderSize);
		}
		else
		{
			panContent.resize(w - iActiveShadowSize * 2 - iBorderSize * 2, h - iActiveShadowSize * 2 - iBorderSize * 2);
		}
		
		external.relocate(bounds.getMinX() - iActiveShadowSize, bounds.getMinY() - iActiveShadowSize);
		internal.setX(0);
		internal.setY(0);
		internal.setWidth(w - iActiveShadowSize * 2);
		internal.setHeight(h - iActiveShadowSize * 2);
		internal.setArcWidth(rectShadow.getArcWidth());
		internal.setArcHeight(rectShadow.getArcHeight());
		
		external.setWidth(w + iActiveShadowSize * 2);
		external.setHeight(h + iActiveShadowSize * 2);
		
		Shape clip = Shape.subtract(external, internal);
		rectShadow.setClip(clip);
		
		internal.setX(0);
		internal.setY(0);
		internal.setWidth(internal.getWidth() - iBorderSize * 2);
		internal.setHeight(internal.getHeight() - iBorderSize * 2);
		
		if (!stage.isFullScreen())
		{
			panContent.setClip(internal);
		}
		else
		{
			panContent.setClip(null);
		}
		
		internal2.setX(0);
		internal2.setY(0);
		internal2.setWidth(w - iActiveShadowSize * 2);
		internal2.setHeight(h - iActiveShadowSize * 2);
		internal2.setArcWidth(rectShadow.getArcWidth());
		internal2.setArcHeight(rectShadow.getArcHeight());
		
		Rectangle rect = new Rectangle(0, 0, w, iHeaderHeight);
		
		Rectangle rect2 = new Rectangle();
		rect2.setX(0);
		rect2.setY(0);
		rect2.setWidth(internal.getWidth() + 2);
		rect2.setHeight(internal.getHeight() + 2);
		rect2.setArcWidth(rectShadow.getArcWidth());
		rect2.setArcHeight(rectShadow.getArcHeight());
		
		rectBorder.setClip(Shape.subtract(rect2, rect));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	Stage getStage()
	{
		return stage;
	}
	
	/**
	 * Sets the restore bounds.
	 * 
	 * @param pBounds the bounds
	 */
	void setRestoreBounds(Bounds pBounds)
	{
		boundsRestore = pBounds;
	}
	
	/**
	 * Gets the full-screen property.
	 * 
	 * @return the property
	 */
	SimpleBooleanProperty fullScreenProperty()
	{
		return propFullScreen;
	}
	
	/**
	 * Gets the maximize property.
	 * 
	 * @return the property
	 */
	SimpleBooleanProperty maximizeProperty()
	{
		return propMax;
	}
	
	/**
	 * Gets the iconify property.
	 * 
	 * @return the property
	 */
	SimpleBooleanProperty iconifyProperty()
	{
		return propIcon;
	}
	
	/**
	 * Gets the close property.
	 * 
	 * @return the property
	 */
	SimpleBooleanProperty closeProperty()
	{
		return propClose;
	}
	
	/**
	 * Gets the translation property.
	 * 
	 * @return the property for the translation
	 */
	public SimpleObjectProperty<TranslationMap> translationProperty()
	{
		return propTranslation;
	}
	
	/**
	 * Gets the mouse controller.
	 * 
	 * @return the controller
	 */
	protected MouseController getMouseController()
	{
		return mouseController;
	}
	
	/**
	 * Gets the root node.
	 * 
	 * @return the root node
	 */
	public Node getRoot()
	{
		return panContent.getCenter();
	}
	
	/**
	 * Gets the content pane. The content pane contains the stage header(title
	 * bar) and root node.
	 * 
	 * @return the content pane
	 * @see #getRoot()
	 */
	public Node getContentPane()
	{
		return panContent;
	}
	
	/**
	 * Gets the header pane. The header pane contains the title and various
	 * buttons, icons, etc.
	 * 
	 * @return the header pane
	 */
	public Node getHeaderPane()
	{
		return panHeader;
	}
	
	/**
	 * Gets the shadow rectangle.
	 * 
	 * @return the rectangle
	 */
	protected Rectangle getShadowRect()
	{
		return rectShadow;
	}
	
	/**
	 * Gets the background rectangle.
	 * 
	 * @return the rectangle
	 */
	protected Rectangle getBackgroundRect()
	{
		return rectBackground;
	}
	
	/**
	 * Gets the border rectangle.
	 * 
	 * @return the rectangle
	 */
	protected Rectangle getBorderRect()
	{
		return rectBorder;
	}
	
	/**
	 * Gets the stage image.
	 * 
	 * @return the image
	 */
	protected FXImageRegion getImage()
	{
		return irIcon;
	}
	
	/**
	 * Gets the size of the active shadow.
	 * 
	 * @return the size
	 */
	public int getActiveShadowSize()
	{
		return iActiveShadowSize;
	}
	
	/**
	 * Gets the size of the inactive shadow.
	 * 
	 * @return the size
	 */
	public int getInactiveShadowSize()
	{
		return iInactiveShadowSize;
	}
	
	/**
	 * Gets the size of the border.
	 * 
	 * @return the size
	 */
	public int getBorderSize()
	{
		return iBorderSize;
	}
	
	/**
	 * Gets the height of the header pane.
	 * 
	 * @return the height
	 */
	public int getHeaderHeight()
	{
		return iHeaderHeight;
	}
	
	/**
	 * Sets the stage maximized or restores to "before maximized" state.
	 * 
	 * @param pMaximize <code>true</code> to maximize the scene
	 * @param pDelegated <code>true</code> if call was delegated
	 */
	private void maximizeOrRestore(boolean pMaximize, boolean pDelegated)
	{
		if (bMaximizing)
		{
			return;
		}
		
		bMaximizing = true;
		
		try
		{
			pseudoClassStateChanged(pseudoMaximized, pMaximize);
			
			if (pMaximize)
			{
				bDelegated = pDelegated;
				
				rectShadow.setVisible(false);
				rectShadow.setEffect(null);
			}
			else
			{
				bDelegated = false;
				
				rectShadow.setVisible(true);
				rectShadow.setEffect(effActiveShadow);
			}
			
			Rectangle2D rectVBounds = null;
			
			if (pMaximize)
			{
				ObservableList<Screen> olScreens = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
				
				if (olScreens.size() > 0)
				{
					Screen screen = olScreens.get(0);
					
					rectVBounds = screen.getVisualBounds();
				}
			}
			
			if (!pDelegated)
			{
				stage.setMaximized(pMaximize);
				
				if (pMaximize)
				{
					stage.setX(rectVBounds.getMinX());
					stage.setY(rectVBounds.getMinY());
				}
			}
			else
			{
				propMax.set(pMaximize);
			}
			
			TranslationMap tmap = translationProperty().get();
			
			if (pMaximize)
			{
				if (rectVBounds != null)
				{
					resizeRelocate(-iActiveShadowSize,
							-iActiveShadowSize,
							rectVBounds.getWidth() + iActiveShadowSize * 2,
							rectVBounds.getHeight() + iActiveShadowSize * 2);
				}
				
				miMaximize.setText(tmap == null ? "Restore" : tmap.translate("Restore"));
				setTooltipText(butMaximize, "Restore");
				
				miMaximize.setGraphic(irMaxRestoreMenu);
			}
			else
			{
				if (boundsRestore != null)
				{
					stage.setX(boundsRestore.getMinX());
					stage.setY(boundsRestore.getMinY());
					
					boundsRestore = null;
				}
				
				resizeRelocate(0, 0, stage.getWidth(), stage.getHeight());
				
				miMaximize.setText(tmap == null ? "Maximize" : tmap.translate("Maximize"));
				setTooltipText(butMaximize, "Maximize");
				
				miMaximize.setGraphic(irMaxMenu);
			}
		}
		finally
		{
			bMaximizing = false;
		}
	}
	
	/**
	 * Sets the window full-screen or restores to previous state.
	 * 
	 * @param pFullScreen <code>true</code> to show the scene in full-screen
	 *            mode
	 * @param pDelegated <code>true</code> if call was delegated
	 */
	private void fullScreenOrRestore(boolean pFullScreen, boolean pDelegated)
	{
		if (bMaximizing)
		{
			return;
		}
		
		bMaximizing = true;
		
		try
		{
			pseudoClassStateChanged(pseudoFullScreen, pFullScreen);
			
			if (pFullScreen)
			{
				bDelegated = pDelegated;
				
				rectShadow.setVisible(false);
				rectShadow.setEffect(null);
				
				rectBorder.setVisible(false);
				rectBackground.setVisible(false);
				
				panHeader.setVisible(false);
			}
			else
			{
				bDelegated = false;
				
				panHeader.setVisible(true);
				
				rectBorder.setVisible(true);
				rectBackground.setVisible(true);
				
				rectShadow.setVisible(true);
				rectShadow.setEffect(effActiveShadow);
			}
			
			if (!pDelegated)
			{
				stage.setFullScreen(pFullScreen);
			}
			else
			{
				fullScreenProperty().set(pFullScreen);
			}
			
			if (pFullScreen)
			{
				if (stage.isMaximized())
				{
					resizeRelocate(-iActiveShadowSize - iBorderSize,
							-iActiveShadowSize - iBorderSize - iHeaderHeight,
							stage.getWidth() + iActiveShadowSize + iBorderSize,
							stage.getHeight() + iActiveShadowSize + iHeaderHeight + iBorderSize);
				}
				else
				{
					if (pDelegated)
					{
						relocate(-iActiveShadowSize - iBorderSize, -iActiveShadowSize - iBorderSize - iHeaderHeight);
					}
					else
					{
						ObservableList<Screen> olScreens = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
						
						if (olScreens.size() > 0)
						{
							Screen screen = olScreens.get(0);
							
							Rectangle2D rectBounds = screen.getBounds();
							
							resizeRelocate(-iActiveShadowSize - iBorderSize,
									-iActiveShadowSize - iHeaderHeight - iBorderSize,
									rectBounds.getWidth() + iActiveShadowSize + iBorderSize,
									rectBounds.getHeight() + iHeaderHeight + iActiveShadowSize + iBorderSize);
						}
					}
				}
			}
			else
			{
				if (stage.isMaximized())
				{
					ObservableList<Screen> olScreens = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
					
					if (olScreens.size() > 0)
					{
						Screen screen = olScreens.get(0);
						
						Rectangle2D rectVBounds = screen.getVisualBounds();
						
						resizeRelocate(-iActiveShadowSize, -iActiveShadowSize,
								rectVBounds.getWidth() + iActiveShadowSize * 2,
								rectVBounds.getHeight() + iActiveShadowSize * 2);
					}
				}
				else
				{
					resizeRelocate(0, 0,
							stage.getWidth() - iActiveShadowSize,
							stage.getHeight() - iActiveShadowSize * 2 - iBorderSize * 2);
				}
				
				propFullScreen.set(false);
			}
		}
		finally
		{
			bMaximizing = false;
		}
	}
	
	/**
	 * Sets whether the stage is resizable.
	 * 
	 * @param pResizable <code>true</code> if stage is resizable,
	 *            <code>false</code> otherwise
	 */
	private void setResizable(boolean pResizable)
	{
		butMaximize.setVisible(pResizable);
		butIconify.setVisible(pResizable);
		butFullScreen.setVisible(pResizable);
		
		miIconify.setVisible(pResizable);
		miMaximize.setVisible(pResizable);
		miFullScreen.setVisible(pResizable);
		
		sepFullScreen.setVisible(pResizable);
		sepClose.setVisible(pResizable);
	}
	
	/**
	 * Iconifies or Deiconifies the stage.
	 */
	private void iconify()
	{
		stage.setIconified(!stage.isIconified());
	}
	
	/**
	 * Closes the stage.
	 */
	private void close()
	{
		//don't force close
		Platform.runLater(() ->
		{
			stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});
	}
	
	/**
	 * Configures a button for the header area.
	 * 
	 * @param pButton the button
	 */
	private void configureHeaderButton(Button pButton)
	{
		pButton.setOnMouseEntered(e -> animateButton((Button)e.getSource()));
		pButton.setOnMouseExited(e ->
		{
			Button button = (Button)e.getSource();
			
			Animation an = htButtonAnimations.get(button);
			
			if (an != null)
			{
				an.jumpTo("end");
				an.stop();
			}
			
			((Button)e.getSource()).setEffect(null);
		});
	}
	
	/**
	 * Animates a custom button with a scale transition.
	 * 
	 * @param pButton the button
	 * @see ScaleTransition
	 */
	private void animateButton(Button pButton)
	{
		pButton.setEffect(effButtonColorAdjust);
		
		Animation an = htButtonAnimations.get(pButton);
		
		if (an != null)
		{
			an.playFromStart();
		}
		else
		{
			ScaleTransition st = new ScaleTransition(Duration.millis(120), pButton);
			st.setByX(0.4f);
			st.setByY(0.4f);
			st.setCycleCount(2);
			st.setAutoReverse(true);
			
			htButtonAnimations.put(pButton, st);
			
			st.play();
		}
	}
	
	/**
	 * Gets the default min width of the stage.
	 * 
	 * @return the default min width
	 */
	double getDefaultMinStageWidth()
	{
		return iActiveShadowSize * 2 + iBorderSize * 2 + irIcon.getWidth() + butClose.getWidth() + 10 +
				(stage.isResizable() ? butMaximize.getWidth() + butIconify.getWidth() + butFullScreen.getWidth() : 0);
	}
	
	/**
	 * Gets the default min height of the stage.
	 * 
	 * @return the default min height
	 */
	double getDefaultMinStageHeight()
	{
		return iActiveShadowSize * 2 + iBorderSize + iHeaderHeight + 1;
	}
	
	/**
	 * Initialization after scene was set.
	 */
	void initScene()
	{
		//accelerators won't work with some keycode combinations
		getScene().addEventFilter(KeyEvent.KEY_PRESSED, e ->
		{
			if (e.isControlDown())
			{
				if (e.getCode() == KeyCode.C)
				{
					propClose.set(true);
				}
				else if (e.getCode() == KeyCode.F)
				{
					propFullScreen.set(!propFullScreen.get());
				}
			}
		});
	}
	
	/**
	 * Creates the context menu for the stage.
	 */
	private void createStageMenu()
	{
		contextMenu = new ContextMenu();
		contextMenu.setAutoHide(true);
		contextMenu.setHideOnEscape(true);
		
		miIconify = new MenuItem("Minimize");
		miIconify.setGraphic(new FXImageRegion(new Image("/com/sibvisions/rad/ui/javafx/ext/scene/css/iconify_menu.png")));
		miIconify.setOnAction(e ->
		{
			contextMenu.hide();
			propIcon.set(true);
		});
		
		miMaximize = new MenuItem("Maximize");
		miMaximize.setGraphic(irMaxMenu);
		miMaximize.setOnAction(e ->
		{
			contextMenu.hide();
			propMax.set(!propMax.get());
		});
		
		sepFullScreen = new SeparatorMenuItem();
		
		miFullScreen = new MenuItem("Fullscreen");
		miFullScreen.setGraphic(new FXImageRegion(new Image("/com/sibvisions/rad/ui/javafx/ext/scene/css/fullscreen_menu.png")));
		miFullScreen.setOnAction(e ->
		{
			contextMenu.hide();
			propFullScreen.set(true);
		});
		miFullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
		
		sepClose = new SeparatorMenuItem();
		
		miClose = new MenuItem("Close");
		miClose.setGraphic(new FXImageRegion(new Image("/com/sibvisions/rad/ui/javafx/ext/scene/css/close_menu.png")));
		miClose.setOnAction(e ->
		{
			contextMenu.hide();
			propClose.set(true);
		});
		miClose.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		
		contextMenu.getItems().addAll(miIconify, miMaximize, sepFullScreen, miFullScreen, sepClose, miClose);
	}
	
	/**
	 * Shows or hides the stage menu.
	 */
	private void toggleStageMenu()
	{
		if (contextMenu.isShowing())
		{
			contextMenu.hide();
		}
		else
		{
			contextMenu.show(irIcon, Side.BOTTOM, 0, 0);
		}
	}
	
	/**
	 * Updates the translation.
	 */
	private void updateTranslation()
	{
		TranslationMap tmap = propTranslation.get();
		
		miIconify.setText(tmap == null ? "Minimize" : tmap.translate("Minimize"));
		miMaximize.setText(tmap == null ? "Maximize" : tmap.translate("Maximize"));
		miFullScreen.setText(tmap == null ? "Fullscreen" : tmap.translate("Fullscreen"));
		miClose.setText(tmap == null ? "Close" : tmap.translate("Close"));
		
		setTooltipText(butFullScreen, "Fullscreen");
		setTooltipText(butIconify, "Minimize");
		setTooltipText(butMaximize, "Maximize");
		setTooltipText(butClose, "Close");
	}
	
	/**
	 * Creates a tooltip for the given text.
	 * 
	 * @param pText the text
	 * @return the tooltip
	 */
	private Tooltip createTooltip(String pText)
	{
		Tooltip ttip = new Tooltip(pText);
		ttip.setHideOnEscape(true);
		
		return ttip;
	}
	
	/**
	 * Sets the tooltip text of the given button.
	 * 
	 * @param pButton the button
	 * @param pText the text
	 */
	private void setTooltipText(Button pButton, String pText)
	{
		Tooltip ttip = pButton.getTooltip();
		
		TranslationMap tmap = propTranslation.get();
		
		if (tmap == null)
		{
			ttip.setText(pText);
		}
		else
		{
			ttip.setText(tmap.translate(pText));
		}
	}
	
}	// StackedScenePane

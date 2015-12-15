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
package com.sibvisions.rad.ui.javafx.ext.mdi;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.FXAbstractZoomableControl;
import com.sibvisions.rad.ui.javafx.ext.mdi.skin.FXInternalWindowSkin;
import com.sibvisions.rad.ui.javafx.ext.panes.FXPositioningPane;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;

/**
 * The {@link FXInternalWindow} is an internal frame implementation for JavaFX,
 * that allows to create MDI applications.
 * 
 * @author Robert Zenz
 */
public class FXInternalWindow extends FXAbstractZoomableControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default class name of the content used for styling. */
	public static final String DEFAULT_CONTENT_STYLE_CLASS = "content";
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/mdi/css/fxinternalwindow.css";
	
	/** The default class name used for styling. */
	public static final String DEFAULT_STYLE_CLASS = "internal-window";
	
	/** The {@link PseudoClass} used for {@link #active}. */
	private static final PseudoClass ACTIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("active");
	
	/** The {@link PseudoClass} used for {@link #borderless}. */
	private static final PseudoClass BORDERLESS_PSEUDO_CLASS = PseudoClass.getPseudoClass("borderless");
	
	/**
	 * The {@link PseudoClass} used for {@link #state} when it is
	 * {@link State#MAXIMIZED}.
	 */
	private static final PseudoClass MAXIMIZED_PSEUDO_CLASS = PseudoClass.getPseudoClass("maximized");
	
	/**
	 * The {@link PseudoClass} used for {@link #state} when it is
	 * {@link State#MINIMIZED}.
	 */
	private static final PseudoClass MINIMIZED_PSEUDO_CLASS = PseudoClass.getPseudoClass("minimized");
	
	/** The property for if the {@link FXInternalWindow} is active. */
	private BooleanProperty active;
	
	/** The property for if the {@link FXInternalWindow} is borderless. */
	private BooleanProperty borderless;
	
	/** The property for if the {@link FXInternalWindow} can be closed. */
	private BooleanProperty closeable;
	
	/** The content. */
	private ObjectProperty<Parent> content;
	
	/** The property for if the {@link FXInternalWindow} is decorated. */
	private BooleanProperty decorated;
	
	/** The property for if the size of the edges, used for resizing. */
	private DoubleProperty edgeSize;
	
	/** The icon. */
	private ObjectProperty<Image> icon;
	
	/** The property for if the {@link FXInternalWindow} can be maximized. */
	private BooleanProperty maximizeable;
	
	/** The property for if the {@link FXInternalWindow} can be minimized. */
	private BooleanProperty minimizeable;
	
	/** The property for if the {@link FXInternalWindow} is modal. */
	private BooleanProperty modal;
	
	/** The property for if the {@link FXInternalWindow} can be moved. */
	private BooleanProperty moveable;
	
	/** The {@link EventHandler} for the closed event. */
	private EventHandler<WindowClosedEvent> onClosed;
	
	/** The {@link EventHandler} for the closing event. */
	private EventHandler<WindowClosingEvent> onClosing;
	
	/** The {@link EventHandler} for the focus changed of the scene. */
	private ChangeListener<Node> onSceneFocusOwnerChanged;
	
	/** The {@link EventHandler} for when the window changes its state. */
	private EventHandler<WindowStateChangedEvent> onStateChanged;
	
	/** The previous bounds, by state. */
	private Map<State, Bounds> previousBounds;
	
	/** The previous focus owner in this {@link FXInternalWindow} . */
	private Node previousFocusOwner;
	
	/** The previous state. */
	private State previousState;
	
	/**
	 * The property for if the {@link FXInternalWindow} should be raised when
	 * clicked.
	 */
	private BooleanProperty raiseOnClick;
	
	/** The property for if the {@link FXInternalWindow} can be resized. */
	private BooleanProperty resizeable;
	
	/** The state. */
	private ObjectProperty<State> state;
	
	/** The title. */
	private StringProperty title;
	
	/**
	 * The property for if the active property should change if the focus
	 * changes.
	 */
	private BooleanProperty triggerActiveOnFocusChange;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXInternalWindow}.
	 */
	public FXInternalWindow()
	{
		this("");
	}
	
	/**
	 * Creates a new instance of {@link FXInternalWindow}.
	 *
	 * @param pTitle the title.
	 */
	public FXInternalWindow(String pTitle)
	{
		this(pTitle, 0.5, 2);
	}
	
	/**
	 * Creates a new instance of {@link FXInternalWindow}.
	 *
	 * @param pTitle the title.
	 * @param pMinZoomValue the minimum zoom value.
	 * @param pMaxZoomValue the maximum zoom value.
	 */
	public FXInternalWindow(String pTitle, double pMinZoomValue, double pMaxZoomValue)
	{
		super(pMinZoomValue, pMaxZoomValue);
		
		onSceneFocusOwnerChanged = this::onSceneFocusOwnerChanged;
		
		previousBounds = new HashMap<>();
		
		getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		
		sceneProperty().addListener(this::onSceneChanged);
		
		active = new SimpleBooleanProperty(false);
		active.addListener(this::onActiveChanged);
		
		borderless = new SimpleBooleanProperty(false);
		borderless.addListener(this::onBorderlessChanged);
		
		closeable = new SimpleBooleanProperty(true);
		
		content = new SimpleObjectProperty<>();
		content.addListener(this::onContentChanged);
		
		decorated = new SimpleBooleanProperty(true);
		
		edgeSize = new SimpleDoubleProperty(24);
		
		icon = new SimpleObjectProperty<>();
		
		maximizeable = new SimpleBooleanProperty(true);
		
		minimizeable = new SimpleBooleanProperty(true);
		
		modal = new SimpleBooleanProperty(false);
		modal.addListener(this::onModalChanged);
		
		moveable = new SimpleBooleanProperty(true);
		
		previousState = State.NORMAL;
		
		raiseOnClick = new SimpleBooleanProperty(true);
		
		resizeable = new SimpleBooleanProperty(true);
		
		state = new SimpleObjectProperty<>(State.NORMAL);
		state.addListener(this::onStateChanged);
		
		title = new SimpleStringProperty(pTitle);
		
		triggerActiveOnFocusChange = new SimpleBooleanProperty(true);
		
		scaleXProperty().bind(zoomHelper.zoomProperty());
		scaleYProperty().bind(zoomHelper.zoomProperty());
		
		setMinSize(256, 64);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserAgentStylesheet()
	{
		return DEFAULT_STYLE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toFront()
	{
		if (getParent() instanceof FXPositioningPane)
		{
			((FXPositioningPane)getParent()).toFront(this);
		}
		else
		{
			super.toFront();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new FXInternalWindowSkin(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The property for if this {@link FXInternalWindow} is active.
	 * 
	 * @return the active property.
	 */
	public BooleanProperty activeProperty()
	{
		return active;
	}
	
	/**
	 * The property for if this {@link FXInternalWindow} is borderless.
	 * 
	 * @return the borderless property.
	 */
	public BooleanProperty borderlessProperty()
	{
		return borderless;
	}
	
	/**
	 * Closes this {@link FXInternalWindow}.
	 * <p>
	 * The window closing event will be fired, which gives all subscribers the
	 * opportunity to cancel the closing by consuming the event.
	 */
	public void close()
	{
		Parent parent = getParent();
		
		if (parent instanceof Pane)
		{
			if (fireEvent(new WindowClosingEvent(this), onClosing).isConsumed())
			{
				return;
			}
			
			dispose();
		}
	}
	
	/**
	 * The property for if this {@link FXInternalWindow} is closeable.
	 * 
	 * @return the closeable property.
	 */
	public BooleanProperty closeableProperty()
	{
		return closeable;
	}
	
	/**
	 * The property for the content.
	 * 
	 * @return the content property.
	 */
	public ObjectProperty<Parent> contentProperty()
	{
		return content;
	}
	
	/**
	 * The property for if this {@link FXInternalWindow} is decorated.
	 * 
	 * @return the decorated property.
	 */
	public BooleanProperty decoratedProperty()
	{
		return decorated;
	}
	
	/**
	 * Disposes this {@link FXInternalWindow}.
	 * <p>
	 * Contrary to {@link #close()}, the closing even will not be fire, giving
	 * no opportunity to cancel it.
	 */
	public void dispose()
	{
		Parent parent = getParent();
		
		if (parent instanceof Pane)
		{
			((Pane)parent).getChildren().remove(this);
			zoomReset();
			
			fireEvent(new WindowClosedEvent(this), onClosed);
		}
	}
	
	/**
	 * The property for the size of the resize-edges.
	 * 
	 * @return the property for the resize-edge size.
	 */
	public DoubleProperty edgeSizeProperty()
	{
		return edgeSize;
	}
	
	/**
	 * Gets the content of this {@link FXInternalWindow}.
	 * 
	 * @return the content.
	 */
	public Parent getContent()
	{
		return content.get();
	}
	
	/**
	 * Gets the size of the resize-edges.
	 * 
	 * @return the size of the resize-edges.
	 */
	public double getEdgeSize()
	{
		return edgeSize.get();
	}
	
	/**
	 * Gets the icon.
	 * 
	 * @return the icon.
	 */
	public Image getIcon()
	{
		return icon.get();
	}
	
	/**
	 * Gets the {@link EventHandler} for the closed event.
	 * 
	 * @return the handler for the closed event.
	 */
	public EventHandler<WindowClosedEvent> getOnClosed()
	{
		return onClosed;
	}
	
	/**
	 * Gets the {@link EventHandler} for the closing event.
	 * 
	 * @return the handler for the closing event.
	 */
	public EventHandler<WindowClosingEvent> getOnClosing()
	{
		return onClosing;
	}
	
	/**
	 * Gets the {@link EventHandler} for the state changed event.
	 * 
	 * @return the handler for the state changed event.
	 */
	public EventHandler<WindowStateChangedEvent> getOnStateChanged()
	{
		return onStateChanged;
	}
	
	/**
	 * Gets the previous bounds for the given state.
	 * 
	 * @param pState the state for which to get the previous bounds.
	 * @return the previous bounds for the given state.
	 */
	public Bounds getPreviousBounds(State pState)
	{
		return previousBounds.get(pState);
	}
	
	/**
	 * Gets the previous focus owner in the content of this
	 * {@link FXInternalWindow}.
	 * 
	 * @return the previous focus owner.
	 */
	public Node getPreviousFocusOwner()
	{
		return previousFocusOwner;
	}
	
	/**
	 * Gets the previous state.
	 * 
	 * @return the previous state.
	 */
	public State getPreviousState()
	{
		return previousState;
	}
	
	/**
	 * Gets the state.
	 * 
	 * @return the state.
	 */
	public State getState()
	{
		return state.get();
	}
	
	/**
	 * Gets the title.
	 * 
	 * @return the title.
	 */
	public String getTitle()
	{
		return title.get();
	}
	
	/**
	 * Gets the property for the icon.
	 * 
	 * @return the icon property.
	 */
	public ObjectProperty<Image> iconProperty()
	{
		return icon;
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is active.
	 * 
	 * @return {@code true} if it is active.
	 */
	public boolean isActive()
	{
		return active.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is borderless.
	 * 
	 * @return {@code true} if it is borderless.
	 */
	public boolean isBorderless()
	{
		return borderless.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is closeable.
	 * 
	 * @return {@code true} if it is closeable.
	 */
	public boolean isCloseable()
	{
		return closeable.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is decorated.
	 * 
	 * @return {@code true} if it is decorated.
	 */
	public boolean isDecorated()
	{
		return decorated.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is maximizeable.
	 * 
	 * @return {@code true} if it is maximizeable.
	 */
	public boolean isMaximizeable()
	{
		return maximizeable.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is minimizeable.
	 * 
	 * @return {@code true} if it is minimizeable.
	 */
	public boolean isMinimizeable()
	{
		return minimizeable.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is modal.
	 * 
	 * @return {@code true} if it is modal.
	 */
	public boolean isModal()
	{
		return modal.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is moveable.
	 * 
	 * @return {@code true} if it is moveable.
	 */
	public boolean isMoveable()
	{
		return moveable.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} should be raised if it is clicked
	 * on.
	 * 
	 * @return {@code true} if it should be raised if it is clicked on.
	 */
	public boolean isRaiseOnClick()
	{
		return raiseOnClick.get();
	}
	
	/**
	 * Gets if this {@link FXInternalWindow} is resizeable.
	 * 
	 * @return {@code true} if it is resizeable.
	 */
	public boolean isResizeable()
	{
		return resizeable.get();
	}
	
	/**
	 * Gets the property for if this {@link FXInternalWindow} is maximizeable.
	 * 
	 * @return the property for if it is maximizeable.
	 */
	public BooleanProperty maximizeableProperty()
	{
		return maximizeable;
	}
	
	/**
	 * Gets the property for if this {@link FXInternalWindow} is minimizeable.
	 * 
	 * @return the property for if it is minimizeable.
	 */
	public BooleanProperty minimizeableProperty()
	{
		return minimizeable;
	}
	
	/**
	 * Gets the property for if this {@link FXInternalWindow} is modal.
	 * 
	 * @return the property for if it is modal.
	 */
	public BooleanProperty modalProperty()
	{
		return modal;
	}
	
	/**
	 * Gets the property for if this {@link FXInternalWindow} is moveable.
	 * 
	 * @return the property for if it is moveable.
	 */
	public BooleanProperty moveableProperty()
	{
		return moveable;
	}
	
	/**
	 * Gets the property for if this {@link FXInternalWindow} is resizeable.
	 * 
	 * @return the property for if it is resizeable.
	 */
	public BooleanProperty resizeableProperty()
	{
		return resizeable;
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is active.
	 * 
	 * @param pActive {@code true} if it should be active.
	 */
	public void setActive(boolean pActive)
	{
		active.set(pActive);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is borderless.
	 * 
	 * @param pBorderless {@code true} if it should be borderless.
	 */
	public void setBorderless(boolean pBorderless)
	{
		borderless.set(pBorderless);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is closeable.
	 * 
	 * @param pCloseable {@code true} if it should be closeable.
	 */
	public void setCloseable(boolean pCloseable)
	{
		closeable.set(pCloseable);
	}
	
	/**
	 * Sets the content.
	 * 
	 * @param pContent the content.
	 */
	public void setContent(Parent pContent)
	{
		content.set(pContent);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is decorated.
	 * 
	 * @param pDecorated {@code true} if it should be decorated.
	 */
	public void setDecorated(boolean pDecorated)
	{
		decorated.set(pDecorated);
	}
	
	/**
	 * Sets the icon.
	 * 
	 * @param pIcon the icon. {@code null} to remove.
	 */
	public void setIcon(Image pIcon)
	{
		icon.set(pIcon);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is maximizeable.
	 * 
	 * @param pMovable {@code true} if it should be maximizeable.
	 */
	public void setMaximizeable(boolean pMovable)
	{
		maximizeable.set(pMovable);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is minimizeable.
	 * 
	 * @param pMovable {@code true} if it should be minimizeable.
	 */
	public void setMinimizable(boolean pMovable)
	{
		minimizeable.set(pMovable);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is modal.
	 * 
	 * @param pModal {@code true} if it should be modal.
	 */
	public void setModal(boolean pModal)
	{
		modal.set(pModal);
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is moveable.
	 * 
	 * @param pMovable {@code true} if it should be moveable.
	 */
	public void setMovable(boolean pMovable)
	{
		moveable.set(pMovable);
	}
	
	/**
	 * Sets the on closed {@link EventHandler}.
	 * 
	 * @param pOnClosed the on closed handler.
	 */
	public void setOnClosed(EventHandler<WindowClosedEvent> pOnClosed)
	{
		onClosed = pOnClosed;
	}
	
	/**
	 * Sets the on closing {@link EventHandler}.
	 * 
	 * @param pOnClosing the on closing handler.
	 */
	public void setOnClosing(EventHandler<WindowClosingEvent> pOnClosing)
	{
		onClosing = pOnClosing;
	}
	
	/**
	 * Sets the on state changed {@link EventHandler}.
	 * 
	 * @param pOnStateChanged the on state changed handler.
	 */
	public void setOnStateChanged(EventHandler<WindowStateChangedEvent> pOnStateChanged)
	{
		onStateChanged = pOnStateChanged;
	}
	
	/**
	 * Sets if this {@link FXInternalWindow} is resizeable.
	 * 
	 * @param pResizeable {@code true} if it should be resizeable.
	 */
	public void setResizeable(boolean pResizeable)
	{
		resizeable.set(pResizeable);
	}
	
	/**
	 * Sets the state.
	 * 
	 * @param pState the state.
	 */
	public void setState(State pState)
	{
		state.set(pState);
	}
	
	/**
	 * Sets the title.
	 * 
	 * @param pTitle the title.
	 */
	public void setTitle(String pTitle)
	{
		title.set(pTitle);
	}
	
	/**
	 * Gets the property for the state.
	 * 
	 * @return the property for the state.
	 */
	public ObjectProperty<State> stateProperty()
	{
		return state;
	}
	
	/**
	 * Gets the property for the title.
	 * 
	 * @return the property for the title.
	 */
	public StringProperty titleProperty()
	{
		return title;
	}
	
	/**
	 * Fires the given {@link Event} over the given {@link EventHandler} and
	 * over {@link #fireEvent(Event)}.
	 * 
	 * @param <E> the type of the event to fire.
	 * @param pEvent the {@link Event}.
	 * @param pEventHandler the {@link EventHandler}. Can be {@code null}.
	 * @return the {@link Event}.
	 */
	private <E extends Event> E fireEvent(E pEvent, EventHandler<E> pEventHandler)
	{
		if (pEventHandler != null)
		{
			pEventHandler.handle(pEvent);
		}
		
		fireEvent(pEvent);
		
		return pEvent;
	}
	
	/**
	 * Invoked if the active property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onActiveChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, pNewValue.booleanValue());
		
		if (pNewValue.booleanValue())
		{
			NodeUtil.triggerDefaultButton(this);
			
			if (previousFocusOwner == null || previousFocusOwner == this)
			{
				previousFocusOwner = NodeUtil.findFirstFocusableNode(this);
			}
			
			if (previousFocusOwner != null)
			{
				previousFocusOwner.requestFocus();
			}
			else
			{
				requestFocus();
			}
		}
	}
	
	/**
	 * Invoked if the borderless property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onBorderlessChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		pseudoClassStateChanged(BORDERLESS_PSEUDO_CLASS, pNewValue.booleanValue());
	}
	
	/**
	 * Invoked if the content property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onContentChanged(ObservableValue<? extends Node> pObservable, Node pOldValue, Node pNewValue)
	{
		if (pOldValue != null)
		{
			getChildren().remove(pOldValue);
			pOldValue.getStyleClass().remove(DEFAULT_CONTENT_STYLE_CLASS);
		}
		
		if (pNewValue != null)
		{
			pNewValue.getStyleClass().add(DEFAULT_CONTENT_STYLE_CLASS);
			getChildren().add(pNewValue);
		}
	}
	
	/**
	 * Invoked if the modal property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onModalChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		fireEvent(new WindowModalChangedEvent(this, pOldValue.booleanValue(), pNewValue.booleanValue()));
	}
	
	/**
	 * Invoked if the scene property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSceneChanged(ObservableValue<? extends Scene> pObservable, Scene pOldValue, Scene pNewValue)
	{
		if (pOldValue != null)
		{
			pOldValue.focusOwnerProperty().removeListener(onSceneFocusOwnerChanged);
		}
		
		if (pNewValue != null)
		{
			pNewValue.focusOwnerProperty().addListener(onSceneFocusOwnerChanged);
		}
	}
	
	/**
	 * Invoked if the {@link Scene} focus owner changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSceneFocusOwnerChanged(ObservableValue<? extends Node> pObservable, Node pOldValue, Node pNewValue)
	{
		if (pNewValue != null && NodeUtil.isSameOrChild(this, pNewValue))
		{
			previousFocusOwner = pNewValue;
			
			if (triggerActiveOnFocusChange.get())
			{
				active.set(true);
			}
		}
		else if (pOldValue != null && pNewValue != null)
		{
			if (NodeUtil.isSameOrChild(this, pOldValue))
			{
				previousFocusOwner = pOldValue;
			}
			
			if (triggerActiveOnFocusChange.get())
			{
				active.set(false);
			}
		}
	}
	
	/**
	 * Invoked if the state property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onStateChanged(ObservableValue<? extends State> pObservable, State pOldValue, State pNewValue)
	{
		if (pOldValue != pNewValue)
		{
			// We need to construct the bounds ourselves, because
			// the given bounds are not always up to date.
			Bounds bounds = new BoundingBox(getLayoutX(), getLayoutY(), getWidth(), getHeight());
			
			previousBounds.put(pOldValue, bounds);
			previousState = pOldValue;
			
			pseudoClassStateChanged(MAXIMIZED_PSEUDO_CLASS, pNewValue == State.MAXIMIZED);
			pseudoClassStateChanged(MINIMIZED_PSEUDO_CLASS, pNewValue == State.MINIMIZED);
			
			zoomReset();
			
			fireEvent(new WindowStateChangedEvent(this, pOldValue, pNewValue), onStateChanged);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The state of the {@link FXInternalWindow}.
	 * 
	 * @author Robert Zenz
	 */
	public enum State
	{
		/** The window is maximized. */
		MAXIMIZED,
		
		/** The window is minimized. */
		MINIMIZED,
		
		/** The window is normal. */
		NORMAL
		
	}	// State
	
}	// FXInternalWindow

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

import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IFactory;
import javax.rad.ui.IFont;
import javax.rad.ui.Style;
import javax.rad.ui.event.ComponentHandler;
import javax.rad.ui.event.FocusHandler;
import javax.rad.ui.event.KeyHandler;
import javax.rad.ui.event.MouseHandler;
import javax.rad.ui.event.UIComponentEvent;
import javax.rad.ui.event.UIFocusEvent;
import javax.rad.ui.event.UIKeyEvent;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.event.type.component.IComponentMovedListener;
import javax.rad.ui.event.type.component.IComponentResizedListener;
import javax.rad.ui.event.type.focus.IFocusGainedListener;
import javax.rad.ui.event.type.focus.IFocusLostListener;
import javax.rad.ui.event.type.key.IKeyPressedListener;
import javax.rad.ui.event.type.key.IKeyReleasedListener;
import javax.rad.ui.event.type.key.IKeyTypedListener;
import javax.rad.ui.event.type.mouse.IMouseClickedListener;
import javax.rad.ui.event.type.mouse.IMouseEnteredListener;
import javax.rad.ui.event.type.mouse.IMouseExitedListener;
import javax.rad.ui.event.type.mouse.IMousePressedListener;
import javax.rad.ui.event.type.mouse.IMouseReleasedListener;

import javafx.collections.ObservableList;
import javafx.css.Styleable;
import javafx.scene.control.Labeled;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import com.sibvisions.rad.ui.javafx.impl.JavaFXFactory;
import com.sibvisions.rad.ui.javafx.impl.JavaFXFont;
import com.sibvisions.rad.ui.javafx.impl.JavaFXResource;
import com.sibvisions.rad.ui.javafx.impl.JavaFXStyleContainer;

/**
 * The {@link JavaFXAbstractComponentBase} acts as an abstract base
 * implementation of {@link IComponent} and provides most common functionality
 * and variables.
 * 
 * @author Robert Zenz
 * @param <C> the type of the component.
 * @see IComponent
 */
public abstract class JavaFXAbstractComponentBase<C extends Styleable> extends JavaFXResource<C> implements IComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link IColor} used as background color. */
	protected IColor backgroundColor;
	
	/** The event source. */
	protected IComponent eventSource;
	
	/** The {@link IFactory} this was created from. */
	protected IFactory factory;
	
	/** The {@link IFont} that is used for this component. */
	private IFont font;
	
	/** The {@link IColor} used as foreground color. */
	private IColor foregroundColor;
	
	/** The parent of this component. */
	protected IContainer parent;
	
	/** The {@link Style} for this component. */
	protected Style style;
	
	/** The {@link JavaFXStyleContainer} that is used for this component. */
	protected JavaFXStyleContainer styleContainer;
	
	/**
	 * The {@link ICursor} that is shown if the mouse is over this component.
	 */
	private ICursor cursor;
	
	/** The {@link ComponentHandler} for when the component is moved. */
	private ComponentHandler<IComponentMovedListener> eventComponentMoved;
	
	/** The {@link ComponentHandler} for when the component is resized. */
	private ComponentHandler<IComponentResizedListener> eventComponentResized;
	
	/** The {@link FocusHandler} for when the component receives the focus. */
	private FocusHandler<IFocusGainedListener> eventFocusGained;
	
	/** The {@link FocusHandler} for when the component loses the focus. */
	private FocusHandler<IFocusLostListener> eventFocusLost;
	
	/** The {@link KeyHandler} for when the component receives a key-press. */
	private KeyHandler<IKeyPressedListener> eventKeyPressed;
	
	/** The {@link KeyHandler} for when the component receives a key-release. */
	private KeyHandler<IKeyReleasedListener> eventKeyReleased;
	
	/** The {@link KeyHandler} for when the component receives a key-typed. */
	private KeyHandler<IKeyTypedListener> eventKeyTyped;
	
	/**
	 * The {@link MouseHandler} for when the component receives a mouse-click.
	 */
	private MouseHandler<IMouseClickedListener> eventMouseClicked;
	
	/**
	 * The {@link MouseHandler} for when the component receives a mouse-entered.
	 */
	private MouseHandler<IMouseEnteredListener> eventMouseEntered;
	
	/**
	 * The {@link MouseHandler} for when the component receives a mouse-exited.
	 */
	private MouseHandler<IMouseExitedListener> eventMouseExited;
	
	/**
	 * The {@link MouseHandler} for when the component receives a mouse-pressed.
	 */
	private MouseHandler<IMousePressedListener> eventMousePressed;
	
	/**
	 * The {@link MouseHandler} for when the component receives a
	 * mouse-released.
	 */
	private MouseHandler<IMouseReleasedListener> eventMouseReleased;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractComponentBase}.
	 *
	 * @param pResource the resource.
	 */
	protected JavaFXAbstractComponentBase(C pResource)
	{
		super(pResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the component moved listener to the {@link #resource}.
	 */
	protected abstract void addComponentMovedListener();
	
	/**
	 * Adds the component resized listener to the {@link #resource}.
	 */
	protected abstract void addComponentResizedListener();
	
	/**
	 * Adds the focus listener to the {@link #resource}.
	 */
	protected abstract void addFocusListener();
	
	/**
	 * Adds the key pressed listener to the {@link #resource}.
	 */
	protected abstract void addKeyPressedListener();
	
	/**
	 * Adds the key released listener to the {@link #resource}.
	 */
	protected abstract void addKeyReleasedListener();
	
	/**
	 * Adds the key typed listener to the {@link #resource}.
	 */
	protected abstract void addKeyTypedListener();
	
	/**
	 * Adds the mouse clicked listener to the {@link #resource}.
	 */
	protected abstract void addMouseClickedListener();
	
	/**
	 * Adds the mouse entered listener to the {@link #resource}.
	 */
	protected abstract void addMouseEnteredListener();
	
	/**
	 * Adds the mouse exited listener to the {@link #resource}.
	 */
	protected abstract void addMouseExitedListener();
	
	/**
	 * Adds the mouse pressed listener to the {@link #resource}.
	 */
	protected abstract void addMousePressedListener();
	
	/**
	 * Adds the mouse released listener to the {@link #resource}.
	 */
	protected abstract void addMouseReleasedListener();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentHandler<IComponentMovedListener> eventComponentMoved()
	{
		if (eventComponentMoved == null)
		{
			eventComponentMoved = new ComponentHandler<>(IComponentMovedListener.class);
			
			addComponentMovedListener();
		}
		
		return eventComponentMoved;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentHandler<IComponentResizedListener> eventComponentResized()
	{
		if (eventComponentResized == null)
		{
			eventComponentResized = new ComponentHandler<>(IComponentResizedListener.class);
			
			addComponentResizedListener();
		}
		
		return eventComponentResized;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FocusHandler<IFocusGainedListener> eventFocusGained()
	{
		if (eventFocusGained == null)
		{
			eventFocusGained = new FocusHandler<>(IFocusGainedListener.class);
			
			addFocusListener();
		}
		
		return eventFocusGained;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FocusHandler<IFocusLostListener> eventFocusLost()
	{
		if (eventFocusLost == null)
		{
			eventFocusLost = new FocusHandler<>(IFocusLostListener.class);
			
			addFocusListener();
		}
		
		return eventFocusLost;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeyHandler<IKeyPressedListener> eventKeyPressed()
	{
		if (eventKeyPressed == null)
		{
			eventKeyPressed = new KeyHandler<>(IKeyPressedListener.class);
			
			addKeyPressedListener();
		}
		
		return eventKeyPressed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeyHandler<IKeyReleasedListener> eventKeyReleased()
	{
		if (eventKeyReleased == null)
		{
			eventKeyReleased = new KeyHandler<>(IKeyReleasedListener.class);
			
			addKeyReleasedListener();
		}
		
		return eventKeyReleased;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeyHandler<IKeyTypedListener> eventKeyTyped()
	{
		if (eventKeyTyped == null)
		{
			eventKeyTyped = new KeyHandler<>(IKeyTypedListener.class);
			
			addKeyTypedListener();
		}
		
		return eventKeyTyped;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MouseHandler<IMouseClickedListener> eventMouseClicked()
	{
		if (eventMouseClicked == null)
		{
			eventMouseClicked = new MouseHandler<>(IMouseClickedListener.class);
			
			addMouseClickedListener();
		}
		
		return eventMouseClicked;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MouseHandler<IMouseEnteredListener> eventMouseEntered()
	{
		if (eventMouseEntered == null)
		{
			eventMouseEntered = new MouseHandler<>(IMouseEnteredListener.class);
			
			addMouseEnteredListener();
		}
		
		return eventMouseEntered;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MouseHandler<IMouseExitedListener> eventMouseExited()
	{
		if (eventMouseExited == null)
		{
			eventMouseExited = new MouseHandler<>(IMouseExitedListener.class);
			
			addMouseExitedListener();
		}
		
		return eventMouseExited;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MouseHandler<IMousePressedListener> eventMousePressed()
	{
		if (eventMousePressed == null)
		{
			eventMousePressed = new MouseHandler<>(IMousePressedListener.class);
			
			addMousePressedListener();
		}
		
		return eventMousePressed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MouseHandler<IMouseReleasedListener> eventMouseReleased()
	{
		if (eventMouseReleased == null)
		{
			eventMouseReleased = new MouseHandler<>(IMouseReleasedListener.class);
			
			addMouseReleasedListener();
		}
		
		return eventMouseReleased;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IColor getBackground()
	{
		return backgroundColor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICursor getCursor()
	{
		return cursor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IComponent getEventSource()
	{
		return eventSource;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFactory getFactory()
	{
		return factory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFont getFont()
	{
		if (font == null)
		{
			return new JavaFXFont(Font.getDefault());
		}
		
		return font;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IColor getForeground()
	{
		return foregroundColor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IContainer getParent()
	{
		return parent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Style getStyle()
	{
		if (style == null)
		{
			return new Style();
		}
		
		return style.clone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBackgroundSet()
	{
		return backgroundColor != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCursorSet()
	{
		return cursor != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFontSet()
	{
		return font != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isForegroundSet()
	{
		return foregroundColor != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pBackground)
	{
		backgroundColor = pBackground;
		
		styleContainer.setBackground(backgroundColor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCursor(ICursor pCursor)
	{
		cursor = pCursor;
		
		styleContainer.setCursor(pCursor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEventSource(IComponent pEventSource)
	{
		eventSource = pEventSource;
	}
	
	/**
	 * Sets the factory.
	 *
	 * @param pFactory the new factory.
	 */
	public void setFactory(IFactory pFactory)
	{
		factory = pFactory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(IFont pFont)
	{
		font = pFont;
		
		if (resource instanceof Labeled)
		{
			// If the resource is an instance of Labeled, we'll set the font
			// by using the setFont(Font) method. Otherwise the font might
			// be inherited by children, which f.e. looks odd in the case
			// of a TitledPane.
			if (font != null)
			{
				((Labeled)resource).setFont((Font)font.getResource());
			}
			else
			{
				((Labeled)resource).setFont(null);
			}
		}
		else
		{
			styleContainer.setFont(font);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(IColor pForeground)
	{
		foregroundColor = pForeground;
		
		styleContainer.setForeground(foregroundColor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		if (pParent == null)
		{
			if (parent != null && parent.indexOf(this) >= 0)
			{
				throw new IllegalArgumentException("Can't unset parent, because this component is still added!");
			}
		}
		else
		{
			if (pParent.indexOf(this) < 0)
			{
				throw new IllegalArgumentException("Can't set parent, because this component is not added!");
			}
		}
		
		parent = pParent;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStyle(Style pStyle)
	{
		ObservableList<String> liStyles = resource.getStyleClass();
		
		if (style != null)
		{
			String[] saNames = style.getStyleNames();
			
			for (int i = 0; i < saNames.length; i++)
			{
				liStyles.remove(saNames[i]);
			}
		}
		
		style = pStyle;
		
		if (style != null)
		{
			String[] saNames = style.getStyleNames();
			
			for (int i = 0; i < saNames.length; i++)
			{
				if (!liStyles.contains(saNames[i]))
				{
					liStyles.add(saNames[i]);
				}
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setResource(C pResource)
	{
		super.setResource(pResource);
		
		if (styleContainer == null)
		{
			styleContainer = new JavaFXStyleContainer();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Fires the component moved event.
	 */
	protected void fireComponentMovedEvent()
	{
		if (eventComponentMoved != null)
		{
			UIComponentEvent event = new UIComponentEvent(
					eventSource,
					UIComponentEvent.COMPONENT_MOVED,
					System.currentTimeMillis(),
					0);
					
			eventComponentMoved.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the component resized event.
	 */
	protected void fireComponentResizedEvent()
	{
		if (eventComponentResized != null)
		{
			UIComponentEvent event = new UIComponentEvent(
					eventSource,
					UIComponentEvent.COMPONENT_RESIZED,
					System.currentTimeMillis(),
					0);
					
			eventComponentResized.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the focus gained event.
	 */
	protected void fireFocusGainedEvent()
	{
		if (eventFocusGained != null)
		{
			UIFocusEvent event = new UIFocusEvent(
					eventSource,
					UIFocusEvent.FOCUS_GAINED,
					System.currentTimeMillis(),
					0);
					
			eventFocusGained.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the focus lost event.
	 */
	protected void fireFocusLostEvent()
	{
		if (eventFocusLost != null)
		{
			UIFocusEvent event = new UIFocusEvent(
					eventSource,
					UIFocusEvent.FOCUS_LOST,
					System.currentTimeMillis(),
					0);
					
			eventFocusLost.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the key pressed event.
	 *
	 * @param pKeyEvent the key event.
	 */
	protected void fireKeyPressedEvent(KeyEvent pKeyEvent)
	{
		if (eventKeyPressed != null)
		{
			UIKeyEvent event = JavaFXFactory.getUIKeyEvent(eventSource, UIKeyEvent.KEY_PRESSED, pKeyEvent);
			eventKeyPressed.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the key released event.
	 *
	 * @param pKeyEvent the key event.
	 */
	protected void fireKeyReleasedEvent(KeyEvent pKeyEvent)
	{
		if (eventKeyReleased != null)
		{
			UIKeyEvent event = JavaFXFactory.getUIKeyEvent(eventSource, UIKeyEvent.KEY_RELEASED, pKeyEvent);
			eventKeyReleased.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the key typed event.
	 *
	 * @param pKeyEvent the key event.
	 */
	protected void fireKeyTypedEvent(KeyEvent pKeyEvent)
	{
		if (eventKeyTyped != null)
		{
			UIKeyEvent event = JavaFXFactory.getUIKeyEvent(eventSource, UIKeyEvent.KEY_TYPED, pKeyEvent);
			eventKeyTyped.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the mouse clicked event.
	 *
	 * @param pMouseEvent the mouse event.
	 */
	protected void fireMouseClickedEvent(MouseEvent pMouseEvent)
	{
		if (eventMouseClicked != null)
		{
			UIMouseEvent event = JavaFXFactory.getUIMouseEvent(eventSource, UIMouseEvent.MOUSE_CLICKED, pMouseEvent);
			
			eventMouseClicked.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the mouse entered event.
	 *
	 * @param pMouseEvent the mouse event.
	 */
	protected void fireMouseEnteredEvent(MouseEvent pMouseEvent)
	{
		if (eventMouseEntered != null)
		{
			UIMouseEvent event = JavaFXFactory.getUIMouseEvent(eventSource, UIMouseEvent.MOUSE_ENTERED, pMouseEvent);
			
			eventMouseEntered.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the mouse exited event.
	 *
	 * @param pMouseEvent the mouse event.
	 */
	protected void fireMouseExitedEvent(MouseEvent pMouseEvent)
	{
		if (eventMouseExited != null)
		{
			UIMouseEvent event = JavaFXFactory.getUIMouseEvent(eventSource, UIMouseEvent.MOUSE_EXITED, pMouseEvent);
			
			eventMouseExited.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the mouse pressed event.
	 *
	 * @param pMouseEvent the mouse event.
	 */
	protected void fireMousePressedEvent(MouseEvent pMouseEvent)
	{
		if (eventMousePressed != null)
		{
			UIMouseEvent event = JavaFXFactory.getUIMouseEvent(eventSource, UIMouseEvent.MOUSE_PRESSED, pMouseEvent);
			
			eventMousePressed.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the mouse released event.
	 *
	 * @param pMouseEvent the mouse event.
	 */
	protected void fireMouseReleasedEvent(MouseEvent pMouseEvent)
	{
		if (eventMouseReleased != null)
		{
			UIMouseEvent event = JavaFXFactory.getUIMouseEvent(eventSource, UIMouseEvent.MOUSE_RELEASED, pMouseEvent);
			
			eventMouseReleased.dispatchEvent(event);
		}
	}
	
}	// JavaFXAbstractComponentBase

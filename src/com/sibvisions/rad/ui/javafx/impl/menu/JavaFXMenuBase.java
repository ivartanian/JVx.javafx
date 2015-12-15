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

import javafx.scene.control.MenuItem;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IDimension;
import javax.rad.ui.IImage;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;

import com.sibvisions.rad.ui.javafx.impl.JavaFXStyleContainer;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXAbstractComponentBase;

/**
 * The {@link JavaFXMenuBase} is the base for all menu related components.
 * 
 * @author Robert Zenz
 * @param <C> the component type.
 */
public class JavaFXMenuBase<C extends MenuItem> extends JavaFXAbstractComponentBase<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXMenuBase}.
	 *
	 * @param pResource the resource.
	 */
	public JavaFXMenuBase(C pResource)
	{
		super(pResource);
		
		styleContainer = new JavaFXStyleContainer(pResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not supported.
	 * 
	 * @param iWidth ignored.
	 * @param iHeight ignored.
	 * @return always {@code null}.
	 */
	@Override
	public IImage capture(int iWidth, int iHeight)
	{
		// Not supported.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IRectangle getBounds()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IPoint getLocation()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IPoint getLocationRelativeTo(IComponent pComponent)
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IDimension getMaximumSize()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IDimension getMinimumSize()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return resource.getId();
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
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IDimension getPreferredSize()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public IDimension getSize()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public Integer getTabIndex()
	{
		// Not supported/possible.
		return null;
	}
	
	/**
	 * Not supported. <a
	 * href="https://javafx-jira.kenai.com/browse/RT-27364">RT-37364</a>
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public String getToolTipText()
	{
		// Not supported/possible. RT-37364
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled()
	{
		return !resource.isDisable();
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isFocusable()
	{
		// Not supported/possible.
		return true;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code false}.
	 */
	@Override
	public boolean isMaximumSizeSet()
	{
		// Not supported/possible.
		return false;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code false}.
	 */
	@Override
	public boolean isMinimumSizeSet()
	{
		// Not supported/possible.
		return false;
	}
	
	/**
	 * Not supported.
	 * 
	 * @return always {@code false}.
	 */
	@Override
	public boolean isPreferredSizeSet()
	{
		// Not supported/possible.
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible()
	{
		return resource.isVisible();
	}
	
	/**
	 * Not supported.
	 */
	@Override
	public void requestFocus()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pBounds not used.
	 */
	@Override
	public void setBounds(IRectangle pBounds)
	{
		// Not supported/possible.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnable)
	{
		resource.setDisable(!pEnable);
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pFocusable not used.
	 */
	@Override
	public void setFocusable(boolean pFocusable)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pLocation not used.
	 */
	@Override
	public void setLocation(IPoint pLocation)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pComponent not used.
	 * @param pLocation not used.
	 */
	@Override
	public void setLocationRelativeTo(IComponent pComponent, IPoint pLocation)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pMaximumSize not used.
	 */
	@Override
	public void setMaximumSize(IDimension pMaximumSize)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pMinimumSize not used.
	 */
	@Override
	public void setMinimumSize(IDimension pMinimumSize)
	{
		// Not supported/possible.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String pName)
	{
		resource.setId(pName);
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pPreferredSize not used.
	 */
	@Override
	public void setPreferredSize(IDimension pPreferredSize)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pSize not used.
	 */
	@Override
	public void setSize(IDimension pSize)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 * 
	 * @param pTabIndex not used.
	 */
	@Override
	public void setTabIndex(Integer pTabIndex)
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported. <a
	 * href="https://javafx-jira.kenai.com/browse/RT-27364">RT-37364</a>
	 * 
	 * @param pText not used.
	 */
	@Override
	public void setToolTipText(String pText)
	{
		// Not supported/possible. RT-37364
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		resource.setVisible(pVisible);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addComponentMovedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addComponentResizedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addFocusListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addKeyPressedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addKeyReleasedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addKeyTypedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addMouseClickedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addMouseEnteredListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addMouseExitedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addMousePressedListener()
	{
		// Not supported/possible.
	}
	
	/**
	 * Not supported.
	 */
	@Override
	protected void addMouseReleasedListener()
	{
		// Not supported/possible.
	}
	
}	// JavaFXMenuBase

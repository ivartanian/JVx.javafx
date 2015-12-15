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

import javafx.scene.layout.Pane;

/**
 * The {@link IFXWindowManager} defines a common interface for classes that want
 * to manage {@link FXInternalWindow}s in a {@link FXDesktopPane}.
 * <p>
 * The {@link IFXWindowManager} is assumed to provide a {@link Pane} which will
 * contain all {@link FXInternalWindow}s.
 * 
 * @author Robert Zenz
 */
public interface IFXWindowManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link FXInternalWindow}.
	 * <p>
	 * That means that it should be added to the {@link Pane} provided by this
	 * {@link IFXWindowManager}.
	 * 
	 * @param pWindow the {@link FXInternalWindow} to add.
	 * @see #removeWindow(FXInternalWindow)
	 */
	public void addWindow(FXInternalWindow pWindow);
	
	/**
	 * Invoked if the {@link IFXWindowManager} is to be destroyed. No
	 * {@link FXInternalWindow}s should be closed or disposed in this method.
	 * The {@link IFXWindowManager} is only supposed to clean up its own state,
	 * and that of its {@link Pane}.
	 */
	public void dispose();
	
	/**
	 * The {@link Pane} that will be used for displaying the
	 * {@link FXInternalWindow}s.
	 * <p>
	 * This function might or might not be called multiple times, and should not
	 * have any side-effects when called.
	 * 
	 * @return the {@link Pane}. Can not be {@code null}.
	 */
	public Pane getPane();
	
	/**
	 * Removes the given {@link FXInternalWindow}.
	 * <p>
	 * That means that it should be removed from the {@link Pane}, but not be
	 * closed or disposed.
	 * 
	 * @param pWindow the {@link FXInternalWindow} to remove.
	 * @see #addWindow(FXInternalWindow)
	 */
	public void removeWindow(FXInternalWindow pWindow);
	
	/**
	 * Restores the state.
	 * <p>
	 * The given state is guaranteed to be the state created by
	 * {@link #storeState()}, however it is not guaranteed that this is still
	 * the same {@link IFXWindowManager} instance.
	 * <p>
	 * If there is no state that might need storing, this function can be left
	 * as NOOP.
	 * 
	 * @param pState the state to restore. Might be {@code null}.
	 * @see #storeState()
	 */
	public void restoreState(Object pState);
	
	/**
	 * Stores the state.
	 * <p>
	 * This method gives the {@link IFXWindowManager} the opportunity to store
	 * any relevant information before being removed as an acting window
	 * manager. That information will be given back to the
	 * {@link IFXWindowManager} in {@link #restoreState(Object)} if it is added
	 * again as acting window manager. The created state information must be
	 * instance independent and should not have any references to this instance.
	 * <p>
	 * Note that this state should not contain hard references to the
	 * {@link FXInternalWindow}s that are currently managed, it should only use
	 * weak references, if at all necessary.
	 * <p>
	 * If there is no state that might need storing, this function can be left
	 * as NOOP.
	 * 
	 * @return the internal state. Can be {@code null}.
	 */
	public Object storeState();
	
	/**
	 * Updates all {@link FXInternalWindow}s and marks the currently focused/top
	 * one as active.
	 * 
	 * @see FXInternalWindow#activeProperty()
	 */
	public void updateActiveWindow();
	
}	// IFXWindowManager

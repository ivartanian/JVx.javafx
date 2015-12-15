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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * The {@link IFXZoomable} defines a basic interface for everything that is
 * supposed to be zoomable.
 * 
 * @author Robert Zenz
 */
public interface IFXZoomable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the zoom.
	 * 
	 * @return the zoom.
	 * @see #setZoom(double)
	 * @see #zoomProperty()
	 */
	public abstract double getZoom();
	
	/**
	 * Gets the zoom factor.
	 * 
	 * @return the zoom factor.
	 * @see #setZoomFactor(double)
	 * @see #zoomFactorProperty()
	 */
	public abstract double getZoomFactor();
	
	/**
	 * Gets if the zoom is enabled.
	 * 
	 * @return {@code true} if the zoom is enabled.
	 */
	public abstract boolean isZoomEnabled();
	
	/**
	 * Gets if zooming in is possible.
	 * 
	 * @return {@code true} if zooming in is possible.
	 */
	public abstract boolean isZoomInPossible();
	
	/**
	 * Gets if zooming out is possible.
	 * 
	 * @return {@code true} if zooming out is possible.
	 */
	public abstract boolean isZoomOutPossible();
	
	/**
	 * Gets if resetting the zoom is possible.
	 * 
	 * @return {@code true} if resetting the zoom is possible.
	 */
	public abstract boolean isZoomResetPossible();
	
	/**
	 * Sets the zoom.
	 * 
	 * @param pZoom the zoom.
	 * @see #getZoom()
	 * @see #zoomProperty()
	 */
	public abstract void setZoom(double pZoom);
	
	/**
	 * Sets if the zoom is enabled.
	 * 
	 * @param pZoomEnabled {@code true} if the zoom should be enabled.
	 */
	public abstract void setZoomEnabled(boolean pZoomEnabled);
	
	/**
	 * Sets the zoom factor.
	 * 
	 * @param pZoomFactor the zoom factor.
	 * @see #getZoomFactor()
	 * @see #zoomFactorProperty()
	 */
	public abstract void setZoomFactor(double pZoomFactor);
	
	/**
	 * Zooms in or out the given number of steps. One step equals the set zoom
	 * factor.
	 * 
	 * @param pSteps the steps to zoom in or out.
	 */
	public abstract void zoom(int pSteps);
	
	/**
	 * Gets the property for if the zoom is enabled.
	 * 
	 * @return the property for if the zoom is enabled.
	 */
	public abstract BooleanProperty zoomEnabledProperty();
	
	/**
	 * Gets the property for the zoom factor.
	 * <p>
	 * The zoom factor is the amount how much one step changes the zoom.
	 * 
	 * @return the property for the zoom factor.
	 * @see #getZoomFactor()
	 * @see #setZoomFactor(double)
	 */
	public abstract DoubleProperty zoomFactorProperty();
	
	/**
	 * Zooms in.
	 */
	public abstract void zoomIn();
	
	/**
	 * Zooms in the given number of steps.
	 * 
	 * @param pSteps the number of steps to zoom in.
	 */
	public abstract void zoomIn(int pSteps);
	
	/**
	 * Gets the property for if zooming in is possible.
	 * 
	 * @return the property for if zooming in is possible.
	 */
	public abstract ReadOnlyBooleanProperty zoomInPossibleProperty();
	
	/**
	 * Zooms out.
	 */
	public abstract void zoomOut();
	
	/**
	 * Zooms out the given number of steps.
	 * 
	 * @param pSteps the number of steps to zoom out.
	 */
	public abstract void zoomOut(int pSteps);
	
	/**
	 * Gets the property for if zooming out is possible.
	 * 
	 * @return the property for if zooming out is possible.
	 */
	public abstract ReadOnlyBooleanProperty zoomOutPossibleProperty();
	
	/**
	 * Gets the property for the zoom value.
	 * <p>
	 * The zoom value is the multiplier used for scaling the content {@code 1}
	 * means no scaling, {@code 2} would be double size and {@code 0.5} half
	 * size.
	 * 
	 * @return the property for the zoom value.
	 */
	public abstract DoubleProperty zoomProperty();
	
	/**
	 * Resets the {@link #zoomProperty()} to 1, meaning not zoomed.
	 */
	public abstract void zoomReset();
	
	/**
	 * Gets the property for if resetting the zoom is possible.
	 * 
	 * @return the property for if resetting the zoom is possible.
	 */
	public abstract ReadOnlyBooleanProperty zoomResetPossibleProperty();
	
}	// IFXZoomable

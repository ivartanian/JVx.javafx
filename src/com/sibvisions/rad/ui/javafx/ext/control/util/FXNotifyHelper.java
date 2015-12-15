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
package com.sibvisions.rad.ui.javafx.ext.control.util;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

/**
 * The {@link FXNotifyHelper} is a helper utility which encapsulates most of the
 * boiler-plate code needed for the notifyRepaint support.
 * 
 * @author Robert Zenz
 */
public class FXNotifyHelper
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Runnable} that is the {@link #execute} method. */
	private Runnable execute;
	
	/** The property for it the helper has been notified. */
	private BooleanProperty notified;
	
	/** The {@link Runnable} to execute. */
	private Runnable runnable;
	
	/** The property for if the {@link #runnable} has been scheduled. */
	private BooleanProperty scheduled;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXNotifyHelper}.
	 *
	 * @param pRunnable the {@link Runnable} to execute.
	 */
	public FXNotifyHelper(Runnable pRunnable)
	{
		execute = this::execute;
		
		runnable = pRunnable;
		
		notified = new SimpleBooleanProperty(false);
		notified.addListener(this::onNotifiedChanged);
		
		scheduled = new SimpleBooleanProperty(false);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets if this helper has been notified.
	 * 
	 * @return {@code true} if this helper has been notified.
	 */
	public boolean isNotified()
	{
		return notified.get();
	}
	
	/**
	 * Gets if the action has already been scheduled for execution with
	 * {@link Platform#runLater(Runnable)}.
	 * 
	 * @return {@code true} if the action is scheduled.
	 */
	public boolean isScheduled()
	{
		return scheduled.get();
	}
	
	/**
	 * Gets the property for if this helper has been notified.
	 * 
	 * @return the property for if this helper has been notified.
	 */
	public BooleanProperty notifiedProperty()
	{
		return notified;
	}
	
	/**
	 * Notifies this helper.
	 */
	public void notifyRepaint()
	{
		notified.set(true);
	}
	
	/**
	 * Gets the property for if the action has been scheduled for execution with
	 * {@link Platform#runLater(Runnable)}.
	 * 
	 * @return the property for if the action has been scheduled.
	 */
	public ReadOnlyBooleanProperty scheduledProperty()
	{
		return ReadOnlyBooleanProperty.readOnlyBooleanProperty(scheduled);
	}
	
	/**
	 * Sets if this helper has been notified.
	 * 
	 * @param pNotified {@code true} if this helper has been notified.
	 */
	public void setNotified(boolean pNotified)
	{
		notified.set(pNotified);
	}
	
	/**
	 * Executes the given the {@link #runnable} and resets the internal state.
	 */
	private void execute()
	{
		if (notified.get())
		{
			if (runnable != null)
			{
				try
				{
					runnable.run();
				}
				catch (Throwable t)
				{
					// We've been invoked by the JavaFX Framework, bubbling
					// the exception upwards might corrupt the internal state
					// of the framework in some way or another. Or at least
					// it creates problems in my tests.
					// TODO Should we log the exception somewhere?
				}
			}
		}
		
		scheduled.set(false);
		notified.set(false);
	}
	
	/**
	 * Invoked if the {@link #notified} changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onNotifiedChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue() && !scheduled.get())
		{
			scheduled.set(true);
			Platform.runLater(execute);
		}
	}
	
}	// FXNotifyHelper

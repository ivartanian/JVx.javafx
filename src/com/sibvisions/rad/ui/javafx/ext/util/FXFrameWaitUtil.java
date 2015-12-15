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
package com.sibvisions.rad.ui.javafx.ext.util;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;

/**
 * The {@link FXFrameWaitUtil} is a utility class that allows to wait for a
 * certain amount of frames to pass before a given {@link Runnable} is executed.
 * 
 * @author Robert Zenz
 */
public final class FXFrameWaitUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link ExecutingTimer} instance that is used. */
	private static final ExecutingTimer TIMER;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		TIMER = new ExecutingTimer();
	}
	
	/**
	 * No instance needed, static only.
	 */
	private FXFrameWaitUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Runs the given {@link Runnable} after one frame has passed. The
	 * {@link Runnable} is guaranteed to be executed on the main JavaFX thread.
	 * 
	 * @param pRunnable the {@link Runnable} to execute.
	 */
	public static void runLater(Runnable pRunnable)
	{
		TIMER.queue(pRunnable, 1);
	}
	
	/**
	 * Runs the given {@link Runnable} after two frames have passed. The
	 * {@link Runnable} is guaranteed to be executed on the main JavaFX thread.
	 * 
	 * @param pRunnable the {@link Runnable} to execute.
	 * @param pFramesToSkip the number of frames to skip.
	 */
	public static void runLater(Runnable pRunnable, int pFramesToSkip)
	{
		TIMER.queue(pRunnable, pFramesToSkip);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ExecutingTimer} is an {@link AnimationTimer} extension that
	 * allows to wait for a certain number of frames and to pass and then
	 * execute {@link Runnable}s.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ExecutingTimer extends AnimationTimer
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link List} of {@link Entry Entries}. */
		private List<Entry> entries;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ExecutingTimer}.
		 */
		public ExecutingTimer()
		{
			super();
			
			entries = new ArrayList<>(3);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handle(long pNow)
		{
			for (int index = 0; index < entries.size(); index++)
			{
				Entry entry = entries.get(index);
				
				if (entry.isDue())
				{
					try
					{
						entry.run();
					}
					finally
					{
						// Remove the now run entry.
						entries.remove(index);
						index--;
					}
				}
				else
				{
					entry.skipFrame();
				}
			}
			
			if (entries.isEmpty())
			{
				stop();
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Queues the given {@link Runnable} to be executed after the given
		 * amount of frames has been skipped.
		 * 
		 * @param pRunnable the {@link Runnable} to execute.
		 * @param pFramesToSkip the amount of frames to skip.
		 */
		public void queue(Runnable pRunnable, int pFramesToSkip)
		{
			entries.add(new Entry(pRunnable, pFramesToSkip));
			
			// We always start the timer, does no harm.
			start();
		}
		
		//****************************************************************
		// Subclass definition
		//****************************************************************
		
		/**
		 * An {@link Entry} encapsulates a {@link Runnable} and the counter for
		 * skipping the frames.
		 * 
		 * @author Robert Zenz
		 */
		private static final class Entry
		{
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Class members
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/** The {@link Runnable} to execute. */
			private Runnable runnable;
			
			/** The amount of already skipped frames. */
			private int frameCounter;
			
			/** The amount of frames to skip. */
			private int framesToSkip;
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Initialization
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/**
			 * Creates a new instance of {@link Entry}.
			 *
			 * @param pRunnable the runnable.
			 * @param pFramesToSkip the frames to skip.
			 */
			public Entry(Runnable pRunnable, int pFramesToSkip)
			{
				runnable = pRunnable;
				framesToSkip = pFramesToSkip;
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// User-defined methods
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/**
			 * Gets if the {@link Entry} should be {@link #runnable executed}.
			 * 
			 * @return {@code true} if the {@link Entry} should be
			 *         {@link #runnable executed}.
			 */
			public boolean isDue()
			{
				return frameCounter >= framesToSkip;
			}
			
			/**
			 * Executes this {@link Entry}.
			 */
			public void run()
			{
				runnable.run();
			}
			
			/**
			 * Skips the current frame, incrementing the counter.
			 */
			public void skipFrame()
			{
				frameCounter++;
			}
			
		}	// Entry
		
	}	// ExecutingTimer
	
}	// FXFrameWaitUtil

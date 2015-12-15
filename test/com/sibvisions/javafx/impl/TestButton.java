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
package com.sibvisions.javafx.impl;

import javafx.scene.control.Button;

import javax.rad.genui.component.UIButton;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;

import org.junit.Assert;
import org.junit.Test;

public class TestButton extends FXTestTemplate
{
	
	@Test
	public void testActionEvent()
	{
		UIButton button = new UIButton();
		
		ActionListener listener = new ActionListener();
		
		button.eventAction().addListener(listener);
		
		((Button) button.getResource()).fire();
		
		Assert.assertTrue(listener.hasFired());
	}
	
	private static class ActionListener implements IActionListener
	{
		private boolean fired = false;
		
		@Override
		public void action(UIActionEvent pActionEvent)
		{
			fired = true;
		}
		
		public boolean hasFired()
		{
			return fired;
		}
		
	}
}

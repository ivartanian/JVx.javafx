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

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.rad.genui.UIFactoryManager;

import org.junit.Assert;
import org.junit.BeforeClass;

import com.sibvisions.rad.ui.javafx.impl.JavaFXFactory;

public abstract class FXTestTemplate
{
	@BeforeClass
	public static void setUp()
	{
		if (UIFactoryManager.getFactory() == null)
		{
			UIFactoryManager.getFactoryInstance(JavaFXFactory.class);
			
			// Set the implicit exit to false, to make sure that JavaFX
			// does not exit during testing.
			Platform.setImplicitExit(false);
		}
	}
	
	protected void assertIsLabel(Node pNode, String pText)
	{
		Assert.assertEquals(Label.class, pNode.getClass());
		Assert.assertEquals(pText, ((Label) pNode).getText());
	}
}

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

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.layout.UIBorderLayout;

import org.junit.Test;

public class TestLayouts extends FXTestTemplate
{
	
	@Test
	public void testBorderLayout()
	{
		UIPanel panel = new UIPanel();
		panel.setLayout(new UIBorderLayout());
		
		panel.add(new UILabel("CENTER"), UIBorderLayout.CENTER);
		panel.add(new UILabel("EAST"), UIBorderLayout.EAST);
		panel.add(new UILabel("NORTH"), UIBorderLayout.NORTH);
		panel.add(new UILabel("SOUTH"), UIBorderLayout.SOUTH);
		panel.add(new UILabel("WEST"), UIBorderLayout.WEST);
		
		BorderPane borderPane = (BorderPane) ((Pane) panel.getResource()).getChildren().get(0);
		
		assertIsLabel(borderPane.getCenter(), "CENTER");
		assertIsLabel(borderPane.getRight(), "EAST");
		assertIsLabel(borderPane.getTop(), "NORTH");
		assertIsLabel(borderPane.getBottom(), "SOUTH");
		assertIsLabel(borderPane.getLeft(), "WEST");
	}
}

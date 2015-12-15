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

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIFrame;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.rad.ui.javafx.impl.JavaFXFactory;

public class Main
{
	
	public static void main(String[] args)
	{
		try
		{
			UIFactoryManager.getFactoryInstance(JavaFXFactory.class).invokeAndWait(() -> {
				showFrame();
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void showFrame()
	{
		UIFrame frame = new UIFrame();
		frame.setLayout(new UIBorderLayout());
		frame.setTitle("JavaFX Test");
		
		UILabel label = new UILabel("This is a simple JavaFX test application.");
		frame.add(label, UIBorderLayout.NORTH);
		
		UIGroupPanel group = new UIGroupPanel("This is a group, in case you didn't notice.");
		group.setLayout(new UIBorderLayout());
		
		UIButton button = new UIButton("This button does open the same window again.");
		button.eventAction().addListener((UIActionEvent pEvent) -> {
			showFrame();
		});
		group.add(button, UIBorderLayout.CENTER);
		
		UIGroupPanel flowGroup = new UIGroupPanel("FlowLayoutTest");
		flowGroup.add(new UILabel("A"));
		flowGroup.add(new UILabel("B"));
		flowGroup.add(new UILabel("C"));
		flowGroup.add(new UILabel("D"));
		flowGroup.add(new UILabel("E"));
		flowGroup.add(new UILabel("F"));
		flowGroup.add(new UILabel("G"));
		
		UISplitPanel splitPanel = new UISplitPanel();
		splitPanel.setFirstComponent(new UILabel("You should not see this label"));
		splitPanel.setFirstComponent(new UILabel("First Component"));
		splitPanel.setSecondComponent(new UILabel("Second Component"));
		
		UIGroupPanel splitGroup = new UIGroupPanel("Below is a split panel.");
		splitGroup.setLayout(new UIBorderLayout());
		splitGroup.add(splitPanel, UIBorderLayout.CENTER);
		
		UITabsetPanel tabset = new UITabsetPanel();
		tabset.add(new UILabel("Tab A Content"), "Tab A");
		tabset.add(new UILabel("Tab B Content"), "Tab B");
		tabset.add(new UILabel("Tab C Content"), "Tab C");
		tabset.add(new UILabel("Tab D Content"), "Tab D");
		
		UIPanel centerPanel = new UIPanel();
		centerPanel.setLayout(new UIBorderLayout());
		centerPanel.add(new UILabel("North"), UIBorderLayout.NORTH);
		centerPanel.add(tabset, UIBorderLayout.EAST);
		centerPanel.add(splitGroup, UIBorderLayout.SOUTH);
		centerPanel.add(flowGroup, UIBorderLayout.WEST);
		centerPanel.add(group, UIBorderLayout.CENTER);
		frame.add(centerPanel, UIBorderLayout.EAST);
		
		frame.add(new UILabel("Bottom"), UIBorderLayout.SOUTH);
		frame.add(new UILabel("Left"), UIBorderLayout.WEST);
		
		frame.pack();
		frame.setVisible(true);
		
	}
}

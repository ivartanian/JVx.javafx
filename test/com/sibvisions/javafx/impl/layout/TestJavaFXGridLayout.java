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
package com.sibvisions.javafx.impl.layout;

import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IGridLayout.IGridConstraints;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.javafx.impl.FXTestTemplate;
import com.sibvisions.rad.ui.javafx.impl.JavaFXInsets;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXPanel;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXGridLayout;

public class TestJavaFXGridLayout extends FXTestTemplate
{
	
	@Test
	public void testAddWithConstraints()
	{
		JavaFXGridLayout layout = new JavaFXGridLayout(2, 7);
		
		JavaFXPanel panel = new JavaFXPanel();
		panel.setLayout(layout);
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		UILabel label = new UILabel("Test");
		IGridConstraints constraints = layout.getConstraints(1, 1);
		
		panel.add(label, constraints);
		Assert.assertEquals(panel.getComponentCount(), 1);
		
		Assert.assertSame(constraints, layout.getConstraints(label));
	}
	
	@Test
	public void testAddWithNoConstraints()
	{
		JavaFXGridLayout layout = new JavaFXGridLayout(2, 7);
		JavaFXPanel panel = new JavaFXPanel();
		panel.setLayout(layout);
		
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		UILabel label = new UILabel("Test");
		
		panel.add(label);
		
		Assert.assertEquals(panel.getComponentCount(), 1);
		
		IGridConstraints constraints = layout.getConstraints(label);
		Assert.assertNotNull(constraints);
		Assert.assertEquals(0, constraints.getGridX());
		Assert.assertEquals(0, constraints.getGridY());
		
		UIButton button = new UIButton("Test");
		
		panel.add(button);
		
		Assert.assertEquals(panel.getComponentCount(), 2);
		
		constraints = layout.getConstraints(button);
		Assert.assertNotNull(constraints);
		Assert.assertEquals(1, constraints.getGridX());
		Assert.assertEquals(0, constraints.getGridY());
		
		button = new UIButton("Test 2");
		
		panel.add(button);
		
		Assert.assertEquals(panel.getComponentCount(), 3);
		
		constraints = layout.getConstraints(button);
		Assert.assertNotNull(constraints);
		Assert.assertEquals(0, constraints.getGridX());
		Assert.assertEquals(1, constraints.getGridY());
	}
	
	@Test
	public void testColumnRowProperties()
	{
		JavaFXGridLayout layout = new JavaFXGridLayout(5, 7);
		Assert.assertEquals("Constructor does not set correct columns value!", 5, layout.getColumns());
		Assert.assertEquals("Constructor does not set correct rows value!", 7, layout.getRows());
		
		layout.setColumns(3);
		Assert.assertEquals("setColumns(int) does not set correct columns value!", 3, layout.getColumns());
		
		layout.setRows(2);
		Assert.assertEquals("setRow(int) does not set correct rows value!", 2, layout.getRows());
	}
	
	@Test
	public void testConstraints()
	{
		IInsets insets = new JavaFXInsets(0, 0, 0, 0);
		IGridConstraints constraints = new JavaFXGridLayout.JavaFXGridConstraints(9, 9, 9, 9, insets);
		
		Assert.assertEquals(9, constraints.getGridX());
		Assert.assertEquals(9, constraints.getGridY());
		Assert.assertEquals(9, constraints.getGridWidth());
		Assert.assertEquals(9, constraints.getGridHeight());
		Assert.assertSame(insets, constraints.getInsets());
		
		constraints.setGridX(5);
		Assert.assertEquals(5, constraints.getGridX());
		Assert.assertEquals(9, constraints.getGridY());
		Assert.assertEquals(9, constraints.getGridWidth());
		Assert.assertEquals(9, constraints.getGridHeight());
		
		constraints.setGridY(6);
		Assert.assertEquals(5, constraints.getGridX());
		Assert.assertEquals(6, constraints.getGridY());
		Assert.assertEquals(9, constraints.getGridWidth());
		Assert.assertEquals(9, constraints.getGridHeight());
		
		constraints.setGridWidth(7);
		Assert.assertEquals(5, constraints.getGridX());
		Assert.assertEquals(6, constraints.getGridY());
		Assert.assertEquals(7, constraints.getGridWidth());
		Assert.assertEquals(9, constraints.getGridHeight());
		
		constraints.setGridHeight(8);
		Assert.assertEquals(5, constraints.getGridX());
		Assert.assertEquals(6, constraints.getGridY());
		Assert.assertEquals(7, constraints.getGridWidth());
		Assert.assertEquals(8, constraints.getGridHeight());
		
		IInsets newInsets = new JavaFXInsets(1, 1, 1, 1);
		constraints.setInsets(newInsets);
		Assert.assertSame(newInsets, constraints.getInsets());
	}
	
	@Test
	public void testRemoveAllComponents()
	{
		JavaFXGridLayout layout = new JavaFXGridLayout(2, 7);
		
		JavaFXPanel panel = new JavaFXPanel();
		panel.setLayout(layout);
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		UILabel label = new UILabel("Test");
		UILabel label2 = new UILabel("Test 2");
		
		panel.add(label);
		panel.add(label2);
		Assert.assertEquals(panel.getComponentCount(), 2);
		
		panel.removeAll();
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		Assert.assertNull(layout.getConstraints(label));
		Assert.assertNull(layout.getConstraints(label2));
	}
	
	@Test
	public void testRemoveSpecificComponent()
	{
		JavaFXGridLayout layout = new JavaFXGridLayout(2, 7);
		
		JavaFXPanel panel = new JavaFXPanel();
		panel.setLayout(layout);
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		UILabel label = new UILabel("Test");
		
		panel.add(label);
		Assert.assertEquals(panel.getComponentCount(), 1);
		
		panel.remove(label);
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		Assert.assertNull(layout.getConstraints(label));
	}
	
	@Test
	public void testSetConstraints()
	{
		JavaFXGridLayout layout = new JavaFXGridLayout(2, 7);
		
		JavaFXPanel panel = new JavaFXPanel();
		panel.setLayout(layout);
		Assert.assertEquals(panel.getComponentCount(), 0);
		
		UILabel label = new UILabel("Test");
		
		panel.add(label);
		Assert.assertEquals(panel.getComponentCount(), 1);
		
		IGridConstraints constraints = layout.getConstraints(1, 1);
		
		layout.setConstraints(label, constraints);
		
		Assert.assertSame(constraints, layout.getConstraints(label));
	}
	
}

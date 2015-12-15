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
package com.sibvisions.javafx.impl;

import javafx.scene.Cursor;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.ui.javafx.ext.StyleContainer;

public class TestStyleContainer
{
	private Region region;
	private StyleContainer style;
	
	@Before
	public void setup()
	{
		region = new Region();
		style = new StyleContainer(region);
	}
	
	@Test
	public void testClear()
	{
		style.clear("test-property");
		Assert.assertEquals("", region.getStyle());
		
		style.set("test-property", "value");
		Assert.assertEquals("test-property: value;\n", region.getStyle());
		
		style.clear("test-property");
		Assert.assertEquals("", region.getStyle());
	}
	
	@Test
	public void testSetBackground()
	{
		style.setBackground(Color.AQUA);
		
		Assert.assertEquals("-fx-background-color: #00FFFF;\n", style.toString());
	}
	
	@Test
	public void testSetCursor()
	{
		style.setCursor(Cursor.CLOSED_HAND);
		
		Assert.assertEquals("-fx-cursor: closed-hand;\n", style.toString());
	}
	
	@Test
	public void testSetForeground()
	{
		style.setForeground(Color.AQUA);
		
		Assert.assertEquals("-fx-text-fill: #00FFFF;\n", style.toString());
	}
	
	@Test
	public void setFont()
	{
		style.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 25.6));
		
		Assert.assertEquals("-fx-font-size: 25.60px;\n-fx-font-style: italic;\n-fx-font-family: \"System\";\n-fx-font-weight: bold;\n", style.toString());
	}
	
	@Test
	public void testSetQuoted()
	{
		style.set("test-property", "value");
		style.set("test-property2", "value2");
		Assert.assertTrue(region.getStyle().contains("test-property: value;"));
		Assert.assertTrue(region.getStyle().contains("test-property2: value2;"));
	}
	
	@Test
	public void testSetStringString()
	{
		style.set("test-property", "value");
		style.setQuoted("test-property2", "value2");
		Assert.assertTrue(region.getStyle().contains("test-property: value;"));
		Assert.assertTrue(region.getStyle().contains("test-property2: \"value2\";"));
	}
	
	@Test
	public void testSetStringStringObject()
	{
		Object firstObject = new Object();
		Object secondObject = new Object();
		
		style.set("test-property", "value", firstObject);
		style.set("test-property2", "value2", secondObject);
		Assert.assertTrue(region.getStyle().contains("test-property: value;"));
		Assert.assertTrue(region.getStyle().contains("test-property2: value2;"));
		Assert.assertSame(firstObject, style.getObject("test-property"));
		Assert.assertSame(secondObject, style.getObject("test-property2"));
	}
	
}

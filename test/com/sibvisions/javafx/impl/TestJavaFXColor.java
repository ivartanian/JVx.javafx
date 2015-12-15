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

import javafx.scene.paint.Color;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.ui.javafx.impl.JavaFXColor;

/**
 * Tests {@link JavaFXColor}.
 * 
 * @author Robert Zenz
 */
public class TestJavaFXColor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the {@link JavaFXColor#JavaFXColor(Color)} constructor.
	 */
	@Test
	public void testJavaFXColorColor()
	{
		Color color = new Color(0.2, 0.3, 0.4, 0.5);
		JavaFXColor javafxColor = new JavaFXColor(color);
		
		Assert.assertSame(color, javafxColor.getResource());
		Assert.assertEquals("Red component did not match.", 51, javafxColor.getRed());
		Assert.assertEquals("Green component did not match.", 77, javafxColor.getGreen());
		Assert.assertEquals("Blue component did not match.", 102, javafxColor.getBlue());
		Assert.assertEquals("Alpha component did not match.", 128, javafxColor.getAlpha());
	}
	
	/**
	 * Tests the {@link JavaFXColor#JavaFXColor(int)} constructor.
	 */
	@Test
	public void testJavaFXColorInt()
	{
		JavaFXColor javafxColor = new JavaFXColor(128 << 24 | 51 << 16 | 77 << 8 | 102);
		
		Assert.assertEquals(51, javafxColor.getRed());
		Assert.assertEquals(77, javafxColor.getGreen());
		Assert.assertEquals(102, javafxColor.getBlue());
		Assert.assertEquals(128, javafxColor.getAlpha());
		
		Color color = (Color) javafxColor.getResource();
		Assert.assertEquals("Red component did not match.", 0.2, color.getRed(), 0.01);
		Assert.assertEquals("Green component did not match.", 0.3, color.getGreen(), 0.01);
		Assert.assertEquals("Blue component did not match.", 0.4, color.getBlue(), 0.01);
		Assert.assertEquals("Alpha component did not match.", 0.5, color.getOpacity(), 0.01);
	}
	
}	// TestJavaFXColor

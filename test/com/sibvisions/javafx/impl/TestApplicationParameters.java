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

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.ui.javafx.impl.ApplicationParameters;

/**
 * Tests the {@link ApplicationParameters} class.
 * 
 * @author Robert Zenz
 */
public class TestApplicationParameters
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the {@link ApplicationParameters#parse(String[])} method with only
	 * the application name provided.
	 */
	@Test
	public void testParseApplicationName()
	{
		ApplicationParameters parameters = ApplicationParameters.parse(new String[] { "ApplicationName" });
		
		Assert.assertEquals("ApplicationName", parameters.getApplicationClassName());
		Assert.assertEquals("application.xml", parameters.getConfiguration());
		Assert.assertTrue(parameters.getParameters().isEmpty());
	}
	
	/**
	 * Tests the {@link ApplicationParameters#parse(String[])} method with
	 * exactly the application name and configuration filename provided.
	 */
	@Test
	public void testParseApplicationNameConfigXml()
	{
		ApplicationParameters parameters = ApplicationParameters.parse(new String[] { "ApplicationName", "myfile.xml" });
		
		Assert.assertEquals("ApplicationName", parameters.getApplicationClassName());
		Assert.assertEquals("myfile.xml", parameters.getConfiguration());
		Assert.assertTrue(parameters.getParameters().isEmpty());
	}
	
	/**
	 * Tests the {@link ApplicationParameters#parse(String[])} method with the
	 * application name and configuration filename provided and additional
	 * parameters.
	 */
	@Test
	public void testParseApplicationNameConfigXmlAdditional()
	{
		ApplicationParameters parameters = ApplicationParameters.parse(new String[] { "ApplicationName", "myfile.xml", "a=1", "b", "c=3" });
		
		Assert.assertEquals("ApplicationName", parameters.getApplicationClassName());
		Assert.assertEquals("myfile.xml", parameters.getConfiguration());
		Assert.assertFalse(parameters.getParameters().isEmpty());
		
		Assert.assertEquals("1", parameters.getParameter("a"));
		// Only parameters that are a key=value pair are accepted.
		Assert.assertNull(parameters.getParameter("b"));
		Assert.assertEquals("3", parameters.getParameter("c"));
	}
	
	/**
	 * Tests the {@link ApplicationParameters#parse(String[])} method with no
	 * parameters.
	 */
	@Test
	public void testParseExceptionWithNoParameters()
	{
		try
		{
			ApplicationParameters.parse(null);
			
			Assert.fail("ApplicationParameters.parse(null) did not throw an exception!");
		}
		catch (Exception e)
		{
			// Ignore the exception, this is expected behavior.
		}
		
		try
		{
			ApplicationParameters.parse(new String[] {});
			
			Assert.fail("ApplicationParameters.parse(new String[] {}) did not throw an exception!");
		}
		catch (Exception e)
		{
			// Ignore the exception, this is expected behavior.
		}
	}
	
	/**
	 * Tests the {@link ApplicationParameters#parse(String[])} method that it
	 * strips quotes from the parameters.
	 */
	@Test
	public void testParseQuoteStrip()
	{
		
		ApplicationParameters parameters = ApplicationParameters.parse(new String[] { "\"Quoted\"", "\"quotedmyfile.xml\"" });
		
		Assert.assertEquals("Quoted", parameters.getApplicationClassName());
		Assert.assertEquals("quotedmyfile.xml", parameters.getConfiguration());
		Assert.assertTrue(parameters.getParameters().isEmpty());
	}
	
}	// TestApplicationParameters

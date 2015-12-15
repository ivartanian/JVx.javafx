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
package com.sibvisions.rad.ui.javafx.impl.focus;

import java.util.Comparator;

import javafx.scene.Node;

/**
 * A {@link Comparator} implementation that compares {@link Node}s based on
 * their set tab index.
 * 
 * @author Robert Zenz
 */
public class JavaFXNodeByTabIndexComparator implements Comparator<Node>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXNodeByTabIndexComparator}.
	 */
	public JavaFXNodeByTabIndexComparator()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Node pO1, Node pO2)
	{
		if (pO1 == pO2)
		{
			return 0;
		}
		
		Integer tabIndex1 = JavaFXFocusUtil.getTabIndex(pO1);
		Integer tabIndex2 = JavaFXFocusUtil.getTabIndex(pO2);
		
		if (tabIndex1 != null && tabIndex2 != null)
		{
			return tabIndex1.compareTo(tabIndex2);
		}
		else if (tabIndex1 != null && tabIndex2 == null)
		{
			return -1;
		}
		else if (tabIndex1 == null && tabIndex2 != null)
		{
			return 1;
		}
		
		return 0;
	}
	
}	// JavaFXNodeByTabIndexComparator

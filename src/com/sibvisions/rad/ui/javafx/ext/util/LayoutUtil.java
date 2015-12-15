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

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Region;

import com.sibvisions.rad.ui.javafx.ext.IFXContentAlignable;
import com.sibvisions.rad.ui.javafx.ext.IFXLayoutForwarding;

/**
 * THe {@link LayoutUtil} is a simple static layout utility.
 * 
 * @author Robert Zenz
 */
public final class LayoutUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instances needed.
	 */
	private LayoutUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Aligns the given node according to the given settings relative to the
	 * anchor node.
	 * 
	 * @param pAnchorNode the node that is used for alignment.
	 * @param pNodeToAlign the node that is going to be aligned.
	 * @param pContentDisplay the content display to use.
	 * @param pHorizontalAlignment the horizontal alignment to use.
	 * @param pVerticalAlignment the vertical alignment to use.
	 */
	public static void alignRelativeToContent(Node pAnchorNode, Node pNodeToAlign, ContentDisplay pContentDisplay, HPos pHorizontalAlignment, VPos pVerticalAlignment)
	{
		if (pAnchorNode == null || pNodeToAlign == null)
		{
			return;
		}
		
		if (pContentDisplay == ContentDisplay.CENTER || pContentDisplay == ContentDisplay.BOTTOM || pContentDisplay == ContentDisplay.TOP)
		{
			if (pHorizontalAlignment == HPos.LEFT)
			{
				pNodeToAlign.setLayoutX(pAnchorNode.getLayoutX());
			}
			else if (pHorizontalAlignment == HPos.RIGHT)
			{
				pNodeToAlign.setLayoutX(pAnchorNode.getLayoutX() + pAnchorNode.prefWidth(-1) - pNodeToAlign.getLayoutBounds().getWidth());
			}
		}
		if (pContentDisplay == ContentDisplay.CENTER || pContentDisplay == ContentDisplay.LEFT || pContentDisplay == ContentDisplay.RIGHT)
		{
			if (pVerticalAlignment == VPos.TOP)
			{
				pNodeToAlign.setLayoutY(pAnchorNode.getLayoutY() - pNodeToAlign.getLayoutBounds().getMinY());
			}
			else if (pVerticalAlignment == VPos.BOTTOM)
			{
				pNodeToAlign.setLayoutY(pAnchorNode.getLayoutY() - pNodeToAlign.getLayoutBounds().getMinY() + pAnchorNode.prefHeight(-1)
						- pNodeToAlign.getLayoutBounds().getHeight());
			}
		}
	}
	
	/**
	 * 
	 * Computes the preferred height of the given container based on the content
	 * display.
	 * <p>
	 * This is a basically just an intermediate function which should have been
	 * in a base class. But this is used with an already existing class
	 * hierarchy, so it's not possible to directly inject it.
	 * 
	 * @param pContainer the container.
	 * @param pContentAlignable the content alignable.
	 * @param pLayoutForwarding the forwarded layout.
	 * @param pContentSelector the content selector.
	 * @return the double.
	 */
	public static double computeContentPreferredHeight(Region pContainer, IFXContentAlignable pContentAlignable, IFXLayoutForwarding pLayoutForwarding, String pContentSelector)
	{
		String styleClassName = pContainer.getStyleClass().get(0);
		
		if (pContentAlignable.getContentDisplay() == ContentDisplay.TOP || pContentAlignable.getContentDisplay() == ContentDisplay.BOTTOM
				|| pContentAlignable.getContentDisplay() == ContentDisplay.CENTER)
		{
			Node imageView = pContainer.lookup("." + styleClassName + " *.image-view");
			
			if (imageView == null)
			{
				Node text = pContainer.lookup("." + styleClassName + " *.text");
				Node content = pContainer.lookup("." + styleClassName + " *." + pContentSelector);
				
				if (pContentAlignable.getContentDisplay() == ContentDisplay.CENTER)
				{
					return Math.max(text.prefHeight(-1), content.prefHeight(-1));
				}
				else
				{
					return text.prefHeight(-1) + content.prefHeight(-1);
				}
			}
		}
		
		return pLayoutForwarding.calculatePreferredHeight(-1);
	}
	
	/**
	 * Computes the preferred width of the given container based on the content
	 * display.
	 * <p>
	 * This is a basically just an intermediate function which should have been
	 * in a base class. But this is used with an already existing class
	 * hierarchy, so it's not possible to directly inject it.
	 *
	 * @param pContainer the container.
	 * @param pContentAlignable the content alignable.
	 * @param pLayoutForwarding the forwarded layout.
	 * @param pContentSelector the content selector.
	 * @return the double.
	 */
	public static double computeContentPreferredWidth(Region pContainer, IFXContentAlignable pContentAlignable, IFXLayoutForwarding pLayoutForwarding, String pContentSelector)
	{
		String styleClassName = pContainer.getStyleClass().get(0);
		
		if (pContentAlignable.getContentDisplay() == ContentDisplay.TOP || pContentAlignable.getContentDisplay() == ContentDisplay.BOTTOM
				|| pContentAlignable.getContentDisplay() == ContentDisplay.CENTER)
		{
			Node imageView = pContainer.lookup("." + styleClassName + " *.image-view");
			
			if (imageView == null)
			{
				Node text = pContainer.lookup("." + styleClassName + " *.text");
				Node content = pContainer.lookup("." + styleClassName + " *." + pContentSelector);
				
				return Math.max(text.prefWidth(-1), content.prefWidth(-1));
			}
		}
		
		return pLayoutForwarding.calculatePreferredWidth(-1);
	}
	
	/**
	 * Layouts a text node relative to a content node or an image view node.
	 * <p>
	 * This is a basically just an intermediate function which should have been
	 * in a base class. But this is used with an already existing class
	 * hierarchy, so it's not possible to directly inject it.
	 *
	 * @param pContainer the container.
	 * @param pContentAlignable the content alignable.
	 * @param pLayoutForwarding the forwarded layout.
	 * @param pContentSelector the content selector.
	 */
	public static void layoutContent(Region pContainer, IFXContentAlignable pContentAlignable, IFXLayoutForwarding pLayoutForwarding, String pContentSelector)
	{
		String styleClassName = pContainer.getStyleClass().get(0);
		
		Node text = NodeUtil.lookup(pContainer, "." + styleClassName + " *.text");
		Node imageView = NodeUtil.lookup(pContainer, "." + styleClassName + " *.image-view");
		
		if (imageView != null)
		{
			pLayoutForwarding.doLayout();
			
			LayoutUtil.alignRelativeToContent(imageView, text, pContentAlignable.getContentDisplay(), pContentAlignable.getRelativeHorizontalTextAlignment(),
					pContentAlignable.getRelativeVerticalTextAlignment());
		}
		else if (text != null && pContentSelector != null)
		{
			Node content = pContainer.lookup("." + styleClassName + " *." + pContentSelector);
			
			double width = computeContentPreferredWidth(pContainer, pContentAlignable, pLayoutForwarding, pContentSelector);
			double height = computeContentPreferredHeight(pContainer, pContentAlignable, pLayoutForwarding, pContentSelector);
			
			double modY = (pContainer.getHeight() - height) / 2;
			
			content.autosize();
			text.autosize();
			
			if (pContentAlignable.getContentDisplay() == ContentDisplay.TOP)
			{
				content.relocate((width - content.prefWidth(-1)) / 2, modY);
				text.relocate((width - text.prefWidth(-1)) / 2, content.prefHeight(-1) + modY);
			}
			else if (pContentAlignable.getContentDisplay() == ContentDisplay.BOTTOM)
			{
				content.relocate((width - content.prefWidth(-1)) / 2, text.prefHeight(-1) + modY);
				text.relocate((width - text.prefWidth(-1)) / 2, modY);
			}
			else if (pContentAlignable.getContentDisplay() == ContentDisplay.CENTER)
			{
				content.relocate((width - content.prefWidth(-1)) / 2, (height - content.prefHeight(-1)) / 2 + modY);
				text.relocate((width - text.prefWidth(-1)) / 2, (height - text.prefHeight(-1)) / 2 + modY);
			}
			else if (pContentAlignable.getContentDisplay() == ContentDisplay.RIGHT)
			{
				content.relocate(width - content.prefWidth(-1), modY);
				text.relocate(0, modY);
			}
			else
			{
				pLayoutForwarding.doLayout();
			}
			
			LayoutUtil.alignRelativeToContent(content, text, pContentAlignable.getContentDisplay(), pContentAlignable.getRelativeHorizontalTextAlignment(),
					pContentAlignable.getRelativeVerticalTextAlignment());
		}
		else
		{
			pLayoutForwarding.doLayout();
		}
	}
	
}	// LayoutUtil

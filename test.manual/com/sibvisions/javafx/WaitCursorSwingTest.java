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
package com.sibvisions.javafx;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class WaitCursorSwingTest extends JFrame
{
	public static void main(String[] args)
	{
		new WaitCursorSwingTest();
	}

	public WaitCursorSwingTest()
	{
		BorderLayout blMain = new BorderLayout();
		
		JButton butWait = new JButton("Show WAIT cursor");
		butWait.addActionListener(l -> 
		{
			getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			getGlassPane().setVisible(true);
			
			try
			{
				Thread.sleep(5000);
			}
			catch (Exception ex)
			{
				//ignore
			}
			finally
			{
				getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				getGlassPane().setVisible(false);
			}
		});

		FlowLayout flCenter = new FlowLayout();

		JPanel panCenter = new JPanel(flCenter);
		panCenter.add(butWait);
		
		setLayout(blMain);
		add(panCenter);
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}

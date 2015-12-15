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
package com.sibvisions.rad.ui.javafx.ext.control;

import java.math.BigDecimal;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.ui.javafx.ext.control.table.FXDataBookView;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXDataBookViewTestMain extends Application
{
	private static final String[] NAMES = { "Alfred", "Otto", "Kim", "Herbert", "Matthias", "Judas", "Cthulhu", "Nora", "Squall", "Peter", "David" };
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		IDataBook dataBook = createDataBook();
		
		FXDataBookView view = new FXDataBookView();
		view.setDataBook(dataBook);
		
		Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction((pActionEvent) ->
		{
			view.notifyRepaint();
		});
		
		Button insertButton = new Button("Insert row");
		insertButton.setOnAction((pActionEvent) ->
		{
			try
			{
				dataBook.insert(false);
				
				dataBook.setValue("ID", BigDecimal.valueOf(dataBook.getRowCount()));
				dataBook.setValue("NAME", NAMES[dataBook.getRowCount() % NAMES.length]);
				
				dataBook.saveAllRows();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		Button deleteButton = new Button("Delete row");
		deleteButton.setOnAction((pActionEvent) ->
		{
			try
			{
				dataBook.delete();
				
				dataBook.saveAllRows();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		Button updateNameButton = new Button("Update name");
		updateNameButton.setOnAction((pActionEvent) ->
		{
			try
			{
				if (dataBook.getSelectedRow() >= 0)
				{
					dataBook.setValue("NAME", NAMES[dataBook.getRowCount() % NAMES.length]);
					
					dataBook.saveAllRows();
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(7);
		controlPane.addChild(refreshButton);
		controlPane.addChild(insertButton);
		controlPane.addChild(deleteButton);
		controlPane.addChild(updateNameButton);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(view);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX DataBookView Test");
		primaryStage.show();
		
		insertButton.fire();
		insertButton.fire();
		insertButton.fire();
	}
	
	private IDataBook createDataBook()
	{
		try
		{
			IDataBook dataBook = new MemDataBook();
			dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
			
			dataBook.setName("TEST");
			dataBook.open();
			
			return dataBook;
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

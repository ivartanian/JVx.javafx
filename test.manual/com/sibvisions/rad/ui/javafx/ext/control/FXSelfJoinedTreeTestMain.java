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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.rad.model.IDataBook;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.MasterConnection;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.remote.http.HttpConnection;
import com.sibvisions.rad.ui.javafx.ext.control.tree.FXDataBooksTree;

public class FXSelfJoinedTreeTestMain extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		IDataBook dataBook = createDataBook();
		
		Label label = new Label("Folders");
		
		FXDataBooksTree tree = new FXDataBooksTree();
		tree.getDataBooks().addAll(dataBook);
		
		BorderPane root = new BorderPane();
		root.setTop(label);
		root.setCenter(tree);
		BorderPane.setMargin(label, new Insets(5, 5, 10, 5));
		BorderPane.setMargin(tree, new Insets(5, 5, 5, 5));
		
		primaryStage.setOnCloseRequest((e) -> 
		{
			Platform.exit();
			System.exit(0);
		});
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX Self-joined Tree Test");
		primaryStage.setHeight(600);
		primaryStage.setWidth(700);
		primaryStage.show();
	}
	
	private IDataBook createDataBook()
	{
		try
		{
			HttpConnection htpc = new HttpConnection("http://localhost:8080/Applications.Server/services/Server");
			
			MasterConnection macon = new MasterConnection(htpc);
			macon.setLifeCycleName("com.sibvisions.apps.showcase.frames.Storages");
			macon.setApplicationName("showcase");
			macon.setUserName("admin");
			macon.setPassword("admin");
			macon.open();
			
			RemoteDataSource rds = new RemoteDataSource(macon);
			rds.open();
	
			RemoteDataBook rdbData = new RemoteDataBook();
			rdbData.setDataSource(rds);
			rdbData.setName("folders");
			rdbData.setMasterReference(new ReferenceDefinition(new String[] {"FOLD_ID"},
															   rdbData,
															   new String[] {"ID"}));
			rdbData.open();
			
			return rdbData;
		}
		catch (Throwable th)
		{
			th.printStackTrace();
		}

		return null;
	}
	
}

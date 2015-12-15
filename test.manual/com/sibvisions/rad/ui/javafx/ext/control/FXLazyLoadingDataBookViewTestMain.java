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

import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.rad.model.ModelException;
import javax.rad.remote.MasterConnection;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.remote.http.HttpConnection;
import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.rad.ui.javafx.ext.control.table.FXDataBookView;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.scene.StyledScene;

public class FXLazyLoadingDataBookViewTestMain extends Application
{
	private MemDataBook dataBook;
	
	private Label lblInfo;
	
	private long lEstimated = -1;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void stop() throws Exception
	{
		super.stop();
		
		Platform.exit();
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		dataBook = createDataBook();
		
		FXDataBookView view = new FXDataBookView();
		view.setDataBook(dataBook);
		
		lblInfo = new Label();
		StyleContainer sclbl = new StyleContainer(lblInfo);
		sclbl.set("-fx-font-weight", "bold");
		sclbl.set("-fx-font-size", "20px");
		
		updateInfo();
		
		((ObservableDataBookList)view.itemsProperty().get()).addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable) 
			{
				updateInfo();
			}
		});
		
		Button reloadButton = new Button("Reload");
		reloadButton.setOnAction((pActionEvent) ->
		{
			try
			{
				dataBook.reload();
				
				lEstimated = -1;
				
				updateInfo();
			}
			catch (ModelException me)
			{
				me.printStackTrace();
			}
		});
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(4);
		controlPane.addChild(reloadButton);
		controlPane.addChild(lblInfo, controlPane.createConstraint(-1, 0));
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(view);
		
		StyleContainer scroot = new StyleContainer(root);
		scroot.set("-fx-background-color", "whitesmoke");
		
		new StyledScene(primaryStage, root);

		primaryStage.setOnCloseRequest((e) -> 
		{
			Platform.exit();
			System.exit(0);
		});
		
		primaryStage.setTitle("JavaFX LazyLoading Test");
		primaryStage.setHeight(600);
		primaryStage.setWidth(700);
		primaryStage.show();
	}
	
	private MemDataBook createDataBook()
	{
		try
		{
			HttpConnection htpc = new HttpConnection("http://localhost/Applications.Server/services/Server");
			htpc.setRetryCount(0);
			
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
			rdbData.setName("files");
			rdbData.open();
			
			return rdbData;
		}
		catch (Throwable th)
		{
			th.printStackTrace();
		}

		return null;
	}
	
	private void updateInfo()
	{
		try
		{
			if (lEstimated < 0)
			{
				lEstimated = dataBook.getEstimatedRowCount();
			}
			
			DecimalFormat df = new DecimalFormat("#,###");
			
			lblInfo.setText("Record count = " + df.format(dataBook.getRowCount()) +" / " + df.format(lEstimated));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

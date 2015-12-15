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
package com.sibvisions.rad.ui.javafx.ext;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXImageRegionTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXImageRegion imageRegion = new FXImageRegion(new Image("http://upload.wikimedia.org/wikipedia/commons/9/90/Colors-i54-ring.png"));
		
		ComboBox<HPos> hPosBox = new ComboBox<>();
		hPosBox.getItems().addAll(HPos.LEFT, HPos.CENTER, HPos.RIGHT);
		hPosBox.setValue(imageRegion.getHorizontalAlignment());
		imageRegion.horizontalAlignmentProperty().bind(hPosBox.valueProperty());
		
		ComboBox<VPos> vPosBox = new ComboBox<>();
		vPosBox.getItems().addAll(VPos.TOP, VPos.CENTER, VPos.BOTTOM, VPos.BASELINE);
		vPosBox.setValue(imageRegion.getVerticalAlignment());
		imageRegion.verticalAlignmentProperty().bind(vPosBox.valueProperty());
		
		CheckBox horizontalStretchBox = new CheckBox("Horizontal Stretch");
		horizontalStretchBox.setSelected(imageRegion.isHorizontalStretch());
		imageRegion.horizontalStretchedProperty().bind(horizontalStretchBox.selectedProperty());
		
		CheckBox verticalStretchBox = new CheckBox("Vertical Stretch");
		verticalStretchBox.setSelected(imageRegion.isVerticalStretched());
		imageRegion.verticalStretchedProperty().bind(verticalStretchBox.selectedProperty());
		
		CheckBox preserveRatioBox = new CheckBox("Preserve Ratio");
		preserveRatioBox.setSelected(imageRegion.isPreserveRatio());
		imageRegion.preserveRatioProperty().bind(preserveRatioBox.selectedProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(4);
		controlPane.getChildren().add(new Label("Horizontal Position"));
		controlPane.getChildren().add(hPosBox);
		controlPane.getChildren().add(new Label("Vertical Position"));
		controlPane.getChildren().add(vPosBox);
		controlPane.getChildren().add(horizontalStretchBox);
		controlPane.getChildren().add(verticalStretchBox);
		controlPane.getChildren().add(preserveRatioBox);
		
		Pane spacerLeft = new Pane();
		spacerLeft.setMinWidth(100);
		spacerLeft.setStyle("-fx-background-color: #FF0000;");
		
		Pane spacerBottom = new Pane();
		spacerBottom.setMinHeight(100);
		spacerBottom.setStyle("-fx-background-color: #FF0000;");
		
		Pane spacerRight = new Pane();
		spacerRight.setMinWidth(100);
		spacerRight.setStyle("-fx-background-color: #FF0000;");
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setLeft(spacerLeft);
		root.setBottom(spacerBottom);
		root.setRight(spacerRight);
		root.setCenter(imageRegion);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		BorderPane.setAlignment(root.getLeft(), Pos.CENTER);
		BorderPane.setAlignment(root.getBottom(), Pos.CENTER);
		BorderPane.setAlignment(root.getRight(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX ImageRegion Test");
		primaryStage.show();
	}
}

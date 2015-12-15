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
package com.sibvisions.javafx.panes;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FlowPaneTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FlowPane flowPane = new FlowPane();
		flowPane.setStyle("-fx-background-color: #FFBBBB;");
		
		FlowPane fixedFlowPane = new FlowPane();
		fixedFlowPane.setStyle("-fx-background-color: #BBBBFF;");
		
		// Needed so that the flow pane can resize itself.
		VBox containerBox = new VBox();
		containerBox.setFillWidth(false);
		containerBox.getChildren().add(flowPane);
		containerBox.getChildren().add(new Label("This is a label to show you that\nthe FlowPane does overlap\nother elements."));
		
		ComboBox<Orientation> orientationBox = new ComboBox<>();
		orientationBox.getItems().addAll(Orientation.values());
		orientationBox.setValue(flowPane.getOrientation());
		flowPane.orientationProperty().bind(orientationBox.valueProperty());
		fixedFlowPane.orientationProperty().bind(orientationBox.valueProperty());
		
		ComboBox<Pos> alignmentBox = new ComboBox<>();
		alignmentBox.getItems().addAll(Pos.values());
		alignmentBox.setValue(flowPane.getAlignment());
		flowPane.alignmentProperty().bind(alignmentBox.valueProperty());
		fixedFlowPane.alignmentProperty().bind(alignmentBox.valueProperty());
		
		Button addButton = new Button("Add component");
		addButton.setOnAction((pActionEvent) ->
		{
			addComponent(flowPane);
			addComponent(fixedFlowPane);
		});
		
		Button removeButton = new Button("Remove last component");
		removeButton.setOnAction((pActionEvent) ->
		{
			if (!flowPane.getChildren().isEmpty())
			{
				flowPane.getChildren().remove(flowPane.getChildren().size() - 1);
			}
			if (!fixedFlowPane.getChildren().isEmpty())
			{
				fixedFlowPane.getChildren().remove(fixedFlowPane.getChildren().size() - 1);
			}
		});
		
		Slider hGapSlider = new Slider(0, 100, flowPane.getHgap());
		flowPane.hgapProperty().bind(hGapSlider.valueProperty());
		fixedFlowPane.hgapProperty().bind(hGapSlider.valueProperty());
		
		Slider vGapSlider = new Slider(0, 100, flowPane.getVgap());
		flowPane.vgapProperty().bind(vGapSlider.valueProperty());
		fixedFlowPane.vgapProperty().bind(vGapSlider.valueProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(5);
		controlPane.getChildren().add(new Label("Orientation"));
		controlPane.getChildren().add(orientationBox);
		controlPane.getChildren().add(new Label("Alignment"));
		controlPane.getChildren().add(alignmentBox);
		controlPane.getChildren().add(addButton);
		controlPane.addChildInNewLine(new Label("Horizontal Gap"));
		controlPane.getChildren().add(hGapSlider);
		controlPane.getChildren().add(new Label("Vertical Gap"));
		controlPane.getChildren().add(vGapSlider);
		controlPane.getChildren().add(removeButton);
		
		SplitPane splitPane = new SplitPane();
		splitPane.getItems().add(containerBox);
		splitPane.getItems().add(fixedFlowPane);
		
		BorderPane root = new BorderPane();
		containerBox.setPrefSize(800, 600);
		root.setTop(controlPane);
		root.setCenter(splitPane);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FlowPane Test");
		primaryStage.show();
		
		addButton.fire();
		addButton.fire();
		addButton.fire();
		addButton.fire();
	}
	
	private void addComponent(Pane pPane)
	{
		int type = pPane.getChildren().size() % 3;
		
		switch (type)
		{
			case 0:
				Label label = new Label("Label " + Integer.toString(pPane.getChildren().size()));
				label.setMinSize(32, 32);
				label.setStyle("-fx-background-color: #0099FF;");
				pPane.getChildren().add(label);
				break;
			
			case 1:
				Button button = new Button("Button " + Integer.toString(pPane.getChildren().size()));
				button.setPrefSize(64, 64);
				pPane.getChildren().add(button);
				break;
			
			case 2:
				CheckBox checkBox = new CheckBox("Some CheckBox " + Integer.toString(pPane.getChildren().size()));
				checkBox.setStyle("-fx-background-color: #99FF00;");
				pPane.getChildren().add(checkBox);
				break;
		
		}
	}
}

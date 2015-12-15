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
package com.sibvisions.rad.ui.javafx.ext.scene;

import javax.rad.util.TranslationMap;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The {@link StyledScene} extends the {@link Scene} and replaces the custom
 * frame with a custom one.
 * 
 * @author René Jahn
 * @see StackedScenePane
 */
public class StyledScene extends Scene
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the stage. */
	private Stage stage;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>StyledScene</code> for given stage and
	 * root node.
	 * 
	 * @param pStage the stage
	 * @param pRoot the root node
	 */
	public StyledScene(Stage pStage, Parent pRoot)
	{
		this(pStage, pRoot, -1, -1);
	}
	
	/**
	 * Creates a new instance of <code>StyledScene</code>.
	 * 
	 * @param pStage the stage
	 * @param pRoot the root node
	 * @param pWidth the initial width
	 * @param pHeight the initial height
	 */
	public StyledScene(Stage pStage, Parent pRoot, double pWidth, double pHeight)
	{
		super(new StackedScenePane(pStage, pRoot), pWidth, pHeight);
		
		stage = pStage;
		
		getStylesheets().add(getClass().getResource("/com/sibvisions/rad/ui/javafx/ext/scene/css/styledscene.css").toExternalForm());
		
		stage.setScene(this);
		
		stage.initStyle(StageStyle.TRANSPARENT);
		setFill(Color.TRANSPARENT);
		
		((StackedScenePane)getRoot()).initScene();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	protected Stage getStage()
	{
		return stage;
	}
	
	/**
	 * Gets the translation property.
	 * 
	 * @return the property for the translation
	 */
	public SimpleObjectProperty<TranslationMap> translationProperty()
	{
		return ((StackedScenePane)getRoot()).translationProperty();
	}
	
}	// StyledScene

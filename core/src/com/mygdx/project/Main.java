package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;


public class Main extends ApplicationAdapter {
	//Used to draw the panels
	SpriteBatch batch;
	//used to draw the MBComponents
	static Stage stage;
	Panel panel;
	//individual panels
	Panel sidePanel;
	Panel topPanel;
	Panel genStatsPanel;
	Panel reminderPanel;
	Panel toolbarPanel;
	Panel masterboardPanel;
	MBTextArea reminderTextArea;

	@Override
	public void create () {
		//setting up batch and stage
		batch = new SpriteBatch();
		stage = new Stage();
		//setting up panels
		sidePanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\SidecardPanel.png",
				new Rectangle(2, 150, 98, 850));
		topPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\TopbarPanel.png",
				new Rectangle(110, 950, 780, 50));
		genStatsPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\GenstatsPanel.png",
				new Rectangle(110, 550, 780, 390));
		reminderPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\ReminderPanel.png",
				new Rectangle(110, 150, 780, 390));
		toolbarPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\ToolbarPanel.png",
				new Rectangle(2, 2, 1916, 138));
		masterboardPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\MasterboardPanel.png",
				new Rectangle(900, 150, 1018, 850));
		//setting up skin for the UI of the app
		Skin uiSkin = new Skin (Gdx.files.internal(
				"C:\\Users\\ak2000\\Documents\\Mine\\MBRemastered\\MBRemastered\\MBRemastered\\assets\\skins\\uiskin.json"));

		//region Reminders
		//creating a textarea
		reminderTextArea = new MBTextArea("", uiSkin);
		reminderTextArea.textArea.setSize(760,330);
		reminderTextArea.textArea.setPosition(120,160);
		//creating a label
		MBLabel reminderLabel = new MBLabel("REMINDERS", uiSkin);
		reminderLabel.label.setSize(760, 40);
		reminderLabel.label.setPosition(120, 490);
		//adding to the Reminders panel as its components
		reminderPanel.add(reminderTextArea);
		reminderPanel.add(reminderLabel);
		reminderTextArea.setBorder(batch);
		//endregion

		//region General Stats
		MBLabel label2 = new MBLabel("idk", uiSkin);
//		ScrollPane pane = new ScrollPane(label2, uiSkin);
		label2.label.setSize(760, 200);
		label2.label.setPosition(120, 560);

		genStatsPanel.add(label2);
//		stage.addActor(pane);


		//endregion


		//honestly don't know what this does, but it's essential
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		//clearing the board before drawing ??
		ScreenUtils.clear(new Color(0xe0e0e0ff));
		//drawing the panels
		batch.begin();
		sidePanel.render(batch);
		topPanel.render(batch);
		genStatsPanel.render(batch);
		reminderPanel.render(batch);
		toolbarPanel.render(batch);
		masterboardPanel.render(batch);
		reminderTextArea.setBorder(batch);
		//drawing the components after so that they are on the top
		stage.draw();
		stage.act();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		panel.dispose();
	}
}

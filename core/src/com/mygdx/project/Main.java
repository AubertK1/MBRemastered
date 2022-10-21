package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.math.Rectangle;

import java.awt.*;


public class Main extends ApplicationAdapter {
	//Used to draw the panels
	SpriteBatch batch;
	//used to draw the MBComponents
	static Stage stage;
	//individual panels
	Panel sidePanel, topPanel, genStatsPanel, reminderPanel, toolbarPanel, masterboardPanel;
	MBTextArea reminderTextArea;


	@Override
	public void create () {
		//setting up batch and stage
		batch = new SpriteBatch();
		stage = new Stage();
		//setting up panels
		sidePanel = new Panel("core\\pics\\SidecardPanel.png",
				new Rectangle(2, 150, 98, 850));
		topPanel = new Panel("core\\pics\\TopbarPanel.png",
				new Rectangle(110, 950, 780, 50));
		genStatsPanel = new Panel("core\\pics\\GenstatsPanel.png",
				new Rectangle(110, 550, 780, 390));
		reminderPanel = new Panel("core\\pics\\ReminderPanel.png",
				new Rectangle(110, 150, 780, 390));
		toolbarPanel = new Panel("core\\pics\\ToolbarPanel.png",
				new Rectangle(2, 2, 1916, 138));
		masterboardPanel = new Panel("core\\pics\\MasterboardPanel.png",
				new Rectangle(900, 150, 1018, 850));
		//setting up skin for the UI of the app
		Skin uiSkin = new Skin (Gdx.files.internal(
				"assets\\skins\\uiskin.json"));

		//region Reminders
		//creating a textarea
		reminderTextArea = new MBTextArea("", uiSkin);
		reminderTextArea.textArea.setSize(760,330);
		reminderTextArea.textArea.setPosition(120,160);
		//creating a label
		MBLabel reminderLabel = new MBLabel("REMINDERS", uiSkin);
		reminderLabel.setSize(760, 40);
		reminderLabel.setPosition(120, 490);
		//adding to the Reminders panel as its components
		reminderPanel.add(reminderTextArea);
		reminderPanel.add(reminderLabel);
//		reminderTextArea.setBorder(batch);
		//endregion

		//region General Stats
		//region stats
		//creating all the minipanels to hold the player stats
		Minipanel strPanel, dexPanel, conPanel, intPanel, wisPanel, chaPanel;
		strPanel = new Minipanel("core\\pics\\minipanel2.png",
				new Rectangle(120, 870, 50, 60));
		dexPanel = new Minipanel("core\\pics\\minipanel2.png",
				new Rectangle(180, 870, 50, 60));
		conPanel = new Minipanel("core\\pics\\minipanel2.png",
				new Rectangle(240, 870, 50, 60));
		intPanel = new Minipanel("core\\pics\\minipanel2.png",
				new Rectangle(300, 870, 50, 60));
		wisPanel = new Minipanel("core\\pics\\minipanel2.png",
				new Rectangle(360, 870, 50, 60));
		chaPanel = new Minipanel("core\\pics\\minipanel2.png",
				new Rectangle(420, 870, 50, 60));
		//creating the labels to put in the stats' minipanels
		MBLabel strL = new MBLabel("STR", uiSkin);
		//setting position equal to its minipanel's left border + half the minipanel's width - half the label's width
		strL.setPosition(strPanel.getX() + (strPanel.getWidth()/2) - (strL.getWidth()/2), 903);
		MBLabel dexL = new MBLabel("DEX", uiSkin);
		dexL.setPosition(dexPanel.getX() + (dexPanel.getWidth()/2) - (dexL.getWidth()/2), 903);
		MBLabel conL = new MBLabel("CON", uiSkin);
		conL.setPosition(conPanel.getX() + (conPanel.getWidth()/2) - (conL.getWidth()/2), 903);
		MBLabel intL = new MBLabel("INT", uiSkin);
		intL.setPosition(intPanel.getX() + (intPanel.getWidth()/2) - (intL.getWidth()/2), 903);
		MBLabel wisL = new MBLabel("WIS", uiSkin);
		wisL.setPosition(wisPanel.getX() + (wisPanel.getWidth()/2) - (wisL.getWidth()/2), 903);
		MBLabel chaL = new MBLabel("CHA", uiSkin);
		chaL.setPosition(chaPanel.getX() + (chaPanel.getWidth()/2) - (chaL.getWidth()/2), 903);
		//creating the textfields to put in the stats' minipanels
		MBTextField strTF = new MBTextField("", uiSkin);
		//size and positions set by eyeballing until it looked nice
		strTF.setSize(44, 35);
		strTF.setPosition(123, 873);
		strTF.textField.setAlignment(Align.center);
		MBTextField dexTF = new MBTextField("", uiSkin);
		dexTF.setSize(44, 35);
		dexTF.setPosition(183, 873);
		dexTF.textField.setAlignment(Align.center);
		MBTextField conTF = new MBTextField("", uiSkin);
		conTF.setSize(44, 35);
		conTF.setPosition(243, 873);
		conTF.textField.setAlignment(Align.center);
		MBTextField intTF = new MBTextField("", uiSkin);
		intTF.setSize(44, 35);
		intTF.setPosition(303, 873);
		intTF.textField.setAlignment(Align.center);
		MBTextField wisTF = new MBTextField("", uiSkin);
		wisTF.setSize(44, 35);
		wisTF.setPosition(363, 873);
		wisTF.textField.setAlignment(Align.center);
		MBTextField chaTF = new MBTextField("", uiSkin);
		chaTF.setSize(44, 35);
		chaTF.setPosition(423, 873);
		chaTF.textField.setAlignment(Align.center);

		//adding minipanels to the panel
		genStatsPanel.add(strPanel);
		genStatsPanel.add(dexPanel);
		genStatsPanel.add(conPanel);
		genStatsPanel.add(intPanel);
		genStatsPanel.add(wisPanel);
		genStatsPanel.add(chaPanel);
		//adding components to their minipanels
		strPanel.add(strTF);
		strPanel.add(strL);
		dexPanel.add(dexTF);
		dexPanel.add(dexL);
		conPanel.add(conTF);
		conPanel.add(conL);
		intPanel.add(intTF);
		intPanel.add(intL);
		wisPanel.add(wisTF);
		wisPanel.add(wisL);
		chaPanel.add(chaTF);
		chaPanel.add(chaL);
		//endregion

		Minipanel listPanel = new Minipanel("core\\pics\\GenstatsPanel.png",
				new Rectangle(120, 560, 470, 300));
		genStatsPanel.add(listPanel);

		Item item1 = new Item("Item 1", 0);
		listPanel.add(item1);

		Item item2 = new Item("Item 2", 1);
		listPanel.add(item2);

		Item item3 = new Item("Item 3", 2);
		listPanel.add(item3);
		Item item4 = new Item("Item 4", 3);
		listPanel.add(item4);
		Item item5 = new Item("Item 5", 4);
		listPanel.add(item5);
		Item item6 = new Item("Item 6", 5);
		listPanel.add(item6);

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
//		reminderTextArea.setBorder(batch);
		//drawing the components after so that they are on the top
		stage.draw();
		stage.act();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

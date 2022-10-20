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
		reminderLabel.label.setSize(760, 40);
		reminderLabel.label.setPosition(120, 490);
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
		strL.label.setPosition(strPanel.position.x + (strPanel.position.width/2) - (strL.label.getWidth()/2), 903);
		MBLabel dexL = new MBLabel("DEX", uiSkin);
		dexL.label.setPosition(dexPanel.position.x + (dexPanel.position.width/2) - (dexL.label.getWidth()/2), 903);
		MBLabel conL = new MBLabel("CON", uiSkin);
		conL.label.setPosition(conPanel.position.x + (conPanel.position.width/2) - (conL.label.getWidth()/2), 903);
		MBLabel intL = new MBLabel("INT", uiSkin);
		intL.label.setPosition(intPanel.position.x + (intPanel.position.width/2) - (intL.label.getWidth()/2), 903);
		MBLabel wisL = new MBLabel("WIS", uiSkin);
		wisL.label.setPosition(wisPanel.position.x + (wisPanel.position.width/2) - (wisL.label.getWidth()/2), 903);
		MBLabel chaL = new MBLabel("CHA", uiSkin);
		chaL.label.setPosition(chaPanel.position.x + (chaPanel.position.width/2) - (chaL.label.getWidth()/2), 903);
		//creating the textfields to put in the stats' minipanels
		MBTextField strTF = new MBTextField("", uiSkin);
		//size and positions set by eyeballing until it looked nice
		strTF.textField.setSize(44, 35);
		strTF.textField.setPosition(123, 873);
		strTF.textField.setAlignment(Align.center);
		MBTextField dexTF = new MBTextField("", uiSkin);
		dexTF.textField.setSize(44, 35);
		dexTF.textField.setPosition(183, 873);
		dexTF.textField.setAlignment(Align.center);
		MBTextField conTF = new MBTextField("", uiSkin);
		conTF.textField.setSize(44, 35);
		conTF.textField.setPosition(243, 873);
		conTF.textField.setAlignment(Align.center);
		MBTextField intTF = new MBTextField("", uiSkin);
		intTF.textField.setSize(44, 35);
		intTF.textField.setPosition(303, 873);
		intTF.textField.setAlignment(Align.center);
		MBTextField wisTF = new MBTextField("", uiSkin);
		wisTF.textField.setSize(44, 35);
		wisTF.textField.setPosition(363, 873);
		wisTF.textField.setAlignment(Align.center);
		MBTextField chaTF = new MBTextField("", uiSkin);
		chaTF.textField.setSize(44, 35);
		chaTF.textField.setPosition(423, 873);
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

		//fixme temporary items...sizes set arbitrarily
		Minipanel item1 = new Minipanel("core\\pics\\TopbarPanel.png",
				new Rectangle(listPanel.position.x+5, (listPanel.position.height + listPanel.position.y - 50), listPanel.position.width-10, 40));
		listPanel.add(item1);

		MBLabel item1L = new MBLabel("Item 1", uiSkin);
//		item1L.label.setSize(1000,50);
		item1L.label.setPosition(item1.position.x+5, item1.position.y+5);
//		item1L.setPosition(new Rectangle(item1.position.x+5, item1.position.y+5, 100,0));
		MBTextField item1TF = new MBTextField("", uiSkin);
		item1TF.textField.setPosition(200, item1.position.getY()+4);
		item1TF.textField.setSize(340, item1TF.textField.getHeight());
		MBButton button1 = new MBButton(uiSkin);
		button1.button.setPosition((item1TF.textField.getX()+item1TF.textField.getWidth()+2), item1TF.textField.getY());
		button1.button.setSize(40, 32);
		button1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Button Pressed");
			}
		});

		item1.add(item1L);
		item1.add(item1TF);
		item1.add(button1);

		//fixme temporary items...sizes set arbitrarily
		Minipanel item2 = new Minipanel("core\\pics\\TopbarPanel.png",
				new Rectangle(listPanel.position.x+5, (listPanel.position.height + listPanel.position.y - 95), listPanel.position.width-10, 40));
		listPanel.add(item2);

		MBLabel item2L = new MBLabel("Item 2", uiSkin);
//		item1L.label.setSize(1000,50);
		item2L.label.setPosition(item2.position.x+5, item2.position.y+5);
//		item1L.setPosition(new Rectangle(item1.position.x+5, item1.position.y+5, 100,0));
		MBTextField item2TF = new MBTextField("", uiSkin);
		item2TF.textField.setPosition(200, item2.position.getY()+4);
		item2TF.textField.setSize(340, item2TF.textField.getHeight());
		item2.add(item2L);
		item2.add(item2TF);
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

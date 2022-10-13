package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;


public class Main extends ApplicationAdapter {
	SpriteBatch batch;

	static Stage stage;
	Panel panel;
	Panel sidePanel;
	Panel topPanel;
	Panel genStatsPanel;
	Panel descriptionPanel;
	Panel toolbarPanel;
	Panel masterboardPanel;
	int arm;
	BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage();

		sidePanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\SidecardPanel.png",
				new Rectangle(2, 150, 98, 850));
		topPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\TopbarPanel.png",
				new Rectangle(110, 950, 780, 50));
		genStatsPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\GenstatsPanel.png",
				new Rectangle(110, 550, 780, 390));
		descriptionPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\DescriptionPanel.png",
				new Rectangle(110, 150, 780, 390));
		toolbarPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\ToolbarPanel.png",
				new Rectangle(2, 2, 1916, 138));
		masterboardPanel = new Panel("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\MasterboardPanel.png",
				new Rectangle(900, 150, 1018, 850));
		Skin uiSkin = new Skin (Gdx.files.internal(
				"C:\\Users\\ak2000\\Documents\\Mine\\MBRemastered\\MBRemastered\\MBRemastered\\assets\\skins\\uiskin.json"));

		MBTextField descriptionTextField = new MBTextField("test", uiSkin);
		descriptionTextField.textArea.setSize(760,370);
//		descriptionTextField.textField.setSize(760,370);
//		descriptionTextField.textField.setPosition(120,160);
		descriptionTextField.textArea.setPosition(120,160);
		descriptionPanel.add(descriptionTextField);
//		stage.addActor(descriptionTextField.textField);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		ScreenUtils.clear(new Color(0xe0e0e0ff));
		batch.begin();
		sidePanel.render(batch);
		topPanel.render(batch);
		genStatsPanel.render(batch);
		descriptionPanel.render(batch);
		toolbarPanel.render(batch);
		masterboardPanel.render(batch);

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

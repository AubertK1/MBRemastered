package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture panel;
	Texture sidePanel;
	Texture topPanel;
	Texture genStatsPanel;
	Texture descriptionPanel;
	Texture toolbarPanel;
	Texture masterboardPanel;
	int arm;

	@Override
	public void create () {
		batch = new SpriteBatch();
		panel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\Panel2.png");
		sidePanel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\SidecardPanel.png");
		topPanel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\TopbarPanel.png");
		genStatsPanel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\GenstatsPanel.png");
		descriptionPanel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\DescriptionPanel.png");
		toolbarPanel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\ToolbarPanel.png");
		masterboardPanel = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
				"\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\MasterboardPanel.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(new Color(0xe0e0e0ff));
		batch.begin();
		//screen is 1920x1000 so adjust accordingly
		batch.draw(sidePanel, 2, 150, 98, 850);
		batch.draw(topPanel, 110, 950, 780, 50);
		batch.draw(genStatsPanel, 110, 550, 780, 390);
		batch.draw(descriptionPanel, 110, 150, 780, 390);
		batch.draw(toolbarPanel, 2, 2, 1916, 138);
		batch.draw(masterboardPanel, 900, 150, 1018, 850);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		panel.dispose();
	}
}

package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture panel;
	int arm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		panel = new Texture("C:\\Users\\ak2000\\Documents\\Mine\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\Panel2.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(new Color(0xe0e0e0ff));
		batch.begin();
		//screen is 1920x1000 so adjust accordingly
		batch.draw(panel, 0, 900, 100, 700);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		panel.dispose();
	}
}

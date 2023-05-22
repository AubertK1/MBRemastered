package com.mygdx.project;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.math.Rectangle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class Main extends ApplicationAdapter {
	//Used to draw the panels
	static SpriteBatch batch;
	//used to draw the MBComponents
	static Stage stage;
	//the skin for the components
	static Skin skin;
	private static MainScreen mainScreen;
	public static MBContextMenu contextMenu;

	//region used for the Upload Image button
	//these are initialized from the start so that when they're used time isn't wasted while loading them
	static String fileChooserPath;
	static final JFileChooser chooser = new JFileChooser();
	static final FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"JPG, PNG & GIF Images", "jpg", "gif", "png");
	static final JFrame f = new JFrame();
	//endregion

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage();
		skin = new Skin (Gdx.files.internal("assets\\skins\\uiskin.json"));

		//sets up the file structures
		try {
			Files.createDirectories(Paths.get("assets\\SaveFiles\\stats"));
			Files.createDirectories(Paths.get("assets\\SaveFiles\\ovalues"));
			Files.createDirectories(Paths.get("assets\\SaveFiles\\pixvalues"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		mainScreen = new MainScreen();
		//initializes the first screen if there is none
		mainScreen.addScreen(new PlayerScreen("PLAYER 1"));

		contextMenu = new MBContextMenu();

		//region listeners
		//the screen listeners
		InputMultiplexer multiplexer = new InputMultiplexer();
		InputProcessor screenProcessor = new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(button == Input.Buttons.RIGHT && !contextMenu.isCustomized()) {
					contextMenu.buildDefaultMenu();
					contextMenu.showAt(screenX, Gdx.graphics.getHeight()-screenY);
				}
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(float amountX, float amountY) {
				return false;
			}
		};
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(screenProcessor);
		Gdx.input.setInputProcessor(multiplexer);
		//endregion
	}

	@Override
	public void render () {
		//clears the board before drawing
		ScreenUtils.clear(new Color(0x747474ff));
		//begins the batch to draw everything
		batch.begin();

		//renders mainScreen which handles all the other rendering for the screens
		mainScreen.render();

		//renders contextMenu on click
		if(contextMenu.isActive()) contextMenu.render();
		else{
			//banish it offscreen when it's not active, so it isn't blocking anything's listener
			contextMenu.setPosition(-100, -100);
			contextMenu.setSize(contextMenu.getWidth(), 1);
		}

		batch.end();

		//allows for buttons to change on hover
		stage.act();
	}

	@Override
	public void dispose () {
		batch.dispose();
		mainScreen.dispose();
		skin.dispose();
		stage.dispose();
		chooser.setEnabled(false);
		f.dispose();
	}

	public static MainScreen getMainScreen(){
		return mainScreen;
	}
}

package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.math.Rectangle;
import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;


public class Main extends ApplicationAdapter {
	//Used to draw the panels
	static SpriteBatch batch;
	//used to draw the MBComponents
	static Stage stage;
	//the skin for the components
	static Skin uiSkin;

	public static MBContextMenu<String> contextMenu;
	//creating main panels
	Panel sidePanel, topPanel, genStatsPanel, reminderPanel, toolbarPanel, masterboardPanel;
	MBBoard masterBoard;
	//list with all the MBComponents
	static ArrayList<MBComponent> allComps = new ArrayList<>();

	static String player;
	//so these can be drawn last
    static ArrayList<Tipbox> tipboxes = new ArrayList<>();
    static ArrayList<MBWindow> windows = new ArrayList<>();
    static ArrayList<MBSelectBox> scrollpanes = new ArrayList<>();
	//weapons or spell items for the itempanel
	static int itemTab = 1;

	//these are initialized from the start so that when they're used time isn't wasted while loading them
	static String path;
	static final JFileChooser chooser = new JFileChooser();
	static final FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"JPG, PNG & GIF Images", "jpg", "gif", "png");
	static final JFrame f = new JFrame();

	@Override
	public void create () {
//        player = "PLAYER 1";
		//setting up batch and stage
		batch = new SpriteBatch();
		stage = new Stage();
		//setting up panels
		sidePanel = new Panel("core\\pics\\MBSkin2\\SidecardPanel.png",
				new Rectangle(2, 150, 98, 850));
		topPanel = new Panel("core\\pics\\MBSkin2\\TopbarPanel.png",
				new Rectangle(110, 950, 780, 50));
		genStatsPanel = new Panel("core\\pics\\MBSkin2\\GenstatsPanel.png",
				new Rectangle(110, 550, 780, 390));
		reminderPanel = new Panel("core\\pics\\MBSkin2\\ReminderPanel.png",
				new Rectangle(110, 150, 780, 390));
		toolbarPanel = new Panel("core\\pics\\MBSkin2\\ToolbarPanel.png",
				new Rectangle(2, 2, 1916, 138));
		masterboardPanel = new Panel("core\\pics\\MBSkin2\\MasterboardPanel4.png",
				new Rectangle(900, 150, 1018, 850));
		//setting up skin for the UI of the app
		uiSkin = new Skin (Gdx.files.internal(
				"assets\\skins\\uiskin.json"));

		contextMenu = new MBContextMenu<>();

		sidePanel.setSoftVisible(true);
		topPanel.setSoftVisible(true);
		genStatsPanel.setSoftVisible(true);

		toolbarPanel.setSoftVisible(true);
		reminderPanel.setSoftVisible(true);
		masterboardPanel.setSoftVisible(true);

		//region Reminders
		//creating a textarea
		MBTextArea reminderTextArea;
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
		//region listpanel
		//creating a list panel to hold all the items and adding it to the genstats panel
		final Minipanel listPanel = new Minipanel("core\\pics\\MBSkin2\\ListPanel.png",
				new Rectangle(120, 560, 470, 300));
		genStatsPanel.add(listPanel);

		//creating item tab buttons
		final MBButton weaponsButton = new MBButton("Weapons", uiSkin);
		((TextButton)weaponsButton.button).getLabel().setFontScale(.92f, .9f);
		weaponsButton.setPosition(listPanel.getX()+5, listPanel.getY()+ listPanel.getHeight()-20);
		weaponsButton.setSize(80, 15);

		final MBButton spellsButton = new MBButton("Spells", uiSkin);
		((TextButton)spellsButton.button).getLabel().setFontScale(1f, .9f);
		spellsButton.setPosition(weaponsButton.getX()+ weaponsButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
		spellsButton.setSize(80, 15);

		listPanel.add(weaponsButton);
		listPanel.add(spellsButton);

		weaponsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 2){
					itemTab = 1;
					for (Item item: listPanel.sItems) {
						item.setSoftVisible(false);
					}
					for (Item item: listPanel.wItems) {
						if(item.getSpot() >= 0 && item.getSpot() <= 5) {
							item.setSoftVisible(true);
							if (item.editMode && item.supposedToBeVisible) {
								item.saveEdit();
								item.edit();
							}
						}
					}
					for (Tipbox tipbox: tipboxes) {
						tipbox.setSoftVisible(true);
					}
				}
			}
		});

		spellsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 1){
					itemTab = 2;
					for (Item item: listPanel.sItems) {
						if(item.getSpot() >= 0 && item.getSpot() <= 5) {
							item.setSoftVisible(true);
							if (item.editMode && item.supposedToBeVisible) {
								item.saveEdit();
								item.edit();
							}
						}
					}
					for (Item item: listPanel.wItems) {
						item.setSoftVisible(false);
					}
					for (Tipbox tipbox: tipboxes) {
						if (tipbox.getPanel().editMode && tipbox.getPanel().supposedToBeVisible) {
							tipbox.setSoftVisible(true);
						}
					}
				}
			}
		});

		//creating item shift buttons and setting their sizes
		MBButton addButton = new MBButton(uiSkin);
		//XPosition = (ListPanelXPos + ListPanelWidth - GapBetweenBorderAndButton - DownButtonWidth - GapBetweenButtons - UpButtonWidth - GapBetweenButtons - AddButtonWidth)
		//which ends up being XPosition = (120 + 470 - 5 - 40 - 2 - 40 - 2 - 40) = 461
		addButton.setPosition(461, listPanel.getY()+ listPanel.getHeight()-20);
		addButton.setSize(40, 15);

		final MBButton upButton = new MBButton(uiSkin);
		upButton.setPosition(addButton.getX()+ addButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
		upButton.setSize(40, 15);

		final MBButton downButton = new MBButton(uiSkin);
		downButton.setPosition(upButton.getX()+ upButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
		downButton.setSize(40, 15);
		//adding item buttons to the list panel
		listPanel.add(addButton);
		listPanel.add(upButton);
		listPanel.add(downButton);
		//making the first WEAPON item and assigning it to the first spot
		final Item item1 = new Item(1, 0);
		//making the first SPELL item and assigning it to the first spot
		final Item itemS1 = new Item(2, 0);
		listPanel.add(item1);
		listPanel.add(itemS1);
		if(itemTab == 1){
			for (Item item: listPanel.sItems) {
				item.setSoftVisible(false);
			}
			for (Tipbox tipbox: tipboxes) {
				tipbox.setSoftVisible(false);
			}
		}
		if(itemTab == 2){
			for (Item item: listPanel.wItems) {
				item.setSoftVisible(false);
			}
			for (Tipbox tipbox: tipboxes) {
				tipbox.setSoftVisible(true);
			}
		}

		//adds a new item
		addButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 1) {
					Item item2 = new Item(1, Panel.nextAvaWSpot);
					listPanel.add(item2);
					item2.edit();
                    if(item2.getSpot() > 5) item2.setSoftVisible(false);
				}
				else if(itemTab == 2) {
					Item item2 = new Item(2, Panel.nextAvaSSpot);
					listPanel.add(item2);
					item2.edit();
					if(item2.getSpot() > 5) item2.setSoftVisible(false);
                }
            }
        });
		//shifts all the items up
		upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 1 && (listPanel.wItems.size() > 0)) {
					for (int i = 0; i < listPanel.wItems.get(0).getPanel().minipanels.size(); i++) {
						if (listPanel.wItems.get(0).getPanel().minipanels.get(i).wSpot > 0) {
							listPanel.wItems.get(0).shuffleItemsUp();
							break;
						}
					}
				}
				else if(itemTab == 2 && (listPanel.sItems.size() > 0)) {
					for (int i = 0; i < listPanel.sItems.get(0).getPanel().minipanels.size(); i++) {
						if (listPanel.sItems.get(0).getPanel().minipanels.get(i).sSpot > 0) {
							listPanel.sItems.get(0).shuffleItemsUp();
							break;
						}
					}
				}
            }
        });
		//shifts all the buttons down
		downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 1 && (listPanel.wItems.size() > 0)) {
					for (int i = 0; i < listPanel.wItems.get(0).getPanel().minipanels.size(); i++) {
						if (listPanel.wItems.get(0).getPanel().minipanels.get(i).wSpot < 0) {
							listPanel.wItems.get(0).shuffleItemsDown();
							break;
						}
					}
				}
				if(itemTab == 2 && (listPanel.sItems.size() > 0)) {
					for (int i = 0; i < listPanel.sItems.get(0).getPanel().minipanels.size(); i++) {
						if (listPanel.sItems.get(0).getPanel().minipanels.get(i).sSpot < 0) {
							listPanel.sItems.get(0).shuffleItemsDown();
							break;
						}
					}
				}
            }
        });
		//endregion

		//region stats
		//creating all the stats panels to hold the player stats
		final Minipanel strPanel, dexPanel, conPanel, intPanel, wisPanel, chaPanel;
		strPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(120, 870, 50, 60));
		dexPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(strPanel.getX()+strPanel.getWidth()+10, strPanel.getY(), 50, 60));
		conPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(dexPanel.getX()+dexPanel.getWidth()+10, strPanel.getY(), 50, 60));
		intPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(conPanel.getX()+conPanel.getWidth()+10, strPanel.getY(), 50, 60));
		wisPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(intPanel.getX()+intPanel.getWidth()+10, strPanel.getY(), 50, 60));
		chaPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(wisPanel.getX()+wisPanel.getWidth()+10, strPanel.getY(), 50, 60));
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
		strTF.setSize(42, 35);
		strTF.setPosition(124, 873);
		strTF.textField.setAlignment(Align.center);
		MBTextField dexTF = new MBTextField("", uiSkin);
		dexTF.setSize(42, 35);
		dexTF.setPosition(184, 873);
		dexTF.textField.setAlignment(Align.center);
		MBTextField conTF = new MBTextField("", uiSkin);
		conTF.setSize(42, 35);
		conTF.setPosition(244, 873);
		conTF.textField.setAlignment(Align.center);
		MBTextField intTF = new MBTextField("", uiSkin);
		intTF.setSize(42, 35);
		intTF.setPosition(304, 873);
		intTF.textField.setAlignment(Align.center);
		MBTextField wisTF = new MBTextField("", uiSkin);
		wisTF.setSize(42, 35);
		wisTF.setPosition(364, 873);
		wisTF.textField.setAlignment(Align.center);
		MBTextField chaTF = new MBTextField("", uiSkin);
		chaTF.setSize(42, 35);
		chaTF.setPosition(424, 873);
		chaTF.textField.setAlignment(Align.center);
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
		//adding stats panels to the panel
		genStatsPanel.add(strPanel);
		genStatsPanel.add(dexPanel);
		genStatsPanel.add(conPanel);
		genStatsPanel.add(intPanel);
		genStatsPanel.add(wisPanel);
		genStatsPanel.add(chaPanel);

		//Short Rest and Long Rest buttons
		Minipanel shortRestPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(chaPanel.getX()+wisPanel.getWidth()+10, strPanel.getY(), 50, 60));
		Minipanel longRestPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(shortRestPanel.getX()+shortRestPanel.getWidth()+10, strPanel.getY(), 50, 60));

		MBButton srButton = new MBButton("Short \n Rest", uiSkin);
		srButton.setSize(50, 60);
		srButton.setPosition(chaPanel.getX()+wisPanel.getWidth()+10, strPanel.getY());
		srButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (Item spell: listPanel.sItems) {
					((TextButton)spell.getUsesButton().button).setText(String.valueOf(spell.srMax));
					spell.uses[0] = spell.srMax;
				}
			}
		});
//		((TextButton)srButton.button).getLabel().setColor(new Color(0x8a8a8aff));

		MBButton lrButton = new MBButton("Long \n Rest", uiSkin);
		lrButton.setSize(50, 60);
		lrButton.setPosition(shortRestPanel.getX()+shortRestPanel.getWidth()+10, strPanel.getY());
		lrButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (Item spell: listPanel.sItems) {
					((TextButton)spell.getUsesButton().button).setText(String.valueOf(spell.lrMax));
					spell.uses[0] = spell.lrMax;
				}
			}
		});
//		((TextButton)lrButton.button).getLabel().setColor(new Color(0x8a8a8aff));

		shortRestPanel.add(srButton);
		longRestPanel.add(lrButton);

		genStatsPanel.add(shortRestPanel);
		genStatsPanel.add(longRestPanel);
		//endregion

		//region imagebutton
		//creating the imageButton as a text button
		final MBButton imageButton = new MBButton(uiSkin);
		imageButton.setPosition(595, 560);
		imageButton.setSize(290, 370);
		//setting the default opacity
		imageButton.aFloat = .5f;

		imageButton.toTextButton("ADD IMAGE");
		imageButton.setupSelectImageTextButton();
		genStatsPanel.add(imageButton);

		//endregion
		//endregion

		//region Top Bar
		final MBSelectBox dropdown = new MBSelectBox();
		dropdown.setSize(300, 40);
		dropdown.setPosition(topPanel.getX()+ topPanel.getWidth()-305, topPanel.getY()+5);
		dropdown.setItems("PLAYER 1", "player2", "etc...", "OPTION 4", "rando opt");
		player = dropdown.dropdown.getSelected();
		dropdown.addScrollPaneListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				player = dropdown.dropdown.getSelected();
			}
		});

		MBLabel playerNameLabel = new MBLabel(player, uiSkin);
		playerNameLabel.setPosition(topPanel.getX() + 10, topPanel.getY() + (topPanel.getHeight()/2) - (playerNameLabel.getHeight()/2));

		topPanel.add(playerNameLabel);
		topPanel.add(dropdown);

		//endregion

		//region MasterBoard
		masterBoard = new MBBoard();
		masterBoard.setPosition(masterboardPanel.getX()+1, masterboardPanel.getY()+1);
		masterBoard.setSize(masterboardPanel.getWidth()-2, masterboardPanel.getHeight()-2);
		masterboardPanel.add(masterBoard);

		//endregion

		//region Tool Bar
		MBButton selectButton = new MBButton("Select", uiSkin);
		selectButton.setPosition(toolbarPanel.getX() + 10, toolbarPanel.getY() +10);
		selectButton.setSize(200, toolbarPanel.getHeight()-20);
		selectButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!masterBoard.board.getSelectMode()){
					masterBoard.board.enterSelectMode();
				}
			}
		});

		MBButton drawButton = new MBButton("Draw", uiSkin);
		drawButton.setPosition(selectButton.getX() + selectButton.getWidth() + 5, selectButton.getY());
		drawButton.setSize(selectButton.getWidth(), selectButton.getHeight());
		drawButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!masterBoard.board.getDrawMode()){
					masterBoard.board.enterDrawMode();
				}
			}
		});

		MBButton eraseButton = new MBButton("Erase", uiSkin);
		eraseButton.setPosition(drawButton.getX() + drawButton.getWidth() + 5, selectButton.getY());
		eraseButton.setSize(selectButton.getWidth(), selectButton.getHeight());
		eraseButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!masterBoard.board.getEraseMode()){
					masterBoard.board.enterEraseMode();
				}
			}
		});

		final MBSelectBox sizesBox = new MBSelectBox();
		sizesBox.setPosition(eraseButton.getX() + eraseButton.getWidth() + 5, selectButton.getY());
		sizesBox.setSize(100, eraseButton.getHeight()/3 - 1);
		sizesBox.setItems("1", "3", "5", "11", "23", "45");
		sizesBox.dropdown.setSelected(String.valueOf(masterBoard.board.getCurrentBrush().brush.length));
		sizesBox.addScrollPaneListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				int newSize = Integer.parseInt(sizesBox.dropdown.getSelected());
				masterBoard.board.setBrush(newSize, masterBoard.board.isBrushSoft());
			}
		});

		final MBSelectBox softnessBox = new MBSelectBox();
		softnessBox.setPosition(sizesBox.getX(), sizesBox.getY() + sizesBox.getHeight() + 2);
		softnessBox.setSize(100, eraseButton.getHeight()/3 - 1);
		softnessBox.setItems("soft", "hard");
		softnessBox.addScrollPaneListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				masterBoard.board.setBrushSoft(softnessBox.dropdown.getSelected().equals("soft"));
			}
		});

		final MBSelectBox colorBox = new MBSelectBox();
		colorBox.setPosition(sizesBox.getX(), softnessBox.getY() + softnessBox.getHeight() + 2);
		colorBox.setSize(100, eraseButton.getHeight()/3 - 1);
		colorBox.setItems("BLACK", "WHITE", "RED", "YELLOW", "GREEN", "BLUE");
		colorBox.addScrollPaneListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				String color = colorBox.dropdown.getSelected();
				switch (color){
					case "BLACK":
						masterBoard.board.setCurrentColor(Color.BLACK);
						masterBoard.board.setDrawingColor(Color.BLACK);
						break;
					case "WHITE":
						masterBoard.board.setCurrentColor(Color.WHITE);
						masterBoard.board.setDrawingColor(Color.WHITE);
						break;
					case "RED":
						masterBoard.board.setCurrentColor(Color.RED);
						masterBoard.board.setDrawingColor(Color.RED);
						break;
					case "YELLOW":
						masterBoard.board.setCurrentColor(Color.YELLOW);
						masterBoard.board.setDrawingColor(Color.YELLOW);
						break;
					case "GREEN":
						masterBoard.board.setCurrentColor(Color.GREEN);
						masterBoard.board.setDrawingColor(Color.GREEN);
						break;
					case "BLUE":
						masterBoard.board.setCurrentColor(Color.BLUE);
						masterBoard.board.setDrawingColor(Color.BLUE);
						break;
				}
			}
		});

		toolbarPanel.add(selectButton);
		toolbarPanel.add(drawButton);
		toolbarPanel.add(eraseButton);
		toolbarPanel.add(sizesBox);
		toolbarPanel.add(softnessBox);
		toolbarPanel.add(colorBox);
		//endregion

		//honestly don't know what this does, but it's essential
		InputMultiplexer multiplexer = new InputMultiplexer();
//		multiplexer.addProcessor((InputProcessor) this);
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render () {
		//clearing the board before drawing ??
		ScreenUtils.clear(new Color(0x747474ff));
		//drawing the panels
		batch.begin();

		sidePanel.render(batch);
		topPanel.render(batch);
		genStatsPanel.render(batch);
		reminderPanel.render(batch);
		toolbarPanel.render(batch);
		masterboardPanel.render(batch);


		//drawing the components after so that they are on the top
		for (Tipbox tipbox: tipboxes) {
			tipbox.render(batch);
		}


		//fixme
/*
		for (Outline outline : outlines) {
			outline.draw(batch, 1);
		}
*/
		for (MBSelectBox selectBox: scrollpanes) {
			if(selectBox.dropdown.isActive) selectBox.draw(selectBox.aFloat);
		}
		for (MBWindow window: windows) {
			window.draw(window.aFloat);
		}
		if(contextMenu.isActive()){
			contextMenu.draw(contextMenu.aFloat);
		}

		batch.end();

//		stage.draw();
		stage.act();

		//fixme might be inefficient but it does the job
		if(!player.contentEquals(((MBLabel)topPanel.components.get(0)).label.getText())){
			((MBLabel)topPanel.components.get(0)).label.setText(player);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
	static public void fileChooseChanged(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//disabling input from the program so this is the only thing clickable
				InputProcessor processor = Gdx.input.getInputProcessor();
				Gdx.input.setInputProcessor(null);

				//makes only .jpg .png or .gif files able to be selected
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Select Image");
				//initializing the frame
				f.setVisible(true);
				f.toFront();
				f.setAlwaysOnTop(true);
				f.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
				f.setVisible(false);
				//if the file is selected...
				int res = chooser.showSaveDialog(f);
				f.dispose();
				if (res == JFileChooser.APPROVE_OPTION) {
					//saves the selected file as a file
					File file = chooser.getSelectedFile();
					//saves the file location as a string
					path = file.toString();
				}
				//re-enabling input
				Gdx.input.setInputProcessor(processor);
			}
		}).start();
	}
	static public void fileChooseHandle(final Panel genStatsPanel, final MBButton imageButton){
		//to make sure this is only ran whenever the user selects a file
		if(path != null) {
			Texture tex2 = new Texture(path);
			//deletes the imageButton from the stage so that when it's added back it doesn't cause any complications in terms of the CompID
			genStatsPanel.delete(imageButton);
			//turns the imageButton into an ImageButton
			imageButton.toImageButton(tex2);
			imageButton.setupSelectImageImageButton();
			//adds the imageButton to the stage so it's listener works
			genStatsPanel.add(imageButton);

			final MBButton reselectButton;
			reselectButton = new MBButton(uiSkin);
			reselectButton.setPosition(imageButton.getX()+10, imageButton.getY()+10);
			reselectButton.setSize(40, 40);
			reselectButton.aFloat = .75f;

            final MBButton deleteButton;
            deleteButton = new MBButton(uiSkin);
            deleteButton.setPosition(imageButton.getX()+60, imageButton.getY()+10);
            deleteButton.setSize(40, 40);
            deleteButton.aFloat = .75f;

            reselectButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					System.out.println("wassup");
					fileChooseChanged();
				}
				@Override
				public boolean handle (Event event) {
					if (!reselectButton.getButton().isOver()) {
                        deleteButton.aFloat = .75f;
                        reselectButton.aFloat = .75f;
					}
					else {
                        imageButton.aFloat = 1f;
                        deleteButton.aFloat = .5f;
                        reselectButton.aFloat = 1f;
					}
					if (!(event instanceof ChangeEvent)) return false;
					changed((ChangeEvent)event, event.getTarget());
					return false;
				}
			});
			deleteButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					System.out.println("wassup");
                    genStatsPanel.delete(imageButton);
					imageButton.toTextButton("ADD IMAGE");
					imageButton.setupSelectImageTextButton();
                    genStatsPanel.add(imageButton);
				}
				@Override
				public boolean handle (Event event) {
					if (!deleteButton.getButton().isOver()) {
						deleteButton.aFloat = .75f;
						reselectButton.aFloat = .75f;
					}
					else {
						imageButton.aFloat = 1f;
						deleteButton.aFloat = 1f;
						reselectButton.aFloat = .5f;
					}
					if (!(event instanceof ChangeEvent)) return false;
					changed((ChangeEvent)event, event.getTarget());
					return false;
				}
			});

            imageButton.add(deleteButton);
            imageButton.add(reselectButton);
			//so that it's not called again
			path = null;
		}
	}
}

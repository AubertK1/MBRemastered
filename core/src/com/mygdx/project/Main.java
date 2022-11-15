package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;


public class Main extends ApplicationAdapter {
	//Used to draw the panels
	static SpriteBatch batch;
	//used to draw the MBComponents
	static Stage stage;
	//creating main panels
	Panel sidePanel, topPanel, genStatsPanel, reminderPanel, toolbarPanel, masterboardPanel;
	//list with all the MBComponents
	static ArrayList<MBComponent> allComps = new ArrayList<>();

	String player;
    static ArrayList<Tipbox> tipboxes = new ArrayList<>();

	static int itemTab = 1;
	@Override
	public void create () {
        player = "PLAYER 1";
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
		masterboardPanel = new Panel("core\\pics\\MBSkin2\\MasterboardPanel.png",
				new Rectangle(900, 150, 1018, 850));
		//setting up skin for the UI of the app
		Skin uiSkin = new Skin (Gdx.files.internal(
				"assets\\skins\\uiskin.json"));

		sidePanel.setSoftVisible(true);
		topPanel.setSoftVisible(true);
		genStatsPanel.setSoftVisible(true);
		reminderPanel.setSoftVisible(true);
		toolbarPanel.setSoftVisible(true);
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
		//region stats
		//creating all the stats panels to hold the player stats
		Minipanel strPanel, dexPanel, conPanel, intPanel, wisPanel, chaPanel;
		strPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(120, 870, 50, 60));
		dexPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(180, 870, 50, 60));
		conPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(240, 870, 50, 60));
		intPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(300, 870, 50, 60));
		wisPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
				new Rectangle(360, 870, 50, 60));
		chaPanel = new Minipanel("core\\pics\\MBSkin2\\minipanel2.png",
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
		//adding stats panels to the panel
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
		//creating a list panel to hold all the items and adding it to the genstats panel
		final Minipanel listPanel = new Minipanel("core\\pics\\MBSkin2\\ListPanel.png",
				new Rectangle(120, 560, 470, 300));
		genStatsPanel.add(listPanel);

		//creating item tab buttons
		final MBButton weaponsButton = new MBButton(uiSkin);
		weaponsButton.setPosition(listPanel.getX()+5, listPanel.getY()+ listPanel.getHeight()-20);
		weaponsButton.setSize(80, 15);

		final MBButton spellsButton = new MBButton(uiSkin);
		spellsButton.setPosition(weaponsButton.getX()+ weaponsButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
		spellsButton.setSize(80, 15);

		listPanel.add(weaponsButton);
		listPanel.add(spellsButton);

		weaponsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 2){
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
						tipbox.setSoftVisible(false);
					}
					itemTab = 1;
				}
			}
		});

		spellsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(itemTab == 1){
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
					itemTab = 2;
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

		//region Top Bar

		MBLabel playerNameLabel = new MBLabel(player, uiSkin);
		playerNameLabel.setPosition(topPanel.getX() + 10, topPanel.getY() + (topPanel.getHeight()/2) - (playerNameLabel.getHeight()/2));

		topPanel.add(playerNameLabel);

		//endregion


		//honestly don't know what this does, but it's essential
		Gdx.input.setInputProcessor(stage);
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

//		reminderTextArea.setBorder(batch);
		//drawing the components after so that they are on the top
		for (Tipbox tipbox: tipboxes) {
			tipbox.render(batch);
		}
		batch.end();

//		stage.draw();
//		stage.act();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Screen implements Renderable{
    //Used to draw the panels
    SpriteBatch batch;
    //used to draw the MBComponents
    Stage stage;
    //the skin for the components
    Skin skin;

    public MBContextMenu contextMenu;
    public Panel grayPanel;
    //creating main panels
    Panel sidePanel, topPanel, genStatsPanel, reminderPanel, toolbarPanel, masterboardPanel;

    ArrayList<Panel> mainPanels = new ArrayList<>();
    ArrayList<Panel> focusedPanels = new ArrayList<>();
    ArrayList<MBComponent> focusedComps = new ArrayList<>();
    ArrayList<Outline> focusedOutlines = new ArrayList<>();
    boolean inFocusMode = false;
    int focusedLayers = 0;

    MBBoard masterBoard;
    //list with all the MBComponents
    ArrayList<MBComponent> allComps = new ArrayList<>();

    String player;
    //so these can be drawn last

    HashMap<Integer, ArrayList<Renderable>> layers = new HashMap<>();

    //weapons or spell items for the itempanel
    int itemTab = 1;

    //controls whether this is rendered or not
    boolean supposedToBeVisible = true;

    //region used for the Upload Image button
    //these are initialized from the start so that when they're used time isn't wasted while loading them
    static String fileChooserPath;
    static final JFileChooser chooser = new JFileChooser();
    static final FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG, PNG & GIF Images", "jpg", "gif", "png");
    static final JFrame f = new JFrame();
    //endregion

    public Screen() {
        //        player = "PLAYER 1";
        //setting up batch and stage
        batch = Main.batch;
        stage = Main.stage;
        //setting up panels
        sidePanel = new Panel("assets\\Panels\\SidecardPanel.png",
                new Rectangle(2, 150, 98, 850), this);
        topPanel = new Panel("assets\\Panels\\TopbarPanel.png",
                new Rectangle(110, 950, 780, 50), this);
        genStatsPanel = new Panel("assets\\Panels\\GenstatsPanel.png",
                new Rectangle(110, 550, 780, 390), this);
        reminderPanel = new Panel("assets\\Panels\\ReminderPanel.png",
                new Rectangle(110, 150, 780, 390), this);
        toolbarPanel = new Panel("assets\\Panels\\ToolbarPanel.png",
                new Rectangle(2, 2, 1916, 138), this);
        masterboardPanel = new Panel("assets\\Panels\\MasterboardPanel4.png",
                new Rectangle(900, 150, 1018, 850), this);
        mainPanels.add(sidePanel);
        mainPanels.add(topPanel);
        mainPanels.add(genStatsPanel);
        mainPanels.add(reminderPanel);
        mainPanels.add(toolbarPanel);
        mainPanels.add(masterboardPanel);

        //setting up skin for the UI of the app
        skin = new Skin (Gdx.files.internal(
                "assets\\skins\\uiskin.json"));

        grayPanel = new Panel("assets\\gradient2.png", new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), this);
        grayPanel.aFloat = .75f;

        sidePanel.setSoftVisible(true);
        topPanel.setSoftVisible(true);
        genStatsPanel.setSoftVisible(true);

        toolbarPanel.setSoftVisible(true);
        reminderPanel.setSoftVisible(true);
        masterboardPanel.setSoftVisible(true);

        //region Reminders
        //creating a textarea
        MBTextArea reminderTextArea;
        reminderTextArea = new MBTextArea("", this);
        reminderTextArea.getTextArea().setSize(760,330);
        reminderTextArea.getTextArea().setPosition(120,160);
        //creating a label
        MBLabel reminderLabel = new MBLabel("REMINDERS", this);
        reminderLabel.setSize(760, 40);
        reminderLabel.setPosition(120, 490);
        //adding to the Reminders panel as its components
        reminderPanel.add(reminderTextArea);
        reminderPanel.add(reminderLabel);
        //endregion

        //region General Stats
        //region listpanel
        //creating a list panel to hold all the items and adding it to the genstats panel
        final Minipanel listPanel = new Minipanel("assets\\Panels\\ListPanel.png",
                new Rectangle(120, 560, 470, 300), this);
        genStatsPanel.add(listPanel);

        //region item panels
        final ItemPanel weaponsPanel = new ItemPanel("assets\\clear.png",
                new Rectangle(listPanel.getX()+5, listPanel.getY() + 5, listPanel.getWidth() - 10, listPanel.getHeight()-34), this);
        final ItemPanel spellsPanel = new ItemPanel("assets\\clear.png",
                new Rectangle(listPanel.getX()+5, listPanel.getY() + 5, listPanel.getWidth() - 10, listPanel.getHeight()-34), this);
        listPanel.add(weaponsPanel);
        listPanel.add(spellsPanel);
        weaponsPanel.setFocused(true);
        spellsPanel.setFocused(true);

        //making the first WEAPON item and assigning it to the first spot
        Item weaponItem1 = new WeaponItem(this);
        //making the first SPELL item and assigning it to the first spot
        Item spellItem1 = new SpellItem(this);

        weaponsPanel.add(weaponItem1);
        spellsPanel.add(spellItem1);

        if(itemTab == 1){
            weaponsPanel.setSoftVisible(true);
            spellsPanel.setSoftVisible(false);
        }
        if(itemTab == 2){
            weaponsPanel.setSoftVisible(false);
            spellsPanel.setSoftVisible(true);
        }
        //endregion

        //region item tab buttons
        final MBButton weaponsButton = new MBButton("Weapons", this, "toggle");
        ((TextButton)weaponsButton.getButton()).getLabel().setFontScale(.92f, .9f);
        weaponsButton.setPosition(listPanel.getX()+5, listPanel.getY()+ listPanel.getHeight()-20);
        weaponsButton.setSize(80, 15);

        final MBButton spellsButton = new MBButton("Spells", this, "toggle");
        ((TextButton)spellsButton.getButton()).getLabel().setFontScale(1f, .9f);
        spellsButton.setPosition(weaponsButton.getX()+ weaponsButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
        spellsButton.setSize(80, 15);

        listPanel.add(weaponsButton);
        listPanel.add(spellsButton);

        if(itemTab == 1){
            weaponsButton.getButton().setChecked(true);
            weaponsButton.getButton().setTouchable(Touchable.disabled);
            spellsButton.getButton().setChecked(false);
        }
        else{
            spellsButton.getButton().setChecked(true);
            spellsButton.getButton().setTouchable(Touchable.disabled);
            weaponsButton.getButton().setChecked(false);
        }

        weaponsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(itemTab == 2){
                    itemTab = 1;
                    weaponsPanel.setSoftVisible(true);
                    spellsPanel.setSoftVisible(false);
                    weaponsButton.getButton().setTouchable(Touchable.disabled);
                    spellsButton.getButton().setTouchable(Touchable.enabled);
                }
                spellsButton.getButton().setChecked(false);

                return false;
            }
        });

        spellsButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(itemTab == 1){
                    itemTab = 2;
                    spellsPanel.setSoftVisible(true);
                    weaponsPanel.setSoftVisible(false);
                    weaponsButton.getButton().setTouchable(Touchable.enabled);
                    spellsButton.getButton().setTouchable(Touchable.disabled);
                }
                weaponsButton.getButton().setChecked(false);

                return false;
            }
        });

        weaponsButton.setFocused(true);
        spellsButton.setFocused(true);
        //endregion

        //region item shift buttons
        //creating item shift buttons and setting their sizes
        MBButton addButton = new MBButton(this);
        //XPosition = (ListPanelXPos + ListPanelWidth - GapBetweenBorderAndButton - DownButtonWidth - GapBetweenButtons - UpButtonWidth - GapBetweenButtons - AddButtonWidth)
        //which ends up being XPosition = (120 + 470 - 5 - 40 - 2 - 40 - 2 - 40) = 461
        addButton.setPosition(461, listPanel.getY()+ listPanel.getHeight()-20);
        addButton.setSize(40, 15);

        final MBButton upButton = new MBButton(this);
        upButton.setPosition(addButton.getX()+ addButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
        upButton.setSize(40, 15);

        final MBButton downButton = new MBButton(this);
        downButton.setPosition(upButton.getX()+ upButton.getWidth()+2, listPanel.getY()+ listPanel.getHeight()-20);
        downButton.setSize(40, 15);
        //adding item buttons to the list panel
        listPanel.add(addButton);
        listPanel.add(upButton);
        listPanel.add(downButton);

        //adds a new item
        addButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(itemTab == 1) {
                    weaponsPanel.add(new WeaponItem(Screen.this));
                }
                else if(itemTab == 2) {
                    spellsPanel.add(new SpellItem(Screen.this));
                }
            }
        });
        //shifts all the items up
        upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(itemTab == 1) {
                    weaponsPanel.shuffleItemsUp();
                }
                else if(itemTab == 2) {
                    spellsPanel.shuffleItemsUp();
                }
            }
        });
        //shifts all the buttons down
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(itemTab == 1) {
                    weaponsPanel.shuffleItemsDown();
                }
                else if(itemTab == 2) {
                    spellsPanel.shuffleItemsDown();
                }
            }
        });
        //endregion
        //endregion

        //region stats
        //creating all the stats panels to hold the player stats
        final Minipanel strPanel, dexPanel, conPanel, intPanel, wisPanel, chaPanel;
        strPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(120, 870, 50, 60), this);
        dexPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(strPanel.getX()+strPanel.getWidth()+10, strPanel.getY(), 50, 60), this);
        conPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(dexPanel.getX()+dexPanel.getWidth()+10, strPanel.getY(), 50, 60), this);
        intPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(conPanel.getX()+conPanel.getWidth()+10, strPanel.getY(), 50, 60), this);
        wisPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(intPanel.getX()+intPanel.getWidth()+10, strPanel.getY(), 50, 60), this);
        chaPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(wisPanel.getX()+wisPanel.getWidth()+10, strPanel.getY(), 50, 60), this);
        //creating the labels to put in the stats' minipanels
        MBLabel strL = new MBLabel("STR", this);
        //setting position equal to its minipanel's left border + half the minipanel's width - half the label's width
        strL.setPosition(strPanel.getX() + (strPanel.getWidth()/2) - (strL.getWidth()/2), 903);
        MBLabel dexL = new MBLabel("DEX", this);
        dexL.setPosition(dexPanel.getX() + (dexPanel.getWidth()/2) - (dexL.getWidth()/2), 903);
        MBLabel conL = new MBLabel("CON", this);
        conL.setPosition(conPanel.getX() + (conPanel.getWidth()/2) - (conL.getWidth()/2), 903);
        MBLabel intL = new MBLabel("INT", this);
        intL.setPosition(intPanel.getX() + (intPanel.getWidth()/2) - (intL.getWidth()/2), 903);
        MBLabel wisL = new MBLabel("WIS", this);
        wisL.setPosition(wisPanel.getX() + (wisPanel.getWidth()/2) - (wisL.getWidth()/2), 903);
        MBLabel chaL = new MBLabel("CHA", this);
        chaL.setPosition(chaPanel.getX() + (chaPanel.getWidth()/2) - (chaL.getWidth()/2), 903);
        //creating the textfields to put in the stats' minipanels
        MBTextField strTF = new MBTextField("", this);
        //size and positions set by eyeballing until it looked nice
        strTF.setSize(42, 35);
        strTF.setPosition(124, 873);
        strTF.getTextField().setAlignment(Align.center);
        MBTextField dexTF = new MBTextField("", this);
        dexTF.setSize(42, 35);
        dexTF.setPosition(184, 873);
        dexTF.getTextField().setAlignment(Align.center);
        MBTextField conTF = new MBTextField("", this);
        conTF.setSize(42, 35);
        conTF.setPosition(244, 873);
        conTF.getTextField().setAlignment(Align.center);
        MBTextField intTF = new MBTextField("", this);
        intTF.setSize(42, 35);
        intTF.setPosition(304, 873);
        intTF.getTextField().setAlignment(Align.center);
        MBTextField wisTF = new MBTextField("", this);
        wisTF.setSize(42, 35);
        wisTF.setPosition(364, 873);
        wisTF.getTextField().setAlignment(Align.center);
        MBTextField chaTF = new MBTextField("", this);
        chaTF.setSize(42, 35);
        chaTF.setPosition(424, 873);
        chaTF.getTextField().setAlignment(Align.center);
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
        Minipanel shortRestPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(chaPanel.getX()+wisPanel.getWidth()+10, strPanel.getY(), 50, 60), this);
        Minipanel longRestPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(shortRestPanel.getX()+shortRestPanel.getWidth()+10, strPanel.getY(), 50, 60), this);

        MBButton srButton = new MBButton("Short \n Rest", this);
        srButton.setSize(50, 60);
        srButton.setPosition(chaPanel.getX()+wisPanel.getWidth()+10, strPanel.getY());
        srButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Item spell: spellsPanel.getItems()) {
                    if(spell instanceof SpellItem) ((SpellItem) spell).shortRest();
                }
            }
        });
//		((TextButton)srButton.button).getLabel().setColor(new Color(0x8a8a8aff));

        MBButton lrButton = new MBButton("Long \n Rest", this);
        lrButton.setSize(50, 60);
        lrButton.setPosition(shortRestPanel.getX()+shortRestPanel.getWidth()+10, strPanel.getY());
        lrButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Item spell: spellsPanel.getItems()) {
                    if(spell instanceof SpellItem) ((SpellItem) spell).longRest();
                }
            }
        });
        //		((TextButton)lrButton.button).getLabel().setColor(new Color(0x8a8a8aff));

        shortRestPanel.add(srButton);
        longRestPanel.add(lrButton);

        genStatsPanel.add(shortRestPanel);
        genStatsPanel.add(longRestPanel);

        strPanel.setFocused(true);
        dexPanel.setFocused(true);
        conPanel.setFocused(true);
        intPanel.setFocused(true);
        wisPanel.setFocused(true);
        chaPanel.setFocused(true);
//		shortRestPanel.setFocused(true);
//		longRestPanel.setFocused(true);
        //endregion

        //region imagebutton
        //creating the imageButton as a text button
        final MBButton imageButton = new MBButton(this);
        imageButton.setPosition(595, 560);
        imageButton.setSize(290, 370);
        //setting the default opacity
        imageButton.aFloat = .5f;

        imageButton.toTextButton("ADD IMAGE");
        imageButton.setupSelectImageTextButton();
        genStatsPanel.add(imageButton);

        imageButton.setFocused(true);
        //endregion
        //endregion

        //region Top Bar
        final MBSelectBox dropdown = new MBSelectBox(this);
        dropdown.setSize(300, 40);
        dropdown.setPosition(topPanel.getX()+ topPanel.getWidth()-305, topPanel.getY()+5);
        dropdown.setItems("PLAYER 1", "player2", "etc...", "OPTION 4", "rando opt");
        player = dropdown.dropdown.getSelected();
        dropdown.addScrollPaneListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                player = dropdown.dropdown.getSelected();
            }
        });

        MBLabel playerNameLabel = new MBLabel(player, this);
        playerNameLabel.setPosition(topPanel.getX() + 10, topPanel.getY() + (topPanel.getHeight()/2) - (playerNameLabel.getHeight()/2));

        topPanel.add(playerNameLabel);
        topPanel.add(dropdown, 1);

        playerNameLabel.setFocused(true);
        dropdown.setFocused(true);
        //endregion

        //region MasterBoard
        masterBoard = new MBBoard(this);
        masterBoard.setPosition(masterboardPanel.getX()+1, masterboardPanel.getY()+1);
        masterBoard.setSize(masterboardPanel.getWidth()-2, masterboardPanel.getHeight()-2);
        masterboardPanel.add(masterBoard);

        //endregion

        //region Tool Bar
        final MBButton focusButton = new MBButton("FOCUS", this);
        focusButton.setPosition(toolbarPanel.getX() + 10, toolbarPanel.getY() + 10);
        focusButton.setSize(toolbarPanel.getHeight()-20, toolbarPanel.getHeight()-20);
        focusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("FOCUSED");

                if(!inFocusMode){
                    focus();
                    inFocusMode = true;
                }
                else{
                    unfocus();
                    inFocusMode = false;
                }
            }
        });

        MBButton selectButton = new MBButton("Select", this);
        selectButton.setPosition(focusButton.getX() + focusButton.getWidth() + 10, toolbarPanel.getY() + 10);
//		selectButton.setPosition(toolbarPanel.getX() + 10, toolbarPanel.getY() + 10);
        selectButton.setSize(200, toolbarPanel.getHeight()-20);
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!masterBoard.board.isInSelectMode()){
                    masterBoard.board.enterSelectMode();
                }
            }
        });

        MBButton drawButton = new MBButton("Draw", this);
        drawButton.setPosition(selectButton.getX() + selectButton.getWidth() + 5, selectButton.getY());
        drawButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        drawButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!masterBoard.board.isInDrawMode()){
                    masterBoard.board.enterDrawMode();
                }
            }
        });

        MBButton eraseButton = new MBButton("Erase", this);
        eraseButton.setPosition(drawButton.getX() + drawButton.getWidth() + 5, selectButton.getY());
        eraseButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        eraseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!masterBoard.board.isInEraseMode()){
                    masterBoard.board.enterEraseMode();
                }
            }
        });

        final MBSelectBox sizesBox = new MBSelectBox(this);
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

        final MBSelectBox softnessBox = new MBSelectBox(this);
        softnessBox.setPosition(sizesBox.getX(), sizesBox.getY() + sizesBox.getHeight() + 2);
        softnessBox.setSize(100, eraseButton.getHeight()/3 - 1);
        softnessBox.setItems("soft", "hard");
        softnessBox.addScrollPaneListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                masterBoard.board.setBrushSoft(softnessBox.dropdown.getSelected().equals("soft"));
            }
        });

        final MBSelectBox colorBox = new MBSelectBox(this);
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

        MBColorPicker colorPicker = new MBColorPicker(this);
        colorPicker.setSize(0, toolbarPanel.getHeight() - 10);
        colorPicker.setPosition((toolbarPanel.getX() + toolbarPanel.getWidth()) - (colorPicker.getWidth() + 5), toolbarPanel.getY()+5);

        toolbarPanel.add(focusButton);
        toolbarPanel.add(selectButton);
        toolbarPanel.add(drawButton);
        toolbarPanel.add(eraseButton);
        toolbarPanel.add(sizesBox, 2);
        toolbarPanel.add(softnessBox, 2);
        toolbarPanel.add(colorBox, 2);
        toolbarPanel.add(colorPicker);
        //endregion
    }

    public void focus(){
        //adding a new layer for every potential layer we may have
        int nonfocusedLayers = layers.size();
        for (int i = 0; i < nonfocusedLayers + 1; i++) {
            layers.put(nonfocusedLayers + i, new ArrayList<Renderable>());
            focusedLayers = i + 1;
        }

        //setting every focused (and that hasn't been brought forward yet) renderables' layer to its corresponding focusedLayer
        for (int layer = 0; layer < nonfocusedLayers; layer++) {
            //so that it's not updating the layers.layer's size/indexes while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layer));
            for (Renderable renderable : currentLayer) {
                if (renderable.isFocused()) {
                    renderable.setLayer(renderable.getLayer() + focusedLayers);
                }
            }
        }

        grayPanel.setLayer(nonfocusedLayers);
    }
    public void unfocus(){
        //setting every focused renderables' layer back to its original layer
        int nonfocusedLayers = layers.size()-focusedLayers;
        for (int layer = layers.size() - 1; layer >= nonfocusedLayers; layer--) {
            //so that it's not updating the layers.layer's size/indexes while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layer));
            for (Renderable value : currentLayer) {
                value.setLayer(value.getLayer() - focusedLayers);
            }
            if(layers.get(layer).size() == 0) layers.remove(layer); //deletes the layer when its empty
        }

        focusedLayers = 0;
    }

    //region File Chooser
    public void fileChooseChanged(){
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
                    fileChooserPath = file.toString();
                }
                //re-enabling input
                Gdx.input.setInputProcessor(processor);
            }
        }).start();
    }
    public void fileChooseHandle(final Panel genStatsPanel, final MBButton imageButton){
        //to make sure this is only ran whenever the user selects a file
        if(fileChooserPath != null) {
            Texture tex2 = new Texture(fileChooserPath);
            //deletes the imageButton from the stage so that when it's added back it doesn't cause any complications in terms of the CompID
            genStatsPanel.delete(imageButton);
            //turns the imageButton into an ImageButton
            imageButton.toImageButton(tex2);
            imageButton.setupSelectImageImageButton();
            //adds the imageButton to the stage, so it's listener works
            genStatsPanel.add(imageButton);

            final MBButton reselectButton;
            reselectButton = new MBButton(this);
            reselectButton.setPosition(imageButton.getX()+10, imageButton.getY()+10);
            reselectButton.setSize(40, 40);
            reselectButton.aFloat = .75f;

            final MBButton deleteButton;
            deleteButton = new MBButton(this);
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
            fileChooserPath = null;
        }
    }
    //endregion

    @Override
    public void render() {
        //rendering everything in layer 1 here so they're rendered in order
        for (Panel panel : mainPanels) {
            panel.render();
        }

        for (int layer = 1; layer < layers.size(); layer++) {
            for (int renderable = 0; renderable < layers.get(layer).size(); renderable++) {
                if(layers.get(layer).get(renderable).isSupposedToBeVisible()) {
                    layers.get(layer).get(renderable).render();
                }
            }
        }

        //fixme might be inefficient but it does the job
        if(!player.contentEquals(((MBLabel)topPanel.components.get(0)).getLabel().getText())){
            ((MBLabel)topPanel.components.get(0)).getLabel().setText(player);
        }
    }

    /**
     * reassigns the compID variable for all the components
     */
    public void resetCompIDs(){
        for (int i = 0; i < allComps.size(); i++) {
            allComps.get(i).compID = i;
        }
    }

    @Override
    public boolean isSupposedToBeVisible() {
        return supposedToBeVisible;
    }

    @Override
    public void setLayer(int layer) {

    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public boolean isFocused() {
        return false;
    }
    public boolean isInFocusMode() {
        return inFocusMode;
    }

    public void dispose() {
        batch.dispose();
        sidePanel.dispose();
        topPanel.dispose();
        genStatsPanel.dispose();
        reminderPanel.dispose();
        toolbarPanel.dispose();
        masterboardPanel.dispose();
        this.dispose();
        stage.dispose();
        chooser.setEnabled(false);
        f.dispose();
    }
}

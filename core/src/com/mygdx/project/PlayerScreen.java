package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class PlayerScreen extends Screen{
    public PlayerScreen() {
        super();
        //setting up panels
        topPanel = new Panel("assets\\Panels\\TopbarPanel.png",
                new Rectangle(110, 950, 780, 50), this);
        genStatsPanel = new Panel("assets\\Panels\\GenstatsPanel.png",
                new Rectangle(110, 550, 780, 390), this);
        reminderPanel = new Panel("assets\\Panels\\ReminderPanel.png",
                new Rectangle(110, 150, 780, 390), this);
        masterboardPanel = new Panel("assets\\Panels\\MasterboardPanel4.png",
                new Rectangle(900, 150, 1018, 850), this);
        mainPanels.add(topPanel);
        mainPanels.add(genStatsPanel);
        mainPanels.add(reminderPanel);
        mainPanels.add(masterboardPanel);

        grayPanel = new Panel("assets\\gradient2.png", new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), this);
        grayPanel.aFloat = .75f;

        topPanel.setSoftVisible(true);
        genStatsPanel.setSoftVisible(true);

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
                    weaponsPanel.add(new WeaponItem(PlayerScreen.this));
                }
                else if(itemTab == 2) {
                    spellsPanel.add(new SpellItem(PlayerScreen.this));
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

/*
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
*/
    }
}

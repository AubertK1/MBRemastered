package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class PlayerScreen extends Screen{
    //weapons or spell items for the itempanel
    private int itemTab = 1;
    private int statTab = 1;

    public PlayerScreen(final String player) {
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


        //region Reminders
        //creating a label
        MBLabel reminderLabel = new MBLabel("REMINDERS", this);
        reminderLabel.setSize(470, 40);
        reminderLabel.setPosition(120, 490);

        //creating a textarea
        MBTextArea reminderTextArea;
        reminderTextArea = new MBTextArea("", this);
        reminderTextArea.setSize(470,330);
        reminderTextArea.setPosition(120,160);

        //region extra stats
        Minipanel statsPanel = new Minipanel("assets\\Panels\\StatsPanel.png",
                new Rectangle(reminderTextArea.getX() + reminderTextArea.getWidth() + 5, reminderTextArea.getY(),
                        reminderPanel.getWidth() - (reminderTextArea.getWidth() + 20), reminderPanel.getHeight() - 20), this);

        //region stat panels
        final ItemPanel skillsPanel = new ItemPanel("assets\\clear.png",
                new Rectangle(statsPanel.getX() + 5, statsPanel.getY() + 5, statsPanel.getWidth() - 10, reminderTextArea.getHeight() - 5), this);
        final ItemPanel savesPanel = new ItemPanel("assets\\clear.png",
                new Rectangle(statsPanel.getX() + 5, statsPanel.getY() + 5, statsPanel.getWidth() - 10, reminderTextArea.getHeight() - 5), this);
        skillsPanel.setFocused(true);
        savesPanel.setFocused(true);
        statsPanel.add(skillsPanel);
        statsPanel.add(savesPanel);

        skillsPanel.setMaxSpot(17);
        skillsPanel.setColumns(2);
        skillsPanel.setRows(9);

        String[] skills = new String[]{"Acrobatics", "Animal Handling", "Arcana", "Athletics", "Deception", "History",
                "Insight", "Intimidation", "Investigation", "Medicine", "Nature", "Perception", "Performance", "Persuasion",
                "Religion", "Sleight of Hand", "Stealth", "Survival"};

        for (int i = 0; i < skills.length; i++) {
            if(i == 0){ //initializing the fist skill
                float skillHeight = (skillsPanel.getHeight() - 40) / 9f;
                skillsPanel.add(new SkillItem(new Rectangle(skillsPanel.getX(), skillsPanel.getY() + skillsPanel.getHeight() - skillHeight, (skillsPanel.getWidth() - 2.5f) / 2f, skillHeight),
                        this, skills[0]));
            }
            else skillsPanel.add(new SkillItem(this, skills[i]));
        }

        savesPanel.setRows(6);

        String[] saves = new String[]{"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};

        for (int i = 0; i < saves.length; i++) {
            if(i == 0){ //initializing the fist skill
                float saveHeight = (savesPanel.getHeight() - 25) / 6f;
                savesPanel.add(new SkillItem(new Rectangle(savesPanel.getX(), savesPanel.getY() + savesPanel.getHeight() - saveHeight, savesPanel.getWidth(), saveHeight),
                        this, saves[0]));
            }
            else savesPanel.add(new SkillItem(this, saves[i]));
        }

        if(statTab == 1){
            skillsPanel.setSoftVisible(true);
            savesPanel.setSoftVisible(false);
        }
        if(statTab == 2){
            skillsPanel.setSoftVisible(false);
            savesPanel.setSoftVisible(true);
        }
        //endregion

        //region stats tab buttons
        final MBButton skillsButton = new MBButton("Skills", this, "toggle");
        ((TextButton)skillsButton.getButton()).getLabel().setFontScale(.92f, .9f);
        skillsButton.setPosition(statsPanel.getX()+5, statsPanel.getY()+ statsPanel.getHeight()-20);
        skillsButton.setSize(80, 15);

        final MBButton savesButton = new MBButton("Saves", this, "toggle");
        ((TextButton)savesButton.getButton()).getLabel().setFontScale(1f, .9f);
        savesButton.setPosition(skillsButton.getX()+ skillsButton.getWidth()+2, statsPanel.getY()+ statsPanel.getHeight()-20);
        savesButton.setSize(80, 15);

        statsPanel.add(skillsButton);
        statsPanel.add(savesButton);

        if(statTab == 1){
            skillsButton.getButton().setChecked(true);
            skillsButton.getButton().setTouchable(Touchable.disabled);
            savesButton.getButton().setChecked(false);
        }
        else{
            savesButton.getButton().setChecked(true);
            savesButton.getButton().setTouchable(Touchable.disabled);
            skillsButton.getButton().setChecked(false);
        }

        skillsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(statTab == 2){
                    statTab = 1;
                    skillsPanel.setSoftVisible(true);
                    savesPanel.setSoftVisible(false);
                    skillsButton.getButton().setTouchable(Touchable.disabled);
                    savesButton.getButton().setTouchable(Touchable.enabled);
                }
                savesButton.getButton().setChecked(false);

                return false;
            }
        });

        savesButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(statTab == 1){
                    statTab = 2;
                    savesPanel.setSoftVisible(true);
                    skillsPanel.setSoftVisible(false);
                    skillsButton.getButton().setTouchable(Touchable.enabled);
                    savesButton.getButton().setTouchable(Touchable.disabled);
                }
                skillsButton.getButton().setChecked(false);

                return false;
            }
        });

        skillsButton.setFocused(true);
        savesButton.setFocused(true);

        //endregion
        //endregion

        //adding to the Reminders panel as its components
        reminderPanel.add(statsPanel);
        reminderPanel.add(reminderTextArea);
        reminderPanel.add(reminderLabel);
        //endregion

        //region General Stats
        //region listpanel
        //creating a list panel to hold all the items and adding it to the genstats panel
        Minipanel listPanel = new Minipanel("assets\\Panels\\ListPanel.png",
                new Rectangle(120, 560, 470, 300), this);

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

        genStatsPanel.add(listPanel);
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
//      ((TextButton)lrButton.button).getLabel().setColor(new Color(0x8a8a8aff));

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
        screenDropdown = new MBSelectBox(this);
        screenDropdown.setSize(300, 40);
        screenDropdown.setPosition(topPanel.getX()+ topPanel.getWidth()-305, topPanel.getY()+5);
        screenDropdown.setItems("PLAYER 1", "<ADD PLAYER>");

        screenDropdown.addScrollPaneListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
            if(screenDropdown.dropdown.getSelected().equals("<ADD PLAYER>")){
                Main.getMainScreen().addScreen();
            }
            else Main.getMainScreen().setSelectedScreen(Main.getMainScreen().getScreenByName(screenDropdown.dropdown.getSelected()));
            }
        });

        setName(player);

        final MBLabel playerNameLabel = new MBLabel(name, this);
        playerNameLabel.setPosition(topPanel.getX() + 10, topPanel.getY() + (topPanel.getHeight()/2) - (playerNameLabel.getHeight()/2));

        final MBButton playerNameButton = new MBButton(name, this);
        playerNameButton.setSize(playerNameLabel.getWidth() + 40, playerNameLabel.getHeight() + 10);
        playerNameButton.setPosition(playerNameLabel.getX() - 5, playerNameLabel.getY() - 5);
        ((TextButton)playerNameButton.getButton()).getLabel().setAlignment(Align.left);
        playerNameButton.aFloat = 0;

        final MBTextField playerNameTF = new MBTextField(name, this);
        playerNameTF.setSize(playerNameButton.getWidth(), playerNameButton.getHeight());
        playerNameTF.setPosition(playerNameButton.getX(), playerNameButton.getY());
        playerNameTF.setVisible(false);

        final boolean[] changingName = new boolean[]{false};
        playerNameTF.setKeyListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == '\n'){
                    System.out.println("hey again");

                    String newName = playerNameTF.getText();

                    setName(newName);
                    ((TextButton)playerNameButton.getButton()).setText(newName);
                    playerNameLabel.setText(newName);

                    Main.getMainScreen().syncScreens();

                    playerNameTF.setVisible(false);
                    changingName[0] = false;
                }
            }
        });

        playerNameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(!changingName[0]) {
                    System.out.println("Got me");
                    playerNameTF.setText(getName());
                    playerNameTF.setVisible(true);
                    changingName[0] = true;
                }
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the button...
                if(playerNameButton.getButton().isOver()){
                    playerNameButton.aFloat = 1;
                }
                else playerNameButton.aFloat = 0;
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });

        topPanel.add(playerNameLabel);
        topPanel.add(playerNameButton);
        topPanel.add(playerNameTF);
        topPanel.add(screenDropdown, 1);

        playerNameLabel.setFocused(true);
        screenDropdown.setFocused(true);
        //endregion

        //region MasterBoard
        masterBoard = new MBBoard(this);
        masterBoard.setPosition(masterboardPanel.getX()+1, masterboardPanel.getY()+1);
        masterBoard.setSize(masterboardPanel.getWidth()-2, masterboardPanel.getHeight()-2);
        masterboardPanel.add(masterBoard);

        //endregion
    }
}

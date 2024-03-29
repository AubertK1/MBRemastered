package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.project.Components.*;
import com.mygdx.project.Panels.*;

import java.util.ArrayList;

public class PlayerScreen extends Screen{
    //weapons or spell items for the itempanel
    private int itemTab = 1;
    private int statTab = 1;
    private ArrayList<ItemPanel> itemPanels = new ArrayList<>();

    public PlayerScreen(final String playerName) {
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


        //region Reminders
        //creating a label
        MBLabel reminderLabel = new MBLabel("REMINDERS", this);
        reminderLabel.setSize(375, 40);
        reminderLabel.setPosition(120, 490);

        //creating a textarea
        MBTextArea reminderTextArea;
        reminderTextArea = new MBTextArea("", this, Stats.Stat.REM);
        reminderTextArea.setSize(375,330);
        reminderTextArea.setPosition(120,160);

        //region imagebutton
        //creating the imageButton as a text button
        final MBButton imageButton = new MBButton(this);
        imageButton.setSize(290, 370);
        imageButton.setPosition(reminderPanel.getX() + reminderPanel.getWidth() - imageButton.getWidth() - 5, reminderTextArea.getY());
        //setting the default opacity
        imageButton.setOpacity(.5f);

        imageButton.toTextButton("ADD IMAGE");
        imageButton.setupSelectImageTextButton();

        imageButton.setFocused(true);
        //endregion

        //region misc.
        final Minipanel hpPanel, tempHPPanel, acPanel, bonusACPanel, speedPanel, initPanel;
        hpPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(reminderTextArea.getRightX() + 5, reminderPanel.getTopY() - 60 - 10, 90, 57), this);
        tempHPPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(hpPanel.getX(), hpPanel.getY() - hpPanel.getHeight() - 5, hpPanel.getWidth(), hpPanel.getHeight()), this);
        acPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(hpPanel.getX(), tempHPPanel.getY() - hpPanel.getHeight() - 5, hpPanel.getWidth(), hpPanel.getHeight()), this);
        bonusACPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(hpPanel.getX(), acPanel.getY() - hpPanel.getHeight() - 5, hpPanel.getWidth(), hpPanel.getHeight()), this);
        speedPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(hpPanel.getX(), bonusACPanel.getY() - hpPanel.getHeight() - 5, hpPanel.getWidth(), hpPanel.getHeight()), this);
        initPanel = new Minipanel("assets\\Panels\\minipanel2.png",
                new Rectangle(hpPanel.getX(), speedPanel.getY() - hpPanel.getHeight() - 5, hpPanel.getWidth(), hpPanel.getHeight()), this);

        //creating the labels to put in the stats' minipanels
        MBLabel hpL = new MBLabel("HP", this);
        hpL.setPosition(hpPanel.getX() + (hpPanel.getWidth()/2) - (hpL.getWidth()/2), hpPanel.getTopY() - hpL.getHeight());
        MBLabel tempHPL = new MBLabel("Temp HP", this);
        tempHPL.setPosition(tempHPPanel.getX() + (tempHPPanel.getWidth()/2) - (tempHPL.getWidth()/2), tempHPPanel.getTopY() - tempHPL.getHeight());
        MBLabel acL = new MBLabel("AC", this);
        acL.setPosition(acPanel.getX() + (acPanel.getWidth()/2) - (acL.getWidth()/2), acPanel.getTopY() - acL.getHeight());
        MBLabel bonusACL = new MBLabel("Bonus AC", this);
        bonusACL.setPosition(bonusACPanel.getX() + (bonusACPanel.getWidth()/2) - (bonusACL.getWidth()/2), bonusACPanel.getTopY() - bonusACL.getHeight());
        MBLabel speedL = new MBLabel("Speed", this);
        speedL.setPosition(speedPanel.getX() + (speedPanel.getWidth()/2) - (speedL.getWidth()/2), speedPanel.getTopY() - speedL.getHeight());
        MBLabel initL = new MBLabel("Initiative", this);
        initL.setPosition(initPanel.getX() + (initPanel.getWidth()/2) - (initL.getWidth()/2), initPanel.getTopY() - initL.getHeight());

        //creating the textfields to put in the stats' minipanels
        MBTextField hpTF = new MBTextField("", this, Stats.Stat.HP);
        //size and positions set by eyeballing until it looked nice
        hpTF.setSize(80, 33);
        hpTF.setPosition(hpPanel.getX() + 5, hpPanel.getY() + 5);
        hpTF.getTextField().setAlignment(Align.center);
        MBTextField tempHPTF = new MBTextField("", this, Stats.Stat.THP);
        tempHPTF.setSize(80, hpTF.getHeight());
        tempHPTF.setPosition(tempHPPanel.getX() + 5, tempHPPanel.getY() + 5);
        tempHPTF.getTextField().setAlignment(Align.center);
        MBTextField acTF = new MBTextField("", this, Stats.Stat.AC);
        acTF.setSize(80, hpTF.getHeight());
        acTF.setPosition(acPanel.getX() + 5, acPanel.getY() + 5);
        acTF.getTextField().setAlignment(Align.center);
        MBTextField bonusACTF = new MBTextField("", this, Stats.Stat.BAC);
        bonusACTF.setSize(80, hpTF.getHeight());
        bonusACTF.setPosition(bonusACPanel.getX() + 5, bonusACPanel.getY() + 5);
        bonusACTF.getTextField().setAlignment(Align.center);
        MBTextField speedTF = new MBTextField("", this, Stats.Stat.SPD);
        speedTF.setSize(80, hpTF.getHeight());
        speedTF.setPosition(speedPanel.getX() + 5, speedPanel.getY() + 5);
        speedTF.getTextField().setAlignment(Align.center);
        MBTextField initTF = new MBTextField("", this, Stats.Stat.INI);
        initTF.setSize(80, hpTF.getHeight());
        initTF.setPosition(initPanel.getX() + 5, initPanel.getY() + 5);
        initTF.getTextField().setAlignment(Align.center);

        //adding components to their minipanels
        hpPanel.add(hpTF);
        hpPanel.add(hpL);
        tempHPPanel.add(tempHPTF);
        tempHPPanel.add(tempHPL);
        acPanel.add(acTF);
        acPanel.add(acL);
        bonusACPanel.add(bonusACTF);
        bonusACPanel.add(bonusACL);
        speedPanel.add(speedTF);
        speedPanel.add(speedL);
        initPanel.add(initTF);
        initPanel.add(initL);
        //endregion

        reminderPanel.add(imageButton);
        reminderPanel.add(reminderTextArea);
        reminderPanel.add(hpPanel);
        reminderPanel.add(tempHPPanel);
        reminderPanel.add(acPanel);
        reminderPanel.add(bonusACPanel);
        reminderPanel.add(speedPanel);
        reminderPanel.add(initPanel);
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
        itemPanels.add(weaponsPanel);
        itemPanels.add(spellsPanel);
        weaponsPanel.setFocused(true);
        spellsPanel.setFocused(true);

        //making the first WEAPON item and assigning it to the first spot
        Item weaponItem1 = new WeaponItem(this);
        //making the first SPELL item and assigning it to the first spot
        Item spellItem1 = new SpellItem(this);

        weaponsPanel.add(weaponItem1);
        spellsPanel.add(spellItem1);

        if(itemTab == 1){
            weaponsPanel.setVisible(true);
            spellsPanel.setVisible(false);
        }
        if(itemTab == 2){
            weaponsPanel.setVisible(false);
            spellsPanel.setVisible(true);
        }
        //endregion

        //region item tab buttons
        final MBButton weaponsButton = new MBButton("Weapons", this, "tab-toggle");
        ((TextButton)weaponsButton.getButton()).getLabel().setFontScale(.92f, .9f);
        weaponsButton.setPosition(listPanel.getX()+5, listPanel.getY()+ listPanel.getHeight()-20);
        weaponsButton.setSize(80, 15);

        final MBButton spellsButton = new MBButton("Spells", this, "tab-toggle");
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
                    weaponsPanel.setVisible(true);
                    spellsPanel.setVisible(false);
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
                    spellsPanel.setVisible(true);
                    weaponsPanel.setVisible(false);
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

        //region stats bar
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
        Minipanel[] statpanels = new Minipanel[]{strPanel, dexPanel, conPanel, intPanel, wisPanel, chaPanel};
        //setting the layout for the stats panels
        for (int i = 0; i < statpanels.length; i++) {
            Minipanel panel = statpanels[i];
            String[] lblTexts = new String[]{"STR", "DEX", "CON", "INT", "WIS", "CHA"};

            MBLabel lbl = new MBLabel(lblTexts[i], this);
            lbl.setPosition(panel.getX() + (panel.getWidth()/2) - (lbl.getWidth()/2), 903);

            final MBTextField tf = new MBTextField("10", this, Stats.statIndexToStat(Stats.basestats, i), true, true);
            tf.setSize(42, 35);
            tf.setPosition(panel.getX() + 4, panel.getY() + 3);
            tf.getTextField().setAlignment(Align.center);
            tf.setVisible(false);

            final MBButton btn = new MBButton(this);
            btn.setSize(panel.getWidth(), panel.getHeight());
            btn.setPosition(panel.getX(), panel.getY());
            btn.setOpacity(0);

            final MBLabel mod = new MBLabel("0", this);
            mod.setSize(42, 29);
            mod.setPosition(panel.getX() + 4, panel.getY() + 11);
            mod.setAlignment(Align.center);
            mod.getLabel().setFontScale(1.25f);

            final MBLabel num = new MBLabel("10", this);
            num.setSize(42, 5);
            num.setPosition(panel.getX() + 4, panel.getY() + 5);
            num.setAlignment(Align.center);
            num.getLabel().setFontScale(.75f);

            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    tf.setVisible(true);
                    mod.setVisible(false);
                    num.setVisible(false);
                    btn.getButton().setTouchable(Touchable.disabled);
                }
                @Override
                public boolean handle (Event event) {
                    //if the mouse is hovered over the button...
                    if(btn.getButton().isOver()){
                        btn.setOpacity(.35f);
                    }
                    else btn.setOpacity(0);
                    if (!(event instanceof ChangeEvent)) return false;
                    changed((ChangeEvent)event, event.getTarget());
                    return false;
                }
            });
            final MBSystem sys = new MBSystem(tf, btn, lbl);
            sys.setUpdateAction(new Action() {
                @Override
                public boolean act(float v) {
                    int rawNum = Stats.findNumber(tf.getText());
                    float modNumF = (rawNum - 10) / 2f;
                    int modNum = (int) Math.floor(modNumF);
                    num.setText(String.valueOf(rawNum));
                    mod.setText(modNum > 0 ? "+" + modNum : String.valueOf(modNum));
                    tf.setStatValue(rawNum);

                    return true;
                }
            });
            tf.setClosingAction(new Action() {
                @Override
                public boolean act(float v) {
                    tf.setVisible(false);
                    mod.setVisible(true);
                    num.setVisible(true);
                    btn.getButton().setTouchable(Touchable.enabled);

                    sys.update();
                    return false;
                }
            });

            panel.add(tf);
            panel.add(mod);
            panel.add(num);
            panel.add(lbl);
            panel.add(btn);
        }
        //endregion

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
        ((TextButton)srButton.getButton()).getLabel().setColor(new Color(0x8a8a8aff));

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
        ((TextButton)lrButton.getButton()).getLabel().setColor(new Color(0x8a8a8aff));

        shortRestPanel.add(srButton);
        longRestPanel.add(lrButton);

        strPanel.setFocused(true);
        dexPanel.setFocused(true);
        conPanel.setFocused(true);
        intPanel.setFocused(true);
        wisPanel.setFocused(true);
        chaPanel.setFocused(true);
        //endregion

        //region skills/saves
        Minipanel statsPanel = new Minipanel("assets\\Panels\\StatsPanel.png",
                new Rectangle(595, 560,
                        290, genStatsPanel.getHeight() - 20), this);
        statsPanel.setPosition(genStatsPanel.getX() + genStatsPanel.getWidth() - statsPanel.getWidth() - 5, statsPanel.getY());

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

        String[] skills = Stats.skills;

        for (int i = 0; i < skills.length; i++) {
            if(i == 0){ //initializing the fist skill
                float skillHeight = (skillsPanel.getHeight() - 40) / 9f;
                skillsPanel.add(new SkillItem(new Rectangle(skillsPanel.getX(), skillsPanel.getY() + skillsPanel.getHeight() - skillHeight, (skillsPanel.getWidth() - 2.5f) / 2f, skillHeight),
                        this, skills[0], Stats.statIndexToStat(Stats.skills, i)));
            }
            else skillsPanel.add(new SkillItem(this, skills[i], Stats.statIndexToStat(Stats.skills, i)));
        }

        savesPanel.setRows(6);

        String[] saves = new String[]{"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};

        for (int i = 0; i < saves.length; i++) {
            if(i == 0){ //initializing the fist skill
                float saveHeight = (savesPanel.getHeight() - 25) / 6f;
                savesPanel.add(new SkillItem(new Rectangle(savesPanel.getX(), savesPanel.getY() + savesPanel.getHeight() - saveHeight, savesPanel.getWidth(), saveHeight),
                        this, saves[0], Stats.statIndexToStat(Stats.saves, i)));
            }
            else savesPanel.add(new SkillItem(this, saves[i], Stats.statIndexToStat(Stats.saves, i)));
        }

        if(statTab == 1){
            skillsPanel.setVisible(true);
            savesPanel.setVisible(false);
        }
        if(statTab == 2){
            skillsPanel.setVisible(false);
            savesPanel.setVisible(true);
        }
        //endregion

        //region stats tab buttons
        final MBButton skillsButton = new MBButton("Skills", this, "tab-toggle");
        ((TextButton)skillsButton.getButton()).getLabel().setFontScale(.92f, .9f);
        skillsButton.setPosition(statsPanel.getX()+5, statsPanel.getY()+ statsPanel.getHeight()-20);
        skillsButton.setSize(80, 15);

        final MBButton savesButton = new MBButton("Saves", this, "tab-toggle");
        ((TextButton)savesButton.getButton()).getLabel().setFontScale(1f, .9f);
        savesButton.setPosition(skillsButton.getX()+ skillsButton.getWidth() + 2, statsPanel.getY()+ statsPanel.getHeight()-20);
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
                    skillsPanel.setVisible(true);
                    savesPanel.setVisible(false);
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
                    savesPanel.setVisible(true);
                    skillsPanel.setVisible(false);
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

        genStatsPanel.add(listPanel);
        genStatsPanel.add(strPanel);
        genStatsPanel.add(dexPanel);
        genStatsPanel.add(conPanel);
        genStatsPanel.add(intPanel);
        genStatsPanel.add(wisPanel);
        genStatsPanel.add(chaPanel);
        genStatsPanel.add(shortRestPanel);
        genStatsPanel.add(longRestPanel);
        genStatsPanel.add(statsPanel);
        //endregion

        //region Top Bar
        screenDropdown = new MBSelectBox(this);
        screenDropdown.setSize(300, 40);
        screenDropdown.setPosition(topPanel.getX()+ topPanel.getWidth()-305, topPanel.getY()+5);
        screenDropdown.setItems("<ADD PLAYER>");

        screenDropdown.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
            if(screenDropdown.getDropdown().getSelected().equals("<ADD PLAYER>")){
                Main.getMainScreen().addScreen();
            }
            else Main.getMainScreen().setSelectedScreen(Main.getMainScreen().getScreenByName(screenDropdown.getDropdown().getSelected()));
            }
        });

        setName(playerName);

        //region name

        final MBLabel playerNameLabel = new MBLabel(name, this);
        playerNameLabel.setPosition(topPanel.getX() + 10, topPanel.getY() + (topPanel.getHeight()/2) - (playerNameLabel.getHeight()/2));
        playerNameLabel.setSize(66, playerNameLabel.getHeight());

        final MBButton playerNameButton = new MBButton(name, this);
        playerNameButton.setSize(playerNameLabel.getWidth() + 100, playerNameLabel.getHeight() + 10);
        playerNameButton.setPosition(playerNameLabel.getX() - 5, playerNameLabel.getY() - 5);
        ((TextButton)playerNameButton.getButton()).getLabel().setAlignment(Align.left);
        playerNameButton.setOpacity(0);

        final MBTextField playerNameTF = new MBTextField(name, this, Stats.Stat.NAME,true, true);
        playerNameTF.setSize(playerNameButton.getWidth(), playerNameButton.getHeight());
        playerNameTF.setPosition(playerNameButton.getX(), playerNameButton.getY());
        playerNameTF.setVisible(false);

        final MBSystem closeSystem = new MBSystem(playerNameTF, playerNameButton, playerNameLabel);
        closeSystem.setUpdateAction(new Action() {
            @Override
            public boolean act(float v) {
                String newName = playerNameTF.getText();

                setName(newName);
                ((TextButton)playerNameButton.getButton()).setText(SpellItem.shortenString(newName, playerNameButton.getWidth()));
                playerNameLabel.setText(SpellItem.shortenString(newName, playerNameButton.getWidth()));

                return true;
            }
        });
        final boolean[] changingName = new boolean[]{false};
        playerNameTF.setClosingAction(new Action() {
            @Override
            public boolean act(float v) {
                closeSystem.update();

                Main.getMainScreen().syncScreens();

                playerNameTF.setVisible(false);
                changingName[0] = false;
                return false;
            }
        });

        playerNameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(!changingName[0]) {
                    playerNameTF.setText(getName());
                    playerNameTF.setVisible(true);
                    changingName[0] = true;
                }
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the button...
                if(playerNameButton.getButton().isOver()){
                    playerNameButton.setOpacity(1);
                }
                else playerNameButton.setOpacity(0);
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });
        //endregion

        MBButton delButt = new MBButton(this, "delete-button");
        delButt.setPosition(playerNameButton.getRightX(), playerNameButton.getY());
        delButt.setSize(playerNameButton.getHeight(), playerNameButton.getHeight());
        delButt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Main.getMainScreen().deleteScreen(PlayerScreen.this);
            }
        });

        //region character stats
        String[] lblTxts = new String[]{"LVL", "PRF", "CLS", "RCE"};
        MBLabel[] lbls = new MBLabel[lblTxts.length];

        for (int i = 0; i < lblTxts.length; i++) {
            final MBLabel lbl = new MBLabel(lblTxts[i], this);
            lbls[i] = lbl;
            lbl.setPosition(i == 0 ? playerNameTF.getRightX() + 75 : lbls[i-1].getRightX() + 30, topPanel.getTopY() - 5 - (lbl.getHeight()));
            lbl.setAlignment(Align.center);
            lbl.getLabel().setFontScale(.9f);

            final MBLabel mod = new MBLabel("0", this);
            mod.setPosition(lbl.getX(), topPanel.getY() + 2);
            mod.setSize(lbl.getWidth(), lbl.getHeight());
            mod.setAlignment(Align.center);
            mod.getLabel().setFontScale(1.25f);

            final MBButton btn = new MBButton(this);
            btn.setSize(lbl.getWidth() + 6, lbl.getTopY() - mod.getY());
            btn.setPosition(lbl.getX() - 3, mod.getY());
            btn.setOpacity(0);

            final MBTextField tf = new MBTextField("0", this, Stats.statIndexToStat(Stats.charstats, i), true, true);
            tf.setSize(mod.getWidth(), mod.getHeight());
            tf.setPosition(mod.getX(), mod.getY());
            tf.setAlignment(Align.center);
            tf.setVisible(false);

            final boolean[] changingPrfncy = new boolean[]{false};
            final MBSystem sys = new MBSystem(tf, mod);
            sys.setUpdateAction(new Action() {
                @Override
                public boolean act(float v) {
                    int newMod = Stats.findNumber(tf.getText());

                    mod.setText(String.valueOf(newMod));

                    return true;
                }
            });
            tf.setClosingAction(new Action() {
                @Override
                public boolean act(float v) {
                    sys.update();

                    tf.setVisible(false);
                    changingPrfncy[0] = false;
                    return false;
                }
            });

            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if(!changingPrfncy[0]) {
                        tf.setVisible(true);
                        changingPrfncy[0] = true;
                    }
                }
                @Override
                public boolean handle (Event event) {
                    //if the mouse is hovered over the button...
                    if(btn.getButton().isOver()){
                        btn.setOpacity(.35f);
                    }
                    else btn.setOpacity(0);
                    if (!(event instanceof ChangeEvent)) return false;
                    changed((ChangeEvent)event, event.getTarget());
                    return false;
                }
            });

            topPanel.add(lbl);
            topPanel.add(mod);
            topPanel.add(btn);
            topPanel.add(tf);
        }
        //endregion

        topPanel.add(playerNameLabel);
        topPanel.add(playerNameButton);
        topPanel.add(playerNameTF);
        topPanel.add(delButt);
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
    public void save(){
        for (int i = 0; i < itemPanels.size(); i++) {
            itemPanels.get(i).saveItems(i);
        }

        stats.save();
        masterBoard.getBoard().save();
    }
    public void load(){
        stats.load();
        masterBoard.getBoard().load();

        boolean[] loadedIPs = new boolean[]{false, false}; // {isWeaponIPLoaded, isSpellIPLoaded}
        for (int i = 0; i < allComps.size(); i++) {
            if(allComps.get(i) instanceof MBTextField) {
                MBTextField textField = (MBTextField) allComps.get(i);
                if(textField.getAssignedStat() != null){
                    if(!loadedIPs[0] && textField.getAssignedStat() == 100){
                        ((Item) textField.getParentPanel()).getParentIP().loadItems(0);
                        loadedIPs[0] = true;
                        i--;
                    }
                    else if(!loadedIPs[1] && textField.getAssignedStat() == 1000){
                        ((Item) textField.getParentPanel()).getParentIP().loadItems(1);
                        loadedIPs[1] = true;
                        i--;
                    }
                    //setting the text of the tfs
                    textField.setText(String.valueOf(stats.getValue(textField.getAssignedStat())));
                    //updating any buttons or labels associated with the tf
                    textField.updateSystem();
                }
            }
            else if(allComps.get(i) instanceof MBTextArea) {
                MBTextArea textArea = (MBTextArea) allComps.get(i);
                if(textArea.getAssignedStat() != null){
                    textArea.setText(String.valueOf(stats.getValue(textArea.getAssignedStat())));
                }
            }
            else if(allComps.get(i) instanceof MBButton && allComps.get(i).getName().equals("image button")){
                MBButton imgbutton = (MBButton) allComps.get(i);
                String path = String.valueOf(stats.getValue(Stats.Stat.IMGFILEPATH));
                if(!path.equals("")) {
                    Main.fileChooserPath = path;
                    fileChooseHandle(imgbutton.getParentPanel(), imgbutton); //this moves the button further down the list...
                    if(allComps.indexOf(imgbutton) != i) i--; //...so we have to go back one so we can load the MBComp that was slide into this index
                }
            }
        }
    }
    public MBBoard getMBBoard(){
        return masterBoard;
    }
    public int getItemTab(){
        return itemTab;
    }
}

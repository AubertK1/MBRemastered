package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import static com.mygdx.project.Main.batch;

public class Item extends Minipanel{
    //skin of the item
    Skin uiSkin = new Skin (Gdx.files.internal(
        "assets\\skins\\uiskin.json"));
    //strings for the labels (if weapon)
    String name, hitDie, mod, type;
    ArrayList<String> names = new ArrayList<>();
    //textfields for when you edit the item (if weapon)
    MBTextField nameLabelTF, hitDieLabelTF, modLabelTF, typeLabelTF;
    ArrayList<MBTextField> textFields = new ArrayList<>();
    //labels for the item (if weapon)
    ArrayList<MBLabel> labels = new ArrayList<>();
    ArrayList<Tipbox> tipboxes = new ArrayList<>();
    int itemType;

    int usesIndexInNames = -1;
    private MBButton usesButton;
    final int[] uses = {0};
    public int srMax = 0;
    public int lrMax = 0;
    ArrayList<MBButton> restButtons = new ArrayList<>();

    public Item(int itemType, int spot) {
        super("core\\pics\\MBSkin2\\ItemPanel4.png", new Rectangle(125, 790, 460, 40));
        this.itemType = itemType;
        //if this item is a weapon it sets it up as a weapon item
        if(itemType == 1){
            makeWeaponItem(spot);
        }
        if(itemType == 2){
            makeSpellItem(spot);
        }
    }

    /**
     * sets up the item as a weapon item
     * @param spot its initial spot
     */
    public void makeWeaponItem(int spot){
        //assigning this item's spot to the given spot
        this.wSpot =spot;
        //increasing the total number of items by one (this item's ID was already set when it was created (code in panel class))
        totalWID++;
        //increasing the next available spot by one
        nextAvaWSpot++;
        //creating the labels
        MBLabel nameLabel,diceLabel,modLabel,typeLabel;
        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Weapon "+ (Panel.totalWID), uiSkin);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        names.add(nameLabel.label.getText().toString());

        diceLabel = new MBLabel("HitDie", uiSkin);
        diceLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        diceLabel.setSize(75, nameLabel.getHeight());
        names.add(diceLabel.label.getText().toString());

        modLabel = new MBLabel("ATKMod", uiSkin);
        modLabel.setPosition(diceLabel.getX()+ diceLabel.getWidth()+2, nameLabel.getY());
        modLabel.setSize(85, nameLabel.getHeight());
        names.add( modLabel.label.getText().toString());

        typeLabel = new MBLabel("Damage/Type", uiSkin);
        typeLabel.setPosition(modLabel.getX()+ modLabel.getWidth()+2, nameLabel.getY());
        typeLabel.setSize(115, nameLabel.getHeight());
        names.add( typeLabel.label.getText().toString());

        labels.add(nameLabel);
        labels.add(diceLabel);
        labels.add(modLabel);
        labels.add(typeLabel);
        //creating buttons and setting their positions and sizes
        final MBButton itemButtonEdit = new MBButton(uiSkin, "edit-toggle");
        itemButtonEdit.button.setChecked(true);
        itemButtonEdit.setName("editbutton");
        itemButtonEdit.button.setName("editbutton");
        itemButtonEdit.setPosition((typeLabel.getX()+typeLabel.getWidth()+10), nameLabel.getY()-1);
        itemButtonEdit.setSize(20, 15);

        final MBButton itemButtonDel = new MBButton(uiSkin, "delete-button");
        itemButtonDel.setPosition((typeLabel.getX()+typeLabel.getWidth()+10), itemButtonEdit.getY()+itemButtonEdit.getHeight()+2);
        itemButtonDel.setSize(20, 15);

        final MBButton itemButtonDown = new MBButton(uiSkin, "down-button");
        itemButtonDown.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonEdit.getY());
        itemButtonDown.setSize(20, 15);

        final MBButton itemButtonUp = new MBButton(uiSkin, "up-button");
        itemButtonUp.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonDown.getY()+itemButtonDown.getHeight()+2);
        itemButtonUp.setSize(20, 15);
        //setting the textfields' values
        for (int i = 0; i < names.size(); i++) {
            textFields.add(new MBTextField(names.get(i), uiSkin));
            //detecting when enter is pressed on each MBTextField so that enter can exit out of edit mode
            textFields.get(i).setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    if(c == '\n'){
                        System.out.println("hey");
                        saveEdit();
                        editMode = false;
                    }
                }
            });
        }
        //adding all the components to this item's components
        add(nameLabel);
        add(diceLabel);
        add(modLabel);
        add(typeLabel);
        add(itemButtonEdit);
        add(itemButtonDel);
        add(itemButtonDown);
        add(itemButtonUp);
        //starting the item in edit mode so the user can immediately edit the item text
        editMode = true;
        edit();

        //setting the buttons' functions
        //changes this item in and out of edit mode
        itemButtonEdit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Edit Button " + (itemButtonEdit.getItem().wID +1));
                if(!editMode) {
                    edit();
                    editMode = true;
                }
                else{
                    saveEdit();
                    editMode = false;
                }
            }
        });
        //fixme still need to work on this button
        itemButtonDel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Delete Button " + (itemButtonDel.getItem().wID +1));

                int currSpot = itemButtonDown.getItem().wSpot;
                shuffleItemsUp(currSpot);

                for (int i = itemButtonDel.getItem().components.size()-1; i >= 0; i--) {
                    itemButtonDel.getItem().delete(itemButtonDel.getItem().components.get(i));
                }
                itemButtonDel.getItem().getPanel().remove(itemButtonDel.getItem());

            }
        });
        //swaps this item with the item under it
        itemButtonDown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Down Button " + (itemButtonDown.getItem().wID +1));
                //initializing spots into set temporary variables
                int currSpot = itemButtonDown.getItem().wSpot;
                int nextSpot = itemButtonDown.getItem().wSpot + 1;
                //if it's not at the bottom...
                if (nextSpot < nextAvaWSpot) {
                    //reassigns the spots to different variables
                    //assigns this item's spot to an arbitrary number so that the two items are never at the same spot
                    itemButtonDown.getItem().wSpot = -100;
                    itemButtonDown.getItem().getPanel().getItemBySpot(nextSpot).wSpot = currSpot;
                    itemButtonDown.getItem().wSpot = nextSpot;
                    //making it easier to read
                    ArrayList<MBComponent> thisItemComponents = itemButtonDown.getItem().components;
                    //making it easier to read
                    ArrayList<MBComponent> nextItemComponents = itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).components;

                    int smallerListSize = Math.min(thisItemComponents.size(), nextItemComponents.size());
                    //saving this item's components' positions before I change them, so I can use there later
                    ArrayList<Float> oldYs = new ArrayList<>();
                    for(int i = 0; i < smallerListSize; i++) {
                        oldYs.add(thisItemComponents.get(i).getY());
                    }
                    //repositioning this item to its new spot
                    //looping through the list of this item's components and assigning their positions to the next item's components' positions
                    for (int i = 0; i < smallerListSize; i++) {
                        thisItemComponents.get(i).setPosition(nextItemComponents.get(i).getX(), nextItemComponents.get(i).getY());
                    }
                    //looping through the list of the next item's components and assigning their positions to this item's components' old positions
                    for (int i = 0; i < smallerListSize; i++) {
                        nextItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), oldYs.get(i));
                    }
                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).getEditMode()){
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).saveEdit();
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).edit();
                    }
                }
            }
        });
        itemButtonUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Up Button "+(itemButtonUp.getItem().wID +1));
                //initializing spots into set temporary variables
                int currSpot = itemButtonUp.getItem().wSpot;
                int prevSpot = itemButtonUp.getItem().wSpot -1;
                //if it's not at the top...
                if(prevSpot>=0) {
                    //reassigns the spots to different variables
                    //assigns this item's spot to an arbitrary number so that the two items are never at the same spot
                    itemButtonUp.getItem().wSpot = -100;
                    itemButtonUp.getItem().getPanel().getItemBySpot(prevSpot).wSpot = currSpot;
                    itemButtonUp.getItem().wSpot = prevSpot;
                    //making it easier to read
                    ArrayList<MBComponent> thisItemComponents = itemButtonUp.getItem().components;
                    //making it easier to read
                    ArrayList<MBComponent> prevItemComponents = itemButtonUp.getItem().getPanel().getItemBySpot(currSpot).components;

                    int smallerListSize = Math.min(thisItemComponents.size(), prevItemComponents.size());
                    //saving this item's components' positions before I change them, so I can use there later
                    ArrayList<Float> oldYs = new ArrayList<>();
                    for(int i = 0; i < smallerListSize; i++) {
                        oldYs.add(thisItemComponents.get(i).getY());
                    }
                    //repositioning this item to its new spot
                    //looping through the list of this item's components and assigning their positions to the next item's components' positions
                    for (int i = 0; i < smallerListSize; i++) {
                        thisItemComponents.get(i).setPosition(prevItemComponents.get(i).getX(), prevItemComponents.get(i).getY());
                    }
                    //looping through the list of the previous item's components and assigning their positions to this item's components' old positions
                    for (int i = 0; i < smallerListSize; i++) {
                        prevItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), oldYs.get(i));
                    }
                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).getEditMode()){
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).saveEdit();
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).edit();
                    }
                }
            }
        });

    }
    public void makeSpellItem(int spot){
        //assigning this item's spot to the given spot
        this.sSpot =spot;
        //increasing the total number of items by one (this item's ID was already set when it was created (code in panel class))
        totalSID++;
        //increasing the next available spot by one
        nextAvaSSpot++;

//        final int[] uses = {0};
        //creating the labels
        MBLabel nameLabel,descLabel,usesLabel;
        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Spell  "+ (Panel.totalSID), uiSkin);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        names.add(nameLabel.label.getText().toString());

        descLabel = new MBLabel("Item Description...", uiSkin);
        descLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        descLabel.setSize(257, nameLabel.getHeight());
        descLabel.setName("tf");
        names.add(descLabel.label.getText().toString());

        usesLabel = new MBLabel(String.valueOf(uses[0]), uiSkin);
        usesLabel.setPosition(descLabel.getX()+ descLabel.getWidth()+2, nameLabel.getY());
        usesLabel.setSize(22, nameLabel.getHeight());
        names.add(usesLabel.label.getText().toString());
        usesIndexInNames = names.size()-1;

        labels.add(nameLabel);
        labels.add(descLabel);
        labels.add(usesLabel);

        //region tipbox components
        final Tipbox spellDesc = new Tipbox(new Rectangle(115, descLabel.getY()+ (descLabel.getHeight()/2)-300, 770, 300));
        MBTextArea spellDescTF = new MBTextArea("", uiSkin);
        spellDescTF.setPosition(spellDesc.getX()+10, spellDesc.getY()+10+35);
        spellDescTF.setSize(750, 225);
        MBLabel spellLevel = new MBLabel("Level: ",uiSkin);
        spellLevel.setPosition(spellDesc.getX()+10, spellDesc.getY()+10);
        MBTextField levelTF = new MBTextField("0", uiSkin);
        levelTF.setPosition(spellLevel.getX() + spellLevel.getWidth() +0, spellLevel.getY());
        levelTF.setSize(25, 30);
//        float ogMargin = levelTF.textField.getStyle().background.getLeftWidth();
//        levelTF.textField.getStyle().background.setLeftWidth(0);
//        levelTF.textField.getStyle().background.setLeftWidth(ogMargin);
//        levelTF.textField.setAlignment(Align.center);
        MBLabel castTime = new MBLabel("Casting Time: ", uiSkin);
        castTime.setPosition(levelTF.getX() + levelTF.getWidth() +10, spellLevel.getY());
        MBTextField castTimeTF = new MBTextField("Action", uiSkin);
        castTimeTF.setPosition(castTime.getX() + castTime.getWidth() +0, spellLevel.getY());
        castTimeTF.setSize(85, levelTF.getHeight());
        castTimeTF.textField.setAlignment(Align.center);
        MBLabel duration = new MBLabel("Duration: ", uiSkin);
        duration.setPosition(castTimeTF.getX() + castTimeTF.getWidth() +10, spellLevel.getY());
        MBTextField durationTF = new MBTextField("Instant", uiSkin);
        durationTF.setPosition(duration.getX() + duration.getWidth() +0, spellLevel.getY());
        durationTF.setSize(85, levelTF.getHeight());
        durationTF.textField.setAlignment(Align.center);
        MBLabel range = new MBLabel("Range: ", uiSkin);
        range.setPosition(durationTF.getX() + durationTF.getWidth() +10, spellLevel.getY());
        MBTextField rangeTF = new MBTextField("5 ft", uiSkin);
        rangeTF.setPosition(range.getX() + range.getWidth() +0, spellLevel.getY());
        rangeTF.setSize(50, levelTF.getHeight());
        rangeTF.textField.setAlignment(Align.center);
        MBLabel damageType = new MBLabel("Damage Type: ", uiSkin);
        damageType.setPosition(rangeTF.getX() + rangeTF.getWidth() +10, spellLevel.getY());
        MBTextField damageTypeTF = new MBTextField("Fire", uiSkin);
        damageTypeTF.setPosition(damageType.getX() + damageType.getWidth() +0, spellLevel.getY());
        damageTypeTF.setSize(75, levelTF.getHeight());
        damageTypeTF.textField.setAlignment(Align.center);

        spellDesc.add(spellDescTF);
        spellDesc.add(spellLevel);
        spellDesc.add(levelTF);
        spellDesc.add(castTime);
        spellDesc.add(castTimeTF);
        spellDesc.add(duration);
        spellDesc.add(durationTF);
        spellDesc.add(range);
        spellDesc.add(rangeTF);
        spellDesc.add(damageType);
        spellDesc.add(damageTypeTF);
        //endregion

        add(spellDesc);
        tipboxes.add(spellDesc);

        //creating buttons and setting their positions and sizes
        final MBButton usesButton = new MBButton(String.valueOf(uses[0]),uiSkin);
        usesButton.setName("usesbutton");
        usesButton.setPosition(usesLabel.getX(), usesLabel.getY());
        usesButton.setSize(usesLabel.getWidth(), 30);
        ((TextButton)usesButton.button).getLabel().setAlignment(Align.left);
        this.usesButton = usesButton;

        final MBButton minusButton = new MBButton(uiSkin, "down-button");
        minusButton.setPosition(usesButton.getX(), usesButton.getY());
        minusButton.setSize(usesButton.getWidth(), usesButton.getHeight()/2 - 1);
        minusButton.aFloat = 0;

        final MBButton plusButton = new MBButton(uiSkin, "up-button");
        plusButton.setPosition(usesButton.getX(), usesButton.getY()+ minusButton.getHeight()+1);
        plusButton.setSize(usesButton.getWidth(), usesButton.getHeight()/2 - 1);
        plusButton.aFloat = 0;

        final MBButton itemButtonEdit = new MBButton(uiSkin, "edit-toggle");
        itemButtonEdit.button.setChecked(true);
        itemButtonEdit.setName("editbutton");
        itemButtonEdit.button.setName("editbutton");
        itemButtonEdit.setPosition((usesLabel.getX()+usesLabel.getWidth()+8), nameLabel.getY()-1);
        itemButtonEdit.setSize(20, 15);

        final MBButton itemButtonDel = new MBButton(uiSkin, "delete-button");
        itemButtonDel.setPosition((usesLabel.getX()+usesLabel.getWidth()+8), itemButtonEdit.getY()+itemButtonEdit.getHeight()+2);
        itemButtonDel.setSize(20, 15);

        final MBButton itemButtonDown = new MBButton(uiSkin, "down-button");
        itemButtonDown.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonEdit.getY());
        itemButtonDown.setSize(20, 15);

        final MBButton itemButtonUp = new MBButton(uiSkin, "up-button");
        itemButtonUp.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonDown.getY()+itemButtonDown.getHeight()+2);
        itemButtonUp.setSize(20, 15);
        //setting the textfields' values
        for (int i = 0; i < labels.size(); i++) {
            textFields.add(new MBTextField(String.valueOf(labels.get(i).label.getText()), uiSkin));
            add(textFields.get(i));
            //detecting when enter is pressed on each MBTextField so that enter can exit out of edit mode
            textFields.get(i).setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    if(c == '\n'){
                        System.out.println("hey");
                        saveEdit();
                        editMode = false;
                    }
                }
            });
        }
        //adding all the components to this item's components
        add(nameLabel);
        add(descLabel);
        add(usesLabel);
        add(usesButton);
        add(itemButtonEdit);
        add(itemButtonDel);
        add(itemButtonDown);
        add(itemButtonUp);
        //starting the item in edit mode so the user can immediately edit the item text
        editMode = true;
        edit();
        //setting the buttons' functions
        //region Uses Button
        usesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("uses clicked");
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the usesButton...
                if (usesButton.getButton().isOver() || minusButton.getButton().isOver() || plusButton.getButton().isOver()) {
                    minusButton.aFloat = .5f;
                    plusButton.aFloat = .5f;
                    usesButton.aFloat = 1;
                }
                else {
                    minusButton.aFloat = 0;
                    plusButton.aFloat = 0;
                    usesButton.aFloat = 1;
                }
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });

        minusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("minus clicked " +(sID +1));
                uses[0]--;
                ((TextButton)usesButton.button).setText(String.valueOf(uses[0]));
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the usesButton...
                if (usesButton.getButton().isOver() || minusButton.getButton().isOver() || plusButton.getButton().isOver()) {
                    minusButton.aFloat = .5f;
                    plusButton.aFloat = .5f;
                    usesButton.aFloat = 1;
                }
                else {
                    minusButton.aFloat = 0;
                    plusButton.aFloat = 0;
                    usesButton.aFloat = 1;
                }
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });
        plusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("plus clicked "+(sID +1));
                uses[0]++;
                ((TextButton)usesButton.button).setText(String.valueOf(uses[0]));
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the usesButton...
                if (usesButton.getButton().isOver() || minusButton.getButton().isOver() || plusButton.getButton().isOver()) {
                    minusButton.aFloat = .5f;
                    plusButton.aFloat = .5f;
                    usesButton.aFloat = 1;
                }
                else {
                    minusButton.aFloat = 0;
                    plusButton.aFloat = 0;
                    usesButton.aFloat = 1;
                }
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });
        usesButton.add(minusButton);
        usesButton.add(plusButton);

        final MBButton srButton = new MBButton("Short Rest", uiSkin);
        srButton.button.setStyle(Main.uiSkin.get("toggle", TextButton.TextButtonStyle.class));
        srButton.button.setChecked(true);
        srButton.setSize(50, itemButtonEdit.getHeight());
        srButton.setPosition(usesButton.getX()-srButton.getWidth()-2, itemButtonDel.getY());
        ((TextButton)srButton.button).getLabel().setFontScale(.6f,.7f);

        final MBButton lrButton = new MBButton("Long Rest", uiSkin);
        lrButton.button.setStyle(Main.uiSkin.get("toggle", TextButton.TextButtonStyle.class));
        lrButton.button.setChecked(false);
        lrButton.setSize(50, itemButtonDel.getHeight());
        lrButton.setPosition(usesButton.getX()-srButton.getWidth()-2, itemButtonEdit.getY());
        ((TextButton)lrButton.button).getLabel().setFontScale(.6f,.7f);
        srButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("short rest " +(sID +1));
                //When the button is pressed while checked. Usually it would be if(button.isChecked), but button unchecks itself before this is called, so it's reversed
                if(!srButton.button.isChecked()){
                    lrButton.button.setChecked(true);
                }
                else if(srButton.button.isChecked()){
                    lrButton.button.setChecked(false);
                }
            }
        });
        lrButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("long rest "+(sID +1));
                //When the button is pressed while checked. Usually it would be if(button.isChecked), but button unchecks itself before this is called, so it's reversed
                if(!lrButton.button.isChecked()){
                    srButton.button.setChecked(true);
                }
                else if(lrButton.button.isChecked()){
                    srButton.button.setChecked(false);
                }
            }
        });
        srButton.setVisible(false);
        lrButton.setVisible(false);
        restButtons.add(srButton);
        restButtons.add(lrButton);
        add(srButton);
        add(lrButton);
        //endregion

        //changes this item in and out of edit mode
        itemButtonEdit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Edit Button " + (itemButtonEdit.getItem().sID +1));
                if(!editMode) {
                    edit();
                    editMode = true;
                }
                else{
                    saveEdit();
                    editMode = false;
                }
            }
        });
        itemButtonDel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Delete Button " + (itemButtonDel.getItem().sID +1));

                int currSpot = itemButtonDown.getItem().sSpot;
                shuffleItemsUp(currSpot);

//                itemButtonDel.getItem().delete(itemButtonDel.getItem().components.get(itemButtonDel.getItem().components.size()));
                for (int i = itemButtonDel.getItem().components.size()-1; i >= 0; i--) {
                    itemButtonDel.getItem().delete(itemButtonDel.getItem().components.get(i));
                }
                itemButtonDel.getItem().remove(spellDesc);
                itemButtonDel.getItem().getPanel().remove(itemButtonDel.getItem());

            }
        });
        //swaps this item with the item under it
        itemButtonDown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Down Button " + (itemButtonDown.getItem().sID +1));
                //initializing spots into set temporary variables
                int currSpot = itemButtonDown.getItem().sSpot;
                int nextSpot = itemButtonDown.getItem().sSpot + 1;
                //if it's not at the bottom...
                if (nextSpot < nextAvaSSpot) {
                    //reassigns the spots to different variables
                    //assigns this item's spot to an arbitrary number so that the two items are never at the same spot
                    itemButtonDown.getItem().sSpot = -100;
                    itemButtonDown.getItem().getPanel().getItemBySpot(nextSpot).sSpot = currSpot;
                    itemButtonDown.getItem().sSpot = nextSpot;
                    //making it easier to read
                    ArrayList<MBComponent> thisItemComponents = itemButtonDown.getItem().components;
                    //making it easier to read
                    ArrayList<MBComponent> nextItemComponents = itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).components;

                    ArrayList<Panel> thisItemTipbox = itemButtonUp.getItem().minipanels;
                    ArrayList<Panel> nextItemTipbox = itemButtonUp.getItem().getPanel().getItemBySpot(currSpot).minipanels;

                    int smallerListSize = Math.min(thisItemComponents.size(), nextItemComponents.size());
                    //saving this item's components' positions before I change them, so I can use them later
                    ArrayList<Float> oldYs = new ArrayList<>();
                    ArrayList<Float> restButtonsYs = new ArrayList<>();
                    for(int i = 0; i < smallerListSize; i++) {
                        oldYs.add(thisItemComponents.get(i).getY());
                        if (thisItemComponents.get(i).components.size() > 0){
                            restButtonsYs.add(thisItemComponents.get(i).components.get(0).getY());
                            restButtonsYs.add(thisItemComponents.get(i).components.get(1).getY());
                        }
                    }
                    ArrayList<Float> oldYs2 = new ArrayList<>();
                    for(int i = 0; i < thisItemTipbox.size(); i++) {
                        oldYs2.add(thisItemTipbox.get(i).getY());
                    }

                    //repositioning this item to its new spot
                    //looping through the list of this item's components and assigning their positions to the next item's components' positions
                    for (int i = 0; i < thisItemComponents.size(); i++) {
                        thisItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), thisItemComponents.get(i).getY() - (getHeight()+5));
                        if(thisItemComponents.get(i).components.size() > 0){
                            thisItemComponents.get(i).components.get(0).setPosition(thisItemComponents.get(i).components.get(0).getX(), thisItemComponents.get(i).components.get(0).getY() - (getHeight()+5));
                            thisItemComponents.get(i).components.get(1).setPosition(thisItemComponents.get(i).components.get(1).getX(), thisItemComponents.get(i).components.get(1).getY() - (getHeight()+5));
                        }
                    }
                    for (int i = 0; i < nextItemComponents.size(); i++) {
                        nextItemComponents.get(i).setPosition(nextItemComponents.get(i).getX(), nextItemComponents.get(i).getY() + (getHeight()+5));
                        if(nextItemComponents.get(i).components.size() > 0){
                            nextItemComponents.get(i).components.get(0).setPosition(nextItemComponents.get(i).components.get(0).getX(), nextItemComponents.get(i).components.get(0).getY() + (getHeight()+5));
                            nextItemComponents.get(i).components.get(1).setPosition(nextItemComponents.get(i).components.get(1).getX(), nextItemComponents.get(i).components.get(1).getY() + (getHeight()+5));
                        }
                    }


                    for (int i = 0; i < thisItemTipbox.size(); i++) {
                        thisItemTipbox.get(i).setPosition(thisItemTipbox.get(i).getX(), thisItemTipbox.get(i).getY() - (getHeight()+5));
                        for (int j = 0; j < thisItemTipbox.get(i).components.size(); j++) {
                            thisItemTipbox.get(i).components.get(j).setPosition(thisItemTipbox.get(i).components.get(j).getX(), thisItemTipbox.get(i).components.get(j).getY() - (getHeight()+5));
                        }
                    }
                    for (int i = 0; i < nextItemTipbox.size(); i++) {
                        nextItemTipbox.get(i).setPosition(nextItemTipbox.get(i).getX(), nextItemTipbox.get(i).getY() + (getHeight()+5));
                        for (int j = 0; j < thisItemTipbox.get(i).components.size(); j++) {
                            nextItemTipbox.get(i).components.get(j).setPosition(nextItemTipbox.get(i).components.get(j).getX(), nextItemTipbox.get(i).components.get(j).getY() + (getHeight() + 5));
                        }
                    }

/*
                    for (int i = 0; i < smallerListSize; i++) {
                        thisItemComponents.get(i).setPosition(nextItemComponents.get(i).getX(), nextItemComponents.get(i).getY());
                        if (thisItemComponents.get(i).components.size() > 0){
                            thisItemComponents.get(i).components.get(0).setPosition(thisItemComponents.get(i).components.get(0).getX(), nextItemComponents.get(i).components.get(0).getY());
                            thisItemComponents.get(i).components.get(1).setPosition(thisItemComponents.get(i).components.get(0).getX(), nextItemComponents.get(i).components.get(1).getY());
                        }
                    }
                    //looping through the list of the next item's components and assigning their positions to this item's components' old positions
                    for (int i = 0; i < smallerListSize; i++) {
                        nextItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), oldYs.get(i));
                        if (nextItemComponents.get(i).components.size() > 0){
                            nextItemComponents.get(i).components.get(0).setPosition(thisItemComponents.get(i).components.get(0).getX(), restButtonsYs.get(0));
                            nextItemComponents.get(i).components.get(1).setPosition(thisItemComponents.get(i).components.get(0).getX(), restButtonsYs.get(1));
                        }

                    }
                    for (int i = 0; i < thisItemTipbox.size(); i++) {
                        thisItemTipbox.get(i).setPosition(thisItemTipbox.get(i).getX(), thisItemTipbox.get(i).getY() - (getHeight()+5));
                        thisItemTipbox.get(i).components.get(i).setPosition(thisItemTipbox.get(i).components.get(i).getX(), thisItemTipbox.get(i).components.get(i).getY() - (getHeight()+5));
                    }
                    for (int i = 0; i < nextItemTipbox.size(); i++) {
                        nextItemTipbox.get(i).setPosition(nextItemTipbox.get(i).getX(), oldYs2.get(i));
                        nextItemTipbox.get(i).components.get(i).setPosition(nextItemTipbox.get(i).components.get(i).getX(), oldYs2.get(i)+10);
                    }
*/
                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).getEditMode()){
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).saveEdit();
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).edit();
                    }
                }
            }
        });
        itemButtonUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Up Button "+(itemButtonUp.getItem().sID +1));
                //initializing spots into set temporary variables
                int currSpot = itemButtonUp.getItem().sSpot;
                int prevSpot = itemButtonUp.getItem().sSpot -1;
                //if it's not at the top...
                if(prevSpot>=0) {
                    //reassigns the spots to different variables
                    //assigns this item's spot to an arbitrary number so that the two items are never at the same spot
                    itemButtonUp.getItem().sSpot = -100;
                    itemButtonUp.getItem().getPanel().getItemBySpot(prevSpot).sSpot = currSpot;
                    itemButtonUp.getItem().sSpot = prevSpot;
                    //making it easier to read
                    ArrayList<MBComponent> thisItemComponents = itemButtonUp.getItem().components;
                    //making it easier to read
                    ArrayList<MBComponent> prevItemComponents = itemButtonUp.getItem().getPanel().getItemBySpot(currSpot).components;

                    ArrayList<Panel> thisItemTipbox = itemButtonUp.getItem().minipanels;
                    ArrayList<Panel> prevItemTipbox = itemButtonUp.getItem().getPanel().getItemBySpot(currSpot).minipanels;

                    int smallerListSize = Math.min(thisItemComponents.size(), prevItemComponents.size());
                    //saving this item's components' positions before I change them, so I can use there later
                    ArrayList<Float> oldYs = new ArrayList<>();
                    for(int i = 0; i < smallerListSize; i++) {
                        oldYs.add(thisItemComponents.get(i).getY());
                    }
                    ArrayList<Float> oldYs2 = new ArrayList<>();
                    for(int i = 0; i < thisItemTipbox.size(); i++) {
                        oldYs2.add(thisItemTipbox.get(i).getY());
                    }
                    //repositioning this item to its new spot
                    //looping through the list of this item's components and assigning their positions to the next item's components' positions

                    for (int i = 0; i < thisItemComponents.size(); i++) {
                        thisItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), thisItemComponents.get(i).getY() + (getHeight()+5));
                        if(thisItemComponents.get(i).components.size() > 0){
                            thisItemComponents.get(i).components.get(0).setPosition(thisItemComponents.get(i).components.get(0).getX(), thisItemComponents.get(i).components.get(0).getY() + (getHeight()+5));
                            thisItemComponents.get(i).components.get(1).setPosition(thisItemComponents.get(i).components.get(1).getX(), thisItemComponents.get(i).components.get(1).getY() + (getHeight()+5));
                        }
                    }
                    for (int i = 0; i < prevItemComponents.size(); i++) {
                        prevItemComponents.get(i).setPosition(prevItemComponents.get(i).getX(), prevItemComponents.get(i).getY() - (getHeight()+5));
                        if(prevItemComponents.get(i).components.size() > 0){
                            prevItemComponents.get(i).components.get(0).setPosition(prevItemComponents.get(i).components.get(0).getX(), prevItemComponents.get(i).components.get(0).getY() - (getHeight()+5));
                            prevItemComponents.get(i).components.get(1).setPosition(prevItemComponents.get(i).components.get(1).getX(), prevItemComponents.get(i).components.get(1).getY() - (getHeight()+5));
                        }
                    }


                    for (int i = 0; i < thisItemTipbox.size(); i++) {
                        thisItemTipbox.get(i).setPosition(thisItemTipbox.get(i).getX(), thisItemTipbox.get(i).getY() + (getHeight()+5));
                        for (int j = 0; j < thisItemTipbox.get(i).components.size(); j++) {
                            thisItemTipbox.get(i).components.get(j).setPosition(thisItemTipbox.get(i).components.get(j).getX(), thisItemTipbox.get(i).components.get(j).getY() + (getHeight() + 5));
                        }
                    }
                    for (int i = 0; i < prevItemTipbox.size(); i++) {
                        prevItemTipbox.get(i).setPosition(prevItemTipbox.get(i).getX(), prevItemTipbox.get(i).getY() - (getHeight()+5));
                        for (int j = 0; j < thisItemTipbox.get(i).components.size(); j++) {
                            prevItemTipbox.get(i).components.get(j).setPosition(prevItemTipbox.get(i).components.get(j).getX(), prevItemTipbox.get(i).components.get(j).getY() - (getHeight() + 5));
                        }
                    }

/*
                    for (int i = 0; i < smallerListSize; i++) {
                        thisItemComponents.get(i).setPosition(prevItemComponents.get(i).getX(), prevItemComponents.get(i).getY());
                    }
                    //looping through the list of the previous item's components and assigning their positions to this item's components' old positions
                    for (int i = 0; i < smallerListSize; i++) {
                        prevItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), oldYs.get(i));
                    }
                    for (int i = 0; i < thisItemTipbox.size(); i++) {
                        thisItemTipbox.get(i).setPosition(thisItemTipbox.get(i).getX(), thisItemTipbox.get(i).getY() + (getHeight()+5));
                        thisItemTipbox.get(i).components.get(i).setPosition(thisItemTipbox.get(i).components.get(i).getX(), thisItemTipbox.get(i).components.get(i).getY() + (getHeight()+5));
                    }
                    for (int i = 0; i < prevItemTipbox.size(); i++) {
                        prevItemTipbox.get(i).setPosition(prevItemTipbox.get(i).getX(), oldYs2.get(i));
                        prevItemTipbox.get(i).components.get(i).setPosition(prevItemTipbox.get(i).components.get(i).getX(), oldYs2.get(i)+10);
                    }
*/
                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).getEditMode()){
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).saveEdit();
                        itemButtonDown.getItem().getPanel().getItemBySpot(currSpot).edit();
                    }
                }
            }
        });

    }

    /**
     * same as Panel.add but sets the component's item too
     * @param component the component you want to add
     */
    public void add(MBComponent component){
        //adds this component to the list of all components
        boolean notAdded = true;
        for (MBComponent comp: Main.allComps) {
            if (component == comp) {
                notAdded = false;
                break;
            }
        }
        if (notAdded) Main.allComps.add(component);
        //adds the component given to this item
        components.add(component);
        //sets the component's parent to this item
        component.parentPanel = this;
        //sets the component's item to this item
        component.item = this;
        //makes sure the component is an actor
        if(component.getComponent() != null) {
            //adds component to the stage so it can be drawn
            Main.stage.addActor(component.getComponent());
        }
    }

    /**
     * makes the textfields appear above the labels
     */
    public void edit(){
        Item item = this;

        //to have only one item edited at a time
        if(item.getList() != null) {
            for (Item item2: item.getList()) {
                if(item2 != item) {
                    item2.saveEdit();
                    item2.editMode = false;
                }
            }
        }

        for (int i = 0; i < components.size(); i++) {
            //finds editbutton and checks it
            if(components.get(i).getName() != null && components.get(i).getName().equals("editbutton") && components.get(i) instanceof MBButton){
                ((MBButton) components.get(i)).button.setChecked(true);
            }
            //finds usesbutton and makes it not rendered so that it's listeners are removed
            if(components.get(i).getName() != null && components.get(i).getName().equals("usesbutton") && components.get(i) instanceof MBButton){
                components.get(i).setVisible(false);
                for (int j = 0; j < components.get(i).components.size(); j++) {
                    components.get(i).components.get(j).setVisible(false);
                }
            }
        }
        //loops through this item's textfields list and reassigns the positions, re-adds them to the list, and sets their hard visibility to true
        for (int i = 0; i < textFields.size(); i++) {
            if(labels.get(i).getName() != null && labels.get(i).getName().equals("tf")){
                if(minipanels.get(0) instanceof Tipbox){
                    MBTextArea tipboxTF = ((Tipbox) minipanels.get(0)).getTextArea();
                    tipboxTF.textArea.setText(names.get(i));
                    labels.get(i).label.setText("");
                }
            }
            else {
                textFields.get(i).textField.setText(names.get(i));
                labels.get(i).label.setText("");
                textFields.get(i).setPosition(labels.get(i).getX(), item.getY() + 5);
                textFields.get(i).setSize(labels.get(i).getWidth(), 30);
                add(textFields.get(i));
                textFields.get(i).setVisible(true);
            }
        }
        //short rest or long rest
        if(itemType == 2){
            for (MBButton restButton: restButtons) {
                restButton.setVisible(true);
//                add(restButton);
            }
        }
        //loops through this item's tipboxes
        for (int i = 0; i < minipanels.size(); i++) {
            minipanels.get(i).editMode = true;
            minipanels.get(i).setSoftVisible(true);
        }
    }

    /**
     * sets the labels to the text of the textfields and makes them disappear
     */
    public void saveEdit(){
        GlyphLayout layout = new GlyphLayout();

        for (int i = 0; i < components.size(); i++) {
            //finds editbutton and unchecks it
            if(components.get(i).getName() != null && components.get(i).getName().equals("editbutton") && components.get(i) instanceof MBButton){
                ((MBButton) components.get(i)).button.setChecked(false);
            }
            //finds usesbutton and makes it rendered again so that it's listeners are back
            if(components.get(i).getName() != null && components.get(i).getName().equals("usesbutton") && components.get(i) instanceof MBButton){
                components.get(i).setVisible(true);
                for (int j = 0; j < components.get(i).components.size(); j++) {
                    components.get(i).components.get(j).setVisible(true);
                }
            }
        }


        for (int i = 0; i < textFields.size(); i++) {
            if(labels.get(i).getName() != null && labels.get(i).getName().equals("tf")) {
                if (minipanels.get(0) instanceof Tipbox) {
                    MBTextArea tipboxTF = ((Tipbox) minipanels.get(0)).getTextArea();
                    names.set(i, tipboxTF.textArea.getText());

                    layout.setText(uiSkin.getFont("default-font"), names.get(i));

                    if(layout.width > labels.get(i).getWidth()){
                        labels.get(i).label.setText(shortenString(names.get(i), labels.get(i).getWidth()));
                    }
                    else if(tipboxTF.textArea.getLines() > 1){
                        labels.get(i).label.setText(shortenString(names.get(i), labels.get(i).getWidth()));
                    }
                    else labels.get(i).label.setText(names.get(i));
                }
            }
            else {
                names.set(i, textFields.get(i).textField.getText());
                layout.setText(uiSkin.getFont("default-font"), names.get(i));

                if(layout.width > labels.get(i).getWidth()){
                    labels.get(i).label.setText(shortenString(names.get(i), labels.get(i).getWidth()));
                }
                else labels.get(i).label.setText(names.get(i));
                textFields.get(i).setVisible(false);
                remove(textFields.get(i));
            }
        }
        if(itemType == 2){
            for (int i = 0; i < restButtons.size(); i++) {
                //if the selected rest button is a short rest...
                if(restButtons.get(i).button.isChecked() && i == 0){
                    //set the short rest max uses to the textfield value
                    srMax = Integer.parseInt(names.get(usesIndexInNames));
                    //if the long rest max is smaller than the short rest mex set the long  rest max uses to the textfield value too
                    if(lrMax < srMax) lrMax = Integer.parseInt(names.get(usesIndexInNames));
                }
                //if the selected rest button is a long rest...
                if(restButtons.get(i).button.isChecked() && i == 1){
                    //set the long rest max uses to the textfield value
                    lrMax = Integer.parseInt(names.get(usesIndexInNames));
                }
                restButtons.get(i).setVisible(false);
//                remove(restButtons.get(i));
            }
        }

        //loops through this item's tipboxes
        for (int i = 0; i < minipanels.size(); i++) {
            minipanels.get(i).editMode = false;
            minipanels.get(i).setSoftVisible(false);
        }
    }
    public String shortenString(String str, float length){
        GlyphLayout layout = new GlyphLayout();
        String shortenedString;

        layout.setText(uiSkin.getFont("default-font"), "...");
        float elipse = layout.width;

        for (int i = 0; i < str.length(); i++) {
            layout.setText(uiSkin.getFont("default-font"), str.substring(0,i));
            if((layout.width+elipse) >= length || str.charAt(i) == '\n' || str.charAt(i) == '\r'){
                if(str.charAt(i) == '\n' || str.charAt(i) == '\r'){
                    shortenedString = str.substring(0,i);
                    return shortenedString.trim()+"...";
                }
                else {
                    shortenedString = str.substring(0, i - 1);
                    return shortenedString.trim() + "...";
                }
            }
        }
        return "...";
    }
    /**
     * shuffles all the items up
     */
    public void shuffleItemsUp(){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        if(getItemType() == 1) nextAvaWSpot--;
        else if(getItemType() == 2) nextAvaSSpot--;
        //loops through all the items
        for(Item item : getList()){
            if(item.getItemType() == 1) item.wSpot--;
            else if(item.getItemType() == 2) item.sSpot--;
            //loops through the item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
            for (int i = 0; i < item.components.size(); i++) {
                item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() + (item.getHeight()+5));
            }
            for (int i = 0; i < item.minipanels.size(); i++) {
                item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() + (item.getHeight()+5));
                item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() + (getHeight()+5));
            }
            //moves the textfields with the item if in edit mode
            if(item.editMode){
                item.saveEdit();
                item.edit();
            }
            if(item.getSpot() < 0 || item.getSpot() > 5){
                item.setSoftVisible(false);
            }
            else if(item.getSpot() >= 0 && item.getSpot() <= 5){
                item.setSoftVisible(true);
            }
        }
    }
    public ArrayList<Item> getList(){
        if(getPanel() != null) {
            if (Main.itemTab == 1) return getPanel().wItems;
            else if (Main.itemTab == 2) return getPanel().sItems;
        }
        return null;
    }
    /**
     * shuffles all the items up starting from a spot
     * @param startSpot the lowest spot you don't want to raise
     */
    public void shuffleItemsUp(int startSpot){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        if(getItemType() == 1) nextAvaWSpot--;
        else if(getItemType() == 2) nextAvaSSpot--;
        //loops through all the items
        for(Item item : getList()) {
            if (item.getSpot() > startSpot) {
                if(item.getItemType() == 1) item.wSpot--;
                else if(item.getItemType() == 2) item.sSpot--;
                //loops through the item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
                for (int i = 0; i < item.components.size(); i++) {
                    item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() + (item.getHeight()+5));
                }
                for (int i = 0; i < item.minipanels.size(); i++) {
                    item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() + (item.getHeight()+5));
                    item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() + (getHeight()+5));
                }
                //moves the textfields with the item if in edit mode
                if (item.editMode) {
                    item.saveEdit();
                    item.edit();
                }
                if(item.getSpot() < 0 || item.getSpot() > 5){
                    item.setSoftVisible(false);
                }
                else if(item.getSpot() >= 0 && item.getSpot() <= 5){
                    item.setSoftVisible(true);
                }
            }
        }
    }

    /**
     * shuffles all the items down
     */
    public void shuffleItemsDown(){
        //increases the next available spot value by one so that new items get added under the lowest item always
        if(getItemType() == 1) nextAvaWSpot++;
        else if(getItemType() == 2) nextAvaSSpot++;
        //loops through all the items
        for(Item item : getList()){
            if(item.getItemType() == 1) item.wSpot++;
            else if(item.getItemType() == 2) item.sSpot++;
            //loops through the item's components' position and reduces the Y value by the item's height plus the gap between items (moving it down)
            for (int i = 0; i < item.components.size(); i++) {
                item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() - (item.getHeight()+5));
            }
            for (int i = 0; i < item.minipanels.size(); i++) {
                item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() - (item.getHeight()+5));
                item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() - (getHeight()+5));
            }
            //moves the textfields with the item if in edit mode
            if(item.editMode){
                item.saveEdit();
                item.edit();
            }
            if(item.getSpot() < 0 || item.getSpot() > 5){
                item.setSoftVisible(false);
            }
            else if(item.getSpot() >= 0 && item.getSpot() <= 5){
                item.setSoftVisible(true);
            }
        }
    }

    /**
     * @return returns whether this item is in edit mode or not
     */
    public boolean getEditMode(){
        return editMode;
    }
    /**
     * @return returns this item's spot value
     */
    public float getSpot() {
        if(itemType == 1) return wSpot;
        else if(itemType == 2) return sSpot;
        else return -100;
    }
    /**
     * @return returns this item's y value
     */
    public float getY(){
        if(itemType == 1) return (position.y-((position.height+5)* wSpot));
        else if(itemType == 2) return (position.y-((position.height+5)* sSpot));
        return -1;
    }
    public int getItemType(){
        return itemType;
    }
    public MBButton getUsesButton(){
        if (usesButton != null) return usesButton;
        return null;
    }
    /**
     * renders this item and any minipanels it may hold
     * @param batch the batch...
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        if(itemType == 1) batch.draw(texture, position.x, (position.y-((position.height+5)* wSpot)), position.width, position.height);
        if(itemType == 2) batch.draw(texture, position.x, (position.y-((position.height+5)* sSpot)), position.width, position.height);

        for (int c = 0; c < components.size(); c++) {

            if(components.get(c).supposedToBeVisible) {
                //sets the soft visibility of the component to true
                components.get(c).setSoftVisible(true);
//                components.get(c).getComponent().act(1/60f);

                components.get(c).draw(components.get(c).aFloat);
            }
        }
        for (int i = 0; i < tipboxes.size(); i++) {
            tipboxes.get(i).render(batch);
        }
    }
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

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
    public Item(int itemType, int spot) {
        super("core\\pics\\MBSkin2\\ItemPanel.png", new Rectangle(125, 790, 460, 40));
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
        //creating the labels
        MBLabel nameLabel,descLabel;
        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Spell  "+ (Panel.totalSID), uiSkin);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        names.add(nameLabel.label.getText().toString());

        descLabel = new MBLabel("Item Description...", uiSkin);
        descLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        descLabel.setSize(279, nameLabel.getHeight());
        descLabel.setName("tf");
        names.add(descLabel.label.getText().toString());

        labels.add(nameLabel);
        labels.add(descLabel);

        final Tipbox spellDesc = new Tipbox(new Rectangle(115, descLabel.getY()+ (descLabel.getHeight()/2)-300, 770, 300));
        MBTextArea spellDescTF = new MBTextArea("", uiSkin);
        spellDescTF.setPosition(spellDesc.getX()+10, spellDesc.getY()+10);
        spellDescTF.setSize(750, 260);
        spellDesc.add(spellDescTF);

        add(spellDesc);
        tipboxes.add(spellDesc);

/*
        modLabel = new MBLabel("ATKMod", uiSkin);
        modLabel.setPosition(diceLabel.getX()+ diceLabel.getWidth()+2, nameLabel.getY());
        modLabel.setSize(85, nameLabel.getHeight());
        mod = modLabel.label.getText().toString();

        typeLabel = new MBLabel("Damage/Type", uiSkin);
        typeLabel.setPosition(modLabel.getX()+ modLabel.getWidth()+2, nameLabel.getY());
        typeLabel.setSize(115, nameLabel.getHeight());
        type = typeLabel.label.getText().toString();
*/
        //creating buttons and setting their positions and sizes
        final MBButton itemButtonEdit = new MBButton(uiSkin, "edit-toggle");
        itemButtonEdit.button.setChecked(true);
        itemButtonEdit.setName("editbutton");
        itemButtonEdit.button.setName("editbutton");
        itemButtonEdit.setPosition((descLabel.getX()+descLabel.getWidth()+10), nameLabel.getY()-1);
        itemButtonEdit.setSize(20, 15);

        final MBButton itemButtonDel = new MBButton(uiSkin, "delete-button");
        itemButtonDel.setPosition((descLabel.getX()+descLabel.getWidth()+10), itemButtonEdit.getY()+itemButtonEdit.getHeight()+2);
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
        add(itemButtonEdit);
        add(itemButtonDel);
        add(itemButtonDown);
        add(itemButtonUp);
        //starting the item in edit mode so the user can immediately edit the item text
        editMode = true;
        edit();
        //detecting when enter is pressed on each MBTextField so that enter can exit out of edit mode
        //setting the buttons' functions
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
        //fixme still need to work on this button
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
                    for (int i = 0; i < smallerListSize; i++) {
                        thisItemComponents.get(i).setPosition(nextItemComponents.get(i).getX(), nextItemComponents.get(i).getY());
                    }
                    //looping through the list of the next item's components and assigning their positions to this item's components' old positions
                    for (int i = 0; i < smallerListSize; i++) {
                        nextItemComponents.get(i).setPosition(thisItemComponents.get(i).getX(), oldYs.get(i));
                    }
                    for (int i = 0; i < thisItemTipbox.size(); i++) {
                        thisItemTipbox.get(i).setPosition(thisItemTipbox.get(i).getX(), thisItemTipbox.get(i).getY() - (getHeight()+5));
                        thisItemTipbox.get(i).components.get(i).setPosition(thisItemTipbox.get(i).components.get(i).getX(), thisItemTipbox.get(i).components.get(i).getY() - (getHeight()+5));
                    }
                    for (int i = 0; i < nextItemTipbox.size(); i++) {
                        nextItemTipbox.get(i).setPosition(nextItemTipbox.get(i).getX(), oldYs2.get(i));
                        nextItemTipbox.get(i).components.get(i).setPosition(nextItemTipbox.get(i).components.get(i).getX(), oldYs2.get(i)+10);
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

        //finds editbutton and checks it
        for (int i = 0; i < components.size(); i++) {
            if(components.get(i).getName() != null && components.get(i).getName().equals("editbutton") && components.get(i) instanceof MBButton){
                ((MBButton) components.get(i)).button.setChecked(true);
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

        //finds editbutton and unchecks it
        for (int i = 0; i < components.size(); i++) {
            if(components.get(i).getName() != null && components.get(i).getName().equals("editbutton") && components.get(i) instanceof MBButton){
                ((MBButton) components.get(i)).button.setChecked(false);
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
    /**
     * renders this item and any minipanels it may hold
     * @param batch the batch...
     */
    @Override
    public void render(SpriteBatch batch) {
        if(itemType == 1) batch.draw(texture, position.x, (position.y-((position.height+5)* wSpot)), position.width, position.height);
        if(itemType == 2) batch.draw(texture, position.x, (position.y-((position.height+5)* sSpot)), position.width, position.height);

        for (int c = 0; c < components.size(); c++) {

            if(components.get(c).supposedToBeVisible) {
                //sets the soft visibility of the component to true
                components.get(c).setSoftVisible(true);
//                components.get(c).getComponent().act(1/60f);

                components.get(c).draw(1);
            }
        }
        for (int i = 0; i < tipboxes.size(); i++) {
            tipboxes.get(i).render(batch);
        }
    }
}

package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class SpellItem extends Item2{
    private final Tipbox spellDesc;

    private static int totalItems = 0;
    private static ArrayList<Item2> allItems = new ArrayList<>();
    private static int nextAvaSpot = 0;

    int usesIndexInNames = -1;
    private MBButton usesButton;
    int uses = 0;
    public int srMax = 0;
    public int lrMax = 0;
    ArrayList<MBButton> restButtons = new ArrayList<>();

    public SpellItem() {
        super();

        this.spot = nextAvaSpot;
        ID = totalItems;

        allItems.add(this);
        //increasing the total number of items by one (this item's ID was already set when it was created (code in panel class))
        totalItems++;
        //increasing the next available spot by one
        nextAvaSpot++;

        //fixme Look over this code and make sure its optimal
        //region labels
        MBLabel nameLabel, descLabel, usesLabel;

        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Spell  "+ ID, skin);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        labelTexts.add(nameLabel.getLabel().getText().toString());

        descLabel = new MBLabel("Item Description...", skin);
        descLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        descLabel.setSize(257, nameLabel.getHeight());
        descLabel.setName("tf"); //setting the name so I can identify it later
        labelTexts.add(descLabel.getLabel().getText().toString());

        usesLabel = new MBLabel(String.valueOf(uses), skin);
        usesLabel.setPosition(descLabel.getX()+ descLabel.getWidth()+2, nameLabel.getY());
        usesLabel.setSize(22, nameLabel.getHeight());
        labelTexts.add(usesLabel.getLabel().getText().toString());
        usesIndexInNames = labelTexts.size()-1;

        labels.add(nameLabel);
        labels.add(descLabel);
        labels.add(usesLabel);

        add(nameLabel);
        add(descLabel);
        add(usesLabel);
        //endregion

        //region buttons
        //creating buttons and setting their positions and sizes
        final MBButton usesButton = new MBButton(String.valueOf(uses), skin),
                minusButton = new MBButton(skin, "down-button"),
                plusButton = new MBButton(skin, "up-button"),
                itemButtonEdit = new MBButton(skin, "edit-toggle"),
                itemButtonDel = new MBButton(skin, "delete-button"),
                itemButtonDown = new MBButton(skin, "down-button"),
                itemButtonUp = new MBButton(skin, "up-button");

        usesButton.setName("usesbutton"); //setting the name so I can identify it later
        usesButton.setPosition(usesLabel.getX(), usesLabel.getY());
        usesButton.setSize(usesLabel.getWidth(), 30);
        ((TextButton)usesButton.getButton()).getLabel().setAlignment(Align.left);
        this.usesButton = usesButton; //fixme is this necessary

        minusButton.setPosition(usesButton.getX(), usesButton.getY());
        minusButton.setSize(usesButton.getWidth(), usesButton.getHeight()/2 - 1);
        minusButton.aFloat = 0;

        plusButton.setPosition(usesButton.getX(), usesButton.getY()+ minusButton.getHeight()+1);
        plusButton.setSize(usesButton.getWidth(), usesButton.getHeight()/2 - 1);
        plusButton.aFloat = 0;

        itemButtonEdit.getButton().setChecked(true);
        itemButtonEdit.setName("editbutton");
        itemButtonEdit.getButton().setName("editbutton"); //setting the name so I can identify it later
        itemButtonEdit.setPosition((usesLabel.getX()+usesLabel.getWidth()+8), nameLabel.getY()-1);
        itemButtonEdit.setSize(20, 15);

        itemButtonDel.setPosition(itemButtonEdit.getX(), itemButtonEdit.getY()+itemButtonEdit.getHeight()+2);
        itemButtonDel.setSize(20, 15);

        itemButtonDown.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonEdit.getY());
        itemButtonDown.setSize(20, 15);

        itemButtonUp.setPosition(itemButtonDown.getX(), itemButtonDown.getY()+itemButtonDown.getHeight()+2);
        itemButtonUp.setSize(20, 15);

        add(usesButton);
        add(itemButtonEdit);
        add(itemButtonDel);
        add(itemButtonDown);
        add(itemButtonUp);
        //endregion

        //region uses button
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
                System.out.println("minus clicked " + (ID + 1));
                uses--;
                ((TextButton)usesButton.getButton()).setText(String.valueOf(uses));
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the Minus Button...
                if (usesButton.getButton().isOver() || minusButton.getButton().isOver() || plusButton.getButton().isOver()) {
                    minusButton.aFloat = .75f;
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
                System.out.println("plus clicked "+(ID + 1));
                uses++;
                ((TextButton)usesButton.getButton()).setText(String.valueOf(uses));
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the Plus Button...
                if (usesButton.getButton().isOver() || minusButton.getButton().isOver() || plusButton.getButton().isOver()) {
                    minusButton.aFloat = .5f;
                    plusButton.aFloat = .75f;
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

        final MBButton srButton = new MBButton("Short Rest", skin);
        srButton.getButton().setStyle(Main.uiSkin.get("toggle", TextButton.TextButtonStyle.class));
        srButton.getButton().setChecked(true);
        srButton.setSize(50, itemButtonEdit.getHeight());
        srButton.setPosition(usesButton.getX()-srButton.getWidth()-2, itemButtonDel.getY());
        ((TextButton)srButton.getButton()).getLabel().setFontScale(.6f,.7f);

        final MBButton lrButton = new MBButton("Long Rest", skin);
        lrButton.getButton().setStyle(Main.uiSkin.get("toggle", TextButton.TextButtonStyle.class));
        lrButton.getButton().setChecked(false);
        lrButton.setSize(50, itemButtonDel.getHeight());
        lrButton.setPosition(srButton.getX(), itemButtonEdit.getY());
        ((TextButton)lrButton.getButton()).getLabel().setFontScale(.6f,.7f);
        srButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("short rest " +(ID + 1));
                //When the button is pressed while checked. Usually it would be if(button.isChecked), but button unchecks itself before this is called, so it's reversed
                if(!srButton.getButton().isChecked()){
                    lrButton.getButton().setChecked(true);
                }
                else if(srButton.getButton().isChecked()){
                    lrButton.getButton().setChecked(false);
                }
            }
        });
        lrButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("long rest "+(ID + 1));
                //When the button is pressed while checked. Usually it would be if(button.isChecked), but button unchecks itself before this is called, so it's reversed
                if(!lrButton.getButton().isChecked()){
                    srButton.getButton().setChecked(true);
                }
                else if(lrButton.getButton().isChecked()){
                    srButton.getButton().setChecked(false);
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

        //region button listeners
        //changes this item in and out of edit mode
        itemButtonEdit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Edit Button " + (ID +1));

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
        //deletes the item from existence
        //fixme still need to work on this button
        itemButtonDel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Delete Button " + (ID +1));

                int currSpot = getSpot();
                shuffleItemsUp(currSpot);

                for (int i = components.size()-1; i >= 0; i--) {
                    delete(components.get(i));
                }
                parentPanel.delete(SpellItem.this);
            }
        });

        //swaps this item with the item under it
        itemButtonDown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Down Button " + (ID + 1));

                int nextSpot = spot + 1;
                //if it's not at the bottom...
                if (nextSpot < nextAvaSpot) {
                    Item2 nextItem = getItemBySpot(nextSpot);

                    //loops through this item's components' position and decreases the Y value by the item's height plus the gap between items (moving it down)
                    for (MBComponent component : components) {
                        component.setPosition(component.getX(), component.getY() - (getHeight() + ITEMGAP));
                    }
                    //loops through the next item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
                    for (MBComponent component : nextItem.components) {
                        component.setPosition(component.getX(), component.getY() + (getHeight() + ITEMGAP));
                    }

                    //decreases this item's tipbox's Y value by the item's height plus the gap between items (moving it down)
                    spellDesc.setPosition(spellDesc.getX(), spellDesc.getY() - (getHeight() + ITEMGAP));

                    //increases the next item's tipbox's Y value by the item's height plus the gap between items (moving it up)
                    if(nextItem instanceof SpellItem) ((SpellItem) nextItem).getTipbox().setPosition(((SpellItem) nextItem).getTipbox().getX(),
                            ((SpellItem) nextItem).getTipbox().getY() + (getHeight() + ITEMGAP));

                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(nextItem.getEditMode()){
                        nextItem.saveEdit();
                        nextItem.edit();
                    }
                }
            }
        });

        itemButtonUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Up Button "+ (ID + 1));

                int prevSpot = spot - 1;
                //if it's not at the top...
                if(prevSpot >= 0) {
                    Item2 prevItem = getItemBySpot(prevSpot);

                    //reassigns the spots to different variables
                    //loops through this item's components' position and decreases the Y value by the item's height plus the gap between items (moving it up)
                    for (MBComponent component : components) {
                        component.setPosition(component.getX(), component.getY() + (getHeight() + ITEMGAP));
                    }
                    //loops through the previous item's components' position and decreases the Y value by the item's height plus the gap between items (moving it down)
                    for (MBComponent component : prevItem.components) {
                        component.setPosition(component.getX(), component.getY() - (getHeight() + ITEMGAP));
                    }

                    //increases this item's tipbox's Y value by the item's height plus the gap between items (moving it up)
                    spellDesc.setPosition(spellDesc.getX(), spellDesc.getY() - (getHeight() + ITEMGAP));

                    //decreases the previous item's tipbox's Y value by the item's height plus the gap between items (moving it down)
                    if(prevItem instanceof SpellItem) ((SpellItem) prevItem).getTipbox().setPosition(((SpellItem) prevItem).getTipbox().getX(),
                            ((SpellItem) prevItem).getTipbox().getY() + (getHeight() + ITEMGAP));

                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(prevItem.getEditMode()){
                        prevItem.saveEdit();
                        prevItem.edit();
                    }
                }
            }
        });
        //endregion


        //region textfields
        //creating textfields and setting their texts to their corresponding label's text
        for (int i = 0; i < labelTexts.size(); i++) {
            textFields.add(new MBTextField(labelTexts.get(i), skin));
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
        //endregion

        //region tipbox components
        spellDesc = new Tipbox(new Rectangle(115, descLabel.getY()+ (descLabel.getHeight()/2)-300, 770, 300));
        MBTextArea spellDescTF = new MBTextArea("");
        spellDescTF.setPosition(spellDesc.getX()+10, spellDesc.getY()+10+35);
        spellDescTF.setSize(750, 225);

        MBLabel spellLevel = new MBLabel("Level: "),
                castTime = new MBLabel("Casting Time: "),
                duration = new MBLabel("Duration: "),
                range = new MBLabel("Range: "), damageType = new MBLabel("Damage Type: ");
        MBTextField spellLevelTF = new MBTextField("0"),
                castTimeTF = new MBTextField("Action"),
                durationTF = new MBTextField("Instant"),
                rangeTF = new MBTextField("5 ft"),
                damageTypeTF = new MBTextField("Fire");

        spellLevel.setPosition(spellDesc.getX()+10, spellDesc.getY()+10);

        spellLevelTF.setPosition(spellLevel.getX() + spellLevel.getWidth() +0, spellLevel.getY());
        spellLevelTF.setSize(25, 30);

        castTime.setPosition(spellLevelTF.getX() + spellLevelTF.getWidth() +10, spellLevel.getY());

        castTimeTF.setPosition(castTime.getX() + castTime.getWidth() +0, spellLevel.getY());
        castTimeTF.setSize(85, spellLevelTF.getHeight());
        castTimeTF.getTextField().setAlignment(Align.center);

        duration.setPosition(castTimeTF.getX() + castTimeTF.getWidth() +10, spellLevel.getY());

        durationTF.setPosition(duration.getX() + duration.getWidth() +0, spellLevel.getY());
        durationTF.setSize(85, spellLevelTF.getHeight());
        durationTF.getTextField().setAlignment(Align.center);

        range.setPosition(durationTF.getX() + durationTF.getWidth() +10, spellLevel.getY());

        rangeTF.setPosition(range.getX() + range.getWidth() +0, spellLevel.getY());
        rangeTF.setSize(50, spellLevelTF.getHeight());
        rangeTF.getTextField().setAlignment(Align.center);

        damageType.setPosition(rangeTF.getX() + rangeTF.getWidth() +10, spellLevel.getY());

        damageTypeTF.setPosition(damageType.getX() + damageType.getWidth() +0, spellLevel.getY());
        damageTypeTF.setSize(75, spellLevelTF.getHeight());
        damageTypeTF.getTextField().setAlignment(Align.center);

        spellDesc.add(spellDescTF);
        spellDesc.add(spellLevel);
        spellDesc.add(spellLevelTF);
        spellDesc.add(castTime);
        spellDesc.add(castTimeTF);
        spellDesc.add(duration);
        spellDesc.add(durationTF);
        spellDesc.add(range);
        spellDesc.add(rangeTF);
        spellDesc.add(damageType);
        spellDesc.add(damageTypeTF);

        add(spellDesc);
        //endregion

        //starting the item in edit mode so the user can immediately edit the item text
        edit();
    }

    public void edit() {
        //to have only one item edited at a time
        if(allItems != null) {
            for (Item2 item2: allItems) {
                if(item2 != this) item2.saveEdit(); //if this isn't the item being edited...
            }
        }

        for (MBComponent component : components) {
            //finds editbutton and checks it
            if (component.getName() != null && component.getName().equals("editbutton") && component instanceof MBButton) {
                ((MBButton) component).getButton().setChecked(true);
            }
            //finds usesbutton and makes it not rendered so that it's listeners are removed
            if (component.getName() != null && component.getName().equals("usesbutton")) {
                component.setVisible(false);
                for (int j = 0; j < component.components.size(); j++) {
                    component.components.get(j).setVisible(false);
                }
            }
        }

        //loops through this item's textfields list and updates their positions, re-adds them to the list, and sets their hard visibility to true
        for (int i = 0; i < textFields.size(); i++) {
            if(labels.get(i).getName() != null && labels.get(i).getName().equals("tf")){
                spellDesc.getTextArea().getTextArea().setText(labelTexts.get(i));
                labels.get(i).getLabel().setText("");
            }
            else {
                textFields.get(i).getTextField().setText(labelTexts.get(i));
                labels.get(i).getLabel().setText("");
                textFields.get(i).setPosition(labels.get(i).getX(), getY() + 5);
                textFields.get(i).setSize(labels.get(i).getWidth(), 30);
//                add(textFields.get(i));
                textFields.get(i).setVisible(true);
            }
        }

        for (MBButton restButton: restButtons) {
            restButton.setVisible(true);
        }

        spellDesc.setSoftVisible(true);

        editMode = true;
    }

    public void saveEdit() {
        for (MBComponent component : components) {
            //finds editbutton and unchecks it
            if (component.getName() != null && component.getName().equals("editbutton") && component instanceof MBButton) {
                ((MBButton) component).getButton().setChecked(false);
            }
            //finds usesbutton and makes it rendered again so that it's listeners are back
            if (component.getName() != null && component.getName().equals("usesbutton")) {
                component.setVisible(true);
                for (int j = 0; j < component.components.size(); j++) {
                    component.components.get(j).setVisible(true);
                }
            }
        }

        //removing the textfields
        for (int i = 0; i < textFields.size(); i++) {
            if(labels.get(i).getName() != null && labels.get(i).getName().equals("tf")) {
                if (minipanels.get(0) instanceof Tipbox) {
                    labelTexts.set(i, textFields.get(i).getTextField().getText()); //updating the label list's text

                    labels.get(i).getLabel().setText(shortenString(labelTexts.get(i), labels.get(i).getWidth())); //updating the label's text
                }
            }
            else {
                labelTexts.set(i, textFields.get(i).getTextField().getText()); //updating the label list's text

                labels.get(i).getLabel().setText(shortenString(labelTexts.get(i), labels.get(i).getWidth())); //updating the label's text

                textFields.get(i).setVisible(false);
            }
        }

        for (int i = 0; i < restButtons.size(); i++) {
            //if the selected rest button is a short rest...
            if(restButtons.get(i).getButton().isChecked() && i == 0){
                //set the short rest max uses to the textfield value
                srMax = Integer.parseInt(labelTexts.get(usesIndexInNames));
                //if the long rest max is smaller than the short rest mex set the long rest max uses to the textfield value too
                if(lrMax < srMax) lrMax = Integer.parseInt(labelTexts.get(usesIndexInNames));
            }
            //if the selected rest button is a long rest...
            if(restButtons.get(i).getButton().isChecked() && i == 1){
                //set the long rest max uses to the textfield value
                lrMax = Integer.parseInt(labelTexts.get(usesIndexInNames));
            }
            restButtons.get(i).setVisible(false);
        }

        spellDesc.setSoftVisible(false);

        editMode = false;
    }

    public Tipbox getTipbox(){
        return spellDesc;
    }
    public MBButton getUsesButton(){
        if (usesButton != null) return usesButton;
        return null;
    }

    public ArrayList<Item2> getItems(){
        return null;
    }
}

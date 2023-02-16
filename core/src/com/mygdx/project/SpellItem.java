package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class SpellItem extends Item {
    private Tipbox spellDesc;

    int usesIndexInNames = -1;
    private MBButton usesButton;
    int uses = 0;
    public int srMax = 0;
    public int lrMax = 0;
    ArrayList<MBButton> restButtons = new ArrayList<>();

    public SpellItem(Screen screen) {
        super(screen);
    }
    public SpellItem(Rectangle position, Screen screen) {
        super(position, screen);
    }
    public void initialize(){
        //region labels
        MBLabel nameLabel, descLabel, usesLabel;

        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Spell  "+ (ID+1), screen);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        labelTexts.add(nameLabel.getLabel().getText().toString());

        descLabel = new MBLabel("Spell Description...", screen);
        descLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        descLabel.setSize(257, nameLabel.getHeight());
        descLabel.setName("tf"); //setting the name so I can identify it later
        labelTexts.add(descLabel.getLabel().getText().toString());

        usesLabel = new MBLabel(String.valueOf(uses), screen);
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
        final MBButton usesButton = new MBButton(String.valueOf(uses), screen),
                minusButton = new MBButton(screen, "down-button"),
                plusButton = new MBButton(screen, "up-button"),
                editButton = new MBButton(screen, "edit-toggle"),
                delButton = new MBButton(screen, "delete-button"),
                downButton = new MBButton(screen, "down-button"),
                upButton = new MBButton(screen, "up-button");

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

        editButton.getButton().setChecked(true);
        editButton.setName("editbutton");
        editButton.getButton().setName("editbutton"); //setting the name so I can identify it later
        editButton.setPosition((usesLabel.getX()+usesLabel.getWidth()+8), nameLabel.getY()-1);
        editButton.setSize(20, 15);

        delButton.setPosition(editButton.getX(), editButton.getY()+editButton.getHeight()+2);
        delButton.setSize(20, 15);

        downButton.setPosition((delButton.getX()+delButton.getWidth()+2), editButton.getY());
        downButton.setSize(20, 15);

        upButton.setPosition(downButton.getX(), downButton.getY()+downButton.getHeight()+2);
        upButton.setSize(20, 15);

        add(usesButton);
        add(editButton);
        add(delButton);
        add(downButton);
        add(upButton);
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
                System.out.println("minus clicked " + (getID() + 1));
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
                System.out.println("plus clicked "+(getID() + 1));
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

        final MBButton srButton = new MBButton("Short Rest", screen);
        srButton.getButton().setStyle(Main.skin.get("toggle", TextButton.TextButtonStyle.class));
        srButton.getButton().setChecked(true);
        srButton.setSize(50, editButton.getHeight());
        srButton.setPosition(usesButton.getX()-srButton.getWidth()-2, delButton.getY());
        ((TextButton)srButton.getButton()).getLabel().setFontScale(.6f,.7f);

        final MBButton lrButton = new MBButton("Long Rest", screen);
        lrButton.getButton().setStyle(Main.skin.get("toggle", TextButton.TextButtonStyle.class));
        lrButton.getButton().setChecked(false);
        lrButton.setSize(50, delButton.getHeight());
        lrButton.setPosition(srButton.getX(), editButton.getY());
        ((TextButton)lrButton.getButton()).getLabel().setFontScale(.6f,.7f);
        srButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("short rest " +(getID() + 1));
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
                System.out.println("long rest "+(getID() + 1));
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
        editButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("S Edit Button " + (getID() +1));

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
        delButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Delete Button " + (getID() +1));

                deleteThisItem();
            }
        });

        //swaps this item with the item under it
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Down Button " + (getID() + 1));

                int currSpot = spot;
                int nextSpot = getSpot() + 1;
                //if it's not at the bottom...
                if (nextSpot < parentIP.getNextAvaSpot()) {
                    Item nextItem = parentIP.getItemBySpot(nextSpot);

                    setSpot(nextSpot);
                    nextItem.setSpot(currSpot);

                    reformat();
                    nextItem.reformat();

                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(nextItem.isInEditMode()){
                        nextItem.saveEdit();
                        nextItem.edit();
                    }
                }
            }
        });

        upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Up Button "+ (getID() + 1));

                int currSpot = getSpot();
                int prevSpot = getSpot() - 1;
                //if it's not at the top...
                if(prevSpot >= 0) {
                    Item prevItem = getItemBySpot(prevSpot);

                    setSpot(prevSpot);
                    prevItem.setSpot(currSpot);

                    reformat();
                    prevItem.reformat();

                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(prevItem.isInEditMode()){
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
            textFields.add(new MBTextField(labelTexts.get(i), screen, false, true));
            if(labels.get(i).getName() != null && labels.get(i).getName().equals("tf")){ //banishing the spell desc label's real textfield
                textFields.get(i).setVisible(false);
                textFields.get(i).setPosition(-1, -1);
                textFields.get(i).setSize(0,0);
            }
            add(textFields.get(i));
            //detecting when enter is pressed on each MBTextField so that enter can exit out of edit mode
            textFields.get(i).setClosingAction(new Action() {
                @Override
                public boolean act(float v) {
                    saveEdit();
                    return false;
                }
            });
        }
        //endregion

        //region tipbox components
        spellDesc = new Tipbox(new Rectangle(115, descLabel.getY()+ (descLabel.getHeight()/2)-300, 770, 300), screen);
        MBTextArea spellDescTF = new MBTextArea("", screen);
        spellDescTF.setPosition(spellDesc.getX()+10, spellDesc.getY()+10+35);
        spellDescTF.setSize(750, 225);

        MBLabel spellLevel = new MBLabel("Level: ", screen),
                castTime = new MBLabel("Casting Time: ", screen),
                duration = new MBLabel("Duration: ", screen),
                range = new MBLabel("Range: ", screen), damageType = new MBLabel("Damage Type: ", screen);
        MBTextField spellLevelTF = new MBTextField("0", screen),
                castTimeTF = new MBTextField("Action", screen),
                durationTF = new MBTextField("Instant", screen),
                rangeTF = new MBTextField("5 ft", screen),
                damageTypeTF = new MBTextField("Fire", screen);

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

        add(spellDesc, 1);
        //endregion

        //starting the item in edit mode so the user can immediately edit the item text
        edit();

    }

    public void edit() {
        //to have only one item edited at a time
        if(parentIP.getItems() != null) {
            for (Item item : parentIP.getItems()) {
                if(item != this) item.saveEdit(); //if this isn't the item being edited...
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
                spellDesc.getTextArea().setText(labelTexts.get(i));
                labels.get(i).getLabel().setText("");
            }
            else {
                textFields.get(i).getTextField().setText(labelTexts.get(i));
                labels.get(i).getLabel().setText("");
                textFields.get(i).setPosition(labels.get(i).getX(), getY() + 5);
                textFields.get(i).setSize(labels.get(i).getWidth(), 30);
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
                    labelTexts.set(i, ((Tipbox)minipanels.get(0)).getTextArea().getText()); //updating the label list's text

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
    public void shortRest(){
        ((TextButton)usesButton.getButton()).setText(String.valueOf(srMax));
        uses = srMax;
    }
    public void longRest(){
        ((TextButton)usesButton.getButton()).setText(String.valueOf(lrMax));
        uses = lrMax;
    }

    public ArrayList<Item> getItems(){
        return null;
    }
}

package com.mygdx.project.Panels;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.project.Components.*;
import com.mygdx.project.Screen;

public class WeaponItem extends Item {

    public WeaponItem(Screen screen) {
        super(screen);
    }
    public WeaponItem(Rectangle position, Screen screen) {
        super(position, screen);
    }

    public void initialize(){
        //region labels
        //creating the labels
        MBLabel nameLabel,diceLabel,modLabel,typeLabel;
        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Weapon "+ (ID+1), screen);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        labelTexts.add(nameLabel.getLabel().getText().toString());

        diceLabel = new MBLabel("Hit Die", screen);
        diceLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        diceLabel.setSize(75, nameLabel.getHeight());
        labelTexts.add(diceLabel.getLabel().getText().toString());

        modLabel = new MBLabel("ATK Mod", screen);
        modLabel.setPosition(diceLabel.getX()+ diceLabel.getWidth()+2, nameLabel.getY());
        modLabel.setSize(85, nameLabel.getHeight());
        labelTexts.add( modLabel.getLabel().getText().toString());

        typeLabel = new MBLabel("Damage/Type", screen);
        typeLabel.setPosition(modLabel.getX()+ modLabel.getWidth()+2, nameLabel.getY());
        typeLabel.setSize(115, nameLabel.getHeight());
        labelTexts.add( typeLabel.getLabel().getText().toString());

        labels.add(nameLabel);
        labels.add(diceLabel);
        labels.add(modLabel);
        labels.add(typeLabel);

        //adding labels to this Item
        add(nameLabel);
        add(diceLabel);
        add(modLabel);
        add(typeLabel);
        //endregion

        //region buttons
        //creating buttons and setting their positions and sizes
        final MBButton editButton = new MBButton(screen, "edit-toggle");
        editButton.getButton().setChecked(true);
        editButton.setName("editbutton");
        editButton.getButton().setName("editbutton");
        editButton.setPosition(typeLabel.getX()+typeLabel.getWidth()+10, nameLabel.getY()-1);
        editButton.setSize(20, 15);

        final MBButton delButton = new MBButton(screen, "delete-button");
        delButton.setPosition(editButton.getX(), editButton.getY()+editButton.getHeight()+2);
        delButton.setSize(20, 15);

        final MBButton downButton = new MBButton(screen, "down-button");
        downButton.setPosition(editButton.getX()+editButton.getWidth()+2, editButton.getY());
        downButton.setSize(20, 15);

        final MBButton upButton = new MBButton(screen, "up-button");
        upButton.setPosition(downButton.getX(), downButton.getY()+downButton.getHeight()+2);
        upButton.setSize(20, 15);

        add(editButton);
        add(delButton);
        add(downButton);
        add(upButton);
        //endregion

        //region textfields
        if(statBlock == null){
            statBlock = screen.getStats().newItemStatBlock(0, labelTexts.size());
        }
        else {
            for (int i = 0; i < labelTexts.size(); i++) {
                String text = screen.getStats().getValue(statBlock + i).toString();
                if(!text.equals("")) labelTexts.set(i, text);
            }
        }
        //creating textfields and setting their texts to their corresponding label's text
        for (int i = 0; i < labelTexts.size(); i++) {
            textFields.add(new MBTextField(labelTexts.get(i), screen, statBlock + i, false, true));

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

        //region button listeners
        //changes this item in and out of edit mode
        editButton.addListener(new ChangeListener() {
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
        delButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Delete Button " + (ID +1));

                deleteThisItem();
            }
        });

        //swaps this item with the item under it
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Down Button " + (ID + 1));

                int currSpot = spot;
                int nextSpot = spot + 1;
                //if it's not at the bottom...
                if (nextSpot < parentIP.getNextAvaSpot()) {
                    Item nextItem = getItemBySpot(nextSpot);

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
                System.out.println("Up Button "+ (ID + 1));

                int currSpot = spot;
                int prevSpot = spot - 1;
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

        //starting the item in edit mode so the user can immediately edit the item text
        edit();
    }

    /**
     * makes the textfields appear above the labels
     */
    public void edit(){
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
        }

        //loops through this item's textfields list and updates their positions, re-adds them to the list, and sets their hard visibility to true
        for (int i = 0; i < textFields.size(); i++) {
//            textFields.get(i).getTextField().setText(labelTexts.get(i));
            labels.get(i).getLabel().setText("");
            textFields.get(i).setPosition(labels.get(i).getX(), getY() + 5);
            textFields.get(i).setSize(labels.get(i).getWidth(), 30);
            textFields.get(i).setVisible(true);
        }

        editMode = true;
    }
    /**
     * sets the labels to the text of the textfields and makes them disappear
     */
    public void saveEdit(){
        for (MBComponent component : components) {
            //finds editbutton and unchecks it
            if (component.getName() != null && component.getName().equals("editbutton") && component instanceof MBButton) {
                ((MBButton) component).getButton().setChecked(false);
            }
        }

        //making the textfields invisible
        for (int i = 0; i < textFields.size(); i++) {
            if(textFields.get(i).getSystem() == null) {
                MBSystem sys = new MBSystem(textFields.get(i), labels.get(i));
                final int finalI = i;
                sys.setUpdateAction(new Action() {
                    @Override
                    public boolean act(float v) {
                        labelTexts.set(finalI, textFields.get(finalI).getTextField().getText()); //updating the label list's text

                        labels.get(finalI).getLabel().setText(shortenString(labelTexts.get(finalI), labels.get(finalI).getWidth())); //updating the label's text

                        return true;
                    }
                });
            }
            textFields.get(i).updateSystem();

            textFields.get(i).setVisible(false);
        }

        editMode = false;
    }

    public int getSpot(){
        return spot;
    }
}

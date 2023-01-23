package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

public class WeaponItem extends Item2{
    private static int totalItems = 0;
    private static ArrayList<Item2> allItems = new ArrayList<>();
    private static int nextAvaSpot = 0;

    public WeaponItem() {
        super();

        this.spot = nextAvaSpot;
        ID = totalItems;

        allItems.add(this);
        //increasing the total number of items by one (this item's ID was already set when it was created (code in panel class))
        totalItems++;
        //increasing the next available spot by one
        nextAvaSpot++;

        //region labels
        //creating the labels
        MBLabel nameLabel,diceLabel,modLabel,typeLabel;
        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel("Weapon "+ ID, skin);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        labelTexts.add(nameLabel.getLabel().getText().toString());

        diceLabel = new MBLabel("HitDie", skin);
        diceLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        diceLabel.setSize(75, nameLabel.getHeight());
        labelTexts.add(diceLabel.getLabel().getText().toString());

        modLabel = new MBLabel("ATKMod", skin);
        modLabel.setPosition(diceLabel.getX()+ diceLabel.getWidth()+2, nameLabel.getY());
        modLabel.setSize(85, nameLabel.getHeight());
        labelTexts.add( modLabel.getLabel().getText().toString());

        typeLabel = new MBLabel("Damage/Type", skin);
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
        final MBButton editButton = new MBButton(skin, "edit-toggle");
        editButton.getButton().setChecked(true);
        editButton.setName("editbutton");
        editButton.getButton().setName("editbutton");
        editButton.setPosition(typeLabel.getX()+typeLabel.getWidth()+10, nameLabel.getY()-1);
        editButton.setSize(20, 15);

        final MBButton delButton = new MBButton(skin, "delete-button");
        delButton.setPosition(editButton.getX(), editButton.getY()+editButton.getHeight()+2);
        delButton.setSize(20, 15);

        final MBButton downButton = new MBButton(skin, "down-button");
        downButton.setPosition(editButton.getX()+editButton.getWidth()+2, editButton.getY());
        downButton.setSize(20, 15);

        final MBButton upButton = new MBButton(skin, "up-button");
        upButton.setPosition(downButton.getX(), downButton.getY()+downButton.getHeight()+2);
        upButton.setSize(20, 15);

        add(editButton);
        add(delButton);
        add(downButton);
        add(upButton);
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

                int currSpot = getSpot();
//                shuffleItemsUp(currSpot);

                for (int i = components.size()-1; i >= 0; i--) {
                    delete(components.get(i));
                }
                parentPanel.delete(WeaponItem.this);
            }
        });

        //swaps this item with the item under it
        downButton.addListener(new ChangeListener() {
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

        upButton.addListener(new ChangeListener() {
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

        //starting the item in edit mode so the user can immediately edit the item text
        edit();
    }

    /**
     * makes the textfields appear above the labels
     */
    public void edit(){
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
        }

        //loops through this item's textfields list and updates their positions, re-adds them to the list, and sets their hard visibility to true
        for (int i = 0; i < textFields.size(); i++) {
            textFields.get(i).getTextField().setText(labelTexts.get(i));
            labels.get(i).getLabel().setText("");
            textFields.get(i).setPosition(labels.get(i).getX(), getY() + 5);
            textFields.get(i).setSize(labels.get(i).getWidth(), 30);
//            add(textFields.get(i));
            textFields.get(i).setVisible(true);
        }

        editMode = true;
    }

    public ArrayList<Item2> getItems() {
        return allItems;
    }
    public int getSpot(){
        return spot;
    }
    public Item2 getItemBySpot(int spot){
        for (Item2 item : allItems) {
            if (item.getSpot() == spot) {
                return item;
            }
        }
        return null;
    }
}

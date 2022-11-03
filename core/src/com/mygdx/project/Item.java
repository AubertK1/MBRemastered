package com.mygdx.project;

import com.badlogic.gdx.Gdx;
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
    //textfields for when you edit the item (if weapon)
    MBTextField nameLabelTF, hitDieLabelTF, modLabelTF, typeLabelTF;
    //labels for the item (if weapon)
    MBLabel nameLabel,diceLabel,modLabel,typeLabel;

    public Item(String text, int spot) {
        super("core\\pics\\TopbarPanel.png", new Rectangle(125, 790, 460, 40));
        //if this item is a weapon it sets it up as a weapon item
        makeWeaponItem(text, spot);
    }

    /**
     * sets up the item as a weapon item
     * @param text name label text
     * @param spot its initial spot
     */
    public void makeWeaponItem(String text, int spot){
        //assigning this item's spot to the given spot
        this.spot=spot;
        //increasing the total number of items by one (this item's ID was already set when it was created (code in panel class))
        totalID++;
        //increasing the next available spot by one
        nextAvaSpot++;
        //setting the labels' texts and positions and sizes
        nameLabel = new MBLabel(text, uiSkin);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        nameLabel.setSize(119, nameLabel.getHeight());
        name = nameLabel.label.getText().toString();

        diceLabel = new MBLabel("HitDie", uiSkin);
        diceLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        diceLabel.setSize(75, nameLabel.getHeight());
        hitDie = diceLabel.label.getText().toString();

        modLabel = new MBLabel("ATKMod", uiSkin);
        modLabel.setPosition(diceLabel.getX()+ diceLabel.getWidth()+2, nameLabel.getY());
        modLabel.setSize(85, nameLabel.getHeight());
        mod = modLabel.label.getText().toString();

        typeLabel = new MBLabel("Damage/Type", uiSkin);
        typeLabel.setPosition(modLabel.getX()+ modLabel.getWidth()+2, nameLabel.getY());
        typeLabel.setSize(115, nameLabel.getHeight());
        type = typeLabel.label.getText().toString();
        //creating buttons and setting their positions and sizes
        final MBButton itemButtonEdit = new MBButton(uiSkin);
        itemButtonEdit.setPosition((typeLabel.getX()+typeLabel.getWidth()+10), nameLabel.getY()-1);
        itemButtonEdit.setSize(20, 15);

        final MBButton itemButtonDel = new MBButton(uiSkin);
        itemButtonDel.setPosition((typeLabel.getX()+typeLabel.getWidth()+10), itemButtonEdit.getY()+itemButtonEdit.getHeight()+2);
        itemButtonDel.setSize(20, 15);

        final MBButton itemButtonDown = new MBButton(uiSkin);
        itemButtonDown.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonEdit.getY());
        itemButtonDown.setSize(20, 15);

        final MBButton itemButtonUp = new MBButton(uiSkin);
        itemButtonUp.setPosition((itemButtonDel.getX()+itemButtonDel.getWidth()+2), itemButtonDown.getY()+itemButtonDown.getHeight()+2);
        itemButtonUp.setSize(20, 15);
        //setting the textfields' values
        nameLabelTF = new MBTextField(name, uiSkin);
        hitDieLabelTF = new MBTextField(hitDie, uiSkin);
        modLabelTF = new MBTextField(mod, uiSkin);
        typeLabelTF = new MBTextField(type, uiSkin);
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
        //detecting when enter is pressed on each MBTextField so that enter can exit out of edit mode
        nameLabelTF.setKeyListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == '\n'){
                    System.out.println("hey");
                    saveEdit();
                    editMode = false;
                }
            }
        });
        hitDieLabelTF.setKeyListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == '\n'){
                    System.out.println("hey");
                    saveEdit();
                    editMode = false;
                }
            }
        });
        modLabelTF.setKeyListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == '\n'){
                    System.out.println("hey");
                    saveEdit();
                    editMode = false;
                }
            }
        });
        typeLabelTF.setKeyListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == '\n'){
                    System.out.println("hey");
                    saveEdit();
                    editMode = false;
                }
            }
        });
        //setting the buttons' functions
        //changes this item in and out of edit mode
        itemButtonEdit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Edit Button " + (itemButtonEdit.getItem().ID+1));
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
                System.out.println("Delete Button " + (itemButtonDel.getItem().ID+1));

                int currSpot = itemButtonDown.getItem().spot;
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
                System.out.println("Down Button " + (itemButtonDown.getItem().ID+1));
                //initializing spots into set temporary variables
                int currSpot = itemButtonDown.getItem().spot;
                int nextSpot = itemButtonDown.getItem().spot + 1;
                //if it's not at the bottom...
                if (nextSpot < (itemButtonDown.getItem().getPanel().minipanels.size())) {
                    //reassigns the spots to different variables
                    //assigns this item's spot to an arbitrary number so that the two items are never at the same spot
                    itemButtonDown.getItem().spot = -100;
                    itemButtonDown.getItem().getPanel().getMPBySpot(nextSpot).spot = currSpot;
                    itemButtonDown.getItem().spot = nextSpot;
                    //making it easier to read
                    ArrayList<MBComponent> thisItemComponents = itemButtonDown.getItem().components;
                    //repositioning this item to its new spot
                    //name label
                    thisItemComponents.get(0).setPosition(itemButtonDown.getItem().getX() + 5, itemButtonDown.getItem().getY() + 5);
                    //hit die label
                    thisItemComponents.get(1).setPosition(thisItemComponents.get(0).getX()+ thisItemComponents.get(0).getWidth()+2, itemButtonDown.getItem().getY() + 5);
                    //attack mod label
                    thisItemComponents.get(2).setPosition(thisItemComponents.get(1).getX()+ thisItemComponents.get(1).getWidth()+2, itemButtonDown.getItem().getY() + 5);
                    //damage type label
                    thisItemComponents.get(3).setPosition(thisItemComponents.get(2).getX()+ thisItemComponents.get(2).getWidth()+2, itemButtonDown.getItem().getY() + 5);
                    //edit button
                    thisItemComponents.get(4).setPosition(thisItemComponents.get(3).getX() + thisItemComponents.get(3).getWidth() + 10, thisItemComponents.get(3).getY()-1);
                    //delete button
                    thisItemComponents.get(5).setPosition(thisItemComponents.get(3).getX() + thisItemComponents.get(3).getWidth() + 10, (thisItemComponents.get(4).getY()+ thisItemComponents.get(4).getHeight()+2));
                    //swap down button
                    thisItemComponents.get(6).setPosition(thisItemComponents.get(5).getX() + thisItemComponents.get(5).getWidth() + 2, (thisItemComponents.get(4).getY()));
                    //swap up button
                    thisItemComponents.get(7).setPosition(thisItemComponents.get(5).getX() + thisItemComponents.get(5).getWidth() + 2, (thisItemComponents.get(5).getY()));

                    //repositioning the item that you swapped it with to this item's spot
                    //making it easier to read
                    ArrayList<MBComponent> nextItemComponents = itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components;
                    //name label
                    nextItemComponents.get(0).setPosition(itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getX() + 5, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //hit die label
                    nextItemComponents.get(1).setPosition(thisItemComponents.get(0).getX()+ thisItemComponents.get(0).getWidth()+2, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //attack mod label
                    nextItemComponents.get(2).setPosition(thisItemComponents.get(1).getX()+ thisItemComponents.get(1).getWidth()+2, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //damage type label
                    nextItemComponents.get(3).setPosition(thisItemComponents.get(2).getX()+ thisItemComponents.get(2).getWidth()+2, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //edit button
                    nextItemComponents.get(4).setPosition((nextItemComponents.get(3).getX() + nextItemComponents.get(3).getWidth() + 10), nextItemComponents.get(3).getY()-1);
                    //delete button
                    nextItemComponents.get(5).setPosition((nextItemComponents.get(3).getX() + nextItemComponents.get(3).getWidth() + 10), nextItemComponents.get(4).getHeight()+ nextItemComponents.get(4).getY()+2);
                    //swap down button
                    nextItemComponents.get(6).setPosition((nextItemComponents.get(4).getX() + nextItemComponents.get(4).getWidth() + 2), nextItemComponents.get(4).getY());
                    //swap up button
                    nextItemComponents.get(7).setPosition((nextItemComponents.get(4).getX() + nextItemComponents.get(4).getWidth() + 2), nextItemComponents.get(5).getY());
                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getEditMode()){
                        itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).saveEdit();
                        itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).edit();
                    }
                }
            }
        });
        itemButtonUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //filler just for my entertainment
                System.out.println("Up Button "+(itemButtonUp.getItem().ID+1));
                //initializing spots into set temporary variables
                int currSpot = itemButtonUp.getItem().spot;
                int prevSpot = itemButtonUp.getItem().spot-1;
                //if it's not at the top...
                if(prevSpot>=0) {
                    //reassigns the spots to different variables
                    //assigns this item's spot to an arbitrary number so that the two items are never at the same spot
                    itemButtonUp.getItem().spot = -100;
                    itemButtonUp.getItem().getPanel().getMPBySpot(prevSpot).spot = currSpot;
                    itemButtonUp.getItem().spot = prevSpot;
                    //making it easier to read
                    ArrayList<MBComponent> thisItemComponents = itemButtonUp.getItem().components;
                    //repositioning this item to its new spot
                    //name label
                    thisItemComponents.get(0).setPosition(itemButtonUp.getItem().getX() + 5, itemButtonUp.getItem().getY() + 5);
                    //hit die label
                    thisItemComponents.get(1).setPosition(thisItemComponents.get(0).getX()+ thisItemComponents.get(0).getWidth()+2, itemButtonUp.getItem().getY() + 5);
                    //attack mod label
                    thisItemComponents.get(2).setPosition(thisItemComponents.get(1).getX()+ thisItemComponents.get(1).getWidth()+2, itemButtonUp.getItem().getY() + 5);
                    //damage type label
                    thisItemComponents.get(3).setPosition(thisItemComponents.get(2).getX()+ thisItemComponents.get(2).getWidth()+2, itemButtonUp.getItem().getY() + 5);
                    //edit button
                    thisItemComponents.get(4).setPosition(thisItemComponents.get(3).getX() + thisItemComponents.get(3).getWidth() + 10, thisItemComponents.get(3).getY()-1);
                    //delete button
                    thisItemComponents.get(5).setPosition(thisItemComponents.get(3).getX() + thisItemComponents.get(3).getWidth() + 10, (thisItemComponents.get(4).getY()+ thisItemComponents.get(4).getHeight()+2));
                    //swap down button
                    thisItemComponents.get(6).setPosition(thisItemComponents.get(5).getX() + thisItemComponents.get(5).getWidth() + 2, (thisItemComponents.get(4).getY()));
                    //swap up button
                    thisItemComponents.get(7).setPosition(thisItemComponents.get(5).getX() + thisItemComponents.get(5).getWidth() + 2, (thisItemComponents.get(5).getY()));

                    //repositioning the item that you swapped it with to this item's spot
                    //making it easier to read
                    ArrayList<MBComponent> prevItemComponents = itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components;
                    //name label
                    prevItemComponents.get(0).setPosition(itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getX() + 5, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //hit die label
                    prevItemComponents.get(1).setPosition(thisItemComponents.get(0).getX()+ thisItemComponents.get(0).getWidth()+2, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //attack mod label
                    prevItemComponents.get(2).setPosition(thisItemComponents.get(1).getX()+ thisItemComponents.get(1).getWidth()+2, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //damage type label
                    prevItemComponents.get(3).setPosition(thisItemComponents.get(2).getX()+ thisItemComponents.get(2).getWidth()+2, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    //edit button
                    prevItemComponents.get(4).setPosition((prevItemComponents.get(3).getX() + prevItemComponents.get(3).getWidth() + 10), prevItemComponents.get(3).getY()-1);
                    //delete button
                    prevItemComponents.get(5).setPosition((prevItemComponents.get(3).getX() + prevItemComponents.get(3).getWidth() + 10), prevItemComponents.get(4).getHeight()+ prevItemComponents.get(4).getY()+2);
                    //swap down button
                    prevItemComponents.get(6).setPosition((prevItemComponents.get(4).getX() + prevItemComponents.get(4).getWidth() + 2), prevItemComponents.get(4).getY());
                    //swap up button
                    prevItemComponents.get(7).setPosition((prevItemComponents.get(4).getX() + prevItemComponents.get(4).getWidth() + 2), prevItemComponents.get(5).getY());
                    //moves the textfields with this item if in edit mode
                    if(editMode){
                        saveEdit();
                        edit();
                    }
                    //moves the textfields of the item being swapped if it is in edit mode
                    if(itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getEditMode()){
                        itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).saveEdit();
                        itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).edit();
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
        //adds the component given to this item
        components.add(component);
        //sets the component's parent to this item
        component.parent = this;
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

        nameLabelTF.setPosition(item.getX()+5, item.getY()+4);
        nameLabelTF.setSize(119, 32);

        hitDieLabelTF.setPosition(nameLabelTF.getX()+ nameLabelTF.getWidth()+2, nameLabelTF.getY());
        hitDieLabelTF.setSize(75, nameLabelTF.getHeight());

        modLabelTF.setPosition(hitDieLabelTF.getX()+ hitDieLabelTF.getWidth()+2, nameLabelTF.getY());
        modLabelTF.setSize(85, nameLabelTF.getHeight());

        typeLabelTF.setPosition(modLabelTF.getX()+ modLabelTF.getWidth()+2, nameLabelTF.getY());
        typeLabelTF.setSize(115, nameLabelTF.getHeight());

        add(nameLabelTF);
        add(hitDieLabelTF);
        add(modLabelTF);
        add(typeLabelTF);

        nameLabelTF.textField.setVisible(true);
        hitDieLabelTF.textField.setVisible(true);
        modLabelTF.textField.setVisible(true);
        typeLabelTF.textField.setVisible(true);
    }

    /**
     * sets the labels to the text of the textfields and makes them disappear
     */
    public void saveEdit(){
        name = nameLabelTF.textField.getText();
        hitDie = hitDieLabelTF.textField.getText();
        mod = modLabelTF.textField.getText();
        type = typeLabelTF.textField.getText();

        nameLabel.label.setText(name);
        diceLabel.label.setText(hitDie);
        modLabel.label.setText(mod);
        typeLabel.label.setText(type);

        nameLabelTF.setVisible(false);
        hitDieLabelTF.setVisible(false);
        modLabelTF.setVisible(false);
        typeLabelTF.setVisible(false);

        remove(nameLabelTF);
        remove(hitDieLabelTF);
        remove(modLabelTF);
        remove(typeLabelTF);
    }

    /**
     * shuffles all the items up
     */
    public void shuffleItemsUp(){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot--;
        //loops through all the items
        for(Item item : getPanel().items){
            item.spot--;
            //replaces the item's components
            item.components.get(0).setPosition(item.getX() + 5, item.getY() + 5);
            item.components.get(1).setPosition(item.components.get(0).getX()+ item.components.get(0).getWidth()+2, item.getY() + 5);
            item.components.get(2).setPosition(item.components.get(1).getX()+ item.components.get(1).getWidth()+2, item.getY() + 5);
            item.components.get(3).setPosition(item.components.get(2).getX()+ item.components.get(2).getWidth()+2, item.getY() + 5);
            item.components.get(4).setPosition(item.components.get(3).getX() + item.components.get(3).getWidth() + 10, item.components.get(3).getY()-1);
            item.components.get(5).setPosition(item.components.get(3).getX() + item.components.get(3).getWidth() + 10, (item.components.get(4).getY()+item.components.get(4).getHeight()+2));
            item.components.get(6).setPosition(item.components.get(5).getX() + item.components.get(5).getWidth() + 2, (item.components.get(4).getY()));
            item.components.get(7).setPosition(item.components.get(5).getX() + item.components.get(5).getWidth() + 2, (item.components.get(5).getY()));
            //moves the textfields with the item if in edit mode
            if(item.editMode){
                item.saveEdit();
                item.edit();
            }
        }
    }

    /**
     * shuffles all the items up starting from a spot
     * @param startSpot the lowest spot you don't want to raise
     */
    public void shuffleItemsUp(int startSpot){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot--;
        //loops through all the items
        for(Item item : getPanel().items) {
            if (item.spot > startSpot) {
                item.spot--;
                //replaces the item's components
                item.components.get(0).setPosition(item.getX() + 5, item.getY() + 5);
                item.components.get(1).setPosition(item.components.get(0).getX() + item.components.get(0).getWidth() + 2, item.getY() + 5);
                item.components.get(2).setPosition(item.components.get(1).getX() + item.components.get(1).getWidth() + 2, item.getY() + 5);
                item.components.get(3).setPosition(item.components.get(2).getX() + item.components.get(2).getWidth() + 2, item.getY() + 5);
                item.components.get(4).setPosition(item.components.get(3).getX() + item.components.get(3).getWidth() + 10, item.components.get(3).getY() - 1);
                item.components.get(5).setPosition(item.components.get(3).getX() + item.components.get(3).getWidth() + 10, (item.components.get(4).getY() + item.components.get(4).getHeight() + 2));
                item.components.get(6).setPosition(item.components.get(5).getX() + item.components.get(5).getWidth() + 2, (item.components.get(4).getY()));
                item.components.get(7).setPosition(item.components.get(5).getX() + item.components.get(5).getWidth() + 2, (item.components.get(5).getY()));
                //moves the textfields with the item if in edit mode
                if (item.editMode) {
                    item.saveEdit();
                    item.edit();
                }
            }
        }
    }

    /**
     * shuffles all the items down
     */
    public void shuffleItemsDown(){
        //increases the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot++;
        //loops through all the items
        for(Item item : getPanel().items){
            item.spot++;
            //replaces the item's components
            item.components.get(0).setPosition(item.getX() + 5, item.getY() + 5);
            item.components.get(1).setPosition(item.components.get(0).getX()+ item.components.get(0).getWidth()+2, item.getY() + 5);
            item.components.get(2).setPosition(item.components.get(1).getX()+ item.components.get(1).getWidth()+2, item.getY() + 5);
            item.components.get(3).setPosition(item.components.get(2).getX()+ item.components.get(2).getWidth()+2, item.getY() + 5);
            item.components.get(4).setPosition(item.components.get(3).getX() + item.components.get(3).getWidth() + 10, item.components.get(3).getY()-1);
            item.components.get(5).setPosition(item.components.get(3).getX() + item.components.get(3).getWidth() + 10, (item.components.get(4).getY()+item.components.get(4).getHeight()+2));
            item.components.get(6).setPosition(item.components.get(5).getX() + item.components.get(5).getWidth() + 2, (item.components.get(4).getY()));
            item.components.get(7).setPosition(item.components.get(5).getX() + item.components.get(5).getWidth() + 2, (item.components.get(5).getY()));
            //moves the textfields with the item if in edit mode
            if(item.editMode){
                item.saveEdit();
                item.edit();
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
        return spot;
    }
    /**
     * @return returns this item's y value
     */
    public float getY(){
        return (position.y-((position.height+5)*spot));
    }

    /**
     * renders this item and any minipanels it may hold
     * @param batch the batch...
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, (position.y-((position.height+5)*spot)), position.width, position.height);
        for (int i = 0; i < minipanels.size(); i++) {
            minipanels.get(i).render(batch);
        }
    }

}

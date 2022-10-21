package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Null;

import java.awt.*;

public class Item extends Minipanel{
//    int spot;
Skin uiSkin = new Skin (Gdx.files.internal(
        "assets\\skins\\uiskin.json"));
String name;
String hitDie;
String mod;
String type;

    MBTextField nameLabelTF;
    MBTextField diceLabelTF;
    MBTextField modLabelTF;
    MBTextField typeLabelTF;

    MBLabel nameLabel,diceLabel,modLabel,typeLabel;

    boolean editMode = false;
    public Item(String text, int spot) {
        super("core\\pics\\TopbarPanel.png", new Rectangle(125, 790, 460, 40));
        this.spot=spot;
        nameLabel = new MBLabel(text, uiSkin);
		nameLabel.setSize(119, nameLabel.getHeight());
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
        name = nameLabel.label.getText().toString();
//        nameLabel.center();

        diceLabel = new MBLabel("HitDie", uiSkin);
        diceLabel.setPosition(nameLabel.getX()+ nameLabel.getWidth()+2, nameLabel.getY());
        diceLabel.setSize(75, nameLabel.getHeight());
        hitDie = diceLabel.label.getText().toString();
//        diceLabel.center();

        modLabel = new MBLabel("ATKMod", uiSkin);
        modLabel.setPosition(diceLabel.getX()+ diceLabel.getWidth()+2, nameLabel.getY());
        modLabel.setSize(85, nameLabel.getHeight());
        mod = modLabel.label.getText().toString();

//        modLabel.center();

        typeLabel = new MBLabel("Damage/Type", uiSkin);
        typeLabel.setPosition(modLabel.getX()+ modLabel.getWidth()+2, nameLabel.getY());
        typeLabel.setSize(115, nameLabel.getHeight());
        type = typeLabel.label.getText().toString();

//        typeLabel.center();
//        MBTextField itemTextField = new MBTextField("", uiSkin);
//        itemTextField.setPosition(200, this.getY()+4);
//        itemTextField.setSize(340, itemTextField.getHeight());

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

        nameLabelTF = new MBTextField(name, uiSkin);
        diceLabelTF = new MBTextField(hitDie, uiSkin);
        modLabelTF = new MBTextField(mod, uiSkin);
        typeLabelTF = new MBTextField(type, uiSkin);

        add(nameLabel);
        add(diceLabel);
        add(modLabel);
        add(typeLabel);
        add(itemButtonEdit);
        add(itemButtonDel);
        add(itemButtonDown);
        add(itemButtonUp);

        itemButtonEdit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
        itemButtonDown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed");
                //initializing spots into set temporary variables
                int currSpot = itemButtonDown.getItem().spot;
                int nextSpot = itemButtonDown.getItem().spot + 1;
                //if it's not at the bottom...
                if (nextSpot < (itemButtonDown.getItem().getPanel().minipanels.size())) {
                    //reassigns the spots to different variables
                    itemButtonDown.getItem().spot = -1;
                    itemButtonDown.getItem().getPanel().getMPBySpot(nextSpot).spot = currSpot;
                    itemButtonDown.getItem().spot = nextSpot;
                    //repositioning the item to its new spot
                    itemButtonDown.getItem().components.get(0).setPosition(itemButtonDown.getItem().getX() + 5, itemButtonDown.getItem().getY() + 5);
                    itemButtonDown.getItem().components.get(1).setPosition(itemButtonDown.getItem().components.get(0).getX()+ itemButtonDown.getItem().components.get(0).getWidth()+2, itemButtonDown.getItem().getY() + 5);
                    itemButtonDown.getItem().components.get(2).setPosition(itemButtonDown.getItem().components.get(1).getX()+ itemButtonDown.getItem().components.get(1).getWidth()+2, itemButtonDown.getItem().getY() + 5);
                    itemButtonDown.getItem().components.get(3).setPosition(itemButtonDown.getItem().components.get(2).getX()+ itemButtonDown.getItem().components.get(2).getWidth()+2, itemButtonDown.getItem().getY() + 5);
                    itemButtonDown.getItem().components.get(4).setPosition(itemButtonDown.getItem().components.get(3).getX() + itemButtonDown.getItem().components.get(3).getWidth() + 10, itemButtonDown.getItem().components.get(3).getY()-1);
                    itemButtonDown.getItem().components.get(5).setPosition(itemButtonDown.getItem().components.get(3).getX() + itemButtonDown.getItem().components.get(3).getWidth() + 10, (itemButtonDown.getItem().components.get(4).getY()+itemButtonDown.getItem().components.get(4).getHeight()+2));
                    itemButtonDown.getItem().components.get(6).setPosition(itemButtonDown.getItem().components.get(5).getX() + itemButtonDown.getItem().components.get(5).getWidth() + 2, (itemButtonDown.getItem().components.get(4).getY()));
                    itemButtonDown.getItem().components.get(7).setPosition(itemButtonDown.getItem().components.get(5).getX() + itemButtonDown.getItem().components.get(5).getWidth() + 2, (itemButtonDown.getItem().components.get(5).getY()));
                    //repositioning the item that you swapped it with to the item's spot
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(0).setPosition(itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getX() + 5, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(1).setPosition(itemButtonDown.getItem().components.get(0).getX()+ itemButtonDown.getItem().components.get(0).getWidth()+2, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(2).setPosition(itemButtonDown.getItem().components.get(1).getX()+ itemButtonDown.getItem().components.get(1).getWidth()+2, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).setPosition(itemButtonDown.getItem().components.get(2).getX()+ itemButtonDown.getItem().components.get(2).getWidth()+2, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).setPosition(
                            (itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getX() + itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getWidth() + 10),
                            itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getY()-1);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(5).setPosition(
                            (itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getX() + itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getWidth() + 10),
                            itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getHeight()+itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getY()+2);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(6).setPosition(
                            (itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getX() + itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getWidth() + 2),
                            itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getY());
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(7).setPosition(
                            (itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getX() + itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getWidth() + 2),
                            itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(5).getY());
                }
            }
        });
        itemButtonUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed");
                //initializing spots into set temporary variables
                int currSpot = itemButtonUp.getItem().spot;
                int prevSpot = itemButtonUp.getItem().spot-1;
                //if it's not at the top...
                if(prevSpot>=0) {
                    //reassigns the spots to different variables
                    itemButtonUp.getItem().spot = 100;
                    itemButtonUp.getItem().getPanel().getMPBySpot(prevSpot).spot = currSpot;
                    itemButtonUp.getItem().spot = prevSpot;
                    //repositioning the item to its new spot
                    itemButtonUp.getItem().components.get(0).setPosition(itemButtonUp.getItem().getX() + 5, itemButtonUp.getItem().getY() + 5);
                    itemButtonUp.getItem().components.get(1).setPosition(itemButtonUp.getItem().components.get(0).getX()+ itemButtonUp.getItem().components.get(0).getWidth()+2, itemButtonUp.getItem().getY() + 5);
                    itemButtonUp.getItem().components.get(2).setPosition(itemButtonUp.getItem().components.get(1).getX()+ itemButtonUp.getItem().components.get(1).getWidth()+2, itemButtonUp.getItem().getY() + 5);
                    itemButtonUp.getItem().components.get(3).setPosition(itemButtonUp.getItem().components.get(2).getX()+ itemButtonUp.getItem().components.get(2).getWidth()+2, itemButtonUp.getItem().getY() + 5);
                    itemButtonUp.getItem().components.get(4).setPosition(itemButtonUp.getItem().components.get(3).getX() + itemButtonUp.getItem().components.get(3).getWidth() + 10, itemButtonUp.getItem().components.get(3).getY()-1);
                    itemButtonUp.getItem().components.get(5).setPosition(itemButtonUp.getItem().components.get(3).getX() + itemButtonUp.getItem().components.get(3).getWidth() + 10, (itemButtonUp.getItem().components.get(4).getY()+itemButtonUp.getItem().components.get(4).getHeight()+2));
                    itemButtonUp.getItem().components.get(6).setPosition(itemButtonUp.getItem().components.get(5).getX() + itemButtonUp.getItem().components.get(5).getWidth() + 2, (itemButtonUp.getItem().components.get(4).getY()));
                    itemButtonUp.getItem().components.get(7).setPosition(itemButtonUp.getItem().components.get(5).getX() + itemButtonUp.getItem().components.get(5).getWidth() + 2, (itemButtonUp.getItem().components.get(5).getY()));
                    //repositioning the item that you swapped it with to the item's spot
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(0).setPosition(itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getX() + 5, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(1).setPosition(itemButtonUp.getItem().components.get(0).getX()+ itemButtonUp.getItem().components.get(0).getWidth()+2, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(2).setPosition(itemButtonUp.getItem().components.get(1).getX()+ itemButtonUp.getItem().components.get(1).getWidth()+2, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).setPosition(itemButtonUp.getItem().components.get(2).getX()+ itemButtonUp.getItem().components.get(2).getWidth()+2, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).setPosition(
                            (itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getX() + itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getWidth() + 10),
                            itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getY()-1);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(5).setPosition(
                            (itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getX() + itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).getWidth() + 10),
                            itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getHeight()+itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getY()+2);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(6).setPosition(
                            (itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getX() + itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getWidth() + 2),
                            itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getY());
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(7).setPosition(
                            (itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getX() + itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(4).getWidth() + 2),
                            itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(5).getY());
                }
            }
        });



    }
    public void add(MBComponent component){
        //adds the component given to the panel
        components.add(component);
        component.parent = this;
        component.item = this;
        //makes sure the component is an actor
        if(component.getComponent() != null) {
            //adds component to the stage so it can be drawn
            Main.stage.addActor(component.getComponent());
        }
    }

    public void edit(){
        Item item = this;

        nameLabelTF.setPosition(item.getX()+5, item.getY()+4);
        nameLabelTF.setSize(119, 32);

        diceLabelTF.setPosition(nameLabelTF.getX()+ nameLabelTF.getWidth()+2, nameLabelTF.getY());
        diceLabelTF.setSize(75, nameLabelTF.getHeight());

        modLabelTF.setPosition(diceLabelTF.getX()+ diceLabelTF.getWidth()+2, nameLabelTF.getY());
        modLabelTF.setSize(85, nameLabelTF.getHeight());

        typeLabelTF.setPosition(modLabelTF.getX()+ modLabelTF.getWidth()+2, nameLabelTF.getY());
        typeLabelTF.setSize(115, nameLabelTF.getHeight());

        add(nameLabelTF);
        add(diceLabelTF);
        add(modLabelTF);
        add(typeLabelTF);
    }
    public void saveEdit(){
        name = nameLabelTF.textField.getText();
        hitDie = diceLabelTF.textField.getText();
        mod = modLabelTF.textField.getText();
        type = typeLabelTF.textField.getText();

        nameLabel.label.setText(name);
        diceLabel.label.setText(hitDie);
        modLabel.label.setText(mod);
        typeLabel.label.setText(type);

        remove(nameLabelTF);
        remove(diceLabelTF);
        remove(modLabelTF);
        remove(typeLabelTF);
    }
    public float getSpot() {
        return spot;
    }

    public float getY(){
        return (position.y-((position.height+5)*spot));
    }
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, (position.y-((position.height+5)*spot)), position.width, position.height);
        for (int i = 0; i < minipanels.size(); i++) {
            minipanels.get(i).render(batch);
        }
    }

}

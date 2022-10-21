package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.awt.*;

public class Item extends Minipanel{
//    int spot;
Skin uiSkin = new Skin (Gdx.files.internal(
        "assets\\skins\\uiskin.json"));
    public Item(String text, int spot) {
        super("core\\pics\\TopbarPanel.png", new Rectangle(125, 790, 460, 40));
        this.spot=spot;
        MBLabel itemLabel = new MBLabel(text, uiSkin);
//		itemLabel.setSize(1000,50);
        itemLabel.setPosition(this.getX()+5, this.getY()+5);
//		itemLabel.setPosition(new Rectangle(item1.getX()+5, item1.getY()+5, 100,0));
        MBTextField itemTextField = new MBTextField("", uiSkin);
        itemTextField.setPosition(200, this.getY()+4);
        itemTextField.setSize(340, itemTextField.getHeight());

        final MBButton itemButtonDown = new MBButton(uiSkin);
        itemButtonDown.setPosition((itemTextField.getX()+itemTextField.getWidth()+2), itemTextField.getY());
        itemButtonDown.setSize(40, 15);
        itemButtonDown.button.addListener(new ChangeListener() {
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
                    itemButtonDown.getItem().components.get(1).setPosition(200, itemButtonDown.getItem().getY() + 4);
                    itemButtonDown.getItem().components.get(2).setPosition(itemButtonDown.getItem().components.get(1).getX() + itemButtonDown.getItem().components.get(1).getWidth() + 2, itemButtonDown.getItem().components.get(1).getY());
                    itemButtonDown.getItem().components.get(3).setPosition(itemButtonDown.getItem().components.get(1).getX() + itemButtonDown.getItem().components.get(1).getWidth() + 2, (itemButtonDown.getItem().components.get(2).getY()+itemButtonDown.getItem().components.get(2).getHeight()+2));
                    //repositioning the item that you swapped it with to the item's spot
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(0).setPosition(itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getX() + 5, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(1).setPosition(200, itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 4);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(2).setPosition(
                            (itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getX() + itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getWidth() + 2),
                            itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).getY() + 4);
                    itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(3).setPosition(
                            (itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getX() + itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getWidth() + 2),
                            itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(2).getHeight()+itemButtonDown.getItem().getPanel().getMPBySpot(currSpot).components.get(2).getY()+2);
                }
            }
        });
        final MBButton itemButtonUp = new MBButton(uiSkin);
        itemButtonUp.setPosition((itemTextField.getX()+itemTextField.getWidth()+2), itemButtonDown.getY()+itemButtonDown.getHeight()+2);
        itemButtonUp.setSize(40, 15);
        itemButtonUp.button.addListener(new ChangeListener() {
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
                    itemButtonUp.getItem().components.get(1).setPosition(200, itemButtonUp.getItem().getY() + 4);
                    itemButtonUp.getItem().components.get(2).setPosition(itemButtonUp.getItem().components.get(1).getX() + itemButtonUp.getItem().components.get(1).getWidth() + 2,
                            itemButtonUp.getItem().components.get(1).getY());
                    itemButtonUp.getItem().components.get(3).setPosition(itemButtonUp.getItem().components.get(1).getX() + itemButtonUp.getItem().components.get(1).getWidth() + 2,
                            itemButtonUp.getItem().components.get(2).getY()+itemButtonUp.getItem().components.get(2).getHeight()+2);
                    //repositioning the item that you swapped it with to the item's spot
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(0).setPosition(itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getX() + 5, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 5);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(1).setPosition(200, itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 4);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(2).setPosition(
                            (itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getX() + itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getWidth() + 2),
                            itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).getY() + 4);
                    itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(3).setPosition(
                            (itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getX() + itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(1).getWidth() + 2),
                            itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(2).getHeight()+itemButtonUp.getItem().getPanel().getMPBySpot(currSpot).components.get(2).getY()+2);
                }
            }
        });


        add(itemLabel);
        add(itemTextField);
        add(itemButtonDown);
        add(itemButtonUp);

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

package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

public class Item extends Minipanel{
    int spot;
    public Item(String fileLocation, int spot) {
        super(fileLocation, new Rectangle(125, 810, 460, 40));
        this.spot=spot;
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

package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;
import java.util.ArrayList;

/**
 * The panels are created from images, but I'm trying to have them act as JSwing panels
 * in how they interact with the components they hold.
 */

public class Panel {
    //the image that the panel is based off of
    Texture texture;
    //all values associated with how to draw it are stored here
    Rectangle position;
    //stores the panel's components in this list
    ArrayList<MBComponent> components = new ArrayList<>();



    public Panel(String fileLocation, Rectangle position){
        //sets the image of the panel
        texture = new Texture(fileLocation);
        //sets the location and size
        this.position = position;
    }

    public void add(MBComponent component){
        //adds the component given to the panel
        components.add(component);
        //makes sure the component is an actor
        if(component.getComponent() != null) {
            //adds component to the stage so it can be drawn
            Main.stage.addActor(component.getComponent());
        }
    }

    public void render (SpriteBatch batch) {
        //screen size is 1920x1000 so adjust accordingly
        batch.draw(texture, position.x, position.y, position.width, position.height);
    }
    public void dispose(){
        texture.dispose();
    }
}

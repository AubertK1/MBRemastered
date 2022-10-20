package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.math.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The panels are created from images, but I'm trying to have them act as JSwing panels
 * in how they interact with the components they hold.
 */

public class Panel {
    //the image that the panel is based off of
    Texture texture;
    //all values associated with how to draw it are stored here
    //fixme change rectangle into seperate variables so I can just input numbers when calling the fuctions
    Rectangle position;
    //stores the panel's components in this list
    ArrayList<MBComponent> components = new ArrayList<>();

    ArrayList<Panel> minipanels = new ArrayList<>();
//    static int panelNum = 0;
//    int panelID;

    Panel parent = null;

    public Panel(String fileLocation, Rectangle position){
        //sets the image of the panel
        texture = new Texture(fileLocation);
        //sets the location and size
        this.position = position;
//        panelID = panelNum;
//        panelNum++;
    }

    public void add(MBComponent component){
        //adds the component given to the panel
        components.add(component);
        component.parent = this;
        //makes sure the component is an actor
        if(component.getComponent() != null) {
            //adds component to the stage so it can be drawn
            Main.stage.addActor(component.getComponent());
        }
    }
    public void add(Minipanel minipanel){
        //adds the component given to the panel
        minipanels.add(minipanel);
        minipanel.parent = this;
//        minipanel.setPanel(panelID);
//        System.out.println(panelID);
    }
    public void add(Minipanel minipanel, MBComponent component){
        //adds component to minipanel
        minipanel.add(component);
    }

    public Panel getPanel() {
        return parent;
    }

    public float getX(){
        return position.x;
    }
    public float getY(){
        return position.y;
    }
    public float getWidth(){
        return position.width;
    }
    public float getHeight(){
        return position.height;
    }
    public float getSpot() {
        return -1;
    }
    public void render (SpriteBatch batch) {
        //screen size is 1920x1000 so adjust accordingly
        batch.draw(texture, position.x, position.y, position.width, position.height);
        for (int i = 0; i < minipanels.size(); i++) {
            minipanels.get(i).render(batch);
        }
    }
    public Panel getMPBySpot(int spot){
        for (Panel minipanel: minipanels) {
            if(minipanel.getSpot() == spot){
                return minipanel;
            }
        }
        return null;
    }
    public void dispose(){
        for (int i = 0; i < minipanels.size(); i++) {
            minipanels.get(i).dispose();
        }
        texture.dispose();
    }
}

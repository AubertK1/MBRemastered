package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Essentially making my own version of JComponents where I can group the different components
 * together and edit any methods I need
 */
public class MBComponent extends Actor {
    Rectangle position;
    Panel parent;
    Item item;
    Texture texture;
    public MBComponent() {

    }

    public Panel getPanel() {
        return parent;
    }
    public Item getItem() {
        return item;
    }

    public void setPosition(float x, float y){
        getComponent().setPosition(x, y);
//        position.setPosition(x,y);
//        position.y = y;
    }
    public void setSize(float width, float height){
        getComponent().setSize(width, height);
//        position.setSize(width, height);
//        position.width = width;
//        position.height = height;
    }

    public float getX(){
        return getComponent().getX();
    }
    public float getY(){
        return getComponent().getY();
    }
    public float getWidth(){
        return getComponent().getWidth();
    }
    public float getHeight(){
        return getComponent().getHeight();
    }
    /**
     * gets the component regardless of what type of component it is (textfield, label, etc.)
     * @return returns the component
     */
    public Actor getComponent(){
        return null;
    }
}

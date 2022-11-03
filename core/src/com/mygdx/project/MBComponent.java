package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Essentially making my own version of JComponents where I can group the different components
 * together and edit any methods I need
 */
public class MBComponent extends Actor {
    Rectangle position;
    Panel parent;
    Item item;
    Texture texture;
    //setting the component's ID in the list
    int compID = Main.allComps.size();
    Stage stage = Main.stage;
    //whether this component is supposed to be visible if it were allowed to be (ie the Item textfields when not in edit mode are allowed to be visible but not supposed to be visible)
    boolean supposedToBeVisible = true;
    public MBComponent() {
        //adds this component to the list of all components
        Main.allComps.add(this);
    }

    public int getCompID(){
        return compID;
    }
    public Panel getPanel() {
        return parent;
    }
    /**
     * @return returns the item this component belongs to
     */
    public Item getItem() {
        return item;
    }
    /**
     * sets the position of this component
     * @param x x value of this component
     * @param y y value of this component
     */
    public void setPosition(float x, float y){
        getComponent().setPosition(x, y);
    }
    /**
     * sets the size of this component
     * @param width width of this component
     * @param height height of this component
     */
    public void setSize(float width, float height){
        getComponent().setSize(width, height);
    }
    /**
     * changes ths component's visibility and whether it's supposed to be visible
     * @param visible its visibility
     */
    public void setVisible(boolean visible){
        getComponent().setVisible(visible);
        this.supposedToBeVisible = visible;
    }
    /**
     * changes this component's visibility but not whether it's supposed to be visible
     * @param visible its visibility
     */
    public void setSoftVisible(boolean visible){
        getComponent().setVisible(visible);
    }
    /**
     * @return returns the x value of this component
     */
    public float getX(){
        return getComponent().getX();
    }
    /**
     * @return returns the y value of this component
     */
    public float getY(){
        return getComponent().getY();
    }
    /**
     * @return returns the width of this component
     */
    public float getWidth(){
        return getComponent().getWidth();
    }
    /**
     * @return returns the height of this component
     */
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
//    public void setStage(Stage stage){
//        this.stage = stage;
//    }
//    public Stage getStage(){
//        return stage;
//    }
//    public void setPanel(Panel parent){
//        this.parent = parent;
//    }
}

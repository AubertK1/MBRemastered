package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Essentially making my own version of JComponents where I can group the different components
 * together and edit any methods I need
 */
public class MBComponent extends Actor {
    Rectangle position;
    ArrayList<MBComponent> components = new ArrayList<>();
    Panel parentPanel;
    MBComponent parentActor;
    int deleteLater = -1;
    Skin skin = Main.uiSkin;
    float aFloat = 1;
    Item item;
    Texture texture;
    boolean hasWindow = false;
    //setting the component's ID in the list
    int compID = Main.allComps.size();
    Stage stage = Main.stage;
    //whether this component is supposed to be visible if it were allowed to be (ie the Item textfields when not in edit mode are allowed to be visible but not supposed to be visible)
    boolean supposedToBeVisible = true;
    public MBComponent() {
    }

    public void add(MBComponent component){
        //adds this component to the list of all components
        boolean notAdded = true;
        for (MBComponent comp: Main.allComps) {
            if (component == comp) {
                notAdded = false;
                break;
            }
        }
        if (notAdded) Main.allComps.add(component);
        //adds the component given to this panel
        components.add(component);
        //sets the component's parent to this panel
        component.parentActor = this;
        //makes sure the component is an actor
        if(component.getComponent() != null) {
            //adds component to the stage so it can be drawn
            Main.stage.addActor(component.getComponent());
            //so that the compID aligns with the component's position on the list
//            component.compID = Main.allComps.size();
        }
        if(component instanceof MBWindow){
            Main.windows.add((MBWindow) component);
        }
        if(component instanceof MBScrollpane){
            Main.scrollpanes.add((MBScrollpane) component);
        }
//        Panel.resetCompIDs();
    }
    public void remove(MBComponent component) {
        //removes component from the stage (don't think this does anything tbh)
        component.remove();
        //removes component from the components list
        components.remove(component);
        if(component instanceof MBWindow){
            Main.windows.remove((MBWindow) component);
        }

    }

    public void delete(MBComponent component){
        //removes component from the stage
        Main.stage.getActors().get(component.getCompID()).addAction(Actions.removeActor());
        //removes component from the all components list
        Main.allComps.remove(component);
        //removes component from the item's components list
        components.remove(component);
        if(component instanceof MBWindow){
            Main.windows.remove((MBWindow) component);
        }
        if(component instanceof MBScrollpane){
            Main.scrollpanes.remove((MBScrollpane) component);
        }

        //reassigns the remaining components' IDs
        Panel.resetCompIDs();
    }


    public int getCompID(){
        return compID;
    }
    public Object getMBParent() {
        if(parentPanel != null) return parentPanel;
        else return parentActor;
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
    public void draw(float alpha){
        Main.allComps = reaarrangeList();
        Panel.resetCompIDs();

        if(this.deleteLater != -1){
            System.out.print("");
        }
        getComponent().draw(Main.batch, alpha);
        for (MBComponent innerComp: components) {
            innerComp.draw(innerComp.aFloat);
        }
    }
    public ArrayList<MBComponent> reaarrangeList(){
        ArrayList<MBComponent> newList = new ArrayList<>();
        Array<Actor> actors = Main.stage.getActors();
        Actor actor;
        MBComponent comp;

        for (int i = 0; i < actors.size; i++) {
            actor = actors.get(i);
            for (int j = 0; j < Main.allComps.size(); j++) {
                comp = Main.allComps.get(j);
                if(comp.getComponent() == actor) newList.add(comp);
            }
        }
        return newList;
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

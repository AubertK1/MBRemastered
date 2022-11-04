package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

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
    //stores the panel's minipanels
    ArrayList<Panel> minipanels = new ArrayList<>();
    //stores the panel's items
    ArrayList<Item> wItems = new ArrayList<>();
    ArrayList<Item> sItems = new ArrayList<>();
//    static int panelNum = 0;
//    int panelID;
    //the parent panel of the minipanel
    Panel parent = null;
    //spot of the items relative to the top of the panel
    int wSpot;
    int sSpot;
    //the total amount of panels created. the same through all panels
    static int totalWID = 0;
    static int totalSID = 0;
    //next available spot
    static int nextAvaWSpot = 0;
    static int nextAvaSSpot = 0;
    //ID of the panel
    int wID = totalWID;
    int sID = totalSID;
    //if the panel is in edit mode
    boolean editMode;

    public Panel(String fileLocation, Rectangle position){
        //sets the image of the panel
        texture = new Texture(fileLocation);
        //sets the location and size
        this.position = position;
//        panelID = panelNum;
//        panelNum++;
    }

    /**
     * adds the component to its panel
     * @param component the component you want to add
     */
    public void add(MBComponent component){
        //adds the component given to this panel
        components.add(component);
        //sets the component's parent to this panel
        component.parent = this;
        //makes sure the component is an actor
        if(component.getComponent() != null) {
            //adds component to the stage so it can be drawn
            Main.stage.addActor(component.getComponent());
        }
    }

    /**
     * adds the minipanel to its panel
     * @param minipanel the minipanel you want to add
     */
    public void add(Minipanel minipanel){
        //adds the minipanel given to this panel
        minipanels.add(minipanel);
        //sets the minipanel's parent to this panel
        minipanel.parent = this;
        //if the minipanel is an item...
        if(minipanel instanceof Item){
            //adds the item to this panel's associated items list too
            if(((Item) minipanel).getItemType() == 1) {
                wItems.add((Item) minipanel);
            }
            else if (((Item) minipanel).getItemType() == 2){
                sItems.add((Item) minipanel);
            }
        }
    }
    /**
     * removes the component from the lists but it can be added back whenever
     * @param component the component you want to remove
     */
    public void remove(MBComponent component) {
        //removes component from the stage (don't think this does anything tbh)
        component.remove();
        //removes component from the components list
        components.remove(component);
    }

    /**
     * permanently removes component from everything
     * @param component
     */
    public void delete(MBComponent component){
        //removes component from the stage
        Main.stage.getActors().get(component.getCompID()).addAction(Actions.removeActor());
        //removes component from the all components list
        Main.allComps.remove(component);
        //removes component from the item's components list
        components.remove(component);
        //reassigns the remaining components' IDs
        resetCompIDs();
    }

    /**
     * removes panel from lists making it unable to be rendered
     * @param panel the panel you want to remove
     */
    public void remove(Panel panel){
        //removes component from the components list
        minipanels.remove(panel);
        if(panel instanceof Item) wItems.remove(panel);
    }

    /**
     * reassigns the compID variable for all the components
     */
    public void resetCompIDs(){
        for (int i = 0; i < Main.allComps.size(); i++) {
            Main.allComps.get(i).compID = i;
        }
    }

    /**
     * @return returns the panel this panel belongs to
     */
    public Panel getPanel() {
        return parent;
    }

    /**
     * @return returns the x value of this panel
     */
    public float getX(){
        return position.x;
    }
    /**
     * @return returns the y value of this panel
     */
    public float getY(){
        return position.y;
    }
    /**
     * @return returns the width of this panel
     */
    public float getWidth(){
        return position.width;
    }
    /**
     * @return returns the height of this panel
     */
    public float getHeight(){
        return position.height;
    }

    /**
     * loops through the minipanels list to find a minipanel whose spot value matches the given spot
     * @param spot the panel's spot
     * @return returns the minipanel
     */
    public Panel getMPBySpot(int spot){
        for (Panel minipanel: minipanels) {
            if(minipanel.getSpot() == spot){
                return minipanel;
            }
        }
        return null;
    }
    public Item getItemBySpot(int spot){
        if(Main.itemTab == 1) {
            for (Item minipanel : wItems) {
                if (minipanel.getSpot() == spot) {
                    return minipanel;
                }
            }
        }
        else if(Main.itemTab == 2) {
            for (Item minipanel : sItems) {
                if (minipanel.getSpot() == spot) {
                    return minipanel;
                }
            }
        }
        return null;
    }

    /**
     * @return returns if the panel is in edit mode
     */
    public boolean getEditMode(){
        return editMode;
    }

    /**
     * future potentially needed functions
     */
    public float getSpot() {
        return -1;
    }
    public void edit(){
    }
    public void saveEdit(){
    }

    /**
     * renders all the panels
     * @param batch the batch...
     */
    public void render (SpriteBatch batch) {
        //screen size is 1920x1000 so adjust accordingly
        //draws this panel
        batch.draw(texture, position.x, position.y, position.width, position.height);
        //loops through this panel's list of minipanels
        for (int i = 0; i < minipanels.size(); i++) {
            //if the minipanel's spot value is less than 0 then it doesn't render it (because it doesn't call render)
            if(minipanels.get(i).wSpot < 0){
                //loops through the minipanel's list of components
                for (int c = 0; c < minipanels.get(i).components.size(); c++) {
                    //sets the soft visibility of the component to false
                    minipanels.get(i).components.get(c).setSoftVisible(false);
                }
            }
            //if the minipanel's spot value is more than 5 then it doesn't render it (may change depending on the group of items)
            else if(minipanels.get(i).wSpot > 5){
                //loops through the minipanel's list of components
                for (int c = 0; c < minipanels.get(i).components.size(); c++) {
                    //sets the soft visibility of the component to false
                    minipanels.get(i).components.get(c).setSoftVisible(false);
                }
            }
            //renders everything else and sets the soft visibility to true
            else {
                minipanels.get(i).render(batch);
                //loops through the minipanel's list of components
                for (int c = 0; c < minipanels.get(i).components.size(); c++) {
                    //if the component is supposed to be visible...
                    if(minipanels.get(i).components.get(c).supposedToBeVisible) {
                        //sets the soft visibility of the component to true
                        minipanels.get(i).components.get(c).setSoftVisible(true);
                    }
                }
            }
        }
    }
    public void dispose(){
        for (Panel minipanel : minipanels) {
            minipanel.dispose();
        }
        //fixme
/*
        for (int i = 0; i < Main.allComps.size(); i++){
            components.get(i).remove();
            components.remove(i);
        }
        texture.dispose();
*/
    }
}

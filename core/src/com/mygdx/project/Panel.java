package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * The panels are created from images, but I'm trying to have them act as JSwing panels
 * in how they interact with the components they hold.
 */

public class Panel implements Renderable{
    //the image that the panel is based off of
    Texture texture;
    //the batch for the render function
    SpriteBatch batch = Main.batch;
    //all values associated with how to draw it are stored here
    float x, y, width, height;
    //stores the panel's components in this list
    ArrayList<MBComponent> components = new ArrayList<>();
    //stores the panel's minipanels
    ArrayList<Panel> minipanels = new ArrayList<>();
    //the parent panel of the minipanel
    protected Panel parentPanel = null;
    //controls whether this is rendered or not
    boolean supposedToBeVisible = true;
    private int layer = -1;

    protected boolean focused = false;
    //the alpha value this is rendered with
    float aFloat = 1f;

    public Panel(String fileLocation, Rectangle position){
        //sets the image of the panel
        texture = new Texture(fileLocation);
        //sets the location and size
        setPosition(position.x, position.y);
        setSize(position.width, position.height);
//        panelID = panelNum;
//        panelNum++;
    }

    public void add( MBComponent component){
        add(component, 0);
    }
    /**
     * adds the component to its panel
     * @param component the component you want to add
     */
    public void add(MBComponent component, int layer){
//        if(component.getActor() != null) return;
        //adds this component to the list of all components if it's not already in it
        if(!Main.allComps.contains(component)) Main.allComps.add(component);
        //adds the component given to this panel
        components.add(component);
        //sets the component's parent to this panel
        component.parentPanel = this;
        //adds component to the stage so it can be drawn
        Main.stage.addActor(component.getActor());

        component.setLayer(layer);
    }

    public void add( Minipanel minipanel) {
        add(minipanel, 0);
    }
    /**
     * adds the minipanel to its panel
     * @param minipanel the minipanel you want to add
     */
    public void add( Minipanel minipanel, int layer){
        //adds the minipanel given to this panel
        minipanels.add(minipanel);
        //sets the minipanel's parent to this panel
        minipanel.parentPanel = this;

        minipanel.setLayer(layer);
    }
    /**
     * permanently removes component from everything
     * @param component the component you want to remove
     */
    public void delete(MBComponent component){
        //deletes all of the component's components
        for (MBComponent childComp : component.components) {
            delete(childComp);
        }

        component.setVisible(false);
        //removes component from the stage
        component.getActor().remove();
//        Main.stage.getActors().get(component.getCompID()).addAction(Actions.removeActor());
        //removes component from the all components list
        Main.allComps.remove(component);
        //removes component from the item's components list
        components.remove(component);
        //disposes of the MBComponent
        component.dispose();

        component.setLayer(-1);
        //reassigns the remaining components' IDs
//        resetCompIDs();
    }

    /**
     * removes panel from lists making it unable to be rendered and deletes its texture
     * @param panel the panel you want to remove
     */
    public void delete(Panel panel){
        //DO NOT CALL THIS IF YOU PLAN ON ADDING THE PANEL BACK LATER
        //deletes all of the panel's components and minipanels
        for (MBComponent component : panel.components) {
            delete(component);
        }
        for (Panel minipanel: panel.minipanels) {
            delete(minipanel);
        }
        //then goes through and deletes the panel
        panel.setSoftVisible(false);
        //removes component from the components list
        minipanels.remove(panel);
        //disposes of the texture
        panel.texture.dispose();

        panel.setLayer(-1);
    }

    /**
     * reassigns the compID variable for all the components
     */
    static public void resetCompIDs(){
        for (int i = 0; i < Main.allComps.size(); i++) {
            Main.allComps.get(i).compID = i;
        }
    }

    public void setSoftVisible(boolean visible){
        supposedToBeVisible = visible;
        for (MBComponent comp: components) {
            comp.setSoftVisible(visible);
        }
        for (Panel mp: minipanels) {
            if(mp.components.size() > 0) mp.setSoftVisible(visible);
        }
    }
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
    }
    public void setLayer(int layer){
        int oldLayer = getLayer();

        if(oldLayer != -1) {
            for (int renderable = 0; renderable < Main.layers.get(oldLayer).size(); renderable++) { //find the panel in the old layer
                if (this == Main.layers.get(oldLayer).get(renderable)) {
                    Main.layers.get(oldLayer).remove(this); //remove the panel from the old layer
                }
            }
        }

        if(layer == -1); //don't add this to a list, so it doesn't get rendered
        else if(Main.layers.containsKey(layer)){ //if the layer already exists
            Main.layers.get(layer).add(this); //add the panel to its new later
        }
        else{
            Main.layers.put(layer, new ArrayList<Renderable>()); //creates a new layer
            Main.layers.get(layer).add(this); //add the panel to the new later
        }

        this.layer = layer;
    }

    public int getLayer(){
        return layer;
    }

    /**
     * @return returns the panel this panel belongs to
     */
    public Panel getParentPanel() {
        return parentPanel;
    }

    /**
     * @return returns the x value of this panel
     */
    public float getX(){
        return x;
    }
    /**
     * @return returns the y value of this panel
     */
    public float getY(){
        return y;
    }
    /**
     * @return returns the width of this panel
     */
    public float getWidth(){
        return width;
    }
    /**
     * @return returns the height of this panel
     */
    public float getHeight(){
        return height;
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

    public boolean isFocused() {
        return focused;
    }
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    /**
     * future potentially needed functions
     */
    public int getSpot() {
        return -1;
    }
    public void edit(){
    }
    public void saveEdit(){
    }
    public boolean isSupposedToBeVisible() {
        return supposedToBeVisible;
    }

    /**
     * renders all the panels
     */
    public void render () {
        if(focused){
            if(!Main.focusedPanels.contains(this)) Main.focusedPanels.add(this);
        }
        else Main.focusedPanels.remove(this);
        //screen size is 1920x1000 so adjust accordingly
        //makes sure the panel's opacity is unchanged by the components' opacity changes
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);
        //draws this panel
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        for (MBComponent component: components) {
            if(component.supposedToBeVisible) {
                component.render();
            }
        }
        //loops through this panel's list of minipanels
        for (Panel minipanel : minipanels) {
            if(minipanel.supposedToBeVisible){
                minipanel.render();
            }
        }
    }

    public void dispose(){
        for (Panel minipanel : minipanels) {
            minipanel.dispose();
        }
        for (MBComponent component : components) {
            component.dispose();
        }
        texture.dispose();
    }
}

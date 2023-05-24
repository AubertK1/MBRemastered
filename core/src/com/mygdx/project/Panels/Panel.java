package com.mygdx.project.Panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.project.Components.MBComponent;
import com.mygdx.project.Main;
import com.mygdx.project.Renderable;
import com.mygdx.project.Screen;

import java.util.ArrayList;

/**
 * The panels are created from images, but I'm trying to have them act as JSwing panels
 * in how they interact with the components they hold.
 */

public class Panel implements Renderable {
    //the image that the panel is based off of
    protected Texture texture;
    //the batch for the render function
    protected SpriteBatch batch = Main.batch;
    //all values associated with how to draw it are stored here
    private float x, y, width, height;
    //stores the panel's components
    protected ArrayList<MBComponent> components = new ArrayList<>();
    //stores the panel's minipanels
    protected ArrayList<Panel> minipanels = new ArrayList<>();
    //the parent panel if it's minipanel
    protected Panel parentPanel = null;
    //the screen this belongs to
    protected Screen screen = null;
    //this panel's layer in the screen
    private int layer = -1;
    //controls whether this is rendered or not
    private boolean supposedToBeVisible = true;
    //whether this is focused
    protected boolean focused = false;
    //the alpha value this is rendered with
    protected float aFloat = 1f;

    public Panel(String fileLocation, Rectangle position, Screen screen){
        setScreen(screen);
        //sets the image of the panel
        texture = new Texture(fileLocation);
        //sets the location and size
        setPosition(position.x, position.y);
        setSize(position.width, position.height);
    }

    //region minipanels and components
    public void add(MBComponent component){
        add(component, 0);
    }
    /**
     * adds the component to its panel
     * @param component the component you want to add
     */
    public void add(MBComponent component, int layer){
        //adds this component to the list of all components
        screen.getAllComps().add(component);
        screen.getRenderables().add(component);
        //adds the component to this panel
        components.add(component);
        //sets the component's parent to this panel
        component.setParentPanel(this);
        component.setScreen(screen);

        //adds component to the stage so it can be drawn
        Main.stage.addActor(component.getActor());

        component.setLayer(layer);
    }

    public void add(Minipanel minipanel) {
        add(minipanel, 0);
    }
    /**
     * adds the minipanel to its panel
     * @param minipanel the minipanel you want to add
     */
    public void add(Minipanel minipanel, int layer){
        screen.getRenderables().add(minipanel);
        //adds the minipanel given to this panel
        minipanels.add(minipanel);
        //sets the minipanel's parent to this panel
        minipanel.parentPanel = this;
        minipanel.setScreen(screen);

        minipanel.setLayer(layer);
    }

    /**
     * removes component from lists making it unable to be rendered and deletes its texture
     * @param component the component you want to delete
     */
    public void delete(MBComponent component){
        //deletes all the component's components
        for (MBComponent childComp : component.getComponents()) {
            delete(childComp);
        }

        component.setLayer(-1);

        component.setVisible(false);
        //removes component from the stage
        component.getActor().remove();
        //removes component from the all components list
        screen.getAllComps().remove(component);
        screen.getRenderables().remove(component);
        //removes component from the item's components list
        components.remove(component);
        //disposes of the component
        component.dispose();
    }

    /**
     * removes panel from lists making it unable to be rendered and deletes its texture
     * @param panel the panel you want to delete
     */
    public void delete(Panel panel){
        //deletes all the panel's components and minipanels first
        for (MBComponent component : panel.components) {
            delete(component);
        }
        for (Panel minipanel: panel.minipanels) {
            delete(minipanel);
        }

        panel.setLayer(-1);

        //goes through and deletes the panel
        panel.setVisible(false);
        //removes component from the components list
        minipanels.remove(panel);
        screen.getRenderables().remove(panel);
        //disposes of the panel
        panel.dispose();
    }

    public void removeCompsFromStage(){
        for (MBComponent comp: components) {
            comp.removeFromStage();
        }
        for (Panel minipanel: minipanels) {
            minipanel.removeCompsFromStage();
        }
        getScreen().resetCompIDs();
    }
    public void addCompsToStage(){
        for (MBComponent comp: components) {
            comp.addToStage();
        }
        for (Panel minipanel: minipanels) {
            minipanel.addCompsToStage();
        }
        getScreen().resetCompIDs();
    }
    //endregion

    //region setters
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
    }
    public void setVisible(boolean visible){
        supposedToBeVisible = visible;

        for (MBComponent comp: components) {
            comp.setSoftVisible(visible);
        }
        for (Panel mp: minipanels) {
            mp.setVisible(visible);
        }
    }
    public void setLayer(int layer){
        int oldLayer = getLayer();

        if(oldLayer != -1) {
            //finds the panel in the old layer
            for (int renderable = 0; renderable < getScreen().getLayers().get(oldLayer).size(); renderable++) {
                if (this == getScreen().getLayers().get(oldLayer).get(renderable)) {
                    //removes the panel from the old layer
                    getScreen().getLayers().get(oldLayer).remove(this);
                }
            }
        }

        if(layer != -1) { //doesn't add this to a list if layer is not valid, so it doesn't get rendered
            if (getScreen().getLayers().containsKey(layer)) { //if the layer already exists...
                if (!getScreen().getLayers().get(layer).contains(this))
                    //adds the panel to its new later
                    getScreen().getLayers().get(layer).add(this);
            } else {
                //makes a layer and adds the panel to the new layer
                getScreen().addLayer(layer);
                getScreen().getLayers().get(layer).add(this);
            }
        }

        this.layer = layer;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;

        for (Panel minipanel: minipanels) {
            minipanel.setScreen(screen);
        }
        for (MBComponent component: components) {
            component.setScreen(screen);
        }
    }

    public void setOpacity(float opacity){
        aFloat = opacity;
    }
    public void setFocused(boolean focused) {
        this.focused = focused;
        if(Main.getMainScreen() != null && Main.getMainScreen().isFocused()) Main.getMainScreen().focus();
    }
    //endregion

    //region getters
    public float getOpacity(){
        return aFloat;
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
    public ArrayList<MBComponent> getComponents(){
        return components;
    }
    public Screen getScreen() {
        return screen;
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

    public float getRightX(){
        return getX() + getWidth();
    }
    public float getTopY(){
        return getY() + getHeight();
    }

    public boolean isFocused() {
        return focused;
    }
    public boolean isSupposedToBeVisible() {
        return supposedToBeVisible;
    }
    //endregion

    /**
     * renders all the panels
     */
    public void render () {
        //screen size is 1920x1000 so adjust accordingly
        //makes sure the panel's opacity is unchanged by the components' opacity changes
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);
        //draws this panel
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        for (MBComponent component: components) {
            if(component.isSupposedToBeVisible()) {
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

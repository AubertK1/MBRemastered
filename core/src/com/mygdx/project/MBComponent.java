package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import java.util.ArrayList;

import static com.mygdx.project.Main.batch;

/**
 * Essentially making my own version of JComponents where I can group the different components
 * together and edit any methods I need
 */
public class MBComponent implements Renderable{
    ArrayList<MBComponent> components = new ArrayList<>();
    Panel parentPanel;
    Screen screen;
    MBComponent parentActor;
    Skin skin = Main.skin;
    float aFloat = 1;
    boolean hasWindow = false;
    //setting the component's ID in the list
    int compID;
    Stage stage = Main.stage;
    //whether this component is supposed to be visible if it were allowed to be (ie the Item textfields when not in edit mode are allowed to be visible but not supposed to be visible)
    boolean supposedToBeVisible = true;
    private int layer = -1;
    private @Null String name;
    protected boolean focused = false;
    public MBComponent() {
    }

    public void add(MBComponent component){
        add(component, 0);
    }
    /**
     * adds the component to this component
     * @param component the component you want to add
     */
    public void add(MBComponent component, int layer){
//        if(component.getActor() != null) return;
        //adds this component to the list of all components if it's not already in it
        if(!getScreen().allComps.contains(component)) getScreen().allComps.add(component);
        //adds the component given to this panel
        components.add(component);
        //sets the component's parent to this panel
        component.parentActor = this;
        //adds component to the stage so it can be drawn
        getScreen().stage.addActor(component.getActor());

        component.setLayer(layer);
    }
    public void delete(MBComponent component){
        //removes actor from the stage
        component.getActor().remove();
//        Main.stage.getActors().get(component.getCompID()).addAction(Actions.removeActor());
        //removes component from the all components list
        getScreen().allComps.remove(component);
        //removes component from the item's components list
        components.remove(component);

        component.setLayer(-1);
        //reassigns the remaining components' IDs
        getScreen().resetCompIDs();
    }


    public int getCompID(){
        return compID;
    }
    public Object getMBParent() {
        if(parentPanel != null) return parentPanel;
        else return parentActor;
    }
    public void setScreen(Screen screen) {
        this.screen = screen;
        compID = screen.allComps.size();
    }

    public Screen getScreen() {
        return screen;
    }
    /**
     * sets the position of this component
     * @param x x value of this component
     * @param y y value of this component
     */
    public void setPosition(float x, float y){
        getActor().setPosition(x, y);
    }
    /**
     * sets the size of this component
     * @param width width of this component
     * @param height height of this component
     */
    public void setSize(float width, float height){
        getActor().setSize(width, height);
    }
    /**
     * changes ths component's visibility and whether it's supposed to be visible
     * @param visible its visibility
     */
    public void setVisible(boolean visible){
        getActor().setVisible(visible);
        this.supposedToBeVisible = visible;
    }
    /**
     * changes this component's visibility but not whether it's supposed to be visible
     * @param visible its visibility
     */
    public void setSoftVisible(boolean visible){
        getActor().setVisible(visible);
    }
    public boolean isFocused() {
        return focused;
    }
    public void setFocused(boolean focused) {
        this.focused = focused;
    }
    public void setLayer(int layer){
        int oldLayer = getLayer();

        if(oldLayer != -1) {
            for (int renderable = 0; renderable < getScreen().layers.get(oldLayer).size(); renderable++) { //find the panel in the old layer
                if (this == getScreen().layers.get(oldLayer).get(renderable)) {
                    getScreen().layers.get(oldLayer).remove(this); //remove the panel from the old layer
                }
            }
        }

        if(layer == -1); //don't add this to a list, so it doesn't get rendered
        else if(getScreen().layers.containsKey(layer)){ //if the layer already exists
            getScreen().layers.get(layer).add(this); //add the panel to its new later
        }
        else{
            getScreen().layers.put(layer, new ArrayList<Renderable>()); //creates a new layer
            getScreen().layers.get(layer).add(this); //add the panel to the new later
        }

        this.layer = layer;
    }

    public int getLayer(){
        return layer;
    }

    public void setName (@Null String name) {
        this.name = name;
    }
    public @Null String getName () {
        return name;
    }
    /**
     * @return returns the x value of this component
     */
    public float getX(){
        return getActor().getX();
    }
    /**
     * @return returns the y value of this component
     */
    public float getY(){
        return getActor().getY();
    }
    /**
     * @return returns the width of this component
     */
    public float getWidth(){
        return getActor().getWidth();
    }
    /**
     * @return returns the height of this component
     */
    public float getHeight(){
        return getActor().getHeight();
    }

    public boolean isSupposedToBeVisible() {
        return supposedToBeVisible;
    }

    /**
     * gets the component regardless of what type of component it is (textfield, label, etc.)
     * @return returns the component
     */
    public Actor getActor(){
        return null;
    }
    public void render(){
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        getScreen().allComps = reaarrangeList();
        getScreen().resetCompIDs();

        getActor().draw(batch, aFloat);
        for (MBComponent innerComp: components) {
            innerComp.render();
        }
    }
    public ArrayList<MBComponent> reaarrangeList(){
        ArrayList<MBComponent> newList = new ArrayList<>();
        Array<Actor> actors = Main.stage.getActors();
        Actor actor;
        MBComponent comp;

        for (int i = 0; i < actors.size; i++) { //loop through the stage's actors
            actor = actors.get(i);
            for (int j = 0; j < getScreen().allComps.size(); j++) { //find its corresponding component in allComps
                comp = getScreen().allComps.get(j);
                if(actor == comp.getActor()) newList.add(comp);
            }
        }
        return newList;
    }
    public void dispose(){

    }
}

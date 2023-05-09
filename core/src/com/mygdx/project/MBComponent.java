package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import java.util.ArrayList;
import java.util.LinkedList;

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
    Stage stage = Main.stage;
    MBSystem system = null;
    private float aFloat = 1;
    boolean hasWindow = false;
    //setting the component's ID in the list
    int compID;
    //whether this component is supposed to be visible if it were allowed to be (ie the Item textfields when not in edit mode are allowed to be visible but not supposed to be visible)
    boolean supposedToBeVisible = true;
    private int layer = -1;
    private @Null String name = "";
    protected boolean focused = false;
    public MBComponent(Screen screen) {
        setScreen(screen);
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
        getScreen().getComps().add(component);
        screen.addRenderable(component);
        //adds the component given to this panel
        components.add(component);
        //sets the component's parent to this panel
        component.parentActor = this;
        //adds component to the stage so it can be drawn
        getScreen().stage.addActor(component.getActor());

        component.setLayer(layer);
    }
    public void delete(MBComponent component){
        screen.removeRenderable(component);
        //deletes all the component's components
        for (MBComponent childComp : component.components) {
            delete(childComp);
        }

        component.setVisible(false);

        //removes actor from the stage
        component.getActor().remove();
        //removes component from the all components list
        getScreen().getComps().remove(component);
        //removes component from the item's components list
        components.remove(component);

        component.setLayer(-1);

        component.dispose();
        //reassigns the remaining components' IDs
        getScreen().resetCompIDs();
    }

    public void remove(){
        for (MBComponent childComp: components) {
            childComp.remove();
        }
        getActor().remove();
    }
    public void reAdd(){
        getScreen().stage.addActor(getActor());
        for (MBComponent childComp: components) {
            childComp.reAdd();
        }
    }

    public int getCompID(){
        return compID;
    }
    public Object getMBParent() {
        if(parentPanel != null) return parentPanel;
        else return parentActor;
    }
    public Panel getParentPanel(){
        return parentPanel;
    }
    public void setScreen(Screen screen) {
        this.screen = screen;
        compID = screen.getComps().size();

        for (MBComponent component: components) {
            component.setScreen(screen);
        }
    }

    public Screen getScreen() {
        return screen;
    }
    public void setSystem(MBSystem system){
        this.system = system;
    }
    public MBSystem getSystem(){
        return system;
    }
    public void updateSystem(){
        if(system == null) return;
        system.update();
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
        if(Main.getMainScreen() != null && Main.getMainScreen().isFocused()) Main.getMainScreen().focus();
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
            if(!getScreen().layers.get(layer).contains(this))
                getScreen().layers.get(layer).add(this); //add the panel to its new later
        }
        else{
            getScreen().addLayer(layer);
            getScreen().layers.get(layer).add(this); //add the panel to the new later
        }

        this.layer = layer;
    }

    public int getLayer(){
        return layer;
    }

    public void setOpacity(float opacity){
        aFloat = opacity;
    }
    public float getOpacity(){
        return aFloat;
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

    public float getRightX(){
        return getX() + getWidth();
    }
    public float getTopY(){
        return getY() + getHeight();
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

        getActor().draw(batch, aFloat);
        for (MBComponent innerComp: components) {
            if(innerComp.supposedToBeVisible) {
                innerComp.render();
            }
        }
    }
    public static LinkedList<MBComponent> reaarrangeList(Screen screen){
        LinkedList<MBComponent> newList = new LinkedList<>();
        Array<Actor> actors = Main.stage.getActors();
        MBComponent comp;

        for (int i = 0; i < actors.size; i++) { //loop through the stage's actors
            for (int j = 0; j < screen.getComps().size(); j++) { //find its corresponding component in allComps
                comp = screen.getComps().get(j);
                if(actors.get(i) == comp.getActor()) newList.add(comp);
            }
        }
        return newList;
    }
    public void dispose(){

    }
}

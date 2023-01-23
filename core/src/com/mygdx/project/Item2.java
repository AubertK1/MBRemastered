package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

public class Item2 extends Minipanel{
    //skin of the item
    Skin skin;
    //strings for the labels
    ArrayList<String> labelTexts = new ArrayList<>();
    //textfields for when you edit the item (if weapon)
    ArrayList<MBTextField> textFields = new ArrayList<>();
    //labels for the item (if weapon)
    ArrayList<MBLabel> labels = new ArrayList<>();

    //if the panel is in edit mode
    protected boolean editMode = false;

    protected ItemPanel parentIP;
    protected int spot;
    protected int ID;

    protected final float ITEMGAP = 5;

    public Item2() {
        super("assets\\Panels\\ItemPanel4.png", new Rectangle(125, 790, 460, 40));
        skin = Main.uiSkin;
    }
    public void initialize(){

    }
    /**
     * moves the item and its components to this item's corresponding spot
     */
    protected void reformat() {
        float oldY, yGap;
        oldY = getY();
        //updates the item's position
        setPosition(getX(),getY()-((getHeight()+ITEMGAP) * spot));

        yGap = getY() - oldY;
        //updates the item's components' positions
        for (MBComponent component : components) {
            component.setPosition(component.getX(), component.getY() + yGap);
        }
        //updates the item's minipanels' positions
        for (Panel minipanel: minipanels) {
            minipanel.setPosition(minipanel.getX(), minipanel.getY() + yGap);
            //updates the item's minipanels' components' positions
            for (MBComponent MPComp: minipanel.components) {
                MPComp.setPosition(MPComp.getX(), MPComp.getY() + yGap);
            }
        }
    }

    /**
     * makes the textfields appear above the labels
     */
    public void edit(){
        //to have only one item edited at a time
        if(getItems() != null) {
            for (Item2 item2: getItems()) {
                if(item2 != this) item2.saveEdit(); //if this isn't the item being edited...
            }
        }

        for (MBComponent component : components) {
            //finds editbutton and checks it
            if (component.getName() != null && component.getName().equals("editbutton") && component instanceof MBButton) {
                ((MBButton) component).getButton().setChecked(true);
            }
        }

        //loops through this item's textfields list and updates their positions, re-adds them to the list, and sets their hard visibility to true
        for (int i = 0; i < textFields.size(); i++) {
            textFields.get(i).getTextField().setText(labelTexts.get(i));
            labels.get(i).getLabel().setText("");
            textFields.get(i).setPosition(labels.get(i).getX(), getY() + 5);
            textFields.get(i).setSize(labels.get(i).getWidth(), 30);
//            add(textFields.get(i));
            textFields.get(i).setVisible(true);
        }

        editMode = true;
    }
    /**
     * sets the labels to the text of the textfields and makes them disappear
     */
    public void saveEdit(){
        for (MBComponent component : components) {
            //finds editbutton and unchecks it
            if (component.getName() != null && component.getName().equals("editbutton") && component instanceof MBButton) {
                ((MBButton) component).getButton().setChecked(false);
            }
        }

        //removing the textfields
        for (int i = 0; i < textFields.size(); i++) {
            labelTexts.set(i, textFields.get(i).getTextField().getText()); //updating the label list's text

            labels.get(i).getLabel().setText(shortenString(labelTexts.get(i), labels.get(i).getWidth())); //updating the label's text

            textFields.get(i).setVisible(false); //fixme I don't think this is necessary
//            remove(textFields.get(i));
        }

        editMode = false;
    }
/*
    */
/**
     * shuffles all the items one spot up
     *//*

    public void shuffleItemsUp(){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        setNextAvaSpot(getNextAvaSpot() - 1);
        //loops through all the items
        for(Item2 item : getItems()){
            item.setSpot(item.getSpot()-1);
            //loops through the item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
            for (int i = 0; i < item.components.size(); i++) {
                item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() + (item.getHeight()+ITEMGAP));
            }
            for (int i = 0; i < item.minipanels.size(); i++) {
                item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() + (item.getHeight()+ITEMGAP));
                item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() + (getHeight()+5));
            }
            //moves the textfields with the item if in edit mode
            if (item.editMode) {
                item.saveEdit();
                item.edit();
            }

            //only show the items in spots 0-5
            if(item.getSpot() < 0 || item.getSpot() > 5){
                item.setSoftVisible(false);
            }
            else if(item.getSpot() >= 0 && item.getSpot() <= 5){
                item.setSoftVisible(true);
            }
        }
    }

    */
/**
     * shuffles all the items under the start spot up one spot
     *//*

    public void shuffleItemsUp(int startSpot){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        setNextAvaSpot(getNextAvaSpot() - 1);
        //loops through all the items
        for(Item2 item : getItems()) {
            if (item.getSpot() > startSpot) {
                item.setNextAvaSpot(getNextAvaSpot() - 1);
                //loops through the item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
                for (int i = 0; i < item.components.size(); i++) {
                    item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() + (item.getHeight()+ITEMGAP));
                }
                for (int i = 0; i < item.minipanels.size(); i++) {
                    item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() + (item.getHeight()+ITEMGAP));
                    item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() + (getHeight()+5));
                }
                //moves the textfields with the item if in edit mode
                if (item.editMode) {
                    item.saveEdit();
                    item.edit();
                }

                //only show the items in spots 0-5
                if(item.getSpot() < 0 || item.getSpot() > 5){
                    item.setSoftVisible(false);
                }
                else if(item.getSpot() >= 0 && item.getSpot() <= 5){
                    item.setSoftVisible(true);
                }
            }
        }
    }

*/
    public String shortenString(String str, float length){
        GlyphLayout layout = new GlyphLayout();
        String shortenedString;

        layout.setText(skin.getFont("default-font"), "...");
        float elipse = layout.width;

        for (int i = 0; i < str.length(); i++) {
            layout.setText(skin.getFont("default-font"), str.substring(0,i));
            if((layout.width+elipse) >= length || str.charAt(i) == '\n' || str.charAt(i) == '\r'){
                if(i == 0) return "...";
                else if(str.charAt(i) == '\n' || str.charAt(i) == '\r'){
                    shortenedString = str.substring(0,i);
                    return shortenedString.trim()+"...";
                }
                else {
                    shortenedString = str.substring(0, i - 1);
                    return shortenedString.trim() + "...";
                }
            }
        }
        return str;
    }

    public void setSpot(int spot){
        this.spot = spot;
    }
    public void setID(int ID){
        this.ID = ID;
    }
/*
    public int getNextAvaSpot(){
        return -1;
    }
*/
    public ArrayList<Item2> getItems(){
        return null;
    }
    /**
     * @return returns this item's spot value
     */
    public int getSpot(){
        return spot;
    }
    public int getID(){
        return ID;
    }
    public Item2 getItemBySpot(int spot){
        return null;
    }
    public void setParentIP(ItemPanel IP){
        parentIP = IP;
    }
    public ItemPanel getParentIP(){
        return parentIP;
    }
    /**
     * @return returns if the panel is in edit mode
     */
    public boolean getEditMode(){
        return editMode;
    }

    /**
     * renders this item and any minipanels it may hold
     * @param batch the batch...
     */
    public void render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        batch.draw(texture, getX(), getY(), getWidth(), getHeight());

        for (MBComponent component: components) {
            if(component.supposedToBeVisible) {
                component.getComponent().draw(batch, component.aFloat);
                if(component.components.size() > 0){
                    for (MBComponent componentComp: component.components) {
                        componentComp.getComponent().draw(batch, componentComp.aFloat);
                    }
                }
            }
        }
        //loops through this panel's list of minipanels
        for (Panel minipanel : minipanels) {
            if(minipanel.supposedToBeVisible){
                minipanel.render(batch);
            }
        }
    }
}

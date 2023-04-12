package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

public class Item extends Minipanel{
    //skin of the item
    Skin skin;
    //strings for the labels
    ArrayList<String> labelTexts = new ArrayList<>();
    //textfields for when you edit the item (if weapon)
    ArrayList<MBTextField> textFields = new ArrayList<>();
    //labels for the item (if weapon)
    ArrayList<MBLabel> labels = new ArrayList<>();

    Integer[] statsIndexes;
    //if the panel is in edit mode
    protected boolean editMode = false;
    protected boolean customLayout = true;

    protected ItemPanel parentIP;
    protected int spot;
    protected int ID;

    protected final float ITEMGAP = 5;

    public Item(Screen screen) {
        this(new Rectangle(125, 790, 460, 40), screen);
        customLayout = false;
    }
    public Item(String fileLocation, Screen screen) {
        this(fileLocation, new Rectangle(125, 790, 460, 40), screen);
        skin = Main.skin;
    }
    public Item(Rectangle position, Screen screen) {
        this("assets\\Panels\\ItemPanel4.png", position, screen);
        skin = Main.skin;
    }
    public Item(String fileLocation, Rectangle position, Screen screen) {
        super(fileLocation, position, screen);
        skin = Main.skin;
    }

    /**
     * setting up the item. Different for every item
     */
    public void initialize(){

    }
    /**
     * sets the item and its components to this item's corresponding spot
     */
    protected void reformat() {
        float oldY, yGap, oldX, xGap;
        oldY = getY();
        oldX = getX();
        //updates the item's position
//        setPosition(parentIP.getSpot0Model().getX(),parentIP.getSpot0Model().getY()-((parentIP.getSpot0Model().getHeight()+parentIP.getItemGap()) * spot));
        reposition();
        setSize(parentIP.getSpot0Model().width, parentIP.getSpot0Model().height);

        yGap = getY() - oldY;
        xGap = getX() - oldX;
        //updates the item's components' positions
        for (MBComponent component : components) {
            component.setPosition(component.getX() + xGap, component.getY() + yGap);
            for (MBComponent MBComp: component.components) {
                MBComp.setPosition(MBComp.getX() + xGap, MBComp.getY() + yGap);
            }
        }
        //updates the item's minipanels' positions
        for (Panel minipanel: minipanels) {
            minipanel.setPosition(minipanel.getX() + xGap, minipanel.getY() + yGap);
            //updates the item's minipanels' components' positions
            for (MBComponent MPComp: minipanel.components) {
                MPComp.setPosition(MPComp.getX() + xGap, MPComp.getY() + yGap);
            }
        }
    }

    public void reposition(){
        int row = spot % getParentIP().getRows();
        int column = (spot / getParentIP().getRows());
        setPosition(parentIP.getSpot0Model().getX() +((parentIP.getSpot0Model().getWidth()+parentIP.getItemGap()) * column),
                parentIP.getSpot0Model().getY()-((parentIP.getSpot0Model().getHeight()+parentIP.getItemGap()) * row));
    }
    /**
     * makes the textfields appear above the labels
     */
    public void edit(){
        //to have only one item edited at a time
        if(getItems() != null) {
            for (Item item : getItems()) {
                if(item != this) item.saveEdit(); //if this isn't the item being edited...
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

        //making the textfields invisible
        for (int i = 0; i < textFields.size(); i++) {
            if(textFields.get(i).getSystem() == null) {
                MBSystem sys = new MBSystem(textFields.get(i), labels.get(i));
                final int finalI = i;
                sys.setUpdateAction(new Action() {
                    @Override
                    public boolean act(float v) {
                        labelTexts.set(finalI, textFields.get(finalI).getTextField().getText()); //updating the label list's text

                        labels.get(finalI).getLabel().setText(shortenString(labelTexts.get(finalI), labels.get(finalI).getWidth())); //updating the label's text

                        return true;
                    }
                });
            }
            textFields.get(i).updateSystem();

            textFields.get(i).setVisible(false);
        }

        editMode = false;
    }

    static public String shortenString(String str, float length){
        GlyphLayout layout = new GlyphLayout();
        String shortenedString;

        layout.setText(Main.skin.getFont("default-font"), "...");
        float elipse = layout.width;

        for (int i = 0; i < str.length(); i++) {
            layout.setText(Main.skin.getFont("default-font"), str.substring(0,i));
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
    /**
     * @return returns if the panel is in edit mode
     */
    public boolean isInEditMode(){
        return editMode;
    }

    public ArrayList<Item> getItems(){
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
    public Item getItemBySpot(int spot){
        for (Item item : parentIP.getItems()) {
            if (item.getSpot() == spot) {
                return item;
            }
        }
        return null;
    }
    public void setParentIP(ItemPanel IP){
        parentIP = IP;
    }
    public ItemPanel getParentIP(){
        return parentIP;
    }

    public boolean hasCustomLayout() {
        return customLayout;
    }

    public void deleteThisItem(){
        parentIP.delete(Item.this);
    }
    /**
     * renders this item and any minipanels it may hold
     */
    public void render() {
        if(spot < 0 || spot > parentIP.getMaxSpot()) return; //if out of bounds, don't render

        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        batch.draw(texture, getX(), getY(), getWidth(), getHeight());

        for (MBComponent component: components) {
            if(component.supposedToBeVisible) {
                component.render();
            }
        }
        //loops through this panel's list of minipanels
        for (Panel minipanel : minipanels) {
            if(minipanel.supposedToBeVisible){
                if(!editMode) minipanel.setSoftVisible(false);
                else minipanel.render();
            }
        }
    }
}

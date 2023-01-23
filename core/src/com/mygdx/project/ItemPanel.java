package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class ItemPanel extends Minipanel{
    private int totalItems = 0;
    private ArrayList<Item2> allItems = new ArrayList<>();
    private int nextAvaSpot = 0;
    private int MINSPOT = 0;
    private int MAXSPOT = 5;

    private final float ITEMGAP = 5;

    public ItemPanel(String fileLocation, Rectangle position) {
        super("assets\\Panels\\ListPanel.png", position);
    }
    /**
     * adds the minipanel to its panel
     */
    public void add(Item2 item){
        //sets the minipanel's parent to this panel
        item.parentPanel = this;
        item.setParentIP(this);
        item.setSpot(nextAvaSpot);
        nextAvaSpot++;
        item.setID(totalItems);
        totalItems++;

        allItems.add(item);
        minipanels.add(item);

        item.initialize();
        item.reformat();

        if(item.getSpot() > MAXSPOT) item.setSoftVisible(false);
    }

    /**
     * shuffles all the items one spot up
     */
    public void shuffleItemsUp(){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot--;
        //loops through all the items
        for(Item2 item : getItems()){
            item.setSpot(item.getSpot() - 1);

            item.reformat();
/*
            //loops through the item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
            for (int i = 0; i < item.components.size(); i++) {
                item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() + (item.getHeight()+ITEMGAP));
            }
            for (int i = 0; i < item.minipanels.size(); i++) {
                item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() + (item.getHeight()+ITEMGAP));
                item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() + (getHeight()+5));
            }
*/
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

    /**
     * shuffles all the items under the start spot up one spot
     * @param startSpot the highest spot you don't want to raise
     */
    public void shuffleItemsUp(int startSpot){
        //reduces the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot--;
        //loops through all the items
        for(Item2 item : getItems()) {
            if (item.getSpot() > startSpot) {
                item.setSpot(item.getSpot() - 1);

                item.reformat();
/*
                //loops through the item's components' position and increases the Y value by the item's height plus the gap between items (moving it up)
                for (int i = 0; i < item.components.size(); i++) {
                    item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() + (item.getHeight()+ITEMGAP));
                }
                for (int i = 0; i < item.minipanels.size(); i++) {
                    item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() + (item.getHeight()+ITEMGAP));
                    item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() + (getHeight()+5));
                }
*/
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

    /**
     * shuffles all the items down
     */
    public void shuffleItemsDown(){
        //increases the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot++;
        //loops through all the items
        for(Item2 item : getItems()){
            item.setSpot(item.getSpot() + 1);

            item.reformat();
/*
            //loops through the item's components' position and reduces the Y value by the item's height plus the gap between items (moving it down)
            for (int i = 0; i < item.components.size(); i++) {
                item.components.get(i).setPosition(item.components.get(i).getX(), item.components.get(i).getY() - (item.getHeight()+5));
            }
            for (int i = 0; i < item.minipanels.size(); i++) {
                item.minipanels.get(i).setPosition(item.minipanels.get(i).getX(), item.minipanels.get(i).getY() - (item.getHeight()+5));
                item.minipanels.get(i).components.get(i).setPosition(item.minipanels.get(i).components.get(i).getX(), item.minipanels.get(i).components.get(i).getY() - (getHeight()+5));
            }
*/
            //moves the textfields with the item if in edit mode
            if(item.editMode){
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

    public Item2 getItemBySpot(int spot){
        for (Item2 item : allItems) {
            if (item.getSpot() == spot) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Item2> getItems(){
        return allItems;
    }
    public int getTotalItems() {
        return totalItems;
    }
    public int getNextAvaSpot(){
        return nextAvaSpot;
    }

    /**
     * renders all the items in this panel
     * @param batch the batch...
     */
    public void render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        batch.draw(texture, getX(), getY(), getWidth(), getHeight());

        for (Item2 item : allItems) {
            if(item.supposedToBeVisible) item.render(batch);
        }
    }
}

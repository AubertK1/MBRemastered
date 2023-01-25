package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class ItemPanel extends Minipanel{
    private int totalItems = 0;
    private final ArrayList<Item> allItems = new ArrayList<>();
    private int nextAvaSpot = 0;

    private final Rectangle spot0Model;
    private final int MINSPOT = 0;
    private int MAXSPOT = 5;

    private float ITEMGAP = 5;

    public ItemPanel(String fileLocation, Rectangle position) {
        super(fileLocation, position);
        spot0Model = new Rectangle(getX(), getHeight()+getY() - 40, 460, 40);
    }
    /**
     * adds the minipanel to its panel
     */
    public void add(Item item){
        //sets the minipanel's parent to this panel
        item.parentPanel = this;
        item.setParentIP(this);
        item.setSpot(nextAvaSpot);
        nextAvaSpot++;
        item.setID(totalItems);
        totalItems++;

        allItems.add(item);
        minipanels.add(item);

        if(getItems().size() == 1){ //only if this is the first item added to the item panel, so it can set a precedent for the layout
            if(item.hasCustomLayout()){
                //setting the model for the items based on the item's bounds
                spot0Model.set(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
        //setting the item's bounds based on the spot 0 model
        item.setPosition(spot0Model.getX(), spot0Model.getY());
        item.setSize(spot0Model.getWidth(), spot0Model.getHeight());

        item.initialize();
        item.reformat();

        if(item.getSpot() > MAXSPOT) item.setSoftVisible(false);
    }

    public void delete(Item item){
        int itemSpot = item.getSpot();

        delete((Panel) item);

        allItems.remove(item);
        shuffleItemsUp(itemSpot);
    }

    /**
     * shuffles all the items one spot up
     */
    public void shuffleItemsUp(){
        if(getLowestItem().getSpot() == MINSPOT) return; //do not continue if the last item is at the top spot
        //reduces the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot--;
        //loops through all the items
        for(Item item : getItems()){
            item.setSpot(item.getSpot() - 1);

            item.reformat();

            //moves the textfields with the item if in edit mode
            if (item.editMode) {
                item.saveEdit();
                item.edit();
            }

            //only show the items in spots 0-5
            if(item.getSpot() < MINSPOT || item.getSpot() > MAXSPOT){
                item.setSoftVisible(false);
            }
            else if(item.getSpot() >= MINSPOT && item.getSpot() <= MAXSPOT){
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
        for(Item item : getItems()) {
            if (item.getSpot() > startSpot) {
                item.setSpot(item.getSpot() - 1);

                item.reformat();

               //moves the textfields with the item if in edit mode
                if (item.editMode) {
                    item.saveEdit();
                    item.edit();
                }

                //only show the items in spots 0-5
                if(item.getSpot() < MINSPOT || item.getSpot() > MAXSPOT){
                    item.setSoftVisible(false);
                }
                else if(item.getSpot() >= MINSPOT && item.getSpot() <= MAXSPOT){
                    item.setSoftVisible(true);
                }
            }
        }
    }

    /**
     * shuffles all the items down
     */
    public void shuffleItemsDown(){
        if(getHighestItem().getSpot() == MINSPOT) return; //do not continue if the first item is at the top spot

        //increases the next available spot value by one so that new items get added under the lowest item always
        nextAvaSpot++;
        //loops through all the items
        for(Item item : getItems()){
            item.setSpot(item.getSpot() + 1);

            item.reformat();

            //moves the textfields with the item if in edit mode
            if(item.editMode){
                item.saveEdit();
                item.edit();
            }

            //only show the items in spots 0-5
            if(item.getSpot() < MINSPOT || item.getSpot() > MAXSPOT){
                item.setSoftVisible(false);
            }
            else if(item.getSpot() >= MINSPOT && item.getSpot() <= MAXSPOT){
                item.setSoftVisible(true);
            }
        }
    }
    public void setMaxSpot(int MAXSPOT) {
        this.MAXSPOT = MAXSPOT;
    }
    public void setItemGap(float ITEMGAP) {
        this.ITEMGAP = ITEMGAP;
    }
    public float getItemGap() {
        return ITEMGAP;
    }
    public int getMaxSpot() {
        return MAXSPOT;
    }
    public Item getItemBySpot(int spot){
        for (Item item : allItems) {
            if (item.getSpot() == spot) {
                return item;
            }
        }
        return null;
    }

    /**
     * @return returns the item with the highest spot (the lowest item visually)
     */
    public Item getLowestItem(){
        int highestSpot = -100;

        for (Item allItem : allItems) {
            if (allItem.getSpot() > highestSpot) highestSpot = allItem.getSpot();
        }
        return getItemBySpot(highestSpot);
    }
    /**
     * @return returns the item with the lowest spot (the highest item visually)
     */
    public Item getHighestItem(){
        int lowestSpot = 100;

        for (Item allItem : allItems) {
            if (allItem.getSpot() < lowestSpot) lowestSpot = allItem.getSpot();
        }
        return getItemBySpot(lowestSpot);
    }
    public ArrayList<Item> getItems(){
        return allItems;
    }
    public int getTotalItems() {
        return totalItems;
    }
    public int getNextAvaSpot(){
        return nextAvaSpot;
    }
    public Rectangle getSpot0Model(){
        return spot0Model;
    }
/*
    */
/**
     * renders all the items in this panel
     * @param batch the batch...
     *//*

    public void render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        batch.draw(texture, getX(), getY(), getWidth(), getHeight());

        for (Item item : allItems) {
            if(item.supposedToBeVisible) item.render(batch);
        }
    }
*/
}

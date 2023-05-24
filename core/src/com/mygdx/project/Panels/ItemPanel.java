package com.mygdx.project.Panels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.project.Components.MBComponent;
import com.mygdx.project.Components.MBTextArea;
import com.mygdx.project.Components.MBTextField;
import com.mygdx.project.PlayerScreen;
import com.mygdx.project.Screen;
import com.mygdx.project.Stats;

import java.util.ArrayList;

public class ItemPanel extends Minipanel{
    private int totalItems = 0;
    private final ArrayList<Item> allItems = new ArrayList<>();
    private int nextAvaSpot = 0;

    private final Rectangle spot0Model;
    private final int MINSPOT = 0;
    private int maxSpot = 100;
    private int columns = 1;
    private int rows = maxSpot;

    private float ITEMGAP = 5;

    public ItemPanel(String fileLocation, Rectangle position, Screen screen) {
        super(fileLocation, position, screen);
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

        if(item.getSpot() > maxSpot) item.setVisible(false);
    }

    public void delete(Item item){
        int itemSpot = item.getSpot();

        getScreen().setAllComps(MBComponent.reaarrangeList(this.screen));
        getScreen().resetCompIDs();

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
            if(item.getSpot() < MINSPOT || item.getSpot() > maxSpot){
                item.setVisible(false);
            }
            else if(item.getSpot() >= MINSPOT && item.getSpot() <= maxSpot){
                item.setVisible(true);
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
                if(item.getSpot() < MINSPOT || item.getSpot() > maxSpot){
                    item.setVisible(false);
                }
                else if(item.getSpot() >= MINSPOT && item.getSpot() <= maxSpot){
                    item.setVisible(true);
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
            if(item.getSpot() < MINSPOT || item.getSpot() > maxSpot){
                item.setVisible(false);
            }
            else if(item.getSpot() >= MINSPOT && item.getSpot() <= maxSpot){
                item.setVisible(true);
            }
        }
    }

    public void saveItems(int itemType){
        if(itemType != 0 && itemType != 1) return;

        Stats stats = screen.getStats();

        int numOfItems = allItems.size();
        int start = 100;
        if(itemType == 1) start = 1000;

        for (int i = start; i < start + 899; i++) {
            try{
                stats.removeStat(i);
            } catch (NullPointerException n){
                if(i % 10 == 0) break;
            }
        }

        for (int statBlock = start; statBlock < start + (allItems.size() * 10); statBlock += 10) {
            stats.newItemStatBlock(itemType, itemType == 0 ? 4 : 9);
            Item item = allItems.get((statBlock - start) / 10);

            item.assignStats(statBlock);

            if(itemType == 0) {
                for (MBTextField tf : item.textFields) {
                    if(tf.getAssignedStat() == null) continue;

                    String statString = String.valueOf(tf.getAssignedStat());
                    int idInsideBlock = Integer.parseInt(statString.substring(statString.length() - 1));
                    tf.assignStat(statBlock + idInsideBlock);
                }
            }
            else if(itemType == 1){
                for (MBTextField tf : item.textFields) {
                    if(tf.getAssignedStat() == null) continue;
                    String statString = String.valueOf(tf.getAssignedStat());
                    int idInsideBlock = Integer.parseInt(statString.substring(statString.length() - 1));
                    tf.assignStat(statBlock + idInsideBlock);
                }
                for (MBComponent comp: ((SpellItem) item).getTipbox().components) {
                    if(comp instanceof MBTextField){
                        MBTextField tf = (MBTextField) comp;

                        if(tf.getAssignedStat() == null) continue;
                        String statString = String.valueOf(tf.getAssignedStat());
                        int idInsideBlock = Integer.parseInt(statString.substring(statString.length() - 1));
                        tf.assignStat(statBlock + idInsideBlock);
                    }
                    else if(comp instanceof MBTextArea) {
                        MBTextArea ta = (MBTextArea) comp;

                        if(ta.getAssignedStat() == null) continue;
                        String statString = String.valueOf(ta.getAssignedStat());
                        int idInsideBlock = Integer.parseInt(statString.substring(statString.length() - 1));
                        ta.assignStat(statBlock + idInsideBlock);
                    }
                }
            }
        }
    }
    public void loadItems(int itemType){
        if(itemType != 0 && itemType != 1) return;

        Stats stats = screen.getStats();

        int numOfItems = 0;
        int start = 100;
        if(itemType == 1) start = 1000;

        while(true){
            if(stats.getValue(start + (numOfItems * 10)).toString().equals("-1")) break;
            else numOfItems++;
        }

        for (int i = allItems.size() - 1; i >= 0; i--) {
            delete(allItems.get(i));
        }

        for (int i = 0; i < numOfItems; i++) {
            Item newItem = null;
            if(itemType == 0) newItem = new WeaponItem(screen);
            else if(itemType == 1) newItem = new SpellItem(screen);

            newItem.assignStats(start + (i * 10));

            add(newItem);
        }

        if(((PlayerScreen) screen).getItemTab() != itemType + 1) this.setVisible(false); //makes this invisible if its not the selected tab
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
    public void setRows(int rows) {
        this.rows = rows;
        if(rows > maxSpot) maxSpot = rows;
    }
    public void setMaxSpot(int maxSpot) {
        this.maxSpot = maxSpot;
    }
    public void setItemGap(float ITEMGAP) {
        this.ITEMGAP = ITEMGAP;
    }
    public int getColumns() {
        return columns;
    }
    public int getRows() {
        return rows;
    }
    public float getItemGap() {
        return ITEMGAP;
    }
    public int getMaxSpot() {
        return maxSpot;
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
}

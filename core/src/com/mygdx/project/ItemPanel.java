package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class ItemPanel extends Minipanel{
    private int totalItems = 0;
    private ArrayList<Item2> allItems = new ArrayList<>();
    private int nextAvaSpot = 0;

    public ItemPanel(String fileLocation, Rectangle position) {
        super("assets\\Panels\\ListPanel.png", position);
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
}

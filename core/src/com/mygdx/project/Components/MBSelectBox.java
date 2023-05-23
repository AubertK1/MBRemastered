package com.mygdx.project.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.project.Screen;
import com.mygdx.project.SelectBoxWrapper;

public class MBSelectBox extends MBComponent{
    final SelectBoxWrapper<String> dropdown;
    private int inactiveLayer = 2;

    public MBSelectBox(Screen screen) {
        super(screen);
        dropdown = new SelectBoxWrapper<>(skin);
        dropdown.setScrollingDisabled(true);
    }
    public String getSelected(){
        return dropdown.getSelected();
    }
    @Override
    public void setLayer(int layer) {
        super.setLayer(layer);
        inactiveLayer = layer;
    }
    private void setLayerSoft(int layer){
        super.setLayer(layer);
    }
    public void addListener(EventListener listener){
        dropdown.scrollPane.list.addListener(listener);
    }
    public void setItems(String... items){
        dropdown.setItems(items);
    }
    public void setItems(Array<String> items){
        dropdown.setItems(items);
    }
    public void addItem(String item){
        Array<String> items = dropdown.getItems();
        items.add(item);
        dropdown.setItems(items);
    }
    public Array<String> getItems(){
        return dropdown.getItems();
    }
    public void insertItemA(String item){
        Array<String> items = dropdown.getItems();
        items.insert(items.size - 1, item);
        dropdown.setItems(items);
    }
    public void removeItem(int index){
        Array<String> items = dropdown.getItems();
        items.removeIndex(index);
        dropdown.setItems(items);
    }
    public void removeItem(Object o){
        Array<String> items = dropdown.getItems();
        int index = -1;
        for (int i = 0; i < items.size; i++) {
            if(items.get(i) == o){
                index = i;
                break;
            }
        }
        if(index == -1) return;
        items.removeIndex(index);
        dropdown.setItems(items);
    }
    public boolean isActive(){
        return dropdown.isActive;
    }
    public Actor getActor(){
        return dropdown;
    }

    public void setInWindow(boolean inWindow){
        dropdown.inWindow = inWindow;
        dropdown.windowGap.set(dropdown.getX(), dropdown.getY() - dropdown.scrollPane.getHeight());
    }

    @Override
    public void render() {
        if(!isActive()){
            if(getLayer() != inactiveLayer) setLayer(inactiveLayer);
        }
        else setLayerSoft(inactiveLayer + 1);
        super.render();
    }
}

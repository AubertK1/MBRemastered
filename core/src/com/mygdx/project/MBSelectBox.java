package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.mygdx.project.Main.batch;

public class MBSelectBox extends MBComponent{
    final SelectBoxWrapper<String> dropdown;
    private int inactiveLayer = 2;

    public MBSelectBox(Screen screen) {
        super(screen);
        dropdown = new SelectBoxWrapper<>(skin);
        dropdown.setScrollingDisabled(true);
    }

    public boolean addScrollPaneListener(EventListener listener){
        return dropdown.scrollPane.list.addListener(listener);
    }
    public void setItems(String... items){
        dropdown.setItems(items);
    }
    public boolean isActive(){
        return dropdown.isActive;
    }
    public Actor getActor(){
        return dropdown;
    }


    @Override
    public void render() {
        if(this.dropdown.getItems().get(0).equals("soft"))
            System.out.print("");
        if(!isActive()){
            if(getLayer() != inactiveLayer) setLayer(inactiveLayer);
            inactiveLayer = getLayer();
        }
        else setLayer(inactiveLayer + 1);
        super.render();
    }
}

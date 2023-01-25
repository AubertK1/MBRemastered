package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.mygdx.project.Main.batch;

public class MBSelectBox extends MBComponent{
    final SelectBoxWrapper<String> dropdown;

    public MBSelectBox() {
        dropdown = new SelectBoxWrapper<>(skin);
        dropdown.setScrollingDisabled(true);
    }

    public boolean addScrollPaneListener(EventListener listener){
        return dropdown.scrollPane.list.addListener(listener);
    }
    public void setItems(String... items){
        dropdown.setItems(items);
    }
    public Actor getComponent(){
        return dropdown;
    }
}

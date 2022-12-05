package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MBSelectBox extends MBComponent{
    final SelectBoxWrapper<String> dropdown;

    public MBSelectBox() {
        dropdown = new SelectBoxWrapper<>(skin);
        dropdown.setScrollingDisabled(true);
    }

    public void setItems(String... items){
        dropdown.setItems(items);
    }
    public Actor getComponent(){
        return dropdown;
    }
    public void draw(float alpha){
        Main.allComps = reaarrangeList();
        Panel.resetCompIDs();

        getComponent().draw(Main.batch, alpha);
        for (MBComponent innerComp: components) {
            innerComp.draw(innerComp.aFloat);
        }
    }

}

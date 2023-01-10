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
    public void draw(float alpha){
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        Main.allComps = reaarrangeList();
        Panel.resetCompIDs();

        getComponent().draw(Main.batch, alpha);
        for (MBComponent innerComp: components) {
            innerComp.draw(innerComp.aFloat);
        }
    }

}

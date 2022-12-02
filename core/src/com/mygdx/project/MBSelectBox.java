package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MBSelectBox extends MBComponent{
    final SelectBoxWrapper<String> dropdown;
    Actor scrollPane;

    public MBSelectBox() {
        dropdown = new SelectBoxWrapper<>(skin);
        deleteLater = 1;
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

        if(this.deleteLater != -1){
            System.out.print("");
        }
        getComponent().draw(Main.batch, alpha);
        for (MBComponent innerComp: components) {
            innerComp.draw(innerComp.aFloat);
        }
        scrollPane = Main.stage.getActors().get(stage.getActors().size-1);
        if(scrollPane instanceof SelectBoxWrapper.SelectBoxScrollPaneWrapper){
//			scrollpanes.get(0).dropdown.scrollPane.list.setX(((MBSelectBox)scrollpanes.get(0)).getX());
//			scrollpanes.get(0).dropdown.scrollPane.list.setX(200);
//			scrollpanes.get(0).dropdown.scrollPane.list.item
//			scrollpanes.get(0).dropdown.scrollPane.list.setY(((MBSelectBox)scrollpanes.get(0)).getY());

//			scrollpanes.get(0).dropdown.scrollPane.list.draw(batch, 1);
//            dropdown.scrollPane.list.draw(Main.batch, 1);
        }

    }

}

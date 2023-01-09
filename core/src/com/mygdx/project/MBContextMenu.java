package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

public class MBContextMenu<T> extends MBComponent{
    ContextMenu<T> contextMenu;

    public MBContextMenu() {
        contextMenu = new ContextMenu<>(skin);
        contextMenu.setWidth(200);

        Main.allComps.add(this);
        Main.stage.addActor(this.getComponent());
    }
    public void showAt(int x, int y){
        contextMenu.showAt(Main.stage, x, y);
    }
    public boolean addListener(ClickListener listener){
        return contextMenu.list.addTempListener(listener);
    }
    public void setItems(T... newItems){
        contextMenu.setItems(newItems);
    }
    public @Null T getSelected () {
        return contextMenu.getSelected();
    }
    public boolean isActive(){
        return contextMenu.doShow;
    }
    public Actor getComponent(){
        return contextMenu;
    }
}

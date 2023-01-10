package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

public class MBContextMenu extends MBComponent{
    ContextMenu<String> contextMenu;

    public MBContextMenu() {
        contextMenu = new ContextMenu<>(skin);
        contextMenu.setWidth(200);

        Main.allComps.add(this);
        Main.stage.addActor(this.getComponent());
    }

    public void buildDefaultMenu(){
        setItems("temp", "temp 2", "temp 3");
        addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                String word = contextMenu.getSelected();
                switch (word){
                    case "temp":
                        System.out.println("hi");
                        break;
                    case "temp 2":
                        System.out.println("hi again");
                        break;
                    case "temp 3":
                        System.out.println("last one");
                        break;
                }
            }
        });
    }
    public void showAt(int x, int y){
        contextMenu.showAt(Main.stage, x, y);
    }
    public boolean addListener(ClickListener listener){
        return contextMenu.list.addTempListener(listener);
    }
    public boolean isCustomized(){
        return contextMenu.list.isCustomized();
    }
    public void setItems(String... newItems){
        contextMenu.setItems(newItems);
    }
    public @Null String getSelected () {
        return contextMenu.getSelected();
    }
    public boolean isActive(){
        return contextMenu.doShow;
    }
    public Actor getComponent(){
        return contextMenu;
    }
}

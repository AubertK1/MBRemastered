package com.mygdx.project.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import com.mygdx.project.Actors.ContextMenu;
import com.mygdx.project.Main;
import com.mygdx.project.Screen;

import static com.mygdx.project.Main.batch;

public class MBContextMenu extends MBComponent{
    ContextMenu<String> contextMenu;

    public MBContextMenu() {
        super(null);
        contextMenu = new ContextMenu<>(skin);
        contextMenu.setWidth(200);

/*
        getScreen().allComps.add(this);
        Main.stage.addActor(this.getActor());
*/
    }

    public void render(){
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, getOpacity());

        getActor().draw(batch, getOpacity());
        for (MBComponent innerComp: components) {
            innerComp.render();
        }
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
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
        return contextMenu.getList().addTempListener(listener);
    }
    public boolean isCustomized(){
        return contextMenu.getList().isCustomized();
    }
    public void setItems(String... newItems){
        contextMenu.setItems(newItems);
    }
    public @Null String getSelected () {
        return contextMenu.getSelected();
    }
    public boolean isActive(){
        return contextMenu.getDoShow();
    }
    public Actor getActor(){
        return contextMenu;
    }
}

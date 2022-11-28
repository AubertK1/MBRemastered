package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MBDropdown extends MBComponent{
    final SelectBox<String> dropdown;
    final MBScrollpane pane;
    boolean i = false;
    int a = 2;

    public MBDropdown() {
        dropdown = new SelectBox<>(skin);
        pane = new MBScrollpane(dropdown);
        pane.scrollPane = dropdown.getScrollPane();

        dropdown.addListener(new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("ye");
                if(i) {
                    delete(pane);

/*
                    int paneID = -1;
                    for (int i = 0; i < Main.stage.getActors().size; i++) {
                        if (Main.stage.getActors().get(i) == pane.scrollPane) paneID = i;
                    }
                    if (paneID != -1) Main.stage.getActors().get(paneID).addAction(Actions.removeActor());
*/

                    i = false;
                }
                else{
                    if(a == 2) {
                        pane.scrollPane = dropdown.getScrollPane();
                        add(pane);
                        i = true;
                        a = 2;
                    }
/*
                    else{
                        pane.scrollPane = dropdown.getScrollPane();
                    }
*/
                }
                return true;
            }
        });
    }

    public void setItems(String... items){
        dropdown.setItems(items);
    }
    public Actor getComponent(){
        return dropdown;
    }
}

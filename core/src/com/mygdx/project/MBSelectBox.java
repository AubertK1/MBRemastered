package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MBSelectBox extends MBComponent{
    final SelectBoxWrapper<String> dropdown;
    final MBScrollpane pane;
    boolean i = false;
    int a = 2;

    public MBSelectBox() {
        dropdown = new SelectBoxWrapper<>(skin);
        pane = new MBScrollpane(dropdown);
        pane.scrollPane = (SelectBoxScrollPaneWrapper) dropdown.getScrollPane2();

        dropdown.addListener(new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("ye");
                if(i) {
                    delete(pane);

                    i = false;
                }
                else{
                    if(a == 2) {
                        pane.scrollPane = (SelectBoxScrollPaneWrapper) dropdown.getScrollPane2();
                        add(pane);
                        i = true;
                        a = 2;
                    }
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

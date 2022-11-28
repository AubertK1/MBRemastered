package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

public class MBScrollpane extends MBComponent{
    SelectBox<String> selectBox;
    SelectBox.SelectBoxScrollPane scrollPane;

    public MBScrollpane(SelectBox<String> selectBox2) {
        selectBox = selectBox2;
        scrollPane = selectBox.getScrollPane();
    }

    @Override
    public Actor getComponent() {
        return scrollPane;
    }
}

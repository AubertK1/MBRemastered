package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

public class MBScrollpane extends MBComponent{
    SelectBoxWrapper<String> selectBox;
    SelectBoxScrollPaneWrapper scrollPane;

    public MBScrollpane(SelectBoxWrapper<String> selectBox2) {
        selectBox = selectBox2;
        scrollPane = (SelectBoxScrollPaneWrapper) selectBox.getScrollPane2();
        deleteLater = 2;
    }

    @Override
    public Actor getComponent() {
        return scrollPane;
    }
}

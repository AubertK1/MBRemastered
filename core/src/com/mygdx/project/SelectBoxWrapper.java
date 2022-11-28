package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SelectBoxWrapper<T> extends SelectBox<T> {
    public SelectBoxWrapper(Skin skin) {
        super(skin);
    }

    public static class SelectBoxScrollPaneWrapper<T> extends SelectBox.SelectBoxScrollPane<T> {

        public SelectBoxScrollPaneWrapper(SelectBox<T> selectBox) {
            super(selectBox);
        }
    }
}

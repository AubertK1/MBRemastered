package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class MBLabel extends MBComponent{
    //making the label
    Label label;
    public MBLabel(String text, Skin skin) {
        //setting the label
        label = new Label(text, skin);
    }

    public Widget getComponent(){
        return label;
    }
}

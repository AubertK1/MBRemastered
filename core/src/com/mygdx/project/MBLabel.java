package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;

public class MBLabel extends MBComponent{
    //making the label
    private final Label label;

    public MBLabel(String text, Skin skin) {
        //setting the label
        label = new Label(text, skin);
    }
    public MBLabel(String text) {
        //setting the label
        label = new Label(text, skin);
    }
    public void center(){
        label.setAlignment(Align.center);
    }

    public Actor getActor(){
        return label;
    }

    public Label getLabel() {
        return label;
    }
}

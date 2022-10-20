package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    public void setPosition(Rectangle position){
        super.position = position;
        label.setSize(position.width, position.height);
        label.setPosition(position.x, position.y);
    }

    public Actor getComponent(){
        return label;
    }
}

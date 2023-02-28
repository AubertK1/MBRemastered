package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class MBLabel extends MBComponent{
    //making the label
    private final Label label;

    public MBLabel(String text, Screen screen) {
        super(screen);
        //setting the label
        label = new Label(text, skin);
    }
    public void center(){
        label.setAlignment(Align.center);
    }

    public void setAlignment(int alignment){
        label.setAlignment(alignment);
    }
   public void setText(String text){
        label.setText(text);
    }

    public Actor getActor(){
        return label;
    }

    public Label getLabel() {
        return label;
    }
}

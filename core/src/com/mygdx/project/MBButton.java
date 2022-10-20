package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MBButton extends MBComponent{
    Button button;

    public MBButton(Skin skin) {
        button = new Button(skin);
    }

    public Actor getComponent(){
        return button;
    }
    public Button getButton(){
        return button;
    }
}

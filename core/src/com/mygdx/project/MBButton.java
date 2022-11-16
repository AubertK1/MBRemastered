package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MBButton extends MBComponent{
    Button button;
    ImageButton button2;

    public MBButton(Skin skin) {
        button = new Button(skin);
    }
    @Override
    public boolean addListener(EventListener listener) {
        return button.addListener(listener);
    }
    public Actor getComponent(){
        return button;
    }
    public Button getButton(){
        return button;
    }
}

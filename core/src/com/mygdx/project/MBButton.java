package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MBButton extends MBComponent{
    Button button;
    ImageButton button2;
    TextButton button3;

    public MBButton(Skin skin) {
        button = new Button(skin);
    }
    public MBButton(Skin skin, String styleName) {
        button = new ImageButton(skin, styleName);
    }
    public MBButton(String text, Skin skin, float width, float height) {
        button3 = new TextButton(text, skin, "default");
        button3.getLabelCell().setActorWidth(width);
        button3.getLabelCell().setActorHeight(height);
        button = button3;
    }
    @Override
    public boolean addListener(EventListener listener) {
        if(button == null) return button2.addListener(listener);
        else return button.addListener(listener);
    }
    public Actor getComponent(){
        if(button == null) return button2;
        else return button;
    }
    public Button getButton(){
        return button;
    }
}

package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MBButton extends MBComponent{
    Button button;
    ImageButton button2;
    ImageTextButton button3;

    public MBButton(Skin skin) {
        button2 = new ImageButton(skin);
    }
    public MBButton(Skin skin, String styleName) {
//        button = new Button(skin, styleName);
        Texture texture1 = new Texture("core\\pics\\Logos\\downlogo.png");
//        button2.setStyle(skin.get(ImageButton.ImageButtonStyle.class));
        button2 = new ImageButton(skin);
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

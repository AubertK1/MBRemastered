package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MBButton extends MBComponent{
    Button button;
    ImageButton button2;
    TextButton button3;
    float oldA;

    public MBButton(Skin skin) {
        button = new Button(skin);
    }
    public MBButton(Skin skin, String styleName) {
        button = new ImageButton(skin, styleName);
    }
    public MBButton(Skin skin, Texture texture) {
        skin1 = skin;
        button = new TextButton("ADD IMAGE", skin1);
    }
    public MBButton(String text, Skin skin, float width, float height) {
        button3 = new TextButton(text, skin, "default");
        button3.getLabelCell().setActorWidth(width);
        button3.getLabelCell().setActorHeight(height);
        button = button3;
    }
    public void setImage(Texture texture){
        Rectangle pos = new Rectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        button = new ImageButton(skin1);
        button.setPosition(pos.x, pos.y);
        button.setSize(pos.width, pos.height);
        skin1.get(ImageButton.ImageButtonStyle.class).imageUp = new TextureRegionDrawable(new TextureRegion(texture));
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

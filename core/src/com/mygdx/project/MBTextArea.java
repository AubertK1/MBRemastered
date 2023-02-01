package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * the text area component
 */
public class MBTextArea extends MBComponent{
    //making the text area
    private final TextArea textArea;

    public MBTextArea(String text, Screen screen) {
        super(screen);
        //setting the text area
        textArea = new TextArea(text, skin);
    }

    public Actor getActor(){
        return textArea;
    }
    public TextArea getTextArea(){
        return textArea;
    }
    public String getText(){
        return textArea.getText();
    }
    public void setText(String text){
        textArea.setText(text);
    }
}

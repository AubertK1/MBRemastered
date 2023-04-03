package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * the text area component
 */
public class MBTextArea extends MBComponent{
    //making the text area
    private final TextArea textArea;

    private Values.Stat stat;

    public MBTextArea(String text, Screen screen) {
        this(text, screen, null);
    }
    public MBTextArea(String text, final Screen screen, final Values.Stat stat) {
        super(screen);
        //setting the text area
        textArea = new TextArea(text, skin);
        this.stat = stat;

        if(stat != null){
            setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    screen.getStats().setStat(stat, getText());
                    System.out.println(Values.statToString(stat));
                }
            });
        }
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
    public void setKeyListener(TextField.TextFieldListener listener){
        textArea.setTextFieldListener(listener);
    }

    public Values.Stat getAssignedStat(){
        return stat;
    }
    public void setStatValue(int value){
        screen.getStats().setStat(stat, value);
    }
}

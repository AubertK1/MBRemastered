package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class MBTextField extends MBComponent{
    private final TextField textField;

    public MBTextField(String text, Screen screen) {
        super(screen);
        textField = new TextField(text, skin);
    }

    public void setKeyListener(TextField.TextFieldListener listener){
        textField.setTextFieldListener(listener);
    }

    public Actor getActor(){
        return textField;
    }

    public TextField getTextField(){
        return textField;
    }
    public String getText(){
        return textField.getText();
    }
    public void setText(String text){
        textField.setText(text);
    }
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MBTextField extends MBComponent{
    TextField textField;

    public MBTextField(String text, Skin skin) {
        Skin uiSkin = new Skin (Gdx.files.internal(
                "assets\\skins\\uiskin.json"));
        textField = new TextField(text, skin);
    }
    public void setKeyListener(TextField.TextFieldListener listener){
        textField.setTextFieldListener(listener);
    }

    public Actor getComponent(){
        return textField;
    }
}

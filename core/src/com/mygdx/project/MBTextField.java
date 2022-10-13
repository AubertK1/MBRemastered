package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;


public class MBTextField extends MBComponent{
    TextField textField;
    TextArea textArea;

    public MBTextField(String text, Skin skin) {
        Skin uiSkin = new Skin (Gdx.files.internal(
                "C:\\Users\\ak2000\\Documents\\Mine\\MBRemastered\\MBRemastered\\MBRemastered\\assets\\skins\\uiskin.json"));
        textField = new TextField(text, skin);
        textArea = new TextArea(text, uiSkin);
        texture = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
                "\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\DescriptionPanel.png");
    }

    public Widget getComponent(){
        return textArea;
    }
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;



public class MBTextField extends MBComponent{
    TextField textField;

    public MBTextField() {
        Skin uiSkin = new Skin (Gdx.files.internal(
                "C:\\Users\\ak2000\\Documents\\Mine\\MBRemastered\\MBRemastered\\MBRemastered\\assets\\skins\\uiskin.json"));
        textField = new TextField("test", uiSkin);
        texture = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
                "\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\DescriptionPanel.png");
    }
}

package com.mygdx.project;

import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class MBTextField extends MBComponent{
    TextField textField;

    public MBTextField() {
        textField = new TextField();
        texture = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
                "\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\DescriptionPanel.png");
    }
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * the text area component
 */
public class MBTextArea extends MBComponent{
    //making the text area
    TextArea textArea;

    public MBTextArea(String text, Skin skin) {
        Skin uiSkin = new Skin (Gdx.files.internal(
                "C:\\Users\\ak2000\\Documents\\Mine\\MBRemastered\\MBRemastered\\MBRemastered\\assets\\skins\\uiskin.json"));
        //setting the text area
        textArea = new TextArea(text, skin);
    }

    public Widget getComponent(){
        return textArea;
    }
    //fixme
/*    public void setBorder(SpriteBatch batch){
        Rectangle size = new Rectangle(textArea.getX()-10, textArea.getY()-10,
                textArea.getWidth()+20, textArea.getHeight()+20);
        Texture texture1 = new Texture("C:\\Users\\ak2000\\Documents\\Mine" +
                "\\MBRemastered\\MBRemastered\\MBRemastered\\core\\pics\\GenstatsPanel.png");
        batch.begin();
        batch.draw(texture1, size.x, size.y, size.width, size.height);

    }*/
}

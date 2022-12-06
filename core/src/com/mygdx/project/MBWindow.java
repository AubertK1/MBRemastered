package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Window which features the close button in top right corner (button moved outside of the window bounds).
 *
 * @author serhiy
 */
public class MBWindow extends MBComponent{
    Window window;
    Image image;
    float inWidth;
    float inHeight;
    float gap;
    /**
     * Default constructor.
     */
    public MBWindow(Texture texture, Skin skin, MBComponent parent) {
        parentActor = parent;
        parentActor.hasWindow = true;

        window = new Window("", skin);
        image = new Image(texture);

        inHeight = 700;
        inWidth = getScaledWidth(image.getHeight(), image.getWidth(), inHeight);
        image.setWidth(inWidth);
        image.setHeight(inHeight);
        window.add(image);

        gap = window.getTitleTable().getPrefHeight();
        window.setClip(false);
        window.setTransform(true);
        window.setResizable(true);
        window.setKeepWithinStage(true);
        final Button closeButton = new ImageButton(skin, "delete-button");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parentActor.hasWindow = false;
                ((MBComponent)getMBParent()).delete(MBWindow.this);
            }
        });
        final Button resizeButton = new ImageButton(skin, "edit-button");
        resizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.setWidth(inWidth);
                window.setHeight(inHeight);
                window.setPosition((float) Gdx.graphics.getWidth()/2 - window.getWidth()/2, (float)Gdx.graphics.getHeight()/2 - window.getHeight()/2);
            }
        });
        final Button reaspectButton = new ImageButton(skin, "edit-button");
        reaspectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.setHeight(image.getHeight()+gap);
                window.setWidth(getScaledWidth(inHeight, inWidth, image.getHeight()));
            }
        });
        window.getTitleTable().add(reaspectButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
        window.getTitleTable().add(resizeButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
        window.getTitleTable().add(closeButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
    }

    public Actor getComponent(){
        return window;
    }
    public Actor getImage(){
        return image;
    }
    private float getScaledWidth(float height, float width, float scaledHeight){
        double scale = (double) scaledHeight / (double) height;
        float scaledWidth = (float)(width * scale);
        return scaledWidth;
    }
}

package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Essentially making my own version of JComponents where I can group the different components
 * together and edit any methods I need
 */
public class MBComponent extends Actor {
    Rectangle position;
    Texture texture;
    public MBComponent() {

    }

    /**
     * gets the component regardless of what type of component it is (textfield, label, etc.)
     * @return returns the component
     */
    public Widget getComponent(){
        return null;
    }
}

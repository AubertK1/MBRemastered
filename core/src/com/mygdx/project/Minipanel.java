package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * I only want there to be a certain amount of panels, so minipanels were created to act as
 * smaller panels that go inside the main panels.
 * This class currently has no other use than to be a label for the non-main panels
 */
public class Minipanel extends Panel{
    public Minipanel(String fileLocation, Rectangle position) {
        super(fileLocation, position);
    }
}

package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.ArrayList;

/**
 * I only want there to be a certain amount of panels, so minipanels were created to act as
 * smaller panels that go inside the main panels. Components set inside a minipanel are also
 * stored inside the panel that the minipanel belongs to.
 */
public class Minipanel extends Panel{
    public Minipanel(String fileLocation, Rectangle position) {
        super(fileLocation, position);
    }
}

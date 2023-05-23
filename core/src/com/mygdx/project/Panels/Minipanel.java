package com.mygdx.project.Panels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.project.Screen;

/**
 * I only want there to be a certain amount of panels, so minipanels were created to act as
 * smaller panels that go inside the main panels.
 * This class currently has no other use than to be a label for the non-main panels
 */
public class Minipanel extends Panel{
    public Minipanel(String fileLocation, Rectangle position, Screen screen) {
        super(fileLocation, position, screen);
    }
}

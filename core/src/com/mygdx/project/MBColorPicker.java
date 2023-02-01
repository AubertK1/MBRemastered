package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class MBColorPicker extends MBComponent{
    ColorPicker CP;

    public MBColorPicker(Screen screen) {
        super(screen);
        CP = new ColorPicker();
    }

    public Actor getActor(){
        return CP;
    }
}

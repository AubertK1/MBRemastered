package com.mygdx.project.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.project.ColorPicker;
import com.mygdx.project.Screen;

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

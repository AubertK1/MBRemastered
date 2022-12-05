package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class MBBoard extends MBComponent{
    Widget board;

    public MBBoard(){
        board = new Widget();
    }

    public Actor getComponent(){
        return board;
    }
}

package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

public class MBBoard extends MBComponent{
    Board board;

    public MBBoard(){
        board = new Board(skin);
    }

    public void setPosition(float x, float y){
        getComponent().setPosition(x, y);
        board.setOffsetX(x);
        board.setOffsetY(y);
    }

    public Actor getComponent(){
        return board;
    }

}

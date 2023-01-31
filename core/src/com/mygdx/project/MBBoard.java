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

    @Override
    public void setScreen(Screen screen){
        super.setScreen(screen);
        board.setScreen(screen);
    }
    public Actor getActor(){
        return board;
    }

    public void dispose(){
        board.dispose();
    }
}

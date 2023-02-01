package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

public class MBBoard extends MBComponent{
    Board board;

    public MBBoard(Screen screen){
        super(screen);
        board = new Board(skin);
        board.setScreen(screen);
    }

    @Override
    public void setScreen(Screen screen){
        super.setScreen(screen);
    }
    public Board getBoard(){
        return board;
    }
    public Actor getActor(){
        return board;
    }

    public void dispose(){
        board.dispose();
    }
}

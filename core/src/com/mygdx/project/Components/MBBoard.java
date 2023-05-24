package com.mygdx.project.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.project.Actors.Board;
import com.mygdx.project.Screen;

public class MBBoard extends MBComponent{
    private Board board;

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

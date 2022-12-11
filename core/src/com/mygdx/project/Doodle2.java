package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.*;
import java.util.ArrayList;

public class Doodle2 extends Widget {
    private InputListener inputListener;
    private ClickListener clickListener;
    private final Doodle doodle;

    public Doodle2(Doodle doodle) {
        this.doodle = doodle;
        initialize();
        Main.doodle2s.add(this);
    }
    protected void initialize() {
        setTouchable(Touchable.enabled);
        setPosition(findBounds().x, findBounds().y);
        setSize(findBounds().width, findBounds().height);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.print("touched me!");
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
            }
        });
    }
    public void reinitialize(){
        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
    }
    public Rectangle findBounds(){
        int mostLeft = 2000;
        int mostRight = 0;
        int mostLow = 0;
        int mostHigh = 1000;

        for (Point point: doodle.drawnPoints) {
            if(point.x == -1 || point.y == -1) continue;
            if(point.x < mostLeft) mostLeft = point.x;
            if(point.x > mostRight) mostRight = point.x;
            if(point.y > mostLow) mostLow = point.y;
            if(point.y < mostHigh) mostHigh = point.y;
        }

        return new Rectangle(mostLeft, mostLow, mostRight-mostLeft, (mostHigh-mostLow)*-1);
    }
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

public class Board extends Widget {
    BoardStyle style;
    private InputListener inputListener;
    private float offsetX = 0, offsetY = 0;

    public float visualX = 0, visualY = 0;
    Pixmap cursor;


    public Board (Skin skin) {
        this(skin.get(BoardStyle.class));
    }

    public Board (Skin skin, String styleName) {
        this(skin.get(styleName, BoardStyle.class));
    }

    public Board(BoardStyle style) {
        initialize();
        setStyle(style);
        setSize(getPrefWidth(), getPrefHeight());

        cursor = new Pixmap(Gdx.files.internal("assets\\cursor.png"));

    }
    protected void initialize () {
        setTouchable(Touchable.enabled);
        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                visualX = offsetX + x;
                visualY = offsetY + y;
                System.out.println("X: "+visualX+"; Y: "+visualY);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                System.out.println("X: "+x+"; Y: "+y);
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
//                System.out.println("Over Board");
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor,0,0));
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                System.out.println("Left Board");
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });
    }

    public void draw (Batch batch, float parentAlpha) {
        validate();

        final Drawable background = getBackgroundDrawable();

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (background != null) {
            background.draw(batch, x, y, width, height);
        }

    }

        public void setStyle (BoardStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }
    protected @Null Drawable getBackgroundDrawable () {
        return style.background;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public BoardStyle getStyle () {
        return style;
    }


    static public class BoardStyle{
        public @Null
        Drawable background;
        public BoardStyle(){
        }

        public BoardStyle(BoardStyle style){
            background = style.background;
        }
    }
}

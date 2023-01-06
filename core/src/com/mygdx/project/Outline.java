package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;

public class Outline extends Widget {
    private InputListener inputListener;
    private ClickListener clickListener;
    private Doodle doodle;
    OutlineStyle style;
    private float offsetX = 0, offsetY = 0;

    private float boardHeight;
    private float boardWidth;


    public Outline (Doodle doodle, Skin skin) {
        this(doodle, skin.get(OutlineStyle.class));
    }

    public Outline(Doodle doodle, OutlineStyle style) {
        this.doodle = doodle;
        initialize();
        setStyle(style);

        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);

        Main.outlines.add(this);
    }
    protected void initialize() {
        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.print("touched me!");
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                System.out.print("touched me!");

            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                if(clickListener.isOver()){
                    System.out.println("OVER");
                }
                x=x;
                y=y;
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
            }
        });
    }
    public void update(){
        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
//        setPosition(1000, 200);
        setSize(rec.width, rec.height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(doodle.drawnPoints.size() == 0) return;
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

        Rectangle rec = new Rectangle((int)offsetX + mostLeft, ((int) boardHeight - mostLow)+(int)offsetY, mostRight-mostLeft, (mostHigh-mostLow)*-1);
        return rec;
    }
    protected @Null Drawable getBackgroundDrawable () {
        return style.background;
    }

    public void setStyle (OutlineStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    public void setDoodle(Doodle doodle) {
        this.doodle = doodle;
    }
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public void setBoardWidth(float boardWidth) {
        this.boardWidth = boardWidth;
    }
    public void setBoardHeight(float boardHeight) {
        this.boardHeight = boardHeight;
    }

    public float getBoardHeight() {
        return boardHeight;
    }
    public float getBoardWidth() {
        return boardWidth;
    }



    static public class OutlineStyle{
        public @Null
        Drawable background;

        public OutlineStyle(){
        }

        public OutlineStyle(Board.BoardStyle style){
            background = style.background;
        }
    }
}

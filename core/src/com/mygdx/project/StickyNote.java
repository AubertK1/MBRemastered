package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;

public class StickyNote extends Outline {
    StickyNoteStyle style;
    private Board parentBoard;

    private float offsetX, offsetY;
    private float boardHeight;
    private float boardWidth;

    private Rectangle bounds = new Rectangle();

    private int LEFTBOUND = 0;
    private int RIGHTBOUND = 0;
    private int UPPERBOUND = 0;
    private int LOWERBOUND = 0;

    public StickyNote(Board board, Skin skin, Vector2 pointer) {
        this(board, skin.get(StickyNoteStyle.class), pointer);
    }

    public StickyNote(Board board, StickyNoteStyle style, Vector2 pointer) {
        super(board);
        this.style = style;

        //sets the board and transfers variables
        parentBoard = board;
        boardHeight = parentBoard.getHeight();
        boardWidth = parentBoard.getWidth();
        offsetX = parentBoard.getOffsetX();
        offsetY = parentBoard.getOffsetY();

        setStyle(style);
        setSize(200, 100);
        setPosition(pointer.x + offsetX, pointer.y-getHeight() + offsetY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //more detections for if the outline is out of bounds
        if(isOutOfBounds()) update();

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

    public void update(){
        Rectangle rec = findBounds();

        //detects if the sticky note is out of bounds
        if(isOutOfBounds()){
            //holding sticky note at border
            if(brokeLeftBounds()) setX(offsetX); //if at left border...
            if(brokeLowerBounds()) setY(offsetY); //if at bottom border...
            if(brokeRightBounds()) setX((offsetX + boardWidth) - getWidth()); //if at right border...
            if(brokeUpperBounds()) setY((offsetY + boardHeight) - getHeight()); //if at top border...


            //resets bounds after changing position
            rec = findBounds();
        }

        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
    }

    private Rectangle findBounds(){
        Rectangle rec = new Rectangle((int) getX(), (int) (boardHeight - getY()), (int) getWidth(), (int) getHeight());

        LEFTBOUND = rec.x;
        RIGHTBOUND = rec.x + rec.width;
        LOWERBOUND = rec.y;
        UPPERBOUND = rec.y + rec.height;

        return rec;
    }

    public boolean isOutOfBounds(){
        findBounds(); //updating the bound
        return (LEFTBOUND < offsetX || LOWERBOUND < offsetY || RIGHTBOUND > offsetX + boardWidth || UPPERBOUND > offsetY + boardHeight);
    }
    public boolean brokeLeftBounds(){
        findBounds(); //updating the bound
        return LEFTBOUND < offsetX;
    }
    public boolean brokeRightBounds(){
        findBounds(); //updating the bound
        return RIGHTBOUND > offsetX + boardWidth;
    }
    public boolean brokeLowerBounds(){
        findBounds(); //updating the bound
        return LOWERBOUND < offsetY;
    }
    public boolean brokeUpperBounds(){
        findBounds(); //updating the bound
        return UPPERBOUND > offsetY + boardHeight;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void setStyle (StickyNoteStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    protected @Null Drawable getBackgroundDrawable () {
        return style.background;
    }

    static public class StickyNoteStyle{
        public @Null
        Drawable background;

        public StickyNoteStyle(){
        }

        public StickyNoteStyle(Board.BoardStyle style){
            background = style.background;
        }
    }
}

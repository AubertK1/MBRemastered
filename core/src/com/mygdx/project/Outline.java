package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;

public class Outline extends Widget {
    protected Board parentBoard;

    protected Rectangle bounds = new Rectangle();

    protected float offsetX, offsetY;
    protected float boardHeight;
    protected float boardWidth;

    protected int LEFTBOUND = 0;
    protected int RIGHTBOUND = 0;
    protected int UPPERBOUND = 0;
    protected int LOWERBOUND = 0;

    public Outline(Board board) {
        //sets the board and transfers variables
        parentBoard = board;
        boardHeight = parentBoard.getHeight();
        boardWidth = parentBoard.getWidth();
        offsetX = parentBoard.getOffsetX();
        offsetY = parentBoard.getOffsetY();

        initialize();

        //setting bounds
        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
    }
    protected void initialize() {
    }

    public void update(){
/*
        Rectangle rec = findBounds();

        //detects if the outline is out of bounds
        if(isOutOfBounds()){
            ArrayList<Point> deltaPoints = new ArrayList<>();
            for (Point point: getDoodleMap().getPoints()) {
                float newY = getDoodleMap().getHeight() - point.y;
                deltaPoints.add(new Point((int) ((offsetX+point.x) - getX()), (int) ((offsetY+newY) - getY())));
            }

            //holding outline at border
            if(brokeLeftBounds()) setX(offsetX); //if at left border...
            if(brokeLowerBounds()) setY(offsetY); //if at bottom border...
            if(brokeRightBounds()) setX((offsetX + boardWidth) - getWidth()); //if at right border...
            if(brokeUpperBounds()) setY((offsetY + boardHeight) - getHeight()); //if at top border...

            //moving doodle points
            int i = 0;
            for (Point point: getDoodleMap().getPoints()) {
                if(point.x == -1 && point.y == -1){
                    i++;
                    continue;
                }

                point.x = (int) (getX()+deltaPoints.get(i).x - offsetX);
                int revertY = (int) (getY()+deltaPoints.get(i).y - offsetY);
                point.y = getDoodleMap().getHeight() - revertY;
                i++;
            }

            //resets bounds after changing position
            rec = findBounds();
        }

        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
*/
    }

    public void draw(Batch batch, float parentAlpha) {
/*
        if(doodleMap.drawnPoints.size() == 0) return;
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
*/
    }

    private Rectangle findBounds(){
/*
        if(doodleMap.drawnPoints.size() == 0) return new Rectangle(-1, -1,0,0);

        int mostLeft = 2000;
        int mostRight = 0;
        int mostLow = 0;
        int mostHigh = 1000;

        for (Point point: doodleMap.drawnPoints) {
            if(point.x == -1 || point.y == -1) continue;
            if(point.x < mostLeft) mostLeft = point.x;
            if(point.x > mostRight) mostRight = point.x;
            if(point.y > mostLow) mostLow = point.y;
            if(point.y < mostHigh) mostHigh = point.y;
        }

        Rectangle rec = new Rectangle((int)offsetX + mostLeft, ((int) boardHeight - mostLow)+(int)offsetY, mostRight-mostLeft, (mostHigh-mostLow)*-1);
        LEFTBOUND = rec.x;
        RIGHTBOUND = rec.x + rec.width;
        LOWERBOUND = rec.y;
        UPPERBOUND = rec.y + rec.height;

        return rec;
*/
        Rectangle rec = new Rectangle((int) getX(), (int) (boardHeight - getY()), (int) getWidth(), (int) getHeight());

        LEFTBOUND = rec.x;
        RIGHTBOUND = rec.x + rec.width;
        LOWERBOUND = rec.y;
        UPPERBOUND = rec.y + rec.height;

        return rec;
    }
/*
    protected @Null Drawable getBackgroundDrawable () {
        return style.background;
    }

    public void setStyle (OutlineStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }
*/

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

    public float getBoardHeight() {
        return boardHeight;
    }
    public float getBoardWidth() {
        return boardWidth;
    }



}

package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;

public class StickyNote extends GenOutline{
    private InputListener inputListener;
    private ClickListener clickListener;
    private StickyNoteStyle style;

    public StickyNote(Board board, Skin skin, int x, int y) {
        this(board, skin.get(StickyNoteStyle.class), x, y);
    }
    public StickyNote(Board board, StickyNoteStyle style, int x, int y) {
        super(board);

        setSize(300, 100);
        setPosition(x, y-getHeight());

        setStyle(style);

        //setting bounds
        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
    }

    public void update(){
        Rectangle rec = findBounds();

        //detects if the outline is out of bounds
        if(isOutOfBounds()){
            //holding outline at border
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        if(parentBoard.getSelectedOutline() != this || !parentBoard.isInSelectMode()) return; //keep going only if this is the selected outline and the board is in select mode

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

    protected Rectangle findBounds() {
        Rectangle rec = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        LEFTBOUND = rec.x;
        RIGHTBOUND = rec.x + rec.width;
        LOWERBOUND = rec.y;
        UPPERBOUND = rec.y + rec.height;

        return rec;
    }

    @Override
    public void fix(){
        //update the selected outline's bounds
        update();
    }

    public void drag(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;

        //so that everything moves relative to where it was
        if(lastx == -1) lastx = (int) x2;
        if(lasty == -1) lasty = (int) y2;
        float deltaX = x2-lastx;
        float deltaY = y2-lasty;

        //moving outline
        if((getX() <= offsetX && deltaX < 0)){ //testing left bounds
            setX(offsetX);
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((getX()+getWidth() >= offsetX+parentBoard.getWidth() && deltaX > 0)){ //testing right bounds
            setX(offsetX+parentBoard.getWidth() - getWidth());
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            setX(getX()+(deltaX));
        }
        if((getY() <= offsetY && deltaY < 0)){ //testing lower bounds
            setY(offsetY);
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((getY()+getHeight() >= offsetY+parentBoard.getHeight() && deltaY > 0)){ //testing upper bounds
            setY(offsetY+parentBoard.getHeight() - getHeight());
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            setY(getY()+(deltaY));
        }

        lastx = (int) x2;
        lasty = (int) y2;
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

        public StickyNoteStyle(StickyNoteStyle style){
            background = style.background;
        }
    }

}

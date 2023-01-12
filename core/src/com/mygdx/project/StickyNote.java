package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;

public class StickyNote extends GenOutline{
    private InputListener inputListener;
    private ClickListener clickListener;
    private StickyNoteStyle style;

    public StickyNote(Board board, Skin skin) {
        this(board, skin.get(StickyNoteStyle.class));
    }
    public StickyNote(Board board, StickyNoteStyle style) {
        super(board);

        setStyle(style);

        //setting bounds
        Rectangle rec = findBounds();
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

    public void setStyle (StickyNoteStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    private @Null Drawable getBackgroundDrawable () {
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

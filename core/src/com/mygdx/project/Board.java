package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.util.ArrayList;

public class Board extends Widget {
    BoardStyle style;
    private InputListener inputListener;
    private ClickListener clickListener;
    private float offsetX = 0, offsetY = 0;

    public float visualX = 0, visualY = 0;
    private Pixmap cursor;
    private Pixmap pixmap;
    private Pixmap minipixmap;

    private Color backgroundColor;
    private Color drawingColor;
    private Brush currentBrush;
    private float brushCenterX;
    private float brushCenterY;

    private int lastx = -1;
    private int lasty = -1;


    ArrayList<Doodle> doodles = new ArrayList<>();


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
        setBackgroundColor(new Color(0xd2b48cff));
        setDrawingColor(Color.BLUE);
        //setting brush/cursor
        currentBrush = new Brush(1, new float[][]{
                {.2f, 1, .2f},
                {1, 1, 1},
                {.2f, 1, .2f}
        });
        brushCenterX = (float)(currentBrush.brush.length/2)+1;
        brushCenterY = (float)(currentBrush.brush[0].length/2)+1;
        cursor = currentBrush.getPixmap();
    }
    protected void initialize () {
        minipixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        minipixmap.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                visualX = offsetX + x + brushCenterX;
                visualY = offsetY + y - brushCenterY;
                System.out.println("X: "+visualX+"; Y: "+visualY);
                drawAt((int)visualX,(int)visualY);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                visualX = offsetX + x;
                visualY = offsetY + y;
                System.out.println("X: "+visualX+"; Y: "+visualY);
                drawAt((int)visualX,(int)visualY);

            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                if(clickListener.isOver()) {
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                }
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!clickListener.isOver()) {
                    System.out.println("Left Board");
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
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

    public void drawAt(int x, int y) {
        int brushSize = currentBrush.getSize();
        float[][] brush = currentBrush.getBrush();
        Color color;

        color = drawingColor;

        // This might look redundant, but should be more efficient because
        // the condition is not evaluated for each pixel on the brush
        if (lastx != -1 && lasty != -1) {
            for (int i = -brushSize; i < brushSize+1; i++) {
                for (int j = -brushSize; j < brushSize+1; j++) {
                    if (brush[brushSize-j][brushSize+i] != 0) {
                        minipixmap.setColor(color.r, color.g, color.b, brush[brushSize-j][brushSize+i]);
                        minipixmap.drawLine(lastx+i, lasty+j, x+i, y+j);
                    }
                }
            }
        } else {
            for (int i = -brushSize; i < brushSize+1; i++) {
                for (int j = -brushSize; j < brushSize+1; j++) {
                    if (brush[brushSize-j][brushSize+i] != 0) {
                        minipixmap.setColor(color.r, color.g, color.b, brush[brushSize-j][brushSize+i]);
                        minipixmap.drawPixel(x+i, y+j);
                    }
                }
            }
        }
        pixmap.drawPixmap(minipixmap, 0, 0, 256, 256, 0, 0, 256, 256);
        lastx = x;
        lasty = y;
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

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        Pixmap bgColor = new Pixmap(1, 1, Pixmap.Format.RGB888);
        bgColor.setColor(color);
        bgColor.fill();
        BoardStyle tempStyle = new BoardStyle(style);
        tempStyle.background = new Image(new Texture(bgColor)).getDrawable();
        setStyle(tempStyle);
        bgColor.dispose();
    }

    public void setDrawingColor(Color color) {
        drawingColor = color;
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

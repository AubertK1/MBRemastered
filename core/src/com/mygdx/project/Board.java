package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    private Doodle doodle;
    private Doodle doodlePixel;
    private Texture doodleTex;

    private Color backgroundColor;
    private Color drawingColor;
    private Color currentColor;
    private Brush currentBrush;
    private float brushCenterX;
    private float brushCenterY;
    private boolean brushSoft = true;

    private boolean selectMode = true;
    private boolean drawMode = false;
    private boolean eraseMode = false;

    private int lastx = -1;
    private int lasty = -1;

    boolean drawCursor;

    ArrayList<Doodle> doodles = new ArrayList<>();
    ArrayList<Texture> doodleTexs = new ArrayList<>();


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
        setBackgroundColor(backgroundColor);
        setDrawingColor(Color.BLACK);
        setCurrentColor(drawingColor);

        doodles.add(doodle);
        doodleTexs.add(doodleTex);

        doodleTex = new Texture(getDoodle());
        doodleTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        doodleTex.bind();

        //setting brush/cursor
/*
        currentBrush = new Brush(2, new float[][]{
                {0.0f,0.5f,0.5f,0.5f,0.0f},
                {0.5f,0.8f,1.0f,0.8f,0.5f},
                {0.5f,1.0f,1.0f,1.0f,0.5f},
                {0.5f,0.8f,1.0f,0.8f,0.5f},
                {0.0f,0.5f,0.5f,0.5f,0.0f}
        });
*/
        currentBrush = Brush.generateBrush(23, brushSoft);
        brushCenterX = (float)(currentBrush.size+1);
        brushCenterY = (float)(currentBrush.size+1);
        cursor = currentBrush.getPixmap();
    }
    protected void initialize () {
        backgroundColor = new Color(0xd2b48cff);

        doodlePixel = new Doodle(1018, 850, Pixmap.Format.RGBA8888);
        doodle = new Doodle(1018, 850, Pixmap.Format.RGBA8888);
        doodlePixel.setFilter(Pixmap.Filter.NearestNeighbour);
        doodle.setFilter(Pixmap.Filter.NearestNeighbour);
        doodle.setColor(new Color(0f,0f,0f,0f));
        doodle.fill();

        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                visualX = offsetX + x + brushCenterX;
                visualY = offsetY + y - brushCenterY;
                System.out.println("X: "+visualX+"; Y: "+visualY);
                drawAt((int)x,(int)y);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
                lastx = -1;
                lasty = -1;
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                visualX = offsetX + x;
                visualY = offsetY + y;
                System.out.println("X: "+visualX+"; Y: "+visualY);
                drawAt((int)x,(int)y);

                if(!drawCursor && !selectMode){
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                    drawCursor = true;
                }
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                if(clickListener.isOver() && !selectMode) {
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                    drawCursor = true;
                }
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!clickListener.isOver()) {
                    System.out.println("Left Board");
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    drawCursor = false;
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
        if(drawMode) {
            int brushSize = currentBrush.getSize();
            float[][] brush = currentBrush.getBrush();
            Color color;

            //flipping the y so that the coordinates aren't upside down for the pixmap
            int y2 = (doodle.getHeight() - y) + (int) brushCenterY;
            int x2 = x + (int) brushCenterX;
            color = currentColor;

            // This might look redundant, but should be more efficient because
            // the condition is not evaluated for each pixel on the brush
            if (lastx != -1 && lasty != -1) {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            doodlePixel.setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i] * .4f);
                            doodlePixel.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                            doodle.storeLine(true, lastx + i, lasty + j, x2 + i, y2 + j);
                        }
                    }
                }
            } else {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            doodlePixel.setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i]);
                            doodlePixel.drawPixel(x2 + i, y2 + j);
                            doodle.storeLine(true, lastx + i, lasty + j, -1, -1);
                        }
                    }
                }
            }
            //so that it doesn't draw old points again
            doodle.setColor(Color.CLEAR);
            doodle.fill();
            doodle.drawPixmap(doodlePixel, 0, 0, 1018, 850, 0, 0, 1018, 850);
            lastx = x2;
            lasty = y2;

            doodleTex.dispose();
            doodleTex = new Texture(getDoodle());
        }
        else if(eraseMode){
            int brushSize = currentBrush.getSize();
            float[][] brush = currentBrush.getBrush();

            //flipping the y so that the coordinates aren't upside down for the pixmap
            int y2 = (doodle.getHeight() - y) + (int) brushCenterY;
            int x2 = x + (int) brushCenterX;

            doodle.setBlending(Pixmap.Blending.None); // before you start drawing pixels.
            doodlePixel.setBlending(Pixmap.Blending.None); // before you start drawing pixels.

            // This might look redundant, but should be more efficient because
            // the condition is not evaluated for each pixel on the brush
            if (lastx != -1 && lasty != -1) {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            doodlePixel.setColor(0x00000000);
                            doodlePixel.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                            doodle.storeLine(false, lastx + i, lasty + j, x2 + i, y2 + j);
                        }
                    }
                }
            } else {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            doodlePixel.setColor(0x00000000);
                            doodlePixel.drawPixel(x2 + i, y2 + j, 0x00000000);
                            doodle.storeLine(false, lastx + i, lasty + j, -1, -1);
                        }
                    }
                }
            }
            doodle.drawPixmap(doodlePixel, 0, 0, 1018, 850, 0, 0, 1018, 850);
            lastx = x2;
            lasty = y2;

            doodleTex.dispose();
            doodleTex = new Texture(getDoodle());

            doodle.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
            doodlePixel.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending

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

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        //sets board background to the color
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
    public void setCurrentColor(Color color) {
        currentColor = color;
    }
    public void setBrush(int width, boolean soft){
        currentBrush = Brush.generateBrush(width, soft);
        brushCenterX = (float)(currentBrush.brush.length/2)+0;
        brushCenterY = (float)(currentBrush.brush[0].length/2)+0;
        cursor = currentBrush.getPixmap();
    }

    public void setBrushSoft(boolean isSoft) {
        brushSoft = isSoft;

        setBrush(currentBrush.getWidth(), isSoft);
    }

    public boolean isBrushSoft() {
        return brushSoft;
    }

    public BoardStyle getStyle () {
        return style;
    }

    public void enterDrawMode() {
        drawMode = true;
        selectMode = false;
        eraseMode = false;

        setCurrentColor(drawingColor);
    }
    public void enterSelectMode() {
        drawMode = false;
        selectMode = true;
        eraseMode = false;
    }
    public void enterEraseMode() {
        drawMode = false;
        selectMode = false;
        eraseMode = true;
    }
    public boolean getSelectMode() {
        return selectMode;
    }
    public boolean getDrawMode() {
        return drawMode;
    }
    public boolean getEraseMode() {
        return eraseMode;
    }

    public Pixmap getDoodle() {
        return doodle;
    }
    public Texture getDoodleTex() {
        return doodleTex;
    }
    public Brush getCurrentBrush(){
        return currentBrush;
    }
    public ArrayList<Texture> getDoodleTexs() {
        return doodleTexs;
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

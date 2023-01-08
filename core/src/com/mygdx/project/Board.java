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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;

public class Board extends Widget {
    BoardStyle style;
    private InputListener inputListener;
    private ClickListener clickListener;
    private float offsetX = 0, offsetY = 0;

    public float visualX = 0, visualY = 0;
    private Pixmap cursor;
    private Pixmap pixmapBoard;
    private Doodle doodle;
    private Texture doodleTex;

    public Outline outline;
    private Outline selectedOutline;

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
    ArrayList<Outline> outlines = new ArrayList<>();
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
        outlines.add(outline);

        doodleTex = new Texture(getDoodle());
        doodleTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        doodleTex.bind();

        //setting brush/cursor
        currentBrush = Brush.generateBrush(11, brushSoft);
        brushCenterX = (float)(currentBrush.size+1);
        brushCenterY = (float)(currentBrush.size+1);
        cursor = currentBrush.getPixmap();
    }
    protected void initialize () {
        backgroundColor = new Color(0xd2b48cff);

        doodle = new Doodle(1018, 850, Pixmap.Format.RGBA8888, outline);
        doodle.setFilter(Pixmap.Filter.NearestNeighbour);
        doodle.setColor(new Color(0f,0f,0f,0f));
        doodle.fill();
        pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
        pixmapBoard.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmapBoard.setColor(new Color(0f,0f,0f,0f));
        pixmapBoard.fill();

        outline = new Outline(doodle, Main.uiSkin);
        doodle.setOutline(outline);

        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                visualX = offsetX + x + brushCenterX;
                visualY = offsetY + y - brushCenterY;
                if(selectMode) findOutline((int) x, (int) y);
                else drawAt((int)x,(int)y);
                System.out.println(doodle.drawnPoints.size());
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
                lastx = -1;
                lasty = -1;

                if(!selectMode) {
                    //so that the program doesn't slow down the more that you draw
                    //(I think this works because it stops storing the same information over and over again in its memory)
                    //called here so that it's only called when drawing but isn't called too often
//                doodleDot.dispose();
//                doodleDot = new Doodle(1018, 850, Pixmap.Format.RGBA8888, null);
//                    ArrayList<Point> points = doodleDot.drawnPoints;
                    pixmapBoard.dispose();
                    pixmapBoard = new Doodle(1018, 850, Pixmap.Format.RGBA8888, null);
//                    doodleDot.drawnPoints = points;
                    //fixme
                    doodle.getOutline().update();
                }
                outline.update();
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                visualX = offsetX + x;
                visualY = offsetY + y;
//                System.out.println("X: "+visualX+"; Y: "+visualY);
                if(selectMode) dragOutline(selectedOutline, (int)x, (int)y);
                else drawAt((int)x,(int)y);

                if(clickListener.isOver() && !selectMode && !drawCursor){
                    cursor = currentBrush.getPixmap();
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                    cursor.dispose();
                    drawCursor = true;
                }
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                if(clickListener.isOver() && !selectMode && !drawCursor) {
                    cursor = currentBrush.getPixmap();
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                    cursor.dispose();
                    drawCursor = true;
                }
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!clickListener.isOver()) {
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
        //fixme
//        doodle.outline.draw(batch, 1);
        batch.draw(getDoodleTex(), offsetX, offsetY);
        outline.draw(batch, 1);
    }

    public void drawAt(int x, int y) {
        if(drawMode) {
            int brushSize = currentBrush.getSize();
            float[][] brush = currentBrush.getBrush();
            Color color;

            //flipping the y so that the coordinates aren't upside down for the pixmap
            int y2 = (pixmapBoard.getHeight() - y) + (int) brushCenterY;
            int x2 = x + (int) brushCenterX;
            color = currentColor;

            // This might look redundant, but should be more efficient because
            // the condition is not evaluated for each pixel on the brush
            if (lastx != -1 && lasty != -1) {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            //making the line lighter
                            doodle.setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i] * .4f);
                            doodle.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                            doodle.storePoints(true, lastx + i, lasty + j, x2 + i, y2 + j);
                        }
                    }
                }
            } else {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.2) {
                            //making the dot darker
                            float a = brush[brushSize - j][brushSize + i] * 1.3f;
                            if (a > 1) a = 1;
                            doodle.setColor(color.r, color.g, color.b, a);
                            doodle.drawPixel(x2 + i, y2 + j);
                            doodle.storePoints(true, x2 + i, y2 + j, -1, -1);
                        }
                    }
                }
            }
            //so that it doesn't draw old points again
            pixmapBoard.setColor(Color.CLEAR);
            pixmapBoard.fill();
            pixmapBoard.drawPixmap(doodle, 0, 0, 1018, 850, 0, 0, 1018, 850);

            lastx = x2;
            lasty = y2;

            doodleTex.dispose();
            doodleTex = new Texture(getDoodle());
        }
        else if(eraseMode){
            int brushSize = currentBrush.getSize();
            float[][] brush = currentBrush.getBrush();

            //flipping the y so that the coordinates aren't upside down for the pixmap
            int y2 = (pixmapBoard.getHeight() - y) + (int) brushCenterY;
            int x2 = x + (int) brushCenterX;

            pixmapBoard.setBlending(Pixmap.Blending.None); // before you start drawing pixels.
            doodle.setBlending(Pixmap.Blending.None); // before you start drawing pixels.

            // This might look redundant, but should be more efficient because
            // the condition is not evaluated for each pixel on the brush
            if (lastx != -1 && lasty != -1) {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        doodle.setColor(0x00000000);
                        doodle.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                        doodle.storePoints(false, lastx + i, lasty + j, x2 + i, y2 + j);
                    }
                }
            } else {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        doodle.setColor(0x00000000);
                        doodle.drawPixel(x2 + i, y2 + j, 0x00000000);
                        doodle.storePoints(false, x2 + i, y2 + j, -1, -1);
                    }
                }
            }
            pixmapBoard.drawPixmap(doodle, 0, 0, 1018, 850, 0, 0, 1018, 850);
            lastx = x2;
            lasty = y2;

            doodleTex.dispose();
            doodleTex = new Texture(getDoodle());

            pixmapBoard.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
            doodle.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
        }
        else if(selectMode){
            int brushSize = currentBrush.getSize();
            float[][] brush = currentBrush.getBrush();
            Color color;

            //flipping the y so that the coordinates aren't upside down for the pixmap
            int y2 = (pixmapBoard.getHeight() - y) + (int) brushCenterY;
            int x2 = x + (int) brushCenterX;
            color = currentColor;

            // This might look redundant, but should be more efficient because
            // the condition is not evaluated for each pixel on the brush
            if (lastx != -1 && lasty != -1) {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            //making the line lighter
                            doodle.setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i] * .4f);
                            doodle.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
//                            doodle.storeTempPoints(true, lastx + i, lasty + j, x2 + i, y2 + j);
                        }
                    }
                }
            } else {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        if (brush[brushSize - j][brushSize + i] > 0.2) {
                            //making the dot darker
                            float a = brush[brushSize - j][brushSize + i] * 1.3f;
                            if (a > 1) a = 1;
                            doodle.setColor(color.r, color.g, color.b, a);
                            doodle.drawPixel(x2 + i, y2 + j);
//                            doodle.storeTempPoints(true, x2 + i, y2 + j, -1, -1);
                        }
                    }
                }
            }
            //so that it doesn't draw old points again
            pixmapBoard.setColor(Color.CLEAR);
            pixmapBoard.fill();
            pixmapBoard.drawPixmap(doodle, 0, 0, 1018, 850, 0, 0, 1018, 850);

            lastx = x2;
            lasty = y2;

            doodleTex.dispose();
            doodleTex = new Texture(getDoodle());
        }
    }
    public boolean findOutline(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        for (Outline outline: outlines) {
            if(outline.getBounds().contains(x2, y2)){
                System.out.print("HERE :D");
                selectedOutline = outline;
                return true;
            }
        }
        selectedOutline = null;
        return false;
    }
    public void dragOutline(Outline outline, int x, int y){
        if(selectedOutline == null) return;
        float x2 = x + offsetX;
        float y2 = y + offsetY;

        //so that everything moves relative to where it was
        if(lastx == -1) lastx = (int) x2;
        if(lasty == -1) lasty = (int) y2;
        float deltaX = x2-lastx;
        float deltaY = y2-lasty;


        //moving outline
        if(!(outline.getX() <= offsetX && deltaX < 0)) outline.setX(outline.getX()+(deltaX)); //move outline x if in bounds
        else{
            outline.setX(offsetX);
            deltaX = 0; //if out of bounds, set so that the points can't move
        }
        if(!(outline.getY() <= offsetY && deltaY < 0)) outline.setY(outline.getY()+(deltaY)); //move outline y if in bounds
        else{
            outline.setY(offsetY);
            deltaY = 0; //if out of bounds, set so that the points can't move
        }

        //moving doodle points
        for (Point point: outline.getDoodle().getPoints()) {
            if(point.x == -1 && point.y == -1) continue;
            point.x += deltaX;
            point.y -= deltaY;
        }

        lastx = (int) x2;
        lasty = (int) y2;
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
        outline.setOffsetX(offsetX);
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        outline.setOffsetY(offsetY);
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

    @Override
    public void setSize(float width, float height){
        super.setSize(width, height);
        outline.setBoardHeight(height);
        outline.setBoardWidth(width);
    }

    //fixme
    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        setOffsetX(x);
        setOffsetY(y);
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

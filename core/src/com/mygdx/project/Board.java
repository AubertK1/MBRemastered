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
    //this only draws the doodles
    private Pixmap pixmapBoard;
    private Doodle doodle;
    private Texture doodleTex;
    private Vector2 doodleTexOffset = new Vector2(0 ,0);
    private Vector2 doodleTexOffset2 = new Vector2(0 ,0);

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

                if(selectMode) selectedOutline = findOutline((int) x, (int) y);
                else drawAt((int)x,(int)y);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
                lastx = -1;
                lasty = -1;

                if(selectMode){
                    if(selectedOutline == null) return;
                    //so the program doesn't keep drawing on itself
                    pixmapBoard.dispose();
                    pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);

                    //temporary pixmap with the points moved over
                    Pixmap px = shiftPixmap(doodle, (int) doodleTexOffset.x, (int) doodleTexOffset.y);
                    //clearing the doodle
                    doodle.setColor(Color.CLEAR);
                    doodle.fill();
                    //setting the doodle's pixels to the shifted pixmap's pixels
                    doodle.setPixels(px.getPixels());
                    //no more need for this pismap
                    px.dispose();

                    //so that the board doesn't draw on top of itself
                    pixmapBoard.setColor(Color.CLEAR);
                    pixmapBoard.fill();
                    pixmapBoard.drawPixmap(doodle, 0, 0, 1018, 850, 0, 0, 1018, 850);
                    //resetting the texture to the new shifted doodle so that it's realigned with the board
                    doodleTex.dispose();
                    doodleTex = new Texture(getDoodle());
                    doodleTexOffset.set(0, 0);
                }
                else{
                    //so that the program doesn't slow down the more that you draw
                    //(I think this works because it stops storing the same information over and over again in its memory)
                    //called here so that it's only called when drawing but isn't called too often
                    pixmapBoard.dispose();
                    pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
                    doodle.getOutline().update();

                    doodleTexOffset2.x = doodle.getOutline().getX() - offsetX;
                    doodleTexOffset2.y = doodle.getOutline().getY() - offsetY;
                }

                selectedOutline.update();
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                visualX = offsetX + x;
                visualY = offsetY + y;

                if(selectMode){
                    dragOutline(selectedOutline, (int)x, (int)y);

                }
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

        //if the outline is visible and out of bounds
        if(selectedOutline != null && selectedOutline.getDoodle().getPoints().size() != 0 && selectedOutline.isOutOfBounds()) {
            //checking which bound it broke
            if (selectedOutline.brokeLeftBounds()) {
                //finding how much the outline shifted by
                float deltaX = (selectedOutline.getX() - offsetX);
                //shifting the doodle texture back into place (where it's supposed to be relative to the outline) based on how much the outline got moved out of place
                doodleTexOffset.x -= deltaX;
            }
            else if(selectedOutline.brokeRightBounds()){ //repeat for the rest of the cases
                float deltaX = ((selectedOutline.getX()+ selectedOutline.getWidth()) - (offsetX+getWidth()));
                doodleTexOffset.x -= deltaX;

            }
            if(selectedOutline.brokeUpperBounds()){
                float deltaY = (selectedOutline.getY() - offsetY);
                doodleTexOffset.y -= deltaY;
            }
            else if(selectedOutline.brokeLowerBounds()){
                float deltaY = ((selectedOutline.getY()+ selectedOutline.getHeight()) - (offsetY+getHeight()));
                doodleTexOffset.y -= deltaY;
            }
        }

        batch.draw(getDoodleTex(), offsetX + doodleTexOffset.x, offsetY + doodleTexOffset.y);
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
    }

    public Outline findOutline(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        for (Outline outline: outlines) {
            if(outline.getBounds().contains(x2, y2)){
                System.out.print("HERE :D ");
                return outline;
            }
        }
        return null;
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
        if((outline.getX() <= offsetX && deltaX < 0)){ //testing left bounds
            outline.setX(offsetX);
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((outline.getX()+outline.getWidth() >= offsetX+getWidth() && deltaX > 0)){ //testing right bounds
            outline.setX(offsetX+getWidth() - outline.getWidth());
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            outline.setX(outline.getX()+(deltaX));
        }
        if((outline.getY() <= offsetY && deltaY < 0)){ //testing lower bounds
            outline.setY(offsetY);
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((outline.getY()+outline.getHeight() >= offsetY+getHeight() && deltaY > 0)){ //testing upper bounds
            outline.setY(offsetY+getHeight() - outline.getHeight());
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            outline.setY(outline.getY()+(deltaY));
        }


        //moving doodle points
        for (Point point: outline.getDoodle().getPoints()) {
            if(point.x == -1 && point.y == -1) continue;
            point.x += deltaX;
            point.y -= deltaY;
        }

        doodleTexOffset.x += deltaX;
        doodleTexOffset.y += deltaY;

        lastx = (int) x2;
        lasty = (int) y2;
    }

    public static Pixmap shiftPixmap(Pixmap src, int offsetX, int offsetY){
        final int width = src.getWidth();
        final int height = src.getHeight();
        Pixmap movedPX = new Pixmap(width, height, src.getFormat());
        final int offsetY2 = -offsetY;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(x - offsetX >= 0 && y - offsetY2 >= 0) movedPX.drawPixel(x, y, src.getPixel(x-offsetX, y-offsetY2));
                else movedPX.drawPixel(x, y);
            }
        }
        return movedPX;
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

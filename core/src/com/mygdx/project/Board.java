package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private Pixmap cursor;

    //this draws the doodles
    private Pixmap pixmapBoard;
    //displacement of texture's bottom left corner relative to board's bottom left corner
    private final Vector2 doodleTexOffset = new Vector2(0 ,0);

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

    ArrayList<Outline> outlines = new ArrayList<>();


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

        //setting brush/cursor
        currentBrush = Brush.generateBrush(11, brushSoft);
        brushCenterX = (float)(currentBrush.size+1);
        brushCenterY = (float)(currentBrush.size+1);
        cursor = currentBrush.getPixmap();
    }
    protected void initialize () {
        backgroundColor = new Color(0xd2b48cff);

        //setting up the pixmap board
        pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
        pixmapBoard.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmapBoard.setColor(new Color(0f,0f,0f,0f));
        pixmapBoard.fill();

        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //so that the computer knows which outline to drag / erase from
                if(selectMode || eraseMode) selectedOutline = findOutline((int) x, (int) y);
                else if (drawMode){
                    //if the user isn't drawing on an outline, make a new one
                    if(selectedOutline == null){
                        Outline newO = new Outline(Board.this, Main.uiSkin);
                        selectedOutline = newO;
                        outlines.add(newO);
                    }
                    drawAt((int)x,(int)y);
                }
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
                lastx = -1;
                lasty = -1;

                //goal is to lock in the position of the outline after being done dragged
                if(selectMode){
                    if(selectedOutline == null) return; //if no outline was being dragged or clicked, then return

                    //getting ready to redraw the board
                    pixmapBoard.dispose();
                    pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);

                    //temporary pixmap with the points moved over
                    Pixmap px = shiftPixmap(selectedOutline.getDoodle(), (int) doodleTexOffset.x, (int) doodleTexOffset.y);
                    //clearing the selected outline's doodle
                    selectedOutline.getDoodle().setColor(Color.CLEAR);
                    selectedOutline.getDoodle().fill();
                    //setting the doodle's pixels to the shifted pixmap's pixels
                    selectedOutline.getDoodle().setPixels(px.getPixels());
                    //no more need for this pixmap
                    px.dispose();

                    //clearing the board then drawing the updated doodle
                    pixmapBoard.setColor(Color.CLEAR);
                    pixmapBoard.fill();
                    pixmapBoard.drawPixmap(selectedOutline.getDoodle(), 0, 0, 1018, 850, 0, 0, 1018, 850);
                    //resetting the texture to the new shifted doodle so that it's realigned with the board
                    selectedOutline.getDoodle().texture.dispose();
                    selectedOutline.getDoodle().texture = new Texture(selectedOutline.getDoodle());
                    doodleTexOffset.set(0, 0);
                    //update the selected outline's bounds
                    selectedOutline.update();
                }
                //goal is to update the doodle
                else{
                    //so that the program doesn't slow down the more that you draw
                    //(I think this works because it stops storing the same information over and over again in its memory)
                    //called here so that it's only called when drawing but isn't called too often
                    pixmapBoard.dispose();
                    pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
                    if(selectedOutline != null) selectedOutline.update();
//                    selectedOutline = null;
                }
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
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

        //if in select mode and the outline is visible and out of bounds
        if(selectMode && selectedOutline != null && selectedOutline.getDoodle().getPoints().size() != 0 && selectedOutline.isOutOfBounds()) {
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
            if(selectedOutline.brokeLowerBounds()){
                float deltaY = (selectedOutline.getY() - offsetY);
                doodleTexOffset.y -= deltaY;
            }
            else if(selectedOutline.brokeUpperBounds()){
                float deltaY = ((selectedOutline.getY()+ selectedOutline.getHeight()) - (offsetY+getHeight()));
                doodleTexOffset.y -= deltaY;
            }
        }

        //loops through the outlines and draws each outline + its texture
        for (Outline outline: outlines) {
            if(outline == selectedOutline) batch.draw(outline.getDoodle().texture, offsetX + doodleTexOffset.x, offsetY + doodleTexOffset.y);
            else batch.draw(outline.getDoodle().texture, offsetX, offsetY);
            if(selectMode && outline == selectedOutline) outline.draw(batch, 1);
        }

        //fixme best version of a keylistener I could think of
        if (Gdx.input.isKeyPressed(Input.Keys.D) && selectedOutline != null) selectedOutline = null;
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
                            selectedOutline.getDoodle().setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i] * .4f);
                            selectedOutline.getDoodle().drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                            selectedOutline.getDoodle().storePoints(true, lastx + i, lasty + j, x2 + i, y2 + j);
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
                            selectedOutline.getDoodle().setColor(color.r, color.g, color.b, a);
                            selectedOutline.getDoodle().drawPixel(x2 + i, y2 + j);
                            selectedOutline.getDoodle().storePoints(true, x2 + i, y2 + j, -1, -1);
                        }
                    }
                }
            }
            //so that it doesn't draw old points again
            pixmapBoard.setColor(Color.CLEAR);
            pixmapBoard.fill();
            pixmapBoard.drawPixmap(selectedOutline.getDoodle(), 0, 0, 1018, 850, 0, 0, 1018, 850);

            lastx = x2;
            lasty = y2;

            selectedOutline.getDoodle().texture.dispose();
            selectedOutline.getDoodle().texture = new Texture(selectedOutline.getDoodle());
        }
        else if(eraseMode){
            if(selectedOutline == null) return;
            int brushSize = currentBrush.getSize();
            float[][] brush = currentBrush.getBrush();

            //flipping the y so that the coordinates aren't upside down for the pixmap
            int y2 = (pixmapBoard.getHeight() - y) + (int) brushCenterY;
            int x2 = x + (int) brushCenterX;

            pixmapBoard.setBlending(Pixmap.Blending.None); // before you start drawing pixels.
            selectedOutline.getDoodle().setBlending(Pixmap.Blending.None); // before you start drawing pixels.

            // This might look redundant, but should be more efficient because
            // the condition is not evaluated for each pixel on the brush
            if (lastx != -1 && lasty != -1) {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        selectedOutline.getDoodle().setColor(0x00000000);
                        selectedOutline.getDoodle().drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                        selectedOutline.getDoodle().storePoints(false, lastx + i, lasty + j, x2 + i, y2 + j);
                    }
                }
            } else {
                for (int i = -brushSize; i < brushSize + 1; i++) {
                    for (int j = -brushSize; j < brushSize + 1; j++) {
                        selectedOutline.getDoodle().setColor(0x00000000);
                        selectedOutline.getDoodle().drawPixel(x2 + i, y2 + j, 0x00000000);
                        selectedOutline.getDoodle().storePoints(false, x2 + i, y2 + j, -1, -1);
                    }
                }
            }
            pixmapBoard.drawPixmap(selectedOutline.getDoodle(), 0, 0, 1018, 850, 0, 0, 1018, 850);
            lastx = x2;
            lasty = y2;

            selectedOutline.getDoodle().texture.dispose();
            selectedOutline.getDoodle().texture = new Texture(selectedOutline.getDoodle());

            pixmapBoard.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
            selectedOutline.getDoodle().setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
        }
    }

    public Outline findOutline(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        for (int i = outlines.size() - 1; i > -1; i--) { //so that if two are stacked it gets the most recent one
            if(outlines.get(i).getBounds().contains(x2, y2)){
                System.out.print("HERE :D ");
                return outlines.get(i);
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
        if((selectedOutline.getX() <= offsetX && deltaX < 0)){ //testing left bounds
            selectedOutline.setX(offsetX);
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((selectedOutline.getX()+selectedOutline.getWidth() >= offsetX+getWidth() && deltaX > 0)){ //testing right bounds
            selectedOutline.setX(offsetX+getWidth() - selectedOutline.getWidth());
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            selectedOutline.setX(selectedOutline.getX()+(deltaX));
        }
        if((selectedOutline.getY() <= offsetY && deltaY < 0)){ //testing lower bounds
            selectedOutline.setY(offsetY);
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((selectedOutline.getY()+selectedOutline.getHeight() >= offsetY+getHeight() && deltaY > 0)){ //testing upper bounds
            selectedOutline.setY(offsetY+getHeight() - selectedOutline.getHeight());
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            selectedOutline.setY(selectedOutline.getY()+(deltaY));
        }


        //moving doodle points
        for (Point point: selectedOutline.getDoodle().getPoints()) {
            if(point.x == -1 && point.y == -1) continue;
            point.x += deltaX;
            point.y -= deltaY;
        }

        //moving the doodle texture
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
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
    public float getOffsetX(){
        return offsetX;
    }
    public float getOffsetY(){
        return offsetY;
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
    }

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

    public Pixmap getCurrentDoodle() {
        return selectedOutline.getDoodle();
    }
    public Brush getCurrentBrush(){
        return currentBrush;
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

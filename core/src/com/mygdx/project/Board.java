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

import java.util.ArrayList;

public class Board extends Widget {
    BoardStyle style;
    Screen screen;
    private InputListener inputListener;
    private ClickListener clickListener;
    private float offsetX = 0, offsetY = 0;

    private Pixmap cursor;

    //this draws the doodles
    public Pixmap pixmapBoard;

    private Outline selectedOutline;
    ArrayList<Outline> outlines = new ArrayList<>();

    ArrayList<Outline> focusedOutlines = new ArrayList<>();

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

    boolean drawCursor;

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
                if (selectMode || eraseMode){
                    if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) selectOutline(findOutlineBehind((int) x, (int) y)); //if trying to select the outline behind
                    //if an outline is already selected and is trying to be dragged
                    else if(selectedOutline != null && selectedOutline.getBounds().contains(x + offsetX,y + offsetY)) selectOutline(selectedOutline);
                    else if(!eraseMode) selectOutline(findOutline((int) x, (int) y));
                    //if in erase mode and no outline is clicked on, it erases the last outline that was selected

                    if(selectedOutline != null) selectedOutline.onBorder((int) x, (int) y);
                }
                if(button == Input.Buttons.LEFT) {
                        //if the user isn't drawing on an outline, make a new one
                        if ((selectedOutline == null || selectedOutline instanceof StickyNote) && drawMode) {
                            Outline newO = new Doodle(Board.this, Main.skin);
                            newO.setScreen(getScreen());
                            selectedOutline = newO;
                            outlines.add(newO);
                        }
                        if(selectedOutline != null)
                            selectedOutline.drawAt((int) x, (int) y);

                }
                else if (button == Input.Buttons.RIGHT && selectMode){
                    if (selectedOutline != null) {
                        String focus = "Focus";
                        if(selectedOutline.isFocused()) focus = "Unfocus";
                        Main.contextMenu.setItems("Bring Forward", "Bring Backward", "Bring to Front", "Bring to Back", "Edit", "Delete", focus);
                        Main.contextMenu.addListener(new ClickListener() {
                            public void clicked(InputEvent event, float x, float y) {
                                String word = Main.contextMenu.getSelected();
                                switch (word) {
                                    case "Bring Forward":
                                        selectedOutline.moveForward();
                                        break;
                                    case "Bring Backward":
                                        selectedOutline.moveBackward();
                                        break;
                                    case "Bring to Front":
                                        selectedOutline.moveToFront();
                                        break;
                                    case "Bring to Back":
                                        selectedOutline.moveToBack();
                                        break;
                                    case "Edit":
                                        enterDrawMode();
                                        break;
                                    case "Delete":
                                        selectedOutline.delete();
                                        selectedOutline = null;
                                        break;
                                    case "Focus":
                                    case "Unfocus":
                                        selectedOutline.setFocused(!selectedOutline.isFocused());
                                        break;
                                }
                            }
                        });
                    }
                    else {
                        Main.contextMenu.setItems("Create New Doodle", "Create New Sticky Note", "Create New Text Box");
                        Main.contextMenu.addListener(new ClickListener() {
                            public void clicked(InputEvent event, float x, float y) {
                                String word = Main.contextMenu.getSelected();
                                switch (word) {
                                    case "Create New Doodle":
                                        selectedOutline = null;
                                        enterDrawMode();
                                        break;
                                    case "Create New Sticky Note":
                                        Outline newS = new StickyNote(Board.this, Main.skin, (int) Main.contextMenu.getX(),
                                                (int) (Main.contextMenu.getY()+Main.contextMenu.getHeight()));
                                        newS.setScreen(getScreen());
                                        selectedOutline = newS;
                                        outlines.add(newS);
                                        break;
                                    case "Create New Text Box":
                                        Outline newT = new TextBox(Board.this, Main.skin, (int) Main.contextMenu.getX(),
                                                (int) (Main.contextMenu.getY()+Main.contextMenu.getHeight()));
                                        newT.setScreen(getScreen());
                                        selectedOutline = newT;
                                        outlines.add(newT);
                                        break;
                                }
                            }
                        });
                    }
                    Main.contextMenu.showAt((int) (x+offsetX), (int) (y+offsetY));
                    return false;
                }

                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
                Outline.wipe();

                if(selectMode){
                    if(selectedOutline != null) selectedOutline.fix();
                }
                //goal is to update the doodleMap
                else{
                    //so that the program doesn't slow down the more that you draw
                    //(I think this works because it stops storing the same information over and over again in its memory)
                    //called here so that it's only called when drawing but isn't called too often
                    if(!pixmapBoard.isDisposed()) pixmapBoard.dispose();
                    pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
                    if(selectedOutline != null) selectedOutline.update();
                }
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                if(pointer == Input.Buttons.LEFT) {
                    if (selectMode && selectedOutline!=null){
                        if(selectedOutline.isResizing()) selectedOutline.resize((int) x, (int) y);
                        else selectedOutline.drag((int) x, (int) y);
                    }
                    else if(selectedOutline != null) selectedOutline.drawAt((int) x, (int) y);

                    if (clickListener.isOver() && !selectMode && !drawCursor) {
                        cursor = currentBrush.getPixmap();
                        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                        cursor.dispose();
                        drawCursor = true;
                    }
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

        //loops through each outline and draws its texture
        for (Outline outline: outlines) {
            outline.drawContent(batch);
        }
        //loops through each outline and draws its outline last so that it's always on top
        for (Outline outline: outlines) {
            outline.drawOutline(batch, 1);
        }

        //fixme best version of a keylistener I could think of
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && selectedOutline != null) selectedOutline = null;
        if (Gdx.input.isKeyPressed(Input.Keys.FORWARD_DEL) && selectedOutline != null) selectedOutline.delete();
    }

    public Outline findOutline(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        for (int i = outlines.size() - 1; i > -1; i--) { //so that if two are stacked it gets the most recent one
            if(outlines.get(i).getBounds().contains(x2, y2)){ //if the point is inside the outline's borders...
                return outlines.get(i);
            }
        }
        return null;
    }
    public Outline findOutlineBehind(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        boolean triggered = false;
        int frontOutlineIndex = -1;
        for (int i = outlines.size() - 1; i > -1; i--) { //so that if two are stacked it gets the most recent one
            if(outlines.get(i).getBounds().contains(x2, y2)){ //if the point is inside the outline's borders...
                if(triggered) return outlines.get(i);
                //if it meets qualifies the first time activate the trigger so that it returns the next outline to qualify
                triggered = true;
                frontOutlineIndex = i;
            }
            if(i == 0 && triggered) return outlines.get(frontOutlineIndex); //if there aren't any outlines behind, return the front outline
        }
        return null;
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

    //fixme DO NOT USE UNTIL FURTHER NOTICE
    public static Pixmap rotatePixmap (Pixmap src, float angle){
        final int width = src.getWidth();
        final int height = src.getHeight();
        Pixmap rotated = new Pixmap(width, height, src.getFormat());

        final double radians = Math.toRadians(angle);
        final double cos = Math.cos(radians);
        final double sin = Math.sin(radians);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int centerX = width/2;
                final int centerY = height / 2;
                final int m = x - centerX;
                final int n = y - centerY;
                final int j = ((int) (m * cos + n * sin)) + centerX;
                final int k = ((int) (n * cos - m * sin)) + centerY;
                if (j >= 0 && j < width && k >= 0 && k < height){
                    rotated.drawPixel(x, y, src.getPixel(j, k));
                }
            }
        }
        return rotated;
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
    public void selectOutline(Outline o){
        for (Outline o2: outlines) {
            o2.setSelect(false);
        }
        selectedOutline = o;

        if(o == null) return;

        o.setSelect(true);

    }
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
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
    public boolean isInSelectMode() {
        return selectMode;
    }
    public boolean isInDrawMode() {
        return drawMode;
    }
    public boolean isInEraseMode() {
        return eraseMode;
    }

    public Outline getSelectedOutline() {
        return selectedOutline;
    }
    public Vector2 getBrushCenter(){
        return new Vector2(brushCenterX, brushCenterY);
    }
    public ArrayList<Outline> getOutlines(){
        return outlines;
    }
    public Brush getCurrentBrush(){
        return currentBrush;
    }
    public Pixmap getPixmapBoard(){
        return pixmapBoard;
    }
    public Color getCurrentColor(){
        return currentColor;
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

    public void dispose(){
        pixmapBoard.dispose();
        cursor.dispose();
        for (Outline outline: outlines) {
            outline.dispose();
        }
    }
}

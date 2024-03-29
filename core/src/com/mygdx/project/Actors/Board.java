package com.mygdx.project.Actors;

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
import com.mygdx.project.Brush;
import com.mygdx.project.Main;
import com.mygdx.project.PixSerializer;
import com.mygdx.project.Screen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Board extends Widget {
    private static int FILEIDs = 0;
    private final int FILEID = FILEIDs;
    public String folder = "board" + FILEID;
    public String pixFolder = "pboard" + FILEID;

    BoardStyle style;
    Screen screen;
    private ClickListener clickListener;
    private float offsetX = 0, offsetY = 0;

    private Pixmap cursor;

    //this draws the doodles
    protected Pixmap pixmapBoard;

    private Outline selectedOutline;
    private ArrayList<Outline> outlines = new ArrayList<>();

    private Color backgroundColor;
    private Color drawingColor;
    private Color currentColor;
    private Brush currentBrush;
    private float brushCenterX;
    private float brushCenterY;
    private boolean brushSoft = true;
    
    private BrushMode brushMode;

    boolean drawCursor;
    
    public enum BrushMode{
        DRAW_MODE, ERASE_MODE, SELECT_MODE
    }

    public Board (Skin skin) {
        this(skin.get(BoardStyle.class));
    }

    public Board (Skin skin, String styleName) {
        this(skin.get(styleName, BoardStyle.class));
    }

    public Board(BoardStyle style) {
        FILEIDs++;
        initialize();
        setStyle(style);
        setSize(getPrefWidth(), getPrefHeight());
        setBackgroundColor(backgroundColor);
        setDrawingColor(Color.BLACK);
        setCurrentColor(drawingColor);
        
        setBrushMode(BrushMode.SELECT_MODE);

        //sets brush/cursor
        currentBrush = Brush.generateBrush(11, brushSoft);
        brushCenterX = (float)(currentBrush.getSize() + 1);
        brushCenterY = (float)(currentBrush.getSize() + 1);
        cursor = currentBrush.getPixmap();
    }
    protected void initialize () {
        backgroundColor = new Color(0xd2b48cff);
        //sets up the pixmap board
        pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
        pixmapBoard.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmapBoard.setColor(new Color(0f,0f,0f,0f));
        pixmapBoard.fill();

        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //so that the computer knows which outline to drag / erase from
                if (isInSelectMode() || isInEraseMode()){
                    if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) selectOutline(findOutlineBehind((int) x, (int) y)); //if trying to select the outline behind
                    //if an outline is already selected and is trying to be dragged
                    else if(selectedOutline != null && selectedOutline.getBounds().contains(x + offsetX,y + offsetY)) selectOutline(selectedOutline);
                    else if(!isInEraseMode()) selectOutline(findOutline((int) x, (int) y));
                    //if in erase mode and no outline is clicked on, it erases the last outline that was selected

                    if(selectedOutline != null) selectedOutline.onBorder((int) x, (int) y);
                }
                if(button == Input.Buttons.LEFT) {
                        //if the user isn't drawing on an outline, make a new one
                        if ((selectedOutline == null || selectedOutline instanceof StickyNote) && isInDrawMode()) {
                            Outline newO = new Doodle(Board.this, Main.skin);
                            newO.setScreen(getScreen());
                            selectedOutline = newO;
                            outlines.add(newO);
                        }
                        if(selectedOutline != null)
                            selectedOutline.drawAt((int) x, (int) y);

                }
                else if (button == Input.Buttons.RIGHT && isInSelectMode()){
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
                                        setBrushMode(BrushMode.DRAW_MODE);
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
                                        setBrushMode(BrushMode.DRAW_MODE);
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

                if(isInSelectMode()){
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
                    if (isInSelectMode() && selectedOutline!=null){
                        if(selectedOutline.isResizing()) selectedOutline.resize((int) x, (int) y);
                        else selectedOutline.drag((int) x, (int) y);
                    }
                    else if(selectedOutline != null) selectedOutline.drawAt((int) x, (int) y);

                    if (clickListener.isOver() && !isInSelectMode() && !drawCursor) {
                        cursor = currentBrush.getPixmap();
                        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
                        cursor.dispose();
                        drawCursor = true;
                    }
                }
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                if(clickListener.isOver() && !isInSelectMode() && !drawCursor) {
                    cursor = currentBrush.getPixmap();
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
//                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, currentBrush.getSize(), currentBrush.getSize()));
                    cursor.dispose();
                    drawCursor = true;
                }
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!clickListener.isOver() && drawCursor) {
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

        batch.setColor(color.r, color.g, color.b, 1);
        if (background != null) {
            background.draw(batch, x, y, width, height);
        }

        //loops through each outline and draws its texture
        for (Outline outline: outlines) {
            outline.drawContent(batch);
        }
        //loops through each outline and draws its outline last so that it's always on top
        for (Outline outline: outlines) {
            outline.drawBorder(batch, 1);
        }

        //fixme best version of a keylistener I can think of
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && selectedOutline != null) selectedOutline = null;
        if (Gdx.input.isKeyPressed(Input.Keys.FORWARD_DEL) && selectedOutline != null) selectedOutline.delete();
    }

    public void wipe(){
        //deletes every outline then clears the list
        for (int i = 0; i < outlines.size(); i++) {
            outlines.get(i).delete();
        }
        outlines.clear();

        selectedOutline = null;

        //disposes the pixmap then re-initializes it
        if(!pixmapBoard.isDisposed()) pixmapBoard.dispose();
        pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);
        pixmapBoard.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmapBoard.setColor(new Color(0f,0f,0f,0f));
        pixmapBoard.fill();
    }

    /**
     * @param x the x coordinate to search at
     * @param y the y coordinate to search at
     * @return the outline at the coordinate
     */
    public Outline findOutline(int x, int y){
        //adjusts the coordinates so they correlate with the board
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        //loops through the outlines from front to back
        for (int i = outlines.size() - 1; i > -1; i--) {
            //if the point is inside the outline's borders...
            if(outlines.get(i).getBounds().contains(x2, y2)){
                return outlines.get(i);
            }
        }
        return null;
    }
    /**
     * @param x the x coordinate to search at
     * @param y the y coordinate to search at
     * @return the outline at the coordinate
     */
    public Outline findOutlineBehind(int x, int y){
        //adjusts the coordinates so they correlate with the board
        float x2 = x + offsetX;
        float y2 = y + offsetY;
        boolean triggered = false;
        int frontOutlineIndex = -1;
        //loops through the outlines from front to back
        for (int i = outlines.size() - 1; i > -1; i--) {
            //if the point is inside the outline's borders...
            if(outlines.get(i).getBounds().contains(x2, y2)){
                //if this is the second-to-front outline at this point, return the outline
                if(triggered) return outlines.get(i);
                //if this is the newest outline at this point, flip the boolean, so that the outline behind at this point can be returned
                triggered = true;
                frontOutlineIndex = i;
            }
            //if there aren't any other outlines at this point, return the front outline
            if(i == 0 && triggered) return outlines.get(frontOutlineIndex);
        }
        return null;
    }

    /**
     * @param src the original pixmap
     * @param offsetX how far to offset the pixmap horizontally
     * @param offsetY how far to offset the pixmap vertically
     * @return the new offset pixmap
     */
    public static Pixmap shiftPixmap(Pixmap src, int offsetX, int offsetY){
        int width = src.getWidth();
        int height = src.getHeight();
        //the pixmap that the pixels will be cloned onto
        Pixmap movedPX = new Pixmap(width, height, src.getFormat());
        //flipping the y coordinate so that the origin can be at the bottom left instead of the top left
        int offsetY2 = -offsetY;

        //looping through each x point's column
        for (int x = 0; x < width; x++) {
            //looping through each y point in the column
            for (int y = 0; y < height; y++) {
                //if the pixel coordinate will be in bounds after being offset...
                if(x - offsetX >= 0 && y - offsetY2 >= 0)
                    //draw the pixel onto the new pixmap
                    movedPX.drawPixel(x, y, src.getPixel(x-offsetX, y-offsetY2));
                //else draws a blank pixel onto the new pixmap at the point
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
        brushCenterX = (float)(currentBrush.getBrush().length/2)+0;
        brushCenterY = (float)(currentBrush.getBrush()[0].length/2)+0;
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

    public void setBrushMode(BrushMode brushMode){
        this.brushMode = brushMode;
    }
    public BrushMode getBrushMode(){
        return brushMode;
    }
    public boolean isInSelectMode() {
        return brushMode == BrushMode.SELECT_MODE;
    }
    public boolean isInDrawMode() {
        return brushMode == BrushMode.DRAW_MODE;
    }
    public boolean isInEraseMode() {
        return brushMode == BrushMode.ERASE_MODE;
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

    public void save(){
        for (Outline outline: outlines) {
            syncFolders(outline.getPS());
            outline.save();
        }
    }
    public void load(){
        //wipes all the outlines off the screen
        wipe();
        try {
            //grabs all the outline files
            File folder = Files.createDirectories(Paths.get("assets\\SaveFiles\\ovalues\\" + this.folder)).toFile();
            File[] files = folder.listFiles();

            for (File file : files) {
                Outline o = null;

                //gets the file name's identifier character
                char oType = PixSerializer.findFileIdentifier(file.getName());

                //initializes each outline type and sets the data based on the file
                switch (oType) {
                    case 'D':
                        o = new Doodle(this, Main.skin);

                        int fileID = PixSerializer.findFileID(file.getName());
                        File pFile = new File("assets\\SaveFiles\\pixvalues\\" + this.pixFolder + "\\pixmap" + fileID + ".ser");

                        o.getPS().setToFile(file, pFile);
                        break;
                    case 'S':
                        o = new StickyNote(this, Main.skin, 0, 0);

                        o.getPS().setToFile(file, null);
                        break;
                    case 'T':
                        o = new TextBox(this, Main.skin, 0, 0);

                        o.getPS().setToFile(file, null);
                        break;
                    case 'N': continue;
                }
                o.setScreen(screen);

                selectedOutline = o;

                outlines.add(o);
                syncFolders(o.getPS());

                //loads the outline
                o.load();
            }

        } catch (NullPointerException n){
            System.out.println("Folder Not Found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void syncFolders(PixSerializer ps){
        //if the PixSerializer's folder names have been initialized, set these folder names to the same thing
        if(!ps.getFolder().equals("temp")){
            folder = ps.getFolder();
            pixFolder = ps.getPixFolder();
            return;
        }
        new File("assets\\SaveFiles\\ovalues\\temp").renameTo(new File("assets\\SaveFiles\\ovalues\\" + folder));
        new File("assets\\SaveFiles\\pixvalues\\temp").renameTo(new File("assets\\SaveFiles\\pixvalues\\" + pixFolder));
        ps.setFolders(folder, pixFolder);
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

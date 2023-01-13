package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Outline extends GenOutline {
    private InputListener inputListener;
    private ClickListener clickListener;
    private Doodle doodle;
    //displacement of texture's bottom left corner relative to board's bottom left corner
    public final Vector2 doodleTexOffset = new Vector2(0 ,0);

    private OutlineStyle style;
    
    static private int lastx = -1, lasty = -1;


    public Outline (Board board, Skin skin) {
        this(board, skin.get(OutlineStyle.class));
    }

    public Outline(Board board, OutlineStyle style) {
        super(board);

        //creates a new doodle
        doodle = new Doodle(1018, 850, Pixmap.Format.RGBA8888, this);
        doodle.setFilter(Pixmap.Filter.NearestNeighbour);
        doodle.setColor(new Color(0f,0f,0f,0f));
        doodle.fill();
        //sets the doodle's texture
        doodle.texture = new Texture(getDoodle());
        doodle.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        doodle.texture.bind();

        initialize();
        setStyle(style);

        //setting bounds
        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
    }
    protected void initialize() {
        setTouchable(Touchable.enabled);

        addListener(clickListener = new ClickListener());
        addListener(inputListener = new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.print("touched me!");
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                System.out.print("touched me!");

            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                if(clickListener.isOver()){
                    System.out.println("OVER");
                }
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
            }
        });
    }
    public void update(){
        Rectangle rec = findBounds();

        //detects if the outline is out of bounds
        if(isOutOfBounds()){
            ArrayList<Point> deltaPoints = new ArrayList<>();
            for (Point point: getDoodle().getPoints()) {
                float newY = getDoodle().getHeight() - point.y;
                deltaPoints.add(new Point((int) ((offsetX+point.x) - getX()), (int) ((offsetY+newY) - getY())));
            }

            //holding outline at border
            if(brokeLeftBounds()) setX(offsetX); //if at left border...
            if(brokeLowerBounds()) setY(offsetY); //if at bottom border...
            if(brokeRightBounds()) setX((offsetX + boardWidth) - getWidth()); //if at right border...
            if(brokeUpperBounds()) setY((offsetY + boardHeight) - getHeight()); //if at top border...

            //moving doodle points
            int i = 0;
            for (Point point: getDoodle().getPoints()) {
                if(point.x == -1 && point.y == -1){
                    i++;
                    continue;
                }

                point.x = (int) (getX()+deltaPoints.get(i).x - offsetX);
                int revertY = (int) (getY()+deltaPoints.get(i).y - offsetY);
                point.y = getDoodle().getHeight() - revertY;
                i++;
            }

            //resets bounds after changing position
            rec = findBounds();
        }

        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(doodle.drawnPoints.size() == 0) return; //if there's no doodle points, do not continue

        //more detections for if the outline is out of bounds
        if(isOutOfBounds()){
            //checking which bound it broke
            if (brokeLeftBounds()) {
                //finding how much the outline shifted by
                float deltaX = (getX() - offsetX);
                //shifting the doodle texture back into place (where it's supposed to be relative to the outline) based on how much the outline got moved out of place
                doodleTexOffset.x -= deltaX;
            }
            else if(brokeRightBounds()){ //repeat for the rest of the cases
                float deltaX = ((getX()+ getWidth()) - (offsetX+parentBoard.getWidth()));
                doodleTexOffset.x -= deltaX;
            }
            if(brokeLowerBounds()){
                float deltaY = (getY() - offsetY);
                doodleTexOffset.y -= deltaY;
            }
            else if(brokeUpperBounds()){
                float deltaY = ((getY()+ getHeight()) - (offsetY+parentBoard.getHeight()));
                doodleTexOffset.y -= deltaY;
            }
            update(); //fixing the outline
        }
        //draws the doodle
        batch.draw(getDoodle().texture, offsetX + doodleTexOffset.x, offsetY + doodleTexOffset.y);
        if(parentBoard.getSelectedOutline() != this || !parentBoard.isInSelectMode()) return; //keep going only if this is the selected outline and the board is in select mode

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
    protected Rectangle findBounds(){
        if(doodle.drawnPoints.size() == 0) return new Rectangle(-1, -1,0,0);

        int mostLeft = 2000;
        int mostRight = 0;
        int mostLow = 0;
        int mostHigh = 1000;

        for (Point point: doodle.drawnPoints) {
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
    }

    @Override
    public void drawAt(int x, int y) {
        if(!(parentBoard.isInDrawMode() || parentBoard.isInEraseMode())) return;

        int brushSize = parentBoard.getCurrentBrush().getSize();
        float[][] brush = parentBoard.getCurrentBrush().getBrush();
        Color color = parentBoard.getCurrentColor();

        //flipping the y so that the coordinates aren't upside down for the pixmap
        int y2 = (parentBoard.getPixmapBoard().getHeight() - y) + (int) parentBoard.getBrushCenter().y;
        int x2 = x + (int) parentBoard.getBrushCenter().x;

        if(parentBoard.isInEraseMode()){
            parentBoard.getPixmapBoard().setBlending(Pixmap.Blending.None); // before you start drawing pixels.
            doodle.setBlending(Pixmap.Blending.None); // before you start drawing pixels.
        }

        // This might look redundant, but should be more efficient because
        // the condition is not evaluated for each pixel on the brush
        if (lastx != -1 && lasty != -1) {
            for (int i = -brushSize; i < brushSize + 1; i++) {
                for (int j = -brushSize; j < brushSize + 1; j++) {
                    if(parentBoard.isInDrawMode()) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            //making the line lighter
                            doodle.setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i] * .4f);
                            doodle.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                            doodle.storePoints(true, lastx + i, lasty + j, x2 + i, y2 + j);
                        }
                    }
                    else if(parentBoard.isInEraseMode()){
                        doodle.setColor(0x00000000);
                        doodle.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                        doodle.storePoints(false, lastx + i, lasty + j, x2 + i, y2 + j);
                    }
                }
            }
        } else {
            for (int i = -brushSize; i < brushSize + 1; i++) {
                for (int j = -brushSize; j < brushSize + 1; j++) {
                    if(parentBoard.isInDrawMode()) {
                        if (brush[brushSize - j][brushSize + i] > 0.2) {
                            //making the dot darker
                            float a = brush[brushSize - j][brushSize + i] * 1.3f;
                            if (a > 1) a = 1;
                            doodle.setColor(color.r, color.g, color.b, a);
                            doodle.drawPixel(x2 + i, y2 + j);
                            doodle.storePoints(true, x2 + i, y2 + j, -1, -1);
                        }
                    }
                    else if(parentBoard.isInEraseMode()){
                        doodle.setColor(0x00000000);
                        doodle.drawPixel(x2 + i, y2 + j, 0x00000000);
                        doodle.storePoints(false, x2 + i, y2 + j, -1, -1);
                    }
                }
            }
        }
        if(parentBoard.isInDrawMode()) {
            //so that it doesn't draw old points again
            parentBoard.getPixmapBoard().setColor(Color.CLEAR);
            parentBoard.getPixmapBoard().fill();
        }
        try {
            parentBoard.getPixmapBoard().drawPixmap(doodle, 0, 0, 1018, 850, 0, 0, 1018, 850);
        } catch (Exception e){
            System.out.println("caught");
        }

        lastx = x2;
        lasty = y2;

        doodle.texture.dispose();
        doodle.texture = new Texture(getDoodle());

        if(parentBoard.isInEraseMode()) {
            parentBoard.getPixmapBoard().setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
            doodle.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
        }
    }

    public void drag(int x, int y){
//        if(selectedOutline == null) return;
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


        //moving doodle points
        for (Point point: getDoodle().getPoints()) {
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

    /**
     * The goal is to lock in the position of the outline after being done dragged
     * so that the texture can go back to being aligned with the board
     */
    @Override
    public void lockIn() {
        Pixmap pixmapBoard = parentBoard.getPixmapBoard();
        //getting ready to redraw the board
        if(!pixmapBoard.isDisposed()) pixmapBoard.dispose();
        pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);

        //temporary pixmap with the points moved over
        Pixmap px = Board.shiftPixmap(getDoodle(), (int) doodleTexOffset.x, (int) doodleTexOffset.y);
        //clearing the selected outline's doodle
        getDoodle().setColor(Color.CLEAR);
        getDoodle().fill();
        //setting the doodle's pixels to the shifted pixmap's pixels
        getDoodle().setPixels(px.getPixels());
        //no more need for this pixmap
        px.dispose();

        //clearing the board then drawing the updated doodle
        pixmapBoard.setColor(Color.CLEAR);
        pixmapBoard.fill();
        pixmapBoard.drawPixmap(getDoodle(), 0, 0, 1018, 850, 0, 0, 1018, 850);
        //resetting the texture to the new shifted doodle so that it's realigned with the board
        getDoodle().texture.dispose();
        getDoodle().texture = new Texture(getDoodle());
        doodleTexOffset.set(0, 0);
        //update the selected outline's bounds
        update();
    }

    public void moveForward(){
        ArrayList<Outline> outlines = parentBoard.getOutlines();
        int i = outlines.indexOf(this);
        if(i+1 < outlines.size()) Collections.swap(outlines, i, ++i);
    }
    public void moveBackward(){
        ArrayList<Outline> outlines = parentBoard.getOutlines();
        int i = outlines.indexOf(this);
        if(i-1 >= 0) Collections.swap(outlines, i, --i);
    }
    public void moveToBack(){
        ArrayList<Outline> outlines = parentBoard.getOutlines();
        outlines.remove(this);
        outlines.add(0, this);
    }
    public void moveToFront(){
        ArrayList<Outline> outlines = parentBoard.getOutlines();
        outlines.remove(this);
        outlines.add(this);
    }
    public void delete(){
        ArrayList<Outline> outlines = parentBoard.getOutlines();
        getDoodle().texture.dispose();
        getDoodle().dispose();
        clear();
        outlines.remove(this);
    }

    static void wipe(){
        lastx = -1;
        lasty = -1;
    }

    private @Null Drawable getBackgroundDrawable () {
        return style.background;
    }
    public void setStyle (OutlineStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }
    public void setDoodle(Doodle doodle) {
        this.doodle = doodle;
    }
    public Doodle getDoodle(){
        return doodle;
    }
    
    static public class OutlineStyle{
        public @Null
        Drawable background;

        public OutlineStyle(){
        }

        public OutlineStyle(Board.BoardStyle style){
            background = style.background;
        }
    }
}

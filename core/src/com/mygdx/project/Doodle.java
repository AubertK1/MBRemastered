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

public class Doodle extends Outline {
    private InputListener inputListener;
    private ClickListener clickListener;
    private DoodleMap doodleMap;
    //displacement of texture's bottom left corner relative to board's bottom left corner
    private final Vector2 doodleTexOffset = new Vector2(0 ,0);

    private DoodleStyle style;


    public Doodle(Board board, Skin skin) {
        this(board, skin.get(DoodleStyle.class));
    }

    public Doodle(Board board, DoodleStyle style) {
        super(board);

        //creates a new doodle
        doodleMap = new DoodleMap(1018, 850, Pixmap.Format.RGBA8888, this);
        doodleMap.setFilter(Pixmap.Filter.NearestNeighbour);
        doodleMap.setColor(new Color(0f,0f,0f,0f));
        doodleMap.fill();
        ps.setData(doodleMap.getPixels());
        //sets the doodle's texture
        doodleMap.texture = new Texture(getDoodle());
        doodleMap.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        doodleMap.texture.bind();

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
            for (Point point: doodleMap.getPoints()) {
                float newY = doodleMap.getHeight() - point.y;
                deltaPoints.add(new Point((int) ((offsetX+point.x) - getX()), (int) ((offsetY+newY) - getY())));
            }

            //holding outline at border
            if(brokeLeftBounds()) setX(offsetX); //if at left border...
            if(brokeLowerBounds()) setY(offsetY); //if at bottom border...
            if(brokeRightBounds()) setX((offsetX + boardWidth) - getWidth()); //if at right border...
            if(brokeUpperBounds()) setY((offsetY + boardHeight) - getHeight()); //if at top border...

            //moving doodle points
            int i = 0;
            for (Point point: doodleMap.getPoints()) {
                if(point.x == -1 && point.y == -1){
                    i++;
                    continue;
                }

                point.x = (int) (getX()+deltaPoints.get(i).x - offsetX);
                int revertY = (int) (getY()+deltaPoints.get(i).y - offsetY);
                point.y = doodleMap.getHeight() - revertY;
                i++;
            }

            //resets bounds after changing position
            rec = findBounds();
        }

        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        bounds.setBounds(rec);
    }

    public void drawContent(Batch batch){
        drawable = doodleMap.drawnPoints.size() != 0; //if there's no doodle points, do not draw the outline
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
        batch.draw(doodleMap.texture, offsetX + doodleTexOffset.x, offsetY + doodleTexOffset.y);
    }

    protected Rectangle findBounds(){
        if(doodleMap.drawnPoints.size() == 0) return new Rectangle(-1, -1,0,0);

        int mostLeft = 2000;
        int mostRight = 0;
        int mostLow = 0;
        int mostHigh = 1000;

        for (Point point: doodleMap.drawnPoints) {
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
            doodleMap.setBlending(Pixmap.Blending.None); // before you start drawing pixels.
        }

        // This might look redundant, but should be more efficient because
        // the condition is not evaluated for each pixel on the brush
        if (lastx != -1 && lasty != -1) {
            for (int i = -brushSize; i < brushSize + 1; i++) {
                for (int j = -brushSize; j < brushSize + 1; j++) {
                    if(parentBoard.isInDrawMode()) {
                        if (brush[brushSize - j][brushSize + i] > 0.15) {
                            //making the line lighter
                            doodleMap.setColor(color.r, color.g, color.b, brush[brushSize - j][brushSize + i] * .4f);
                            doodleMap.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                            doodleMap.storePoints(true, lastx + i, lasty + j, x2 + i, y2 + j);
                        }
                    }
                    else if(parentBoard.isInEraseMode()){
                        doodleMap.setColor(0x00000000);
                        doodleMap.drawLine(lastx + i, lasty + j, x2 + i, y2 + j);
                        doodleMap.storePoints(false, lastx + i, lasty + j, x2 + i, y2 + j);
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
                            doodleMap.setColor(color.r, color.g, color.b, a);
                            doodleMap.drawPixel(x2 + i, y2 + j);
                            doodleMap.storePoints(true, x2 + i, y2 + j, -1, -1);
                        }
                    }
                    else if(parentBoard.isInEraseMode()){
                        doodleMap.setColor(0x00000000);
                        doodleMap.drawPixel(x2 + i, y2 + j, 0x00000000);
                        doodleMap.storePoints(false, x2 + i, y2 + j, -1, -1);
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
            parentBoard.getPixmapBoard().drawPixmap(doodleMap, 0, 0, 1018, 850, 0, 0, 1018, 850);
        } catch (Exception e){
            System.out.println("caught");
        }

        lastx = x2;
        lasty = y2;

        doodleMap.texture.dispose();
        doodleMap.texture = new Texture(getDoodle());

        if(parentBoard.isInEraseMode()) {
            parentBoard.getPixmapBoard().setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
            doodleMap.setBlending(Pixmap.Blending.SourceOver); // if you want to go back to blending
        }
    }

    public void resize(int x, int y){
        drag(x, y);
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


        //moving doodle points
        for (Point point: doodleMap.getPoints()) {
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
    public void fix() {
        //getting ready to redraw the board
        if(!parentBoard.pixmapBoard.isDisposed()) parentBoard.pixmapBoard.dispose();
        parentBoard.pixmapBoard = new Pixmap(1018, 850, Pixmap.Format.RGBA8888);

        //temporary pixmap with the points moved over
        Pixmap px = Board.shiftPixmap(doodleMap, (int) doodleTexOffset.x, (int) doodleTexOffset.y);
        //clearing the selected outline's doodle
        doodleMap.setColor(Color.CLEAR);
        doodleMap.fill();
        //setting the doodle's pixels to the shifted pixmap's pixels
        doodleMap.setPixels(px.getPixels());
        //no more need for this pixmap
        px.dispose();

        //clearing the board then drawing the updated doodle
        parentBoard.pixmapBoard.setColor(Color.CLEAR);
        parentBoard.pixmapBoard.fill();
        parentBoard.pixmapBoard.drawPixmap(doodleMap, 0, 0, 1018, 850, 0, 0, 1018, 850);
        //resetting the texture to the new shifted doodle so that it's realigned with the board
        doodleMap.texture.dispose();
        doodleMap.texture = new Texture(getDoodle());
        doodleTexOffset.set(0, 0);
        //update the selected outline's bounds
        update();
    }

    public void save(){
        ps.setData(doodleMap.getPixels());
        Point[] points = doodleMap.getPoints().toArray(new Point[0]);
        ps.setStat(PixSerializer.Stat.DMPOINTS, new Value(Value.StoreType.PLIST).setValue(points));

        ps.save();
    }

    public void load(){
        super.load();


        Pixmap px = new Pixmap(ps.getData());
        doodleMap.setPixels(px.getPixels());
        doodleMap.setPoints((Point[]) ps.getValue(PixSerializer.Stat.DMPOINTS));
        doodleMap.texture = new Texture(doodleMap);
    }

    public void delete(){
        super.delete();
        ArrayList<Outline> outlines = parentBoard.getOutlines();
        doodleMap.texture.dispose();
        doodleMap.dispose();
        clear();
        outlines.remove(this);
    }
    public void dispose(){
        doodleMap.dispose();
    }

    @Override
    protected @Null Drawable getOutlineDrawable() {
        return style.outline;
    }
    public void setStyle (DoodleStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }
    public void setDoodle(DoodleMap doodleMap) {
        this.doodleMap = doodleMap;
    }

    public DoodleMap getDoodle(){
        return doodleMap;
    }

    static public class DoodleStyle {

        public @Null
        Drawable outline;

        public DoodleStyle(){
        }
        public DoodleStyle(Board.BoardStyle style){
            outline = style.background;
        }
    }
}

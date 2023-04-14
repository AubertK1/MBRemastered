package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Outline extends Widget implements Renderable{
    protected Board parentBoard;
    protected Screen screen;
    protected PixSerializer ps = new PixSerializer();
    protected boolean selected = false;
    //the batch for the render function
    SpriteBatch batch = Main.batch;

    protected float offsetX, offsetY;
    protected float boardHeight;
    protected float boardWidth;

    private boolean focused;

    protected int BORDERSIZE = 4;
    protected int LEFTBOUND = 0;
    protected int RIGHTBOUND = 0;
    protected int UPPERBOUND = 0;
    protected int LOWERBOUND = 0;
    protected int resize = -1;

    protected Rectangle bounds = new Rectangle();
    protected boolean drawable = true;

    //controls whether this is rendered or not
    boolean supposedToBeVisible = true;
    private int layer = -1;

    static protected int lastx = -1, lasty = -1;

    public Outline(Board board) {
        //sets the board and transfers variables
        parentBoard = board;
        boardHeight = parentBoard.getHeight();
        boardWidth = parentBoard.getWidth();
        offsetX = parentBoard.getOffsetX();
        offsetY = parentBoard.getOffsetY();

        setScreen(board.getScreen());
        setLayer(0);
        screen.addRenderable(this);
    }

    public void update() {
    }

    public void drawContent(Batch batch){

    }

    public void drawOutline(Batch batch, float parentAlpha) {
        if(!drawable) return; //if there's no doodle points, do not continue
        if(parentBoard.getSelectedOutline() != this || !parentBoard.isInSelectMode()) return; //keep going only if this is the selected outline and the board is in select mode

        validate();

        final Drawable outline = getOutlineDrawable();

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (outline != null) {
            outline.draw(batch, x, y, width, height);
        }
    }

    @Override
    public void render() {
        drawContent(batch);
        drawOutline(batch, 1);
    }

    protected Rectangle findBounds() {
        return null;
    }
    public void drawAt(int x, int y){

    }
    public void fix(){

    }
    public void drag(int x, int y){

    }
    public void resize(int x, int y){

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
        setLayer(-1);
        screen.removeRenderable(this);
    }

    public int onBorder(int x, int y){
        if(resize != -1) return resize; //so the changed border isn't switched while resizing
        x += offsetX;
        y += offsetY;
        if(x >= LEFTBOUND && x < LEFTBOUND + BORDERSIZE){ //if touching left border
            if(y <= UPPERBOUND && y > UPPERBOUND - BORDERSIZE) return resize = 1; //if also touching top border
            if(y >= LOWERBOUND && y < LOWERBOUND + BORDERSIZE) return resize = 7; //if also touching bottom border
            return resize = 0; //if only touching left border
        }
        if(x <= RIGHTBOUND && x > RIGHTBOUND - BORDERSIZE){ //if touching right border
            if(y <= UPPERBOUND && y > UPPERBOUND - BORDERSIZE) return resize = 3; //if also touching top border
            if(y >= LOWERBOUND && y < LOWERBOUND + BORDERSIZE) return resize = 5; //if also touching bottom border

            return resize = 4; //if only touching right border
        }
        if(y <= UPPERBOUND && y > UPPERBOUND - BORDERSIZE) return resize = 2; //if only touching top border
        if(y >= LOWERBOUND && y < LOWERBOUND + BORDERSIZE) return resize = 6; //if only touching bottom border
        return resize = -1; //if not touching any border
    }
    public boolean isOutOfBounds(){
        findBounds(); //updating the bound
        return (LEFTBOUND < offsetX || LOWERBOUND < offsetY || RIGHTBOUND > offsetX + boardWidth || UPPERBOUND > offsetY + boardHeight);
    }
    public boolean brokeLeftBounds(){
        findBounds(); //updating the bound
        return LEFTBOUND < offsetX;
    }
    public boolean brokeRightBounds(){
        findBounds(); //updating the bound
        return RIGHTBOUND > offsetX + boardWidth;
    }
    public boolean brokeLowerBounds(){
        findBounds(); //updating the bound
        return LOWERBOUND < offsetY;
    }
    public boolean brokeUpperBounds(){
        return UPPERBOUND > offsetY + boardHeight;
    }
    public boolean breakingLeftBounds(float deltaX){
        return (getX() <= offsetX && deltaX < 0);
    }
    public boolean breakingRightBounds(float deltaX){
        return (getX()+getWidth() >= offsetX+parentBoard.getWidth() && deltaX > 0);
    }
    public boolean breakingLowerBounds(float deltaY){
        return (getY() <= offsetY && deltaY < 0);
    }
    public boolean breakingUpperBounds(float deltaY){
        return (getY()+getHeight() >= offsetY+parentBoard.getHeight() && deltaY > 0);
    }

    public void setSelect(boolean select){
        selected = select;
    }
    static void wipe(){
        lastx = -1;
        lasty = -1;
    }

    public void save(){
        ps.setStat(PixSerializer.Stat.XPOS, (int) getX());
        ps.setStat(PixSerializer.Stat.YPOS, (int) getY());
        ps.setStat(PixSerializer.Stat.WIDTH, (int) getWidth());
        ps.setStat(PixSerializer.Stat.HEIGHT, (int) getHeight());
        ps.setStat(PixSerializer.Stat.XOFFSET, (int) offsetX);
        ps.setStat(PixSerializer.Stat.YOFFSET, (int) offsetY);
    }

    public void load(){
        ps.load();

        setPosition(Float.parseFloat(ps.getValue(PixSerializer.Stat.XPOS).toString()), Float.parseFloat(ps.getValue(PixSerializer.Stat.YPOS).toString()));
        setSize(Float.parseFloat(ps.getValue(PixSerializer.Stat.WIDTH).toString()), Float.parseFloat(ps.getValue(PixSerializer.Stat.HEIGHT).toString()));
        setOffsetX(Float.parseFloat(ps.getValue(PixSerializer.Stat.XOFFSET).toString()));
        setOffsetY(Float.parseFloat(ps.getValue(PixSerializer.Stat.YOFFSET).toString()));
    }

    public void setFocused(boolean focused){
        this.focused = focused;
        if(Main.getMainScreen() != null && Main.getMainScreen().isFocused()) Main.getMainScreen().focus();
    }

    public void setSoftVisible(boolean visible) {
        supposedToBeVisible = visible;
    }

    @Override
    public boolean isSupposedToBeVisible() {
        return supposedToBeVisible;
    }

    @Override
    public void setLayer(int layer) {
        int oldLayer = getLayer();

        if(oldLayer != -1) {
            for (int renderable = 0; renderable < getScreen().layers.get(oldLayer).size(); renderable++) { //find the panel in the old layer
                if (this == screen.layers.get(oldLayer).get(renderable)) {
                    screen.layers.get(oldLayer).remove(this); //remove the panel from the old layer
                }
            }
        }

        if(layer == -1); //don't add this to a list, so it doesn't get rendered
        else if(screen.layers.containsKey(layer)){ //if the layer already exists
            screen.layers.get(layer).add(this); //add the panel to its new later
        }
        else{
            getScreen().addLayer(layer);
            screen.layers.get(layer).add(this); //add the panel to the new later
        }

        this.layer = layer;
    }

    @Override
    public int getLayer(){
        return layer;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
    public void setBoardWidth(float boardWidth) {
        this.boardWidth = boardWidth;
    }
    public void setBoardHeight(float boardHeight) {
        this.boardHeight = boardHeight;
    }
    public boolean isResizing(){
        return resize != -1;
    }
    public float getBoardHeight() {
        return boardHeight;
    }
    protected @Null Drawable getOutlineDrawable() {
        return null;
    }

    public float getBoardWidth() {
        return boardWidth;
    }
    public Rectangle getBounds(){
        return bounds;
    }

    public PixSerializer getPS() {
        return ps;
    }

    public void dispose(){

    }
}

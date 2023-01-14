package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class GenOutline extends Widget {
    protected Board parentBoard;

    protected float offsetX, offsetY;
    protected float boardHeight;
    protected float boardWidth;

    protected int LEFTBOUND = 0;
    protected int RIGHTBOUND = 0;
    protected int UPPERBOUND = 0;
    protected int LOWERBOUND = 0;
    protected boolean activated;

    protected Rectangle bounds = new Rectangle();
    protected boolean drawable = true;

    static protected int lastx = -1, lasty = -1;

    public GenOutline(Board board) {
        //sets the board and transfers variables
        parentBoard = board;
        boardHeight = parentBoard.getHeight();
        boardWidth = parentBoard.getWidth();
        offsetX = parentBoard.getOffsetX();
        offsetY = parentBoard.getOffsetY();
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

    protected Rectangle findBounds() {
        return null;
    }
    public void drawAt(int x, int y){

    }
    public void fix(){

    }
    public void drag(int x, int y){
    }
    public void moveForward(){
        ArrayList<GenOutline> outlines = parentBoard.getOutlines();
        int i = outlines.indexOf(this);
        if(i+1 < outlines.size()) Collections.swap(outlines, i, ++i);
    }
    public void moveBackward(){
        ArrayList<GenOutline> outlines = parentBoard.getOutlines();
        int i = outlines.indexOf(this);
        if(i-1 >= 0) Collections.swap(outlines, i, --i);
    }
    public void moveToBack(){
        ArrayList<GenOutline> outlines = parentBoard.getOutlines();
        outlines.remove(this);
        outlines.add(0, this);
    }
    public void moveToFront(){
        ArrayList<GenOutline> outlines = parentBoard.getOutlines();
        outlines.remove(this);
        outlines.add(this);
    }
    public void delete(){

    }
    public void setActivated(boolean activate){
        activated = activate;
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
        findBounds(); //updating the bound
        return UPPERBOUND > offsetY + boardHeight;
    }

    static void wipe(){
        lastx = -1;
        lasty = -1;
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


}

package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import java.awt.*;

public class GenOutline extends Widget {
    protected Board parentBoard;

    protected float offsetX, offsetY;
    protected float boardHeight;
    protected float boardWidth;

    protected int LEFTBOUND = 0;
    protected int RIGHTBOUND = 0;
    protected int UPPERBOUND = 0;
    protected int LOWERBOUND = 0;

    protected Rectangle bounds = new Rectangle();


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

    @Override
    public void draw(Batch batch, float parentAlpha) {
    }

    protected Rectangle findBounds() {
        return null;
    }
    public void drawAt(int x, int y){

    }
    public void lockIn(){

    }
    public void drag(int x, int y){
    }
    public void moveForward(){
    }
    public void moveBackward(){
    }
    public void moveToBack(){
    }
    public void moveToFront(){
    }
    public void delete(){
    }

    static void wipe(){
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
    public float getBoardWidth() {
        return boardWidth;
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
    public Rectangle getBounds(){
        return bounds;
    }


}

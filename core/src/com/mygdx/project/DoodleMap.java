package com.mygdx.project;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;
import java.util.ArrayList;

public class DoodleMap extends Pixmap {
    ArrayList<Point> drawnPoints = new ArrayList<>();
    ArrayList<Point> tempPoints = new ArrayList<>();
    Texture texture;

    public Doodle doodle;

    public DoodleMap(int width, int height, Format format, Doodle doodle) {
        super(width, height, format);
        this.doodle = doodle;
        if(doodle != null) doodle.setDoodle(this);
        texture = new Texture(this);
    }

    public void storePoints(boolean drawMode, int x, int y, int x2, int y2){
        //if the point is out of bounds, do not store
        if(x > getWidth() || x2 > getWidth()) return;
        if(x < -1 || x2 < -1) return;
        if(y > getHeight() || y2 > getHeight()) return;
        if(y < -1 || y2 < -1) return;


        Point point1 = new Point(x, y);
        Point point2 = new Point(x2, y2);

        int P1Index = -1;
        int P2Index = -1;

        if (drawMode){
            for (Point point: drawnPoints) {
                if (point.x == point1.x && point.y == point1.y){
                    P1Index = 1;
                }
                if (point.x == point2.x && point.y == point2.y){
                    P2Index = 1;
                }
            }
            if(P1Index == -1) drawnPoints.add(point1);
            if(P2Index == -1) drawnPoints.add(point2);
        }
        if(!drawMode){
            for (int i = drawnPoints.size()-1; i >= 0; i--) {
                if (drawnPoints.get(i).x == point1.x && drawnPoints.get(i).y == point1.y){
                    P1Index = i;
                }
                if (drawnPoints.get(i).x == point2.x && drawnPoints.get(i).y == point2.y){
                    P2Index = i;
                }
            }
            if(P1Index != -1) drawnPoints.remove(P1Index);
            if(P2Index != -1) drawnPoints.remove(P2Index);
        }
    }
    public void storeTempPoints(boolean drawMode, int x, int y, int x2, int y2){
        //if the point is out of bounds, do not store
        if(x > getWidth() || x2 > getWidth()) return;
        if(x < 0 || x2 < 0) return;
        if(y > getHeight() || y2 > getHeight()) return;
        if(y < 0 || y2 < 0) return;

        Point point1 = new Point(x, y);
        Point point2 = new Point(x2, y2);

        int P1Index = -1;
        int P2Index = -1;

        if (drawMode){
            for (Point point: tempPoints) {
                if (point.x == point1.x && point.y == point1.y){
                    P1Index = 1;
                }
                if (point.x == point2.x && point.y == point2.y){
                    P2Index = 1;
                }
            }
            if(P1Index == -1) tempPoints.add(point1);
            if(P2Index == -1) tempPoints.add(point2);
        }
        if(!drawMode){
            for (int i = tempPoints.size()-1; i >= 0; i--) {
                if (tempPoints.get(i).x == point1.x && tempPoints.get(i).y == point1.y){
                    P1Index = i;
                }
                if (tempPoints.get(i).x == point2.x && tempPoints.get(i).y == point2.y){
                    P2Index = i;
                }
            }
            if(P1Index != -1) tempPoints.remove(P1Index);
            if(P2Index != -1) tempPoints.remove(P2Index);
        }
    }
    public void transferPoints(){
        drawnPoints.clear();
        drawnPoints.addAll(tempPoints);
        tempPoints.clear();
    }
    public void setOutline(Doodle doodle) {
        this.doodle = doodle;
    }
    public Doodle getOutline(){
        return doodle;
    }
    public ArrayList<Point> getPoints(){
        return drawnPoints;
    }

    public void save(){

    }
}

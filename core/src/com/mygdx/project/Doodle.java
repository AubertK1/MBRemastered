package com.mygdx.project;

import com.badlogic.gdx.graphics.Pixmap;

import java.awt.*;
import java.util.ArrayList;

public class Doodle extends Pixmap {
    ArrayList<Point> drawnPoints = new ArrayList<>();
    ArrayList<Point> erasePoints = new ArrayList<>();
    //fixme
    public Outline outline;

    public Doodle(int width, int height, Format format, Outline outline) {
        super(width, height, format);
        this.outline = outline;
    }
    public void storePoints(boolean drawMode, int x, int y, int x2, int y2){
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

    public void setOutline(Outline outline) {
        this.outline = outline;
    }
}

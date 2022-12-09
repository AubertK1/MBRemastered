package com.mygdx.project;

import com.badlogic.gdx.graphics.Pixmap;

import java.awt.*;
import java.util.ArrayList;

public class Doodle extends Pixmap {
    ArrayList<Point> drawnPoints = new ArrayList<>();
    ArrayList<Point> erasePoints = new ArrayList<>();

    public Doodle(int width, int height, Format format) {
        super(width, height, format);
    }
    public void storeLine(boolean drawMode, int x, int y, int x2, int y2){
        if (drawMode){
            Point point1 = new Point(x, y);
            Point point2 = new Point(x2, y2);
            boolean addP1 = true;
            boolean addP2 = true;
            for (Point point: drawnPoints) {
                if (point.x == point1.x && point.y == point1.y){
                    addP1 = false;
                }
                if (point.x == point2.x && point.y == point2.y){
                    addP2 = false;
                }
            }
            if(addP1) drawnPoints.add(point1);
            if(addP2) drawnPoints.add(point2);
        }
    }
}

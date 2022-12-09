package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;


/**
 * Class that stores a Matrix that represents a brush
 * @author Román Jiménez
 */
public class Brush {
    int size;
    float[][] brush;

    /**
     * Constructor
     * @param size size of the brush (matrix size)
     * @param brush must have n*2 + 1 size, actual brush data
     */
    public Brush(int size, float[][] brush) {
        this.size = size;
        this.brush = brush;
    }

    public static Brush generateBrush(int width, boolean soft){
        int size = (int)width/2;
        int fadedSize = size;
        if(width > 5) fadedSize = 2;

        float[][] brushLayout = new float[width][width];
        int lastSpot = brushLayout.length-1;

        if(!soft) {
            for (int i = 0; i < brushLayout.length; i++) {
                for (int j = 0; j < brushLayout.length; j++) {
                    brushLayout[i][j] = 1;
                }
            }
        }
        if(soft) {
            for (int i = 0; i < brushLayout.length; i++) {
                for (int j = 0; j < brushLayout.length; j++) {
                    //middle row
                    if(i == fadedSize+1 || i == brushLayout.length - fadedSize){
                        //center
                        if(j == fadedSize+1){
                            brushLayout[i][j] = 1;
                        }
                        //first and last column
                        else if(j == 0 || j == lastSpot){
                            brushLayout[i][j] = 1f/2;
                        }
                        else brushLayout[i][j] = 1;
                    }
                    //2nd abd 2nd to last row
                    else if(i == 1 || i == lastSpot - 1){
                        //first and last column
                        if(j == 0 || j == lastSpot){
                            brushLayout[i][j] = 1f/4;
                        }
                        //second and second to last column
                        if(j == fadedSize || j == lastSpot+1-fadedSize){
                            brushLayout[i][j] = 1f/2;
                        }
                        else brushLayout[i][j] = 1;
                    }
                    //top and bottow row
                    else if(i == 0 || i == lastSpot){
                        //first and last column / corners
                        if(j == 0 || j == lastSpot){
                            brushLayout[i][j] = 0.0f;
                        }
                        //second and second to last column
                        if(j == 0 || j == lastSpot+1-fadedSize){
                            brushLayout[i][j] = 1f/4;
                        }

                        if(j == fadedSize || j == lastSpot-fadedSize){
                            brushLayout[i][j] = 1f/2;
                        }
                        else brushLayout[i][j] = 1;
                    }
                    else brushLayout[i][j] = 1;
                }
            }
        }

        Brush brush = new Brush(size, brushLayout);
        return brush;
    }

    /**
     * Getter for the size attribute
     * @return size of the brush
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the brush data
     * @return float matrix representing brush data
     */
    public float[][] getBrush() {
        return brush;
    }

    /**
     * Gets a {@link Pixmap} representing the Brush
     * @return a {@link Pixmap}
     */
    public Pixmap getPixmap() {
        Pixmap pix = new Pixmap(brush.length, brush.length, Format.RGBA8888);
        for (int i = 0; i < brush.length; i++) {
            for (int j = 0; j < brush.length; j++) {
                //changed the original code to only vary the opacity of the pixel and hard set the color to black
                pix.setColor(new Color( 0, 0, 0, brush[i][j]));
                pix.drawPixel(j, i);
            }
        }

        Pixmap canvas = new Pixmap(32, 32, Format.RGBA8888);
        canvas.setFilter(Filter.NearestNeighbour);
        pix.setFilter(Filter.NearestNeighbour);
        canvas.drawPixmap(pix, 0, 0, brush.length, brush.length, 0, 0, brush.length*2, brush.length*2);

        return canvas;
    }
}
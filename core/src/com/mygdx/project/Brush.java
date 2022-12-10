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
    int width;
    float[][] brush;

    /**
     * Constructor
     * @param size size of the brush (matrix size)
     * @param brush must have n*2 + 1 size, actual brush data
     */
    public Brush(int size, float[][] brush) {
        this.size = size;
        width = size*2+1;
        this.brush = brush;
    }

    //fixme
    public static Brush generateBrush(int width, boolean soft){
        int size = width/2;
        int absRow;
        int absCol;
        double rounded;

        float[][] brushLayout = new float[width][width];

        if(!soft || width == 1) {
            for (int i = 0; i < brushLayout.length; i++) {
                for (int j = 0; j < brushLayout.length; j++) {
                    brushLayout[i][j] = 1;
                }
            }
        }
        else {
            for (int i = 0; i < brushLayout.length; i++) {
                for (int j = 0; j < brushLayout.length; j++) {
                    absRow = i;
                    absCol = j;
                    //so that the opposite rows/columns are the same value
                    if(i > size) absRow = (width-1) - i;
                    if(j > size) absCol = (width-1) - j;

                    rounded = Math.round(((float)(absRow+absCol)/(size*2.0)) * 100.00)/100.00 * 1.5f;
                    if (rounded >= 1.0) brushLayout[i][j] = 1.0f;
                    else brushLayout[i][j] = (float) rounded;
                    //fixme for testing purposes
                    if(i == brushLayout.length-1 && j == brushLayout.length-1){
                        System.out.print("");
                    }
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
    public int getWidth() {
        return width;
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
        if (size*2+1 > 32) return getBigPixmap();

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
        canvas.drawPixmap(pix, 0, 0, brush.length, brush.length, 0, 0, brush.length, brush.length);

        return canvas;
    }
    public Pixmap getBigPixmap() {
        Pixmap pix = new Pixmap(brush.length, brush.length, Format.RGBA8888);
        for (int i = 0; i < brush.length; i++) {
            for (int j = 0; j < brush.length; j++) {
                //changed the original code to only vary the opacity of the pixel and hard set the color to black
                pix.setColor(new Color( 0, 0, 0, brush[i][j]));
                pix.drawPixel(j, i);
            }
        }

        Pixmap canvas = new Pixmap(128, 128, Format.RGBA8888);
        canvas.setFilter(Filter.NearestNeighbour);
        pix.setFilter(Filter.NearestNeighbour);
        canvas.drawPixmap(pix, 0, 0, brush.length, brush.length, 0, 0, brush.length, brush.length);

        return canvas;
    }
}
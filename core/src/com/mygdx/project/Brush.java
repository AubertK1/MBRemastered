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

    //fixme
    public static Brush generateBrush(int width, boolean soft){
        int size = width/2;
        int absSpotR;
        int absSpotC;

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
                    absSpotR = i;
                    absSpotC = j;
                    if(i > size) absSpotR = (width-1) - i;
                    if(j > size) absSpotC = (width-1) - j;

                    if(absSpotR == 0) {
                        if ((absSpotR+absSpotC) >= 4) brushLayout[i][j] = 1.0f;
                        else brushLayout[i][j] = (float)(absSpotR+absSpotC)/4.0f;
                    }
                    else if(absSpotR == 1) {
                        if ((absSpotR+absSpotC+1) >= 4) brushLayout[i][j] = 1.0f;
                        else brushLayout[i][j] = (float)(absSpotR+absSpotC)/4.0f;
                    }
                    else if(absSpotR == 2) {
                        if ((absSpotR+absSpotC+1) >= 4) brushLayout[i][j] = 1.0f;
                        else brushLayout[i][j] = (float)(absSpotR+absSpotC)/4.0f;
                    }
                    else brushLayout[i][j] = 1.0f;
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
        canvas.drawPixmap(pix, 0, 0, brush.length, brush.length, 0, 0, brush.length, brush.length);

        return canvas;
    }
}
package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;


/**
 * Class that stores a Matrix that represents a brush. <s>
 * Supposed to have a one and done usage due to how easily they can be generated. <p>
 * Inspired by: Román Jiménez
 */
public class Brush {
    private final int size;
    private final int width;
    private final float[][] brush;

    /**
     * Constructor
     * @param size size of the brush (matrix size)
     * @param brush must have n*2 + 1 size, actual brush data
     */
    private Brush(int size, float[][] brush) {
        this.size = size;
        width = size*2+1;
        this.brush = brush;
    }

    //fixme develop further for more appealing brushes
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

                    rounded = Math.round(((absRow + absCol) / (size * 2f)) * 100f) / 100f * 1.5f;
                    if (rounded >= 1) brushLayout[i][j] = 1;
                    else brushLayout[i][j] = (float) rounded;
                }
            }
        }

        return new Brush(size, brushLayout);
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
        if (width > 32) return getBigPixmap();

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
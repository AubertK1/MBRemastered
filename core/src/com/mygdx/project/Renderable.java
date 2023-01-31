package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    void render();

    boolean isSupposedToBeVisible();

    void setLayer(int layer);

    int getLayer();

    boolean isFocused();

    void dispose();
}
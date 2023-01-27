package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    void render();

    boolean isSupposedToBeVisible();
}
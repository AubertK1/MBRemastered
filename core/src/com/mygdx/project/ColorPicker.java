package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ColorPicker extends Widget {
    Texture texture;
    Pixmap pixmap;
    InputListener inputListener;

    public ColorPicker() {
        texture = new Texture("core\\pics\\MBSkin2\\colormap.png");

        texture.getTextureData().prepare();
        pixmap = texture.getTextureData().consumePixmap();

        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.print(x + ", " + y + "; ");
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                return false;
            }

            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
            }

        });
    }

    public void draw (Batch batch, float parentAlpha) {
        float x = getX();
        float y = getY();
        float height = getHeight();
        float width = getWidth();

        batch.draw(texture, x, y, width, height);
    }

    private Color getColor(float x, float y){
        pixmap.getPixel((int) x, (int) y);
        return null;
    }

    public void convertTex(Texture texture){
        for (int width = 0; width < texture.getWidth() + 1; width++) {
            for (int height = 0; height < texture.getHeight() + 1; height++) {

            }
        }
    }

    @Override
    public void setSize(float width, float height){
        width = height * 1.173f;
        super.setSize(width, height);
    }
}

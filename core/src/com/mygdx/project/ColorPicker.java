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
    private Color newColor;

    public ColorPicker() {
        texture = new Texture("core\\pics\\MBSkin2\\colormap2.png");

        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        pixmap = texture.getTextureData().consumePixmap();
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);

        addListener(inputListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                newColor = getColor(x, y);
                if(newColor != null) {
                    System.out.println(newColor);
                    Main.masterBoard.board.setDrawingColor(newColor);
                    Main.masterBoard.board.setCurrentColor(newColor);
                }
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
        float y2 = getHeight()-y;

        int i = pixmap.getPixel((int) x, (int) y2);
        Color color = new Color(i);

        if(color.toString().equals("00000000"))
            return null;
        else return color;
    }

    @Override
    public void setSize(float width, float height){
        width = height * 1.173f;
        super.setSize(width, height);

        Pixmap imagePixmap = new Pixmap(Gdx.files.internal("core\\pics\\MBSkin2\\colormap2.png"));
        Pixmap resizedPixmap = new Pixmap((int) width, (int) height, imagePixmap.getFormat());
        resizedPixmap.drawPixmap(imagePixmap,
                0, 0, imagePixmap.getWidth(), imagePixmap.getHeight(),
                0, 0, resizedPixmap.getWidth(), resizedPixmap.getHeight()
        );
        texture = new Texture(resizedPixmap);
        imagePixmap.dispose();
        resizedPixmap.dispose();

        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        pixmap = texture.getTextureData().consumePixmap();
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
    }
}

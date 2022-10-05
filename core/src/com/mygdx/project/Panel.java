package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.ArrayList;

public class Panel {
    Texture texture;
    Rectangle position;
    ArrayList<MBComponent> components;

    public Panel(String fileLocation, Rectangle position){
        texture = new Texture(fileLocation);
        this.position = position;
    }

    public void add(MBComponent component){
        components.add(component);
    }

    public void render (SpriteBatch batch) {
        //screen is 1920x1000 so adjust accordingly
        batch.draw(texture, position.x, position.y, position.width, position.height);
    }
    public void dispose(){
        texture.dispose();
    }
}

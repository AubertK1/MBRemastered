package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;
import java.util.ArrayList;

public class Panel {
    Texture texture;
    Rectangle position;
    ArrayList<MBComponent> components = new ArrayList<>();



    public Panel(String fileLocation, Rectangle position){
        texture = new Texture(fileLocation);
        this.position = position;
    }

    public void add(MBComponent component){
        components.add(component);
        Main.stage.addActor(component);
    }

    public void render (SpriteBatch batch) {
        //screen is 1920x1000 so adjust accordingly
        batch.draw(texture, position.x, position.y, position.width, position.height);
/*        if (components.size() != 0) {
            batch.draw(components.get(0)., (position.x + 50), (position.y + 50), 100, 100);
        }*/
    }
    public void dispose(){
        texture.dispose();
    }
}

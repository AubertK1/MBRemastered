package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Tipbox extends Minipanel{
    public Tipbox(Rectangle position) {
        super("core\\pics\\TipBox3.png", position);
    }


    @Override
    public void render(SpriteBatch batch) {
        if (getPanel().editMode && getPanel().supposedToBeVisible) {
        batch.draw(texture, position.x, position.y, position.width, position.height);
        //loops through the minipanel's list of components
            for (int c = 0; c < components.size(); c++) {
                //sets the soft visibility of the component to true
                components.get(c).setSoftVisible(true);
                components.get(c).toFront();
//                Main.stage.getRoot().swapActor(components.get(c), Main.stage.getRoot().getChild(Main.stage.getRoot().getChildren().size-1));
            }
        } else {
            //loops through the minipanel's list of components
            for (int c = 0; c < components.size(); c++) {
                //if the component is supposed to be visible...
                if (components.get(c).supposedToBeVisible) {
                    //sets the soft visibility of the component to false
                    components.get(c).setSoftVisible(false);
                }
            }
        }
    }

}

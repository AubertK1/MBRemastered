package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Tipbox extends Minipanel{
    public Tipbox(Rectangle position) {
        super("assets\\Panels\\TipBox4.png", position);
    }


    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        if (getParentPanel().editMode && getParentPanel().supposedToBeVisible) {

            batch.draw(texture, position.x, position.y, position.width, position.height);
            //loops through the minipanel's list of components
            for (int c = 0; c < components.size(); c++) {
                //sets the soft visibility of the component to true
                components.get(c).getComponent().toFront();
                components.get(c).setSoftVisible(true);
                components.get(c).draw(aFloat);
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
    public MBTextArea getTextArea(){
        for (MBComponent textarea: components) {
            if(textarea instanceof MBTextArea) return (MBTextArea) textarea;
        }
        return null;
    }
}

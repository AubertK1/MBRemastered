package com.mygdx.project.Panels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.project.Components.MBComponent;
import com.mygdx.project.Components.MBTextArea;
import com.mygdx.project.Screen;

public class Tipbox extends Minipanel{
    public Tipbox(Rectangle position, Screen screen) {
        super("assets\\Panels\\TipBox4.png", position, screen);
        setFocused(true);
    }


/*
    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, aFloat);

        if (getParentPanel().supposedToBeVisible) {

            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
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
*/
    public MBTextArea getTextArea(){
        for (MBComponent textarea: components) {
            if(textarea instanceof MBTextArea) return (MBTextArea) textarea;
        }
        return null;
    }
}

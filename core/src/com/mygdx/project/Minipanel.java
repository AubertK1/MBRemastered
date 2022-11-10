package com.mygdx.project;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * I only want there to be a certain amount of panels, so minipanels were created to act as
 * smaller panels that go inside the main panels.
 * This class currently has no other use than to be a label for the non-main panels
 */
public class Minipanel extends Panel{
    public Minipanel(String fileLocation, Rectangle position) {
        super(fileLocation, position);
    }
    public void render (SpriteBatch batch) {
        //screen size is 1920x1000 so adjust accordingly
        //draws this panel
        batch.draw(texture, position.x, position.y, position.width, position.height);
        //loops through this panel's list of minipanels
        for (int i = 0; i < minipanels.size(); i++) {
            //if the minipanel's spot value is less than 0 then it doesn't render it (because it doesn't call render)
            if(!minipanels.get(i).supposedToBeVisible){
                //loops through the minipanel's list of components
                for (int c = 0; c < minipanels.get(i).components.size(); c++) {
                    //sets the soft visibility of the component to false
                    minipanels.get(i).components.get(c).setSoftVisible(false);
                }
            }
            //renders everything else and sets the soft visibility to true
            else {
                minipanels.get(i).render(batch);
            }
        }
        for (int c = 0; c < components.size(); c++) {

            if(components.get(c).supposedToBeVisible) {
                //sets the soft visibility of the component to true
                components.get(c).setSoftVisible(true);
                components.get(c).getComponent().draw(batch, 1);
            }
        }
    }
}

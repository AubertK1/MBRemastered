package com.mygdx.project;

import com.badlogic.gdx.Gdx;
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
        for (Panel minipanel : minipanels) {
            //if the minipanel's spot value is less than 0 then it doesn't render it (because it doesn't call render)
            if (!minipanel.supposedToBeVisible) {
                //loops through the minipanel's list of components
                for (int c = 0; c < minipanel.components.size(); c++) {
                    //sets the soft visibility of the component to false
                    minipanel.components.get(c).setSoftVisible(false);
                }
            }
            //renders everything else and sets the soft visibility to true
            else {
                minipanel.render(batch);
            }
        }
        for (MBComponent component : components) {

            if (component.supposedToBeVisible) {
                //sets the soft visibility of the component to true
                component.setSoftVisible(true);
//                components.get(c).getComponent().act(1/60f);

                component.getComponent().draw(batch, 1);
            }
        }
    }
}

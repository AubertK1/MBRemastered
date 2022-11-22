package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.DelayedRemovalArray;

public class MBButton extends MBComponent{
    Button button;
    String style = "default";

    public MBButton(Skin skin) {
        button = new Button(skin);
    }
    public MBButton(Skin skin, String styleName) {
        skin1 = skin;
        button = new ImageButton(skin1, styleName);
        style = styleName;
    }
    public MBButton(String text, Skin skin) {
        skin1 = skin;
        button = new TextButton(text, skin1);
    }
    public MBButton(String text, Skin skin, String styleName) {
        skin1 = skin;
        button = new TextButton(text, skin1, styleName);
    }
    public void setImage(final Texture texture){
        //saves the position of the text button
        Rectangle pos = new Rectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        //removes the button's listeners
        for (int i = 0; i < button.getListeners().size; i++) {
            button.removeListener(button.getListeners().get(i));
        }
        //changes this textbutton to an imagebutton
        if(!(button instanceof ImageButton)) button = new ImageButton(skin1);
        //re-initializes the button
        button.setPosition(pos.x, pos.y);
        button.setSize(pos.width, pos.height);
        aFloat = .5f;

        final int[] IB = {1};
        //adds a listener to the button
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(IB[0] == 1) {
                    System.out.println("yo");
					IB[0] = 1;
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            //fixme
                            Main.launch(Main.class);
                        }
                    });

                }
                else{

                }

            }
            @Override
            public boolean handle (Event event) {
                if (!getButton().isOver()) {
                    aFloat = .5f;
                }
                else aFloat = 1f;
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });
        //changes the image of the button to the new image
        skin1.get(style, ImageButton.ImageButtonStyle.class).imageUp = new TextureRegionDrawable(new TextureRegion(texture));
    }
    @Override
    public boolean addListener(EventListener listener) {
        return button.addListener(listener);
    }
    public Actor getComponent(){
        return button;
    }
    public Button getButton(){
        return button;
    }
}

package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

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
    public void toImageButton(final Texture texture){
        //saves the position of the text button
        Rectangle pos = new Rectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        //removes the button's listeners
        for (int i = button.getListeners().size-1; i >= 0; i--) {
            button.removeListener(button.getListeners().get(i));
        }
        int buttonID = -1;

        for (int i = 0; i < Main.stage.getActors().size; i++) {
            if (Main.stage.getActors().get(i) == button) buttonID = i;
        }
        if (buttonID != -1) Main.stage.getActors().get(buttonID).addAction(Actions.removeActor());

        //changes this textbutton to an imagebutton
        button = new ImageButton(skin1);
        //re-initializes the button
        button.setPosition(pos.x, pos.y);
        button.setSize(pos.width, pos.height);
        aFloat = .75f;

        final int[] IB = {1};
        //adds a listener to the button
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!hasWindow) {
                    System.out.println("yo");
                    MBWindow window = new MBWindow(texture, skin1, MBButton.this);
                    window.setSize(window.image.getWidth(), window.image.getHeight());
                    window.setPosition((float)Gdx.graphics.getWidth()/2 - window.getWidth()/2, (float)Gdx.graphics.getHeight()/2 - window.getHeight()/2);
                    window.setVisible(true);
                    //makes it so that the user can interact with the app while the window is up (if true it will be the only thing you can interact with)
                    ((Window)window.getComponent()).setModal(false);
                    //allows the user to drag it
                    ((Window)window.getComponent()).setMovable(true);
                    add(window);
                }
                else{

                }

            }
            @Override
            public boolean handle (Event event) {
                if (!getButton().isOver()) {
                    aFloat = .75f;
                    for (MBComponent innerButt: components) {
                        if (!(innerButt instanceof MBWindow)) innerButt.aFloat = 0f;
                    }
                }
                else{
                    aFloat = 1f;
                    for (MBComponent innerButt: components) {
                        if (!(innerButt instanceof MBWindow)) innerButt.aFloat = .75f;
                    }
                }
                button.setDisabled(hasWindow);
                //renders the file (in handle because it's always called, so it will be called as soon as the file is chosen)
                Main.fileChooseHandle(MBButton.this.parentPanel, MBButton.this);
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });

        //changes the image of the button to the new image
        skin1.get(style, ImageButton.ImageButtonStyle.class).imageUp = new TextureRegionDrawable(new TextureRegion(texture));
//        skin1.get(style, ImageButton.ImageButtonStyle.class).over = skin1.get(style, ImageButton.ImageButtonStyle.class).up;
    }
    public void toTextButton(String text){
        Rectangle pos = new Rectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        for (int i = 0; i < button.getListeners().size; i++) {
            button.removeListener(button.getListeners().get(i));
        }
        for (int i = components.size()-1; i >= 0; i--) {
            delete(components.get(i));
        }
        int buttonID = -1;

        for (int i = 0; i < Main.stage.getActors().size; i++) {
            if (Main.stage.getActors().get(i) == button) buttonID = i;
        }
        if (buttonID != -1) Main.stage.getActors().get(buttonID).addAction(Actions.removeActor());

        //changes this imagebutton to a textbutton
        button = new TextButton(text, skin1);
        //re-initializes the button
        button.setPosition(pos.x, pos.y);
        button.setSize(pos.width, pos.height);
        aFloat = .5f;

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //allows the user to choose the file they want to display
                Main.fileChooseChanged();
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is not hovered over the imageButton...
                if (!getButton().isOver()) {
                    aFloat = .5f;
                }
                else aFloat = 1f;
                //renders the file (in handle because it's always called, so it will be called as soon as the file is chosen)
                Main.fileChooseHandle(MBButton.this.parentPanel, MBButton.this);
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });

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

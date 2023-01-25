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
    private Button button;
    String style = "default";
    Texture texture;

    /**
     * Regular plain button
     * @param skin
     */
    public MBButton(Skin skin) {
        button = new Button(skin);
    }

    /**
     * ImageButton with an image from a preset style
     * @param skin
     * @param styleName
     */
    public MBButton(Skin skin, String styleName) {
        this.skin = skin;
        button = new ImageButton(this.skin, styleName);
        style = styleName;
    }

    /**
     * TextButton
     * @param text
     * @param skin
     */
    public MBButton(String text, Skin skin) {
        this.skin = skin;
        button = new TextButton(text, this.skin);
    }
    /**
     * TextButton with a special style
     * @param text
     * @param skin
     */
    public MBButton(String text, Skin skin, String style) {
        this.skin = skin;
        this.style = style;
        button = new TextButton(text, this.skin, style);
    }
    public void toImageButton(final Texture texture){
        this.texture = texture;
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

        //changes this button to an imagebutton
        button = new ImageButton(skin);
        //re-initializes the button
        button.setPosition(pos.x, pos.y);
        button.setSize(pos.width, pos.height);
        setVisible(true);
        //changes the image of the button to the new image
        skin.get(style, ImageButton.ImageButtonStyle.class).imageUp = new TextureRegionDrawable(new TextureRegion(texture));
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

        //changes this button to a textbutton
        button = new TextButton(text, skin);
        //re-initializes the button
        button.setPosition(pos.x, pos.y);
        button.setSize(pos.width, pos.height);
        setVisible(true);
    }
    public void setupSelectImageTextButton(){
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
    public void setupSelectImageImageButton(){
        aFloat = .75f;

        //adds a listener to the button
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!hasWindow) {
                    System.out.println("yo");
                    MBWindow window = new MBWindow(texture, skin, MBButton.this);
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
    }
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

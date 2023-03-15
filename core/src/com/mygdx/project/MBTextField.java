package com.mygdx.project;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class MBTextField extends MBComponent{
    private final TextField textField;
    private InputListener hideFromCLick;
    private InputListener hideFromKeys;
    private Action closingAction;
    private final Action actionReset;
    private Stats.Stat stat;
    public MBTextField(String text, Screen screen) {
        this(text, screen, null, false, false);
    }
    public MBTextField(String text, Screen screen, Stats.Stat stat) {
        this(text, screen, stat, false, false);
    }
    public MBTextField(String text, Screen screen, boolean hideableFromClick, boolean hideableFromKeys) {
        this(text, screen, null, hideableFromClick, hideableFromKeys);
    }
    public MBTextField(String text, final Screen screen, final Stats.Stat stat, boolean hideableFromClick, boolean hideableFromKeys) {
        super(screen);
        textField = new TextField(text, skin);
        setHideableFromClick(hideableFromClick);
        setHideableFromKeys(hideableFromKeys);

        closingAction = new Action() {
            @Override
            public boolean act(float v) {
                return false;
            }
        };
        actionReset = new Action() {
            @Override
            public boolean act(float v) {
                textField.clearActions();
                return false;
            }
        };

        if(stat != null){
            this.stat = stat;
            setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    screen.getStats().setStat(stat, getText());
                }
            });
        }
    }

    public void setHideableFromClick(boolean isHidable){
        if(isHidable){
            hideFromCLick = new InputListener() {
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    Actor target = event.getTarget();
                    if (textField.isAscendantOf(target)) return false;
                    close();
                    return false;
                }

            };
        }
        else hideFromCLick = null;
    }
    public void setHideableFromKeys(boolean isHidable){
        if(isHidable){
/*
            hideFromKeys = new InputListener(){
                public boolean keyDown (InputEvent event, int keycode) {
                    switch (keycode) {
                        case Input.Keys.NUMPAD_ENTER:
                        case Input.Keys.ENTER:
                        case Input.Keys.ESCAPE:
                            close();
                            event.stop();
                            return true;
                    }
                    return false;
                }
            };
*/
            setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    switch (c) {
                        case Input.Keys.NUMPAD_ENTER:
                        case Input.Keys.ENTER:
                        case Input.Keys.ESCAPE:
                        case '\r':
                        case '\n':
                            close();
                    }
                }
            });
        }
//        else hideFromKeys = null;
        else setKeyListener(null);
    }
    private void close(){
        textField.clearActions();
        textField.addAction(closingAction);
        textField.addAction(actionReset);
    }
    public void setClosingAction(Action closingAction){
        this.closingAction = closingAction;
    }
    @Override
    public void setSoftVisible(boolean visible){
        super.setSoftVisible(visible);
        if(visible){
            if(hideFromCLick != null) stage.addCaptureListener(hideFromCLick);
        }
        else{
            if(hideFromCLick != null) stage.removeCaptureListener(hideFromCLick);
        }
    }
    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
        if(visible){
            if(hideFromCLick != null) stage.addCaptureListener(hideFromCLick);
//            if(hideFromKeys != null) stage.addCaptureListener(hideFromKeys);
        }
        else{
            if(hideFromCLick != null) stage.removeCaptureListener(hideFromCLick);
//            if(hideFromKeys != null) stage.removeCaptureListener(hideFromKeys);
        }
    }

    public void setKeyListener(TextField.TextFieldListener listener){
        textField.setTextFieldListener(listener);
    }
    public void setAlignment(int alignment){
        textField.setAlignment(alignment);
    }
    public Actor getActor(){
        return textField;
    }

    public TextField getTextField(){
        return textField;
    }
    public String getText(){
        return textField.getText();
    }
    public void setText(String text){
        textField.setText(text);
    }

    public Stats.Stat getStat(){
        return stat;
    }
    public void setStatValue(int value){
        screen.getStats().setStat(stat, value);
    }
}

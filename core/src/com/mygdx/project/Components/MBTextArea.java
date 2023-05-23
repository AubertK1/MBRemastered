package com.mygdx.project.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.project.Screen;
import com.mygdx.project.Stats;

/**
 * the text area component
 */
public class MBTextArea extends MBComponent{
    //making the text area
    private final TextArea textArea;

    private Integer stat;

    public MBTextArea(String text, Screen screen) {
        this(text, screen, -1);
    }
    public MBTextArea(String text, final Screen screen, final int stat) {
        super(screen);
        //setting the text area
        textArea = new TextArea(text, skin);
        this.stat = stat;

        if(stat != -1){
            setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    screen.getStats().setStat(stat, getText());
                    System.out.println(Stats.statToString(stat));
                }
            });
        }
    }

    public Actor getActor(){
        return textArea;
    }
    public TextArea getTextArea(){
        return textArea;
    }
    public String getText(){
        return textArea.getText();
    }
    public void setText(String text){
        textArea.setText(text);
    }
    public void setKeyListener(TextField.TextFieldListener listener){
        textArea.setTextFieldListener(listener);
    }
    public void assignStat(Integer stat){
        this.stat = stat;
        if(this.stat != -1) screen.getStats().setStat(this.stat, textArea.getText()); //initializes the stat value automatically
    }
    public Integer getAssignedStat(){
        return stat;
    }
    public void setStatValue(int value){
        screen.getStats().setStat(stat, value);
    }
}

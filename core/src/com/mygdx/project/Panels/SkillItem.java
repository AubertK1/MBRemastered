package com.mygdx.project.Panels;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.project.Components.MBButton;
import com.mygdx.project.Components.MBLabel;
import com.mygdx.project.Components.MBSystem;
import com.mygdx.project.Components.MBTextField;
import com.mygdx.project.Screen;

public class SkillItem extends Item{
    private String skill = "";
    private int mod = 0;
    private final int stat;

    public SkillItem(Screen screen, String skill, int stat) {
        super("assets\\Panels\\MiniItemPanel.png", screen);
        this.skill = skill;
        this.stat = stat;
    }
    public SkillItem(Rectangle position, Screen screen, String skill, int stat) {
        super("assets\\Panels\\MiniItemPanel.png", position, screen);
        this.skill = skill;
        this.stat = stat;
    }

    public void initialize() {
        final MBLabel nameLabel, modLabel;

        nameLabel = new MBLabel(skill, screen);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);

        modLabel = new MBLabel(String.valueOf(mod), screen);
        modLabel.setSize(20, getHeight() - 10);
        modLabel.setPosition(getX() + getWidth() - modLabel.getWidth() - 5, getY() + 5);
        modLabel.getLabel().setAlignment(Align.right);

        final MBButton editButton = new MBButton(skill, screen);
        editButton.setPosition(getX(), getY());
        editButton.setSize(getWidth(), getHeight());
        ((TextButton)editButton.getButton()).getLabel().setAlignment(Align.left);
        editButton.setOpacity(0);

        final MBTextField modTF = new MBTextField("0", screen, stat, true, true);
        modTF.setSize(27, getHeight() - 6);
        modTF.setPosition(getX() + getWidth() - modTF.getWidth() - 3, getY() + 3);
        modTF.setVisible(false);

        String skillName2 = shortenString(skill, modLabel.getX() - getX());
        nameLabel.setText(skillName2);

        final MBSystem closeSystem = new MBSystem(modTF, modLabel);
        closeSystem.setUpdateAction(new Action() {
            @Override
            public boolean act(float v) {
                mod = Integer.parseInt(modTF.getText());
                modLabel.setText(String.valueOf(mod));

                return true;
            }
        });
        modTF.setClosingAction(new Action() {
            @Override
            public boolean act(float v) {
                closeSystem.update();

                modTF.setVisible(false);
                modLabel.setVisible(true);
                editMode = false;
                return false;
            }
        });

        editButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(!editMode) {
                    modTF.setVisible(true);
                    modLabel.setVisible(false);
                    editMode = true;
                }
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the button...
                if(editButton.getButton().isOver()){
                    editButton.setOpacity(1);
                }
                else editButton.setOpacity(0);
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });

        add(nameLabel);
        add(modLabel);
        add(editButton);
        add(modTF);
    }
}

package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class SkillItem extends Item{
    private String skill = "";
    private int mod = 0;

    public SkillItem(Screen screen, String skill) {
        super("assets\\Panels\\MiniItemPanel.png", screen);
        this.skill = skill;
    }
    public SkillItem(Rectangle position, Screen screen, String skill) {
        super("assets\\Panels\\MiniItemPanel.png", position, screen);
        this.skill = skill;
    }

    public void initialize() {
        final MBLabel nameLabel, modLabel;
        nameLabel = new MBLabel(skill, screen);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);

        modLabel = new MBLabel(String.valueOf(mod), screen);
        modLabel.setSize(20, getHeight() - 10);
        modLabel.setPosition(getX() + getWidth() - modLabel.getWidth() - 5, getY() + 5);

        final MBButton editButton = new MBButton(skill, screen);
        editButton.setPosition(getX(), getY());
        editButton.setSize(getWidth(), getHeight());
        ((TextButton)editButton.getButton()).getLabel().setAlignment(Align.left);
        editButton.aFloat = 0;

        final MBTextField bonusMod = new MBTextField("0", screen, true, true);
        bonusMod.setSize(20, getHeight() - 10);
        bonusMod.setPosition(getX() + getWidth() - bonusMod.getWidth() - 5, getY() + 5);
        bonusMod.setVisible(false);

        bonusMod.setClosingAction(new Action() {
            @Override
            public boolean act(float v) {
                mod = Integer.parseInt(bonusMod.getText());

                modLabel.setText(String.valueOf(mod));

                bonusMod.setVisible(false);
                modLabel.setVisible(true);
                editMode = false;
                return false;
            }
        });

        editButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(!editMode) {
                    bonusMod.setText(String.valueOf(mod));
                    bonusMod.setVisible(true);
                    modLabel.setVisible(false);
                    editMode = true;
                }
            }
            @Override
            public boolean handle (Event event) {
                //if the mouse is hovered over the button...
                if(editButton.getButton().isOver()){
                    editButton.aFloat = 1;
                }
                else editButton.aFloat = 0;
                if (!(event instanceof ChangeEvent)) return false;
                changed((ChangeEvent)event, event.getTarget());
                return false;
            }
        });

        add(nameLabel);
        add(editButton);
        add(bonusMod);
        add(modLabel);
    }
}

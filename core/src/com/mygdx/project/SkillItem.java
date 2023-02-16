package com.mygdx.project;

import com.badlogic.gdx.math.Rectangle;

public class SkillItem extends Item{
    private String skill = "";

    public SkillItem(Screen screen, String skill) {
        super("assets\\Panels\\MiniItemPanel.png", screen);
        this.skill = skill;
    }
    public SkillItem(Rectangle position, Screen screen, String skill) {
        super("assets\\Panels\\MiniItemPanel.png", position, screen);
        this.skill = skill;
    }

    public void initialize() {
        MBLabel nameLabel;
        nameLabel = new MBLabel(skill, screen);
        nameLabel.setPosition(this.getX()+5, this.getY()+5);
//        nameLabel.setSize(119, nameLabel.getHeight());


        add(nameLabel);
    }
}

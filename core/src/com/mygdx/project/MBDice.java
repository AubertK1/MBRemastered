package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class MBDice extends MBButton{
    private Button dice;

    public MBDice(Screen screen) {
        super(screen);

        toImageButton(new Texture("assets\\Images\\diceIcon.png"));
        dice = getButton();
    }
}

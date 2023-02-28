package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Random;

public class MBDice extends MBButton{
    private Button dice;

    private Random random = new Random();

    public MBDice(Screen screen) {
        super(screen);

        toImageButton(new Texture("assets\\Images\\diceIcon.png"));
        dice = getButton();

//        addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent changeEvent, Actor actor) {
//                System.out.println("Dice Clicked");
//            }
//        });
    }

    public int roll(int dice){
        return random.nextInt(dice) + 1;
    }
}

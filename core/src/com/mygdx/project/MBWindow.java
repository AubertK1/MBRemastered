package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Window which features the close button in top right corner (button moved outside of the window bounds).
 *
 * @author serhiy
 */
public class MBWindow extends MBComponent{
    Window window;
    Image image;
    float inWidth;
    float inHeight;
    float gap;
    /**
     * Default constructor.
     */
    public MBWindow(Texture texture, Screen screen, MBComponent parent) {
        super(screen);
        parentActor = parent;
        parentActor.hasWindow = true;
        focused = true;

        window = new Window("", skin);
        image = new Image(texture);

        inHeight = 700;
        inWidth = getScaledWidth(image.getHeight(), image.getWidth(), inHeight);
        image.setWidth(inWidth);
        image.setHeight(inHeight);
        window.add(image);

        gap = window.getTitleTable().getPrefHeight();
        window.setClip(false);
        window.setTransform(true);
        window.setResizable(true);
        window.setKeepWithinStage(true);
        final Button closeButton = new ImageButton(skin, "delete-button");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parentActor.hasWindow = false;
                ((MBComponent)getMBParent()).delete(MBWindow.this);
            }
        });
        final Button resizeButton = new ImageButton(skin, "edit-button");
        resizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.setWidth(inWidth);
                window.setHeight(inHeight);
                window.setPosition((float) Gdx.graphics.getWidth()/2 - window.getWidth()/2, (float)Gdx.graphics.getHeight()/2 - window.getHeight()/2);
            }
        });
        final Button reaspectButton = new ImageButton(skin, "edit-button");
        reaspectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.setHeight(image.getHeight()+gap);
                window.setWidth(getScaledWidth(inHeight, inWidth, image.getHeight()));
            }
        });
        window.getTitleTable().add(reaspectButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
        window.getTitleTable().add(resizeButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
        window.getTitleTable().add(closeButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
    }
    public MBWindow(MBComponent parent, MainScreen mainScreen, boolean diceWindow){
        super(mainScreen);
        parentActor = parent;
        parentActor.hasWindow = true;
        focused = true;

        window = new Window("DICE", skin);

        MBDice dice = new MBDice(mainScreen);
        //region row 1
        MBButton d4 = new MBButton("d4", mainScreen);
        MBButton d6 = new MBButton("d6", mainScreen);
        MBButton d8 = new MBButton("d8", mainScreen);
        MBButton d12 = new MBButton("d12", mainScreen);
        MBButton d20 = new MBButton("d20", mainScreen);
        MBTextField dCustom = new MBTextField("d100", mainScreen);
        dCustom.setAlignment(Align.center);
        //endregion
        //region row 2
        MBTextField d4Num = new MBTextField("1", mainScreen);
        d4Num.setAlignment(Align.center);
        MBTextField d6Num = new MBTextField("1", mainScreen);
        d6Num.setAlignment(Align.center);
        MBTextField d8Num = new MBTextField("1", mainScreen);
        d8Num.setAlignment(Align.center);
        MBTextField d12Num = new MBTextField("1", mainScreen);
        d12Num.setAlignment(Align.center);
        MBTextField d20Num = new MBTextField("1", mainScreen);
        d20Num.setAlignment(Align.center);
        MBTextField dCustomNum = new MBTextField("1", mainScreen);
        dCustomNum.setAlignment(Align.center);
        //endregion

        MBButton test4 = new MBButton(mainScreen);
        MBButton test5 = new MBButton(mainScreen);
        MBButton test6 = new MBButton(mainScreen);
        MBSelectBox playerBox = new MBSelectBox(mainScreen);
        playerBox.setItems(mainScreen.screens.get(0).screenDropdown.getItems());
        MBTextField bonusMod = new MBTextField("Bonus Mod", mainScreen);
        MBButton test7 = new MBButton(mainScreen);
        MBButton test8 = new MBButton(mainScreen);

        window.add(d4.getActor()).expandX().fill();
        window.add(d6.getActor()).expandX().fill();
        window.add(d8.getActor()).expandX().fill();
        window.add(d12.getActor()).expandX().fill();
        window.add(d20.getActor()).expandX().fill();
        window.add(dCustom.getActor()).expandX().fill().width(50);
        window.row();
        window.add(d4Num.getActor()).width(49).fill();
        window.add(d6Num.getActor()).width(49).fill();
        window.add(d8Num.getActor()).width(49).fill();
        window.add(d12Num.getActor()).width(49).fill();
        window.add(d20Num.getActor()).width(49).fill();
        window.add(dCustomNum.getActor()).width(50).fill();
        window.row();
        window.add(test4.getActor()).fill().colspan(2);
        window.add(dice.getActor()).width(90).height(90).expand().colspan(2);
        window.add(test5.getActor()).fill().colspan(2);
        window.row();
//        window.add(test6.getActor()).fill().colspan(2);
        window.add(playerBox.getActor()).width(90).fill().colspan(6);
        window.row();
        window.add(bonusMod.getActor()).width(90).fill().colspan(6);
//        window.add(test8.getActor()).fill().colspan(2);

        window.setClip(false);
        window.setTransform(true);
        window.setResizable(false);
        window.setKeepWithinStage(true);
        final Button closeButton = new ImageButton(skin, "delete-button");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parentActor.hasWindow = false;
                ((MBComponent)getMBParent()).delete(MBWindow.this);
            }
        });
        window.getTitleTable().add(closeButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
    }

    public Actor getActor(){
        return window;
    }
    public Actor getImage(){
        return image;
    }
    private float getScaledWidth(float height, float width, float scaledHeight){
        double scale = (double) scaledHeight / (double) height;
        float scaledWidth = (float)(width * scale);
        return scaledWidth;
    }
}

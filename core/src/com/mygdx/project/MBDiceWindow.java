package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class MBDiceWindow extends MBWindow{
    //region row 1
    MBButton d4;
    MBButton d6;
    MBButton d8;
    MBButton d12;
    MBButton d20;
    MBTextField dCustom;
    //endregion
    //region row 2
    MBTextField d4Num;
    MBTextField d6Num;
    MBTextField d8Num;
    MBTextField d12Num;
    MBTextField d20Num;
    MBTextField dCustomNum;
    //endregion
    //region row 3
    MBSelectBox typeBox;

    MBDice dice;

    MBSelectBox statsBox;
    MBSelectBox skillsBox;
    MBSelectBox savesBox;
    MBSelectBox atksBox;
    //endregion
    //region row 4
    MBLabel rollLabel;
    //endregion
    //region row 5
    MBSelectBox playerBox;

    //endregion
    //region row 6
    MBTextField bonusModTF;
    MBLabel bonusModL;
    //endregion

    private int rolledDice = 20;
    private int numberOfDice = 1;
    public MBDiceWindow(MBComponent parent, MainScreen mainScreen, boolean diceWindow) {
        super(parent, mainScreen, diceWindow);
        parentActor = parent;
        parentActor.hasWindow = true;
        focused = true;

        window = new Window("DICE", skin);
        //region row 1
        d4 = new MBButton("d4", mainScreen, "toggle");
        d6 = new MBButton("d6", mainScreen, "toggle");
        d8 = new MBButton("d8", mainScreen, "toggle");
        d12 = new MBButton("d12", mainScreen, "toggle");
        d20 = new MBButton("d20", mainScreen, "toggle");
        d20.getButton().setChecked(true);
        dCustom = new MBTextField("d100", mainScreen);
        dCustom.setAlignment(Align.center);
        //endregion
        //region row 2
        d4Num = new MBTextField("1", mainScreen);
        d4Num.setAlignment(Align.center);
        d6Num = new MBTextField("1", mainScreen);
        d6Num.setAlignment(Align.center);
        d8Num = new MBTextField("1", mainScreen);
        d8Num.setAlignment(Align.center);
        d12Num = new MBTextField("1", mainScreen);
        d12Num.setAlignment(Align.center);
        d20Num = new MBTextField("1", mainScreen);
        d20Num.setAlignment(Align.center);
        dCustomNum = new MBTextField("1", mainScreen);
        dCustomNum.setAlignment(Align.center);
        //endregion
        //region row 3
        typeBox = new MBSelectBox(mainScreen);
        typeBox.setItems("Stat", "Skill", "Saving Throw", "Attack");
        typeBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String word = typeBox.getSelected();
                switch (word) {
                    case "Stat":
                        reformat(0);
                        break;
                    case "Skill":
                        reformat(1);
                        break;
                    case "Saving Throw":
                        reformat(2);
                        break;
                    case "Attack":
                        reformat(3);
                        break;
                }
            }
        });
        typeBox.setInWindow(true);
        dice = new MBDice(mainScreen);

        statsBox = new MBSelectBox(mainScreen);
        statsBox.setItems("Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma");
        statsBox.setInWindow(true);
        skillsBox = new MBSelectBox(mainScreen);
        skillsBox.setItems("A", "B", "C");
        skillsBox.setInWindow(true);
        savesBox = new MBSelectBox(mainScreen);
        savesBox.setItems("Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma");
        savesBox.setInWindow(true);
        atksBox = new MBSelectBox(mainScreen);
        atksBox.setItems("Weapon 1", "Weapon 2");
        atksBox.setInWindow(true);
        //endregion
        //region row 4
        rollLabel = new MBLabel("Result: ", mainScreen);
        rollLabel.setAlignment(Align.top);
        //endregion
        //region row 5
        playerBox = new MBSelectBox(mainScreen);
        Array<String> players = new Array<>(mainScreen.screens.get(0).screenDropdown.getItems());
        players.removeIndex(players.size - 1);
        playerBox.setItems(players);
        playerBox.setInWindow(true);
        //endregion
        //region row 6
        bonusModL = new MBLabel("Bonus Modifier: ", mainScreen);
        bonusModL.setAlignment(Align.center);
        bonusModTF = new MBTextField("", mainScreen);
        //endregion

        //region dice buttons
        final MBButton[] diceButtons = new MBButton[]{d4, d6, d8, d12, d20};
        final String[] numOfDices = new String[]{d4Num.getText(), d6Num.getText(), d8Num.getText(), d12Num.getText(), d20Num.getText()};

        for (int i = 0; i < diceButtons.length; i++) {
            final int finalI = i;
            diceButtons[i].addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if(diceButtons[finalI].getButton().isChecked()) {
                        if (finalI == 0) rolledDice = 4;
                        else if (finalI == 1) rolledDice = 6;
                        else if (finalI == 2) rolledDice = 8;
                        else if (finalI == 3) rolledDice = 12;
                        else rolledDice = 20;

                        for (int j = 0; j < diceButtons.length; j++) {
                            //unchecks all the other buttons
                            if (j != finalI) diceButtons[j].getButton().setChecked(false);
                        }
                    }
                    else {
                        for (int j = 0; j < diceButtons.length; j++) {
                            if(diceButtons[j].getButton().isChecked()) break;
                            else if(j == diceButtons.length - 1) rolledDice = 100;
                        }
                    }
                }
            });
        }
        //endregion

        dice.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(dice.roll(20));
                rollLabel.setText("Result: " + dice.roll(rolledDice));
                return true;
            }
        });

        format();

        window.setClip(false);
        window.setTransform(true);
        window.setResizable(false);
        window.setKeepWithinStage(true);
        final Button closeButton = new ImageButton(skin, "delete-button");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parentActor.hasWindow = false;
                ((MBComponent)getMBParent()).delete(MBDiceWindow.this);
            }
        });
        window.getTitleTable().add(closeButton).size(20, 15).padRight(5.5f).padTop(0).padBottom(0);
    }
    private void format(){
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
        window.add(typeBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        window.add(dice.getActor()).width(90).height(90).expand().colspan(2).padTop(30);
        window.add(statsBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        window.row();
        window.add(rollLabel.getActor()).height(56).fill().colspan(6);
        window.row();
        window.add(bonusModL.getActor()).fill().colspan(3).padBottom(10);
        window.add(bonusModTF.getActor()).width(95).left().colspan(3).padBottom(10);
        window.row();
        window.add(playerBox.getActor()).fill().colspan(6).padBottom(4);

        //region making sure selectboxes are on top
        Button actorLast = new Button(skin);
        Button actor2ndLast = new Button(skin);
        Button actor3rdLast = new Button(skin);
        window.addActor(actor3rdLast);
        window.addActor(actor2ndLast);
        window.addActor(actorLast);
        window.swapActor(playerBox.getActor(), actor3rdLast);
        window.swapActor(statsBox.getActor(), actor2ndLast);
        window.swapActor(typeBox.getActor(), actorLast);
        window.removeActor(actor3rdLast);
        window.removeActor(actor2ndLast);
        window.removeActor(actorLast);
        //endregion
    }
    private void reformat(int typeIndex){
        window.clearChildren();
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
        window.add(typeBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        window.add(dice.getActor()).width(90).height(90).expand().colspan(2).padTop(30);
        if(typeIndex == 0) window.add(statsBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        else if(typeIndex == 1) window.add(skillsBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        else if(typeIndex == 2) window.add(savesBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        else if(typeIndex == 3) window.add(atksBox.getActor()).width(95).fillX().colspan(2).padTop(30);
        window.row();
        window.add(rollLabel.getActor()).height(56).fill().colspan(6);
        window.row();
        window.add(bonusModL.getActor()).fill().colspan(3).padBottom(10);
        window.add(bonusModTF.getActor()).width(95).left().colspan(3).padBottom(10);
        window.row();
        window.add(playerBox.getActor()).fill().colspan(6).padBottom(4);

        //region making sure selectboxes are on top
        Button actorLast = new Button(skin);
        Button actor2ndLast = new Button(skin);
        Button actor3rdLast = new Button(skin);
        window.addActor(actor3rdLast);
        window.addActor(actor2ndLast);
        window.addActor(actorLast);
        window.swapActor(playerBox.getActor(), actor3rdLast);
        if(typeIndex == 0) window.swapActor(statsBox.getActor(), actor2ndLast);
        else if(typeIndex == 1) window.swapActor(skillsBox.getActor(), actor2ndLast);
        else if(typeIndex == 2) window.swapActor(savesBox.getActor(), actor2ndLast);
        else if(typeIndex == 3) window.swapActor(atksBox.getActor(), actor2ndLast);
        window.swapActor(typeBox.getActor(), actorLast);
        window.removeActor(actor3rdLast);
        window.removeActor(actor2ndLast);
        window.removeActor(actorLast);
        //endregion
    }

    @Override
    public void render(){
        typeBox.setInWindow(true);
        statsBox.setInWindow(true);
        skillsBox.setInWindow(true);
        savesBox.setInWindow(true);
        atksBox.setInWindow(true);
        playerBox.setInWindow(true);

        super.render();
    }
}

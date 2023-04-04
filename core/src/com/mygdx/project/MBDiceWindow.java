package com.mygdx.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;

public class MBDiceWindow extends MBWindow{
    //region row 1
    MBButton d4;
    MBButton d6;
    MBButton d8;
    MBButton d12;
    MBButton d20;
    MBLabel dCustomL;
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
    String[] selectedList = new String[0];
    MBSelectBox selectedBox;
    Screen selectedScreen;
    public MBDiceWindow(MBComponent parent, final MainScreen mainScreen, boolean diceWindow) {
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
        dCustomL = new MBLabel("d:", mainScreen);
        dCustomL.setAlignment(Align.left);
        dCustom = new MBTextField("100", mainScreen);
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
        typeBox.setItems("Stat", "Skill", "Saving Throw", "Attack", "None");
        typeBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String word = typeBox.getSelected();
                switch (word) {
                    case "Stat":
                        reformat(0);
                        selectedList = Stats.basestats;
                        selectedBox = statsBox;
                        break;
                    case "Skill":
                        reformat(1);
                        selectedList = Stats.skills;
                        selectedBox = skillsBox;
                        break;
                    case "Saving Throw":
                        reformat(2);
                        selectedList = Stats.saves;
                        selectedBox = savesBox;
                        break;
                    case "Attack":
                        reformat(3);
                        selectedBox = statsBox;
                        break;
                    case "None":
                        reformat(4);
                        selectedBox = null;
                        break;
                }
            }
        });
        typeBox.setInWindow(true);
        dice = new MBDice(mainScreen);

        statsBox = new MBSelectBox(mainScreen);
        statsBox.setItems(Stats.basestats);
        statsBox.setInWindow(true);
        skillsBox = new MBSelectBox(mainScreen);
        skillsBox.setItems(Stats.skills);
        skillsBox.setInWindow(true);
        savesBox = new MBSelectBox(mainScreen);
        savesBox.setItems(Stats.saves);
        savesBox.setInWindow(true);
        atksBox = new MBSelectBox(mainScreen);
        atksBox.setItems("Weapon 1", "Weapon 2");
        atksBox.setInWindow(true);

        selectedBox = statsBox;
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
        playerBox.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedScreen = mainScreen.getScreenByName(playerBox.getSelected());
            }
        });

        selectedScreen = mainScreen.getScreenByName(playerBox.getSelected());
        //endregion
        //region row 6
        bonusModL = new MBLabel("Bonus Modifier: ", mainScreen);
        bonusModL.setAlignment(Align.center);
        bonusModTF = new MBTextField("", mainScreen);
        //endregion

        //region dice buttons
        final MBButton[] diceButtons = new MBButton[]{d4, d6, d8, d12, d20};
        final MBTextField[] numOfDiceTFs = new MBTextField[]{d4Num, d6Num, d8Num, d12Num, d20Num, dCustomNum};

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

                        numberOfDice = findNumber(numOfDiceTFs[finalI].getText());

                        for (int j = 0; j < diceButtons.length; j++) {
                            //unchecks all the other buttons
                            if (j != finalI) diceButtons[j].getButton().setChecked(false);
                        }
                    }
                    else {
                        for (int j = 0; j < diceButtons.length; j++) {
                            if(diceButtons[j].getButton().isChecked()) break;
                            else if(j == diceButtons.length - 1){
                                rolledDice = findNumber(dCustom.getText());
                                numberOfDice = findNumber(dCustomNum.getText());
                            }
                        }
                    }
                }
            });
        }
        //endregion

        //region tf listeners
        dCustom.setKeyListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                for (int i = 0; i < diceButtons.length; i++) {
                    if(diceButtons[i].getButton().isChecked()) break;
                    else if(i == diceButtons.length - 1) rolledDice = findNumber(dCustom.getText());
                }
            }
        });
        for (int i = 0; i < numOfDiceTFs.length; i++) {
            final int finalI = i;
            numOfDiceTFs[i].setKeyListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    try {
                        if (diceButtons[finalI].getButton().isChecked())
                            numberOfDice = findNumber(numOfDiceTFs[finalI].getText());
                    } catch (ArrayIndexOutOfBoundsException a){
                        numberOfDice = findNumber(numOfDiceTFs[finalI].getText());
                    }
                }
            });
        }
        //endregion

        dice.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int totalRoll = 0;
                int natRoll = 0;
                ArrayList<Integer> rolls = new ArrayList<>();
                ArrayList<Integer> mods = new ArrayList<>();
                for (int i = 1; i <= numberOfDice; i++) {
                    if(rolledDice < 1) {
                        totalRoll = 0;
                        natRoll = 0;
                        break;
                    }
                    int roll = dice.roll(rolledDice);
                    totalRoll += roll;
                    natRoll += roll;
                    rolls.add(roll);
                }
                if(selectedBox != null) {
                    int stat1 = Stats.statIndexToStat(selectedList, selectedBox.dropdown.getSelectedIndex());
                    int mod1 = (int) selectedScreen.getStats().getValue(stat1);
                    mods.add(mod1);
                }
                mods.add(Stats.findNumber(bonusModTF.getText()));

                String[] stringMods = new String[mods.size()];
                for (int i = 0; i < stringMods.length; i++) {
                    totalRoll += mods.get(i);
                    stringMods[i] = mods.get(i) > 0 ? "+" + mods.get(i) : String.valueOf(mods.get(i));
                }
//                mods.add(Values.findNumber());
                rollLabel.setText("Result: " + (natRoll > 0 ? totalRoll  + "\n" + rolls + "\n" + Arrays.toString(stringMods) : -1));
                return true;
            }
        });

        format();

        window.setClip(false);
        window.setTransform(true);
        window.setResizable(false);
        window.setKeepWithinStage(false);
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
        window.add(dCustomL.getActor()).expandX().fill().width(15);
        window.add(dCustom.getActor()).expandX().fill().width(35);
        window.row();
        window.add(d4Num.getActor()).width(49).fill();
        window.add(d6Num.getActor()).width(49).fill();
        window.add(d8Num.getActor()).width(49).fill();
        window.add(d12Num.getActor()).width(49).fill();
        window.add(d20Num.getActor()).width(49).fill();
        window.add(dCustomNum.getActor()).width(50).fill().colspan(2);
        window.row();
        window.add(typeBox.getActor()).width(95).fillX().colspan(2).padTop(35);
        window.add(dice.getActor()).width(90).height(90).expand().colspan(2).padTop(35);
        window.add(statsBox.getActor()).width(95).fillX().colspan(3).padTop(35);
        window.row();
        window.add(rollLabel.getActor()).height(66).fill().colspan(7);
        window.row();
        window.add(bonusModL.getActor()).fill().colspan(3).padBottom(5);
        window.add(bonusModTF.getActor()).width(95).left().colspan(4).padBottom(5);
        window.row();
        window.add(playerBox.getActor()).fill().colspan(7).padBottom(4);

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
        window.add(dCustomL.getActor()).expandX().fill().width(15);
        window.add(dCustom.getActor()).expandX().fill().width(35);
        window.row();
        window.add(d4Num.getActor()).width(49).fill();
        window.add(d6Num.getActor()).width(49).fill();
        window.add(d8Num.getActor()).width(49).fill();
        window.add(d12Num.getActor()).width(49).fill();
        window.add(d20Num.getActor()).width(49).fill();
        window.add(dCustomNum.getActor()).width(50).fill().colspan(2);
        window.row();
        window.add(typeBox.getActor()).width(95).fillX().colspan(2).padTop(35);
        window.add(dice.getActor()).width(90).height(90).expand().colspan(2).padTop(35);
        if(typeIndex == 0) window.add(statsBox.getActor()).width(95).fillX().colspan(3).padTop(35);
        else if(typeIndex == 1) window.add(skillsBox.getActor()).width(95).fillX().colspan(3).padTop(35);
        else if(typeIndex == 2) window.add(savesBox.getActor()).width(95).fillX().colspan(3).padTop(35);
        else if(typeIndex == 3) window.add(atksBox.getActor()).width(95).fillX().colspan(3).padTop(35);
        else if(typeIndex == 4) ;
        window.row();
        window.add(rollLabel.getActor()).height(66).fill().colspan(7);
        window.row();
        window.add(bonusModL.getActor()).fill().colspan(3).padBottom(5);
        window.add(bonusModTF.getActor()).width(95).left().colspan(4).padBottom(5);
        window.row();
        window.add(playerBox.getActor()).fill().colspan(7).padBottom(4);

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

    private int findNumber(String text){
        return Stats.findNumber(text);
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

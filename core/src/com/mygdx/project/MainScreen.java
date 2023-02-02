package com.mygdx.project;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainScreen extends Screen{

    //creating main panels
    Panel sidePanel, toolbarPanel;

    public MainScreen() {
        super();
        sidePanel = new Panel("assets\\Panels\\SidecardPanel.png",
                new Rectangle(2, 150, 98, 850), this);
        toolbarPanel = new Panel("assets\\Panels\\ToolbarPanel.png",
                new Rectangle(2, 2, 1916, 138), this);

        mainPanels.add(sidePanel);
        mainPanels.add(toolbarPanel);

        sidePanel.setSoftVisible(true);
        toolbarPanel.setSoftVisible(true);

        //region Tool Bar
        final MBButton focusButton = new MBButton("FOCUS", this);
        focusButton.setPosition(toolbarPanel.getX() + 10, toolbarPanel.getY() + 10);
        focusButton.setSize(toolbarPanel.getHeight()-20, toolbarPanel.getHeight()-20);
        focusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("FOCUSED");

                if(!inFocusMode){
                    focus();
                    inFocusMode = true;
                }
                else{
                    unfocus();
                    inFocusMode = false;
                }
            }
        });

        MBButton selectButton = new MBButton("Select", this);
        selectButton.setPosition(focusButton.getX() + focusButton.getWidth() + 10, toolbarPanel.getY() + 10);
//		selectButton.setPosition(toolbarPanel2.getX() + 10, toolbarPanel2.getY() + 10);
        selectButton.setSize(200, toolbarPanel.getHeight()-20);
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!Main.selectedScreen.masterBoard.board.isInSelectMode()){
                    Main.selectedScreen.masterBoard.board.enterSelectMode();
                }
            }
        });

        MBButton drawButton = new MBButton("Draw", this);
        drawButton.setPosition(selectButton.getX() + selectButton.getWidth() + 5, selectButton.getY());
        drawButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        drawButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!Main.selectedScreen.masterBoard.board.isInDrawMode()){
                    Main.selectedScreen.masterBoard.board.enterDrawMode();
                }
            }
        });

        MBButton eraseButton = new MBButton("Erase", this);
        eraseButton.setPosition(drawButton.getX() + drawButton.getWidth() + 5, selectButton.getY());
        eraseButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        eraseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!Main.selectedScreen.masterBoard.board.isInEraseMode()){
                    Main.selectedScreen.masterBoard.board.enterEraseMode();
                }
            }
        });

        final MBSelectBox sizesBox = new MBSelectBox(this);
        sizesBox.setPosition(eraseButton.getX() + eraseButton.getWidth() + 5, selectButton.getY());
        sizesBox.setSize(100, eraseButton.getHeight()/3 - 1);
        sizesBox.setItems("1", "3", "5", "11", "23", "45");
        sizesBox.dropdown.setSelected(String.valueOf(Main.selectedScreen.masterBoard.board.getCurrentBrush().brush.length));
        sizesBox.addScrollPaneListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                int newSize = Integer.parseInt(sizesBox.dropdown.getSelected());
                Main.selectedScreen.masterBoard.board.setBrush(newSize, Main.selectedScreen.masterBoard.board.isBrushSoft());
            }
        });

        final MBSelectBox softnessBox = new MBSelectBox(this);
        softnessBox.setPosition(sizesBox.getX(), sizesBox.getY() + sizesBox.getHeight() + 2);
        softnessBox.setSize(100, eraseButton.getHeight()/3 - 1);
        softnessBox.setItems("soft", "hard");
        softnessBox.addScrollPaneListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                Main.selectedScreen.masterBoard.board.setBrushSoft(softnessBox.dropdown.getSelected().equals("soft"));
            }
        });

        final MBSelectBox colorBox = new MBSelectBox(this);
        colorBox.setPosition(sizesBox.getX(), softnessBox.getY() + softnessBox.getHeight() + 2);
        colorBox.setSize(100, eraseButton.getHeight()/3 - 1);
        colorBox.setItems("BLACK", "WHITE", "RED", "YELLOW", "GREEN", "BLUE");
        colorBox.addScrollPaneListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                String color = colorBox.dropdown.getSelected();
                switch (color){
                    case "BLACK":
                        Main.selectedScreen.masterBoard.board.setCurrentColor(Color.BLACK);
                        Main.selectedScreen.masterBoard.board.setDrawingColor(Color.BLACK);
                        break;
                    case "WHITE":
                        Main.selectedScreen.masterBoard.board.setCurrentColor(Color.WHITE);
                        Main.selectedScreen.masterBoard.board.setDrawingColor(Color.WHITE);
                        break;
                    case "RED":
                        Main.selectedScreen.masterBoard.board.setCurrentColor(Color.RED);
                        Main.selectedScreen.masterBoard.board.setDrawingColor(Color.RED);
                        break;
                    case "YELLOW":
                        Main.selectedScreen.masterBoard.board.setCurrentColor(Color.YELLOW);
                        Main.selectedScreen.masterBoard.board.setDrawingColor(Color.YELLOW);
                        break;
                    case "GREEN":
                        Main.selectedScreen.masterBoard.board.setCurrentColor(Color.GREEN);
                        Main.selectedScreen.masterBoard.board.setDrawingColor(Color.GREEN);
                        break;
                    case "BLUE":
                        Main.selectedScreen.masterBoard.board.setCurrentColor(Color.BLUE);
                        Main.selectedScreen.masterBoard.board.setDrawingColor(Color.BLUE);
                        break;
                }
            }
        });

        MBColorPicker colorPicker = new MBColorPicker(this);
        colorPicker.setSize(0, toolbarPanel.getHeight() - 10);
        colorPicker.setPosition((toolbarPanel.getX() + toolbarPanel.getWidth()) - (colorPicker.getWidth() + 5), toolbarPanel.getY()+5);

        toolbarPanel.add(focusButton);
        toolbarPanel.add(selectButton);
        toolbarPanel.add(drawButton);
        toolbarPanel.add(eraseButton);
        toolbarPanel.add(sizesBox, 1);
        toolbarPanel.add(softnessBox, 1);
        toolbarPanel.add(colorBox, 1);
        toolbarPanel.add(colorPicker);
        //endregion
    }
}

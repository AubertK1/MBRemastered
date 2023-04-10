package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainScreen extends Screen{

    //creating main panels
    Panel sidePanel, toolbarPanel;

    ArrayList<Screen> screens = new ArrayList<>();

    Screen selectedScreen = null;
    HashMap<Integer, ArrayList<Renderable>> screenLayers = new HashMap<>();
    ArrayList<Panel> screenPanels = new ArrayList<>();

    public MainScreen() {
        super();

        grayPanel = new Panel("assets\\gradient2.png", new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), this);
        grayPanel.aFloat = .75f;

        sidePanel = new Panel("assets\\Panels\\SidecardPanel.png",
                new Rectangle(2, 150, 98, 850), this);
        toolbarPanel = new Panel("assets\\Panels\\ToolbarPanel.png",
                new Rectangle(2, 2, 1916, 138), this);

        mainPanels.add(sidePanel);
        mainPanels.add(toolbarPanel);
    }
    public void initialize(){
        //region Tool Bar
        final MBButton focusButton = new MBButton("FOCUS", this);
        focusButton.setPosition(toolbarPanel.getX() + 10, toolbarPanel.getY() + 10);
        focusButton.setSize(toolbarPanel.getHeight()-20, toolbarPanel.getHeight()-20);
        focusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if(!inFocusMode){
                    System.out.println("FOCUS");
                    focus();
                }
                else{
                    System.out.println("UNFOCUS");
                    unfocus();
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
                if (!selectedScreen.masterBoard.board.isInSelectMode()){
                    selectedScreen.masterBoard.board.enterSelectMode();
                }
            }
        });

        MBButton drawButton = new MBButton("Draw", this);
        drawButton.setPosition(selectButton.getX() + selectButton.getWidth() + 5, selectButton.getY());
        drawButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        drawButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!selectedScreen.masterBoard.board.isInDrawMode()){
                    selectedScreen.masterBoard.board.enterDrawMode();
                }
            }
        });

        MBButton eraseButton = new MBButton("Erase", this);
        eraseButton.setPosition(drawButton.getX() + drawButton.getWidth() + 5, selectButton.getY());
        eraseButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        eraseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!selectedScreen.masterBoard.board.isInEraseMode()){
                    selectedScreen.masterBoard.board.enterEraseMode();
                }
            }
        });

        final MBSelectBox sizesBox = new MBSelectBox(this);
        sizesBox.setPosition(eraseButton.getX() + eraseButton.getWidth() + 5, selectButton.getY());
        sizesBox.setSize(100, eraseButton.getHeight()/3 - 1);
        sizesBox.setItems("1", "3", "5", "11", "23", "45");
        sizesBox.dropdown.setSelected(String.valueOf(selectedScreen.masterBoard.board.getCurrentBrush().brush.length));
        sizesBox.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                int newSize = Integer.parseInt(sizesBox.dropdown.getSelected());
                selectedScreen.masterBoard.board.setBrush(newSize, selectedScreen.masterBoard.board.isBrushSoft());
            }
        });

        final MBSelectBox softnessBox = new MBSelectBox(this);
        softnessBox.setPosition(sizesBox.getX(), sizesBox.getY() + sizesBox.getHeight() + 2);
        softnessBox.setSize(100, eraseButton.getHeight()/3 - 1);
        softnessBox.setItems("soft", "hard");
        softnessBox.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                selectedScreen.masterBoard.board.setBrushSoft(softnessBox.dropdown.getSelected().equals("soft"));
            }
        });

        final MBSelectBox colorBox = new MBSelectBox(this);
        colorBox.setPosition(sizesBox.getX(), softnessBox.getY() + softnessBox.getHeight() + 2);
        colorBox.setSize(100, eraseButton.getHeight()/3 - 1);
        colorBox.setItems("BLACK", "WHITE", "RED", "YELLOW", "GREEN", "BLUE");
        colorBox.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                String color = colorBox.dropdown.getSelected();
                switch (color){
                    case "BLACK":
                        selectedScreen.masterBoard.board.setCurrentColor(Color.BLACK);
                        selectedScreen.masterBoard.board.setDrawingColor(Color.BLACK);
                        break;
                    case "WHITE":
                        selectedScreen.masterBoard.board.setCurrentColor(Color.WHITE);
                        selectedScreen.masterBoard.board.setDrawingColor(Color.WHITE);
                        break;
                    case "RED":
                        selectedScreen.masterBoard.board.setCurrentColor(Color.RED);
                        selectedScreen.masterBoard.board.setDrawingColor(Color.RED);
                        break;
                    case "YELLOW":
                        selectedScreen.masterBoard.board.setCurrentColor(Color.YELLOW);
                        selectedScreen.masterBoard.board.setDrawingColor(Color.YELLOW);
                        break;
                    case "GREEN":
                        selectedScreen.masterBoard.board.setCurrentColor(Color.GREEN);
                        selectedScreen.masterBoard.board.setDrawingColor(Color.GREEN);
                        break;
                    case "BLUE":
                        selectedScreen.masterBoard.board.setCurrentColor(Color.BLUE);
                        selectedScreen.masterBoard.board.setDrawingColor(Color.BLUE);
                        break;
                }
            }
        });

        MBButton diceButton = new MBButton("DICE", this);
        diceButton.setPosition(sizesBox.getRightX() + 5, selectButton.getY());
        diceButton.setSize(selectButton.getWidth() + 10, selectButton.getHeight());
        diceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Spawn DICE");
            }
        });
        diceButton.setupSelectImageImageButton();

        MBButton saveButton = new MBButton("SAVE", this);
        saveButton.setPosition(diceButton.getRightX() + 5, diceButton.getY());
        saveButton.setSize(selectButton.getWidth() + 10, selectButton.getHeight());
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveScreen(selectedScreen);
            }
        });

        MBButton loadButton = new MBButton("LOAD", this);
        loadButton.setPosition(saveButton.getRightX() + 5, saveButton.getY());
        loadButton.setSize(selectButton.getWidth() + 10, selectButton.getHeight());
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadScreen(selectedScreen);
            }
        });

        MBColorPicker colorPicker = new MBColorPicker(this);
        colorPicker.setSize(0, toolbarPanel.getHeight() - 10);
        colorPicker.setPosition(toolbarPanel.getRightX() - (colorPicker.getWidth() + 5), toolbarPanel.getY()+5);

        toolbarPanel.add(focusButton);
        toolbarPanel.add(selectButton);
        toolbarPanel.add(drawButton);
        toolbarPanel.add(eraseButton);
        toolbarPanel.add(sizesBox, 1);
        toolbarPanel.add(softnessBox, 1);
        toolbarPanel.add(colorBox, 1);
        toolbarPanel.add(diceButton);
        toolbarPanel.add(saveButton);
        toolbarPanel.add(loadButton);
        toolbarPanel.add(colorPicker);
        //endregion
    }

    public void focus(){
        //adding a new layer for every potential layer we may have
        int nonfocusedLayers = layers.size();
        for (int i = 0; i < nonfocusedLayers + 1; i++) {
            layers.put(nonfocusedLayers + i, new ArrayList<Renderable>());
            focusedLayers = i + 1;
        }

        //setting every focused (and that hasn't been brought forward yet) renderables' layer to its corresponding focusedLayer
        for (int layer = 0; layer < nonfocusedLayers; layer++) {
            //so that it's not updating the layers.layer's size/indexes while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layer));
            for (Renderable renderable : currentLayer) {
                if (renderable.isFocused()) {
                    renderable.setLayer(renderable.getLayer() + focusedLayers);
                }
            }
        }
        //removing nonfocused renderables from the focused layers
        for (int layer = layers.size() - 1; layer >= nonfocusedLayers; layer--) {
            //so that it's not updating the layers.layer's size/indexes while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layer));
            for (Renderable renderable : currentLayer) {
                if (!renderable.isFocused()) {
                    renderable.setLayer(renderable.getLayer() - focusedLayers);
                }
            }
        }

        grayPanel.setLayer(nonfocusedLayers);

        inFocusMode = true;
    }
    public void unfocus(){
        //setting every focused renderables' layer back to its original layer
        int nonfocusedLayers = layers.size()-focusedLayers;
        for (int layer = layers.size() - 1; layer >= nonfocusedLayers; layer--) {
            //so that it's not updating the layers.layer's size/indexes while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layer));
            for (Renderable renderable : currentLayer) {
                renderable.setLayer(renderable.getLayer() - focusedLayers);
            }
            if(layers.get(layer).size() == 0) layers.remove(layer); //deletes the layer when its empty
        }

        for (int screenLayer = screenLayers.size() - 1; screenLayer > 0; screenLayer--) {
            if(screenLayers.get(screenLayer).size() == 0) screenLayers.remove(screenLayer);
        }

        focusedLayers = 0;

        inFocusMode = false;
    }

    public void update(){
        screenLayers.clear();
        screenLayers.putAll(selectedScreen.getLayers());

        for (int layer = 0; layer < screenLayers.size(); layer++) {
            if(layers.containsKey(layer)){ //if the layer exists
                //empties out the selected screen's renderables from the layer's list
                layers.get(layer).removeAll(selectedScreen.getRenderables());
                //re-adds the renderables in their updated order
                layers.get(layer).addAll(screenLayers.get(layer));
            }
            else{
                //makes a new layer for the renderables
                addLayer(layer);
                //adds the renderables in their updated order
                layers.get(layer).addAll(screenLayers.get(layer));
            }
        }
    }

    public void addScreen(){
        addScreen(new PlayerScreen("New Player " + (screens.size())));
    }
    public void addScreen(Screen screen){
        if(screens.size() == 0){
            selectedScreen = screen;
            initialize();
        }

        screens.add(screen);

        screens.get(0).screenDropdown.insertItemA(screen.getName());

        setSelectedScreen(screen);
    }

    public Screen getScreenByName(String name){
        for (Screen screen: screens) {
            if(screen.getName().equals(name)) return screen;
        }

        return null;
    }

    public void setSelectedScreen(Screen screen){
        //removes the old screen's actors from the stage
        for (Panel panel: screenPanels) {
            panel.removeComps();
        }
        for (int layer = 0; layer < screenLayers.size(); layer++) {
            if (layers.containsKey(layer)) { //if the layer exists
                //empties out the old screen's renderables from the layer's list
                layers.get(layer).removeAll(selectedScreen.getRenderables());
            }
        }

        selectedScreen = screen;

        screenPanels.clear();
        screenPanels.addAll(screen.getMainPanels());

        //adds the new screen's actors to the stage
        for (Panel panel: screenPanels) {
            panel.reAddComps();
        }

        //making sure all the screens' are synced
        if(screens.size() > 0) {
//            screens.get(0).screenDropdown.dropdown.setSelected(screen.getName());
            syncScreens();
        }

        update();
        if(inFocusMode) focus();
    }

    public void syncScreens(){
        for (Screen screen0: screens) {
            //updates the amount of items in each dropdown
            screen0.screenDropdown.setItems(screens.get(0).screenDropdown.getItems());
            //makes sure the items in every dropdown has the correct name
            for (int i = 0; i < screens.size(); i++) {
                screen0.screenDropdown.getItems().set(i, screens.get(i).getName());
            }
            //sets the current screen to each dropdown's selected screen
            screen0.screenDropdown.dropdown.setSelected(selectedScreen.getName());
        }
        //region syncScreens() Again
        //runs the function again, so the dropdowns don't get out of sync
        for (Screen screen0: screens) {
            //updates the amount of items in each dropdown
            screen0.screenDropdown.setItems(screens.get(0).screenDropdown.getItems());
            //makes sure the items in every dropdown has the correct name
            for (int i = 0; i < screens.size(); i++) {
                screen0.screenDropdown.getItems().set(i, screens.get(i).getName());
            }
            //sets the current screen to each dropdown's selected screen
            screen0.screenDropdown.dropdown.setSelected(selectedScreen.getName());
        }
        //endregion
    }

    public Screen getSelectedScreen(){
        return selectedScreen;
    }

    public void saveScreen(Screen screen){
        screen.getStats().save();
    }
    public void loadScreen(Screen screen){
        //recovering the stats
        screen.getStats().load();
        for (Renderable tf: screen.getRenderables()) {
            if(tf instanceof MBTextField) {
                if(((MBTextField) tf).getAssignedStat() != -1){
                    //setting the text of the tfs
                    ((MBTextField) tf).setText(String.valueOf(screen.getStats().getValue(((MBTextField) tf).getAssignedStat())));
                    //updating any buttons or labels associated with the tf
                    ((MBTextField) tf).updateSystem();
                    //making sure all the screens are still synced
                    syncScreens();
                }
            }
            else if(tf instanceof MBTextArea) {
                if(((MBTextArea) tf).getAssignedStat() != -1){
                    ((MBTextArea) tf).setText(String.valueOf(screen.getStats().getValue(((MBTextArea) tf).getAssignedStat())));
                }
            }
        }
    }

    @Override
    public void render() {
        update();
        //rendering everything in layer 0 here, so they're rendered in order
        for (Panel panel : mainPanels) {
            panel.render();
        }
        for (Panel screenPanel: screenPanels) {
            screenPanel.render();
        }

        for (int layer = 1; layer < layers.size(); layer++) {
            for (int renderable = 0; renderable < layers.get(layer).size(); renderable++) {
                if(layers.get(layer).get(renderable).isSupposedToBeVisible()) {
                    layers.get(layer).get(renderable).render();
                }
            }
        }
    }

    @Override
    public void dispose() {
        for (Screen screen: screens) {
            screen.dispose();
        }
        super.dispose();
    }
}

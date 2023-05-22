package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MainScreen extends Screen{
    //the main panels
    private final Panel sidePanel, toolbarPanel, grayPanel;
    //this screen's renderables' layers renderable
    private HashMap<Integer, LinkedList<Renderable>> mainLayers = new HashMap<>();
    //all screens
    private ArrayList<Screen> screens = new ArrayList<>();
    //the selected screen's layers
    private HashMap<Integer, LinkedList<Renderable>> screenLayers = new HashMap<>();
    //the selected screen's main panels
    private ArrayList<Panel> screenPanels = new ArrayList<>();
    private Screen selectedScreen = null;

    public MainScreen() {
        super();

        sidePanel = new Panel("assets\\Panels\\SidecardPanel.png",
                new Rectangle(2, 150, 98, 850), this);
        toolbarPanel = new Panel("assets\\Panels\\ToolbarPanel.png",
                new Rectangle(2, 2, 1916, 138), this);

        mainPanels.add(sidePanel);
        mainPanels.add(toolbarPanel);

        //sets up the grayPanel for later
        grayPanel = new Panel("assets\\gradient2.png",
                new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), this);
        grayPanel.aFloat = .75f;

        initialize();
    }

    /**
     * initializes the panels and components
     */
    public void initialize(){
        //region Tool Bar
        //focus button
        final MBButton focusButton = new MBButton("FOCUS", this);
        focusButton.setPosition(toolbarPanel.getX() + 10, toolbarPanel.getY() + 10);
        focusButton.setSize(toolbarPanel.getHeight()-20, toolbarPanel.getHeight()-20);
        focusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!inFocusMode) focus();
                else unfocus();
            }
        });

        //region art buttons
        MBButton selectButton = new MBButton("Select", this);
        selectButton.setPosition(focusButton.getX() + focusButton.getWidth() + 10, toolbarPanel.getY() + 10);
        selectButton.setSize(200, toolbarPanel.getHeight()-20);
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedScreen.masterBoard.board.enterSelectMode();
            }
        });

        MBButton drawButton = new MBButton("Draw", this);
        drawButton.setPosition(selectButton.getX() + selectButton.getWidth() + 5, selectButton.getY());
        drawButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        drawButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedScreen.masterBoard.board.enterDrawMode();
            }
        });

        MBButton eraseButton = new MBButton("Erase", this);
        eraseButton.setPosition(drawButton.getX() + drawButton.getWidth() + 5, selectButton.getY());
        eraseButton.setSize(selectButton.getWidth(), selectButton.getHeight());
        eraseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedScreen.masterBoard.board.enterEraseMode();
            }
        });
        //endregion

        //region art dropdowns
        final MBSelectBox sizesBox = new MBSelectBox(this);
        sizesBox.setPosition(eraseButton.getX() + eraseButton.getWidth() + 5, selectButton.getY());
        sizesBox.setSize(100, eraseButton.getHeight()/3 - 1);
        sizesBox.setItems("1", "3", "5", "11", "23", "45");
        sizesBox.dropdown.setSelected(String.valueOf(11)); //sets the brush size to 11 by default
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
        //endregion

        //region save buttons
        MBButton saveButton = new MBButton("SAVE", this);
        saveButton.setPosition(sizesBox.getRightX() + 5, selectButton.getY());
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
        //endregion

        //color picker
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
        toolbarPanel.add(saveButton);
        toolbarPanel.add(loadButton);
        toolbarPanel.add(colorPicker);
        //endregion

        //loading in all the saved data
        onStart();
    }

    /**
     * loads in all saved data
     */
    public void onStart(){
        try {
            //grabs the stats folder's files
            File[] files = new File("assets\\SaveFiles\\stats").listFiles();

            if(files.length == 0) { //if the folder is empty just make a new screen
                addScreen(selectedScreen);
                loadScreen(selectedScreen);
            }
            else {
                //loops through each file
                for (File file : files) {
                    Stats s = new Stats();
                    //assigns this stat to this file
                    s.setToFile(file);
                    //loads the file's information into the stat
                    s.load();

                    //makes a new screen for the file
                    PlayerScreen screen = new PlayerScreen(String.valueOf(s.getValue(Stats.Stat.NAME)));
                    screen.setStats(s);
                    //adds the screen to this screen and loads it
                    addScreen(screen);
                    loadScreen(screen);
                }
            }

            setSelectedScreen(screens.get(0));
        } catch (NullPointerException n){
            System.out.println("Folder Not Found!");
        }
    }

    public void focus(){
        int nonfocusedLayers = layers.size();
        //doubles the number of layers + grayPanel layer
        for (int i = 0; i < nonfocusedLayers + 1; i++) {
            layers.put(nonfocusedLayers + i, new LinkedList<Renderable>());
            focusedLayers = i + 1;
        }

        //sets every focused (and that hasn't been brought forward yet) renderables' layer to its corresponding focusedLayer
        for (int layerI = 0; layerI < nonfocusedLayers; layerI++) {
            //so that the layers.layer list's size/indexes isn't getting updated while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layerI));
            for (Renderable renderable : currentLayer) {
                if (renderable.isFocused()) {
                    renderable.setLayer(renderable.getLayer() + focusedLayers);
                }
            }
        }
        //removing nonfocused renderables from the focused layers
        for (int layerI = layers.size() - 1; layerI >= nonfocusedLayers; layerI--) {
            //so that the layers.layer list's size/indexes isn't getting updated while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layerI));
            for (Renderable renderable : currentLayer) {
                if (!renderable.isFocused()) {
                    renderable.setLayer(renderable.getLayer() - focusedLayers);
                }
            }
        }

        grayPanel.setLayer(nonfocusedLayers);
        //forces the grayPanel to get rendered
        this.addRenderable(grayPanel);

        inFocusMode = true;
    }
    public void unfocus(){
        //sets every focused renderables' layer back to its original layer
        int nonfocusedLayers = layers.size()-focusedLayers;
        for (int layerI = layers.size() - 1; layerI >= nonfocusedLayers; layerI--) {
            //so that the layers.layer list's size/indexes isn't getting updated while looping through it
            ArrayList<Renderable> currentLayer = new ArrayList<>(layers.get(layerI));
            for (Renderable renderable : currentLayer) {
                renderable.setLayer(renderable.getLayer() - focusedLayers);
            }
            if(layers.get(layerI).size() == 0) layers.remove(layerI); //deletes the layer when its empty
        }

        //making sure every layer is deleted
        for (int screenLayerI = screenLayers.size() - 1; screenLayerI > 0; screenLayerI--) {
            if(screenLayers.get(screenLayerI).size() == 0) screenLayers.remove(screenLayerI);
        }

        focusedLayers = 0;
        //stops the grayPanel from being rendered
        this.removeRenderable(grayPanel);

        inFocusMode = false;
    }

    public void update(){
        //makes sure screenLayers is up-to-date
        screenLayers.clear();
        screenLayers.putAll(selectedScreen.getLayers());

        //makes sure mainLayers is up-to-date
        mainLayers.clear();
        for (int r = 0; r < getRenderables().size(); r++) {
            addToMainLayer(getRenderables().get(r));
        }

        //loops through the layers
        for (int layer = 0; layer < screenLayers.size(); layer++) {
            if(layers.containsKey(layer)){ //if the layer exists
                //empties out the layer
                layers.get(layer).clear();
            }
            else{
                //makes a new layer for the renderables
                addLayer(layer);
            }
            //adds/re-adds this screen's renderables in their updated order
            if(mainLayers.containsKey(layer))
                layers.get(layer).addAll(mainLayers.get(layer));
            //adds/re-adds the selected screen's renderables in their updated order
            if(screenLayers.containsKey(layer))
                layers.get(layer).addAll(screenLayers.get(layer));
        }
    }

    public void setSelectedScreen(Screen screen){
        //removes the old screen's components from the stage
        for (Panel panel: screenPanels) {
            panel.removeCompsFromStage();
        }
        for (int layerI = 0; layerI < screenLayers.size(); layerI++) {
            if (layers.containsKey(layerI)) { //if the layer exists...
                //empties out the old screen's renderables from the layer's list
                layers.get(layerI).removeAll(selectedScreen.getRenderables());
            }
        }

        selectedScreen = screen;

        screenPanels.clear();
        screenPanels.addAll(screen.getMainPanels());

        //adds the new screen's components to the stage
        for (Panel panel: screenPanels) {
            panel.addCompsToStage();
        }

        //syncs the screens again
        syncScreens();

        update();
        if(inFocusMode) focus();
    }

    public void addScreen(){
        //region finding the next open default name
        int nextScreenID = screens.size();
        boolean freeName = false;
        while (!freeName) {
            for (int i = 0; i < screens.size(); i++) {
                if(screens.get(i).getName().equals("PLAYER " + nextScreenID)) {
                    nextScreenID++;
                    break;
                }
                else if(i == screens.size() - 1) freeName = true;
            }
        }
        //endregion

        addScreen(new PlayerScreen("PLAYER " + nextScreenID));
    }
    public void addScreen(Screen screen){
        screens.add(screen);

        //adds this screen the player dropdown
        screens.get(0).screenDropdown.insertItemA(screen.getName());

        setSelectedScreen(screen);
    }
    public void deleteScreen(Screen screen){
        if(screens.size() == 1) return; //there cannot be less than one screen

        //removes the screen from lists and the player dropdown
        int index = screens.indexOf(screen);
        screens.remove(index);
        screens.get(0).screenDropdown.removeItem(index);

        //sets the selected screen to the screen above the deleted screen
        setSelectedScreen(index != 0 ? screens.get(index - 1) : screens.get(index));

        screen.delete();
    }

    public Screen getScreenByName(String name){
        for (Screen screen: screens) {
            if(screen.getName().equals(name)) return screen;
        }

        return null;
    }

    /**
     * syncs all the screens' dropdowns so all the values are the same
     */
    public void syncScreens(){
        for (Screen screen: screens) {
            //updates the amount of items in each dropdown
            screen.screenDropdown.setItems(screens.get(0).screenDropdown.getItems());
            //makes sure the items in every dropdown has the correct name
            for (int i = 0; i < screens.size(); i++) {
                screen.screenDropdown.getItems().set(i, screens.get(i).getName());
            }
            //sets the current screen to each dropdown's selected screen
            screen.screenDropdown.dropdown.setSelected(selectedScreen.getName());
        }
        //for some reason this doesn't work when called once, so it
        //runs the function again, so the dropdowns don't get out of sync
        //region syncScreens() again
        for (Screen screen: screens) {
            //updates the amount of items in each dropdown
            screen.screenDropdown.setItems(screens.get(0).screenDropdown.getItems());
            //makes sure the items in every dropdown has the correct name
            for (int i = 0; i < screens.size(); i++) {
                screen.screenDropdown.getItems().set(i, screens.get(i).getName());
            }
            //sets the current screen to each dropdown's selected screen
            screen.screenDropdown.dropdown.setSelected(selectedScreen.getName());
        }
        //endregion
    }

    private void addToMainLayer(Renderable r){
        int layer = r.getLayer();

        if(layer == -1); //doesn't add this to a list, so it doesn't get rendered
        else if(mainLayers.containsKey(layer)){ //if the layer already exists...
            if(!mainLayers.get(layer).contains(r))
                mainLayers.get(layer).add(r); //adds the panel to its new later
        }
        else{
            //adds in any new layers between the highest existing layer and this layer
            for (int newLayer = 0; newLayer <= layer; newLayer++) {
                if(!mainLayers.containsKey(newLayer)) mainLayers.put(newLayer, new LinkedList<Renderable>()); //creates a new layer
            }

            mainLayers.get(layer).add(r); //adds the panel to the new later
        }
    }

    public void saveScreen(@NotNull Screen screen){
        screen.save();
    }
    public void loadScreen(@NotNull Screen screen){
        //recovers the stats
        screen.load();
        //makes sure all the screens are still synced
        syncScreens();
    }

    public Screen getSelectedScreen(){
        return selectedScreen;
    }

    @Override
    public void render() {
        update();
        //renders everything in layer 0 here, so they're rendered in order
        for (Panel panel : mainPanels) {
            panel.render();
        }
        for (Panel screenPanel: screenPanels) {
            screenPanel.render();
        }

        //renders all the renderables with a non-zero layer
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

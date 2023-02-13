package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Screen implements Renderable{
    //Used to draw the panels
    SpriteBatch batch;
    //used to draw the MBComponents
    Stage stage;
    //the skin for the components
    Skin skin;

    public Panel grayPanel;
    //creating main panels
    Panel topPanel, genStatsPanel, reminderPanel, masterboardPanel;

    protected ArrayList<Panel> mainPanels = new ArrayList<>();
    boolean inFocusMode = false;
    int focusedLayers = 0;

    MBBoard masterBoard;
    //list with all the MBComponents
    ArrayList<MBComponent> allComps = new ArrayList<>();

    String name;
    MBSelectBox screenDropdown;

    //so these can be drawn last

    HashMap<Integer, ArrayList<Renderable>> layers = new HashMap<>();

    //weapons or spell items for the itempanel
    int itemTab = 1;

    //controls whether this is rendered or not
    boolean supposedToBeVisible = true;

    public Screen() {
        //        player = "PLAYER 1";
        //setting up batch, stage, and skin
        batch = Main.batch;
        stage = Main.stage;
        skin = Main.skin;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
       return name;
    }

    public void focus(){
        Main.getMainScreen().focus();
    }
    public void unfocus(){
        Main.getMainScreen().unfocus();
    }

    //region File Chooser
    public void fileChooseChanged(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //disabling input from the program so this is the only thing clickable
                InputProcessor processor = Gdx.input.getInputProcessor();
                Gdx.input.setInputProcessor(null);

                //makes only .jpg .png or .gif files able to be selected
                Main.chooser.setFileFilter(Main.filter);
                Main.chooser.setDialogTitle("Select Image");
                //initializing the frame
                Main.f.setVisible(true);
                Main.f.toFront();
                Main.f.setAlwaysOnTop(true);
                Main.f.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                Main.f.setVisible(false);
                //if the file is selected...
                int res = Main.chooser.showSaveDialog(Main.f);
                Main.f.dispose();
                if (res == JFileChooser.APPROVE_OPTION) {
                    //saves the selected file as a file
                    File file = Main.chooser.getSelectedFile();
                    //saves the file location as a string
                    Main.fileChooserPath = file.toString();
                }
                //re-enabling input
                Gdx.input.setInputProcessor(processor);
            }
        }).start();
    }
    public void fileChooseHandle(final Panel genStatsPanel, final MBButton imageButton){
        //to make sure this is only ran whenever the user selects a file
        if(Main.fileChooserPath != null) {
            Texture tex2 = new Texture(Main.fileChooserPath);
            //deletes the imageButton from the stage so that when it's added back it doesn't cause any complications in terms of the CompID
            genStatsPanel.delete(imageButton);
            //turns the imageButton into an ImageButton
            imageButton.toImageButton(tex2);
            imageButton.setupSelectImageImageButton();
            //adds the imageButton to the stage, so it's listener works
            genStatsPanel.add(imageButton);

            final MBButton reselectButton;
            reselectButton = new MBButton(this);
            reselectButton.setPosition(imageButton.getX()+10, imageButton.getY()+10);
            reselectButton.setSize(40, 40);
            reselectButton.aFloat = .75f;

            final MBButton deleteButton;
            deleteButton = new MBButton(this);
            deleteButton.setPosition(imageButton.getX()+60, imageButton.getY()+10);
            deleteButton.setSize(40, 40);
            deleteButton.aFloat = .75f;

            reselectButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    System.out.println("wassup");
                    fileChooseChanged();
                }
                @Override
                public boolean handle (Event event) {
                    if (!reselectButton.getButton().isOver()) {
                        deleteButton.aFloat = .75f;
                        reselectButton.aFloat = .75f;
                    }
                    else {
                        imageButton.aFloat = 1f;
                        deleteButton.aFloat = .5f;
                        reselectButton.aFloat = 1f;
                    }
                    if (!(event instanceof ChangeEvent)) return false;
                    changed((ChangeEvent)event, event.getTarget());
                    return false;
                }
            });
            deleteButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    System.out.println("wassup");
                    genStatsPanel.delete(imageButton);
                    imageButton.toTextButton("ADD IMAGE");
                    imageButton.setupSelectImageTextButton();
                    genStatsPanel.add(imageButton);
                }
                @Override
                public boolean handle (Event event) {
                    if (!deleteButton.getButton().isOver()) {
                        deleteButton.aFloat = .75f;
                        reselectButton.aFloat = .75f;
                    }
                    else {
                        imageButton.aFloat = 1f;
                        deleteButton.aFloat = 1f;
                        reselectButton.aFloat = .5f;
                    }
                    if (!(event instanceof ChangeEvent)) return false;
                    changed((ChangeEvent)event, event.getTarget());
                    return false;
                }
            });

            imageButton.add(deleteButton);
            imageButton.add(reselectButton);
            //so that it's not called again
            Main.fileChooserPath = null;
        }
    }
    //endregion

    @Override
    public void render() {
        //rendering everything in layer 0 here, so they're rendered in order
        for (Panel panel : mainPanels) {
            panel.render();
        }

        for (int layer = 1; layer < layers.size(); layer++) {
            for (int renderable = 0; renderable < layers.get(layer).size(); renderable++) {
                if(layers.get(layer).get(renderable).isSupposedToBeVisible()) {
                    layers.get(layer).get(renderable).render();
                }
            }
        }
    }

    /**
     * reassigns the compID variable for all the components
     */
    public void resetCompIDs(){
        for (int i = 0; i < allComps.size(); i++) {
            allComps.get(i).compID = i;
        }
    }

    public ArrayList<Panel> getMainPanels(){
        return mainPanels;
    }
    @Override
    public boolean isSupposedToBeVisible() {
        return supposedToBeVisible;
    }

    public void addLayer(int layer){
        //adds in any new layers between the highest existing layer and this layer
        for (int newLayer = layers.size(); newLayer <= layer; newLayer++) {
            if(!layers.containsKey(newLayer)) layers.put(newLayer, new ArrayList<Renderable>()); //creates a new layer
        }
    }
    @Override
    public void setLayer(int layer) {

    }

    @Override
    public int getLayer() {
        return 0;
    }

    public HashMap<Integer, ArrayList<Renderable>> getLayers() {
        return layers;
    }

    @Override
    public boolean isFocused() {
        return false;
    }
    public boolean isInFocusMode() {
        return inFocusMode;
    }

    public void dispose() {
        batch.dispose();
        for (Panel panel: mainPanels) {
            panel.dispose();
        }
        stage.dispose();
    }
}

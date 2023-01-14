package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import java.awt.*;
import java.util.ArrayList;

public class StickyNote extends GenOutline{
    private InputListener inputListener;
    private ClickListener clickListener;
    private StickyNoteStyle style;

    private final TextArea textArea;
    private final Vector2 TFOFFSET = new Vector2();
    /**Initialises the generator using the file location given.*/
    private final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("assets\\fonts\\doodlefont-normal.ttf"));
    private final FreeTypeFontParameter params = new FreeTypeFontParameter();
    private BitmapFont font;
    private boolean fontChanged = false;

    private final int MINWIDTH = 150;
    private final int MINHEIGHT = 150;

    private String text = "";


    public StickyNote(Board board, Skin skin, int x, int y) {
        this(board, skin.get(StickyNoteStyle.class), x, y);
    }
    public StickyNote(Board board, StickyNoteStyle style, int x, int y) {
        super(board);

        /**Sets the parameters of the object constant for the font, regardless of size.*/
        params.borderWidth = 0;
        params.borderColor = Color.DARK_GRAY;
        params.color = Color.BLACK;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        params.magFilter = Texture.TextureFilter.Nearest;
        params.minFilter = Texture.TextureFilter.Nearest;
        params.genMipMaps = true;
        params.size = 30;

        textArea = new TextArea("", style.textfield);
        textArea.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.RIGHT){
                    Main.contextMenu.setItems("Increase Size", "Decrease Size", "Blue");
                    Main.contextMenu.addListener(new ClickListener() {
                        public void clicked(InputEvent event, float x, float y) {
                            String word = Main.contextMenu.getSelected();
                            switch (word) {
                                case "Increase Size":
                                    setFontSize(params.size + 2);
                                    break;
                                case "Decrease Size":
                                    setFontSize(params.size - 2);
                                    break;
                                case "Blue":
                                    setFontColor(Color.BLUE);
                                    break;
                            }
                        }
                    });
                    Main.contextMenu.showAt((int) (x+getX() + TFOFFSET.x), (int) (y+getY() + TFOFFSET.y));
                }
                return false;
            }

        });

        setSize(300, 250);
        setPosition(x, y-getHeight());
        TFOFFSET.set(5, 5);

        setStyle(style);

        //setting bounds
        Rectangle rec = findBounds();
        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        format();
        bounds.setBounds(rec);
    }

    private void format(){
        float topGap = 40;
        textArea.setBounds(getX()+TFOFFSET.x, getY()+TFOFFSET.y, getWidth()-10, getHeight()-topGap);
        float length = textArea.getWidth() * textArea.getLinesShowing();
        textArea.setText(shortenString(text, length));
    }

    public void update(){
        Rectangle rec = findBounds();

        //detects if the outline is out of bounds
        if(isOutOfBounds()){
            //holding outline at border
            if(brokeLeftBounds()) setX(offsetX); //if at left border...
            if(brokeLowerBounds()) setY(offsetY); //if at bottom border...
            if(brokeRightBounds()) setX((offsetX + boardWidth) - getWidth()); //if at right border...
            if(brokeUpperBounds()) setY((offsetY + boardHeight) - getHeight()); //if at top border...

            //resets bounds after changing position
            rec = findBounds();
        }

        setPosition(rec.x, rec.y);
        setSize(rec.width, rec.height);
        format();
        bounds.setBounds(rec);
    }

    @Override
    public void drawContent(Batch batch) {
        if(isOutOfBounds()){
            update(); //fixing the outline
        }
        if(parentBoard.getSelectedOutline() == this && !activated) {
            activated = true;
            Main.stage.addActor(textArea);
        }
        else if(parentBoard.getSelectedOutline() != this && activated){
            activated = false;
            textArea.remove();
        }

        /**Generates the font using the generator object.*/
        if(!fontChanged) {
            if(getTextFieldStyle().font != null) getTextFieldStyle().font.dispose();
            font = generator.generateFont(params);
            getTextFieldStyle().font = font;
            fontChanged = true;
        }

        final Drawable background  = getBackgroundDrawable();

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        batch.setColor(color.r, color.g, color.b, color.a * 1);
        if (background != null) {
            background.draw(batch, x, y, width, height);
        }

        text = textArea.getText();
        textArea.draw(batch, 1);
    }

    public void resize(int x, int y){
        int border = onBorder(x, y);
        if(resize == -1) return;

        float x2 = x + offsetX;
        float y2 = y + offsetY;

        //so that everything moves relative to where it was
        if(lastx == -1) lastx = (int) x2;
        if(lasty == -1) lasty = (int) y2;
        float deltaX = x2-lastx;
        float deltaY = y2-lasty;

        //moving outline
        //moving left border
        if(border == 0 && !breakingLeftBounds(deltaX) && getWidth() - deltaX > MINWIDTH){
            setX(getX() + deltaX);
            setWidth(getWidth() - deltaX);
        }
        //moving top left border
        else if(border == 1){
            if(!breakingLeftBounds(deltaX) && getWidth() - deltaX > MINWIDTH) {
                setX(getX() + deltaX);
                setWidth(getWidth() - deltaX);
            }
            if(!breakingUpperBounds(deltaY) && getHeight() + deltaY > MINHEIGHT) {
                setY(getY());
                setHeight(getHeight() + deltaY);
            }
        }
        //moving top border
        else if(border == 2 && !breakingUpperBounds(deltaY) && getHeight() + deltaY > MINHEIGHT){
            setY(getY());
            setHeight(getHeight() + deltaY);
        }
        //moving top right border
        else if(border == 3){
            if(!breakingRightBounds(deltaX) && getWidth() + deltaX > MINWIDTH) {
                setWidth(getWidth() + deltaX);
                setX(getX());
            }
            if(!breakingUpperBounds(deltaY) && getHeight() + deltaY > MINHEIGHT) {
                setY(getY());
                setHeight(getHeight() + deltaY);
            }
        }
        //moving right border
        else if(border == 4 && !breakingRightBounds(deltaX) && getWidth() + deltaX > MINWIDTH){
            setWidth(getWidth() + deltaX);
            setX(getX());
        }
        //moving bottom right border
        else if(border == 5){
            if(!breakingRightBounds(deltaX) && getWidth() + deltaX > MINWIDTH) {
                setWidth(getWidth() + deltaX);
                setX(getX());
            }
            if(!breakingLowerBounds(deltaY) && getHeight() - deltaY > MINHEIGHT) {
                setY(getY() + deltaY);
                setHeight(getHeight() - deltaY);
            }
        }
        //moving bottom border
        else if(border == 6 && !breakingLowerBounds(deltaY) && getHeight() - deltaY > MINHEIGHT){
            setY(getY() + deltaY);
            setHeight(getHeight() - deltaY);
        }
        //moving bottom left border
        else if(border == 7){
            if(!breakingLeftBounds(deltaX) && getWidth() - deltaX > MINWIDTH) {
                setWidth(getWidth() - deltaX);
                setX(getX() + deltaX);
            }
            if(!breakingLowerBounds(deltaY) && getHeight() - deltaY > MINHEIGHT) {
                setY(getY() + deltaY);
                setHeight(getHeight() - deltaY);
            }
        }

        findBounds();
        format();

        lastx = (int) x2;
        lasty = (int) y2;
    }

    protected Rectangle findBounds() {
        Rectangle rec = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        LEFTBOUND = rec.x;
        RIGHTBOUND = rec.x + rec.width;
        LOWERBOUND = rec.y;
        UPPERBOUND = rec.y + rec.height;
        bounds.setBounds(rec);

        return rec;
    }

    @Override
    public void fix(){
        resize = -1;
        //update the selected outline's bounds
        update();
    }

    public void drag(int x, int y){
        if(resize != -1) return;

        float x2 = x + offsetX;
        float y2 = y + offsetY;

        //so that everything moves relative to where it was
        if(lastx == -1) lastx = (int) x2;
        if(lasty == -1) lasty = (int) y2;
        float deltaX = x2-lastx;
        float deltaY = y2-lasty;

        //moving outline
        if(breakingLeftBounds(deltaX)){ //testing left bounds
            setX(offsetX);
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if(breakingRightBounds(deltaX)){ //testing right bounds
            setX(offsetX+parentBoard.getWidth() - getWidth());
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            setX(getX()+(deltaX));
        }
        if(breakingLowerBounds(deltaY)){ //testing lower bounds
            setY(offsetY);
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if(breakingUpperBounds(deltaY)){ //testing upper bounds
            setY(offsetY+parentBoard.getHeight() - getHeight());
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            setY(getY()+(deltaY));
        }

        textArea.setPosition(textArea.getX() + deltaX, textArea.getY() + deltaY);

        lastx = (int) x2;
        lasty = (int) y2;
    }

    public void delete(){
        ArrayList<GenOutline> outlines = parentBoard.getOutlines();
        if(activated) textArea.remove();
        clear();
        outlines.remove(this);
    }

    public String shortenString(String str, float length){
        GlyphLayout layout = new GlyphLayout();
        String shortenedString;

        layout.setText(getTextFieldStyle().font, "...");
        float elipse = layout.width;

        for (int i = 0; i < str.length(); i++) {
            layout.setText(getTextFieldStyle().font, str.substring(0,i));
            if((layout.width+elipse) >= length){
                shortenedString = str.substring(0, i - 1);
                return shortenedString.trim() + "...";
            }
        }
        return str;
    }

    public void setFontSize(int size){
        StickyNoteStyle newStyle = new StickyNoteStyle(getStyle());
        params.size = size;
        font = generator.generateFont(params);
        newStyle.textfield.font = font;
        setStyle(newStyle);
    }

    public void setFontColor(Color color){
        StickyNoteStyle newStyle = new StickyNoteStyle(getStyle());
        params.color = color;
        newStyle.textfield.font = generator.generateFont(params);
        setStyle(newStyle);
    }

    public void setStyle (StickyNoteStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    public StickyNoteStyle getStyle(){
        return style;
    }

    protected @Null Drawable getOutlineDrawable() {
        return style.outline;
    }
    private @Null Drawable getBackgroundDrawable(){
        return style.background;
    }
    private @Null TextFieldStyle getTextFieldStyle(){
        return style.textfield;
    }

    static public class StickyNoteStyle{
        public @Null
        Drawable outline;
        Drawable background;
        TextFieldStyle textfield;

        public StickyNoteStyle(){
        }

        public StickyNoteStyle(StickyNoteStyle style){
            outline = style.outline;
            background = style.background;
            textfield = style.textfield;
        }
    }
}

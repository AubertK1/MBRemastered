package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
        float topGap = getHeight()/6f;
        textArea.setBounds(getX()+TFOFFSET.x, getY()+TFOFFSET.y, getWidth()-10, getHeight()-topGap);
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
            if(getTextFieldDrawable().font != null) getTextFieldDrawable().font.dispose();
            font = generator.generateFont(params);
            getTextFieldDrawable().font = font;
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

        textArea.draw(batch, 1);
    }

    protected Rectangle findBounds() {
        Rectangle rec = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        LEFTBOUND = rec.x;
        RIGHTBOUND = rec.x + rec.width;
        LOWERBOUND = rec.y;
        UPPERBOUND = rec.y + rec.height;

        return rec;
    }

    @Override
    public void fix(){
        //update the selected outline's bounds
        update();
    }

    public void drag(int x, int y){
        float x2 = x + offsetX;
        float y2 = y + offsetY;

        //so that everything moves relative to where it was
        if(lastx == -1) lastx = (int) x2;
        if(lasty == -1) lasty = (int) y2;
        float deltaX = x2-lastx;
        float deltaY = y2-lasty;

        //moving outline
        if((getX() <= offsetX && deltaX < 0)){ //testing left bounds
            setX(offsetX);
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((getX()+getWidth() >= offsetX+parentBoard.getWidth() && deltaX > 0)){ //testing right bounds
            setX(offsetX+parentBoard.getWidth() - getWidth());
            deltaX = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else{
            setX(getX()+(deltaX));
        }
        if((getY() <= offsetY && deltaY < 0)){ //testing lower bounds
            setY(offsetY);
            deltaY = 0; //if trying to go out of bounds, set so that the points can't move
        }
        else if((getY()+getHeight() >= offsetY+parentBoard.getHeight() && deltaY > 0)){ //testing upper bounds
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
    private @Null TextFieldStyle getTextFieldDrawable(){
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

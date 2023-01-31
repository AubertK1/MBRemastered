package com.mygdx.project;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ObjectSet;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;

public class ContextMenu<T> extends ScrollPane {
    static final Vector2 temp = new Vector2();
    int maxListCount;
    private final Vector2 stagePosition = new Vector2();
    final ListWrapper<T> list;

    final Array<T> items = new Array();
    private InputListener hideListener;
    private Actor previousScrollFocus;

    private List.ListStyle listStyle;
    boolean doShow;

    final ArraySelection<T> selection = new ArraySelection(items) {
        public boolean fireChangeEvent () {
            return super.fireChangeEvent();
        }
    };

    public ContextMenu (Skin skin) {
        this(skin.get("contextmenu", ScrollPaneStyle.class));
    }

    public ContextMenu (ScrollPaneStyle style) {
        super(null, style);

        selection.setActor(this);
        selection.setRequired(true);

        setOverscroll(false, false);
        setFadeScrollBars(false);
        setScrollingDisabled(true, false);
        listStyle = Main.skin.get("contextmenu", List.ListStyle.class);

        list = newList();
        list.setTouchable(Touchable.disabled);
        list.setTypeToSelect(true);
        setActor(list);

        list.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                T selected = list.getSelected();
                // Force clicking the already selected item to trigger a change event.
                if (selected != null) selection.items().clear(51);
                selection.choose(selected);
                hide();
            }

            public boolean mouseMoved (InputEvent event, float x, float y) {
                int index = list.getItemIndexAt(y);
                if (index != -1) list.setSelectedIndex(index);
                return true;
            }
        });

/*      //fixme if something with the listeners messes up...add this back in until further notice
        addListener(new InputListener() {
            public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                if (toActor == null || !isAscendantOf(toActor)) list.getSelection().set(getSelected());
            }
        });
*/

        hideListener = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Actor target = event.getTarget();
                if (isAscendantOf(target)) return false;
                list.getSelection().set(list.getItems().get(0));
                hide();
                return false;
            }

            public boolean keyDown (InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.NUMPAD_ENTER:
                    case Input.Keys.ENTER:
//                        selectBox.selection.choose(list.getSelected());
                        // Fall thru.
                    case Input.Keys.ESCAPE:
                        hide();
                        event.stop();
                        return true;
                }
                return false;
            }
        };
    }

    /** Allows a subclass to customize the select box list. The default implementation returns a list that delegates
     * {@link List#toString(Object)} to {@link SelectBoxWrapper#toString(Object)}. */
    protected ListWrapper<T> newList () {
        return new ListWrapper<T>(listStyle) {
            public String toString (T obj) {
                return obj.toString();
            }
        };
    }

    public void showAt (Stage stage, int x, int y) {
        if (items.size == 0) return;
        if (getStage() == null) return;
//        if (list.isTouchable()) return;

        doShow = true;
        stage.addActor(this);

        stage.addCaptureListener(hideListener);
        stage.addListener(list.getKeyListener());

        //fixme idk if I did this right
//        selectBox.localToStageCoordinates(stagePosition.set(0, 0));
//        localToStageCoordinates(stagePosition.set(0, 0));
        stagePosition.set(x, y);

        // Show the list above or below the select box, limited to a number of items and the available height in the stage.
        float itemHeight = list.getItemHeight();
        float height = itemHeight * (maxListCount <= 0 ? items.size : Math.min(maxListCount, items.size));
        Drawable scrollPaneBackground = getStyle().background;
        if (scrollPaneBackground != null) height += scrollPaneBackground.getTopHeight() + scrollPaneBackground.getBottomHeight();
        Drawable listBackground = list.getStyle().background;
        if (listBackground != null) height += listBackground.getTopHeight() + listBackground.getBottomHeight();

        float heightBelow = stagePosition.y;
        float heightAbove = stage.getHeight() - heightBelow;
        boolean below = true;
        if (height > heightBelow) {
            if (heightAbove > heightBelow) {
                below = false;
                height = Math.min(height, heightAbove);
            } else
                height = heightBelow;
        }

        if (below)
            setY(stagePosition.y - height - 1);
        else
            setY(stagePosition.y);
        setX(stagePosition.x);
        setHeight(height);
        validate();
        float width = Math.max(getPrefWidth(), getWidth());
        setWidth(width);

        validate();
        scrollTo(0, list.getHeight() - getSelectedIndex() * itemHeight - itemHeight / 2, 0, 0, true, true);
        updateVisualScroll();

        previousScrollFocus = null;
        Actor actor = stage.getScrollFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;
        stage.setScrollFocus(this);

        list.getSelection().set(getSelected());
        list.setTouchable(Touchable.enabled);
        clearActions();
        onShow(this, below);
    }

    public void hide () {
        if (!list.isTouchable() || !hasParent()) return;
        list.setTouchable(Touchable.disabled);

        doShow = false;
        Stage stage = getStage();
        if (stage != null) {
            stage.removeCaptureListener(hideListener);
            stage.removeListener(list.getKeyListener());
            if (previousScrollFocus != null && previousScrollFocus.getStage() == null) previousScrollFocus = null;
            Actor actor = stage.getScrollFocus();
            if (actor == null || isAscendantOf(actor)) stage.setScrollFocus(previousScrollFocus);
        }

        list.removeTempListener();
        clearActions();
        onHide(this);
    }

    public void draw (Batch batch, float parentAlpha) {
//        localToStageCoordinates(temp.set(0, 0));
//        if (!temp.equals(stagePosition)){
//            hide();
//        }
        super.draw(batch, parentAlpha);

        //MINE
        list.visualX = getX();
        list.visualY = getY();

        list.draw(Main.batch, parentAlpha);
        //MINE
//        isActive = doShow;
    }

    public void act (float delta) {
        super.act(delta);
        toFront();
    }

    /** Returns the first selected item, or null. For multiple selections use {@link SelectBoxWrapper#getSelection()}. */
    public @Null T getSelected () {
        return selection.first();
    }

    /** Sets the selection to only the passed item, if it is a possible choice, else selects the first item. */
    public void setSelected (@Null T item) {
        if (items.contains(item, false))
            selection.set(item);
        else if (items.size > 0)
            selection.set(items.first());
        else
            selection.clear();
    }

    /** @return The index of the first selected item. The top item has an index of 0. Nothing selected has an index of -1. */
    public int getSelectedIndex () {
        ObjectSet<T> selected = selection.items();
        return selected.size == 0 ? -1 : items.indexOf(selected.first(), false);
    }

    /** Set the backing Array that makes up the choices available in the SelectBoxWrapper */
    public void setItems (T... newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth();

        items.clear();
        items.addAll(newItems);
        selection.validate();
        list.setItems(items);

        invalidate();
        if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
    }

    /** Sets the items visible in the select box. */
    public void setItems (Array<T> newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth();

        if (newItems != items) {
            items.clear();
            items.addAll(newItems);
        }
        selection.validate();
        list.setItems(items);

        invalidate();
        if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
    }

    public void clearItems () {
        if (items.size == 0) return;
        items.clear();
        selection.clear();
        list.clearItems();
        invalidateHierarchy();
    }

    protected void onShow (Actor scrollPane, boolean below) {
        scrollPane.getColor().a = 0;
        scrollPane.addAction(fadeIn(0.3f, Interpolation.fade));
    }

    protected void onHide (Actor scrollPane) {
        scrollPane.getColor().a = 1;
        scrollPane.addAction(sequence(fadeOut(0.15f, Interpolation.fade)));
    }

    /** Returns the internal items array. If modified, {@link #setItems(Array)} must be called to reflect the changes. */
    public Array<T> getItems () {
        return items;
    }

    protected void setStage (Stage stage) {
        Stage oldStage = getStage();
        if (oldStage != null) {
            oldStage.removeCaptureListener(hideListener);
            oldStage.removeListener(list.getKeyListener());
        }
        super.setStage(stage);
    }

    public ListWrapper<T> getList () {
        return list;
    }
}

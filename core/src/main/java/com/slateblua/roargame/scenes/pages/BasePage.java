package com.slateblua.roargame.scenes.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slateblua.roargame.Resources;

public abstract class BasePage extends Table {
    protected Table content;
    protected Cell<Table> contentCell;

    public BasePage () {
        setFillParent(true);
        setBackground(Resources.getDrawable("components/ui-white-pixel", Color.valueOf("#69605Bff")));
        setTouchable(Touchable.enabled);
        addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void clicked (InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });

        content = new Table();
        contentCell = add(content).grow();
        constructContent(content);
    }
    protected abstract void constructContent (Table content);

    /**
     * Called when the page becomes visible
     */
    public void onShow () {
        // To be overridden by subclasses if needed
    }

    /**
     * Called when the page becomes hidden
     */
    public void onHide () {
        // To be overridden by subclasses if needed
    }
}

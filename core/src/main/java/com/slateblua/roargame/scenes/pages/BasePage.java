package com.slateblua.roargame.scenes.pages;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class BasePage extends Table {
    public BasePage () {
        setFillParent(true);
        init();
    }

    /**
     * Initialize the UI components of the page
     */
    protected abstract void init ();

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

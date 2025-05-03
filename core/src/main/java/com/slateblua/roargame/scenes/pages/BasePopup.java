package com.slateblua.roargame.scenes.pages;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class BasePopup extends Dialog {
    protected NavController navController;

    public BasePopup (String title, NavController navController, Skin skin) {
        super(title, skin);
        this.navController = navController;
        init();
    }

    /**
     * Initialize the UI components of the dialog
     */
    protected abstract void init ();

    /**
     * Called when the dialog becomes visible
     */
    public void onShow () {
        // To be overridden by subclasses if needed
    }

    /**
     * Called when the dialog becomes hidden
     */
    public void onHide () {
        // To be overridden by subclasses if needed
    }
}

package com.slateblua.roargame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slateblua.roargame.scenes.screens.SplashScreen;
import lombok.Getter;

@Getter
public class RoarGame extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        addModules();

        setScreen(new SplashScreen(this));
    }

    private void addModules () {
        Locator.get().addModule(Resources.class);
        Locator.get().addModule(GameData.class);
    }

    @Override
    public void dispose() {
        getScreen().dispose();
    }
}

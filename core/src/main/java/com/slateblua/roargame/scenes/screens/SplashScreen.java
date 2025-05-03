package com.slateblua.roargame.scenes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import com.slateblua.roargame.RoarGame;

import java.util.Objects;

public class SplashScreen extends ScreenAdapter {
    private Stage splashStage;
    private float splashShownSeconds;
    private final RoarGame game;

    private static final float MIN_SPLASH_SCREEN_SHOWTIME = 3f;

    public SplashScreen (final RoarGame game) {
        this.game = game;
    }

    @Override
    public void show () {
        final Table splashRoot = new Table();
        splashRoot.setFillParent(true);

        splashStage = new Stage();
        splashStage.addActor(splashRoot);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final Resources resources = Locator.get(Resources.class);
        final boolean isUpdated = Objects.requireNonNull(resources).update(16);

        if (splashShownSeconds >= MIN_SPLASH_SCREEN_SHOWTIME) {
            if (isUpdated) {
                final Game applicationListener = (Game) Gdx.app.getApplicationListener();
                applicationListener.setScreen(new TracyGameScreen(game));
            }
        } else {
            splashShownSeconds += delta;
        }

        splashStage.act(delta);
        splashStage.draw();
    }


    @Override
    public void dispose () {
        splashStage.dispose();
    }
}

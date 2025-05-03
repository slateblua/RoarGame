package com.roargame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slateblua.roargame.scenes.components.OffsetButton;
import com.slateblua.roargame.scenes.pages.BasePage;
import com.slateblua.roargame.scenes.pages.BasePopup;
import com.slateblua.roargame.scenes.pages.NavController;
import lombok.Getter;

public class GameUI {
    private final Stage stage;
    @Getter
    private final Table rootTable;
    private final NavController navController;

    private static GameUI gameUI;

    public static GameUI get () {
        if (gameUI == null) {
            gameUI = new GameUI();
        }
        return gameUI;
    }

    private GameUI () {
        stage = new Stage(new ScreenViewport());

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        navController = new NavController(stage, rootTable);

        Gdx.input.setInputProcessor(stage);
    }

    public void closePopup () {
        navController.closePopup();
    }

    public void openPage (final Class<? extends BasePage> pageClass) {
        navController.openPage(pageClass);
    }

    public void showPopup (final Class<? extends BasePopup> popupClass) {
        navController.showPopup(popupClass);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        stage.dispose();
    }
}

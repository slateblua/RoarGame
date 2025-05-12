package com.slateblua.roargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slateblua.roargame.scenes.pages.BasePage;
import com.slateblua.roargame.scenes.pages.BasePopup;
import com.slateblua.roargame.scenes.pages.NavController;
import com.slateblua.roargame.weapon.WeaponData;
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

        final Table weaponsTable = createWeaponsComponent();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(weaponsTable).expand().bottom();

        stage.addActor(rootTable);

        final GamePad actor = new GamePad();
        rootTable.add(actor).size(240).expand().bottom().padBottom(100);

        navController = new NavController(stage, rootTable);

        Gdx.input.setInputProcessor(stage);
    }

    private Table createWeaponsComponent () {
        final Table weaponsTable = new Table();
        weaponsTable.pad(20);
        weaponsTable.defaults().space(20).size(130);
        final Array<WeaponData> weapons = Locator.get(GameData.class).getWeapons();

        for (WeaponData weapon : weapons) {
            final WeaponButton weaponButton = new WeaponButton(weapon, data ->  Player.get().setCurrentWeapon(Locator.get(GameData.class).getWeaponMap().get(data)));
            weaponsTable.add(weaponButton);
        }

        return weaponsTable;
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

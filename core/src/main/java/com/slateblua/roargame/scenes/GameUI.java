package com.slateblua.roargame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slateblua.roargame.core.Locator;
import com.slateblua.roargame.data.GameData;
import com.slateblua.roargame.gamecomponent.Player;
import com.slateblua.roargame.scenes.pages.PetsPage;
import com.slateblua.roargame.core.component.IconButton;
import com.slateblua.roargame.core.component.Style;
import com.slateblua.roargame.gamecomponent.weapon.WeaponData;
import lombok.Getter;

public class GameUI {
    private final Stage stage;
    @Getter
    private final Table rootTable;

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

        final Table gamePadTable = createGamePadTable();
        rootTable.addActor(gamePadTable);

        final Table petTable = createPetButtonTable();
        rootTable.addActor(petTable);

        // init dialog manager
        final Table dialogContainer = new Table();
        dialogContainer.setFillParent(true);

        stage.addActor(dialogContainer);
        Locator.get(NavController.class).init(dialogContainer);

        Gdx.input.setInputProcessor(stage);
    }

    private Table createPetButtonTable () {
        final Table petTable = new Table();
        petTable.setFillParent(true);

        final IconButton petPageButton = new IconButton(Style.BLUE_40_35_7_13, "core/pet_cat");
        petPageButton.getIconCell().pad(5);

        petPageButton.setOnClick(() -> {
            NavController.show(PetsPage.class);
        });

        petTable.add(petPageButton).size(140).expand().top().right().padTop(150).pad(100);

        return petTable;
    }

    private Table createGamePadTable () {
        final Table controllerTable = new Table();
        controllerTable.setFillParent(true);
        final GamePad actor = new GamePad();

        controllerTable.add(actor).expand().bottom().size(100).padBottom(250);

        return controllerTable;
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

package com.slateblua.roargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slateblua.roargame.scenes.components.IconButton;
import com.slateblua.roargame.scenes.components.OffsetButton;
import com.slateblua.roargame.scenes.components.Style;
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
            final WeaponButton weaponButton = new WeaponButton(weapon);

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

    public static class GamePad extends Table {
        private final Player playerInstance;
        private final OffsetButton joystickKnobImage;

        private final Vector2 knobRelativePosition = new Vector2(); // Knob's current position relative to joystick center
        private final Vector2 knobVisualCenterOffset = new Vector2(); // To help draw knob centered

        private final float joystickOperableRadius; // Max distance knob's center can travel from joystick's center
        private final float joystickActivationRadius; // How far from center a touch must be to activate joystick

        private boolean isDragging = false;

        // Define sizes (adjust as needed)
        private final float baseSize = 180f;

        public GamePad() {
            this.playerInstance = Player.get();

            joystickKnobImage = new OffsetButton(Style.BLUE_40_35_7_13);

            final float knobSize = 70f;
            joystickOperableRadius = baseSize / 2f - knobSize / 2f;
            joystickActivationRadius = baseSize / 2f;

            knobVisualCenterOffset.set(knobSize / 2f, knobSize / 2f);

            pad(30f);

            final Table joystickStack = new Table();
            joystickStack.add(joystickKnobImage); // Knob is drawn on top

            this.add(joystickStack).size(baseSize, baseSize); // The joystick assembly size

            resetKnobPosition();

            joystickStack.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Vector2 touchOffsetFromCenter = new Vector2(x - baseSize / 2f, y - baseSize / 2f);

                    if (touchOffsetFromCenter.len() <= joystickActivationRadius) {
                        isDragging = true;
                        updateJoystickState(touchOffsetFromCenter);
                        return true;
                    }
                    return false;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (isDragging) {
                        Vector2 touchOffsetFromCenter = new Vector2(x - baseSize / 2f, y - baseSize / 2f);
                        updateJoystickState(touchOffsetFromCenter);
                    }
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (isDragging) {
                        isDragging = false;
                        resetKnobPosition();
                        playerInstance.resetJoystickInfluence();
                    }
                }
            });
        }

        private void updateJoystickState(Vector2 touchOffsetFromCenter) {
            knobRelativePosition.set(touchOffsetFromCenter);

            if (knobRelativePosition.len() > joystickOperableRadius) {
                knobRelativePosition.nor().scl(joystickOperableRadius);
            }

            float knobVisualX = (baseSize / 2f) + knobRelativePosition.x - knobVisualCenterOffset.x;
            float knobVisualY = (baseSize / 2f) + knobRelativePosition.y - knobVisualCenterOffset.y;
            joystickKnobImage.setPosition(knobVisualX, knobVisualY);

            if (joystickOperableRadius == 0) return; // Avoid division by zero

            float normalizedX = knobRelativePosition.x / joystickOperableRadius;
            float normalizedY = knobRelativePosition.y / joystickOperableRadius;

            playerInstance.joystickVelocityInfluence.set(
                normalizedX * Player.SPEED,
                normalizedY * Player.SPEED
            );
        }

        private void resetKnobPosition() {
            knobRelativePosition.set(0, 0);
            float knobCenterX = baseSize / 2f - knobVisualCenterOffset.x;
            float knobCenterY = baseSize / 2f - knobVisualCenterOffset.y;
            joystickKnobImage.setPosition(knobCenterX, knobCenterY);
        }

    }

    public static class WeaponButton extends IconButton {
        public WeaponButton (final WeaponData data) {
            super(Style.BLUE_40_35_7_13, "core/projectile_" + data.getBulletData().getBulletId().getName());

            setOnClick(() -> {
                Player.get().setCurrentWeapon(Locator.get(GameData.class).getWeaponMap().get(data));
            });
        }
    }
}

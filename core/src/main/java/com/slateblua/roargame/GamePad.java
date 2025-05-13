package com.slateblua.roargame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slateblua.roargame.scenes.components.Shape;

public class GamePad extends Table {
    private final Player playerInstance;
    private final Table joystickKnobImage;

    private final Vector2 knobRelativePosition = new Vector2(); // Knob's current position relative to joystick center
    private final Vector2 knobVisualCenterOffset = new Vector2(); // To help draw knob centered

    private final float joystickOperableRadius; // Max distance knob's center can travel from joystick's center
    private final float joystickActivationRadius; // How far from center a touch must be to activate joystick

    private boolean isDragging = false;

    // Define sizes (adjust as needed)
    private final float baseSize = 180f;

    public GamePad () {
        this.playerInstance = Player.get();

        joystickKnobImage = new Table();
        joystickKnobImage.setTouchable(Touchable.enabled);
        joystickKnobImage.setBackground(Shape.ROUNDED_50.getDrawable(Color.valueOf("#43a6f2")));

        final float knobSize = 70f;
        joystickOperableRadius = baseSize / 2f - knobSize / 2f;
        joystickActivationRadius = baseSize / 2f;

        knobVisualCenterOffset.set(knobSize / 2f, knobSize / 2f);

        pad(30f);

        final Table joystickStack = new Table();
        joystickStack.add(joystickKnobImage); // Knob is drawn on top

        final Table wrapper = new Table();
        pad(10);
        wrapper.setBackground(Shape.ROUNDED_50.getDrawable(Color.valueOf("#11111181")));
        wrapper.add(joystickStack).size(baseSize, baseSize); // The joystick assembly size

        add(wrapper);

        resetKnobPosition();

        joystickStack.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Vector2 touchOffsetFromCenter = new Vector2(x - baseSize / 2f, y - baseSize / 2f);

                if (touchOffsetFromCenter.len() <= joystickActivationRadius) {
                    isDragging = true;
                    updateJoystickState(touchOffsetFromCenter);
                    return true;
                }
                return false;
            }

            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                if (isDragging) {
                    Vector2 touchOffsetFromCenter = new Vector2(x - baseSize / 2f, y - baseSize / 2f);
                    updateJoystickState(touchOffsetFromCenter);
                }
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (isDragging) {
                    isDragging = false;
                    resetKnobPosition();
                    playerInstance.resetJoystickInfluence();
                }
            }
        });
    }

    private void updateJoystickState (Vector2 touchOffsetFromCenter) {
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

    private void resetKnobPosition () {
        knobRelativePosition.set(0, 0);
        float knobCenterX = (baseSize - joystickKnobImage.getWidth()) / 2f;
        float knobCenterY = (baseSize - joystickKnobImage.getHeight()) / 2f;
        joystickKnobImage.setPosition(knobCenterX, knobCenterY);
    }

}

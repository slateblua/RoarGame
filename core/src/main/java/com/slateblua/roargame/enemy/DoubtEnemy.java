package com.slateblua.roargame.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;

public class DoubtEnemy extends BaseEnemy {
    private float circleTimer;
    private Vector2 circleCenter;

    public DoubtEnemy (Vector2 position) {
        super(position, 20, 5, 60, BaseEnemy.ENEMY_BASE_SPEED * 1.2f);
        this.circleTimer = 0;
        this.circleCenter = position.cpy();
    }

    @Override
    public TextureRegion getTexture () {
        return Locator.get(Resources.class).getTexture("core/enemy_doubt");
    }

    @Override
    public void update(float deltaTime, Vector2 playerPosition) {
        circleTimer += deltaTime;

        // Doubt enemy circles around for a while, then moves toward player
        if (circleTimer < 5.0f) {
            // Circle around
            float angle = circleTimer * 2.0f;
            position.x = circleCenter.x + (float) Math.cos(angle) * 100;
            position.y = circleCenter.y + (float) Math.sin(angle) * 100;
        } else if (circleTimer < 10.0f) {
            // Move toward player
            super.update(deltaTime, playerPosition);
        } else {
            // Reset circle behavior
            circleTimer = 0;
            circleCenter = position.cpy();
        }
    }
}

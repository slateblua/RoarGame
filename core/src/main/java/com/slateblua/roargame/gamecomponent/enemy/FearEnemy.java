package com.slateblua.roargame.gamecomponent.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.core.Locator;
import com.slateblua.roargame.core.Resources;

public class FearEnemy extends BaseEnemy {
    private float dashTimer;
    private boolean isDashing;

    public FearEnemy (Vector2 position) {
        super(position, 20, 15, 75, BaseEnemy.ENEMY_BASE_SPEED);
        this.dashTimer = 0;
        this.isDashing = false;
    }

    @Override
    public TextureRegion getTexture () {
        return Locator.get(Resources.class).getTexture("core/enemy_doubt");
    }

    @Override
    public void update (float deltaTime, Vector2 playerPosition) {
        // Fear enemy occasionally dashes towards player
        dashTimer += deltaTime;

        if (dashTimer > 3.0f && !isDashing) {
            isDashing = true;
            speed = BaseEnemy.ENEMY_BASE_SPEED * 3.0f;
            dashTimer = 0;
        } else if (dashTimer > 0.5f && isDashing) {
            isDashing = false;
            speed = BaseEnemy.ENEMY_BASE_SPEED;
        }

        super.update(deltaTime, playerPosition);
    }
}


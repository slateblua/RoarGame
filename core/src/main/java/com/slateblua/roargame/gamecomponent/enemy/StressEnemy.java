package com.slateblua.roargame.gamecomponent.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.core.Locator;
import com.slateblua.roargame.core.Resources;

public class StressEnemy extends BaseEnemy {
    public StressEnemy (Vector2 position) {
        super(position, 90, 10, 50, BaseEnemy.ENEMY_BASE_SPEED * 1.5f);
    }

    @Override
    public TextureRegion getTexture () {
        return Locator.get(Resources.class).getTexture("core/enemy_anxiety");
    }

    @Override
    public void update (float deltaTime, Vector2 playerPosition) {
        // Anxiety moves faster but erratically
        super.update(deltaTime, playerPosition);

        // Add some jitter to movement
        position.x += (float) (Math.random() * 2 - 1) * 2;
        position.y += (float) (Math.random() * 2 - 1) * 2;
    }
}

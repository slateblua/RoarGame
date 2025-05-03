package com.slateblua.roargame.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;

public class TraumaEnemy extends BaseEnemy {
    public TraumaEnemy (Vector2 position) {
        super(position, 20, 20, 100, 90f);
    }

    @Override
    public TextureRegion getTexture () {
        return Locator.get(Resources.class).getTexture("core/enemy_trauma");
    }
}

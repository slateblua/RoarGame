package com.slateblua.roargame.bonus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Movable;
import com.slateblua.roargame.Resources;
import lombok.Getter;

@Getter
public class Bonus implements Movable {
    private final Vector2 position;
    private final float width;
    private final float height;
    private final int energyAmount;
    private final int scoreValue;

    public Bonus (Vector2 position) {
        this.position = position;
        this.width = 80f;
        this.height = 80f;
        this.energyAmount = 20;
        this.scoreValue = 10;
    }

    final TextureRegion region = Locator.get(Resources.class).getTexture("core/bonus");

    public void render (SpriteBatch batch) {
        batch.draw(
            region,
            position.x - width / 2,
            position.y - height / 2,
            width,
            height
        );
    }
}

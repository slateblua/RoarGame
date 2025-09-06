package com.slateblua.roargame.gamecomponent.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.gamecomponent.Movable;
import lombok.Getter;

public abstract class BaseEnemy implements Movable {
    @Getter
    protected Vector2 position;
    protected Vector2 velocity;
    @Getter
    protected int health;
    @Getter
    protected int damage;
    @Getter
    protected int scoreValue;
    protected float speed;

    public static final float ENEMY_BOX_SIZE = 120f;
    public static final float ENEMY_BASE_SPEED = 80f;

    public BaseEnemy (final Vector2 position, int health, int damage, int scoreValue, float speed) {
        this.position = position;
        this.velocity = new Vector2();
        this.health = health;
        this.damage = damage;
        this.scoreValue = scoreValue;
        this.speed = speed;
    }

    public void update (float deltaTime, Vector2 playerPosition) {
        // Move towards player
        final Vector2 direction = new Vector2(playerPosition).sub(position).nor();
        velocity.set(direction).scl(speed);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    private final TextureRegion texture = getTexture();

    public void render (SpriteBatch batch) {
        batch.draw(
            texture,
            position.x - ENEMY_BOX_SIZE / 2,
            position.y - ENEMY_BOX_SIZE / 2,
            ENEMY_BOX_SIZE,
            ENEMY_BOX_SIZE
        );
    };

    public void takeDamage (int damage) {
        health = Math.max(0, health - damage);
    }

    public abstract TextureRegion getTexture ();

    @Override
    public float getWidth () {
        return ENEMY_BOX_SIZE;
    }

    @Override
    public float getHeight () {
        return ENEMY_BOX_SIZE;
    }
}

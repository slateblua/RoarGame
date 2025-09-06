package com.slateblua.roargame.gamecomponent.weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.slateblua.roargame.gamecomponent.Movable;
import lombok.Getter;

@Getter
public class Bullet implements Movable, Pool.Poolable {
    protected Vector2 position = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected int damage;
    protected float lifetime;

    private TextureRegion texture;

    private static final int BULLET_LIFE_TIME = 15;
    private static final float BOX_SIZE = 90f;

    public void update (final float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        lifetime += delta;

        if (isExpired()) {
            reset(); // Reset the bullet's state before returning it to the pool
            Pools.free(this);
        }
    }

    public static Bullet getFromPool (final Vector2 position, final Vector2 direction, final WeaponData weaponData) {
        final Bullet pooledBullet = Pools.obtain(Bullet.class);
        pooledBullet.position.set(position);
        pooledBullet.velocity.set(direction).scl(600f);
        pooledBullet.setData(weaponData);

        return pooledBullet;
    }

    public void render (SpriteBatch batch) {
        batch.draw(
            texture,
            position.x - BOX_SIZE / 2,
            position.y - BOX_SIZE / 2,
            BOX_SIZE,
            BOX_SIZE
        );
    }

    public boolean isExpired () {
        return lifetime >= BULLET_LIFE_TIME;
    }

    public void setData (final WeaponData weaponData) {
        if (weaponData == null || weaponData.getBulletData() == null || weaponData.getBulletData().getTexture() == null) {
            throw new IllegalArgumentException("WeaponData or its BulletData is invalid.");
        }
        texture = weaponData.getBulletData().getTexture();
        damage = weaponData.getDamage();
    }

    @Override
    public void reset () {
        position.setZero();
        velocity.setZero();
        lifetime = 0;
        damage = 0;
    }

    @Override
    public float getWidth () {
        return BOX_SIZE;
    }

    @Override
    public float getHeight () {
        return BOX_SIZE;
    }
}

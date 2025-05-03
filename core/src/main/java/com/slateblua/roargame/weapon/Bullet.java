package com.slateblua.roargame.weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.slateblua.roargame.Movable;
import lombok.Getter;

@Getter
public class Bullet implements Movable, Pool.Poolable {
    protected Vector2 position;
    protected Vector2 velocity;
    protected int damage;
    protected float lifetime;

    private TextureRegion texture;

    private static final int BULLET_LIFE_TIME = 15;
    private static final float BOX_SIZE = 90f;

    public void update (final float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        lifetime += delta;

        if (isExpired()) {
            Pools.free(this);
        }
    }

    public static Bullet getFromPool (final Vector2 position, final Vector2 direction, final WeaponData weaponData) {
        final Bullet pooledBullet = Pools.obtain(Bullet.class);
        pooledBullet.position = position;
        pooledBullet.velocity = direction.cpy().scl(600f);
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
        texture = weaponData.getBulletData().getTexture();
        damage = weaponData.getDamage();
    }

    @Override
    public void reset () {
        lifetime = 0;
        velocity.setZero();
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

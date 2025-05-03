package com.slateblua.roargame.weapon;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

public class BaseWeapon {
    // Getters
    @Getter
    protected String name;
    @Getter
    protected float cooldownState;
    @Getter
    protected int damage;

    protected WeaponData weaponData;

    private BaseWeapon (final WeaponData weaponData) {
        this.weaponData = weaponData;
    }

    public Bullet shoot (Vector2 position, Vector2 direction) {
        return createBullet(position, direction);
    }

    protected Bullet createBullet (Vector2 position, Vector2 direction) {
        return Bullet.getFromPool(position, direction, weaponData);
    }

    public static BaseWeapon fromData (final WeaponData weaponData) {
        return new BaseWeapon(weaponData);
    }
}

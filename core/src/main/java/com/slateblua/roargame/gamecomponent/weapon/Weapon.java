package com.slateblua.roargame.gamecomponent.weapon;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

public class Weapon {
    // Getters
    @Getter
    protected String name;
    @Getter
    protected float cooldownState;
    @Getter
    protected int damage;

    protected WeaponData weaponData;

    private Weapon (final WeaponData weaponData) {
        this.weaponData = weaponData;
    }

    public Bullet shoot (Vector2 position, Vector2 direction) {
        return createBullet(position, direction);
    }

    protected Bullet createBullet (Vector2 position, Vector2 direction) {
        return Bullet.getFromPool(position, direction, weaponData);
    }

    public static Weapon fromData (final WeaponData weaponData) {
        return new Weapon(weaponData);
    }
}

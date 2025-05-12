package com.slateblua.roargame;

import com.slateblua.roargame.scenes.components.IconButton;
import com.slateblua.roargame.scenes.components.Style;
import com.slateblua.roargame.weapon.WeaponData;

public class WeaponButton extends IconButton {
    public WeaponButton (final WeaponData data, OnWeaponButtonPressed onButtonPressed) {
        super(Style.BLUE_40_35_7_13, "core/projectile_" + data.getBulletData().getBulletId().getName());

        setOnClick(() -> {
            onButtonPressed.process(data);
        });
    }

    @FunctionalInterface
    public interface OnWeaponButtonPressed {
        void process (final WeaponData data);
    }
}

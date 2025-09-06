package com.slateblua.roargame.scenes;

import com.slateblua.roargame.core.component.IconButton;
import com.slateblua.roargame.core.component.Style;
import com.slateblua.roargame.gamecomponent.weapon.WeaponData;

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

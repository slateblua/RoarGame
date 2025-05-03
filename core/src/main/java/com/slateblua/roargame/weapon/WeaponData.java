package com.slateblua.roargame.weapon;

import com.badlogic.gdx.utils.XmlReader;
import lombok.Data;
import lombok.Getter;

@Getter
public class WeaponData {
    private final int damage;
    private final float cooldown;
    private final WeaponId name;
    private final BulletData bulletData;

    public WeaponData (final XmlReader.Element element) {
        damage = element.getIntAttribute("damage");
        cooldown = element.getIntAttribute("cooldown");
        name = new WeaponId(element.getAttribute("name"));
        bulletData = new BulletData(element);
    }

    // Value object for WeaponId
    @Data
    private static final class WeaponId {
        private final String name;
    }
}

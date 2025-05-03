package com.slateblua.roargame.weapon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import lombok.Data;
import lombok.Getter;

@Getter
public class BulletData {
    private final BulletId name;

    public BulletData (XmlReader.Element element) {
        final XmlReader.Element bullet = element.getChildByName("bullet");
        name = new BulletId(bullet.getAttribute("name"));
    }

    public TextureRegion getTexture () {
        return Locator.get(Resources.class).getTexture("core/projectile_" + name.name);
    }

    // Value object for BulletId
    @Data
    private static final class BulletId {
        private final String name;
    }
}

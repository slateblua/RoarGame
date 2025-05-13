package com.slateblua.roargame.pet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.XmlReader;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import lombok.Data;
import lombok.Getter;

public class PetData {
    @Getter
    private final PetId petId;

    private TextureRegion texture;

    public PetData (final XmlReader.Element element) {
        petId = new PetId(element.getAttribute("name"));
    }

    public TextureRegion getTexture () {
        if (texture == null) {
            texture = Locator.get(Resources.class).getTexture("core/pet_" + petId.name);
        }
        return texture;
    }

    public Drawable getDrawable () {
        return Resources.getDrawable("core/pet_" + petId.name);
    }

    @Data
    public static class PetId {
        private final String name;
    }
}

package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import lombok.Getter;

public enum Shape {
    ROUNDED_30(30, "ui-white-squircle"),
    ROUNDED_35(35, "ui-white-squircle"),
    ROUNDED_40(40, "ui-white-squircle"),
    ROUNDED_50(50, "ui-white-squircle"),

    ROUNDED_35_BORDER(35, "ui-white-squircle-border"),
    ROUNDED_30_BORDER(30, "ui-white-squircle-border"),
    ROUNDED_40_BORDER(40, "ui-white-squircle-border"),
    ROUNDED_50_BORDER(50, "ui-white-squircle-border"),
    ;

    @Getter
    private final int roundness;
    private final String assetName;

    Shape (int roundness, String name) {
        this.roundness = roundness;
        this.assetName = name;
    }

    public String getRegionName () {
        return "components/" + assetName + "-" + roundness;
    }

    @SuppressWarnings("all")
    public Drawable getDrawable (Color color) {
        return Locator.get(Resources.class).obtainDrawable("components/" + assetName + "-" + roundness, color);
    }
}

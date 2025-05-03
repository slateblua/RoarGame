package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import lombok.Getter;

public enum Shape {
    ROUNDED_20(20, "ui-white-squircle"),
    ROUNDED_25(25, "ui-white-squircle"),
    ROUNDED_30(30, "ui-white-squircle"),
    ROUNDED_35(35, "ui-white-squircle"),
    ROUNDED_40(40, "ui-white-squircle"),
    ROUNDED_50(50, "ui-white-squircle"),

    ROUNDED_20_BORDER(20, "ui-white-squircle-border"),
    ROUNDED_25_BORDER(25, "ui-white-squircle-border"),
    ROUNDED_35_BORDER(35, "ui-white-squircle-border"),
    ROUNDED_30_BORDER(30, "ui-white-squircle-border"),
    ROUNDED_40_BORDER(40, "ui-white-squircle-border"),
    ROUNDED_50_BORDER(50, "ui-white-squircle-border"),

    ROUNDED_25_BTM(25, "ui-white-squircle-bottom"),
    ROUNDED_35_BTM(35, "ui-white-squircle-bottom"),
    ROUNDED_40_BTM(40, "ui-white-squircle-bottom"),
    ROUNDED_50_BTM(50, "ui-white-squircle-bottom"),

    ROUNDED_35_TOP(35, "ui-white-squircle-top"),
    ROUNDED_50_TOP(50, "ui-white-squircle-top"),

    ROUNDED_25_LEFT(25, "ui-white-squircle-left"),
    ROUNDED_35_LEFT(35, "ui-white-squircle-left"),

    ROUNDED_25_RIGHT(25, "ui-white-squircle-right"),
    ROUNDED_35_RIGHT(35, "ui-white-squircle-right"),

    ROUNDED_35_BOTTOM_LEFT(35, "ui-white-squircle-bottom-left"),
    ROUNDED_35_BOTTOM_RIGHT(35, "ui-white-squircle-bottom-right"),

    ROUNDED_35_BORDER_BTM_STROKED(35, "ui-white-squircle-border-bottom-stroked"),

    ROUNDED_35_BORDER_TOP(35, "ui-white-squircle-border-top"),
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

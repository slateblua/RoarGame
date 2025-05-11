package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.slateblua.roargame.Resources;

public class BorderedProgressBar extends ProgressBar {

    private final Image border;
    private String borderRegionName;

    public BorderedProgressBar (String background, String foreground, String borderRegionName, Color backgroundColor, Color fillColor, Color borderColor) {
        super(background, foreground, backgroundColor, fillColor);
        this.borderRegionName = borderRegionName;
        this.border = new Image(Resources.getDrawable(borderRegionName, borderColor));
        this.border.setFillParent(true);
        addActor(this.border);
    }

    public BorderedProgressBar (Drawable background, TextureAtlas.AtlasRegion foreground, Drawable borderDrawable, Color fillColor) {
        super(background, foreground, fillColor);

        this.border = new Image(borderDrawable);
        this.border.setFillParent(true);
        addActor(this.border);
    }

    public BorderedProgressBar (String background, String foreground, String borderRegionName) {
        this(background, foreground, borderRegionName, Color.WHITE, Color.WHITE, Color.WHITE);
    }

    public void setBorderColor (Color borderColor) {
        border.setDrawable(Resources.getDrawable(borderRegionName, borderColor));
    }
}

package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.slateblua.roargame.Resources;
import lombok.Getter;

public class IconButton extends OffsetButton {
    protected Drawable iconDrawable;
    @Getter
    protected Image icon;
    @Getter
    protected Cell<Image> iconCell;

    public IconButton (final Style style, final String icon) {
        this.iconDrawable = Resources.getDrawable(icon);
        build(style);
    }

    @Override
    protected void buildInner (final Table container) {
        icon = new Image(iconDrawable, Scaling.fit);
        iconCell = container.add(icon).grow().pad(15);
    }

    public void setIcon (final Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        this.icon.setDrawable(iconDrawable);
    }

    @Override
    public void visuallyEnable () {
        super.visuallyEnable();
        icon.setColor(Color.WHITE);
    }

    @Override
    public void visuallyDisable () {
        super.visuallyDisable();
        icon.setColor(Color.RED);
    }
}

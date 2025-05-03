package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Getter;

public class BorderedTable extends PressableTable {
    @Getter
    protected final Image borderImage;
    protected int borderSize = 8;

    public BorderedTable () {
        this(Shape.ROUNDED_35.getDrawable(Color.valueOf("c2b8b0")), Shape.ROUNDED_35_BORDER.getDrawable(Color.valueOf("81776e")));
    }

    public BorderedTable (Drawable backgroundDrawable, Drawable borderDrawable) {
        borderImage = new Image(borderDrawable);
        borderImage.setFillParent(true);
        borderImage.setTouchable(Touchable.disabled);
        addActor(borderImage);

        setBackground(backgroundDrawable);
        pad(borderSize); // pad for border
    }

    public void setBorderDrawable (Drawable borderDrawable) {
        borderImage.setDrawable(borderDrawable);
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        addActor(borderImage);
    }
}

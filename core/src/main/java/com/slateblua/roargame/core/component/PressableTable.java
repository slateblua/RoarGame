package com.slateblua.roargame.core.component;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PressableTable extends BaseTable {
    protected float pressedScale;
    protected float preTouchScaleX;
    protected float preTouchScaleY;

    // click box
    private Table clickBox;
    private int clickBoxPadX;
    private int clickBoxPadY;
    private int clickBoxOffsetX;
    private int clickBoxOffsetY;

    private int origin;

    public PressableTable () {
        pressedScale = 0.9f;
        touchDownDuration = 0.07f;
        touchUpDuration = 0.05f;

        initClickBox();
    }

    protected void initClickBox () {
        clickBox = new Table();
        addActor(clickBox);
        clickBox.setTouchable(Touchable.enabled);
    }

    public void setClickBoxPad (int pad) {
        setClickBoxPad(pad, pad);
    }

    public void setClickBoxPad (int padX, int padY) {
        this.clickBoxPadX = padX;
        this.clickBoxPadY = padY;
    }

    public void setClickBoxOffset (int offset) {
        setClickBoxOffset(offset, offset);
    }

    public void setClickBoxOffset (int offsetX, int offsetY) {
        this.clickBoxOffsetX = offsetX;
        this.clickBoxOffsetY = offsetY;
    }

    @Override
    protected void sizeChanged () {
        super.sizeChanged();

        final float width = clickBoxPadX + getWidth();
        final float height = clickBoxPadY + getHeight();
        clickBox.setSize(width, height);

        final float x = -clickBoxPadX / 2.0f + clickBoxOffsetX;
        final float y = -clickBoxPadY / 2.0f + clickBoxOffsetY;
        clickBox.setPosition(x, y);

        super.setOrigin(origin);
    }

    @Override
    public void setOrigin (int alignment) {
        this.origin = alignment;
        super.setOrigin(origin);
    }

    @Override
    protected void animateTouchDown () {
        preTouchScaleX = getScaleX();
        preTouchScaleY = getScaleY();

        setTransform(true);

        super.animateTouchDown();
    }

    @Override
    protected Action getTouchDownAction () {
        return Actions.scaleTo(
            preTouchScaleX * pressedScale,
            preTouchScaleY * pressedScale,
            touchDownDuration,
            Interpolation.sineIn
        );
    }

    @Override
    protected Action getTouchUpAction () {
        return Actions.scaleTo(
            preTouchScaleX,
            preTouchScaleY,
            touchUpDuration,
            Interpolation.sineOut
        );
    }

    @Override
    protected Actor getAnimatedActor () {
        return this;
    }

    @Override
    protected void touchedUp () {
        super.touchedUp();
        setTransform(false);
    }

    @Override
    public void clearChildren () {
        super.clearChildren();
        addActor(clickBox);
    }

    public void setPressedScale (float pressedScale) {
        this.pressedScale = pressedScale;
        hasSound = pressedScale != 1f;
    }

    public void resetAnimationState () {
        clearActions();
        setScale(1f);
        animating = false;
        clicked = false;
        shouldTouchUp = false;
    }
}

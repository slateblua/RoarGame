package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import lombok.Getter;
import lombok.Setter;

public class ProgressBar extends Table {

    public enum Orientation {
        HORIZONTAL_LEFT,  // fill from left (mask out right)
        HORIZONTAL_RIGHT, // fill from right (mask out left)
        VERTICAL_TOP,     // fill from top (mask out bottom)
        VERTICAL_BOTTOM   // fill from bottom (mask out top)
    }

    protected String backgroundRegionName;
    @Getter
    protected float maxProgress;
    @Getter
    protected float currentProgress;

    protected final MaskedNP progressBarNP;

    @Setter
    protected float speed = 50f; // pixels per second

    @Setter
    protected int fillPaddingX;
    @Setter
    protected int fillPaddingY;

    protected float targetProgress;

    protected float timeToReachTarget;

    private Runnable onTargetProgressReached;

    @Setter
    private Orientation orientation = Orientation.HORIZONTAL_LEFT; // default

    public ProgressBar (String background, String foreground, Color backgroundColor, Color fillColor) {
        this.backgroundRegionName = background;
        final TextureAtlas.AtlasRegion fillRegion = Locator.get(Resources.class).getAtlas().findRegion(foreground);
        progressBarNP = new MaskedNP(fillRegion, fillColor);
        if (background != null) {
            setBackground(Resources.getDrawable(backgroundRegionName, backgroundColor));
        }
    }

    public ProgressBar (Drawable background, TextureAtlas.AtlasRegion foreground, Color fillColor) {
        progressBarNP = new MaskedNP(foreground, fillColor);

        if (background != null) {
            setBackground(background);
        }
    }

    public void setBackgroundColor (Color backgroundColor) {
        setBackground(Resources.getDrawable(backgroundRegionName, backgroundColor));
    }

    public void setFillColor (Color fillColor) {
        progressBarNP.setFillColor(fillColor);
    }

    @Override
    protected void sizeChanged () {
        super.sizeChanged();
        progressBarNP.setWidth(getWidth() - 2 * fillPaddingX);
        progressBarNP.setHeight(getHeight() - 2 * fillPaddingY);
        updateProgressBar();
    }

    @Override
    public void act (float delta) {
        super.act(delta);
        update(delta);
    }

    protected void update (float delta) {
        float distance = Math.abs(targetProgress - currentProgress);
        timeToReachTarget = distance / speed;

        if (delta >= timeToReachTarget) {
            if (currentProgress != targetProgress) {
                // it just reached on this frame
                if (onTargetProgressReached != null) {
                    onTargetProgressReached.run();
                    onTargetProgressReached = null;
                }
            }
            currentProgress = targetProgress;
        } else {
            float progressPerFrame = distance / timeToReachTarget * delta;
            if (currentProgress < targetProgress) {
                currentProgress += progressPerFrame;
            } else {
                currentProgress -= progressPerFrame;
            }
        }

        updateProgressBar();
    }

    public void setMaxProgress (float maxProgress) {
        this.maxProgress = maxProgress;
        updateProgressBar();
    }

    public void setCurrentProgress (float currentProgress) {
        setCurrentProgress(currentProgress, false);
    }

    public void setCurrentProgress (float currentProgress, boolean hard) {
        currentProgress = MathUtils.clamp(currentProgress, 0, maxProgress);
        if (hard) {
            this.currentProgress = currentProgress;
        }
        this.targetProgress = currentProgress;
    }

    public void setCurrentProgress (float currentProgress, boolean hard, Runnable onTargetReached) {
        currentProgress = MathUtils.clamp(currentProgress, 0, maxProgress);
        if (hard) {
            this.currentProgress = currentProgress;
        }
        this.targetProgress = currentProgress;

        if (onTargetReached != null) {
            this.onTargetProgressReached = onTargetReached;
        }
    }

    public void setCurrentProgress (float currentProgress, Runnable onTargetReached) {
        setCurrentProgress(currentProgress, false, onTargetReached);
    }

    public void updateProgressBar () {
        float ratio = (maxProgress - currentProgress) / maxProgress;
        switch (orientation) {
            case HORIZONTAL_LEFT:
                progressBarNP.setRightOffset(ratio * progressBarNP.getWidth());
                break;
            case HORIZONTAL_RIGHT:
                progressBarNP.setLeftOffset(ratio * progressBarNP.getWidth());
                break;
            case VERTICAL_TOP:
                progressBarNP.setBottomOffset(ratio * progressBarNP.getHeight());
                break;
            case VERTICAL_BOTTOM:
                progressBarNP.setTopOffset(ratio * progressBarNP.getHeight());
                break;
        }
    }

    @Override
    protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
        super.drawBackground(batch, parentAlpha, x, y);
        drawProgress(batch, x, y, parentAlpha);
    }

    protected void drawProgress (Batch batch, float x, float y, float parentAlpha) {
        if (currentProgress >= 0) {
            progressBarNP.draw(batch, x + fillPaddingX, y + fillPaddingY, parentAlpha);
        }
    }

    public void setFillPadding (int fillPaddingX, int fillPaddingY) {
        setFillPaddingX(fillPaddingX);
        setFillPaddingY(fillPaddingY);
    }

    public void setFillPadding (int fillPadding) {
        setFillPaddingX(fillPadding);
        setFillPaddingY(fillPadding);
    }
}

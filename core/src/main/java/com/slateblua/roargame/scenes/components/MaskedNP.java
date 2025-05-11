package com.slateblua.roargame.scenes.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import lombok.Getter;
import lombok.Setter;

public class MaskedNP {

    @Setter
    private TextureAtlas.AtlasRegion region;

    private final int[] originalSplits;
    private final float[] splits;

    @Getter
    @Setter
    private float width;
    @Setter
    @Getter
    private float height;

    private float x;
    private float y;

    @Setter
    private Color fillColor;

    private float rightOffset;
    private float leftOffset;
    // New vertical offsets—only one should be used at a time.
    private float topOffset;    // When filling from bottom (masking top)
    private float bottomOffset; // When filling from top (masking bottom)

    // Old pre-calculated fields (unchanged)
    private float leftStartX, midStartX, rightStartX;
    private float leftEndX, midEndX, rightEndX;
    private float leftWidth, midWidth, rightWidth;
    private float topYStart, midYStart, botYStart;
    private float v0, v1, v2, v3;
    private float sizeToU, sizeToV;
    private float topHeight, midHeight, botHeight;
    private final Color tmpBatchColor = new Color();

    public MaskedNP (TextureAtlas.AtlasRegion region, Color color) {
        this(region, 1f, color);
    }

    public MaskedNP (TextureAtlas.AtlasRegion region, float scale, Color color) {
        this.region = region;
        fillColor = color;
        originalSplits = new int[4];
        int[] regionSplits = region.findValue("split");
        originalSplits[0] = regionSplits[0]; // LEFT
        originalSplits[1] = regionSplits[1]; // RIGHT
        originalSplits[2] = regionSplits[2]; // TOP
        originalSplits[3] = regionSplits[3]; // BOTTOM
        splits = new float[4];
        splits[0] = originalSplits[0] * scale;
        splits[1] = originalSplits[1] * scale;
        splits[2] = originalSplits[2] * scale;
        splits[3] = originalSplits[3] * scale;

        width = region.getRegionWidth() * scale;
        height = region.getRegionHeight() * scale;
    }

    /**
     * Set the top offset (in pixels) to mask out the top portion.
     * This is used when you want the fill to grow from the bottom.
     * Calling this resets any bottom offset.
     */
    public void setTopOffset (float topOffset) {
        this.topOffset = MathUtils.clamp(topOffset, 0, height);
        this.bottomOffset = 0;
    }

    /**
     * Set the bottom offset (in pixels) to mask out the bottom portion.
     * This is used when you want the fill to grow from the top.
     * Calling this resets any top offset.
     */
    public void setBottomOffset (float bottomOffset) {
        this.bottomOffset = MathUtils.clamp(bottomOffset, 0, height);
        this.topOffset = 0;
    }

    public void setLeftOffset (float leftOffset) {
        this.leftOffset = MathUtils.clamp(leftOffset, 0, width - rightOffset);
    }

    public void setRightOffset (float rightOffset) {
        this.rightOffset = MathUtils.clamp(rightOffset, 0, width - leftOffset);
    }

    private void recalculate () {
        sizeToU = 1f / region.getTexture().getWidth();
        sizeToV = 1f / region.getTexture().getHeight();

        // --- Vertical calculations ---
        if (topOffset > 0) {
            // Using topOffset: mask out the top.
            float effectiveHeight = height - topOffset;
            topHeight = splits[2];
            midHeight = effectiveHeight - splits[3] - splits[2];
            botHeight = splits[3];

            topYStart = y + effectiveHeight - topHeight;
            midYStart = y + splits[3];
            botYStart = y;
        } else if (bottomOffset > 0) {
            // Using bottomOffset: mask out the bottom.
            float effectiveHeight = height - bottomOffset;
            topHeight = splits[2];
            midHeight = effectiveHeight - splits[3] - splits[2];
            botHeight = splits[3];

            // Keep the top edge at full height, then shift lower patches upward.
            topYStart = y + height - topHeight;
            midYStart = y + bottomOffset + splits[3];
            botYStart = y + bottomOffset;
        } else {
            // No vertical offset; use default.
            topHeight = splits[2];
            midHeight = height - splits[3] - splits[2];
            botHeight = splits[3];

            topYStart = y + height - topHeight;
            midYStart = y + splits[3];
            botYStart = y;
        }

        // --- Horizontal calculations (unchanged) ---
        leftStartX = x + leftOffset;
        leftEndX = x + Math.min(splits[0], width - rightOffset);
        leftWidth = leftEndX - leftStartX;

        midStartX = x + Math.max(splits[0], leftOffset);
        midEndX = x + Math.min(width - splits[1], width - rightOffset);
        midWidth = midEndX - midStartX;

        rightStartX = x + Math.max(width - splits[1], leftOffset);
        rightEndX = x + width - rightOffset;
        rightWidth = rightEndX - rightStartX;

        // --- Texture coordinates (unchanged) ---
        v3 = region.getV();
        v2 = region.getV() + originalSplits[3] * sizeToV;
        v1 = region.getV2() - originalSplits[2] * sizeToV;
        v0 = region.getV2();
    }

    public void draw (Batch batch, float x, float y, float parentAlpha) {
        this.x = x;
        this.y = y;

        recalculate();
        drawInternal(batch, parentAlpha);
    }

    private void drawInternal (Batch batch, float parentAlpha) {
        float percent;
        // Draw left part
        if (leftWidth >= 1) {
            percent = leftWidth / splits[0];
            copyColor(tmpBatchColor, batch.getColor());
            batch.setColor(fillColor.r, fillColor.g, fillColor.b, parentAlpha * fillColor.a);
            batch.draw(region.getTexture(), leftStartX, topYStart, leftWidth, topHeight,
                region.getU(), v2, region.getU() + originalSplits[0] * sizeToU * percent, v3);
            batch.draw(region.getTexture(), leftStartX, midYStart, leftWidth, midHeight,
                region.getU(), v1, region.getU() + originalSplits[0] * sizeToU * percent, v2);
            batch.draw(region.getTexture(), leftStartX, botYStart, leftWidth, botHeight,
                region.getU(), v0, region.getU() + originalSplits[0] * sizeToU * percent, v1);
            batch.setColor(tmpBatchColor);
        }
        // Draw middle part
        if (midWidth >= 1) {
            percent = midWidth / (width - splits[0] - splits[1]);
            copyColor(tmpBatchColor, batch.getColor());
            batch.setColor(fillColor.r, fillColor.g, fillColor.b, parentAlpha * fillColor.a);
            batch.draw(region.getTexture(), midStartX, topYStart, midWidth, topHeight,
                region.getU() + originalSplits[0] * sizeToU, v2,
                region.getU() + originalSplits[0] * sizeToU + (region.getU2() - originalSplits[1] * sizeToU - region.getU() - originalSplits[0] * sizeToU) * percent, v3);
            batch.draw(region.getTexture(), midStartX, midYStart, midWidth, midHeight,
                region.getU() + originalSplits[0] * sizeToU, v1,
                region.getU() + originalSplits[0] * sizeToU + (region.getU2() - originalSplits[1] * sizeToU - region.getU() - originalSplits[0] * sizeToU) * percent, v2);
            batch.draw(region.getTexture(), midStartX, botYStart, midWidth, botHeight,
                region.getU() + originalSplits[0] * sizeToU, v0,
                region.getU() + originalSplits[0] * sizeToU + (region.getU2() - originalSplits[1] * sizeToU - region.getU() - originalSplits[0] * sizeToU) * percent, v1);
            batch.setColor(tmpBatchColor);
        }
        // Draw right part
        if (rightWidth >= 1) {
            percent = rightWidth / splits[1];
            copyColor(tmpBatchColor, batch.getColor());
            batch.setColor(fillColor.r, fillColor.g, fillColor.b, parentAlpha * fillColor.a);
            batch.draw(region.getTexture(), rightStartX, topYStart, rightWidth, topHeight,
                region.getU2() - originalSplits[1] * sizeToU, v2,
                region.getU2() - originalSplits[1] * sizeToU + originalSplits[1] * sizeToU * percent, v3);
            batch.draw(region.getTexture(), rightStartX, midYStart, rightWidth, midHeight,
                region.getU2() - originalSplits[1] * sizeToU, v1,
                region.getU2() - originalSplits[1] * sizeToU + originalSplits[1] * sizeToU * percent, v2);
            batch.draw(region.getTexture(), rightStartX, botYStart, rightWidth, botHeight,
                region.getU2() - originalSplits[1] * sizeToU, v0,
                region.getU2() - originalSplits[1] * sizeToU + originalSplits[1] * sizeToU * percent, v1);
            batch.setColor(tmpBatchColor);
        }
    }

    private static void copyColor (Color color1, Color color2) {
        color1.r = color2.r;
        color1.g = color2.g;
        color1.b = color2.b;
        color1.a = color2.a;
    }
}

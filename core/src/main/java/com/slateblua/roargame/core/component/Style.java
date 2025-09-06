package com.slateblua.roargame.core.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.slateblua.roargame.core.Resources;
import lombok.Getter;

public enum Style {
    BLUE_40_35_7_13(Shape.ROUNDED_40, Shape.ROUNDED_40_BORDER, Color.valueOf("#59a4f0"), Color.valueOf("#4589d1"), 13),

    // new pastel colors
    GREEN_PASTEL_35_30(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#9bd781"), Color.valueOf("#6b9d55"), 30),
    GREEN_PASTEL_35_30_NON_DISABLE(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#9bd781"), Color.valueOf("#6b9d55"), Color.valueOf("#9bd781"), Color.valueOf("#6b9d55"), 30),
    GREEN_PASTEL_35_15(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#9bd781"), Color.valueOf("#6b9d55"), 15),
    BLUE_PASTEL_35_30(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#43a6f2"), Color.valueOf("#2e8bd3"), 30),
    YELLOW_PASTEL_35_30(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#ddb46d"), Color.valueOf("#99825a"), 30),
    YELLOW_PASTEL_35_20(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#ddb46d"), Color.valueOf("#99825a"), 20),
    RED_PASTEL_35_30(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#d95454"), Color.valueOf("#b34140"), 39),
    RED_PASTEL_35_15(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#d95454"), Color.valueOf("#b34140"), 15),
    WHITE_GRAY_40_20(Shape.ROUNDED_40, Shape.ROUNDED_40_BORDER, Color.valueOf("#f4e6de"), Color.valueOf("#918377"), 20),
    WHITE_GRAY_35_30(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#f4e6de"), Color.valueOf("#918377"), 30),
    WHITE_GRAY_35_5(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#f4e6de"), Color.valueOf("#918377"), 5),
    YELLOW_35_20(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#ffbb4b"), Color.valueOf("#9e5f25"), 20),
    YELLOW_35_20_NON_DISABLE(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#ffbb4b"), Color.valueOf("#9e5f25"), Color.valueOf("#ffbb4b"), Color.valueOf("#9e5f25"), 20),
    YELLOW_50_28(Shape.ROUNDED_50, Shape.ROUNDED_50_BORDER, Color.valueOf("#ffbb4b"), Color.valueOf("#9e5f25"), 28),
    PURPLE_50_28(Shape.ROUNDED_50, Shape.ROUNDED_50_BORDER, Color.valueOf("#af8de7"), Color.valueOf("#68508e"), 28),
    PURPLE_35_20_NON_DISABLE(Shape.ROUNDED_35, Shape.ROUNDED_35_BORDER, Color.valueOf("#af8de7"), Color.valueOf("#68508e"), Color.valueOf("#af8de7"), Color.valueOf("#68508e"), 28),
    BLUE_50_28(Shape.ROUNDED_50, Shape.ROUNDED_50_BORDER, Color.valueOf("#5aa4f0"), Color.valueOf("#4589d1"), 28);

    @Getter
    private final String backgroundPath;
    @Getter
    private final String borderPath;
    @Getter
    private final Color enabledBorderColor;
    @Getter
    private final Color enabledBackgroundColor;
    @Getter
    private final Color disabledBorderColor;
    @Getter
    private final Color disabledBackgroundColor;
    @Getter
    private final float offset;

    Style (Shape backgroundPath, Shape borderPath, Color enabledBackgroundColor, Color enabledBorderColor, float offset) {
        this(backgroundPath, borderPath, enabledBackgroundColor, enabledBorderColor, null, null, offset);
    }

    Style (String backgroundPath, String borderPath, Color enabledBackgroundColor, Color enabledBorderColor, float offset) {
        this(backgroundPath, borderPath, enabledBackgroundColor, enabledBorderColor, null, null, offset);
    }

    Style (Shape backgroundPath, Shape borderPath, Color enabledBackgroundColor, Color enabledBorderColor, Color disabledBackgroundColor, Color disabledBorderColor, float offset) {
        this(backgroundPath.getRegionName(), borderPath.getRegionName(), enabledBackgroundColor, enabledBorderColor, disabledBackgroundColor, disabledBorderColor, offset);
    }

    Style (String backgroundPath, String borderPath, Color enabledBackgroundColor, Color enabledBorderColor, Color disabledBackgroundColor, Color disabledBorderColor, float offset) {
        this.backgroundPath = backgroundPath;
        this.borderPath = borderPath;
        this.enabledBackgroundColor = enabledBackgroundColor;
        this.enabledBorderColor = enabledBorderColor;
        this.disabledBackgroundColor = disabledBackgroundColor == null ? Color.valueOf("#BABABA") : disabledBackgroundColor;
        this.disabledBorderColor = disabledBorderColor == null ? Color.valueOf("#7D7D7D") : disabledBorderColor;
        this.offset = offset;
    }

    public Drawable getInnerBackground (boolean enabled) {
        if (enabled) {
            return getBackgroundDrawable(enabledBackgroundColor);
        }
        return getBackgroundDrawable(disabledBackgroundColor);
    }

    public Drawable getOuterBackground (boolean enabled) {
        if (enabled) {
            return getBackgroundDrawable(enabledBorderColor);
        }
        return getBackgroundDrawable(disabledBorderColor);
    }

    public Drawable getBorderDrawable (boolean enabled) {
        if (enabled) {
            return getBorderDrawable(enabledBorderColor);
        }
        return getBorderDrawable(disabledBorderColor);
    }

    public Drawable getBackgroundDrawable (Color color) {
        if (backgroundPath == null) return null;
        return Resources.getDrawable(backgroundPath, color);
    }

    public Drawable getBorderDrawable (Color color) {
        if (borderPath == null) return null;
        return Resources.getDrawable(borderPath, color);
    }
}

package com.slateblua.roargame.scenes.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.slateblua.roargame.Locator;
import com.slateblua.roargame.Resources;
import com.slateblua.roargame.scenes.NavController;
import com.slateblua.roargame.scenes.components.*;
import lombok.Getter;
import lombok.Setter;

public abstract class BasePage extends Table {
    // title
    protected Table titleSegment;

    // content
    protected Table contentWrapper;
    protected Table content;

    @Getter
    protected OffsetButton closeButton;

    @Getter
    protected Table dialog;
    protected Table overlayTable;

    @Getter
    private boolean shown;

    protected float duration = 0.08f;

    @Getter
    protected boolean selfHide = true;

    protected Cell<Table> titleSegmentCell;

    @Getter
    @Setter
    private boolean analyticsSilent;
    protected Table loadingContent;

    private final float backgroundAlpha = 1f;


    protected Table dialogBorder;


    public BasePage () {
        initialisation();
    }

    protected void initialisation() {
        initDialog();

        initCloseButton();

        // init overlay
        overlayTable = new Table();
        constructOverlay(overlayTable);

        // init title segment
        initTitleSegment();

        // init content
        initContentTable();
        constructContent(content);

        // wrap content
        contentWrapper = constructContentWrapper();

        // assemble dialog
        initDialogTable();
        constructDialog(dialog);

        constructLayout();
    }

    protected void initContentTable () {
        content = new Table();
    }

    protected void initDialogTable () {
        dialog = new Table();
    }

    protected void initTitleSegment () {
        titleSegment = new Table();
    }

    protected void constructLayout () {
        add(dialog);
        row();
    }

    @Override
    protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
        super.drawBackground(batch, backgroundAlpha, x, y);
    }

    protected void constructOverlay(Table overlayTable) {
        overlayTable.add(closeButton).expand().top().right().pad(20).padTop(18).size(100);
    }

    protected void initCloseButton () {
        // init close button
        closeButton = new IconButton(Style.RED_PASTEL_35_15, "core/ui-close-icon");
        closeButton.setOnClick(this::hide);
    }

    protected Table initDialogBorder (Drawable borderDrawable) {
        dialogBorder = new Table();
        dialogBorder.setBackground(borderDrawable);
        dialogBorder.setFillParent(true);
        contentWrapper.addActor(dialogBorder);

        content.setZIndex(0);
        dialogBorder.setZIndex(2);

        if (titleSegment != null) {
            titleSegment.setZIndex(1);
        }
        return dialogBorder;
    }

    protected Table initDialogBorder () {
        return initDialogBorder(Shape.ROUNDED_50_BORDER.getDrawable(Color.valueOf("#c2b8b0")));
    }
    protected void initDialog() {
        setBackground(Locator.get(Resources.class).obtainDrawable("components/ui-white-pixel", Color.valueOf("#000000bf")));
        setTouchable(Touchable.enabled);
        setFillParent(true);
    }

    protected Table constructContentWrapper() {
        final Table asyncContentWrapper = new Table();
        asyncContentWrapper.add(content).grow();

        final Table contentWrapper = new Table();
        titleSegmentCell = contentWrapper.add(titleSegment).growX();
        contentWrapper.row();
        contentWrapper.add(asyncContentWrapper).grow();

        setMainState();

        return contentWrapper;
    }

    protected boolean isAsync() {
        return false;
    }


    public void setMainState() {
        if(loadingContent != null) loadingContent.setVisible(false);
        content.setVisible(true);
    }

    protected void constructDialog(Table dialog) {
        dialog.setBackground(getDialogBackground());
        dialog.setTouchable(Touchable.enabled);
        dialog.stack(contentWrapper, overlayTable).grow();
    }

    protected Drawable getDialogBackground() {
        return Shape.ROUNDED_50.getDrawable(Color.valueOf("cdcdcc"));
    }

    protected abstract void constructContent(Table content);

    public void show () {
        resetDialogStartAnimationState();
        shown = true;

        NavController.addDialog(this);

        if (dialog.hasParent()) {
            dialog.addAction(Actions.parallel(
                Actions.fadeIn(duration),
                Actions.sequence(
                    Actions.scaleTo(1f, 1f, duration),
                    Actions.run(this::onShowAnimationComplete)
                )
            ));
        } else {
            onShowAnimationComplete();
        }
    }


    private void resetDialogStartAnimationState () {
        dialog.pack();
        dialog.setTransform(true);
        dialog.setScale(0.9f);
        dialog.getColor().a = 0.0f;
        dialog.setOrigin(Align.center);
    }


    private void onShowAnimationComplete () {
        dialog.setTransform(false);
    }

    public void hide () {
        shown = false;
        NavController.close(this);
    }
}

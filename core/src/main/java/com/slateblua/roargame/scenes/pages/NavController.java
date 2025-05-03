package com.slateblua.roargame.scenes.pages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import lombok.Getter;

import java.util.Stack;

public class NavController {
    @Getter
    private final Stage stage;

    @Getter
    private final Table rootTable;

    private final Stack<BasePage> pageStack = new Stack<>();

    private final Stack<BasePopup> popupStack = new Stack<>();

    private final ObjectMap<Class<? extends BasePage>, BasePage> pageCash = new ObjectMap<>();
    private final ObjectMap<Class<? extends BasePopup>, BasePopup> popupCash = new ObjectMap<>();

    public NavController (final Stage stage, final Table rootTable) {
        this.stage = stage;
        this.rootTable = rootTable;

        // Configure the root table
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
    }

    public void showPopup (final Class<? extends BasePopup> popupClass) {
        if (popupCash.containsKey(popupClass)) {
            showPopup(popupCash.get(popupClass));
        } else {
            try {
                final BasePopup basePopup = ClassReflection.newInstance(popupClass);
                popupCash.put(popupClass, basePopup);
                showPopup(basePopup);
            } catch (final ReflectionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void openPage (final Class<? extends BasePage> pageClass) {
        if (pageCash.containsKey(pageClass)) {
            openPage(pageCash.get(pageClass));
        } else {
            try {
                final BasePage basePage = ClassReflection.newInstance(pageClass);
                pageCash.put(pageClass, basePage);
                openPage(basePage);
            } catch (final ReflectionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Navigate to a new page, adding it to the stack
     *
     * @param page The page to navigate to
     */
    public void openPage (BasePage page) {
        // Hide current page if exists
        if (!pageStack.isEmpty()) {
            pageStack.peek().onHide();
        }

        // Clear the root table but keep its properties
        rootTable.clearChildren();

        // Add the new page to the root table
        rootTable.add(page).expand().fill();

        // Add to navigation stack
        pageStack.push(page);

        // Notify page it's now visible
        page.onShow();
    }

    /**
     * Navigate back to the previous page
     */
    public void back () {
        if (pageStack.size() <= 1) {
            return;
        }

        // Remove current page
        pageStack.pop().onHide();

        // Get the previous page
        BasePage previousPage = pageStack.peek();

        // Clear the root table but keep its properties
        rootTable.clearChildren();

        // Add the previous page to the root table
        rootTable.add(previousPage).expand().fill();

        // Notify page it's now visible
        previousPage.onShow();
    }

    /**
     * Show a popup on top of the current page
     *
     * @param popup The dialog to show
     */
    public void showPopup (BasePopup popup) {
        popupStack.push(popup);
        stage.addActor(popup);
        popup.onShow();
    }

    /**
     * Dismiss the most recent dialog
     *
     * @return true if successful, false if there's no dialog to dismiss
     */
    public boolean closePopup () {
        if (popupStack.isEmpty()) {
            return false;
        }

        BasePopup dialog = popupStack.pop();
        dialog.onHide();
        dialog.remove();
        return true;
    }

    /**
     * Dismiss all open popups
     */
    public void closeAllPopups () {
        while (!popupStack.isEmpty()) {
            BasePopup dialog = popupStack.pop();
            dialog.onHide();
            dialog.remove();
        }
    }
}

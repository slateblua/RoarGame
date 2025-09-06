package com.slateblua.roargame.scenes;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.slateblua.roargame.core.Locator;
import com.slateblua.roargame.core.component.BasePage;
import lombok.Getter;

public class NavController {

    private final ObjectMap<Class<? extends BasePage>, BasePage> cache = new ObjectMap<>();
    private Table dialogContainer;
    @Getter
    private final Array<BasePage> openedDialogs = new Array<>();

    public void init (Table dialogContainer) {
        this.dialogContainer = dialogContainer;
    }

    public static <T extends BasePage> T show (T dialog) {
        dialog.show();
        return dialog;
    }

    public static <T extends BasePage> T show (Class<T> clazz) {
        return show(get(clazz));
    }

    public static void close (BasePage dialog) {
        dialog.remove();

        final NavController dialogManager = Locator.get(NavController.class);
        dialogManager.openedDialogs.removeValue(dialog, true);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasePage> T get (Class<T> clazz) {
        final NavController dialogManager = Locator.get(NavController.class);
        if (!dialogManager.cache.containsKey(clazz)) {
            try {
                final BasePage dialog = ClassReflection.newInstance(clazz);
                dialogManager.cache.put(clazz, dialog);
                return (T) dialog;
            } catch (ReflectionException e) {
                throw new RuntimeException(e);
            }
        }
        return (T) dialogManager.cache.get(clazz);
    }

    public static void addDialog (BasePage dialog) {
        final NavController dialogManager = Locator.get(NavController.class);
        final Table dialogContainer = dialogManager.dialogContainer;
        dialogManager.openedDialogs.add(dialog);

        dialogContainer.addActor(dialog);
    }
}

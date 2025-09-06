package com.slateblua.roargame.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * The Locator class serves as a Dependency Injection container for managing
 * instances of various objects in the application. It allows registering
 * and retrieving modules by their class types, handling dependencies efficiently.
 */
public class Locator implements Disposable {
    private final ObjectMap<Class<?>, Object> dependencyInstanceMap = new ObjectMap<>();
    private final ObjectMap<Class<?>, Class<?>> dependencyClassMap = new ObjectMap<>();

    private static Locator locatorInstance;
    private static final Array<Class<?>> tempArray = new Array<>();

    /**
     * Returns the singleton instance of the Locator class.
     *
     * @return singleton instance of Locator
     */
    public static Locator get () {
        if (locatorInstance == null) {
            locatorInstance = new Locator();
        }
        return locatorInstance;
    }

    private Locator () {
    }

    /**
     * Retrieves an instance of the specified class from the dependency map.
     *
     * @param clazz the class type to retrieve
     * @param <U>   the upper bound type of the class to retrieve
     * @param <T>   the type of instance returned
     * @return the instance of the specified class, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <U, T extends U> T get (Class<? extends U> clazz) {
        Locator instance = get();
        Object object = instance.dependencyInstanceMap.get(clazz);

        ObjectMap<Class<?>, Object> classMap = locatorInstance.dependencyInstanceMap;

        if (object == null) {
            Class<?> assignable = instance.dependencyClassMap.get(clazz);
            if (assignable != null) {
                // take from cache
                return (T) classMap.get(assignable);
            }

            tempArray.clear();
            for (final ObjectMap.Entry<Class<?>, Object> classObjectEntry : classMap) {
                tempArray.add(classObjectEntry.key);
            }

            for (final Class<?> aClass : tempArray) {
                if (clazz.isAssignableFrom(aClass)) {
                    // cache this
                    instance.dependencyClassMap.put(clazz, aClass);
                    return (T) classMap.get(aClass);
                }
            }

            return null;
        }

        return (T) instance.dependencyInstanceMap.get(clazz);
    }

    /**
     * Adds a module instance to the dependency map associated with the specified key.
     *
     * @param key    the class type key to associate the instance with
     * @param object the instance to store
     * @param <T>    the type of the instance
     * @return the added instance
     */
    public <T> T addModule (final Class<?> key, final T object) {
        if (!dependencyInstanceMap.containsKey(key)) {
            dependencyInstanceMap.put(key, object);
        }
        return object;
    }

    /**
     * Adds a module class to the dependency map by instantiating the class.
     *
     * @param clazz the class type to instantiate and store
     * @param <T>   the type of the class
     */
    public <T> void addModule (Class<?> clazz) {
        if (!dependencyInstanceMap.containsKey(clazz)) {
            try {
                final Object object = ClassReflection.newInstance(clazz);
                dependencyInstanceMap.put(clazz, object);
                return;
            } catch (ReflectionException e) {
                throw new RuntimeException();
            }
        }
        dependencyInstanceMap.get(clazz);
    }

    /**
     * Adds a module instance to the dependency map, using its class type as the key.
     *
     * @param object the instance to store
     * @param <T>    the type of the instance
     * @return the added instance
     */
    public <T> T addModule (final T object) {
        return addModule(object.getClass(), object);
    }

    /**
     * Disposes all instances in the dependency map that implement the Disposable interface.
     */
    @Override
    public void dispose () {
        final OrderedMap<Class<?>, Object> copy = new OrderedMap<>();
        copy.putAll(dependencyInstanceMap);

        for (ObjectMap.Entry<Class<?>, Object> classObjectEntry : copy) {
            final Object object = classObjectEntry.value;
            ((Disposable) object).dispose();
        }

        dependencyClassMap.clear();
        dependencyInstanceMap.clear();
        locatorInstance = null;
    }

    /**
     * Removes a module of the specified class type from the dependency map.
     *
     * @param clazz the class type to remove
     */
    public void remove (Class<?> clazz) {
        final Object object = dependencyInstanceMap.get(clazz);
        if (object instanceof Disposable) {
            ((Disposable) object).dispose();
        }
        dependencyInstanceMap.remove(clazz);

        dependencyClassMap.remove(clazz);

        final Class<?> keyByValue = dependencyClassMap.findKey(clazz, true);
        if (keyByValue != null) {
            dependencyClassMap.remove(keyByValue);
        }
    }
}

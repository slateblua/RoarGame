package com.slateblua.roargame.core.systems;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

import java.lang.reflect.Method;

public class EventSystem {

    // Listener entry (cached)
    private static class ListenerEntry {
        Object listenerObject;
        Method method;
        Class<? extends Event> eventType;

        ListenerEntry (Object listenerObject, Method method, Class<? extends Event> eventType) {
            this.listenerObject = listenerObject;
            this.method = method;
            this.eventType = eventType;
        }
    }

    private final Array<ListenerEntry> listeners = new Array<>();
    private final PoolMap poolMap = new PoolMap();

    // Register an object that has @Subscribe methods
    public void register (Object listenerObject) {
        for (Method method : listenerObject.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) {
                    throw new IllegalArgumentException("Methods annotated with @Subscribe must have exactly one Event parameter: " + method);
                }
                method.setAccessible(true); // Allow calling private methods
                @SuppressWarnings("unchecked")
                Class<? extends Event> eventType = (Class<? extends Event>) params[0];
                listeners.add(new ListenerEntry(listenerObject, method, eventType));
            }
        }
    }

    // Fire an event
    public <T extends Event> void fire (T event) {
        for (int i = 0; i < listeners.size; i++) {
            ListenerEntry entry = listeners.get(i);
            if (entry.eventType.isInstance(event)) {
                try {
                    entry.method.invoke(entry.listenerObject, event);
                } catch (Exception e) {
                    throw new RuntimeException("Error invoking event listener", e);
                }
            }
        }
        pool(event);
    }

    // Obtain and pool
    public <T extends Event> T obtain (Class<T> type) {
        return poolMap.obtain(type);
    }

    private <T extends Event> void pool (T event) {
        poolMap.free(event);
    }

    // Pool manager
    private static class PoolMap {
        private final ObjectMap<Class<? extends Event>, Pool<? extends Event>> pools = new ObjectMap<>();

        @SuppressWarnings("unchecked")
        public <T extends Event> T obtain (Class<T> type) {
            Pool<T> pool = (Pool<T>) pools.get(type);
            if (pool == null) {
                pool = new Pool<T>() {
                    @Override
                    protected T newObject () {
                        try {
                            return type.newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot instantiate event: " + type, e);
                        }
                    }
                };
                pools.put(type, pool);
            }
            return pool.obtain();
        }

        @SuppressWarnings("unchecked")
        public <T extends Event> void free (T event) {
            Pool<T> pool = (Pool<T>) pools.get(event.getClass());
            if (pool != null) {
                pool.free(event);
            }
        }
    }
}

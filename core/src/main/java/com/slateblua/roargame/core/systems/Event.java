package com.slateblua.roargame.core.systems;

import com.badlogic.gdx.utils.Pool;

// Base Event
public abstract class Event implements Pool.Poolable {
    @Override
    public void reset () {
        // Reset fields if needed
    }
}

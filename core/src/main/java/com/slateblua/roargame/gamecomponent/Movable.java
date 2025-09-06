package com.slateblua.roargame.gamecomponent;

import com.badlogic.gdx.math.Vector2;

public interface Movable {
    Vector2 getPosition ();

    float getWidth ();

    float getHeight ();

    default float getSize () {
        return Math.max(getHeight(), getWidth());
    }
}

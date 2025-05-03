package com.slateblua.roargame.systems;

import com.slateblua.roargame.Movable;

/**
 * Handles collision detection between two movable objects.
 */
public class CollisionHandler {
    /**
     * Checks if two {@link Movable} objects are colliding based on their positions and sizes.
     *
     * @param movable      The first {@link Movable} object.
     * @param otherMovable The second {@link Movable} object.
     * @return {@code true} if the two objects are colliding; {@code false} otherwise.
     */
    public boolean checkCollision (final Movable movable, final Movable otherMovable) {
        float dst = movable.getPosition().dst(otherMovable.getPosition());
        return dst < (movable.getWidth() + otherMovable.getWidth()) / 2;
    }
}

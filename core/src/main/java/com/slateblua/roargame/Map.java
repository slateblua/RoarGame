package com.slateblua.roargame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {
    private static final int TILE_SIZE = 256;

    final TextureRegion region = Locator.get(Resources.class).getTexture("core/map_tile");

    public void render (SpriteBatch batch, OrthographicCamera camera) {
        // Calculate visible area based on camera
        int startX = (int) (camera.position.x - camera.viewportWidth / 2) / TILE_SIZE - 1;
        int startY = (int) (camera.position.y - camera.viewportHeight / 2) / TILE_SIZE - 1;
        int endX = (int) (camera.position.x + camera.viewportWidth / 2) / TILE_SIZE + 1;
        int endY = (int) (camera.position.y + camera.viewportHeight / 2) / TILE_SIZE + 1;

        // Render visible tiles
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                batch.setColor(Color.WHITE);
                batch.draw(
                    region,
                    x * TILE_SIZE,
                    y * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
                );
                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

}

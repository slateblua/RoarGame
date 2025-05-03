package com.slateblua.roargame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.Getter;

/**
 * The Resources class handles the loading, caching, and management of game assets such as
 * texture atlases, skins, drawables, and texture regions. It ensures that assets are efficiently
 * reused to minimize memory usage and improve performance.
 */
public class Resources {
    private Skin skin;

    @Getter
    private TextureAtlas atlas;

    @Getter
    private final AssetManager assetManager;

    private final ObjectMap<String, Drawable> drawableCache = new ObjectMap<>();
    private final ObjectMap<String, ObjectMap<Color, String>> drawableKeyCache = new ObjectMap<>();
    private final ObjectMap<String, TextureRegion> textureRegionCache = new ObjectMap<>();

    /**
     * Initializes a new Resources instance and starts loading the primary texture atlas.
     */
    public Resources () {
        this.assetManager = new AssetManager();
        assetManager.load("gameassets/gameatlas.atlas", TextureAtlas.class);
    }

    /**
     * Updates the asset loading process and finalizes asset setup when done.
     *
     * @param millis the time in milliseconds to perform the loading update
     * @return true if asset loading is complete, false otherwise
     */
    public boolean update (int millis) {
        if (assetManager.update(millis)) {
            onUpdateFinished();
            return true;
        }

        return false;
    }

    /**
     * Called when asset loading is finished, finalizing the loading of the atlas and skin.
     */
    private void onUpdateFinished () {
        assetManager.finishLoading();

        atlas = assetManager.get("gameassets/gameatlas.atlas");
        skin = new Skin(atlas);
    }

    /**
     * Retrieves a white-colored drawable by its region name from the skin.
     *
     * @param regionName the name of the region to retrieve
     * @return the corresponding Drawable
     */
    public static Drawable getDrawable (final String regionName) {
        return Locator.get(Resources.class).obtainDrawable(regionName, Color.WHITE);
    }

    /**
     * Retrieves a colored drawable by its region name and specified color.
     *
     * @param regionName the name of the region to retrieve
     * @param color      the color to apply to the drawable
     * @return the corresponding Drawable
     */
    public static Drawable getDrawable (String regionName, Color color) {
        return Locator.get(Resources.class).obtainDrawable(regionName, color);
    }

    /**
     * Obtains a drawable with the specified region and color, generating and caching it if necessary.
     *
     * @param region the region name of the drawable
     * @param color  the color to apply to the drawable
     * @return the corresponding Drawable
     */
    public Drawable obtainDrawable (String region, Color color) {
        final String key = getKeyFromRegionColor(region, color);

        if (drawableCache.containsKey(key)) {
            return drawableCache.get(key);
        }

        final Drawable newDrawable = skin.newDrawable(region, color);
        drawableCache.put(key, newDrawable);

        return newDrawable;
    }

    /**
     * Obtains a drawable with the specified region, color, and a fallback default region.
     * The default region is used if the specified region does not exist.
     *
     * @param region        the region name of the drawable
     * @param color         the color to apply to the drawable
     * @param defaultRegion the fallback default region if the original region doesn't exist
     * @return the corresponding Drawable
     */
    public Drawable obtainDrawable (String region, Color color, String defaultRegion) {
        final String key = getKeyFromRegionColor(region, color);

        if (drawableCache.containsKey(key)) {
            return drawableCache.get(key);
        }

        final boolean hasRegion = skin.has(region, TextureRegion.class);
        final String regionToUse = hasRegion ? region : defaultRegion;
        final Drawable drawable = skin.newDrawable(regionToUse, color);
        drawableCache.put(key, drawable);

        return drawable;
    }

    /**
     * Retrieves a texture region by its name, generating and caching it if necessary.
     *
     * @param regionName the name of the texture region to retrieve
     * @return the corresponding TextureRegion
     */
    public TextureRegion getTexture (String regionName) {
        if (textureRegionCache.containsKey(regionName)) {
            return textureRegionCache.get(regionName);
        }

        final TextureRegion region = skin.getRegion(regionName);
        textureRegionCache.put(regionName, region);

        return region;
    }

    /**
     * Generates a unique key for caching based on the region name and color.
     *
     * @param region the region name
     * @param color  the color to apply
     * @return the unique key as a String
     */
    private String getKeyFromRegionColor (String region, Color color) {
        if (!drawableKeyCache.containsKey(region)) {
            drawableKeyCache.put(region, new ObjectMap<>());
        }
        if (!drawableKeyCache.get(region).containsKey(color)) {
            drawableKeyCache.get(region).put(color, region + color.toString());
        }

        return drawableKeyCache.get(region).get(color);
    }
}

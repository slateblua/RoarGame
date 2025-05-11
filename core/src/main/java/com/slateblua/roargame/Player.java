package com.slateblua.roargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slateblua.roargame.bonus.Bonus;
import com.slateblua.roargame.weapon.*;
import lombok.Getter;
import lombok.Setter;

public class Player implements Movable {
    public static final float SPEED = 400f;
    public static final float PLAYER_BOX_SIZE = 200f;

    @Getter
    private final Vector2 position;
    private final  Vector2 velocity;
    @Getter
    private int health;
    @Getter
    private int energy;
    @Getter
    private int score;

    @Getter @Setter
    private Weapon currentWeapon;


    private Player () {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        GameData gameData = Locator.get(GameData.class);
        this.currentWeapon = gameData.getWeaponMap().get(gameData.getWeapons().first());
        this.health = 100;
        this.energy = 0;
        this.score = 0;
    }

    private static Player player;

    public static Player get () {
        if (player == null) {
            player = new Player();
        }
        return player;
    }

    public void update (float deltaTime) {
        // Handle player movement
        handleInput();
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    final TextureRegion region = Locator.get(Resources.class).getTexture("core/character_hero");

    public void render (SpriteBatch batch) {
        batch.draw(
            region,
            position.x - PLAYER_BOX_SIZE / 2,
            position.y - PLAYER_BOX_SIZE / 2,
            PLAYER_BOX_SIZE,
            PLAYER_BOX_SIZE
        );
    }

    // temporary
    private void handleInput () {
        // Reset velocity
        velocity.set(0, 0);

        // Movement input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = -SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = SPEED;
        }

        // Normalize diagonal movement
        if (velocity.len() > SPEED) {
            velocity.nor().scl(SPEED);
        }
    }

    public Bullet shoot () {
        // Get mouse position in world coordinates
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        // Convert screen coordinates to world coordinates
        // Note: This is a simplified version. In a real game, you'd use camera.unproject
        Vector2 mousePosition = new Vector2(mouseX, Gdx.graphics.getHeight() - mouseY);

        // Calculate direction
        Vector2 direction = new Vector2(mousePosition).sub(new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2)).nor();

        // Fire weapon
        return getCurrentWeapon().shoot(position.cpy(), direction);
    }

    public boolean canShoot () {
        return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    public void takeDamage (int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public void collectBonus (Bonus bonus) {
        energy += bonus.getEnergyAmount();
        score += bonus.getScoreValue();
    }

    public void useEnergy (int amount) {
        energy -= amount;
        if (energy < 0) {
            energy = 0;
        }
    }

    public void addScore (int value) {
        score += value;
    }

    @Override
    public float getWidth () {
        return PLAYER_BOX_SIZE;
    }

    @Override
    public float getHeight () {
        return PLAYER_BOX_SIZE;
    }
}

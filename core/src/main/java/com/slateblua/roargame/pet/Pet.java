package com.slateblua.roargame.pet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slateblua.roargame.*;
import com.slateblua.roargame.enemy.BaseEnemy;
import com.slateblua.roargame.systems.EventSystem;
import com.slateblua.roargame.systems.Subscribe;
import lombok.Getter;
import lombok.Setter;

public class Pet implements Movable {
    @Setter
    private PetData data;

    @Getter
    private final Vector2 position;
    private final Vector2 velocity;
    private float attackTimer;

    private float rotationAngle;

    public Pet (Vector2 position) {
        this.position = position;
        this.velocity = new Vector2();
        this.attackTimer = 0;
        this.rotationAngle = 0;

        Locator.get(EventSystem.class).register(this);

        final String selectedPet = "pet_story_cat";

        data = Locator.get(GameData.class).getPetMap().get(new PetData.PetId(selectedPet));
    }

    public void update (float deltaTime, Vector2 playerPosition, Array<BaseEnemy> enemies) {
        // Find the closest enemy
        BaseEnemy closestEnemy = null;
        float closestDistance = Float.MAX_VALUE;

        for (BaseEnemy enemy : enemies) {
            float distance = position.dst(enemy.getPosition());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = enemy;
            }
        }

        // Move towards closest enemy or orbit player if no enemies
        if (closestEnemy != null) {
            Vector2 direction = new Vector2(closestEnemy.getPosition()).sub(position).nor();
            velocity.set(direction).scl(400f);
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);

            // Attack closest enemy
            attackTimer += deltaTime;
            if (attackTimer >= 0.5f) {
                attackTimer = 0;
                if (position.dst(closestEnemy.getPosition()) < PET_BOX_SIZE + closestEnemy.getWidth()) {
                    closestEnemy.takeDamage(15);
                }
            }
        } else {
            // Orbit around player
            rotationAngle += deltaTime * 2.0f;
            float orbitRadius = 80.0f;
            position.x = playerPosition.x + (float) Math.cos(rotationAngle) * orbitRadius;
            position.y = playerPosition.y + (float) Math.sin(rotationAngle) * orbitRadius;
        }
    }

    public void render (SpriteBatch batch) {
            batch.draw(
                data.getTexture(),
                position.x - PET_BOX_SIZE / 2,
                position.y - PET_BOX_SIZE / 2 + 120,
                PET_BOX_SIZE,
                PET_BOX_SIZE
            );
    }

    public static final float PET_BOX_SIZE = 120f;

    @Override
    public float getWidth () {
        return PET_BOX_SIZE;
    }

    @Override
    public float getHeight () {
        return PET_BOX_SIZE;
    }

    @Subscribe
    public void onPetChanged (final PetChangedEvent event) {
        setData(Locator.get(GameData.class).getPetMap().get(event.getNewPet()));
    }
}

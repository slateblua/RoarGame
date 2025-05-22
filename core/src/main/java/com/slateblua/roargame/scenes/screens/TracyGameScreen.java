package com.slateblua.roargame.scenes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slateblua.roargame.GameUI;
import com.slateblua.roargame.GameMap;
import com.slateblua.roargame.Player;
import com.slateblua.roargame.RoarGame;
import com.slateblua.roargame.bonus.Bonus;
import com.slateblua.roargame.pet.Pet;
import com.slateblua.roargame.enemy.BaseEnemy;
import com.slateblua.roargame.enemy.EnemyFactory;
import com.slateblua.roargame.systems.CollisionHandler;
import com.slateblua.roargame.weapon.Bullet;

public class TracyGameScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final Stage stage;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final GameUI gameUI;

    private final Player player;
    private final GameMap map;
    private final EnemyFactory enemyFactory;
    private final CollisionHandler collisionHandler;

    private final Array<BaseEnemy> enemies;
    private final Array<Bonus> bonuses;
    private final Array<Bullet> projectiles;
    private Pet innerSpirit;

    private float enemySpawnTimer;
    private float bonusSpawnTimer;
    private boolean innerSpiritActive;
    private float innerSpiritTimer;

    private float autoShootTimer = 0f; // Timer for auto-shooting
    private static final float AUTO_SHOOT_COOLDOWN = 0.8f; // Cooldown in seconds

    public TracyGameScreen(final RoarGame game) {
        this.batch = game.getBatch();

        final float decisionAspect = 1440.0f / 2560.0f;
        final float width, height;

        float aspect = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();

        if (aspect < decisionAspect) {
            width = 1440;
            height = aspect * width;
        } else {
            height = 2560;
            width = height * aspect;
        }

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(width, height, camera);
        stage = new Stage(viewport, batch);

        // Create game UI
        gameUI = GameUI.get();

        // Create entities
        player = Player.get();
        map = new GameMap();
        enemyFactory = new EnemyFactory();
        collisionHandler = new CollisionHandler();

        enemies = new Array<>();
        bonuses = new Array<>();
        projectiles = new Array<>();

        innerSpiritActive = false;
        innerSpiritTimer = 0;
        enemySpawnTimer = 0;
        bonusSpawnTimer = 0;
    }

    private final Vector3 cameraTarget = new Vector3();

    @Override
    public void render(float delta) {
        update(delta);
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Smooth camera follow with delay
        cameraTarget.set(player.getPosition().x, player.getPosition().y, 0);
        camera.position.lerp(cameraTarget, 2f * delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderWorld();
        batch.end();

        // Render UI on top of the game world
        gameUI.render();
    }

    private void update (float delta) {
        // Update player
        player.update(delta);

        // Update enemies
        for (int i = enemies.size - 1; i >= 0; i--) {
            final BaseEnemy enemy = enemies.get(i);
            enemy.update(delta, player.getPosition());

            // Check if enemy is dead
            if (enemy.getHealth() <= 0) {
                enemies.removeIndex(i);
                player.addScore(enemy.getScoreValue());
            }
        }

        // Update projectiles
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Bullet projectile = projectiles.get(i);
            projectile.update(delta);

            // Check if projectile is out of bounds or has expired
            if (projectile.isExpired()) {
                projectiles.removeIndex(i);
            }
        }

        // Update inner spirit if active
        if (innerSpiritActive) {
            innerSpirit.update(delta, player.getPosition(), enemies);
            innerSpiritTimer -= delta;

            if (innerSpiritTimer <= 0) {
                innerSpiritActive = false;
            }
        }

        // Handle collisions
        handleCollisions();

        // Spawn enemies
        enemySpawnTimer += delta;
        if (enemySpawnTimer >= 10f) {
            spawnEnemy();
            enemySpawnTimer = 0;
        }

        // Spawn bonuses
        bonusSpawnTimer += delta;
        if (bonusSpawnTimer >= 10f) {
            spawnBonus();
            bonusSpawnTimer = 0;
        }

        // Check if player can summon inner spirit
        if (!innerSpiritActive && player.getEnergy() >= PET_ENERGY_COST) {
            spawnPet();
        }

        autoShootTimer -= delta;
        // Auto-shoot logic
        autoShoot();
    }

    private void autoShoot () {
        // Auto-shoot logic
        if (autoShootTimer <= 0 && !enemies.isEmpty() && !innerSpiritActive) {
            BaseEnemy closestEnemy = findClosestEnemy();
            if (closestEnemy != null) {
                final Vector2 direction = new Vector2(closestEnemy.getPosition())
                    .sub(player.getPosition())
                    .nor();

                // Create and configure the bullet
                final Bullet bullet = player.shoot(direction);
                if (bullet != null) {
                    projectiles.add(bullet);
                    autoShootTimer = AUTO_SHOOT_COOLDOWN;
                }
            }
        }
    }

    private static final int PET_ENERGY_COST = 100;

    private void renderWorld () {
        // Render map
        map.render(batch, camera);

        // Render bonuses
        for (Bonus bonus : bonuses) {
            bonus.render(batch);
        }

        // Render enemies
        for (BaseEnemy enemy : enemies) {
            enemy.render(batch);
        }

        // Render projectiles
        for (Bullet projectile : projectiles) {
            projectile.render(batch);
        }

        // Render inner spirit if active
        if (innerSpiritActive) {
            innerSpirit.render(batch);
        }
        // Render player
        player.render(batch);
    }

    private BaseEnemy findClosestEnemy() {
        if (enemies.isEmpty()) {
            return null;
        }

        BaseEnemy closest = null;
        float closestDistSq = Float.MAX_VALUE;
        Vector2 playerPos = player.getPosition();

        for (BaseEnemy enemy : enemies) {
            float distSq = playerPos.dst2(enemy.getPosition());
            if (distSq < closestDistSq) {
                closestDistSq = distSq;
                closest = enemy;
            }
        }

        return closest;
    }

    private void handleCollisions () {
        // Check projectile collisions with enemies
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Bullet projectile = projectiles.get(i);
            boolean hit = false;

            for (int j = enemies.size - 1; j >= 0; j--) {
                BaseEnemy enemy = enemies.get(j);
                if (collisionHandler.checkCollision(projectile, enemy)) {
                    enemy.takeDamage(projectile.getDamage());
                    hit = true;
                    break;
                }
            }

            if (hit) {
                Pools.free(projectiles.get(i));
                projectiles.removeIndex(i);
            }
        }

        // Check player collisions with enemies
        for (final BaseEnemy enemy : enemies) {
            if (collisionHandler.checkCollision(player, enemy)) {
                player.takeDamage(enemy.getDamage());
                enemy.takeDamage(enemy.getHealth()); // Kill enemy on collision
            }
        }

        // Check player collisions with bonuses
        for (int i = bonuses.size - 1; i >= 0; i--) {
            final Bonus bonus = bonuses.get(i);
            if (collisionHandler.checkCollision(player, bonus)) {
                player.collectBonus(bonus);
                bonuses.removeIndex(i);
            }
        }
    }

    private void spawnEnemy () {
        // Calculate spawn position (outside screen but within spawn radius)
        float angle = (float) (Math.random() * Math.PI * 2);
        float distance = 450f;
        float x = player.getPosition().x + (float) Math.cos(angle) * distance;
        float y = player.getPosition().y + (float) Math.sin(angle) * distance;

        // Create random enemy type
        BaseEnemy enemy = enemyFactory.createRandomEnemy(new Vector2(x, y));
        enemies.add(enemy);
    }

    private static final float BONUS_SPAWN_RADIUS = 600f;

    private void spawnBonus () {
        // Calculate spawn position (outside screen but within spawn radius)
        float angle = (float) (Math.random() * Math.PI * 2);
        float distance = (float) (Math.random() * BONUS_SPAWN_RADIUS);
        float x = player.getPosition().x + (float) Math.cos(angle) * distance;
        float y = player.getPosition().y + (float) Math.sin(angle) * distance;

        // Create bonus
        Bonus bonus = new Bonus(new Vector2(x, y));
        bonuses.add(bonus);
    }

    private void spawnPet () {
        innerSpirit = new Pet(player.getPosition().cpy());
        innerSpiritActive = true;
        innerSpiritTimer = 20f;
        player.useEnergy(PET_ENERGY_COST);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        gameUI.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        gameUI.dispose();
    }
}

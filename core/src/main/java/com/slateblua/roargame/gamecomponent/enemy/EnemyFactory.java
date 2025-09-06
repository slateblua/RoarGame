package com.slateblua.roargame.gamecomponent.enemy;

import com.badlogic.gdx.math.Vector2;

public class EnemyFactory {
    public BaseEnemy createRandomEnemy(Vector2 position) {
        int randomType = (int) (Math.random() * 4);

        switch (randomType) {
            case 1:
                return new TraumaEnemy(position);
            case 2:
                return new FearEnemy(position);
            case 3:
                return new DoubtEnemy(position);
            default:
                return new StressEnemy(position);
        }
    }
}

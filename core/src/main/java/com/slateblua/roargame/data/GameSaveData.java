package com.slateblua.roargame.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameSaveData implements Serializable {
    @Setter
    private String selectedPetId;
    private int totalScore;
    private long lastSaveTime;

    public GameSaveData () {
        this.selectedPetId = "pet_story_cat";
        this.totalScore = 0;
        this.lastSaveTime = System.currentTimeMillis();
    }

    public void addScore (int score) {
        this.totalScore += score;
    }

    public void updateLastSaveTime () {
        this.lastSaveTime = System.currentTimeMillis();
    }

    @Override
    public void write (Json json) {
        json.writeValue("selectedPetId", selectedPetId);
        json.writeValue("totalScore", totalScore);
        json.writeValue("lastSaveTime", lastSaveTime);
    }

    @Override
    public void read (Json json, JsonValue jsonData) {
        selectedPetId = json.readValue("selectedPetId", String.class, jsonData);
        totalScore = json.readValue("totalScore", Integer.class, jsonData);
        lastSaveTime = json.readValue("lastSaveTime", Long.class, jsonData);
    }
}

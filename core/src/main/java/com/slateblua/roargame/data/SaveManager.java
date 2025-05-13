package com.slateblua.roargame.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import lombok.Getter;

public class SaveManager {
    private static final String SAVE_FILE = "savedata.json";
    private static SaveManager instance;
    private final Json json;
    @Getter
    private GameSaveData currentSaveData;

    private SaveManager() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        currentSaveData = new GameSaveData();
        loadGame();
    }

    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    public void saveGame() {
        try {
            final FileHandle file = Gdx.files.local(SAVE_FILE);
            currentSaveData.updateLastSaveTime();
            String jsonString = json.toJson(currentSaveData);
            file.writeString(jsonString, false);
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Error saving game data", e);
        }
    }

    public void loadGame() {
        try {
            final FileHandle file = Gdx.files.local(SAVE_FILE);
            if (file.exists()) {
                String jsonString = file.readString();
                currentSaveData = json.fromJson(GameSaveData.class, jsonString);
            } else {
                currentSaveData = new GameSaveData();
                saveGame(); // Create initial save file
            }
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Error loading game data", e);
            currentSaveData = new GameSaveData();
        }
    }

    public void setSelectedPet (String petId) {
        currentSaveData.setSelectedPetId(petId);
        saveGame();
    }

    public String getSelectedPet() {
        return currentSaveData.getSelectedPetId();
    }

    public void addScore (int score) {
        currentSaveData.addScore(score);
    }

    public int getTotalScore() {
        return currentSaveData.getTotalScore();
    }

    public void resetGame() {
        currentSaveData = new GameSaveData();
        saveGame();
    }
}
